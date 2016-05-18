/*
 * Copyright 2016 Red Eléctrica de España, S.A.U.
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

package es.ree.eemws.core.utils.error;

/**
 * Defines error literal constants.
 *  
 * @author Red Eléctrica de España S.A.U.
 * @version 1.1 10/01/2016
 * 
 */
interface ErrorCodes {

    /** Invalid parameters. Code must be a positive integer value. */
    String LST_001 = "LST-001"; //$NON-NLS-1$

    /** Invalid operation parameters. Code must be an integer value. */
    String LST_002 = "LST-002"; //$NON-NLS-1$

    /** Invalid operation parameters. EndTime cannot precede StartTime. */
    String LST_003 = "LST-003"; //$NON-NLS-1$

    /** Invalid operation parameters. Time interval cannot span more than ? days. */
    String LST_004 = "LST-004"; //$NON-NLS-1$

    /** Invalid operation parameters. You must provide either Code or StartTime and EndTime time interval values */
    String LST_005 = "LST-005"; //$NON-NLS-1$

    /** Database read failed. */
    String LST_006 = "LST-006"; //$NON-NLS-1$

    /** The operation returns more than ? messages. Please, use a smaller time interval value or add more filters. */
    String LST_007 = "LST-007"; //$NON-NLS-1$

    /** Unable to create list response. */
    String LST_008 = "LST-008"; //$NON-NLS-1$

    /** Invalid operation parameters. IntervalType must be one of: ? */
    String LST_009 = "LST-009"; //$NON-NLS-1$

    /** Invalid operation parameters. ? */
    String LST_010 = "LST-010"; //$NON-NLS-1$

    /** Unknown parameter for list operation: ? */
    String LST_011 = "LST-011"; //$NON-NLS-1$

    /** Received message list has an invalid entry with no message code- */
    String LST_012 = "LST-012"; //$NON-NLS-1$

    /** Received message list has an invalid list entry with no message identification. Message info: [code=?]. */
    String LST_013 = "LST-013"; //$NON-NLS-1$

    /** Received message list has an invalid list entry with no message type. Message info: [code=?][id=?]. */
    String LST_014 = "LST-014"; //$NON-NLS-1$

    /** Received message list has an invalid list entry with no start application time interval. Message info: [code=?][id=?][type=?]. */
    String LST_015 = "LST-015"; //$NON-NLS-1$

    /** Received message list has an invalid list entry with no server timestamp. Message info: [code=?][id=?][type=?]. */
    String LST_016 = "LST-016"; //$NON-NLS-1$

    /** Received message list has an invalid list entry with no owner. Message info: [code=?][id=?][type=?]. */
    String LST_017 = "LST-017"; //$NON-NLS-1$

    /** Received message list is invalid. ?. */
    String LST_018 = "LST-018"; //$NON-NLS-1$

    /** Unable to get message list from the received payload. */
    String LST_019 = "LST-019"; //$NON-NLS-1$

    /** User has exceeded list operation limits. User is temporarily blocked. */
    String LST_020 = "LST-020"; //$NON-NLS-1$

    /** Invalid operation parameters. Code must be a positive integer value. */
    String GET_001 = "GET-001"; //$NON-NLS-1$

    /** Invalid operation parameters. Code must be an integer value.  */
    String GET_002 = "GET-002"; //$NON-NLS-1$

    /** Invalid invocation parameters. You must provide either Code or MessageIdentification and MessageVersion values. */
    String GET_003 = "GET-003"; //$NON-NLS-1$

    /** Invalid invocation parameters. You must provide Code or MessageIdentification and MessageVersion values.  */
    String GET_004 = "GET-004"; //$NON-NLS-1$

    /** QUEUE filter is not supported.  */
    String GET_005 = "GET-005"; //$NON-NLS-1$

    /** The requested message doesn't exist.  */
    String GET_006 = "GET-006"; //$NON-NLS-1$

    /** Database read failed.  */
    String GET_007 = "GET-007"; //$NON-NLS-1$

    /** Unable to create get response.  */
    String GET_008 = "GET-008"; //$NON-NLS-1$

    /** Unable to filter the message payload.  */
    String GET_009 = "GET-009"; //$NON-NLS-1$

