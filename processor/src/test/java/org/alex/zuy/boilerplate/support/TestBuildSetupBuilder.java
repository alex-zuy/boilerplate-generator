package org.alex.zuy.boilerplate.support;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.alex.zuy.boilerplate.support.compiler.CompileOutputClassLoader;
import org.alex.zuy.boilerplate.support.compiler.InMemoryFileManager;
import org.alex.zuy.boilerplate.utils.IOUtils;

public class TestBuildSetupBuilder {

    private static final Collection<Diagnostic.Kind> FAILURE_DIAGNOSTIC_KIND = Arrays.asList(Diagnostic.Kind.values());

    private final InMemoryFileManager fileManager;

    private final Set<Processor> annotationProcessors = new HashSet<>();

    private final List<String> options = new ArrayList<>();

    private final Locale locale = Locale.getDefault();

    private final JavaCompiler compiler;

    private final AssertingSuccessfulBuildDiagnosticListenerDecorator diagnosticListener;

    private final CompileOutputClassLoader compileOutputClassLoader;

    private List<JavaFileObject> compilationUnits = new ArrayList<>();

    private DiagnosticListener<JavaFileObject> userProvidedListener;

    private TestBuildSetupBuilder() {
        diagnosticListener = new AssertingSuccessfulBuildDiagnosticListenerDecorator();
        compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileManager standardFileManager = compiler.getStandardFileManager(diagnosticListener, locale, null);
        fileManager = new InMemoryFileManager(standardFileManager);
        compileOutputClassLoader = new CompileOutputClassLoader(this.getClass().getClassLoader(), fileManager);
    }

    public TestBuildSetupBuilder addSourceFile(String sourceFilePath, String sourceFileContents) throws IOException {
        try (InputStream input = new ByteArrayInputStream(sourceFileContents.getBytes())) {
            compilationUnits.add(addFileToSourcePath(fileManager, URI.create(sourceFilePath), input));
        }
        return this;
    }

    public TestBuildSetupBuilder addTestSpecificSources(Class<?> testClass) throws IOException {
        return addTestSpecificSources(testClass, null);
    }

    public TestBuildSetupBuilder addTestSpecificSources(Class<?> testClass, String subdirectory) throws IOException {
        String resourceDir = subdirectory != null
            ? String.format("%s/%s", testClass.getSimpleName(), subdirectory)
            : testClass.getSimpleName();
        File testClassResourceDir = new File(testClass.getResource(resourceDir).getFile());
        Map<URI, File> files = collectFilesInResourceDir(testClassResourceDir);

        for (Map.Entry<URI, File> entry : files.entrySet()) {
            try (InputStream input = new FileInputStream(entry.getValue())) {
                compilationUnits.add(addFileToSourcePath(fileManager, entry.getKey(), input));
            }
        }

        return this;
    }

    public TestBuildSetupBuilder addAnnotationProcessor(Processor processor) {
        annotationProcessors.add(processor);
        return this;
    }

    public TestBuildSetupBuilder setDiagnosticListener(DiagnosticListener<JavaFileObject> diagnosticListener) {
        this.userProvidedListener = diagnosticListener;
        return this;
    }

    public TestBuildSetupBuilder addOptions(String... options) {
        this.options.addAll(Arrays.asList(options));
        return this;
    }

    public Callable<Boolean> createCompileTask(Writer outputWriter) {
        diagnosticListener.setTargetDiagnosticListener(userProvidedListener);
        CompilationTask compilerTask = compiler.getTask(outputWriter, fileManager, diagnosticListener, options, null,
            compilationUnits);
        compilerTask.setProcessors(annotationProcessors);
        return compilerTask;
    }

    public Class<?> getCompiledClass(String className) throws ClassNotFoundException {
        return compileOutputClassLoader.loadClass(className);
    }

    public static TestBuildSetupBuilder newInstance() {
        return new TestBuildSetupBuilder();
    }

    private static Map<URI, File> collectFilesInResourceDir(File resourceDir) {
        URI resourceDirUrl = URI.create(resourceDir.getPath());

        return new Object() {
            Stream<File> listFilesRecursively(File file) {
                return file.isDirectory()
                    ? Arrays.stream(file.listFiles()).flatMap(this::listFilesRecursively)
                    : Stream.of(file);
            }
        }.listFilesRecursively(resourceDir)
            .collect(
                Collectors.toMap(file -> resourceDirUrl.relativize(URI.create(file.getPath())), Function.identity()));
    }

    private static JavaFileObject addFileToSourcePath(JavaFileManager fileManager, URI filePath, InputStream contents)
        throws IOException {

        JavaFileObject javaSourceObject = fileManager.getJavaFileForOutput(StandardLocation.SOURCE_PATH,
            stripExtension(filePath.getPath()), JavaFileObject.Kind.SOURCE, null);

        try (OutputStream outputStream = javaSourceObject.openOutputStream()) {
            IOUtils.copy(contents, outputStream);
        }

        return javaSourceObject;
    }

    private static String stripExtension(String path) {
        final int lastDotIndex = path.lastIndexOf('.');
        return lastDotIndex > 0 ? path.substring(0, lastDotIndex) : path;
    }

    private static final class AssertingSuccessfulBuildDiagnosticListenerDecorator implements
        DiagnosticListener<JavaFileObject> {

        private DiagnosticListener<? super JavaFileObject> target;

        public void setTargetDiagnosticListener(DiagnosticListener<JavaFileObject> target) {
            this.target = target;
        }

        @Override
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            try {
                if (target != null) {
                    target.report(diagnostic);
                }
            }
            finally {
                if (FAILURE_DIAGNOSTIC_KIND.contains(diagnostic.getKind())) {
                    System.err.printf("Compilations ERROR!\n" +
                            "Message: %s\n" +
                            "Code: %s\n" +
                            "Kind: %s\n" +
                            "Line: %d\n" +
                            "Column: %d\n",
                        diagnostic.getMessage(Locale.getDefault()), diagnostic.getCode(), diagnostic.getKind(),
                        diagnostic.getLineNumber(), diagnostic.getColumnNumber());
                    if (diagnostic.getSource() != null) {
                        try (Reader reader = diagnostic.getSource().openReader(true)) {
                            String sourceFileName = diagnostic.getSource().getName();
                            String separator = ">>>>>>>>>>>>>>>>>>>>>>>>>>>>";
                            String source = IOUtils.readToString(reader);
                            System.err.printf("Source:\n" +
                                    "%s %s\n" +
                                    "%s\n" +
                                    "%s\n",
                                separator, sourceFileName, source, separator);
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    fail();
                }
            }
        }
    }
}
