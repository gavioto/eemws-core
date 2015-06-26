/*
 * Copyright 2015 Red Eléctrica de España, S.A.U.
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import es.ree.eemws.core.utils.messages.Messages;

/**
 * Representation for W3C XML Schema 1.0 date/time datatypes.
 *
 * @author Red Eléctrica de España, S.A.U.
 * @version 1.1 25/06/2015
 */
public final class XMLGregorianCalendarFactory {

    /** Generic error message. */
    private static final String ERR_MSG = Messages.getString("XML_GREGORIAN_CALENDAR"); //$NON-NLS-1$
    
    /** Date format for string date values. */
    private static final String Z_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    
    /**
     * Constructor.
     */
    private XMLGregorianCalendarFactory() {

        /* This method should not be implemented. */
    }

    /**
     * Gets an instance of a XMLGregorianCalendar GMT with date values given by the parameter.
     * Millisecond values are removed anyway.
     * @param date Date to create a XMLGregorianCalendar.
     * @return XMLGregorianCalendar GMT with date values given by the parameter.
     */
    public static XMLGregorianCalendar getGMTInstance(final Date date) {

        XMLGregorianCalendar retValue = getGMTInstanceMs(date);
        retValue.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);

        return retValue;
    }

    /**
     * Gets an instance of a XMLGregorianCalendar GMT (with milliseconds) with date values given by the parameter.
     * @param date Date to create a XMLGregorianCalendar.
     * @return XMLGregorianCalendar GMT with date values given by the parameter.
     */
    public static XMLGregorianCalendar getGMTInstanceMs(final Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar retValue = null;

        try {
            retValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal).normalize();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(ERR_MSG, e);  
        }

        return retValue;
    }

    /**
     * Gets an instance of a XMLGregorianCalendar with date values given by the string parameter.
     * @param date String with the format yyyy-MM-dd'T'HH:mm:ss'Z'
     * @return XMLGregorianCalendar with date values given by the parameter.
     */
    public static XMLGregorianCalendar getInstance(final String date) throws ParseException {
        
        SimpleDateFormat sdf = new SimpleDateFormat(Z_DATE_FORMAT);
        sdf.setLenient(false);
        return getInstance(sdf.parse(date));
    }
    
    
    /**
     * Gets an instance of a XMLGregorianCalendar with date values given by the parameter.
     * Millisecond values are removed anyway.
     * @param date Date to create a XMLGregorianCalendar.
     * @return XMLGregorianCalendar with date values given by the parameter.
     */
    public static XMLGregorianCalendar getInstance(final Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getInstance(cal);
    }
       

    /**
     * Gets an instance of a XMLGregorianCalendar with date values given by the parameter.
     * Millisecond values are removed anyway.
     * @param date Date to create a XMLGregorianCalendar.
     * @return XMLGregorianCalendar with date values given by the parameter.
     */
    public static XMLGregorianCalendar getInstance(final Calendar date) {

        XMLGregorianCalendar retValue = null;
        try {

            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date.getTime());
            retValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

        } catch (DatatypeConfigurationException e) {

            throw new RuntimeException(ERR_MSG, e);  
        }

        return retValue;
    }
}
