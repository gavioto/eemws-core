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


/**
 * Simple exception implementation to notify errors in the sign and validation process.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public class SignatureManagerException extends Exception {

    /** Serial ID. */
    private static final long serialVersionUID = -386928825860553754L;

    /**
     * Creates a new exception with reason text and cause exception.
     * @param reasonText Text that clarifies the reason of the exception.
     * @param cause Cause of the exception.
     */
    public SignatureManagerException(final String reasonText, final Exception cause) {

        super(reasonText, cause);
    }

    /**
     * Creates a new exception with the given text detail.
     * @param reasonText Text that clarifies the reason of the exception.
     */
    public SignatureManagerException(final String reasonText) {

        super(reasonText);
    }
}
