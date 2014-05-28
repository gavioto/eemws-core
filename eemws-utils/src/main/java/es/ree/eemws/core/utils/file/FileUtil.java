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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


/**
 * Utilities to read and write files.
 *
 * @author Red Eléctrica de España S.A.U.
 * @version 1.0 13/06/2014
 */
public final class FileUtil {

    /** UTF-8 BOM header (should be removed to use the content as string). */
    private static final int UTF8_BOM_HEADER = 65279;

    /**
     * Constructor.
     */
    private FileUtil() {

        /* This method should not be implemented. */
    }

    /**
     * This method reads a text file using the default platform char set.
     * NOTE: This is not intended for reading in large files.
     * @param fullFileName Path of the file.
     * @return String with the content of the file.
     * @throws IOException Exception with the error.
     */
    public static String read(final String fullFileName) throws IOException {

        return Charset.defaultCharset().decode(ByteBuffer.wrap(Files.readAllBytes(Paths.get(fullFileName)))).toString();
    }

    /**
     * This method reads a text file in a UTF-8 Char set.
     * NOTE: This is not intended for reading in large files.
     * @param fullFileName Path of the file.
     * @return String with the content of the file.
     * @throws IOException Exception with the error.
     */
    public static String readUTF8(final String fullFileName) throws IOException {

        CharBuffer cb = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Files.readAllBytes(Paths.get(fullFileName))));
        if (cb.length() > 1 && cb.charAt(0) ==  UTF8_BOM_HEADER) {

            cb.get();
        }

        return cb.toString();
    }

    /**
     * This method reads a text file using the given char set.
     * NOTE: This is not intended for reading in large files.
     * @param fullFileName Path of the file.
     * @param encoding Char set
     * @return String with the content of the file.
     * @throws IOException Exception with the error.
     */
    public static String read(final String fullFileName, final Charset encoding) throws IOException {

        return encoding.decode(ByteBuffer.wrap(Files.readAllBytes(Paths.get(fullFileName)))).toString();
    }

    /**
     * This method writes a text file using the default platform char set.
     * NOTE: This is not intended for writing out large files.
     * @param fullFileName Path of the file.
     * @param content String with the content of the file.
     * @throws IOException Exception with the error.
     */
    public static void write(final String fullFileName, final String content) throws IOException {

        Files.write(Paths.get(fullFileName), content.getBytes(Charset.defaultCharset()), StandardOpenOption.CREATE);
    }

    /**
     * This method writes a text file using the default platform char set.
     * NOTE: This is not intended for writing out large files.
     * @param fullFileName Path of the file.
     * @param content String with the content of the file.
     * @throws IOException Exception with the error.
     */
    public static void writeUTF8(final String fullFileName, final String content) throws IOException {

        Files.write(Paths.get(fullFileName), content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    }

    /**
     * This method writes a text file using the given char set.
     * NOTE: This is not intended for writing out large files.
     * @param fullFileName Path of the file.
     * @param content String with the content of the file.
     * @param encoding Char set
     * @throws IOException Exception with the error.
     */
    public static void write(final String fullFileName, final String content, final Charset encoding) throws IOException {

        Files.write(Paths.get(fullFileName), content.getBytes(encoding), StandardOpenOption.CREATE);
    }
}
