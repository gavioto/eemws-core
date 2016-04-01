/*
 * Copyright 2015 Red Eléctrica de España, S.A.U.
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

/**
 * Implements an exception to notify problems with the signature syntaxis.
 *  
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 21/01/2015
 */
public class SignatureSyntaxException extends SignatureManagerException {
    
    /** Serial UID. */
    private static final long serialVersionUID = 3269437989541029868L;

    /**
     * Constructor. Creates a new Signature Syntax Exception with the given text detail and cause.
     * @param reasonText Error message with details.
     * @param cause Caused exception.
     */
    public SignatureSyntaxException(final String reasonText, final Exception cause) {
        super(reasonText, cause);
    }
}

