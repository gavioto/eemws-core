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

package es.ree.eemws.core.utils.iec61968100;

/**
 * Simple exception implementation that support the use of a code along with a message text and an exception cause.
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.1 10/05/2015
 * 
 */
public class GenericCodedException extends Exception {

    /** Serial ID. */
    private static final long serialVersionUID = 6627907085138612803L;
    
    /** Exception code. */
    private final String errCode;

    /**
     * Creates a new exception with the given message text and code.
     * @param text Message text.
     * @param code Exception code.
     * @param args List of string
     * @param args Optional arguments for the text error message.
     */
    public GenericCodedException(final String text, final String code, final String ... args) {
        super(handleParameters(text, args));
        errCode = code;
    }

    /**
     * Creates a new exception with the given message text, code and exception cause.
     * @param text Message text.
     * @param code Exception code.
     * @param exCause Exception cause.
     * @param args Optional arguments for the text error message.
     */
    public GenericCodedException(final String text, final String code, final Exception exCause, final String ... args) {
        super(handleParameters(text, args), exCause);
        errCode = code;
    }

    /**
     * Returns this exception code value.
     * @return Exception code value.
     */
    public String getCode() {
        return errCode;
    }
    
    /**
     * Replace the parameters in the error message text (?) by its value given in the args String list. 
     * @param errMsg Error message with parameters.
     * @param args Optional arguments for the text error message.
     * @return <code>errMsg</code> String with all the ocurrences of ? replaced by a String given by <code>args</code>
     */
    private static String handleParameters(final String errMsg, final String ... args) {
        String text = errMsg;
        for (String str : args) {
            text = text.replaceFirst("\\?", str);
        }
        return text;
    }

}
