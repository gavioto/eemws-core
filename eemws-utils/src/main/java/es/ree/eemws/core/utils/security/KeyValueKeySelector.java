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

import java.security.KeyException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;


/**
 * Finds and returns a key used in the signature <code>KeyInfo</code> object.
 * Supports DSA, RSA and X509 keys.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class KeyValueKeySelector extends KeySelector {

    /** Constant for algorithm DSA. */
    private static final String ALGORITHM_DSA = "DSA";

    /** Constant for algorithm RSA. */
    private static final String ALGORITHM_RSA = "RSA";

    /** Stores the signature's public key. */
    private SimpleKeySelectorResult sKeyResult = null;

    /**
     * Stores the x509 Certificate used in the signature.
     * Will be <code>null</code> if the signature uses RSA or DSA keys.
     */
    private X509Certificate x509Cert;

    /**
     * Returns the x509 certificate used in the signature.
     * @return x509 certificate used in the signature or <code>null</code> if the
     * signature used other kind of certificate (RSA, DSA).
     */
    public X509Certificate getX509Certificate() {
        return x509Cert;
    }

    @Override
    public KeySelectorResult select(final KeyInfo keyInfo, final KeySelector.Purpose purpose, final AlgorithmMethod method, final XMLCryptoContext context)
            throws KeySelectorException {

        if (keyInfo == null) {
            throw new KeySelectorException("Null KeyInfo object!");
        }

        List<?> list = keyInfo.getContent();
        int numInfo = list.size();

        for (int i = 0; sKeyResult == null && i < numInfo; i++) {
            XMLStructure xmlStructure = (XMLStructure) list.get(i);

            if (xmlStructure instanceof X509Data) {
                Iterator<?> x509DataListContent = ((X509Data) xmlStructure).getContent().iterator();
                while (sKeyResult == null && x509DataListContent.hasNext()) {
                    Object obj = x509DataListContent.next();
                    if (obj instanceof X509Certificate) {
                        x509Cert = (X509Certificate) obj;
                        sKeyResult = new SimpleKeySelectorResult(x509Cert.getPublicKey());
                    }
                }
            } else if (xmlStructure instanceof KeyValue) {
                try {
                    PublicKey pk = ((KeyValue) xmlStructure).getPublicKey();
                    String algURI = method.getAlgorithm();
                    String algName = pk.getAlgorithm();

                    /* Checks algorithm matches with method. */
                    if ((algName.equalsIgnoreCase(ALGORITHM_DSA) && algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1))
                            || (algName.equalsIgnoreCase(ALGORITHM_RSA) && algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1))) {

                        sKeyResult = new SimpleKeySelectorResult(pk);
                    }
                } catch (KeyException ke) {
                    throw new KeySelectorException(ke);
                }
            }
        }

        if (sKeyResult == null) {
            throw new KeySelectorException("No valid KeyValue element found!");
        }

        return sKeyResult;
    }
}
