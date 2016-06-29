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

package es.ree.eemws.core.utils.iec61968100;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import ch.iec.tc57._2011.schema.message.HeaderType;
import ch.iec.tc57._2011.schema.message.OptionType;
import ch.iec.tc57._2011.schema.message.PayloadType;
import ch.iec.tc57._2011.schema.message.ReplyType;
import ch.iec.tc57._2011.schema.message.RequestMessage;
import ch.iec.tc57._2011.schema.message.RequestType;
import ch.iec.tc57._2011.schema.message.RequestType.ID;
import ch.iec.tc57._2011.schema.message.ResponseMessage;
import es.ree.eemws.core.utils.file.GZIPUtil;
import es.ree.eemws.core.utils.i18n.Messages;
import es.ree.eemws.core.utils.xml.XMLElementUtil;
import es.ree.eemws.core.utils.xml.XMLGregorianCalendarFactory;

/**
 * Miscellaneous utilities to handle messages.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.1 17/06/2016
 */
public final class MessageUtil {

    /** IEC 61968-100 schema file (it's included in <code>core.jar</code>. */
    private static final String IEC_61968_100_SCHEMA_FILE = "http-iec-ch-TC57-2011-schema-message.xsd"; //$NON-NLS-1$

    /**
     * Constructor. Utility classes should not have a public constructor.
     */
    private MessageUtil() {

        /* This constructor must not be implemented. */
    }

    /**
     * Creates a request message with the given options.
     * @param verb Request verb
     * @param noun Request noun
     * @param options Options to be included in the request.
     * @return Request message.
     */
    public static RequestMessage createRequestWithOptions(final EnumVerb verb, final EnumNoun noun, final Map<String, String> options) {
        return createRequestWithOptions(verb.toString(), noun.toString(), options);
    }

