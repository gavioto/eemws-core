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

package es.ree.eemws.core.utils.operations.list;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import ch.iec.tc57._2011.schema.message.RequestMessage;
import es.ree.eemws.core.utils.error.EnumErrorCatalog;
import es.ree.eemws.core.utils.iec61968100.EnumFilterElement;
import es.ree.eemws.core.utils.iec61968100.EnumIntervalTimeType;
import es.ree.eemws.core.utils.iec61968100.EnumParameterLimit;
import es.ree.eemws.core.utils.iec61968100.MessageUtil;
import es.ree.eemws.core.utils.operations.OperationLimits;

/**
 * List request validator.
 * Perform validation on request message. This class can be used by both client and server avoiding
 * code duplication.
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.1 11/05/2015
 * 
 */
public final class ListRequestMessageValidator {

    /** Constant to conver days into milliseconds. */
    private static final Integer DAYS_TO_MILLISECONDS = 24 * 60 * 60 * 1000;

    /**
     * Validates the given request message.
     * @param message Request message to be validated.
     * @return a map with all the request "options" values.
     * @throws ListOperationException if the request is not valid.
     */
    public static Map<String, Object> validate(final RequestMessage message) throws ListOperationException {
        return validate(message, null);
    }

    /**
     * Validates the given request message.
     * @param message Request message to be validated.
     * @param limits List operation limits. This parameter can be <code>null</code>
     * @return a map with all the request "options" values.
     * @throws ListOperationException if the request is not valid.
     */
    public static Map<String, Object> validate(final RequestMessage message, final OperationLimits limits) throws ListOperationException {

        Map<String, Object> listParameters = validateParametersValues(message, limits);
        validateParametersNames(listParameters);

        return listParameters;
    }

    /**
     * Validates the given request parameter's values.
     * @param message Request message to be validated.
     * @param limits Lis operation limits. This parameter can be <code>null</code>
     * @return a map with all the request "options" values.
     * @throws ListOperationException if the request is not valid.
     */
    private static Map<String, Object> validateParametersValues(final RequestMessage message, final OperationLimits limits) throws ListOperationException {

        Map<String, Object> listParameters = null;

        try {

            listParameters = MessageUtil.getRequestMessageOptions(message);

            XMLGregorianCalendar startTime = (XMLGregorianCalendar) listParameters.get(EnumFilterElement.START_TIME.toString());
            XMLGregorianCalendar endTime = (XMLGregorianCalendar) listParameters.get(EnumFilterElement.END_TIME.toString());

            boolean listByCode = listParameters.get(EnumFilterElement.CODE.toString()) != null;

            if ((listByCode && (startTime != null || endTime != null)) 
                    || (!listByCode && (startTime == null || endTime == null)) || (listByCode && listParameters.get(EnumFilterElement.INTERVAL_TYPE.toString()) != null)) {

                throw new ListOperationException(EnumErrorCatalog.ERR_LST_005);
            }

            if (listByCode) {

                String val = (String) listParameters.get(EnumFilterElement.CODE.toString());
                try {
                    long code = Long.parseLong(val);
                    if (code < 0) {
                        throw new ListOperationException(EnumErrorCatalog.ERR_LST_001);
                    }
                } catch (NumberFormatException e) {
                    throw new ListOperationException(EnumErrorCatalog.ERR_LST_002);
                }

            } else {

                /* Checks interval type filter existence, overwrite it with a enumeration entry instead of a string. */
                String interval = (String) listParameters.get(EnumFilterElement.INTERVAL_TYPE.toString());

                EnumIntervalTimeType intervalT;

                if (interval == null) {
                    intervalT = EnumIntervalTimeType.DEFAULT_INTERVAL_TYPE;
                } else {
                    intervalT = EnumIntervalTimeType.fromString(interval);
                    if (intervalT == null) {
                        throw new ListOperationException(EnumErrorCatalog.ERR_LST_009, EnumIntervalTimeType.getList());
                    }
                }

                listParameters.put(EnumFilterElement.INTERVAL_TYPE.toString(), intervalT);

                if (endTime.compare(startTime) == DatatypeConstants.LESSER) {
                    throw new ListOperationException(EnumErrorCatalog.ERR_LST_003);
                }

                if (limits != null) {
                    Integer maxAllowedInterval = null;

                    if (intervalT.equals(EnumIntervalTimeType.APPLICATION)) {
                        maxAllowedInterval = limits.getLimit(EnumParameterLimit.MAX_APPLICATION_TIME_INTERVAL_IN_DAYS_IN_LIST_REQUEST);
                    } else {
                        maxAllowedInterval = limits.getLimit(EnumParameterLimit.MAX_SERVER_TIME_INTERVAL_IN_DAYS_IN_LIST_REQUEST);
                    }

                    if (maxAllowedInterval != null 
                            && endTime.toGregorianCalendar().getTimeInMillis() - startTime.toGregorianCalendar().getTimeInMillis() > maxAllowedInterval * DAYS_TO_MILLISECONDS) {
                                
                        throw new ListOperationException(EnumErrorCatalog.ERR_LST_004, maxAllowedInterval.toString());
                        
                    }
                }
            }

        } catch (IllegalArgumentException e) {
            throw new ListOperationException(EnumErrorCatalog.ERR_LST_010, e.getMessage());
        }

        return listParameters;
    }

    /**
     * Validates that only list operation options (filters) have been used in the request. 
     * @param listParameters Request option elements.
     * @throws ListOperationException If there are repeated or unknow options. 
     */
    private static void validateParametersNames(final Map<String, Object> listParameters) throws ListOperationException {
        Set<String> parameterNames = new HashSet<>(listParameters.keySet());
        parameterNames.remove(EnumFilterElement.INTERVAL_TYPE.toString());
        parameterNames.remove(EnumFilterElement.CODE.toString());
        parameterNames.remove(EnumFilterElement.START_TIME.toString());
        parameterNames.remove(EnumFilterElement.END_TIME.toString());
        parameterNames.remove(EnumFilterElement.MESSAGE_TYPE.toString());
        parameterNames.remove(EnumFilterElement.OWNER.toString());
        parameterNames.remove(EnumFilterElement.MESSAGE_IDENTIFICATION.toString());

        if (!parameterNames.isEmpty()) {
            throw new ListOperationException(EnumErrorCatalog.ERR_LST_011, parameterNames.iterator().next());
        }
    }
    
    /**
     * Constructor. 
     * Utility classes shouldn't have public constructor.
     */
    private ListRequestMessageValidator() {
       
        /* This constructor must be empty. */
    }
   
}
