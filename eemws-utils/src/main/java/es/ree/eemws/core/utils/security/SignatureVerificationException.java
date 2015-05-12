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

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import es.ree.eemws.core.utils.messages.Messages;

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
    private final SignatureVerificationExceptionDetails details = new SignatureVerificationExceptionDetails();

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
     * Keeps detail information about the signature's references
     */
    public final class ReferenceStatus {

        /** Status of the reference. */
        private Boolean refValid;

        /** Expected digest value. */
        private String calculatedDigestValue;

        /** Provided digest value. */
        private String providedDigestValue;

        /**
         * Constructor. Creates a new detail information.
         * @param refV Validation status.
         * @param calcu Expected digest value.
         * @param prov Calculated digest value.
         */
        public ReferenceStatus(Boolean refV, String calcu, String prov) {
            refValid = refV;
            calculatedDigestValue = calcu;
            providedDigestValue = prov;
        }

        /**
         * Returns whether  this reference is valid.
         * @return <code>True</code> if the reference is valid.
         */
        public Boolean isValid() {
            return refValid;
        }

        /*
         * Returns a human readable version of the reference.
         */
        @Override
        public String toString() {
            String msg;
            if (refValid) {
                msg = Messages.getString("SECURITY_REFERENCE_STATUS_VALID"); //$NON-NLS-1$
            } else {
                msg = Messages.getString("SECURITY_REFERENCE_STATUS_NO_VALID", providedDigestValue, calculatedDigestValue); //$NON-NLS-1$
            }
            
            return msg;
        }
    }

    /**
     * Implements a simple detail structure to give more details about the signaturevalidation error.
     */
    public final class SignatureVerificationExceptionDetails {

        /** Status of the signature. */
        private Boolean signatureStatus = null;

        /** Status of the certificate. */
        private Boolean certificateValid = null;

        /** Certificate used in the signature. */
        private X509Certificate signatureCertificate = null;

        /** Status of the document's reference. */
        private List<ReferenceStatus> references = new ArrayList<ReferenceStatus>();

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (signatureStatus != null) {
                if (signatureStatus) {
                    sb.append(Messages.getString("SECURITY_SIGNATURE_STATUS_VALID")); //$NON-NLS-1$
                } else {
                    sb.append(Messages.getString("SECURITY_SIGNATURE_STATUS_NO_VALID")); //$NON-NLS-1$
                }
            }

            if (certificateValid != null) {
                if (certificateValid) {
                    sb.append(Messages.getString("SECURITY_CERTIFICATE_STATUS_VALID")); //$NON-NLS-1$
                } else {
                    sb.append(Messages.getString("SECURITY_CERTIFICATE_STATUS_NO_VALID")); //$NON-NLS-1$
                }
            }

            int cont = 0;
            for (ReferenceStatus rs : references) {
                sb.append(Messages.getString("SECURITY_REFERENCE_STATUS", cont, rs.toString())); //$NON-NLS-1$
                cont++;
            }

            return sb.toString();
        }

        /**
         * Includes the given certificate to this exception detail.
         * @param x509Cert Certificate used for signature.
         */
        public void setSignatureCertificate(X509Certificate x509Cert) {
            signatureCertificate = x509Cert;
        }

        /**
         * Gets the certificate used in the signature. 
         * @return Certificate used in the signature.
         */
        public X509Certificate getSignatureCertificate() {
            return signatureCertificate;
        }

        /**
         * Sets the status of the signature.
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
         * Sets the reference status.
         * @param ref Reference value <code>true</code> if the reference is ok.
         * <code>false</code> otherwise.
         */
        public void addReferenceStatus(Boolean refValid, String calculated, String provided) {

            references.add(new ReferenceStatus(refValid, calculated, provided));
        }

        /**
         * Gets the complete list of reference status.
         * @return A list with the reference status.
         */
        public List<ReferenceStatus> getReferencesStatus() {

            return references;
        }

        /**
         * Gets the referece status for the reference with the given index.
         * @param index Reference index.
         * @return <code>true</code> if the reference is ok.
         * <code>false</code> otherwise.
         */
        public Boolean isRefereceValid(final int index) {

            return references.get(index).isValid();
        }

        /**
         * Sets the certificate validity.
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
