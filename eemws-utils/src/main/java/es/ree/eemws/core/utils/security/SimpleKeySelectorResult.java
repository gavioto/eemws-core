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

import java.security.Key;
import java.security.PublicKey;
import javax.xml.crypto.KeySelectorResult;


/**
 * Implements a simple <code>KeySelectorResult</code>. It works as a key wrapper.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class SimpleKeySelectorResult implements KeySelectorResult {

    /** Public key to verify signature. */
    private PublicKey publicKey;

    /**
     * Creates a new instance.
     * @param pk Public key used in the signature.
     */
    SimpleKeySelectorResult(final PublicKey pk) {

        publicKey = pk;
    }

    /**
     * Returns the public key used in the signature.
     * @return Public key used in the signature.
     */
    @Override
    public Key getKey() {

        return publicKey;
    }
}
