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
package es.ree.eemws.core.utils.iec61968100;

import java.io.Serializable;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;

/**
 * Class with the Meta data of the message. Information that is not available in the
 * payload, and it is generated from the context: Received timestamp, user's certificate, etc.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class MessageMetaData implements Serializable {

    /** Serial ID. */
   private static final long serialVersionUID = -4872587875516287663L;

    /** Key used to store this information in the process context. */
    public static final String META_DATA_CONTEXT_KEY = "MSG_META_DATA"; //$NON-NLS-1$

    /** Message timestamp. */
    private Calendar serverTimestamp = null;

    /** X.509 Certificate used to transmit this message (http). */
    private X509Certificate httpCertificate = null;

    /** X.509 Certificate used to sing this message. */
    private X509Certificate signCertificate = null;

    /** Remote user. */
    private String remoteUser = null;

    /** List of user's roles. */
    private List<String> userRoles = new ArrayList<String>();

    /** Message code (when it's stored). */
    private Long msgCode = null;

    /** The message type. */
    private String messageType = null;

    /** Status of the message. */
    private EnumMessageStatus status = null;

    /** Reject text of the message. */
    private String rejectText = null;

    /** Exception asociated with this message. */
    private GenericCodedException exception;

    /** Rejection flag. Note this is not associated with the bussines process (Ack)!. */
    private boolean rejected;

    /** 
     * Creates a new instance of the message metadata. Sets the timestamp.
     */
    public MessageMetaData() {
        serverTimestamp = Calendar.getInstance();
    }

    /**
     * Gets the timestamp when the message is received by the server.
     * @return Date when the message is received by the server.
     */
    public Calendar getServerTimestamp() {

        return serverTimestamp;
    }

    /**
     * Sets the timestamp when the message is received by the server.
     * @param date Date when the message is received by the server.
     */
    public void setServerTimestamp(final Calendar date) {

        serverTimestamp = date;
    }

    /**
     * Gets the user's X.509 Certificate.
     * @return User's X.509 Certificate.
     */
    public X509Certificate getHttpCertificate() {

        return httpCertificate;
    }

    /**
     * Sets the X.509 Certificate.
     * @param x509Certificate X.509 Certificate.
     */
    public void setHttpCertificate(final X509Certificate x509Certificate) {

        httpCertificate = x509Certificate;
    }

    /**
     * Sets the certificate used in the signature.
     * @param x509Certificate Certificate used in the signature.
     */
    public void setSignatureCertificate(final X509Certificate x509Certificate) {
        signCertificate = x509Certificate;
    }

    /**
     * Gets the certificate used in the signature.
     * @return Certificate used in the signature.
     */
    public X509Certificate getSignatureCertificate() {
        return signCertificate;
    }

    /**
     * Gets the remote user identification (from http request).
     * @return Remote user identification.
     */
    public String getRemoteUser() {
        return remoteUser;
    }

    /**
     * Sets the remote user identification (from http request).
     * @param inRemoteUser Remote user identification.
     */
    public void setRemoteUser(final String inRemoteUser) {
        remoteUser = inRemoteUser;
    }

    /**
     * Gets the user's list of roles.
     * @return User's list of roles.
     */
    public List<String> getUserRoles() {
        return userRoles;
    }

    /**
     * Sets the user's list of roles.
     * @param inRoles User's list of roles.
     */
    public void setUserRoles(final List<String> inRoles) {
        userRoles = inRoles;
    }

    /**
     * Gets the code of the message in the database table.
     * @return Code of the XML in the database table.
     */
    public Long getCode() {
        return msgCode;
    }

    /**
     * Sets the code of the message in the database table.
     * @return Code of the XML in the database table.
     */
    public void setCode(final Long inCode) {
        msgCode = inCode;
    }

    /**
     * Gets the message type. 
     * @return Message type.
     */
    public String getMessageType() {

        return messageType;
    }

    /**
     * Sets the message type.
     * @param type Message type.
     */
    public void setMessageType(final String type) {

        messageType = type;
    }

    /**
     * Sets an exception caused during the processing of this message
     * @param ex Exception caused during the processing of this message
     */
    public void setException(final GenericCodedException ex) {
        exception = ex;
    }

    /**
     * Gets an exception caused during the processing of this message.
     * @returns Exception caused during the processing of this message
     */
    public GenericCodedException getException() {
        return exception;
    }

    /**
     * Gets the status of the message (OK, FAILED).
     * @return Status of the message.
     */
    public EnumMessageStatus getStatus() {
        if (status == null) {
            if (exception == null) {
                status = EnumMessageStatus.OK;
            } else {
                status = EnumMessageStatus.FAILED;
            }
        }

        return status;
    }

    /**
     * Sets the status of the message (OK, FAILED).
     * @param inStatus Status of the message.
     */
    public void setStatus(final EnumMessageStatus inStatus) {

        status = inStatus;
    }

    /**
     * Gets the reject text of the message. 
     * @return Reject text of the message. <code>null</code> if the message was accepted.
     */
    public String getRejectText() {
        if (rejectText == null && exception != null) {
            rejectText = exception.getMessage();
        }

        return rejectText;
    }

    /**
     * Sets the reject text of the message.
     * @param inRejectText Reject text of the message.
     */
    public void setRejectText(final String inRejectText) {

        rejectText = inRejectText;
    }

    /**
     * Returns <code>true</code> if any further process of this message must be skipped because the message is rejected. <code>false</code> otherwise.
     * This is usefull to abort chain process if one of the chains elements reject the message.
     * @return <code>true</code> if any futher process of this message must be skipped. <code>false</code> otherwise.
     */
    public boolean isRejected() {
        return rejected;
    }
    
    /**
     * Sets if the process of this message (not the message itself) is rejected. For instance if the server receives a message
     * whose owner it's not the one associated with the received certificate.
     * @param rej <code>true</code> if the process of message is rejected. <code>false</code> otherwise. 
     */
    public void setRejected(final boolean rej) {
        rejected = rej;
    }

}
