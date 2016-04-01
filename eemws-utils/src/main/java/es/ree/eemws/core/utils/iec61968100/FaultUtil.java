/*
 * Copyright 2016 Red Eléctrica de España, S.A.U.
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

package es.ree.eemws.core.utils.iec61968100;

import _504.iec62325.wss._1._0.MsgFaultMsg;
import ch.iec.tc57._2011.schema.message.ErrorType;
import ch.iec.tc57._2011.schema.message.FaultMessage;
import ch.iec.tc57._2011.schema.message.ReplyType;

/**
 * Utility methods to create Fault messages.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.1 02/02/2016
 */

public final class FaultUtil {
    
    
    /**
     * Utility classes must not have public constructor.
     */
    private FaultUtil() {
        
        /* This constructor must not be implemented. */
    }
    
    
    /**
     * Returns a FaultMessage using the given message and code.
     * @param msg Fault message.
     * @param code Fault code, can be <code>null</code> in this case code doesn't appear in the fault.
     * @return a FaultMessage with message and code (if provided).
     */
    public static FaultMessage getFaultMessageFromException(final String msg, final String code) {

        FaultMessage faultMessage = new FaultMessage();

        ReplyType replyType = new ReplyType();
        replyType.setResult(EnumMessageStatus.FAILED.toString());

        ErrorType errorType = new ErrorType();
        errorType.setDetails(msg);
        if (code != null) {
            errorType.setCode(code);
        }

        replyType.getErrors().add(errorType);

        faultMessage.setReply(replyType);

        return faultMessage;
    }
    
    
    /**
     * Creates a <code>MsgFaultMsg</code> using the information provider by the exception.
     * @param exception Exception with the information needed by the fault message.
     * @return A <code>MsgFaultMsg</code> with the exception information.
     */
    public static MsgFaultMsg getMsgFaultFromException(final GenericCodedException exception) {
        return new MsgFaultMsg(exception.getMessage(), getFaultMessageFromException(exception.getMessage(), exception.getCode()));
    }

    /**
     * Creates a simple <code>MsgFaultMsg</code> with the given error message.
     * @param msg Error message
     * @return Message Fault with the given error message.
     */
    public static MsgFaultMsg getMsgFaultFromMessage(final String msg) {
        return new MsgFaultMsg(msg, getFaultMessageFromException(msg, null));
    }

    /**
     * Returns a message fault using the information provided by the given <code>MessageMetaData</code>.
     * @param mmd Message Metadata
     * @return A Fault message using the information provided by the given <code>MessageMetaData</code>. 
     */
    public static MsgFaultMsg getMsgFaultFromMetaData(final MessageMetaData mmd) {

        MsgFaultMsg msgFaultMsg;

        if (mmd.getException() == null) {
            msgFaultMsg = getMsgFaultFromMessage(mmd.getRejectText());
        } else {
            msgFaultMsg = getMsgFaultFromException(mmd.getException());
        }

        return msgFaultMsg;
    }
}
