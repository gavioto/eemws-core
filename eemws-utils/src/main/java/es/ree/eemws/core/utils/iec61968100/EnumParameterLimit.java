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
 * Parameters limits for the query data operation.
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/12/2014
 */

public enum EnumParameterLimit {

    /** Maximum number of messages that will be returned in a list operation response. */
    MAX_NUM_MESSAGES_IN_LIST_RESPONSE("MaxNumMessagesInListResponse"), //$NON-NLS-1$

    /**
     * Number of days that are guarantee to be included in the response list when the request has used a small code
     * value (typically 0). According to this specification, the default value for this parameter limit is 1 (all
     * messages available from 00:00 of D-1).
     */
    NUMBER_OF_DAYS_FOR_LOW_CODE_IN_LIST_RESPONSE("NumberOfDaysForLowCodeInListResponse"), //$NON-NLS-1$

    /** Maximum number of days that a request for Application time interval type can span. */
    MAX_APPLICATION_TIME_INTERVAL_IN_DAYS_IN_LIST_REQUEST("MaxApplicationTimeIntervalInDaysInListRequest"), //$NON-NLS-1$

    /** Maximum number of days that a request for Server time interval type can span. */
    MAX_SERVER_TIME_INTERVAL_IN_DAYS_IN_LIST_REQUEST("MaxServerTimeIntervalInDaysInListRequest"), //$NON-NLS-1$

    /** Maximum size, in Megabytes, that message payload content can have. Messages with bigger size will be rejected. */
    MAX_PAYLOAD_SIZE_IN_MB_IN_PUT_REQUEST("MaxPayloadSizeInMBInPutRequest"), //$NON-NLS-1$

    /** Number of Get operations per minute that a user can execute. */
    MAX_GET_REQUEST_PER_MINUTE("MaxGetRequestPerMinute"), //$NON-NLS-1$

    /** Number of Put operations per minute that a user can execute. */
    MAX_PUT_REQUEST_PER_MINUTE("MaxPutRequestPerMinute"), //$NON-NLS-1$

    /** Number of List operations per minute that a user can execute. */
    MAX_LIST_REQUEST_PER_MINUTE("MaxListRequestPerMinute"), //$NON-NLS-1$

    /** Number of Query Data operations per minute that a user can execute. */
    MAX_QUERY_REQUEST_PER_MINUTE("MaxQueryRequestPerMinute"), //$NON-NLS-1$

    /** Max number of days that a message will be accessible by this specification operations. */
    MAX_MESSAGE_AGE_IN_DAYS("MaxMessageAgeInDays"), //$NON-NLS-1$
    
    /**
     * If set, the server will reject messages that do not meet the following criteria: CT - ST + MD >= 0 Being CT the
     * current server time, ST the msg:serverTimestamp indicated in the request message and MD this parameter value.
     */
    MAX_DIFF_SERVER_TIMESTAMP_IN_SECONDS("MaxDiffServerTimestampInSeconds"), //$NON-NLS-1$
    
    /* From this point on NOT 62325-504 limits: */
    
    /** Xml messages will be transmited as binary if their size is bigger than the one configured. */
    GET_XML_AS_BINARY_THRESHOLD_IN_KB("GetXmlAsBinaryThresholdInKb"), //$NON-NLS-1$
        
    /** Number of times that a client can "GET" certain message. */ 
    MAX_GET_REQUESTS_PER_MESSAGE("MaxGetRequestsPerMessage"), //$NON-NLS-1$ 
    
    /** (Client limit) Max payload size (in Mb) that the get response can retrieve. */
    MAX_PAYLOAD_SIZE_IN_MB_IN_GET_RESPONSE("MaxPayloadSizeInMBInGetResponse"); //$NON-NLS-1$ 
    
    /** Parameter as a text. */
    private String parameterText;

    /**
     * Creates a new parameter limit with the given text value.
     * @param text Parameter's text value.
     */
    EnumParameterLimit(final String text) {
        parameterText = text;
    }

    /**
     * Returns a <code>EnumParameterLimit</code> from the given text. <code>null</code> if the given text doesn't describes any of
     * the elements.
     * @param text Text to retrieve the EnumType
     * @return EnumParameterLimit from the given text. <code>null</code> if the given text doesn't describes any of the elements.
     */
    public static EnumParameterLimit fromString(final String text) {
        EnumParameterLimit[] elemts = EnumParameterLimit.values();
        EnumParameterLimit retValue = null;
        for (EnumParameterLimit elem : elemts) {
            if (elem.toString().equals(text)) {
                retValue = elem;
            }
        }

        return retValue;
    }

    /**
     * Returns the text code for the current parameter.
     * @return Text code for the current parameter.
     */
    @Override
    public String toString() {
        return parameterText;
    }
}
