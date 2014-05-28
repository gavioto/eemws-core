package es.ree.eemws.core.utils.security;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.hamcrest.CoreMatchers.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * Simple test cases.
 */
public class SignatureTest  {
	
	final Logger logger = LoggerFactory.getLogger(SignatureTest.class);
	
	
	 @Before
	 public void setup(){
		setSecurityEnvironment();
	 }
	
	/**
	 * Simple test case, read an xml document, sign it, verify it.
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
	 */
	@Test
	public void verifyTest2() throws Exception{
		Document doc = getDocumentFromFile("verify-test-2.xml");
		logger.debug("verifyTest2 - Verifying Document...");
		SignatureManager.verifyDocument(doc);
	}
	
	/**
	 * Signature verification test (fail).
	 * In this case the certificate of the test is different from the
	 * one used in the signature.
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = SignatureManagerException.class)
	public void verifyTest3() throws Exception {
		try {
			Document doc = getDocumentFromFile("verify-test-3.xml");
			SignatureManager.verifyDocument(doc);
		} catch (SignatureManagerException ex) {
			logger.debug("verifyTest3 - Caught expected SignatureManagerException. Message: {}", ex.getMessage());
			assertThat(ex.getMessage(), anyOf(containsString("Signature validation failed."),
					containsString("certificate signature is not trusted")));
			throw ex;			
		}
	}
	

	/**
	 * Signature verification test (fail).
	 * The document was modified. Signature is ok, but the reference is not correct.
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = SignatureManagerException.class)
	public void verifyTest4() throws Exception {
		try {
			Document doc = getDocumentFromFile("verify-test-4.xml");
			SignatureManager.verifyDocument(doc);
		} catch (SignatureManagerException ex) {
			logger.debug("verifyTest4 - Caught expected SignatureManagerException. Message: {}", ex.getMessage());
			assertThat(ex.getMessage(), anyOf(containsString("Signature validation failed."),
					containsString("certificate signature is not trusted")));
			throw ex;
		}
	}
	
	
	/**
	 * Try to sign a random (not standard) xml document.
	 */
	@Test(expected = SignatureManagerException.class)
	public void signatureTest5() throws Exception{
		try {
			Document doc = getDocumentFromFile("signature-test-5.xml");
			SignatureManager.signDocument(doc);
			SignatureManager.verifyDocument(doc);
		} catch (SignatureManagerException ex) {
			assertThat(ex.getMessage(), containsString("Invalid document. The given document has no [Header:http://iec.ch/TC57/2011/schema/message] tag to place the signature."));
			throw ex;
		}
	}
	
	/**
	 * Sets the security enviroment using a test certificate.
	 */
	private void setSecurityEnvironment() {
		System.setProperty("javax.net.ssl.keyStore", getClass().getClassLoader().getResource("test.jks").getFile());
		System.setProperty("javax.net.ssl.keyStoreType", "JKS");
		System.setProperty("javax.net.ssl.keyStorePassword", "test");
		System.setProperty("javax.net.ssl.trustStore", getClass().getClassLoader().getResource("test.jks").getFile());
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		System.setProperty("javax.net.ssl.trustStorePassword", "test");
	}
	
	/**
	 * Utility method, returns a Document object given its file name.
	 * @param fileName The file name of the document to be retrieved.
	 * @return A Document object.
	 * @throws Exception If the document is not an xml or cannot be read.
	 */
	private Document getDocumentFromFile(final String fileName) throws Exception {
		/* Read xml document. */
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(getClass().getClassLoader().getResourceAsStream(fileName));

	}
}