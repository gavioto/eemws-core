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
 * @version 1.1 11/10/2015
 */
public final class StringBuilderMessage {

    /** Payload element name. */
    private static final String TAG_MSG_PAYLOAD = "Payload"; //$NON-NLS-1$
    
    /** Format element name. */
    private static final String TAG_MSG_FORMAT = "Format"; //$NON-NLS-1$
    
    /** Format tag end for <n1:Format/>  and <Format/> (empty) cases. */
    private static final String EMPTY_FORMAT_TAG_END = TAG_MSG_FORMAT + "/>"; //$NON-NLS-1$
    
    /* <Format>asdf</Format> and  <n1:Format>asdf</n1:Format> */
    private static final String NON_EMPTY_FORMAT_TAG_END = TAG_MSG_FORMAT + ">"; //$NON-NLS-1$
    
    /** Fault element name. */
    private static final String TAG_MSG_FAULT = "FaultMessage";

    /** Fault details element name. */
    private static final String TAG_DETAILS_FAULT = "details";

    /** Reduce the payload in order to search string without memory waste. */
    private static final int MAX_LENGTH_TEST = 100;

    /** XML start tag character. */
    private static final int START_TAG_CHAR = '<'; 
    
    /**  IEC-61968-100 message a string. */
    private StringBuilder messageStr = null;

    /**
     * Creates a new message from the soap message context.
     * @throws SOAPException If it's not possible to transform the incoming message into a string.
     */
    public StringBuilderMessage(final SOAPMessageContext context) throws SOAPException {
        try {
            messageStr = new StringBuilder(XMLUtil.getNodeValue(SOAPUtil.SOAP_BODY_TAG, new StringBuilder(SOAPUtil.soapMessage2String(context.getMessage()))));
        } catch (NullPointerException e) {
            throw new SOAPException(Messages.getString("IEC_UNABLE_TO_RETRIEVE_FROM_CONTEXT"));   //$NON-NLS-1$
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
        return getElement(EnumNoun.ELEMENT_NOUN);
    }
    
    /**
     * Returns this message header verb.<code>null</code> if there is no valid verb in the header.
     * @return Message header verb.
     */
    public String getVerb() {
        return getElement(EnumVerb.ELEMENT_VERB);
    }
    
    /**
     * Returns the node value of the given element.
     * @param elementName Element (tag) name which value we want to retrieve.
     * @return <code>null</code> if there is no such element in the current xml or
     * the element value if exists.
     */
    private String getElement(final String elementName) {
        String retValue = null;
        if (messageStr != null) {
            retValue = XMLUtil.getNodeValue(elementName, messageStr);
            
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
     * Note that the Format element (if present) is not returned.
     * @return Message payload or <code>null</code> if the current message has no payload.
     */
    public String getPayload() {
        String payL = getElement(TAG_MSG_PAYLOAD);
        
        
        String testStr;
        int payLen = payL.length();
        if (payLen > MAX_LENGTH_TEST) {
            testStr = payL.substring(payLen - MAX_LENGTH_TEST, payLen).trim();
        } else {
            testStr = payL.trim();
        }
        
        if (testStr.endsWith(EMPTY_FORMAT_TAG_END)) {
            payL = payL.substring(0, payL.lastIndexOf(START_TAG_CHAR));
        } else if (testStr.endsWith(NON_EMPTY_FORMAT_TAG_END)) {
            int k = payL.lastIndexOf(START_TAG_CHAR) - 1;
            while (payL.charAt(k) != START_TAG_CHAR && k > 0) {
                k--;
            }
            
            payL = payL.substring(0, k);
        }
      
        return payL;
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
                FaultMessage fm = (FaultMessage) XMLElementUtil.element2Obj(XMLElementUtil.string2Element(XMLUtil.getNodeValue(TAG_MSG_FAULT, messageStr)), FaultMessage.class);
                
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
        String result = getElement(EnumMessageStatus.ELEMENT_RESULT);
        if (result != null) {
            retValue = EnumMessageStatus.fromString(result);
        }

        return retValue;        
    }
 
}
