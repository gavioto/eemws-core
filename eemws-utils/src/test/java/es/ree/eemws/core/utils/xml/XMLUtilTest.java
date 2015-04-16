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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Test class for XMLUtil.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/11/2014
 */
public final class XMLUtilTest {

    /** Logger messages. */
    private final Logger logger = LoggerFactory.getLogger(XMLUtilTest.class);
   
	/** Sample created date time. */
	private static final String CREATED_DATE_TIME = "2015-04-08T12:22:48Z";
   
	/** Sample doc type. */
	private static final String DOC_TYPE = "B02";
	
	/** Sample code */
	private static final String CODE = "A02";	
	  
    /** Sample xml. */
	private static final StringBuilder NO_NAME_SPACE_PREFIX = new StringBuilder(""
		+ "<Acknowledgement_MarketDocument xmlns=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">"
		+ "		 <mRID>ack_code</mRID>"
		+ "		 <createdDateTime>2015-04-08T12:22:48Z</createdDateTime>"
		+ "		 <sender_MarketParticipant.mRID codingScheme=\"A01\">10XES-REE------E</sender_MarketParticipant.mRID>"
		+ "		 <sender_MarketParticipant.marketRole.type>A04</sender_MarketParticipant.marketRole.type>"
		+ "		 <receiver_MarketParticipant.mRID codingScheme=\"A01\">10XES-REE------E</receiver_MarketParticipant.mRID>"
		+ "		 <receiver_MarketParticipant.marketRole.type>A04</receiver_MarketParticipant.marketRole.type>"
		+ "		 <received_MarketDocument.mRID>code</received_MarketDocument.mRID>"
		+ "		 <received_MarketDocument.revisionNumber>12</received_MarketDocument.revisionNumber>"
		+ "		 <received_MarketDocument.type>" + DOC_TYPE + "</received_MarketDocument.type>"
		+ "		 <received_MarketDocument.createdDateTime>" + CREATED_DATE_TIME + "</received_MarketDocument.createdDateTime>"
		+ "		 <Reason>"
		+ "				<code>A02</code>"
		+ "				<text>COMPLETELY REJECTED MESSAGE. REASON: un expected element schedule_Period.timeInterval in line: 11 </text>"
		+ "		</Reason>"
		+ "		<Reason>"
		+ "				<code>AXX</code>"
		+ "				<text>COMPLETELY REJECTED MESSAGE. REASON: received_MarketDocument.type is invalid in this context</text>"
		+ "		</Reason>"
		+ "</Acknowledgement_MarketDocument>");
		
	/** Sample xml. */
	private static final StringBuilder NAME_SPACE_PREFIX = new StringBuilder(""
		+ "<n1:Acknowledgement_MarketDocument xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">"
		+ "		 <n1:mRID>ack_code</n1:mRID>"
		+ "		 <n1:createdDateTime>2015-04-08T12:22:48Z</n1:createdDateTime>"
		+ "		 <n1:sender_MarketParticipant.mRID codingScheme=\"A01\">10XES-REE------E</n1:sender_MarketParticipant.mRID>"
		+ "		 <n1:sender_MarketParticipant.marketRole.type>A04</n1:sender_MarketParticipant.marketRole.type>"
		+ "		 <n1:receiver_MarketParticipant.mRID codingScheme=\"A01\">10XES-REE------E</n1:receiver_MarketParticipant.mRID>"
		+ "		 <n1:receiver_MarketParticipant.marketRole.type>A04</n1:receiver_MarketParticipant.marketRole.type>"
		+ "		 <n1:received_MarketDocument.mRID>code</n1:received_MarketDocument.mRID>"
		+ "		 <n1:received_MarketDocument.revisionNumber>12</n1:received_MarketDocument.revisionNumber>"
		+ "		 <n1:received_MarketDocument.type>" + DOC_TYPE + "</n1:received_MarketDocument.type>"
		+ "		 <n1:received_MarketDocument.createdDateTime>" + CREATED_DATE_TIME + "</n1:received_MarketDocument.createdDateTime>"
		+ "		 <n1:Reason>"
		+ "				<n1:code>A02</n1:code>"
		+ "				<n1:text>COMPLETELY REJECTED MESSAGE. REASON: un expected element schedule_Period.timeInterval in line: 11 </n1:text>"
		+ "		</n1:Reason>"
		+ "		<n1:Reason>"
		+ "				<n1:code>AXX</n1:code>"
		+ "				<n1:text>COMPLETELY REJECTED MESSAGE. REASON: received_MarketDocument.type is invalid in this context</n1:text>"
		+ "		</n1:Reason>"
		+ "</n1:Acknowledgement_MarketDocument>");