    /** User has exceeded get operation limits. User is temporarily blocked.  */
    String GET_010 = "GET-010"; //$NON-NLS-1$

    /** Invalid operation parameters. ?  */
    String GET_011 = "GET-011"; //$NON-NLS-1$

    /** Unknown parameter for get operation: ?  */
    String GET_012 = "GET-012"; //$NON-NLS-1$

    /** File read failed.  */
    String GET_013 = "GET-013"; //$NON-NLS-1$

    /** The received message format is not supported [?]. Valid formats are [?]  */
    String GET_014 = "GET-014"; //$NON-NLS-1$

    /** Unable to unzip received binary XML.  */
    String GET_015 = "GET-015"; //$NON-NLS-1$

    /** Unable to read payload element..  */
    String GET_016 = "GET-016"; //$NON-NLS-1$

    /** Queue value must be "?" not "?" */
    String GET_017 = "GET-017"; //$NON-NLS-1$

    /** Server returns a binary message but did not provide file name.  */
    String GET_018 = "GET-018"; //$NON-NLS-1$

    /** MessageVersion must be a positive integer.  */
    String GET_019 = "GET-019"; //$NON-NLS-1$

    /** Server requieres MessageVersion when MessageIdentification is provided.  */
    String GET_020 = "GET-020"; //$NON-NLS-1$

    /** The received message's size ? is greater that the maximun allowed ?  */
    String GET_021 = "GET-021"; //$NON-NLS-1$
    
    /** Invalid parameters. DataType value must be provided. */
    String QRY_001 = "QRY-001"; //$NON-NLS-1$

    /** Invalid parameters. Provided DataType value is not recognized. */
    String QRY_002 = "QRY-002"; //$NON-NLS-1$

    /** Invalid parameters. EndTime cannot precede StartTime. */
    String QRY_003 = "QRY-003"; //$NON-NLS-1$

    /** Unable to create QueryData response. */
    String QRY_004 = "QRY-004"; //$NON-NLS-1$

    /** Invalid parameters. Provided value ? for parameter ? is not recognized. */
    String QRY_005 = "QRY-005"; //$NON-NLS-1$

    /** Database read failed. */
    String QRY_006 = "QRY-006"; //$NON-NLS-1$

    /** Invalid parameters. Provided value ? for parameter ? must be a positive number. */
    String QRY_007 = "QRY-007"; //$NON-NLS-1$

    /** The given parameters (?, ?) are mutually exclusive. */
    String QRY_008 = "QRY-008"; //$NON-NLS-1$

    /** Provided date parameter ? has invalid format: ? */
    String QRY_009 = "QRY-009"; //$NON-NLS-1$

    /** Invalid operation parameters. ? */
    String QRY_010 = "QRY-010"; //$NON-NLS-1$

    /** Unknown parameter for query DataType ?: ? */
    String QRY_011 = "QRY-011"; //$NON-NLS-1$

    /** Cannot process response: ? */
    String QRY_012 = "QRY-012"; //$NON-NLS-1$

    /** User has exceeded Query operation limits. User is temporarily blocked. */
    String QRY_013 = "QRY-013"; //$NON-NLS-1$

    /** Remote system is unable to process your message [?] and cannot give a detailed (human readable) reason why. Please ask system administrator. */
    String PUT_001 = "PUT-001"; //$NON-NLS-1$

    /** System is currently processing a message for the same message type and application date. Please wait until the system provides a proper acknowledgement. */
    String PUT_002 = "PUT-002"; //$NON-NLS-1$

    /**  Malformed request: [provided noun=?] [expected noun=?] */
    String PUT_003 = "PUT-003"; //$NON-NLS-1$

    /** Unable to process message [?] there is no message handler for [noun=?]. Check your client's URL. */
    String PUT_004 = "PUT-004"; //$NON-NLS-1$

    /** You have no rights on this message type [MsgType=?][Rights=?] */
    String PUT_005 = "PUT-005"; //$NON-NLS-1$

    /**  Invalid signature. Details: ? */
    String PUT_006 = "PUT-006"; //$NON-NLS-1$

    /** The provided signature document is incorrect and cannot be validated. Details: ? */
    String PUT_007 = "PUT-007"; //$NON-NLS-1$

