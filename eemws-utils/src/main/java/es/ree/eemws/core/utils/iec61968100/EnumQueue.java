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
 * Queue possible values. So far only one value is possible, though for 
 * homogeneity it is implemented as an enumeration. 
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/12/2014
 */ 
public enum EnumQueue {

    /** Queue value. */
    NEXT("NEXT");  //$NON-NLS-1$
   
    /** Queue as string. */
    private String strVal;

    /**
     * Creates a new EnumQueue with the given text value.
     * @param str EnumQueue's text value.
     */
    EnumQueue(final String str) {
        strVal = str;
    }
    
    /**
     * Returns a <code>EnumQueue</code> from the given text. <code>null</code> if the given text doesn't describes any of
     * the elements.
     * @param text Text to retrieve the EnumType
     * @return EnumQueue from the given text. <code>null</code> if the given text doesn't describes any of the elements.
     */    
    public static EnumQueue fromString(final String text) {
        EnumQueue[] elemts = EnumQueue.values();
        EnumQueue retValue = null;
        for (EnumQueue elem : elemts) {
            if (elem.toString().equals(text)) {
                retValue = elem;
            }
        }

        return retValue;
    }
    
    /**
     * Returns the text code for the current queue value.
     * @return Text code for the current queue .
     */ 
    @Override
    public String toString() {
        return strVal;
    }

}
