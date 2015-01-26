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
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;

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
import es.ree.eemws.core.utils.xml.XMLElementUtil;
import es.ree.eemws.core.utils.xml.XMLGregorianCalendarFactory;

/**
 * Miscellaneous utilities to handle message.
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */

public class MessageUtil {

    /** Name of the name of the binary file. */
    private static final String BINARY_FILENAME_ID = "name"; //$NON-NLS-1$

    /**
     * Constructor. Utility classes should not have a public constructor.
     */
    private MessageUtil() {

        /* This constructor must not be implemented. */
    }

    /**
     * Creates a <code>ResponseMessage</code> with the given parameters and payload.
     * @param verb Header verb.
     * @param noun Header noun.
     * @param status Header status
     * @param msgPayload Message payload
     * @return a Response message with payload.
     */
    public static ResponseMessage createResponseWithPayload(final EnumVerb verb, final EnumNoun noun, final EnumMessageStatus status, final Element msgPayload) {

        return createResponseWithPayload(verb.toString(), noun.toString(), status, msgPayload);
    }

    /**
     * Creates a <code>ResponseMessage</code> with the given parameters and payload.
     * @param verb Header verb.
     * @param noun Header noun.
     * @param status Header status
     * @param msgPayload Message payload
     * @return a Response message with payload.
     */
    public static ResponseMessage createResponseWithPayload(final String verb, final String noun, final EnumMessageStatus status, final Element msgPayload) {
        ResponseMessage response = new ResponseMessage();

        response.setHeader(createHeader(verb, noun));

        ReplyType reply = new ReplyType();

        reply.setResult(status.getStatus());
        response.setReply(reply);
        PayloadType payLoad = new PayloadType();
        response.setPayload(payLoad);

        payLoad.getAnies().add(msgPayload);

        return response;
    }

    /**
     * This method creates a request message with the given options.
     * @param verb Rquest verb
     * @param noun Request noun
     * @param options Options to be included in the request. 
     * @return Request message.
     */
    public static RequestMessage createRequestWithOptions(final EnumVerb verb, final EnumNoun noun, final Map<String, String> options) {
        return createRequestWithOptions(verb.toString(), noun.toString(), options);
    }

    /**
     * This method creates a request message with the given options.
     * @param verb Rquest verb
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
     * Returns a <code>Map</code> with the given RequestMessage options. The start time and end time are also included
     * in the map.
     * @param message Request message.
     * @return a Map with the options that the given RequestMessage has.
     */
    public static Map<String, Object> getRequestMessageOptions(final RequestMessage message) {

        List<OptionType> requestOption = message.getRequest().getOptions();
        Map<String, Object> map = new HashMap<String, Object>();

        for (OptionType optionType : requestOption) {
            map.put(optionType.getName(), optionType.getValue());
        }

        XMLGregorianCalendar time;

        time = message.getRequest().getStartTime();
        if (time != null) {
            map.put(EnumFilterElement.START_TIME.toString(), time);
        }

        time = message.getRequest().getEndTime();
        if (time != null) {
            map.put(EnumFilterElement.END_TIME.toString(), time);
        }

        return map;
    }

   

    /**
     * Creates a message header with the given verb and noun.
     * @param verb Message verb.
     * @param noun Message noun.
     * @return Message header with the given verb and noun.
     */
    private static HeaderType createHeader(final String verb, final String noun) {
        HeaderType headerResponse = new HeaderType();
        headerResponse.setVerb(verb);
        headerResponse.setNoun(noun);
        headerResponse.setTimestamp(XMLGregorianCalendarFactory.getGMTInstanceMs(new Date()));

        return headerResponse;
    }

    /**
     * Creates a new option.
     * @param name Name of the new option.
     * @param value Value of the new option. Value is optional, so its value can be <code>null</code> if not set.
     * @return New option.
     */
    private static final OptionType createOption(final String name, final String value) {

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
    public static RequestMessage createRequestWithPayload(String verb, String noun, StringBuilder xmlMessage) throws ParserConfigurationException, SAXException, IOException {

        RequestMessage message = new RequestMessage();

        message.setHeader(createHeader(verb, noun));

        Element xml = XMLElementUtil.string2Element(xmlMessage.toString());
        PayloadType payload = new PayloadType();
        payload.getAnies().add(xml);
        message.setPayload(payload);

        return message;

    }
    

    /**
     * Creates a request message with binary payload. The payload will be GZIP compressed.
     * @param verb Message verb.
     * @param noun Message noun.
     * @param name Binary name (file name).
     * @param data Binary data as byte array.
     * @param format Optional value for the binary format (can be <code>null</code>).
     * @return Request message.
       * @throws IOException If it is not possible to compress the given byte array.
     */
    public static RequestMessage createResponseWithBinaryPayload(String verb, String noun, String name, byte[] data, String format) throws IOException {
        RequestMessage message = new RequestMessage();

        HeaderType header = createHeader(verb, noun);
        message.setHeader(header);

        RequestType resquest = new RequestType();

        ID id = new ID();
        id.setIdType(BINARY_FILENAME_ID);
        id.setValue(name);

        List<ID> ids = resquest.getIDS();
        ids.add(id);

        message.setRequest(resquest);

        PayloadType payload = new PayloadType();
        byte[] dataCompress = GZIPUtil.compress(data);
        String dataBase64 = DatatypeConverter.printBase64Binary(dataCompress);
        payload.setCompressed(dataBase64);
        if (format != null) {
            payload.setFormat(format);
        }
        message.setPayload(payload);

        return message;

    }

}
