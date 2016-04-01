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

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import ch.iec.tc57._2011.schema.message.RequestMessage;
import es.ree.eemws.core.utils.error.EnumErrorCatalog;
import es.ree.eemws.core.utils.iec61968100.EnumFilterElement;
import es.ree.eemws.core.utils.iec61968100.MessageUtil;

/**
 * Query request validator.
 * Perform validation on request message. This class can be used by both client and server avoiding
 * code duplication.
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.1 11/05/2015
 * 
 */
public final class QueryRequestMessageValidator {

    /** Empty collection for non-parameter queries such as ServerTimestamp .*/
    private static final Set<String> EMPTY_SET = Collections.emptySet();
        
    /**
     * Validates the given request message.
     * Aditional validations must be performed by the invoker depending on the query needs.
     * @param message Request message to be validated.
     * @return a map with all the request "options" values.
     * @throws QueryOperationException if the request is not valid.
     */
    public static Map<String, Object> validate(final RequestMessage message) throws QueryOperationException {

        Map<String, Object> queryParameters = null;
        try {

            queryParameters = MessageUtil.getRequestMessageOptions(message);
            String idQuery = (String) queryParameters.get(EnumFilterElement.DATA_TYPE.toString());

            if (idQuery == null) {
                throw new QueryOperationException(EnumErrorCatalog.ERR_QRY_001);
            }

            XMLGregorianCalendar startTime = (XMLGregorianCalendar) queryParameters.get(EnumFilterElement.START_TIME.toString());
            XMLGregorianCalendar endTime = (XMLGregorianCalendar) queryParameters.get(EnumFilterElement.END_TIME.toString());

            if (startTime != null && endTime != null && endTime.compare(startTime) == DatatypeConstants.LESSER) {
                throw new QueryOperationException(EnumErrorCatalog.ERR_QRY_003);
            }

        } catch (IllegalArgumentException e) {
            throw new QueryOperationException(EnumErrorCatalog.ERR_QRY_010, e.getMessage());
        }

        return queryParameters;
    }


    /**
     * Checks that the operation only received the queryId with no aditional parametes.
     * @param queryId Query identification (DataType value)
     * @param receivedParameterNames Received set of parameter names.
     * @throws QueryOperationException if the <code>receivedParameterNames</code> set has at least a parameter 
     * name that is not included in the <code>validParameterNames</code> set.
     */    
    public static void validateParameterNames(final String queryId, final Set<String> receivedParameterNames) throws QueryOperationException {
        
        validateParameterNames(queryId, EMPTY_SET, receivedParameterNames);
    }
    
    
    /**
     * Checks that the operation only received valid (known) filter names.
     * @param queryId Query identification (DataType value)
     * @param validParameterNames Valid (understood) parameter for the query.
     * @param receivedParameterNames Received set of parameter names.
     * @throws QueryOperationException if the <code>receivedParameterNames</code> set has at least a parameter
     * name that is not included in the <code>validParameterNames</code> set.
     */    
    public static void validateParameterNames(final String queryId, final Set<String> validParameterNames, 
            final Set<String> receivedParameterNames) throws QueryOperationException {
        
        Set<String> parameterNames = new HashSet<>(receivedParameterNames);
        
        parameterNames.removeAll(validParameterNames);
        parameterNames.remove(EnumFilterElement.DATA_TYPE.toString());
                
        if (!parameterNames.isEmpty()) {
            throw new QueryOperationException(EnumErrorCatalog.ERR_QRY_011, queryId, parameterNames.iterator().next());
        }
    }

    /**
     * Constructor. 
     * Utility classes shouldn't have public constructor.
     */
    private QueryRequestMessageValidator() {
       
        /* This constructor must be empty. */
    }    
}
