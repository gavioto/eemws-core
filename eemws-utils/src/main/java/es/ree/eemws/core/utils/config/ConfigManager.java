/*
 * Copyright 2014 Red El�ctrica de Espa�a, S.A.U.
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
 * reference to Red El�ctrica de Espa�a, S.A.U. as the copyright owner of
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
import es.ree.eemws.core.utils.security.CryptoException;
import es.ree.eemws.core.utils.security.CryptoManager;


/**
 * Class to manage configuration files.
 *
 * @author Red El�ctrica de Espa�a S.A.U.
 * @version 1.0 13/06/2014
 */
public final class ConfigManager {

    /** Properties with the configuration values. */
    private Properties config = new Properties();

    /** Standard key for java key store. */
    private static final String KEY_STORE = "javax.net.ssl.keyStore";

    /** Standard key for java key store password. */
    private static final String KEY_STORE_PASSWORD = "javax.net.ssl.keyStorePassword";

    /** Standard key for java key store type. */
    private static final String KEY_STORE_TYPE = "javax.net.ssl.keyStoreType";

    /** Standard key for java trust store file. */
    private static final String KEY_TRUST_STORE = "javax.net.ssl.trustStore";

    /** Standard key for java trust store password. */
    private static final String KEY_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";

    /** Standard key for java trust store type. */
    private static final String KEY_TRUST_STORE_TYPE = "javax.net.ssl.trustStoreType";

    /** Default key store type. */
    private static final String DEFAULT_KEY_STORE_TYPE = "PKCS12";

    /** Default trust store type. */
    private static final String DEFAULT_TRUST_STORE_TYPE = "JKS";

    /** Default key store password. */
    private static final String DEFAULT_KEY_STORE_PASSWORD = "";

    /** System's configuration keys, these values will be setup as java system properties. */
    private static final String[] SYSTEM_KEYS = {KEY_TRUST_STORE, KEY_TRUST_STORE_PASSWORD, KEY_TRUST_STORE_TYPE, KEY_STORE, KEY_STORE_PASSWORD, KEY_STORE_TYPE,
        "https.proxyHost", "https.proxyPort", "https.proxyUser", "https.proxyPassword" };

    /** Token to recognize password keys. */
    private static final String PASSWORD_TOKEN = "PASSWORD";

    /** New line character. */
    private static final String NEW_LINE = "\n";

    /** Constant for "=". */
    private static final String EQUALS = "=";

    /**
     * This method read a configuration file that must be accessible from the classpath.
     * @param configFileName Path of the configuration file
     * @return Properties with the configuration values.
     * @throws ConfigException If the given file cannot be read of if there is a miss configuration.
     */
    public Properties readConfigFile(final String configFileName) throws ConfigException {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try (InputStream isProps = loader.getResourceAsStream(configFileName);) {

            if (isProps == null) {

                throw new ConfigException("Unable to read the given config file [" + configFileName + "]");
            } else {
                config = new Properties();
                config.load(isProps);
                isProps.close();

                boolean shouldCypherFile = clearPasswords();
                loadAndCheckSecurityConfig();
                setSystem();

                if (shouldCypherFile) {

                    File configFile = new File(loader.getResource(configFileName).getFile());
                    if (configFile.canWrite()) {

                        cypherFile(configFile);
                    }
                }
            }
        } catch (IOException e) {

            throw new ConfigException("Unable to read the given config file [" + configFileName + "]", e);
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

        String failedKey = "";
        try {

            String fullFilePath = configFile.getAbsolutePath();
            String[] fullFileConfig = FileUtil.read(fullFilePath).split(NEW_LINE);
            StringBuilder bul = new StringBuilder();

            for (String line : fullFileConfig) {

                if (!line.trim().startsWith("#") && line.toUpperCase().indexOf(PASSWORD_TOKEN) != -1 && line.indexOf(EQUALS) != -1) {

                    String[] pair = line.split(EQUALS);
                    String key = pair[0];
                    if (config.containsKey(key)) {

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

            throw new ConfigException("Unable to cipher the key [" + failedKey + "]", e);

        } catch (IOException e) {

            throw new ConfigException("Unable to write the configuration file", e);
        }
    }

    /**
     * Goes through the keys and store in clear text those values whose key contains the word "password".
     * @return <code>true</code> if at least one of the password values are already as clear text.
     * @throws ConfigException If one of the encrypted keys cannot be decrypted.
     */
    private boolean clearPasswords() throws ConfigException {

        boolean hasPasswordEntryClear = false;
        String failedKey = "";
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

            throw new ConfigException("Unable to decrypt value for key [" + failedKey + "]. Edit the configuration file and set its value as clear text.");
        }

        return hasPasswordEntryClear;
    }

    /**
     * Tries to open keystore, if it is defined.
     * @throws ConfigException If the key store is misconfigurated.
     */
    private void loadAndCheckSecurityConfig() throws ConfigException {

        if (config.getProperty(KEY_STORE_PASSWORD) == null) {
            config.setProperty(KEY_STORE_PASSWORD, DEFAULT_KEY_STORE_PASSWORD);
        }

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
     * Open a key store using the given parameters.
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

                    throw new ConfigException("Unable to read the keystore [" + certFile + "]");
                }

                KeyStore ks = KeyStore.getInstance(type);
                ks.load(isCertFile, passwd.toCharArray());

            } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {

                throw new ConfigException("Unable to load the keystore [" + certFile + "]. Check keystore password and keystore type.");
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
