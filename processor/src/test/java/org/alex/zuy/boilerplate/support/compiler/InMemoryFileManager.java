package org.alex.zuy.boilerplate.support.compiler;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

public class InMemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private Map<URI, InMemoryJavaFileObject> files = new HashMap<>();

    public InMemoryFileManager(JavaFileManager standardJavaFileManager) {
        super(standardJavaFileManager);
    }

    @Override
    public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
        throws IOException {

        if (isBuiltinLocation(location)) {
            return super.list(location, packageName, kinds, recurse);
        } else {
            final String packagePath = packageName.replace('.', '/');
            Predicate<URI> packagePathPredicate = recurse
                ? uri -> isFilePresentUnderPath(uri, packagePath)
                : uri -> isFileDirectlyPresentUnderPath(uri, packagePath);
            return files.entrySet().stream()
                .filter(uriAndFile -> kinds.contains(uriAndFile.getValue().getKind()))
                .filter(uriAndFile -> packagePathPredicate.test(uriAndFile.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        }
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        if (isBuiltinLocation(location)) {
            return super.inferBinaryName(location, file);
        } else if (Kind.SOURCE.equals(file.getKind())) {
            return replaceFileExtension(file.toUri().toString(), Kind.SOURCE.extension, Kind.CLASS.extension);
        } else {
            return null;
        }
    }

    @Override
    public boolean isSameFile(FileObject lhs, FileObject rhs) {
        return lhs.equals(rhs);
    }

    @Override
    public boolean handleOption(String s, Iterator<String> iterator) {
        return false;
    }

    @Override
    public boolean hasLocation(Location location) {
        return true;
    }

    @Override
    public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) throws IOException {
        if (isBuiltinLocation(location)) {
            return super.getJavaFileForInput(location, className, kind);
        } else {
            return files.get(URI.create(getJavaFilePath(className, kind)));
        }
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling)
        throws IOException {
        if (isBuiltinLocation(location)) {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        } else {
            final String filePath = getJavaFilePath(className, kind);
            return files.computeIfAbsent(URI.create(filePath), uri -> new InMemoryJavaFileObject(kind, uri));
        }
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        return null;
    }

    @Override
    public FileObject getFileForOutput(Location location, String packageName, String relativeName,
        FileObject sibling) throws IOException {
        return null;
    }

    private String getJavaFilePath(String className, Kind kind) {
        return className.replace('.', '/') + kind.extension;
    }

    private boolean isFilePresentUnderPath(URI file, String path) {
        return file.toString().startsWith(path);
    }

    private boolean isFileDirectlyPresentUnderPath(URI file, String basePath) {
        if (file.toString().startsWith(basePath)) {
            final String pathToFileRelativeBasePath = file.toString().substring(basePath.length());
            return !pathToFileRelativeBasePath.contains("/");
        } else {
            return false;
        }
    }

    private String replaceFileExtension(String fileUri, String searchExtension, String replaceExtension) {
        return fileUri.substring(0, fileUri.lastIndexOf(searchExtension)) + replaceExtension;
    }

    private boolean isBuiltinLocation(Location location) {
        return StandardLocation.CLASS_PATH.equals(location) || StandardLocation.PLATFORM_CLASS_PATH.equals(location);
    }
}
