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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Representation for W3C XML Schema 1.0 date/time datatypes.
 *
 * @author Red Eléctrica de España, S.A.U.
 * @version 1.0 01/04/2014
 */
public final class XMLGregorianCalendarFactory {

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

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar retValue = null;

        try {

            retValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal).normalize();
            retValue.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);

        } catch (DatatypeConfigurationException e) {

            throw new RuntimeException("Error creating XMLGregorianCalendarFactory");
        }

        return retValue;
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

            throw new RuntimeException("Error creating XMLGregorianCalendarFactory", e);
        }

        return retValue;
    }
}
