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
package es.ree.eemws.core.utils.iec61968100;


/**
 * Message status.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public enum EnumMessageStatus {
    
    /** Status OK. */
    OK("OK"),  //$NON-NLS-1$
    
    /** Status FAILED. */
    FAILED("FAILED"); //$NON-NLS-1$
    
    /** Tag name (element) of the "Result" in IEC 61968-100. */
    public static final String ELEMENT_RESULT = "Result"; //$NON-NLS-1$
    
    /** Message status text. */
    private String status;
    
    /** Creates a new message status with the given text. 
     * @param str Text associated to this status.
     */
    EnumMessageStatus(final String str) {

        status = str;
    }

    /**
     * Gets the status text.
     * @return Status text.
     */
    public String getStatus() {

        return status;
    }

    /**
     * Returns a <code>EnumMessageStatus</code> from the given text. <code>null</code> if the given text doesn't describes any of
     * the elements.
     * @param text Text to retrieve the EnumType
     * @return EnumMessageStatus from the given text. <code>null</code> if the given text doesn't describes any of the elements.
     */
    public static EnumMessageStatus fromString(final String text) {
        EnumMessageStatus[] elemts = EnumMessageStatus.values();
        EnumMessageStatus retValue = null;
        for (EnumMessageStatus elem : elemts) {
            if (elem.toString().equals(text)) {
                retValue = elem;
            }
        }

        return retValue;
    }

}
