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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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

    /** Keeps thread safe JAXBContext in order to re-build them. */
    private static final Map<Class<?>, JAXBContext> JAXB_CONTEXT_CACHE = new ConcurrentHashMap<>();

    /** Keeps a pool (just 1 instance per class of Unmarshaller. Note UnMarshallers are not thread safe. */
    private static final ConcurrentMap<Class<?>, Unmarshaller> UNMARSHALLER_CACHE = new ConcurrentHashMap<>();

    /** Keeps a pool (just 1 instance per class of Marshaller. Note Marshallers are not thread safe. */
    private static final ConcurrentMap<Class<?>, Marshaller> MARSHALLER_CACHE = new ConcurrentHashMap<>();

    /**
     * Constructor.
     */
    private XMLElementUtil() {

        /* This method should not be implemented. */
    }

    /**
     * Transforms an Element class in an Object.
     * @param element Element to transform.
     * @param classType Class type of the Object.
     * @return Object transform.
     * @throws JAXBException Exception unmarshal the object.
     */
    public static Object element2Obj(final Element element, final Class<?> classType) throws JAXBException {

        /* Remove the instance, so only the current thread could use it. Others will create their own. */
        Unmarshaller jaxbUnmarshaller = UNMARSHALLER_CACHE.remove(classType);

        if (jaxbUnmarshaller == null) {
            jaxbUnmarshaller = getJAXBContext(classType).createUnmarshaller();
        }

        Object retValue = (jaxbUnmarshaller.unmarshal(element, classType)).getValue();

        /* Other thread could put an instance before. */
        UNMARSHALLER_CACHE.putIfAbsent(classType, jaxbUnmarshaller);

        return retValue;
    }

    /**
     * Transforms a String in an Element.
     * @param xml String with the XML document.
     * @return Element with the XML.
     * @throws ParserConfigurationException Exception parsing XML.
     * @throws SAXException Exception parsing XML.
     * @throws IOException Exception reading XML.
     */
    public static Element string2Element(final String xml)  //NOSONAR - We do not want to encapsulate these exceptions
            throws ParserConfigurationException, SAXException, IOException  {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        docFactory.setValidating(false);

        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new InputSource(new StringReader(xml)));
        return doc.getDocumentElement();
    }

    /**
     * Transforms an Element to a String.
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
        transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); //$NON-NLS-1$
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");  //$NON-NLS-1$

        transformer.transform(domSource, result);
        return stringWriter.toString();
    }

    /**
     * Transforms an Object in an Element class.
     * @param obj Object to transform.
     * @return Element class.
     * @throws JAXBException Exception marshal the object.
     */
    public static Element obj2Element(final Object obj) throws JAXBException {

        Class<?> classType = obj.getClass();

        Marshaller jaxbMarshaller = getMarshaller(classType);
        DOMResult res = new DOMResult();
        jaxbMarshaller.marshal(obj, res);
        MARSHALLER_CACHE.putIfAbsent(classType, jaxbMarshaller);

        return ((Document) res.getNode()).getDocumentElement();
    }

    /**
     * Transforms an Object into a String.
     * @param obj Object to transform.
     * @return StringBuilder String with the object.
     * @throws JAXBException Exception marshal the object.
     */
    public static StringBuilder object2StringBuilder(final Object obj) throws JAXBException {

        Class<?> classType = obj.getClass();

        Marshaller jaxbMarshaller = getMarshaller(classType);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(obj, baos);
        MARSHALLER_CACHE.putIfAbsent(classType, jaxbMarshaller);

        return new StringBuilder(baos.toString());
    }

    /**
     * Returns a Marshaller for a given class.
     * If there is an instance in the cache, this will be removed from the cache and returned.
     * @param classType The class which the marshaller will be created for.
     * @return Marshaller for the given class.
     * @throws JAXBException If cannot create a marshaller for the given class.

     */
    private static Marshaller getMarshaller(final Class<?> classType) throws JAXBException {

        Marshaller jaxbMarshaller = MARSHALLER_CACHE.remove(classType);

        if (jaxbMarshaller == null) {
            jaxbMarshaller = getJAXBContext(classType).createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        }

        return jaxbMarshaller;
    }

    /**
     * Returns a JAXBContext for the given class.
     * If there is an instance in the cache, this will be removed from the cache and returned.
     * @param classType The class which the context will be created for.
     * @return JAXBContext for the given class.
     * @throws JAXBException if cannot create an instance for the given class
     */
    private static JAXBContext getJAXBContext(final Class<?> classType) throws JAXBException {
        JAXBContext jaxbContext = JAXB_CONTEXT_CACHE.get(classType);
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(classType);
            JAXB_CONTEXT_CACHE.put(classType, jaxbContext);
        }
        return jaxbContext;
    }
}
