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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.security.KeyException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;

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
 * @version 1.0 13/06/2014
 */
public final class KeyValueKeySelectorTest {

    /** Logger messages. */
    private final Logger logger = LoggerFactory.getLogger(KeyValueKeySelectorTest.class);

    /** Key info. */
    @Mock private KeyInfo mKeyInfo;

    /** Purpose. */
    @Mock private KeySelector.Purpose mPurpose;

    /** Algorithm method. */
    @Mock private AlgorithmMethod mMethod;

    /** XML crypto context. */
    @Mock private XMLCryptoContext mContext;

    /** X.509 Data. */
    @Mock private X509Data mX509Data;

    /** X.509 certificate. */
    @Mock private X509Certificate mX509Certificate;

    /** Public key. */
    @Mock private PublicKey mPublicKey;

    /** Public key. */
    @Mock private PublicKey mPublicKey2;

    /** Private key. */
    @Mock private KeyValue mKeyValue;

    /** Key selector. */
    private KeyValueKeySelector keyValueKeySelector;

    /** Key selector result. */
    private KeySelectorResult selectorResult;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        keyValueKeySelector = new KeyValueKeySelector();
    }

    /**
     * Test to verify X509.Certificate is null on creation.
     * @throws Exception in case of error.
     */
    @Test
    public void x509CertificateNullOnCreation() throws Exception {

        logger.debug("x509CertificateNullOnCreation");
        assertThat(keyValueKeySelector.getX509Certificate(), is(nullValue()));
    }

    /**
     * Test to verify X509.Certificate with null KeyInfo.
     * @throws Exception in case of error.
     */
    @Test(expected = KeySelectorException.class)
    public void selectWithNullKeyInfo() throws Exception {

        logger.debug("selectWithNullKeyInfo");

        try {

            keyValueKeySelector.select(null, mPurpose, mMethod, mContext);

        } catch (KeySelectorException e) {

            logger.debug("KeySelectorException message: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Test to verify X509.Certificate with KeyInfo and no key.
     * @throws Exception in case of error.
     */
    @Test(expected = KeySelectorException.class)
    public void selectWithKeyInfoNoKeys() throws Exception {

        logger.debug("selectWithKeyInfoNoKeys");

        try {

            keyValueKeySelector.select(mKeyInfo, mPurpose, mMethod, mContext);

        } catch (KeySelectorException e) {

            logger.debug("KeySelectorException message: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Test to verify X509.Certificate.
     * @throws Exception in case of error.
     */
    @Test
    public void selectWithX509Certificate() throws Exception {

        logger.debug("selectWithX509Certificate");

        when(mX509Certificate.getPublicKey()).thenReturn(mPublicKey);
        List<X509Certificate> listX509 = new ArrayList<X509Certificate>();
        listX509.add(mX509Certificate);
        when(mX509Data.getContent()).thenReturn(listX509);
        List<XMLStructure> listXS = new ArrayList<XMLStructure>();
        listXS.add(mX509Data);
        when(mKeyInfo.getContent()).thenReturn(listXS);

        selectorResult = keyValueKeySelector.select(mKeyInfo, mPurpose, mMethod, mContext);
        assertThat("Same PublicKey", mPublicKey, is(selectorResult.getKey()));
        assertThat("Different PublicKey", mPublicKey2, is(not(selectorResult.getKey())));
    }

    /**
     * Test to verify X509.Certificate with broken KeyValue.
     * @throws Exception in case of error.
     */
    @Test(expected = KeySelectorException.class)
    public void selectWithBrokenKeyValue() throws Exception {

        logger.debug("selectWithBrokenKeyValue");

        when(mKeyValue.getPublicKey()).thenThrow(new KeyException());
        List<XMLStructure> listXS = new ArrayList<XMLStructure>();
        listXS.add(mKeyValue);
        when(mKeyInfo.getContent()).thenReturn(listXS);

        AlgorithmMethod method = XMLSignatureFactory.getInstance().newSignatureMethod(SignatureMethod.DSA_SHA1, null);

        try {

            selectorResult = keyValueKeySelector.select(mKeyInfo, mPurpose, method, mContext);

        } catch (KeySelectorException e) {

            assertThat(e.getCause(), is(KeyException.class));
            throw e;
        }
    }

    /**
     * Test to verify X509.Certificate with KeyValue.
     * @throws Exception in case of error.
     */
    @Test
    public void selectWithKeyValue() throws Exception {

        logger.debug("selectWithKeyValue");

        when(mPublicKey.getAlgorithm()).thenReturn("DSA");
        when(mKeyValue.getPublicKey()).thenReturn(mPublicKey);
        List<XMLStructure> listXS = new ArrayList<XMLStructure>();
        listXS.add(mKeyValue);
        when(mKeyInfo.getContent()).thenReturn(listXS);

        AlgorithmMethod method = XMLSignatureFactory.getInstance().newSignatureMethod(SignatureMethod.DSA_SHA1, null);

        selectorResult = keyValueKeySelector.select(mKeyInfo, mPurpose, method, mContext);

        assertThat("Same PublicKey", mPublicKey, is(selectorResult.getKey()));
        assertThat("Different PublicKey", mPublicKey2, is(not(selectorResult.getKey())));
    }
}
