package com.github.alex.zuy.boilerplate.support.compiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.Date;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

import com.github.alex.zuy.boilerplate.utils.IoUtils;

public class InMemoryJavaFileObject implements JavaFileObject {

    private final ByteArrayOutputStream fileContents;

    private final Kind kind;

    private final URI uri;

    private Date lastModified = new Date();

    private final OpenedStreamsCounter referencesCounter = new OpenedStreamsCounter();

    public InMemoryJavaFileObject(Kind kind, URI uri) {
        this.kind = kind;
        this.uri = uri;
        fileContents = new CloseNotifyingByteArrayOutputStream(InMemoryJavaFileObject::writeReferenceClosed,
            InMemoryJavaFileObject::writeReferenceBufferFlushed);
    }

    public InputStream openInputStream() throws IOException {
        synchronized (referencesCounter) {
            if (referencesCounter.isOpenedForWriting()) {
                throw new InMemoryFileOpenException(
                    String.format("File \"%s\" is currently opened for writing", uri));
            }
            else {
                InputStream stream = new CloseNotifyingByteArrayInputStream(fileContents.toByteArray(),
                    InMemoryJavaFileObject::readReferenceClosed);
                referencesCounter.openedReadReference();
                return stream;
            }
        }
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        synchronized (referencesCounter) {
            if (referencesCounter.isOpenedForReading() || referencesCounter.isOpenedForWriting()) {
                throw new InMemoryFileOpenException(
                    String.format("File \"%s\" is currently opened for reading and/or writing", uri));
            }
            else {
                referencesCounter.openedWriteReference();
                return fileContents;
            }
        }
    }

    @Override
    public Kind getKind() {
        return kind;
    }

    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        return this.kind.equals(kind) && isNamePartCompatible(simpleName);
    }

    private boolean isNamePartCompatible(String simpleName) {
        final String simpleNameWithExtension = simpleName + kind.extension;
        return uri.toString().equals(simpleNameWithExtension) || uri.toString().endsWith("/" + simpleNameWithExtension);
    }

    @Override
    public NestingKind getNestingKind() {
        return null;
    }

    @Override
    public Modifier getAccessLevel() {
        return null;
    }

    @Override
    public URI toUri() {
        return uri;
    }

    @Override
    public String getName() {
        return uri.getPath();
    }

    @Override
    public long getLastModified() {
        return lastModified.getTime();
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
        return new InputStreamReader(openInputStream());
    }

    @Override
    public Writer openWriter() throws IOException {
        return new OutputStreamWriter(openOutputStream());
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        try (Reader reader = openReader(true)) {
            return IoUtils.readToString(reader);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }
        else {
            InMemoryJavaFileObject fileObject = (InMemoryJavaFileObject) o;
            return uri.equals(fileObject.uri) && kind.equals(fileObject.kind);
        }
    }

    @Override
    public int hashCode() {
        return uri.hashCode() + kind.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s: %s; %s", getClass().getSimpleName(), kind, uri);
    }

    private static void readReferenceClosed(InputStream reference, InMemoryJavaFileObject fileObject) {
        synchronized (fileObject.referencesCounter) {
            fileObject.referencesCounter.closedReadReference();
        }
    }

    private static void writeReferenceClosed(OutputStream reference, InMemoryJavaFileObject fileObject) {
        synchronized (fileObject.referencesCounter) {
            fileObject.referencesCounter.closedWriteReference();
            fileObject.lastModified = new Date();
        }
    }

    private static void writeReferenceBufferFlushed(OutputStream reference, InMemoryJavaFileObject fileObject) {
        fileObject.lastModified = new Date();
    }

    private static final class OpenedStreamsCounter {

        private int openedReadStreams;

        private int openedWriteStreams;

        public void openedReadReference() {
            ++openedReadStreams;
        }

        public void closedReadReference() {
            --openedReadStreams;
        }

        public boolean isOpenedForReading() {
            return openedReadStreams != 0;
        }

        public void openedWriteReference() {
            ++openedWriteStreams;
        }

        public void closedWriteReference() {
            --openedWriteStreams;
        }

        public boolean isOpenedForWriting() {
            return openedWriteStreams != 0;
        }
    }

    protected interface StreamClosedListener<T> {

        void streamClosed(T reference, InMemoryJavaFileObject fileObject);
    }

    protected interface OutputStreamFlushedListener<T> {

        void outputStreamFlushed(T reference, InMemoryJavaFileObject fileObject);
    }

    public static final class InMemoryFileOpenException extends IllegalStateException {

        public InMemoryFileOpenException() { }

        public InMemoryFileOpenException(String s) {
            super(s);
        }
    }

    private class CloseNotifyingByteArrayOutputStream extends ByteArrayOutputStream {

        private final StreamClosedListener<ByteArrayOutputStream> closedListener;

        private final OutputStreamFlushedListener<ByteArrayOutputStream> flushedListener;

        public CloseNotifyingByteArrayOutputStream(
            StreamClosedListener<ByteArrayOutputStream> closedListener,
            OutputStreamFlushedListener<ByteArrayOutputStream> flushedListener) {
            this.closedListener = closedListener;
            this.flushedListener = flushedListener;
        }

        @Override
        public void close() throws IOException {
            super.close();
            closedListener.streamClosed(this, InMemoryJavaFileObject.this);
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            flushedListener.outputStreamFlushed(this, InMemoryJavaFileObject.this);
        }
    }

    private class CloseNotifyingByteArrayInputStream extends ByteArrayInputStream {

        private StreamClosedListener<ByteArrayInputStream> closedListener;

        public CloseNotifyingByteArrayInputStream(byte[] bytes,
            StreamClosedListener<ByteArrayInputStream> closedListener) {
            super(bytes);
            this.closedListener = closedListener;
        }

        @Override
        public void close() throws IOException {
            super.close();
        }
    }
}
