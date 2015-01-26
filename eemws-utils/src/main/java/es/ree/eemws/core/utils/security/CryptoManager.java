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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import es.ree.eemws.core.utils.messages.Messages;


/**
 * Utilities to encrypt / decrypt strings.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class CryptoManager {

    /** Algorithm. */
    private static final String ALGORITHM = "AES"; //$NON-NLS-1$

    /** Encode prefix. */
    private static final String ALGORITHM_PREFIX = "{" + ALGORITHM + "}"; //$NON-NLS-1$ //$NON-NLS-2$

    /** Name of the cipher. */
    private static final String CIPHER_NAME = "AES/ECB/PKCS5Padding"; //$NON-NLS-1$

    /**
     * Secret key that works as a seed.
     * XXX YOU MUST CHANGE THESE VALUES IN YOUR SYSTEM!.
     */
    private static final byte[] SECRET_KEY = {0x6b, 0x65, 0x65, 0x70, 0x74, 0x68, 0x69, 0x73, 0x73, 0x61, 0x66, 0x65, 0x70, 0x6c, 0x7a, 0x2e};

    /**
     * Constructor.
     */
    private CryptoManager() {

        /* This method should not be implemented. */
    }

    /**
     * Returns the encript value of the given string with a prefix.
     * @param value String to ve encripted.
     * @return The encript value with the prefix {AES}
     * @throws CryptoException If its not possible to encript the given string.
     */
    public static String encrypt(final String value) throws CryptoException {

        String retValue = null;

        try {

            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY, ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            retValue = ALGORITHM_PREFIX + DatatypeConverter.printBase64Binary(encrypted);

        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {

            throw new CryptoException(Messages.getString("SECURITY_UNABLE_TO_CIPHER"), e); //$NON-NLS-1$
        }

        return retValue;
    }

    /**
     * Returns the clear value of the given encripted string.
     * @param text Encripted string with the prefix {AES}
     * @return The clear value of the encripted string. If the given string
     * has no {AES} prefix, then the method will return the input stream without changes.
     * @throws CryptoException If it is not possible to decript the input string.
     */
    public static String decrypt(final String text) throws CryptoException {

        String retValue = null;

        try {

            /* No prefix?, return the same string. */
            if (text.indexOf(ALGORITHM_PREFIX) == -1) {

                retValue = text;

            } else {

                String textToDecode = text.substring(ALGORITHM_PREFIX.length());
                Cipher cipher = Cipher.getInstance(CIPHER_NAME);
                SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY, ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] bToDecript = DatatypeConverter.parseBase64Binary(textToDecode);
                retValue = new String(cipher.doFinal(bToDecript));
            }

        } catch (ArrayIndexOutOfBoundsException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {

            throw new CryptoException(Messages.getString("SECURITY_UNABLE_TO_DECRYPT"), e); //$NON-NLS-1$
        }

        return retValue;
    }
}
