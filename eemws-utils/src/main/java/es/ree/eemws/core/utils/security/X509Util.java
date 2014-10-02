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

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * Implements simple X509 validations and utilities.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class X509Util {

    /** TrustFactory algorimth names. */
    private static final String[] ALGORITHM_NAMES = {"SunX509", "IbmX509"};

    /** Authentication type. */
    private static final String AUTHENTICATION_TYPE = "RSA";

    /** Trust manager, validate that the given certificate was issued by a trusted CA. */
    private static final X509TrustManager X509_TRUST_MANAGER;

    /*
     * Initialize the x509 trust manager.
     */
    static {
        TrustManagerFactory tmf = null;

        for (int cont = 0; tmf == null && cont < ALGORITHM_NAMES.length; cont++) {
            try {
                tmf = TrustManagerFactory.getInstance(ALGORITHM_NAMES[cont]);
            } catch (NoSuchAlgorithmException e) {

                /* No algorithm[i] available, try next one... */
            }
        }

        if (tmf == null) {
            throw new IllegalStateException("Unable to find an X509 Trust validator");

        } else {
            try {
                tmf.init((KeyStore) null);
                X509_TRUST_MANAGER = (X509TrustManager) tmf.getTrustManagers()[0];
            } catch (KeyStoreException e) {
                throw new IllegalStateException("Unable to initialize the X509 Trust validator [" + tmf.getAlgorithm() + "]");
            }
        }
    }

    /**
     * Constructor.
     */
    private X509Util() {

        /* This method should not be implemented. */
    }

    /**
     * Check the given X509 certificate.
     * @param x509Cert Certificate to be validated.
     * @throws CertificateException if the certificate is not valid (out of date or untrusted).
     */
    public static void checkCertificate(final X509Certificate x509Cert) throws CertificateException {

        checkCertificate(new X509Certificate[] {x509Cert});
    }

    /**
     * Check the given X509 certificates.
     * @param x509Certs Certificates to be validated.
     * @throws CertificateException if certificates are not valid (out of date or untrusted).
     */
    public static void checkCertificate(final X509Certificate[] x509Certs) throws CertificateException  {

        X509_TRUST_MANAGER.checkClientTrusted(x509Certs, AUTHENTICATION_TYPE);
        for (int cont = 0; cont < x509Certs.length; cont++) {

            x509Certs[cont].checkValidity();
        }
    }
}
