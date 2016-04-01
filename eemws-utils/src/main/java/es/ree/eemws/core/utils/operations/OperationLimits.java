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

package es.ree.eemws.core.utils.operations;

import java.util.HashMap;
import java.util.Map;

import es.ree.eemws.core.utils.iec61968100.EnumParameterLimit;

/**
 * Operation limits keeps the configured set of operation limits. 
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/12/2014
 */
public final class OperationLimits {

    /** Current configuration set. */
    private Map<String, Integer> limits = new HashMap<>();

    /**
     * Sets a new limit given its name and value.
     * Use this method to set no IEC 62325-504 standard limits
     * @param limitName Limit name. 
     * @param limitValue Limit value.
     */
    public void setLimit(final String limitName, final Integer limitValue) {
        limits.put(limitName, limitValue);
    }

    /**
     * Sets a new limit given its name and value.
     * @param limit Limit name (according to IEC 62325-504). 
     * @param limitValue Limit value.
     */
    public void setLimit(final EnumParameterLimit limit, final Integer limitValue) {
        setLimit(limit.toString(), limitValue);
    }

    /**
     * Returns a limit value given its name.
     * @param limit Limit name
     * @return Limit value or <code>null</code> if no limit exists.
     */
    public Integer getLimit(final EnumParameterLimit limit) {
        return limits.get(limit.toString());
    }

    /**
     * Returns a limit value given its name.
     * @param limitName Limit name
     * @return Limit value or <code>null</code> if no limit exists.
     */
    public Integer getLimit(final String limitName) {
        return limits.get(limitName);
    }
}
