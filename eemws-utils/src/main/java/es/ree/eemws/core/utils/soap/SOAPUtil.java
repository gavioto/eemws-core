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
package es.ree.eemws.core.utils.soap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import es.ree.eemws.core.utils.xml.XMLUtil;


/**
 * Class that contains utilities for SOAP messages management.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class SOAPUtil {

    /** Name space SOAP Envelope. */
    private static final String SOAP_NS_PREFIX = "SOAP-ENV";

    /** Content type for soap 1.2. */
    private static final String SOAP_12_CONTENT_TYPE = "application/soap+xml";

    /** Name space type for soap 1.2. */
    private static final String SOAP_12_NAME_SPACE = "http://www.w3.org/2003/05/soap-envelope";

    /** Name space type for soap 1.1. */
    private static final String SOAP_11_NAME_SPACE = "http://schemas.xmlsoap.org/soap/envelope/";

    /** Content type for the headers. */
    private static final String[] CONTENT_TYPE_HEADERS = {"content-type", "accept"};

    /**
     * Constructor.
     */
    private SOAPUtil() {

        /* This method should not be implemented. */
    }

    /**
     * This method transform a SOAP message into a String.
     * @param message SOAP message.
     * @return String with the content of the SOAP message.
     * @throws SOAPException Exception transform the message.
     */
    public static String soapMessage2String(final SOAPMessage message) throws SOAPException {

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            message.writeTo(baos);
            return baos.toString("UTF-8");

        } catch (IOException e) {

            throw new SOAPException("Unable to transform SOAP messagge to string", e);
        }
    }

    /**
     * This method sets the body text into a SOAPMessage.
     * @param message SOAPMessage to modify.
     * @param body Text with the new body part.
     * @throws SOAPException Exception with the problem.
     */
    @SuppressWarnings("unchecked")
    public static void setSOAPMessage(final SOAPMessage message, final StringBuilder body) throws SOAPException {

        MimeHeaders mimeHeaders = message.getMimeHeaders();
        boolean soap12 = false;

        if (mimeHeaders != null) {

            Iterator<MimeHeader> itrHeaders = mimeHeaders.getMatchingHeaders(CONTENT_TYPE_HEADERS);
            while (itrHeaders.hasNext() && !soap12) {

                MimeHeader header = itrHeaders.next();
                soap12 = header.getValue().indexOf(SOAP_12_CONTENT_TYPE) != -1;
            }
        }

        message.getSOAPPart().setContent(new DOMSource(getDocumentSoap(soap12, body)));
    }

    /**
     * This method create a SOAP message with the body content.
     * @param soap12 Check to indicate SOAP 1.2
     * @param message Message to include in the body part.
     * @return SOAP message with the body content.
     * @throws SOAPException Exception with the problem.
     */
    private static Document getDocumentSoap(final boolean soap12, final StringBuilder message) throws SOAPException {

        try {

            StringBuilder sb = new StringBuilder();

            /* Envelope. */
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            sb.append("<");
            sb.append(SOAP_NS_PREFIX);
            sb.append(":Envelope xmlns:");
            sb.append(SOAP_NS_PREFIX);
            sb.append("=\"");
            if (soap12) {
                sb.append(SOAP_12_NAME_SPACE);
            } else {
                sb.append(SOAP_11_NAME_SPACE);
            }
            sb.append("\" ");
            sb.append(" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\">");

            /* Body. */
            sb.append("<");
            sb.append(SOAP_NS_PREFIX);
            sb.append(":Body>\n");
            sb.append(message.toString());

            /* Close Envelope. */
            sb.append("\n</");
            sb.append(SOAP_NS_PREFIX);
            sb.append(":Body>\n</");
            sb.append(SOAP_NS_PREFIX);
            sb.append(":Envelope>\n");

            return XMLUtil.string2Document(sb.toString());

        } catch (ParserConfigurationException | SAXException | IOException e) {

            throw new SOAPException("Unable to create the SOAP messagge", e);
        }
    }
}
