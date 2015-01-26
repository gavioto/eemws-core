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

import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.ree.eemws.core.utils.messages.Messages;
import es.ree.eemws.core.utils.soap.SOAPUtil;
import es.ree.eemws.core.utils.xml.XMLUtil;

/**
 * Handles IEC-61968-100 messages as a string instead of as a set of beans classes.
 * This is specillay useful for handler procesing where the message is not already modeled as beans.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class StringBuilderMessage {

    /** Payload element name. */
    private static final String TAG_MSG_PAYLOAD = "Payload"; //$NON-NLS-1$
    
    /**  IEC-61968-100 message a string. */
    private StringBuilder messageStr = null;

    /**
     * Creates a new message from the soap message context.
     * @throws SOAPException If it's not possible to transform the incoming message into a string.
     */
    public StringBuilderMessage(final SOAPMessageContext context) throws GenericCodedException {
        try {
            messageStr = new StringBuilder(XMLUtil.getNodeValue(SOAPUtil.SOAP_BODY_TAG, new StringBuilder(SOAPUtil.soapMessage2String(context.getMessage()))));
        } catch (SOAPException e) {
            throw new GenericCodedException(Messages.getString("IEC_UNABLE_TO_RETRIEVE_FROM_CONTEXT"), "SBM-001", e);   //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * Creates an empty string builder message.
     */
    public StringBuilderMessage() {
        
    }
    
    /**
     * Sets this StringBuilder content with the content of other message. 
     * @param anotherMessage Other StringBuilder context.
     */
    public void setStringMessage(StringBuilderMessage anotherMessage) {
        messageStr = anotherMessage.getStringMessage();
    }
    
    
    /**
     * Returns the current IEC-61968-100 message as a String.
     * @return Current IEC-61968-100 message as a String.
     */
    public StringBuilder getStringMessage() {
        return messageStr;
    }

    /**
     * Returns this message header noun. <code>null</code> if there is no valid noun in the header.
     * @return Message header noun.
     */
    public EnumNoun getNoun() {
        EnumNoun retValue = null;
        if (messageStr != null) {
            String nounStr = XMLUtil.getNodeValue(EnumNoun.ELEMENT_NOUN, messageStr);
            retValue = EnumNoun.fromString(nounStr);
        }

        return retValue;
    }

    /**
     * Returns this message header verb.<code>null</code> if there is no valid verb in the header.
     * @return Message header verb.
     */
    public EnumVerb getVerb() {
        EnumVerb retValue = null;
        if (messageStr != null) {
            String verbStr = XMLUtil.getNodeValue(EnumVerb.ELEMENT_VERB, messageStr);
            retValue = EnumVerb.fromString(verbStr);
        }

        return retValue;
    }
    
    
    /**
     * Returns this message payload
     * @return Message paylaod or <code>null</code> if the current message has no payload.
     */
    public String getPayload() {
        String retValue = null;
        if (messageStr != null) {
            retValue = XMLUtil.getNodeValue(TAG_MSG_PAYLOAD, messageStr);
        }

        return retValue;
    }

    public EnumMessageStatus getStatus() {
        EnumMessageStatus retValue = null;
        if (messageStr != null) {
            String result = XMLUtil.getNodeValue(EnumMessageStatus.ELEMENT_RESULT, messageStr);
            retValue = EnumMessageStatus.fromString(result);
        }

        return retValue;        
    }
}