    /** There is no relationship among the identities [Sender=?][Signer=?][Owner=?] */
    String PUT_008 = "PUT-008"; //$NON-NLS-1$

    /**  Unable to store the message. Please, send a new document version. If the message persists ask system administrator. */
    String PUT_009 = "PUT-009"; //$NON-NLS-1$

    /** Invalid message format: ? */
    String PUT_010 = "PUT-010"; //$NON-NLS-1$

    /** Invalid binary payload */
    String PUT_011 = "PUT-011"; //$NON-NLS-1$

    /** Message was rejected due to technical validations: ? */
    String PUT_012 = "PUT-012"; //$NON-NLS-1$

    /** Received message has no payload or it is empty. */
    String PUT_013 = "PUT-013"; //$NON-NLS-1$

    /** Cannot create request: ? */
    String PUT_014 = "PUT-014"; //$NON-NLS-1$

    /** Cannot process response: ? */
    String PUT_015 = "PUT-015"; //$NON-NLS-1$

    /** User has exceeded put operation limits. User is temporarily blocked. */
    String PUT_016 = "PUT-016"; //$NON-NLS-1$

    /** Invalid operation parameters. ? */
    String PUT_017 = "PUT-017"; //$NON-NLS-1$
    
    /** The received message's size ? is greater that the maximun allowed ? */
    String PUT_018 = "PUT-018"; //$NON-NLS-1$

    /** Unable to retrieve remote user from the https context [IP=?]. */
    String HAND_001 = "HAND-001"; //$NON-NLS-1$

    /** Request message is not valid against schema. Details: ?. */
    String HAND_002 = "HAND-002"; //$NON-NLS-1$

    /** User has no proper role for current message type. */
    String HAND_003 = "HAND-003"; //$NON-NLS-1$

    /** Unable to read soap body.  */
    String HAND_004 = "HAND-004"; //$NON-NLS-1$

    /** Unsupported combination: [verb=?][noun=?] */
    String HAND_005 = "HAND-005"; //$NON-NLS-1$

    /** Invalid message format */
    String HAND_006 = "HAND-006"; //$NON-NLS-1$

    /** Invalid signature. */
    String HAND_007 = "HAND-007"; //$NON-NLS-1$

    /** Signature syntax error. */
    String HAND_008 = "HAND-008"; //$NON-NLS-1$

    /** Unable to sign message. */
    String HAND_009 = "HAND-009"; //$NON-NLS-1$

    /** Server returns FAULT message. */
    String HAND_010 = "HAND-010"; //$NON-NLS-1$

    /** Response message has no payload or payload is empty. */
    String HAND_011 = "HAND-011"; //$NON-NLS-1$

    /** Response has invalid header elements: [verb=?][noun=?] expected: [verb=?][noun=?]. */
    String HAND_012 = "HAND-012"; //$NON-NLS-1$

    /** Client do not trust server identity. Check client's trust store, check URL (do not use IP). */
    String HAND_013 = "HAND-013"; //$NON-NLS-1$

    /** Server's certificate identity does not match URL name. */
    String HAND_014 = "HAND-014"; //$NON-NLS-1$

    /** Cannot connect with the given URL. Check URL, check DNS. */
    String HAND_015 = "HAND-015"; //$NON-NLS-1$

    /** The requested service does not exist in the server. Check configured URL. (HTTP=404, Not found). */
    String HAND_016 = "HAND-016"; //$NON-NLS-1$

    /** You do not have permission to access the web services on the server. (HTTP=403, Forbidden). */
    String HAND_017 = "HAND-017"; //$NON-NLS-1$

    /** You are unauthorized to perform such request. (HTTP=401, Unauthorized). */
    String HAND_018 = "HAND-018"; //$NON-NLS-1$

    /** The server has received your request, but didn't sent a response in return (HTTP=200, Accepted). */
    String HAND_019 = "HAND-019"; //$NON-NLS-1$

    /** Server seems to be shutdown has rejected your request. Check URL, check internet connection, check firewalls configuretion.  */
    String HAND_020 = "HAND-020"; //$NON-NLS-1$
    
    /** The server has rejected the request because it is incorrect. */
    String HAND_021 = "HAND-021"; //$NON-NLS-1$
    
    /** Runtime exception. */
    String HAND_022 = "HAND-022"; //$NON-NLS-1$

}
