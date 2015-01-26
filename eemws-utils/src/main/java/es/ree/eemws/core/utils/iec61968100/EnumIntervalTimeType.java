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
 * Possible values for the interval time type.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */

public enum EnumIntervalTimeType {

    /* Application time interval */
    APPLICATION("Application"), //$NON-NLS-1$

    /* Server timestamp time interval. */
    SERVER("Server"); //$NON-NLS-1$

    /** Default interval type. */
    public static final EnumIntervalTimeType DEFAULT_INTERVAL_TYPE = EnumIntervalTimeType.APPLICATION;

    /** String used to separate values in the list view. */
    private static final String LIST_SEPARATOR = ", "; //$NON-NLS-1$

    /** Text code. */
    private String text;

    /**
     * Private constructor. Sets the text code.
     * @param txt Text code to use.
     */
    private EnumIntervalTimeType(final String txt) {
        text = txt;
    }

    /**
     * Returns the text code for the current IntervalTimeType.
     * @return Text code for the current IntervalTimeType.
     */
    @Override
    public String toString() {
        return text;
    }

    /**
     * Returns a <code>EnumIntervalTimeType</code> from the given text.
     * <code>null</code> if the given text doesn't describes any of the elements.
     * @param text Text to retrieve the EnumType
     * @return EnumIntervalTimeType from the given text.
     * <code>null</code> if the given text doesn't describes any of the elements.
     */
    public static EnumIntervalTimeType fromString(final String text) {
        EnumIntervalTimeType[] elemts = EnumIntervalTimeType.values();
        EnumIntervalTimeType retValue = null;
        for (EnumIntervalTimeType elem : elemts) {
            if (elem.toString().equals(text)) {
                retValue = elem;
            }
        }

        return retValue;
    }

    /**
     * Returns a "printable" list of the elements' values.
     * @return printable list of the elements' values.
     */
    public static String getList() {
        EnumIntervalTimeType[] elemts = EnumIntervalTimeType.values();
        StringBuilder sb = new StringBuilder();
        for (EnumIntervalTimeType elem : elemts) {
            sb.append(elem);
            sb.append(LIST_SEPARATOR);
        }

        sb.setLength(sb.length() - LIST_SEPARATOR.length());

        return sb.toString();
    }

}