    /**
     * Creates a request message with the given options.
     * @param verb Request verb
     * @param noun Request noun
     * @param options Options to be included in the request.
     * @return Request message.
     */
    public static RequestMessage createRequestWithOptions(final String verb, final String noun, final Map<String, String> options) {

        RequestMessage message = new RequestMessage();

        message.setHeader(createHeader(verb, noun));

        RequestType resquest = new RequestType();

        if (options != null && !options.isEmpty()) {
            Set<Map.Entry<String, String>> entries = options.entrySet();

            for (Map.Entry<String, String> entry : entries) {

                try {

                    String key = entry.getKey();

                    if (key.equals(EnumFilterElement.START_TIME.toString())) {
                        DateFormat df = DateFormat.getInstance();
                        resquest.setStartTime(XMLGregorianCalendarFactory.getGMTInstance(df.parse(entry.getValue())));
                    } else if (key.equals(EnumFilterElement.END_TIME.toString())) {
                        DateFormat df = DateFormat.getInstance();
                        resquest.setEndTime(XMLGregorianCalendarFactory.getGMTInstance(df.parse(entry.getValue())));
                    } else {
                        OptionType option = createOption(key, entry.getValue());
                        resquest.getOptions().add(option);
                    }

                } catch (ParseException e) {

                    /* Ignore invalid date entries. */
                    Logger.getLogger(".").fine("Invalid date parameter [" + entry.getKey() + "=" + entry.getValue() + "]"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
                }
            }
        }
        message.setRequest(resquest);

        return message;
    }

    /**
     * Returns a <code>Map</code> with the given RequestMessage options. The start time and end time are also included in the map.
     * @param message Request message.
     * @return a Map with the options that the given RequestMessage has.
     */
    public static Map<String, Object> getRequestMessageOptions(final RequestMessage message) {
        return getInternalRequestMessageOptions(message, false);
    }

    /**
     * Returns a <code>Map</code> with the given RequestMessage options ignoring invalid date format parameters and duplicates.
     * The start time and end time are also included in the map.
     * @param message Request message.
     * @return a Map with the options that the given RequestMessage has.
     */
    public static Map<String, Object> getRequestMessageOptionsAllowingErrors(final RequestMessage message) {
        return getInternalRequestMessageOptions(message, true);
    }

    /**
     * Returns a <code>Map</code> with the given RequestMessage options. The start time and end time are also included in the map.
     * @param message Request message.
     * @param allowInvalidValues allow invalid date format and duplicate values.
     * @return a Map with the options that the given RequestMessage has.
     */
    private static Map<String, Object> getInternalRequestMessageOptions(final RequestMessage message, final boolean allowInvalidValues) {

        Map<String, Object> retValue = new HashMap<>();

        boolean stopIfError = !allowInvalidValues;

        RequestType msgRequest = message.getRequest();

        if (msgRequest == null) {
            if (stopIfError) {
                throw new IllegalArgumentException(Messages.getString("REQUEST_MESSAGE_HAS_NO_REQUEST")); //$NON-NLS-1$
            }
        } else {

            XMLGregorianCalendar time;

            String startElementStr = EnumFilterElement.START_TIME.toString();
            String endElementStr = EnumFilterElement.END_TIME.toString();

            time = msgRequest.getStartTime();
            if (time != null) {
                retValue.put(startElementStr, time);
            }

            time = msgRequest.getEndTime();
            if (time != null) {
                retValue.put(endElementStr, time);
            }

            List<OptionType> msgOptions = msgRequest.getOptions();

            if (msgOptions != null) {
                for (OptionType optionType : msgOptions) {

                    String optName = optionType.getName();
                    Object optValue = optionType.getValue();
                    Object obj = null;

                    /*
                     * It's recommended to use the IEC 61968-100 elements where posible. Here we are giving a facility
                     * to the user that could use StartTime and EndTime as Option instead of elements.
                     */
                    if (optName.equals(startElementStr) || optName.equals(endElementStr)) {
                        try {
                            optValue = XMLGregorianCalendarFactory.getInstance((String) optValue);
                        } catch (ParseException e) {
                            if (stopIfError) {
                                throw new IllegalArgumentException(Messages.getString("INVALID_DATE_PARAMETER_VALUE", optName)); //$NON-NLS-1$
                            }
                        }
                    }

                    obj = retValue.put(optName, optValue);

                    /* If obj is not null, the parameter was already in the map. */
                    if (obj != null) {
                        if (stopIfError) {
                            throw new IllegalArgumentException(Messages.getString("INVALID_PARAMETER_TWICE", optName)); //$NON-NLS-1$
                        }

                        /* Add repeated option to the output in order to trace the request. */
                        int cont = 1;
                        StringBuilder key = new StringBuilder();
                        key.append(optName).append("(").append(cont).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
                        while (retValue.containsKey(key.toString())) {
                            cont++;
                            key.setLength(0);
                            key.append(optName).append("(").append(cont).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
                        }

                        retValue.put(key.toString(), obj);
                    }
                }
            }
        }

        return retValue;
    }

    /**
     * Creates a message header with the given verb and noun.
     * @param verb Message verb.
     * @param noun Message noun.
     * @return Message header with the given verb and noun.
     */
    private static HeaderType createHeader(final String verb, final String noun) {
        HeaderType header = new HeaderType();
        header.setVerb(verb);
        header.setNoun(noun);
        header.setTimestamp(XMLGregorianCalendarFactory.getGMTInstanceMs(new Date()));

        return header;
    }

    /**
     * Creates a new option.
     * @param name Name of the new option.
     * @param value Value of the new option. Value is optional, so its value can be <code>null</code> if not set.
     * @return New option.
     */
    private static OptionType createOption(final String name, final String value) {

        OptionType option = new OptionType();
        option.setName(name);

        if (value != null) {
            option.setValue(value);
        }

        return option;
    }

    /**
     * Creates a request message with a payload.
     * @param verb Message verb.
     * @param noun Message noun.
     * @param xmlMessage Document payload.
     * @return Request message.
     * @throws ParserConfigurationException If the current configuration does not support xml parsing.
     * @throws SAXException If the given payload is not well formed.
     * @throws IOException If it is not possible to read the given xml payload.
     */
    public static RequestMessage createRequestWithPayload(final String verb, final String noun,
            final StringBuilder xmlMessage) throws ParserConfigurationException, SAXException, IOException {

        RequestMessage message = new RequestMessage();

        message.setHeader(createHeader(verb, noun));

        Element xml = XMLElementUtil.string2Element(xmlMessage.toString());
        PayloadType payload = new PayloadType();
        payload.getAnies().add(xml);
        message.setPayload(payload);

        return message;

    }

    /**
     * Creates a request message with compressed xml message.
     * @param xmlMessage Xml document (payload) to be transmited.
     * @return Request message With with the given xml in compressed format.
     * @throws IOException If the given xml cannot be compressed.
     */
    public static RequestMessage createRequestWithCompressedXmlPayload(final StringBuilder xmlMessage) throws IOException {
        byte[] compressedPayload = GZIPUtil.compress(xmlMessage.toString().getBytes(StandardCharsets.UTF_8));

        return createRequestWithBinaryPayload(null, compressedPayload, EnumMessageFormat.XML);
    }

    /**
     * Creates a request message with binary payload.
     * @param name Name for binary files. For compressed XML documents this value is not necessary (name will be taken form the xml document during procesing)
     * @param binaryB64 Binary data in B64.
     * @param format Optional value for the binary format. if <code>null</code> XML will be used.
     * @return Request message With binary data.
     */
    public static RequestMessage createRequestWithBinaryPayload(final String name, final StringBuilder binaryB64, final EnumMessageFormat format) {

        RequestMessage message = new RequestMessage();
        HeaderType header = createHeader(EnumVerb.CREATE.toString(), EnumNoun.COMPRESSED.toString());
        message.setHeader(header);

        if (name != null) {

            RequestType resquest = new RequestType();

            ID id = new ID();
            id.setIdType(EnumMessageFormat.BINARY_FILENAME_ID);
            id.setValue(name);

            List<ID> ids = resquest.getIDS();
            ids.add(id);

            message.setRequest(resquest);
        }

        PayloadType payload = new PayloadType();
        payload.setCompressed(binaryB64.toString());

        if (format == null) {
            payload.setFormat(EnumMessageFormat.XML.toString());
        } else {
            payload.setFormat(format.toString());
        }

        message.setPayload(payload);

        return message;
    }

    /**
     * Creates a request message with binary payload.
     * @param name Binary name (file name).
     * @param binary Binary data as byte[].
     * @param format Optional value for the binary format. if <code>null</code> XML will be used.
     * @return Request message With binary data.
     */
    public static RequestMessage createRequestWithBinaryPayload(final String name, final byte[] binary, final EnumMessageFormat format) {

        return createRequestWithBinaryPayload(name, new StringBuilder(DatatypeConverter.printBase64Binary(binary)), format);
    }

    /**
     * Creates a <code>ResponseMessage</code> with the given parameters and payload.
     * @param noun Header noun.
     * @param status Header status
     * @param msgPayload Message payload
     * @return a Response message with payload.
     */
    public static ResponseMessage createResponseWithPayload(final EnumNoun noun, final EnumMessageStatus status, final Element msgPayload) {

        return createResponseWithPayload(noun.toString(), status, msgPayload);
    }

    /**
     * Creates a <code>ResponseMessage</code> with the given parameters and payload.
     * @param noun Header noun.
     * @param status Header status
     * @param msgPayload Message payload
     * @return a Response message with payload.
     */
    public static ResponseMessage createResponseWithPayload(final String noun, final EnumMessageStatus status, final Element msgPayload) {
        ResponseMessage response = new ResponseMessage();

        response.setHeader(createHeader(EnumVerb.REPLY.toString(), noun));

        ReplyType reply = new ReplyType();

        reply.setResult(status.getStatus());
        response.setReply(reply);
        PayloadType payLoad = new PayloadType();
        response.setPayload(payLoad);

        payLoad.getAnies().add(msgPayload);

        return response;
    }
    
    /**
     * Creates an empty response (with no payload).
     * This method is usefull to send back responses to the client when:
     * <li>No response is expected: For instance, if the user has send an acknowledgement.
     * <li>Server works in asynchronous mode, sending back a response as soon as it receives a message. 
     * @param status Response's status OK / FAILED.
     * @return An empty resopnse.
     */
    public static ResponseMessage createResponseWithNoPayload(final EnumMessageStatus status) {
        ResponseMessage response = new ResponseMessage();

        response.setHeader(createHeader(EnumVerb.REPLY.toString(), EnumNoun.EMPTY.toString()));

        ReplyType reply = new ReplyType();

        reply.setResult(status.getStatus());
        response.setReply(reply);
        PayloadType payLoad = new PayloadType();
        response.setPayload(payLoad);

        return response;
    }

    /**
     * Creates a ResponseMessage with a binary content.
     * @param status Message status (OK, FAILED)
     * @param name Binary file name.
     * @param binary Binary context as byte[].
     * @param format Binary format. if <code>null</code> EnumMessageFormat.XML is set.
     * @return ResponseMessage with binary content.
     */
    public static ResponseMessage createResponseWithBinaryPayload(final EnumMessageStatus status, final String name, final byte[] binary, final EnumMessageFormat format) {

        return createResponseWithBinaryPayload(status, name, new StringBuilder(DatatypeConverter.printBase64Binary(binary)), format);
    }

    /**
     * Creates a ResponseMessage with a binary content.
     * @param status Message status (OK, FAILED)
     * @param name Binary file name.
     * @param binaryB64 Binary context in B64.
     * @param format Binary format. if <code>null</code> EnumMessageFormat.XML is set.
     * @return ResponseMessage with binary content.
     */
    public static ResponseMessage createResponseWithBinaryPayload(final EnumMessageStatus status, final String name, 
            final StringBuilder binaryB64, final EnumMessageFormat format) {

        ResponseMessage response = new ResponseMessage();

        response.setHeader(createHeader(EnumVerb.REPLY.toString(), EnumNoun.COMPRESSED.toString()));

        ReplyType reply = new ReplyType();

        reply.setResult(status.getStatus());
        ch.iec.tc57._2011.schema.message.ReplyType.ID id = new ch.iec.tc57._2011.schema.message.ReplyType.ID();
        reply.getIDS().add(id);
        id.setIdType(EnumMessageFormat.BINARY_FILENAME_ID);
        id.setValue(name);

        response.setReply(reply);

        PayloadType payLoad = new PayloadType();
        response.setPayload(payLoad);

        payLoad.setCompressed(binaryB64.toString());

        if (format == null) {
            payLoad.setFormat(EnumMessageFormat.XML.toString());
        } else {
            payLoad.setFormat(format.toString());
        }

        return response;
    }

    /**
     * Validates against schema the given IEC 61968-100 message.
     * Note that schema validation has impact on the performance.
     * @param msg A StringBuilderMessage with 61968-100 message.
     * @throws SAXException If the message is not valid against schema.
     */
    public static void validateMessage(final StringBuilderMessage msg) throws SAXException {
        validateMessage(msg.getStringMessage());
    }

    /**
     * Validates against schema the given IEC 61968-100 message.
     * Note that schema validation has impact on the performance.
     * @param stringMessage A String representation of the xml message
     * @throws SAXException If the message is not valid against schema.
     */
    public static void validateMessage(final StringBuilder stringMessage) throws SAXException {

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL schemaUrl = loader.getResource(IEC_61968_100_SCHEMA_FILE);

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaUrl);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(stringMessage.toString())));

        } catch (IOException | NullPointerException e) {

            /* Ignore IOException. */
            Logger.getLogger(".").log(Level.FINE, "Unable to read message", e); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    
    /**
     * Returns the operation response's payload content as a String.
     * @param responseMessage Response message received from server.
     * @return Operation response payload as string. 
     * If the servers has returned no payload (for asynchronous communication) <code>null</code> is returned. 
     * @throws TransformerException If the response cannot be transformed as an String.
     * @throws ParserConfigurationException If the response cannot be transformed as an string.
     */
    public static String responsePayload2String(final ResponseMessage responseMessage) throws TransformerException, ParserConfigurationException {
        String retValue = null;
        
        PayloadType payload = responseMessage.getPayload();

        List<Element> anies = payload.getAnies();
        
        if (!anies.isEmpty()) { 
        
            retValue = XMLElementUtil.element2String(payload.getAnies().get(0));
            
        }

        return retValue;
    }
    
}
