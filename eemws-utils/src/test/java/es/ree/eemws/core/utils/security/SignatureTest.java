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

import static org.hamcrest.CoreMatchers.anyOf;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import es.ree.eemws.core.utils.messages.Messages;


/**
 * Simple test cases.
 */
public final class SignatureTest  {

    /** Logger messages. */
    private final Logger logger = LoggerFactory.getLogger(SignatureTest.class);

	/** Test keystore type. */
	private static final String KEY_STORE_TYPE = "PKCS12";
	
	/** Test keystore file name. */
	private static final String KEY_STORE_FILE = "test.p12";
	
	/** Test keystore password. */
	private static final String KEY_STORE_PASSWORD = "test";
	
    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {

        setSecurityEnvironment();
    }

    /**
     * Simple test case, read an xml document, sign it, verify it.
     * @throws Exception in case of error.
     */
    @Test
    public void signatureTest1() throws Exception {

        Document doc = getDocumentFromFile("signature-test-1.xml");
        logger.debug("signatureTest1 - Signing Document...");
        SignatureManager.signDocument(doc);
        logger.debug("signatureTest1 - Verifying Document...");
        SignatureManager.verifyDocument(doc);
    }

    /**
     * Signature verification test (successful).
     * @throws Exception in case of error.
     */
    @Test
    public void verifyTest2() throws Exception {

        Document doc = getDocumentFromFile("verify-test-2.xml");
        logger.debug("verifyTest2 - Verifying Document...");
        SignatureManager.verifyDocument(doc);
    }

    /**
     * Signature verification test (fail).
     * In this case the certificate of the test is different from the
     * one used in the signature.
     * @throws Exception in case of error.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = SignatureManagerException.class)
    public void verifyTest3() throws Exception {

        try {

            Document doc = getDocumentFromFile("verify-test-3.xml");
            SignatureManager.verifyDocument(doc);

        } catch (SignatureManagerException ex) {

            logger.debug("verifyTest3 - Caught expected SignatureManagerException. Message: {}", ex.getMessage());
            assertThat(ex.getMessage(), anyOf(containsString(Messages.getString("SECURITY_SIGNATURE_VALIDATION_FAILED")),
                    containsString(Messages.getString("SECURITY_SIGNATURE_NO_TRUSTED_CERT"))));
            throw ex;
        }
    }

    /**
     * Signature verification test (fail).
     * The document was modified. Signature is ok, but the reference is not correct.
     * @throws Exception in case of error.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = SignatureManagerException.class)
    public void verifyTest4() throws Exception {

        try {

            Document doc = getDocumentFromFile("verify-test-4.xml");
            SignatureManager.verifyDocument(doc);

        } catch (SignatureManagerException ex) {

            logger.debug("verifyTest4 - Caught expected SignatureManagerException. Message: {}", ex.getMessage());
            assertThat(ex.getMessage(), anyOf(containsString(Messages.getString("SECURITY_SIGNATURE_VALIDATION_FAILED")),
                    containsString(Messages.getString("SECURITY_SIGNATURE_NO_TRUSTED_CERT"))));
            throw ex;
        }
    }

    /**
     * Try to sign a random (not standard) xml document.
     * @throws Exception in case of error.
     */
    @Test(expected = SignatureManagerException.class)
    public void signatureTest5() throws Exception {

        try {

            Document doc = getDocumentFromFile("signature-test-5.xml");
            SignatureManager.signDocument(doc);
            SignatureManager.verifyDocument(doc);

        } catch (SignatureManagerException ex) {

            assertThat(ex.getMessage(), containsString(Messages.getString("SECURITY_INVALID_DOCUMENT_NO_HEADER", "Header", "http://iec.ch/TC57/2011/schema/message")));
            throw ex;
        }
    }
   
    /**
     * Sets the security environment using a test certificate.
     */
    private void setSecurityEnvironment() {

        System.setProperty("javax.net.ssl.keyStore", getClass().getClassLoader().getResource(KEY_STORE_FILE).getFile());
        System.setProperty("javax.net.ssl.keyStoreType", KEY_STORE_TYPE);
        System.setProperty("javax.net.ssl.keyStorePassword", KEY_STORE_PASSWORD);
        System.setProperty("javax.net.ssl.trustStore", getClass().getClassLoader().getResource(KEY_STORE_FILE).getFile());
        System.setProperty("javax.net.ssl.trustStoreType", KEY_STORE_TYPE);
        System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);
    }

    /**
     * Utility method, returns a Document object given its file name.
     * @param fileName The file name of the document to be retrieved.
     * @return A Document object.
     * @throws Exception If the document is not an xml or cannot be read.
     */
    private Document getDocumentFromFile(final String fileName) throws Exception {

        /* Read XML document. */
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(getClass().getClassLoader().getResourceAsStream(fileName));
    }
}
