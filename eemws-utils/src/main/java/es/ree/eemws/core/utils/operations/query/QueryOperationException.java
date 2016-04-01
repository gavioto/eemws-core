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

package es.ree.eemws.core.utils.operations.query;
 
import es.ree.eemws.core.utils.error.EnumErrorCatalog;
import es.ree.eemws.core.utils.iec61968100.GenericCodedException;


/**
 * Query operation general exception.
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.1 11/05/2015
 * 
 */
public final class QueryOperationException extends GenericCodedException {
    
    /** Serial version. */
    private static final long serialVersionUID = 8190591012608848426L;
   
    /**
     * Creates a new exception with the given error.
     * @param err Error raised by the operation.
     * @param args Optional arguments for the text error message. 
     */
    public QueryOperationException(final EnumErrorCatalog err, final String ... args) {
        super(err.getMessage(), err.getCode(), args);
    }
    
    /**
     * Creates a new exception with the given error.
     * @param err Error raised by the operation.
     * @param cause Exception that caused this error.
     * @param args Optional arguments for the text error message.
     */
    public QueryOperationException(final EnumErrorCatalog err, final Exception cause, final String ... args) {
        super(err.getMessage(), err.getCode(), cause, args);
    }
    
    /**
     * Creates a new exception using the code and message of other GenericCodedException.
     * @param other codedException which data we want to use.
     */
    public QueryOperationException(final GenericCodedException other) {
        super(other);
    }
    
    
}

