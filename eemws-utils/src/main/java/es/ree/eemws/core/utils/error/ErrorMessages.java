/*
 * Copyright 2016 Red Eléctrica de España, S.A.U.
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

package es.ree.eemws.core.utils.error;

import es.ree.eemws.core.utils.i18n.Messages;

/**
 * Error text detailed information.
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.1 10/01/2016
 * 
 */
public final class ErrorMessages {
    
    /** No noun expected. */
    public static final String NO_NOUN_EXPECTED = Messages.getString("NO_NOUN_EXPECTED"); //$NON-NLS-1$
    
    /** Message has no header. */
    public static final String NO_HEADER = Messages.getString("NO_HEADER"); //$NON-NLS-1$
    
    /**
     * Utility classes should have a private constructor.
     */
    private ErrorMessages() {
       
        /* This constructor must be empty. */
    }
}
