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

package es.ree.eemws.core.utils.operations;

import es.ree.eemws.core.utils.error.EnumErrorCatalog;
import es.ree.eemws.core.utils.iec61968100.GenericCodedException;
 
/**
 * Handler Exception implements a generic coded exception in order to notify problems during the
 * "handler" phase.
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/12/2014
 */

public final class HandlerException extends GenericCodedException {
   
    /** Serial version. */
    private static final long serialVersionUID = -2198541837747052546L;
  
    /** Http error to be send in the response (can be null). */    
    private final Integer httpError;
        
    /**
     * Creates a HandlerException with a HTTP error code. 
     * @param httpErr HTTP error code.
     * @param err Error code.
     * @param args Optional arguments (detail info) for the error code.
     */
    public HandlerException(final Integer httpErr, final EnumErrorCatalog err, final String ... args) {
        super(err.getMessage(), err.getCode(), args);
        httpError = httpErr;
    }
    
    /**
     * Creates a HandlerException with the given error and optional details.   
     * @param err Error code.
     * @param args Optional arguments (detail info) for the error code.
     */
    public HandlerException(final EnumErrorCatalog err, final String ... args) {
        super(err.getMessage(), err.getCode(), args);      
        httpError = null;
    }
    
    /**
     * Creates a HandlerException with the given error, optional details and cause.   
     * @param err Error code.
     * @param cause Exception cause.
     * @param args Optional arguments (detail info) for the error code.
     */
    public HandlerException(final EnumErrorCatalog err, final Exception cause, final String ... args) {
        super(err.getMessage(), err.getCode(), cause, args);
        httpError = null;
    }
         
    /**
     * Creates a HandlerException with the given error, optional details, HTTP error and cause.   
     * @param httpErr HTTP error code.
     * @param err Error code.
     * @param cause Exception cause.
     * @param args Optional arguments (detail info) for the error code.
     */
    public HandlerException(final Integer httpErr, final EnumErrorCatalog err, final Exception cause, final String ... args) {
        super(err.getMessage(), err.getCode(), cause, args);
        httpError = httpErr;
    }

    /**
     * Returns this exception http error code. 
     * @return This exception http error code. <code>null</code> if there is no error code associated.
     */
    public Integer getHttpError() {
        return httpError;
    }
}
