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
package es.ree.eemws.core.utils.security;

import static org.junit.Assert.*;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.xml.crypto.KeySelectorException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Test class for KeyValueKeySelector.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/11/2014
 */
public final class X509UtilTest {

    /** Logger messages. */
    private final Logger logger = LoggerFactory.getLogger(KeyValueKeySelectorTest.class);
    
    /** X.509 certificate. */
    @Mock private X509Certificate mX509Certificate;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        setSecurityEnvironment();
    }

    /**
     * Test method for {@link es.ree.eemws.core.utils.security.X509Util#checkCertificate(java.security.cert.X509Certificate)}
     * with an invalid certificate.
     */
    @Test(expected = CertificateException.class)
    public void testCheckInvalidCertificateX509Certificate() throws Exception{
        logger.debug("testCheckInvalidCertificateX509Certificate");
        try {
            X509Util.checkCertificate(mX509Certificate);
        } catch (CertificateException e) {
            logger.debug("testCheckInvalidCertificateX509Certificate message: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Test method for {@link es.ree.eemws.core.utils.security.X509Util#checkCertificate(java.security.cert.X509Certificate[])}.
     */
    @Test
    public void testCheckCertificateX509CertificateArray() throws Exception{
        logger.debug("testCheckCertificateX509CertificateArray");
        KeyStore ks = KeyStore.getInstance("JKS");
        java.io.FileInputStream fis = null;
        try {
            fis = new java.io.FileInputStream(getClass().getClassLoader().getResource("test.jks").getFile());
            ks.load(fis, "test".toCharArray()); 
            Enumeration<String> aliases = ks.aliases();
            
            while (aliases.hasMoreElements()) {
                String alias = (String) aliases.nextElement();
                Certificate[] certs = ks.getCertificateChain(alias);
                logger.debug("Alias: {}, certChain size: {}", alias, certs.length);
                X509Certificate[] xcerts = new X509Certificate[certs.length];
                for (int i = 0; i < certs.length; i++) {
                    xcerts[i] = (X509Certificate) certs[i];
                }
                X509Util.checkCertificate(xcerts);
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
    
    /**
     * Sets the security environment using a test certificate.
     */
    private void setSecurityEnvironment() {

        System.setProperty("javax.net.ssl.keyStore", getClass().getClassLoader().getResource("test.jks").getFile());
        System.setProperty("javax.net.ssl.keyStoreType", "JKS");
        System.setProperty("javax.net.ssl.keyStorePassword", "test");
        System.setProperty("javax.net.ssl.trustStore", getClass().getClassLoader().getResource("test.jks").getFile());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("javax.net.ssl.trustStorePassword", "test");
    }
    
}
