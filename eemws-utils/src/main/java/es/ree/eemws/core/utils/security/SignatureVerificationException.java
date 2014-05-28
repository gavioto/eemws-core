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
package es.ree.eemws.core.utils.security;

import java.util.ArrayList;
import java.util.List;


/**
 * Implements an exception to notify problems with the signature validation.
 * The Exception also contains details about the validation.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class SignatureVerificationException extends SignatureManagerException {

    /** Serial UID. */
    private static final long serialVersionUID = -1610760793648350360L;

    /** Exception details. */
    private SignatureVerificationExceptionDetails details = null;

    /**
     * Constructor. Creates a new Verification Exception with the given text detail.
     * @param reasonText Error message with details.
     */
    public SignatureVerificationException(final String reasonText) {

        super(reasonText);
    }

    /**
     * Constructor. Creates a new Verification Exception with the given text detail
     * and Exception
     * @param reasonText Error message with details.
     * @param cause Exception with the details.
     */
    public SignatureVerificationException(final String reasonText, final Exception cause) {

        super(reasonText, cause);
    }

    /**
     * Returns the details of the exception.
     * Note that detail information could be <code>null</code> if there is no detail available.
     * @return Detail information, could be <code>null</code>
     */
    public SignatureVerificationExceptionDetails getDetails() {

        return details;
    }

    /**
     * Creates a new detail structure.
     * @return New detail structure.
     */
    public SignatureVerificationExceptionDetails createDetails() {

        details = new SignatureVerificationExceptionDetails();
        return details;
    }

    /**
     * Implements a simple detail structure to give more details about the signaturevalidation error.
     */
    public final class SignatureVerificationExceptionDetails {

        /** Status of the signature. */
        private boolean signatureStatus = false;

        /** Status of the certificate. */
        private boolean certificateValid = false;

        /** Status of the document's reference. */
        private List<Boolean> references = new ArrayList<Boolean>();

        /**
         * Set the status of the signature.
         * @param status <code>true</code> if the signature validation is OK.
         * <code>false</code> otherwise.
         */
        public void setSignatureValid(final boolean status) {

            signatureStatus = status;
        }

        /**
         * Returns the status of the signature.
         * @return <code>true</code> if the signature validation is OK.
         * <code>false</code> otherwise.
         */
        public boolean isSignatureValid() {

            return signatureStatus;
        }

        /**
         * Set the reference status.
         * @param refValid Reference value <code>true</code> if the reference is ok.
         * <code>false</code> otherwise.
         */
        public void addReferenceStatus(final Boolean refValid) {

            references.add(refValid);
        }

        /**
         * Get the complete list of reference status.
         * @return A list with the reference status.
         */
        public List<Boolean> getReferencesStatus() {

            return references;
        }

        /**
         * Get the referece status for the reference with the given index.
         * @param index Reference index.
         * @return <code>true</code> if the reference is ok.
         * <code>false</code> otherwise.
         */
        public Boolean isRefereceValid(final int index) {

            return references.get(index);
        }

        /**
         * Set the certificate validity.
         * @param certValidity <code>true</code> if the certificate is valid.
         * <code>false</code> otherwise.
         */
        public void setCertificateValid(final boolean certValidity) {

            certificateValid = certValidity;
        }

        /**
         * Returns the certificate validity.
         * @return <code>true</code> if the certificate is valid.
         * <code>false</code> otherwise.
         */
        public boolean isCertificateValid() {

            return certificateValid;
        }
    }
}
