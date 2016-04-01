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
package es.ree.eemws.core.utils.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import es.ree.eemws.core.utils.i18n.Messages;

/**
 * Class that contains utilities for XML messages.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class XMLUtil {

    /** Transform the document as XML (not text). */
    private static final String STRING_XML_METHOD = "xml"; //$NON-NLS-1$

    /** String format of a Document will be have UTF-8 codification. */
    private static final String STRING_XML_CODIFICATION = "UTF-8"; //$NON-NLS-1$

    /** Do not add xml declaration, the xml will be included into another!. */
    private static final String STRING_XML_OMIT_DECLARATION = "yes"; //$NON-NLS-1$

    /** XML namespace. */
    private static final String XMLNS = "xmlns"; //$NON-NLS-1$

    /** Double quote constant. */
    private static final char DOUBLE_QUOTE = '"';

    /** Simple quote constant. */
    private static final char SIMPLE_QUOTE = '\'';

    /** XML end tag character. */
    private static final String END_TAG = ">"; //$NON-NLS-1$

    /** XML start tag character. */
    private static final String START_TAG = "<"; //$NON-NLS-1$

    /** Start tag as char. */
    private static final char START_TAG_CHAR = START_TAG.charAt(0);

    /** XML start close tag character. */
    private static final String START_CLOSE_TAG = "</"; //$NON-NLS-1$

    /** Blank string. */
    private static final String BLANK = " "; //$NON-NLS-1$

    /** Emtpy string. */
    private static final String EMPTY = ""; //$NON-NLS-1$

    /** Colon character constant. */
    private static final String COLON = ":"; //$NON-NLS-1$

    /** Colon character constant as character. */
    private static final char COLON_CHAR = COLON.charAt(0);

    /** New line constant. */
    private static final String NEW_LINE = "\n"; //$NON-NLS-1$

    /** Slash character constant. */
    private static final String SLASH = "/"; //$NON-NLS-1$

    /** Tab size. */
    private static final int TAB_SIZE = 4;

    /** Size of header where namespaces are searched. */
    private static final int TNSNAME_SEARCH_SIZE = 3000;

    /**
     * Constructor.
     */
    private XMLUtil() {

        /* This method should not be implemented. */
    }

    /**
     * Gets the value of the node.
     * @param tag Tag name.
     * @param doc XML document in string.
     * @return Value of the node.
     */
    public static String getNodeValue(final String tag, final StringBuilder doc) {

        String retValue = null;
        int pos = -1;
        int totLen = doc.length();

        do {
            pos++;
            String tag1 = tag + END_TAG;
            String tag2 = tag + BLANK;
            int len = tag.length();
            int pos1 = doc.indexOf(tag1, pos);
            int pos2 = doc.indexOf(tag2, pos);
            String finalToken = EMPTY;

            if (pos1 == -1 && pos2 == -1) {
                pos = totLen;
            } else {

                if (pos2 == -1) {
                    if (pos1 != -1) {
                        pos = pos1;
                        len++;
                    }
                } else {
                    pos = pos2;
                    finalToken = END_TAG;
                }

                retValue = extractValue(doc, pos, len, finalToken);

                // No value returned, check the other posibility.
                if (retValue == null && pos2 != -1 && pos1 != -1) {
                    pos = pos1;
                    len++;
                    finalToken = EMPTY;

                    retValue = extractValue(doc, pos, len, finalToken);
                }
            }
        } while (pos < totLen && retValue == null);

        return retValue;
    }

    /**
     * Returns the element value from the current possition or <code>null</code> if the current possition is not an element (it's text).
     * @param doc Document with the value to extract
     * @param pos possition of the Tag.
     * @param len Tag's length.
     * @param finalToken End character to be used in the element search.
     * @return Element value from the current possition or <code>null</code> if the current possition is not an element (it's text).
     */
    private static String extractValue(final StringBuilder doc, final int pos, final int len, final String finalToken) {
        String retValue = null;

        int end = doc.indexOf(END_TAG, pos) + 1;
        if (end != -1) {
            int posBegin = pos - 1;
            while (doc.charAt(posBegin) != START_TAG_CHAR && posBegin > 0) {
                posBegin--;
            }

            String endTag = START_CLOSE_TAG + doc.substring(posBegin + 1, pos + len) + finalToken;
            int idx = doc.indexOf(endTag);
            if (idx != -1) {
                retValue = doc.substring(end, idx);
            }
        }
        return retValue;
    }

    /**
     * Returns an XML document from a String.
     * @param msgAsString String that contains an XML document to be transformed.
     * @return An XML document.
     * @throws ParserConfigurationException If the factory cannot transform the the string into a document.
     * @throws SAXException If the given string doesn't contain a valid XML document.
     * @throws IOException If the given string cannot be read.
     */
    public static Document string2Document(final String msgAsString) throws ParserConfigurationException, SAXException, IOException {

        return string2Document(new StringBuilder(msgAsString));
    }

    /**
     * Returns an XML document from a StringBuilder (String).
     * @param msgAsString String that contains an XML document to be transformed.
     * @return An XML document.
     * @throws ParserConfigurationException If the factory cannot transform the the string into a document.
     * @throws SAXException If the given string doesn't contain a valid XML document.
     * @throws IOException If the given string cannot be read.
     */
    public static Document string2Document(final StringBuilder msgAsString) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(msgAsString.toString())));
    }

    /**
     * Returns the string representation of a given XML document. Note that the return string us UTF-8 encoded and has no
     * XML declaration.
     * @param doc The input document to transform.
     * @return A string representation of the given XML document.
     * @throws TransformerException If it's impossible to convert the given document.
     */
    public static String document2String(final Document doc) throws TransformerException {

        StringWriter stringWriter = new StringWriter();

        StreamResult result = new StreamResult(stringWriter);

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.METHOD, STRING_XML_METHOD);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, STRING_XML_OMIT_DECLARATION);
        transformer.setOutputProperty(OutputKeys.ENCODING, STRING_XML_CODIFICATION);
        DOMSource domSource = new DOMSource(doc);
        transformer.transform(domSource, result);

        return stringWriter.toString();
    }

    /**
     * Gets a pretty print document using the format of the opening and closing tags.
     * @param whatToPretty Text formatting.
     * @return Document formatted as.
     */
    public static StringBuilder prettyPrint(final String whatToPretty) {

        StringBuilder output = new StringBuilder();

        try {
            String tab = NEW_LINE;

            String tabBlanks = EMPTY;
            StringBuilder buf = new StringBuilder();
            for (int cont = 0; cont < TAB_SIZE; cont++) {
                buf.append(BLANK);
            }
            tabBlanks = buf.toString();

            String[] text = whatToPretty.trim().split(END_TAG);

            String current = EMPTY;
            String previous = BLANK;
            int pos = 0;

            for (int cont = 0; cont < text.length; cont++) {

                current = text[cont].trim().replaceAll(NEW_LINE, BLANK);

                if (!current.endsWith(SLASH) && !current.startsWith(START_CLOSE_TAG)) {

                    if (current.startsWith(START_TAG)) {

                        tab += tabBlanks;

                    } else {

                        int k = output.length();
                        output.delete(k - tab.length(), k);
                        tab = tab.substring(0, tab.length() - TAB_SIZE);
                    }

                } else if (current.startsWith(START_CLOSE_TAG)) {

                    tab = tab.substring(0, tab.length() - TAB_SIZE);
                    int k = output.length();
                    output.delete(k - TAB_SIZE, k);

                    if (isSameTag(previous, current)) {

                        output.setLength(pos - 1);
                        current = SLASH;
                    }
                }

                previous = current;
                output.append(current);
                output.append(END_TAG);
                pos = output.length();
                output.append(tab);
            }

        } catch (IndexOutOfBoundsException e) { // NOSONAR The input string is not an XML
            
            output.setLength(0);
            output.append(whatToPretty);
        }

        return output;
    }

    /**
     * This method checks whether two XML tags are equal.
     * @param previousTag Previous tag.
     * @param currentTag Current tag.
     * @return true if both labels are equal.
     */
    private static boolean isSameTag(final String previousTag, final String currentTag) {

        String tag1 = previousTag.substring(1);
        if (tag1.indexOf(BLANK) > 0) {

            tag1 = tag1.substring(0, tag1.indexOf(BLANK));
        }

        String tag2 = currentTag.substring(2);
        if (tag2.indexOf(BLANK) > 0) {

            tag2 = tag2.substring(0, tag2.indexOf(BLANK));
        }

        return tag1.equals(tag2);
    }

    /**
     * This method removes namespace prefixes of a given document with leaving this default namespace.
     * Example:
     * input
     * <code>
     *   <n1:Tag xmnls:n1="http...." xmnls:n2="http...." xmnls:n3="http..." >
     *      <n1:OtherTag>
     *      ...
     *   </n1:Tag>
     * </code>
     *
     * output
     * <code>
     *   <Tag xmnls="http....">
     *      <OtherTag>
     *      ...
     *   </Tag>
     * </code>
     *
     * @param xml XML document to remove references to prefixes of namespaces.
     * @return XML document without namespaces.
     */
    public static StringBuilder removeNameSpaces(final String xml) {

        StringBuilder xmlOut = new StringBuilder(xml.trim());
        xmlOut = removeExtraNS(xmlOut.toString());

        String prefix = getTargetNameSpacePrefix(xmlOut);
        if (prefix != null) {

            String retXml = xmlOut.toString();
            retXml = retXml.replaceAll(prefix + COLON, EMPTY);
            retXml = retXml.replaceFirst(XMLNS + COLON + prefix, XMLNS);
            xmlOut = new StringBuilder(retXml);
        }

        return xmlOut;
    }

    /**
     * This method removes the given document, namespaces that are not referred to the document.
     * Example:
     *
     * input
     * <code>
     *   <n1:Tag xmlns:n1="http...." xmlns:n2="http...." xmlns:n3="http...">
     *   ...
     *   </n1:Tag>
     * </code>
     *
     * output
     * <code>
     * <n1:Tag xmlns:n1="http....">
     *   ...
     * </n1:Tag>
     * </code>
     *
     * @param xml XML document.
     * @return XML document without namespaces.
     */
    private static StringBuilder removeExtraNS(final String xml) {

        StringBuilder xmlOut = new StringBuilder(xml.trim());

        String ns = getTargetNameSpace(xmlOut);
        String prefix = getTargetNameSpacePrefix(xmlOut);

        if (prefix == null) {
            prefix = EMPTY;
        } else {
            prefix = COLON + prefix;
        }

        String newDoc = xml;

        int posEndTag = newDoc.indexOf(END_TAG);
        if (newDoc.charAt(posEndTag - 1) == '/') {
            posEndTag--;
        }

        String rootTag = newDoc.substring(0, posEndTag);
        String[] rootTags = rootTag.split("\\s"); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder(rootTags[0]);
        sb.append(BLANK + XMLNS + prefix + "=\"" + ns + DOUBLE_QUOTE + BLANK); //$NON-NLS-1$

        int len = rootTags.length;
        for (int i = 1; i < len; i++) {
            if (rootTags[i].indexOf(XMLNS) == -1) {
                sb.append(rootTags[i]);
                sb.append(BLANK);
            }
        }
        sb.setLength(sb.length() - 1);
        sb.append(xml.substring(posEndTag));

        return sb;
    }

    /**
     * This method gets the namespace prefix of the given XML document.
     * @param xml XML document.
     * @return null if there is no prefix (default namespace).
     * A string with the namespace prefix otherwise.
     */
    private static String getTargetNameSpacePrefix(final StringBuilder xml) {

        String prefix = null;

        String subXml;
        if (xml.length() > TNSNAME_SEARCH_SIZE) {
            subXml = xml.substring(0, TNSNAME_SEARCH_SIZE);
        } else {
            subXml = xml.toString();
        }
        subXml += END_TAG;

        int startPos = subXml.indexOf(START_TAG);
        int endPos1 = subXml.indexOf(BLANK);
        int endPos2 = subXml.indexOf(END_TAG);

        if (endPos1 == -1) {
            endPos1 = endPos2;
        }

        if (endPos2 == -1) {
            endPos2 = endPos1;
        }

        try {

            String tag = subXml.substring(startPos, Math.min(endPos1, endPos2));
            if (tag.indexOf(COLON) != -1) {
                prefix = tag.substring(tag.indexOf(START_TAG) + 1, tag.indexOf(COLON));
            }

        } catch (IndexOutOfBoundsException e) { // NOSONAR Do not throw exception, there is no prefix

            prefix = null;
        }

        return prefix;
    }

    /**
     * Searches a XML document root tag value given the document as a string.
     * We search from the end to the top (The last tag is also the root tag!)
     * There are serveral cases:
     * <li>(1) Simple end tag:  </etiqueta>
     * <li>(2) End tag with namespace prefix: </n1:etiqueta>
     * <li>(3) Empty document: <etiqueta/>
     * <li>(4) Empty document with namespace prefix: <n1:etiqueta xmnls:n1="abde"/>
     * @param strXml document as a string.
     * @return Xml's root tag as a String <code>null</code> if the given xml is not well formed (cannot find root tag).
     */
    public static String getRootTag(final StringBuilder strXml) {

        int posEndTag = strXml.lastIndexOf(END_TAG);
        int posCloseTag = strXml.lastIndexOf(SLASH);

        int end;
        if (posCloseTag == posEndTag - 1) {
            int posBlank = strXml.indexOf(BLANK);

            /* 3rd case: */
            if (posBlank == -1) {
                end = posCloseTag;
            } else {

                /* 4th case */
                end = posBlank;
            }
        } else {

            /* 1st & 2nd case. */
            end = posEndTag;
        }

        int start = end - 1;

        while (start >= 0 && strXml.charAt(start) != COLON_CHAR && strXml.charAt(start) != '/' && strXml.charAt(start) != START_TAG_CHAR) {
            start--;
        }

        String retValue;
        if (start < 0) {

            /* The given xml is not well formed. */
            retValue = null;
        } else {
            retValue = strXml.substring(start + 1, end);
        }

        return retValue;
    }

    /**
     * This method gets the "targetNamespace" XML document given.
     * Find if the root tag has space prefixed names or default and returns the string that defines the namespace accordingly.
     * @param xml XML document.
     * @return null if no namespace is. The value of the "targetNamespace" otherwise.
     */
    private static String getTargetNameSpace(final StringBuilder xml) {

        String targetNamespace = null;

        String subXml;
        if (xml.length() > TNSNAME_SEARCH_SIZE) {
            subXml = xml.substring(0, TNSNAME_SEARCH_SIZE);
        } else {
            subXml = xml.toString();
        }

        String prefix = getTargetNameSpacePrefix(xml);
        try {

            if (prefix == null) {

                int pos = subXml.indexOf(XMLNS);
                int pos2 = subXml.indexOf(XMLNS + COLON);

                if (pos != -1) {

                    while (pos == pos2 && pos != -1) {
                        pos = subXml.indexOf(XMLNS, pos + 1);
                        pos2 = subXml.indexOf(XMLNS + COLON, pos2 + 1);
                    }

                    if (pos != -1) {

                        pos = subXml.indexOf(DOUBLE_QUOTE, pos);
                        pos2 = subXml.indexOf(DOUBLE_QUOTE, pos + 1);
                        if (pos == -1) {

                            pos = subXml.indexOf(SIMPLE_QUOTE, pos);
                            pos2 = subXml.indexOf(SIMPLE_QUOTE, pos + 1);
                        }

                        targetNamespace = subXml.substring(pos + 1, pos2);
                    }
                }

            } else {

                prefix = XMLNS + COLON + prefix;
                int posIni = subXml.indexOf(prefix) + prefix.length();
                posIni = subXml.indexOf(DOUBLE_QUOTE, posIni) + 1;
                int posFin = subXml.indexOf(DOUBLE_QUOTE, posIni);
                if (posIni == -1) {

                    posIni = subXml.indexOf(prefix) + prefix.length();
                    posIni = subXml.indexOf(SIMPLE_QUOTE, posIni) + 1;
                    posFin = subXml.indexOf(SIMPLE_QUOTE, posIni);
                }

                targetNamespace = subXml.substring(posIni, posFin);
            }

        } catch (IndexOutOfBoundsException e) { // NOSONAR Do not throw exception, tns is null

            targetNamespace = null;
        }

        return targetNamespace;
    }
    
    
    /**
     * Transforms a SOAP message into a String.
     * @param message SOAP message.
     * @return String with the content of the SOAP message.
     * @throws SOAPException Exception transform the message.
     */
    public static String soapMessage2String(final SOAPMessage message) throws SOAPException {

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            message.writeTo(baos);
            return baos.toString(StandardCharsets.UTF_8.name()); 

        } catch (IOException e) {

            throw new SOAPException(Messages.getString("SOAP_UNABLE_TO_TRANSFORM"), e); //$NON-NLS-1$
        }
    }
}
