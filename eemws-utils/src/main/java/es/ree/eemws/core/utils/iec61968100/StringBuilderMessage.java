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

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.xml.sax.SAXException;

import _504.iec62325.wss._1._0.MsgFaultMsg;
import ch.iec.tc57._2011.schema.message.FaultMessage; 
import es.ree.eemws.core.utils.messages.Messages;
import es.ree.eemws.core.utils.soap.SOAPUtil;
import es.ree.eemws.core.utils.xml.XMLElementUtil;
import es.ree.eemws.core.utils.xml.XMLUtil;

/**
 * Handles IEC-61968-100 messages as a string instead of as a set of beans classes.
 * This is specillay useful for handler procesing where the message is not already modeled as beans.
 * Note that IEC-61968-100 message could be a RequestMessage a ResponseMessage or a FaultMessage
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class StringBuilderMessage {

    /** Payload element name. */
    private static final String TAG_MSG_PAYLOAD = "Payload"; //$NON-NLS-1$
    
    /** Fault element name. */
    private static final String TAG_MSG_FAULT = "FaultMessage";

    /** Fault details element name. */
    private static final String TAG_DETAILS_FAULT = "details"; 
    
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
        
        /* This constructor must be empty. */
    }
    
    /**
     * Sets this StringBuilder content with the content of other message. 
     * @param anotherMessage Other StringBuilder context.
     */
    public void setStringMessage(final StringBuilderMessage anotherMessage) {
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
    public String getNoun() {
        String retValue = null;
        if (messageStr != null) {
            retValue = XMLUtil.getNodeValue(EnumNoun.ELEMENT_NOUN, messageStr);
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
     * Returns the current IEC-61968-100 message without payload.
     * @return Current IEC-61968-100 message without payload. <code>null</code> 
     * if the current message is null.
     * @see #getStringPayLoad(StringBuilder)
     */
    public String getStringMessageNoPayload() {
        String retValue;
        if (messageStr == null) {
            retValue = null;
        } else {
            String payLoad = getPayload();
            int pos = messageStr.indexOf(payLoad);
            if (pos == -1) {
                retValue = messageStr.toString();
            } else {
                retValue = messageStr.substring(0, pos) + messageStr.substring(pos + payLoad.length()); 
            }
        }
        
        return retValue;
    }
    
    /**
     * Returns this message's payload. Note that for a Fault messages {@link #getFault()} must be used.
     * @return Message payload or <code>null</code> if the current message has no payload.
     */
    public String getPayload() {
        String retValue = null;
        if (messageStr != null) {
            retValue = XMLUtil.getNodeValue(TAG_MSG_PAYLOAD, messageStr);
        }

        return retValue;
    }
    
    /**
     * Returns this message Fault as a Fault object.  
     * @return <code>null</code> if the message is empty or if it is not a Fault message. A <code>MsgFaultMsg</code>
     * object otherwise.
     */
    public MsgFaultMsg getFault() {
        MsgFaultMsg retValue = null;
        
        if (messageStr != null) {
            try {                
                FaultMessage fm = (FaultMessage) XMLElementUtil.elment2Obj(XMLElementUtil.string2Element(XMLUtil.getNodeValue(TAG_MSG_FAULT, messageStr)), FaultMessage.class);
                
                String msg = XMLUtil.getNodeValue(TAG_DETAILS_FAULT, messageStr);
                if (msg == null) {
                    msg = "";
                }
                
                retValue = new MsgFaultMsg(msg, fm);
            } catch (JAXBException | ParserConfigurationException | SAXException | IOException e) {
                
                /* The current document is not a Fault. */
                retValue = null;
            }
        }
        
        return retValue;        
    }

    /**
     * Returns this message's status value. Note that fault messages has no status value.
     * @return This message's status value. <code>null</code> if the message is empty or has no status.
     */
    public EnumMessageStatus getStatus() {
        EnumMessageStatus retValue = null;
        if (messageStr != null) {
            String result = XMLUtil.getNodeValue(EnumMessageStatus.ELEMENT_RESULT, messageStr);
            retValue = EnumMessageStatus.fromString(result);
        }

        return retValue;        
    }
}
