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
 * Filter elements (tags).
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/12/2014
 */

public enum EnumFilterElement {

    START_TIME("StartTime"),  //$NON-NLS-1$
    END_TIME("EndTime"),  //$NON-NLS-1$
    CODE("Code"),  //$NON-NLS-1$
    INTERVAL_TYPE("IntervalType"),  //$NON-NLS-1$
    DATA_TYPE("DataType"),  //$NON-NLS-1$
    OWNER("Owner"),  //$NON-NLS-1$
    MESSAGE_IDENTIFICATION("MessageIdentification"),  //$NON-NLS-1$
    MESSAGE_VERSION("MessageVersion"), //$NON-NLS-1$
    MESSAGE_TYPE("MsgType"),  //$NON-NLS-1$
    QUEUE("Queue"); //$NON-NLS-1$

    /** Filter text. */
    private String filterStr;

    /**
     * Creates a new Filter Element from the given text.
     * @param text Element (tag name) for the filter.
     */
    EnumFilterElement(final String text) {
        filterStr = text;
    }

    /**
     * Returns a <code>EnumFiltersElement</code> from the given text.
     * <code>null</code> if the given text doesn't describes any of the elements.
     * @param text Text to retrieve the EnumType
     * @return EnumFiltersElement from the given text.
     * <code>null</code> if the given text doesn't describes any of the elements.
     */
    public static EnumFilterElement fromString(final String text) {
        EnumFilterElement[] elemts = EnumFilterElement.values();
        EnumFilterElement retValue = null;
        for (EnumFilterElement elem : elemts) {
            if (elem.toString().equals(text)) {
                retValue = elem;
            }
        }

        return retValue;
    }

    /**
     * Returns the text code for the current filter.
     * @return Text code for the current filter.
     */
    @Override
    public String toString() {
        return filterStr;
    }
}
