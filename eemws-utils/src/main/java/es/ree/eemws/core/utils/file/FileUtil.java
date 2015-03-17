/*
 * Copyright 2015 Red Eléctrica de España, S.A.U.
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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
 * @version 1.1 13/02/2015
 */
public final class FileUtil {

    /** UTF-8 BOM header (should be removed to use the content as string). */
    private static final int UTF8_BOM_HEADER = 65279;
    
    /** Backup file extension prefix. */
    private static final String BACKUP_EXTENSION = ".bak_"; //$NON-NLS-1$

    /**
     * Constructor.
     */
    private FileUtil() {

        /* This method should not be implemented. */
    }

    /**
     * Reads a text file using the default platform char set.
     * @param fullFileName Path of the file.
     * @return String with the content of the file.
     * @throws IOException Exception with the error.
     */
    public static String read(final String fullFileName) throws IOException {

        return Charset.defaultCharset().decode(ByteBuffer.wrap(Files.readAllBytes(Paths.get(fullFileName)))).toString();
    }

    /**
     * Reads a text file in a UTF-8 Char set.
     * @param fullFileName Path of the file.
     * @return String with the content of the file.
     * @throws IOException Exception with the error.
     */
    public static String readUTF8(final String fullFileName) throws IOException {

        CharBuffer cb = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Files.readAllBytes(Paths.get(fullFileName))));
        if (cb.length() > 1 && cb.charAt(0) == UTF8_BOM_HEADER) {

            cb.get();
        }

        return cb.toString();
    }

    /**
     * Reads a text file using the given char set.
     * @param fullFileName Path of the file.
     * @param encoding Char set
     * @return String with the content of the file.
     * @throws IOException Exception with the error.
     */
    public static String read(final String fullFileName, final Charset encoding) throws IOException {

        return encoding.decode(ByteBuffer.wrap(Files.readAllBytes(Paths.get(fullFileName)))).toString();
    }

    /**
     * Reads a binary file.
     * @param fullFileName Path of the file.
     * @return Byte[] with the content of the file.
     * @throws IOException Exception with the error.
     */
    public static byte[] readBinary(final String fullFileName) throws IOException {

        return Files.readAllBytes(Paths.get(fullFileName));
    }

    /**
     * Writes a text file using the default platform char set.
     * @param fullFileName Path of the file.
     * @param content String with the content of the file.
     * @throws IOException Exception with the error.
     */
    public static void write(final String fullFileName, final String content) throws IOException {

        Files.write(Paths.get(fullFileName), content.getBytes(Charset.defaultCharset()), StandardOpenOption.CREATE);
    }


    /**
     * Writes a binary file.
     * @param fullFileName Path of the file.
     * @param content Binary content of the file.
     * @throws IOException Exception with the error.
     */
    public static void write(final String fullFileName, final byte[] content) throws IOException {

        Files.write(Paths.get(fullFileName), content, StandardOpenOption.CREATE);
    }
    
    
    /**
     * Writes a text file using the default platform char set.
     * @param fullFileName Path of the file.
     * @param content String with the content of the file.
     * @throws IOException Exception with the error.
     */
    public static void writeUTF8(final String fullFileName, final String content) throws IOException {

        Files.write(Paths.get(fullFileName), content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    }

    /**
     * Writes a text file using the given char set.
     * @param fullFileName Path of the file.
     * @param content String with the content of the file.
     * @param encoding Char set
     * @throws IOException Exception with the error.
     */
    public static void write(final String fullFileName, final String content, final Charset encoding) throws IOException {

        Files.write(Paths.get(fullFileName), content.getBytes(encoding), StandardOpenOption.CREATE);
    }

    /**
     * Tests whether a file exists.
     * @param fullFileName Path of the file.
     * @return true if the file exists; false if the file does not exist or its existence cannot be determined.
     */
    public static boolean exists(final String fullFileName) {

        return Files.exists(Paths.get(fullFileName));
    }

    /**
     * Creates a backup file. Uses extension .bak_N. N is a number from 1 to the max version create.
     * @param fullFileName Path of the file.
     * @return Name of the file backup.
     */
    public static String createBackup(final String fullFileName) {

        File f = new File(fullFileName);
        File f2 = new File(fullFileName);
        boolean isRename = false;

        if (f.exists()) {

            for (int n = 1; !isRename; n++) {

                f2 = new File(fullFileName + BACKUP_EXTENSION + n);
                isRename = f.renameTo(f2);
            }
        }

        return f2.getAbsolutePath();
    }

    /**
     * Returns full path of a resource.
     * @param resourceName Resource name
     * @return Full path of the given resourceName.<br>
     *   <code>null</code> if the given resourceName is not found in the current thread's ClassLoader.
     */
    public static String getFullPathOfResoruce(final String resourceName) {
        String retValue;
        try {
            retValue = new File(Thread.currentThread().getContextClassLoader().getResource(resourceName).toURI()).getAbsolutePath();
        } catch (URISyntaxException e) { //NOSONAR - We specifically do not want to propagate nor log this exception
            retValue = null;
        }

        return retValue;
    }

}
