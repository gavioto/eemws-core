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
 * Verbs values.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/12/2014
 */
public enum EnumVerb {

    MODIFY("modify"),  //$NON-NLS-1$
    CREATE("create"),  //$NON-NLS-1$
    GET("get"),  //$NON-NLS-1$
    REPLY("reply");   //$NON-NLS-1$

    /** Verb text. */
    private String verbStr;

    /**
     * Creates a new verb with the given text value.
     * @param verbText Verb's text value.
     */
    EnumVerb(final String verbText) {
        verbStr = verbText;
    }

    /** Tag name (element) of the "Verb" in IEC 61968-100. */
    public static final String ELEMENT_VERB = "Verb"; //$NON-NLS-1$

    /**
     * Returns a <code>EnumVerb</code> from the given text.
     * <code>null</code> if the given text doesn't describes any of the elements.
     * @param text Text to retrieve the EnumType
     * @return EnumVerb from the given text.
     * <code>null</code> if the given text doesn't describes any of the elements.
     */
    public static EnumVerb fromString(final String text) {
        EnumVerb[] elemts = EnumVerb.values();
        EnumVerb retValue = null;
        for (EnumVerb elem : elemts) {
            if (elem.toString().equals(text)) {
                retValue = elem;
            }
        }

        return retValue;
    }

    /**
     * Returns the text code for the current verb.
     * @return Text code for the current verb.
     */
    @Override
    public String toString() {
        return verbStr;
    }

}
