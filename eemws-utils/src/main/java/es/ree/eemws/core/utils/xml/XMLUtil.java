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
        String etFinal = "";

        if ((pos1 == -1) && (pos2 == -1)) {
            return null;
        }

        if ((pos1 != -1) && (pos2 != -1)) {
            pos = pos2;
            etFinal = ">";
        }

        if ((pos1 == -1) && (pos2 != -1)) {
            pos = pos2;
            etFinal = ">";
        }

        if ((pos1 != -1) && (pos2 == -1)) {
            pos = pos1;
            len++;
        }

        int fin = doc.indexOf(">", pos) + 1;
        int posBeg = pos - 1;
        while (doc.charAt(posBeg) != '<' && posBeg > 0) {
            posBeg--;
        }

        String etiquetaFin = "</" + doc.substring(posBeg + 1, pos + len) + etFinal;

        return inclusiveXml(doc, doc.substring(fin, doc.indexOf(etiquetaFin)));
    }

    /**
     * This method copy the name space from the XML document to the XML sub-document.
     * @param holeXml XML document with all the name spaces.
     * @param subXml XML sub-document.
     * @return XML sub-document with all the name spaces.
     */
    private static String inclusiveXml(final String holeXml, String subXml) {

        boolean loop = true;

        while (loop) {

            try {

                string2Document(subXml);
                loop = false;

            } catch (Exception p) {

                loop = false;
                String msg = p.getMessage();

                int pos = msg.indexOf("The prefix");
                if (pos == -1) {
                    pos = msg.indexOf("The namespace prefix");
                }

                if (pos != -1) {

                    loop = true;

                    pos = msg.indexOf("\"", pos);

                    int pos2 = msg.indexOf("\"", pos + 1);
                    String prefix = msg.substring(pos + 1, pos2).trim();

                    pos = holeXml.indexOf("xmlns:" + prefix);
                    pos2 = holeXml.indexOf("\"", pos);
                    pos2 = holeXml.indexOf("\"", pos2 + 1);
                    String nameSpace = holeXml.substring(pos, pos2 + 1);

                    pos = subXml.indexOf(">");
                    subXml = subXml.substring(0, pos) + " " + nameSpace + subXml.substring(pos);
                }
            }
        }

        return subXml;
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
}
