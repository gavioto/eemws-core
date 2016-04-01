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
 * Nouns values.
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/12/2014
 */
public enum EnumNoun {

    /** ANY noun for get and put operations. */
    ANY("Any"),   //$NON-NLS-1$
    
    /** MessageList noun for list operation. */
    MESSAGE_LIST("MessageList"),   //$NON-NLS-1$
    
    /** QueryData noun for query operation. */
    QUERY_DATA("QueryData"),  //$NON-NLS-1$
    
    /** Compressed noun for binary (or xml compressed) put and get operations. */
    COMPRESSED("Compressed"),  //$NON-NLS-1$

    /** Empty response noun. */
    EMPTY("");  //$NON-NLS-1$
    
    /** Tag name (element) of the "Noun" in IEC 61968-100. */
    public static final String ELEMENT_NOUN = "Noun";  //$NON-NLS-1$

    /** Noun as string. */
    private String nounStr;

    /**
     * Creates a new Noun with the given text value.
     * @param nounText Noun's text value.
     */
    EnumNoun(final String nounText) {
        nounStr = nounText;
    }

    /**
     * Returns a <code>EnumNoun</code> from the given text. <code>null</code> if the given text doesn't describes any of
     * the elements.
     * @param text Text to retrieve the EnumType
     * @return EnumNoun from the given text. <code>null</code> if the given text doesn't describes any of the elements.
     */
    public static EnumNoun fromString(final String text) {
        EnumNoun[] elemts = EnumNoun.values();
        EnumNoun retValue = null;
        for (EnumNoun elem : elemts) {
            if (elem.toString().equals(text)) {
                retValue = elem;
            }
        }

        return retValue;
    }

    /**
     * Returns the text code for the current noun.
     * @return Text code for the current noun.
     */
    @Override
    public String toString() {
        return nounStr;
    }

}
