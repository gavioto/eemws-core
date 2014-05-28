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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Utilities to use the XML Element class.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class XMLElementUtil {

    /**
     * Constructor.
     */
    private XMLElementUtil() {

        /* This method should not be implemented. */
    }

    /**
     * This method transforms an Object in an Element class.
     * @param obj Object to transform.
     * @return Element class.
     * @throws JAXBException Exception marshal the object.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Element obj2Element(final Object obj) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        DOMResult res = new DOMResult();

        try {

            jaxbMarshaller.marshal(obj, res);

        } catch (JAXBException e) {

            /* Tries to get an object if the given element has no @XmlRootElement. */
            QName qName = new QName(null, obj.getClass().getSimpleName());
            JAXBElement<?> root = new JAXBElement(qName, obj.getClass(), obj);
            jaxbMarshaller.marshal(root, res);
        }

        return ((Document) res.getNode()).getDocumentElement();
    }

    /**
     * This method transforms an Element class in an Object.
     * @param element Element to transform.
     * @param classType Class type of the Object.
     * @return Object transform.
     * @throws JAXBException Exception unmarshal the object.
     */
    public static Object elment2Obj(final Element element, final Class<?> classType) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(classType);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (jaxbUnmarshaller.unmarshal(element, classType)).getValue();
    }

    /**
     * This method transforms a String in an Element.
     * @param xml String with the XML document.
     * @return Element with the XML.
     * @throws ParserConfigurationException Exception parsing XML.
     * @throws SAXException Exception parsing XML.
     * @throws IOException Exception reading XML.
     */
    public static Element string2Element(final String xml)
            throws ParserConfigurationException, SAXException, IOException  {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        docFactory.setValidating(false);

        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new InputSource(new StringReader(xml)));
        return doc.getDocumentElement();
    }

    /**
     * This method transforms an Element in a String.
     * @param element Element with the XML.
     * @return String with the XML.
     * @throws TransformerException Exception transforms XML.
     * @throws ParserConfigurationException Exception creating DOM.
     */
    public static String element2String(final Element element)
            throws TransformerException, ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        document.appendChild(document.importNode(element, true));

        DOMSource domSource = new DOMSource(document);

        StringWriter stringWriter = new StringWriter();
        StreamResult result = new StreamResult(stringWriter);

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        transformer.transform(domSource, result);
        return stringWriter.toString();
    }
}
