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
package es.ree.eemws.core.utils.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Base class to manage multi-language texts in the application.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public abstract class AbstractMessages {
    
    /** If there is no text for a given key, returns a string with the raw key using also this token. */
    private static final String NO_KEY_INFORMATION_TOKEN = "???"; //$NON-NLS-1$
    
    /**
     * This method gets the message given its key.
     * If there is no text for the given key the string <code>???KEY???</code> will be returned.
     * @param reBundle A resource boundle witht the pairs key=text.
     * @param key Key of the message.
     * @param parameters parameters that will be replaced in the message.
     * @return Message of the key.
     */
    protected static String getString(final ResourceBundle reBundle, final String key, final Object... parameters) {

        return MessageFormat.format(getString(reBundle, key), parameters);
    }
    
    /**
     * This method gets the message given its key.
     * If there is no text for the given key the string <code>???KEY???</code> will be returned.
     * @param reBundle A resource boundle witht the pairs key=text.
     * @param key Key of the message.
     * @return Message of the key.
     */
    private static String getString(final ResourceBundle reBundle, final String key) {

        String retValue = ""; //$NON-NLS-1$
        try {

            retValue = reBundle.getString(key);

        } catch (MissingResourceException e) { // NOSONAR Ignore the exception use default text

            retValue = NO_KEY_INFORMATION_TOKEN + key + NO_KEY_INFORMATION_TOKEN;
        }
        
        return retValue;
    }
}
