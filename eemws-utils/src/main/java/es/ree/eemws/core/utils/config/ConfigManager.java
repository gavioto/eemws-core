/*
 * Copyright 2014 Red Eléctrica de España, S.A.U.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation, version 3 of the license.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTIBIILTY or FITNESS FOR A PARTICULAR PURPOSE. See GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program. If not, see
 * http://www.gnu.org/licenses/.
 *
 * Any redistribution and/or modification of this program has to make
 * reference to Red Eléctrica de España, S.A.U. as the copyright owner of
 * the program.
 */
package es.ree.eemws.core.utils.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import es.ree.eemws.core.utils.file.FileUtil;
import es.ree.eemws.core.utils.i18n.Messages;
import es.ree.eemws.core.utils.security.CryptoException;
import es.ree.eemws.core.utils.security.CryptoManager;


/**
 * Class to manage configuration files.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class ConfigManager {

    /** Properties with the configuration values. */
    private Properties config = new Properties();

    /** Standard key for java key store. */
    private static final String KEY_STORE = "javax.net.ssl.keyStore"; //$NON-NLS-1$

    /** Standard key for java key store password. */
    private static final String KEY_STORE_PASSWORD = "javax.net.ssl.keyStorePassword"; //$NON-NLS-1$

    /** Standard key for java key store type. */
    private static final String KEY_STORE_TYPE = "javax.net.ssl.keyStoreType"; //$NON-NLS-1$

    /** Standard key for java trust store file. */
    private static final String KEY_TRUST_STORE = "javax.net.ssl.trustStore"; //$NON-NLS-1$

    /** Standard key for java trust store password. */
    private static final String KEY_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword"; //$NON-NLS-1$

    /** Standard key for java trust store type. */
    private static final String KEY_TRUST_STORE_TYPE = "javax.net.ssl.trustStoreType"; //$NON-NLS-1$

    /** Default key store type. */
    private static final String DEFAULT_KEY_STORE_TYPE = "PKCS12"; //$NON-NLS-1$

    /** Default trust store type. */
    private static final String DEFAULT_TRUST_STORE_TYPE = "JKS"; //$NON-NLS-1$

    /** Default key store password. */
    private static final String DEFAULT_KEY_STORE_PASSWORD = ""; //$NON-NLS-1$

    /** System's configuration keys, these values will be setup as java system properties. */
    private static final String[] SYSTEM_KEYS = {KEY_TRUST_STORE, KEY_TRUST_STORE_PASSWORD, KEY_TRUST_STORE_TYPE, KEY_STORE, KEY_STORE_PASSWORD, KEY_STORE_TYPE,
        "https.proxyHost", //$NON-NLS-1$
        "https.proxyPort", //$NON-NLS-1$
        "https.proxyUser", //$NON-NLS-1$
    "https.proxyPassword" }; //$NON-NLS-1$

    /** Token to recognize password keys. */
    private static final String PASSWORD_TOKEN = "PASSWORD"; //$NON-NLS-1$

    /** New line character. */
    private static final String NEW_LINE = "\n"; //$NON-NLS-1$

    /** Constant for "=". */
    private static final String EQUALS = "="; //$NON-NLS-1$

    /**
     * Reads a configuration file that must be accessible from the classpath.
     * @param configFileName Path of the configuration file
     * @return Properties with the configuration values.
     * @throws ConfigException If the given file cannot be read of if there is a miss configuration.
     */
    public Properties readConfigFile(final String configFileName) throws ConfigException {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try (InputStream isProps = loader.getResourceAsStream(configFileName);) {

            if (isProps == null) {

                throw new ConfigException(Messages.getString("CONFIG_UNABLE_TO_READ_CONFIG_FILE", configFileName)); //$NON-NLS-1$
            }
            config = new Properties();
            config.load(isProps);
            
            boolean shouldCypherFile = clearPasswords();
            loadAndCheckSecurityConfig();
            setSystem();

            if (shouldCypherFile) {

                File configFile = new File(FileUtil.getFullPathOfResoruce(configFileName));
                if (configFile.canWrite()) {

                    cypherFile(configFile);
                }
            }

        } catch (IOException e) {

            throw new ConfigException(Messages.getString("CONFIG_UNABLE_TO_READ_CONFIG_FILE", configFileName), e); //$NON-NLS-1$
        }

        return config;
    }

    /**
     * Returns the value of the given configuration key.
     * @param key Key.
     * @return Value of the key, <code>null</code> if there is no such key in the configuration set.
     */
    public String getValue(final String key) {

        return config.getProperty(key);
    }

    /**
     * Returns the value of the given configuration key. If there is no configuration value for the given key, then the
     * second String parameter is returned.
     * @param key Key.
     * @param defaultValue Default value if the key is not set.
     * @return Value of the key, defaultValue if there is no such key in the configuration set.
     */
    public String getValue(final String key, final String defaultValue) {

        return config.getProperty(key, defaultValue);
    }

    /**
     * Rewrites the configuration file changing all the password to its cypher value.
     * This method is called only if the read configuration has clear password values.
     * @param configFile The configuration file reference.
     * @throws ConfigException If any value cannot be cypher.
     */
    private void cypherFile(final File configFile) throws ConfigException {

        String failedKey = ""; //$NON-NLS-1$
        try {

            String fullFilePath = configFile.getAbsolutePath();
            String[] fullFileConfig = FileUtil.read(fullFilePath).split(NEW_LINE);
            StringBuilder bul = new StringBuilder();

            for (String line : fullFileConfig) {

                if (!line.trim().startsWith("#") && line.toUpperCase().indexOf(PASSWORD_TOKEN) != -1 && line.indexOf(EQUALS) != -1) { //$NON-NLS-1$

                    String[] pair = line.split(EQUALS);
                    String key = pair[0];
                    
                    /* Ciphers only values which its key has the word "password". Does not cipher keys which its value has the word "password". */
                    if (key.toUpperCase().indexOf(PASSWORD_TOKEN) != -1 && config.containsKey(key)) {

                        String value = pair[1].trim();
                        String configValue = config.getProperty(key);

                        if (configValue.equals(value)) {
                            failedKey = key;
                            value = CryptoManager.encrypt(value);
                            line = key + EQUALS + value;
                        }
                    }
                }

                bul.append(line);
                bul.append(NEW_LINE);
            }

            FileUtil.write(fullFilePath, bul.toString());

        } catch (CryptoException e) {

            throw new ConfigException(Messages.getString("CONFIG_UNABLE_TO_CIPHER_PASSWORD", failedKey), e); //$NON-NLS-1$

        } catch (IOException e) {

            throw new ConfigException(Messages.getString("CONFIG_UNABLE_TO_SAVE_CONFIG"), e); //$NON-NLS-1$
        }
    }

    /**
     * Goes through the keys and store in clear text those values whose key contains the word "password".
     * @return <code>true</code> if at least one of the password values are already as clear text.
     * @throws ConfigException If one of the encrypted keys cannot be decrypted.
     */
    private boolean clearPasswords() throws ConfigException {

        boolean hasPasswordEntryClear = false;
        String failedKey = ""; //$NON-NLS-1$
        try {

            Set<Entry<Object, Object>> configKeysAndValues = config.entrySet();
            for (Entry<Object, Object> keyAndValue : configKeysAndValues) {

                String key = (String) keyAndValue.getKey();
                String value = (String) keyAndValue.getValue();
                if (key.toUpperCase().indexOf(PASSWORD_TOKEN) != -1) {

                    failedKey = key;
                    String clearValue = CryptoManager.decrypt(value);
                    if (clearValue.equals(value)) {

                        hasPasswordEntryClear = true;
                    }
                    keyAndValue.setValue(clearValue);
                }
            }

        } catch (CryptoException e) {

            throw new ConfigException(Messages.getString("CONFIG_UNABLE_TO_DECRYPT_PASSWORD", failedKey), e); //$NON-NLS-1$
        }

        return hasPasswordEntryClear;
    }

    /**
     * Tries to open keystore, if it is defined.
     * @throws ConfigException If the key store is misconfigurated.
     */
    private void loadAndCheckSecurityConfig() throws ConfigException {

        if (config.getProperty(KEY_STORE_TYPE) == null) {
            config.setProperty(KEY_STORE_TYPE, DEFAULT_KEY_STORE_TYPE);
        }

        if (config.getProperty(KEY_TRUST_STORE_PASSWORD) == null) {
            config.setProperty(KEY_TRUST_STORE_PASSWORD, DEFAULT_KEY_STORE_PASSWORD);
        }

        if (config.getProperty(KEY_TRUST_STORE_TYPE) == null) {
            config.setProperty(KEY_TRUST_STORE_TYPE, DEFAULT_TRUST_STORE_TYPE);
        }

        loadAndCheckSecurityConfig(config.getProperty(KEY_STORE), config.getProperty(KEY_STORE_PASSWORD), config.getProperty(KEY_STORE_TYPE));
    }

    /**
     * Opens a key store using the given parameters.
     * @param certFile Key store file. Can be <code>null</code>.
     * @param passwd Key store password.
     * @param type Key store type.
     * @throws ConfigException If the given keystore cannot be loaded.
     */
    @SuppressWarnings("static-method")
    private void loadAndCheckSecurityConfig(final String certFile, final String passwd, final String type) throws ConfigException {

        if (certFile != null) {

            try (InputStream isCertFile = new FileInputStream(certFile)) {

                File f = new File(certFile);
                if (!f.canRead()) {

                    throw new ConfigException(Messages.getString("CONFIG_UNABLE_TO_READ_KEY_STORE", certFile)); //$NON-NLS-1$
                }

                KeyStore ks = KeyStore.getInstance(type);
                ks.load(isCertFile, passwd.toCharArray());

            } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {

                throw new ConfigException(Messages.getString("CONFIG_UNABLE_TO_LOAD_KEY_STORE", certFile), e); //$NON-NLS-1$
            }
        }
    }

    /**
     * Sets the system configuration values.
     */
    private void setSystem() {

        String value;

        for (String key : SYSTEM_KEYS) {

            value = config.getProperty(key);
            if (value != null) {

                System.setProperty(key, value);
            }
        }
    }
}
