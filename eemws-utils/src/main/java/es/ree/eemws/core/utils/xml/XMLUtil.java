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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Class that contains utilities for XML messages.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class XMLUtil {

    /** Transform the document as XML (not text). */
    private static final String STRING_XML_METHOD = "xml";

    /** String format of a Document will be have UTF-8 codification. */
    private static final String STRING_XML_CODIFICATION = "UTF-8";

    /** Do not add xml declaration, the xml will be included into another!. */
    private static final String STRING_XML_OMIT_DECLARATION = "yes";

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
     * This method get the value of the node.
     * @param tag Tag name.
     * @param doc XML document in string.
     * @return Value of the node.
     */
    public static String getNodeValue(final String tag, final String doc) {

        String tag1 = tag + ">";
        String tag2 = tag + " ";
        int len = tag.length();
        int pos1 = doc.indexOf(tag1);
        int pos2 = doc.indexOf(tag2);
        int pos = 0;
        String finalTag = "";

        if ((pos1 == -1) && (pos2 == -1)) {
            return null;
        }

        if ((pos1 != -1) && (pos2 != -1)) {
            pos = pos2;
            finalTag = ">";
        }

        if ((pos1 == -1) && (pos2 != -1)) {
            pos = pos2;
            finalTag = ">";
        }

        if ((pos1 != -1) && (pos2 == -1)) {
            pos = pos1;
            len++;
        }

        int end = doc.indexOf(">", pos) + 1;
        int posBegin = pos - 1;
        while (doc.charAt(posBegin) != '<' && posBegin > 0) {
            posBegin--;
        }

        String endTag = "</" + doc.substring(posBegin + 1, pos + len) + finalTag;
        return doc.substring(end, doc.indexOf(endTag));
    }

    /**
     * Return an XML document from a String.
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
     * Return an XML document from a StringBuilder (String).
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
     * Return the string representation of a given XML document. Note that the return string us UTF-8 encoded and has no
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
     * This method tabulates the document reference given using the format of the opening and closing tags.
     * @param whatToPretty Text formatting.
     * @return Document formatted as.
     */
    public static StringBuilder prettyPrint(final String whatToPretty) {

    	StringBuilder output = new StringBuilder();

        String tab = "\n";

        String tabBlanks = "";
        StringBuilder buf = new StringBuilder();
        for (int cont = 0; cont < TAB_SIZE; cont++) {
            buf.append(' ');
        }
        tabBlanks = buf.toString();

        String[] text = whatToPretty.trim().split(">");

        String current = "";
        String previous = " ";
        int pos = 0;

        for (int cont = 0; cont < text.length; cont++) {

            current = text[cont].trim().replaceAll("\n", " ");

            if (!current.endsWith("/") && !current.startsWith("</")) {

                if (current.startsWith("<")) {

                    tab += tabBlanks;

                } else {

                    int k = output.length();
                    output.delete(k - tab.length(), k);
                    tab = tab.substring(0, tab.length() - TAB_SIZE);
                }

            } else if (current.startsWith("</")) {

                tab = tab.substring(0, tab.length() - TAB_SIZE);
                int k = output.length();
                output.delete(k - TAB_SIZE, k);

                if (isSameTag(previous, current)) {

                    output.setLength(pos - 1);
                    current = "/";
                }
            }

            previous = current;
            output.append(current);
            output.append('>');
            pos = output.length();
            output.append(tab);
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
        if (tag1.indexOf(" ") > 0) {

            tag1 = tag1.substring(0, tag1.indexOf(" "));
        }

        String tag2 = currentTag.substring(2);
        if (tag2.indexOf(" ") > 0) {

            tag2 = tag2.substring(0, tag2.indexOf(" "));
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
            retXml = retXml.replaceAll(prefix + ":", "");
            retXml = retXml.replaceFirst("xmlns:" + prefix, "xmlns");
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
            prefix = "";
        } else {
            prefix = ":" + prefix;
        }

        String newDoc = xml.toString();

        int posEndTag = newDoc.indexOf(">");
        if (newDoc.charAt(posEndTag - 1) == '/') {
            posEndTag--;
        }

        String rootTag = newDoc.substring(0, posEndTag);
        String[] rootTags = rootTag.split("\\s");
        StringBuilder sb = new StringBuilder(rootTags[0]);
        sb.append(" xmlns" + prefix + "=\"" + ns + "\" ");

        int len = rootTags.length;
        for (int i = 1; i < len; i++) {
            if (rootTags[i].indexOf("xmlns") == -1) {
                sb.append(rootTags[i]);
                sb.append(" ");
            }
        }
        sb.setLength(sb.length() - 1);

        return new StringBuilder(sb.toString() + xml.substring(posEndTag));
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
        subXml += ">";

        int startPos = subXml.indexOf('<');
        int endPos1 = subXml.indexOf(' ');
        int endPos2 = subXml.indexOf('>');

        if (endPos1 == -1) {
            endPos1 = endPos2;
        }

        if (endPos2 == -1) {
            endPos2 = endPos1;
        }

        try {

            String tag = subXml.substring(startPos, Math.min(endPos1, endPos2));
            if (tag.indexOf(":") != -1) {
                prefix = tag.substring(tag.indexOf("<") + 1, tag.indexOf(":"));
            }

        } catch (IndexOutOfBoundsException e) {

            prefix = null;
        }

        return prefix;
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

                int pos = subXml.indexOf("xmlns");
                int pos2 = subXml.indexOf("xmlns:");

                if (pos != -1) {

                    while (pos == pos2 && pos != -1) {
                        pos = subXml.indexOf("xmlns", pos + 1);
                        pos2 = subXml.indexOf("xmlns:", pos2 + 1);
                    }

                    if (pos != -1) {

                        pos = subXml.indexOf("\"", pos);
                        pos2 = subXml.indexOf("\"", pos + 1);
                        if (pos == -1) {

                            pos = subXml.indexOf("'", pos);
                            pos2 = subXml.indexOf("'", pos + 1);
                        }

                        targetNamespace = subXml.substring(pos + 1, pos2);
                    }
                }

            } else {

                prefix = "xmlns:" + prefix;
                int posIni = subXml.indexOf(prefix) + prefix.length();
                posIni = subXml.indexOf('"', posIni) + 1;
                int posFin = subXml.indexOf('"', posIni);
                if (posIni == -1) {

                    posIni = subXml.indexOf(prefix) + prefix.length();
                    posIni = subXml.indexOf("'", posIni) + 1;
                    posFin = subXml.indexOf("'", posIni);
                }

                targetNamespace = subXml.substring(posIni, posFin);
            }

        } catch (IndexOutOfBoundsException e) {

            targetNamespace = null;
        }

        return targetNamespace;
    }
}
