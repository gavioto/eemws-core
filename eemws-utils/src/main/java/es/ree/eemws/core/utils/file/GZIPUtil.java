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
package es.ree.eemws.core.utils.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * Utilities to compress and decompress with gzip.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class GZIPUtil {

    /**
     * Constructor.
     */
    private GZIPUtil() {

        /* This method should not be implemented. */
    }

    /**
     * This method compress the data with gzip.
     * @param dataToCompress Data to compress.
     * @return Data compress.
     * @throws IOException Exception with the error.
     */
    public static byte[] compress(final byte[] dataToCompress) throws IOException {

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(dataToCompress.length);
        try (GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);) {

            zipStream.write(dataToCompress);
        }

        return byteStream.toByteArray();
    }

    /**
     * This method uncompress the data with gzip.
     * @param contentBytes Data to uncompress.
     * @return Data uncompress.
     * @throws IOException Exception with the error.
     */
    public static byte[] uncompress(final byte[] contentBytes) throws IOException {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

            try (GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(contentBytes));) {

                int len;
                byte[] buffer = new byte[1024];
                while ((len = gzis.read(buffer)) > 0) {

                    out.write(buffer, 0, len);
                }
            }

            return out.toByteArray();
        }
    }
}
