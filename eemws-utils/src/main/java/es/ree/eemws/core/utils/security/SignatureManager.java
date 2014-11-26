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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.ree.eemws.core.utils.security.SignatureVerificationException.SignatureVerificationExceptionDetails;
import es.ree.eemws.core.utils.xml.XMLUtil;

/**
 * Simple class to deal with xml signature.
 * The signature can be invoked with xml as String or as a Document.
 * If no certitifcate is provided, the class will use the default java ssl key store certificate.
 * The validation can be also invoked with xml as String or as a Document.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class SignatureManager {

    /** Signature URI ("" = means the whole document). */
    private static final String SIGNATURE_URI = "";

    /** The type of the XML processing mechanism and representation. */
    private static final String SIGNATURE_FACTORY_TYPE = "DOM";

    /** Digest method. */
    private static final String DIGEST_METHOD = DigestMethod.SHA1;

    /** Canonicalization method. */
    private static final String CANONICALIZATION_METHOD = CanonicalizationMethod.INCLUSIVE;

    /** Signature method. */
    private static final String SIGNATURE_METHOD = SignatureMethod.RSA_SHA1;

    /** Transform method. */
    private static final String TRANSFORM = Transform.ENVELOPED;

    /** Tag where the signature result is going to be placed. */
    private static final String HEADER_TAG = "Header";

    /** Name space of the tag where the signature is going to be placed. */
    private static final String HEADER_NAME_SPACE = "http://iec.ch/TC57/2011/schema/message";

    /** Java parameter name to set a key store type. */
    private static final String SYSTEM_KEY_STORE_TYPE = "javax.net.ssl.keyStoreType";

    /** Default user's key store type. */
    private static final String DEFAULT_KEY_STORE_TYPE = "PKCS12";

    /** Java parameter name to set the key store password. */
    private static final String SYSTEM_KEY_STORE_PASSWORD = "javax.net.ssl.keyStorePassword";

    /** Default user's key store password. */
    private static final String DEFAULT_KEY_STORE_PASSWORD = "";

    /** Java parameter name to set a key store file. */
    private static final String SYSTEM_KEY_STORE_FILE = "javax.net.ssl.keyStore";

    /** Signature tag. */
    private static final String SIGNATURE_TAG = "Signature";

    /** Date format for error details. */
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Constructor.
     */
    private SignatureManager() {

        /* This method should not be implemented. */
    }

    /**
     * Verifies the signature of the given signed document expressed as String (StringBuilder).
     * @param msgAsString The document to be validated.
     * @return X509 Key used in signature. <code>null</code> if other kind of certificate was used (RSA, DSA).
     * @throws SignatureVerificationException If the document cannot be validated or if its signature is invalid.
     * @see #verifyString(StringBuilder)
     */
    public static X509Certificate verifyString(final StringBuilder msgAsString) throws SignatureVerificationException {

        try {

            return verifyDocument(XMLUtil.string2Document(msgAsString));

        } catch (ParserConfigurationException | SAXException | IOException e) {

            throw new SignatureVerificationException("The given message seems to be an invalid XML", e);
        }
    }

    /**
     * Verifies the signature of the given signed document.
     * @param msgAsDocument The document to be validated.
     * @return X509 Key used in signature. <code>null</code> if other kind of certificate was used (RSA, DSA).
     * @throws SignatureVerificationException If the document cannot be validated or if its signature is invalid.
     * @see #verifyString(StringBuilder)
     */
    public static X509Certificate verifyDocument(final Document msgAsDocument) throws SignatureVerificationException  {

        X509Certificate x509 = null;
        try {

            Node signatureNode = null;
            NodeList nl = msgAsDocument.getElementsByTagNameNS(XMLSignature.XMLNS, SIGNATURE_TAG);
            if (nl.getLength() == 1) {

                signatureNode = nl.item(0);

            } else {

                throw new SignatureVerificationException("Invalid document. The given document has no [" + SIGNATURE_TAG + ":" + XMLSignature.XMLNS + "] tag.");
            }

            XMLSignatureFactory fac = XMLSignatureFactory.getInstance(SIGNATURE_FACTORY_TYPE);
            KeyValueKeySelector keySelector = new KeyValueKeySelector();
            DOMValidateContext valContext = new DOMValidateContext(keySelector, signatureNode);
            XMLSignature signature = fac.unmarshalXMLSignature(valContext);
            boolean coreValidity = signature.validate(valContext);
            boolean certValidity = true;

            x509 = keySelector.getX509Certificate();

            String msgError = "Signature validation failed.";

            if (x509 != null) {
                try {
                    X509Util.checkCertificate(x509);

                } catch (CertificateNotYetValidException | CertificateExpiredException e) {
                    certValidity = false;
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                    msgError = "The certificate signature is not valid [Today=" + sdf.format(new Date())
                            + "][" + sdf.format(x509.getNotBefore())   + " - " + sdf.format(x509.getNotAfter()) + "]";
                } catch (CertificateException e) {
                    certValidity = false;
                    msgError = "The certificate signature is not trusted";
                }
            }


            if (!coreValidity || !certValidity) {
                SignatureVerificationException sve = new SignatureVerificationException(msgError);

                SignatureVerificationExceptionDetails details = sve.getDetails();
                details.setSignatureValid(signature.getSignatureValue().validate(valContext));
                details.setCertificateValid(certValidity);
                details.setSignatureCertificate(x509);
                
                Iterator<?> iter = signature.getSignedInfo().getReferences().iterator();
                while (iter.hasNext()) {
                	Reference ref = (Reference) iter.next();
                    Boolean refValid = Boolean.valueOf((ref.validate(valContext)));
                    String calculated = DatatypeConverter.printBase64Binary(ref.getCalculatedDigestValue());
                    String provided = DatatypeConverter.printBase64Binary(ref.getDigestValue());
                    details.addReferenceStatus(refValid, calculated, provided);
                    
                }

                throw sve;
            }

        } catch (XMLSignatureException | MarshalException e) {

            throw new SignatureVerificationException("Unable to verify signature", e);
        }

        return x509;
    }

    /**
     * Signs the given xml document usign the given private key and certificate.
     * @param msgAsDocument The document to be signed, the result of the process will be returned in this parameter.
     * @param privateKey The private key to be used for signature.
     * @param cert The certificate to be used for signature.
     * @throws SignatureManagerException If it's impossible to sign the document.
     * @see #signString(StringBuilder, RSAPrivateKey, X509Certificate)
     */
    public static void signDocument(final Document msgAsDocument, final RSAPrivateKey privateKey, final X509Certificate cert) throws SignatureManagerException {
    	signDocument(msgAsDocument, (PrivateKey) privateKey, cert);
    }

    /**
     * Signs the given xml document usign the default keystore.
     * @param msgAsDocument The document to be signed, the result of the process will be returned in this parameter.
     * @throws SignatureManagerException If it's impossible to sign the document.
     * @see #signString(StringBuilder)
     */
    public static void signDocument(final Document msgAsDocument) throws SignatureManagerException {

        RSAPrivateKey privateKey = null;
        X509Certificate certificate = null;

        String keyStoreFile = System.getProperty(SYSTEM_KEY_STORE_FILE);
        if (keyStoreFile == null) {

            throw new SignatureManagerException("The system key store is not defined. Set the system property [" + SYSTEM_KEY_STORE_FILE + "]");
        }

        String keyStorePasswd = System.getProperty(SYSTEM_KEY_STORE_PASSWORD, DEFAULT_KEY_STORE_PASSWORD);
        String keyStoreType = System.getProperty(SYSTEM_KEY_STORE_TYPE, DEFAULT_KEY_STORE_TYPE);

        try (InputStream keystoreInputStream = new FileInputStream(keyStoreFile)) {

            KeyStore ks = KeyStore.getInstance(keyStoreType);
            ks.load(keystoreInputStream, keyStorePasswd.toCharArray());

            Enumeration<String> keyAlias = ks.aliases();
            String entryAlias = null;
            boolean okAlias = false;

            while (!okAlias && keyAlias.hasMoreElements()) {

                try {

                    entryAlias = keyAlias.nextElement();
                    privateKey = (RSAPrivateKey) ks.getKey(entryAlias, keyStorePasswd.toCharArray());
                    certificate = (X509Certificate) ks.getCertificate(entryAlias);
                    certificate.checkValidity();
                    okAlias = true;

                } catch (CertificateException | UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {

                    okAlias = false;
                }
            }

            if (!okAlias) {

                throw new SignatureManagerException("Unable to find a valid certificate in the system key store. Check system parameters.");
            }

        } catch (FileNotFoundException e) {

            throw new SignatureManagerException("Unable to read the specified system key store [" + keyStoreFile + "]. Check system parameter [" + SYSTEM_KEY_STORE_FILE
                    + "], check file read permission.", e);

        } catch (IOException | CertificateException | NoSuchAlgorithmException e) {

            throw new SignatureManagerException("Unable to load  the specified system key store [" + keyStoreFile + "]. Check parameters [" + SYSTEM_KEY_STORE_TYPE + ", " + SYSTEM_KEY_STORE_PASSWORD
                    + "]", e);

        } catch (KeyStoreException e) {

            throw new SignatureManagerException("Unable to get an instance of the specified key store type [" + keyStoreType + "]. Check parameter [" + SYSTEM_KEY_STORE_TYPE + "]", e);
        }

        signDocument(msgAsDocument, privateKey, certificate);
    }

    /**
     * Signs the given xml document expressed as String (StringBuilder) usign the given private key and certificate.
     * @param msgAsString The document to be signed, the result of the process will be returned in this parameter.
     * @param privateKey The private key to be used for signature.
     * @param cert The certificate to be used for signature.
     * @throws SignatureManagerException If it's impossible to sign the document.
     * @see #signDocument(Document, RSAPrivateKey, X509Certificate)
     */
    public static void signString(final StringBuilder msgAsString, final RSAPrivateKey privateKey, final X509Certificate cert) throws SignatureManagerException {

        try {

            Document doc = XMLUtil.string2Document(msgAsString);
            signDocument(doc, privateKey, cert);

            msgAsString.setLength(0);
            msgAsString.append(XMLUtil.document2String(doc));
            msgAsString.trimToSize();

        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {

            throw new SignatureManagerException("The given message seems to be an invalid XML", e);
        }
    }

    /**
     * Signs the given xml document expressed as String (StringBuilder) using the default keystore.
     * @param msgAsString The document to be signed, the result of the process will be returned in this parameter.
     * @throws SignatureManagerException If it's impossible to sign the document.
     * @see #signDocument(Document)
     */
    public static void signString(final StringBuilder msgAsString) throws SignatureManagerException {

        try {

            Document doc = XMLUtil.string2Document(msgAsString);
            signDocument(doc);
            msgAsString.setLength(0);
            msgAsString.append(XMLUtil.document2String(doc));
            msgAsString.trimToSize();

        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {

            throw new SignatureManagerException("The given message seems to an invalid XML", e);
        }
    }

    /**
     * Signs the given xml document expressed as String (StringBuilder) usign the given private key and certificate.
     * @param msgAsString The document to be signed, the result of the process will be returned in this parameter.
     * @param privateKey The private key to be used for signature.
     * @param cert The certificate to be used for signature.
     * @throws SignatureManagerException If it's impossible to sign the document.
     * @see #signDocument(Document, RSAPrivateKey, X509Certificate)
     */
    public static void signString(final StringBuilder msgAsString, final PrivateKey privateKey, final X509Certificate cert)
            throws SignatureManagerException {

        try {

            Document doc = XMLUtil.string2Document(msgAsString);
            signDocument(doc, privateKey, cert);

            msgAsString.setLength(0);
            msgAsString.append(XMLUtil.document2String(doc));
            msgAsString.trimToSize();

        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {

            throw new SignatureManagerException("The given message seems to be an invalid XML", e);
        }
    }

    /**
     * Signs the given xml document usign the given private key and certificate.
     * @param msgAsDocument The document to be signed, the result of the process will be returned in this parameter.
     * @param privateKey The private key to be used for signature.
     * @param cert The certificate to be used for signature.
     * @throws SignatureManagerException If it's impossible to sign the document.
     * @see #signString(StringBuilder, RSAPrivateKey, X509Certificate)
     */
    public static void signDocument(final Document msgAsDocument, final PrivateKey privateKey, final X509Certificate cert) throws SignatureManagerException {

        try {

            XMLSignatureFactory fac = XMLSignatureFactory.getInstance(SIGNATURE_FACTORY_TYPE);

            Reference ref = fac.newReference(SIGNATURE_URI, fac.newDigestMethod(DIGEST_METHOD, null), Collections.singletonList(fac.newTransform(TRANSFORM, (TransformParameterSpec) null)), null, null);
            SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CANONICALIZATION_METHOD, (C14NMethodParameterSpec) null), fac.newSignatureMethod(SIGNATURE_METHOD, null), Collections.singletonList(ref));

            Node headerNode = null;
            NodeList nl = msgAsDocument.getElementsByTagNameNS(HEADER_NAME_SPACE, HEADER_TAG);
            if (nl.getLength() == 1) {

                headerNode = nl.item(0);

            } else {

                throw new SignatureManagerException("Invalid document. The given document has no [" + HEADER_TAG + ":" + HEADER_NAME_SPACE + "] tag to place the signature.");
            }

            DOMSignContext dsc = new DOMSignContext(privateKey, headerNode);

            KeyInfoFactory keyInfoFactory = fac.getKeyInfoFactory();
            List<Object> x509Content = new ArrayList<>();
            x509Content.add(keyInfoFactory.newX509IssuerSerial(cert.getIssuerDN().getName(), cert.getSerialNumber()));
            x509Content.add(cert.getSubjectX500Principal().getName());
            x509Content.add(cert);
            X509Data xd = keyInfoFactory.newX509Data(x509Content);

            KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(xd));

            XMLSignature signature = fac.newXMLSignature(si, keyInfo);
            signature.sign(dsc);

        } catch (GeneralSecurityException e) {

            throw new SignatureManagerException("Invalid signature algorithm / parameters.", e);

        } catch (MarshalException | XMLSignatureException e) {

            throw new SignatureManagerException("Unable to sign the given document. Check document and exception details.", e);
        }
    }
}
