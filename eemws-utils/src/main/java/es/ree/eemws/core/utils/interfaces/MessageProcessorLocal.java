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

package es.ree.eemws.core.utils.interfaces;

import javax.ejb.Local;

import _504.iec62325.wss._1._0.MsgFaultMsg;
import ch.iec.tc57._2011.schema.message.RequestMessage;
import ch.iec.tc57._2011.schema.message.ResponseMessage;
import es.ree.eemws.core.utils.iec61968100.MessageMetaData;


/**
 * Common local interface to be implemented by all the classes that provides services to the web service façade.
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
@Local
public interface MessageProcessorLocal {
     
    /**
     * Common method to be implemented by classes that provides services to the web service façade.
     * @param msg Request message to be processed.
     * @param mmd Message meta data associated with the request message.
     * @return Response message with the processing answer.
     * @throws MsgFaultMsg Fault exception if the request message cannot be processed.
     */
    ResponseMessage processRequest(final RequestMessage msg, final MessageMetaData mmd) throws MsgFaultMsg; 

}