	/** Sample xml. */
	private static final StringBuilder NAME_SPACE_PREFIX_DECLARATION_IN_EACH_ELEMENT = new StringBuilder(""
		+ "<n1:Acknowledgement_MarketDocument xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">"
		+ "		 <n1:mRID xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">ack_code</n1:mRID>"
		+ "		 <n1:createdDateTime xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">2015-04-08T12:22:48Z</n1:createdDateTime>"
		+ "		 <n1:sender_MarketParticipant.mRID codingScheme=\"A01\" xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">10XES-REE------E</n1:sender_MarketParticipant.mRID>"
		+ "		 <n1:sender_MarketParticipant.marketRole.type xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">A04</n1:sender_MarketParticipant.marketRole.type>"
		+ "		 <n1:receiver_MarketParticipant.mRID codingScheme=\"A01\" xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">10XES-REE------E</n1:receiver_MarketParticipant.mRID>"
		+ "		 <n1:receiver_MarketParticipant.marketRole.type xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">A04</n1:receiver_MarketParticipant.marketRole.type>"
		+ "		 <n1:received_MarketDocument.mRID xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">code</n1:received_MarketDocument.mRID>"
		+ "		 <n1:received_MarketDocument.revisionNumber xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">12</n1:received_MarketDocument.revisionNumber>"
		+ "		 <n1:received_MarketDocument.type xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">" + DOC_TYPE + "</n1:received_MarketDocument.type>"
		+ "		 <n1:received_MarketDocument.createdDateTime xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">" + CREATED_DATE_TIME + "</n1:received_MarketDocument.createdDateTime>"
		+ "		 <n1:Reason xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">"
		+ "				<n1:code xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">A02</n1:code>"
		+ "				<n1:text xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">COMPLETELY REJECTED MESSAGE. REASON: un expected element schedule_Period.timeInterval in line: 11 </n1:text>"
		+ "		</n1:Reason>"
		+ "		<n1:Reason xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">"
		+ "				<n1:code xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">AXX</n1:code>"
		+ "				<n1:text xmlns:n1=\"urn:iec62325.351:tc57wg16:451-1:acknowledgementdocument:7:0\">COMPLETELY REJECTED MESSAGE. REASON: received_MarketDocument.type is invalid in this context</n1:text>"
		+ "		</n1:Reason>"
		+ "</n1:Acknowledgement_MarketDocument>");
		
    /**
     * Test method for {@link es.ree.eemws.core.utils.xml.XMLUtil#getNodeValue(String, StringBuilder)}
	 * Will extract document values with several conditions.
     */
    @Test 
    public void testGetNodeValueWithNoNameSpacePrefix() {
		
		testGetNodeValue("testGetNodeValue with no namespace prefix", NO_NAME_SPACE_PREFIX);
		testGetNodeValue("testGetNodeValue with namespace prefix", NAME_SPACE_PREFIX);
		testGetNodeValue("testGetNodeValue with namespace prefix and declaration", NAME_SPACE_PREFIX_DECLARATION_IN_EACH_ELEMENT);
	}

	/**
	 * Extract different XML node values.
	 * @param idCase Case identification (for log pourposes)
	 * @param xml Test xml instance.
	 */
    private void testGetNodeValue(final String idCase, final StringBuilder xml) {
		String tmp;
		
		logger.debug(idCase + " testGetNodeValue (existent element)");
		tmp = XMLUtil.getNodeValue("received_MarketDocument.createdDateTime", xml);
		assertThat(tmp, is(CREATED_DATE_TIME));
		
		logger.debug(idCase + " testGetNodeValue (nonexistent element)");
		tmp = XMLUtil.getNodeValue("non_existent_element", xml);
		assertThat(tmp, is((String) null));
		
		logger.debug(idCase + " testGetNodeValue (nonexistent element but in text)");
		tmp = XMLUtil.getNodeValue("schedule_Period.timeInterval", xml);
		assertThat(tmp, is((String) null));
		
		logger.debug(idCase + " testGetNodeValue (existent element and text case 1: fist the element then text)");
		tmp = XMLUtil.getNodeValue("received_MarketDocument.type", xml);
		assertThat(tmp, is(DOC_TYPE));
		
		logger.debug(idCase + " testGetNodeValue (existent element and text case 2: fist the text then element)");
		tmp = XMLUtil.getNodeValue("code", xml);
		assertThat(tmp, is(CODE));
		
		
    }
	
	

    
    
}
