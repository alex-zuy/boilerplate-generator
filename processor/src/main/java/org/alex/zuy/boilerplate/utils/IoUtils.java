package org.alex.zuy.boilerplate.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public final class IoUtils {

    private static final int BUFFER_SIZE = 1024 * 4;

    private IoUtils() {

    }

    @SuppressWarnings("PMD.AssignmentInOperand")
    public static void copy(InputStream in, OutputStream out) throws IOException {
        final byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
    }

    @SuppressWarnings("PMD.AssignmentInOperand")
    public static void copy(Reader in, Writer out) throws IOException {
        final char[] buffer = new char[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
    }

    public static String readToString(Reader reader) throws IOException {
        final StringWriter result = new StringWriter();
        copy(reader, result);
        return result.toString();
    }
}
