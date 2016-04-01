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
 * Possible DataType values for QueryData operation.
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/12/2014
 */

public enum EnumDataTypeValues {

    /** The server will return a list of valid DataTypes that can be used for this service on this server. */
    LIST_OF_DATA_TYPES("listOfDataTypes"), //$NON-NLS-1$

    /** The server will return the Server Timestamp in UTC format. */
    SERVER_TIME_STAMP("serverTimestamp"), //$NON-NLS-1$

    /** The server will return its operational limits for parameters used in the List, Get, Put services. */
    PARAMETER_LIMITS("parameterLimits"); //$NON-NLS-1$

    /** String used to separate values in the list view. */
    private static final String LIST_SEPARATOR = ", "; //$NON-NLS-1$

    /** Value as string. */
    private String value;

    /**
     * Creates a new DataType with the given value.
     * @param val DataType value.
     */
    EnumDataTypeValues(final String val) {
        value = val;
    }

    /**
     * Returns a <code>EnumNoun</code> from the given text. <code>null</code> if the given text doesn't describes any of
     * the elements.
     * @param text Text to retrieve the EnumType
     * @return EnumNoun from the given text. <code>null</code> if the given text doesn't describes any of the elements.
     */
    public static EnumDataTypeValues fromString(final String text) {
        EnumDataTypeValues[] elemts = EnumDataTypeValues.values();
        EnumDataTypeValues retValue = null;
        for (EnumDataTypeValues elem : elemts) {
            if (elem.toString().equals(text)) {
                retValue = elem;
            }
        }

        return retValue;
    }

    /**
     * Returns the value for the current DataType.
     * @return Value for the current DataType.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns a "printable" list of the elements' values.
     * @return printable list of the elements' values.
     */
    public static String getList() {
        EnumDataTypeValues[] elemts = EnumDataTypeValues.values();
        StringBuilder sb = new StringBuilder();
        for (EnumDataTypeValues elem : elemts) {
            sb.append(elem);
            sb.append(LIST_SEPARATOR);
        }

        sb.setLength(sb.length() - LIST_SEPARATOR.length());

        return sb.toString();
    }

}
