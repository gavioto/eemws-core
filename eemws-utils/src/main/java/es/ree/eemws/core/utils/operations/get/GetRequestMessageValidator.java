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

package es.ree.eemws.core.utils.operations.get;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.iec.tc57._2011.schema.message.RequestMessage;
import es.ree.eemws.core.utils.error.EnumErrorCatalog;
import es.ree.eemws.core.utils.iec61968100.EnumFilterElement;
import es.ree.eemws.core.utils.iec61968100.EnumQueue;
import es.ree.eemws.core.utils.iec61968100.MessageUtil;

/**
 * Get request validator.
 * Perform validation on request message. This class can be used by both client and server avoiding
 * code duplication.
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.1 11/05/2015
 * 
 */
public final class GetRequestMessageValidator {
    
    /**
     * Validates the given get request.
     * @param message Request message to be validated.
     * @return a map with all the request "options" values.
     * @throws GetOperationException If the request is not valid.     
     */
    public static Map<String, Object> validate(final RequestMessage message) throws GetOperationException {

        Map<String, Object> getParameters = validateParametersValues(message);
        validateParametersNames(getParameters);

        return getParameters;
    }

    /**
     * Checks get request parameters.
     * Aditional checks can be made in order to:
     * <li>Reject a request by QUEUE if this is not supported.
     * <li>Reject a request by Identification if no version is provided and it is necessary in the business' context.
     * @param message Request message to be validated.
     * @return a map with all the request "options" values.
     * @throws GetOperationException If the request has invalid parameters.
     */
    private static Map<String, Object> validateParametersValues(final RequestMessage message) throws GetOperationException {

        Map<String, Object> getParameters = null;

        try {

            getParameters = MessageUtil.getRequestMessageOptions(message);

            boolean getByCode = getParameters.get(EnumFilterElement.CODE.toString()) != null;
            boolean getByQueue = getParameters.get(EnumFilterElement.QUEUE.toString()) != null;
            boolean getById = getParameters.get(EnumFilterElement.MESSAGE_IDENTIFICATION.toString()) != null;

            int numGet = 0;
            
            if (getByCode) {
                numGet++;
            }

            if (getByQueue) {
                numGet++;
            }

            if (getById) {
                numGet++;
            }

            if (numGet > 1) {
                throw new GetOperationException(EnumErrorCatalog.ERR_GET_003);
            }

            if (numGet == 0) {
                throw new GetOperationException(EnumErrorCatalog.ERR_GET_004);
            }

            if (getByQueue) {
                
                String queue = (String) getParameters.get(EnumFilterElement.QUEUE.toString());
                if (!EnumQueue.NEXT.toString().equals(queue)) {
                    throw new GetOperationException(EnumErrorCatalog.ERR_GET_017, EnumQueue.NEXT.toString(), queue);
                }
                
            } else if (getByCode) {
                
                Long code;
                String val = (String) getParameters.get(EnumFilterElement.CODE.toString());
                try {
                    code = Long.parseLong(val);
                    if (code < 0) {
                        throw new GetOperationException(EnumErrorCatalog.ERR_GET_001);
                    }
                } catch (NumberFormatException e) {
                    throw new GetOperationException(EnumErrorCatalog.ERR_GET_002);
                }         
            } else if (getById) {
                String ver = (String) getParameters.get(EnumFilterElement.MESSAGE_VERSION.toString());
                if (ver != null) {
                    try {
                        Integer.valueOf(ver);
                    } catch (NumberFormatException e) {
                        throw new GetOperationException(EnumErrorCatalog.ERR_GET_019, ver);  
                    }
                }
            }
            
        } catch (IllegalArgumentException e) {
            throw new GetOperationException(EnumErrorCatalog.ERR_GET_011, e.getMessage());
        }

        return getParameters;
    }

    /**
    * Checks that the operation only received valid (known) filter name.
    * @param getParameters Parameters used in the current request.
    * @throws GetOperationException If the user has used an invalid (not known) parameter
    */
    private static void validateParametersNames(final Map<String, Object> getParameters) throws GetOperationException {
        Set<String> parameterNames = new HashSet<>(getParameters.keySet());

        parameterNames.remove(EnumFilterElement.MESSAGE_IDENTIFICATION.toString());
        parameterNames.remove(EnumFilterElement.MESSAGE_VERSION.toString());
        parameterNames.remove(EnumFilterElement.QUEUE.toString());
        parameterNames.remove(EnumFilterElement.CODE.toString());

        if (!parameterNames.isEmpty()) {
            throw new GetOperationException(EnumErrorCatalog.ERR_GET_012, parameterNames.iterator().next());
        }
    }

    /**
     * Constructor. 
     * Utility classes shouldn't have public constructor.
     */
    private GetRequestMessageValidator() {
       
        /* This constructor must be empty. */
    }
    
}
