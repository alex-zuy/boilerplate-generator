package org.alex.zuy.boilerplate.generator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Set;
import javax.annotation.processing.Completion;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.alex.zuy.boilerplate.config.ConfigException;
import org.alex.zuy.boilerplate.generatorimpl.ProcessorImpl;

public class AnnotationProcessor implements Processor {

    public static final int UNKNOWN_CONFIG_POSITION = -1;

    private ProcessorImpl processor;

    private Messager messager;

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        messager = processingEnvironment.getMessager();
        try {
            processor = new ProcessorImpl();
            processor.init(processingEnvironment);
        }
        catch (ConfigException e) {
            logError("Configuration reading error!");
            logException(e);
            throw e;
        }
        catch (Exception e) {
            logUnknownError(e);
            throw e;
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            return processor.process(set, roundEnvironment);
        }
        catch (Exception e) {
            logUnknownError(e);
            throw e;
        }
    }

    @Override
    public Set<String> getSupportedOptions() {
        try {
            return processor.getSupportedOptions();
        }
        catch (Exception e) {
            logUnknownError(e);
            throw e;
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        try {
            return processor.getSupportedAnnotationTypes();
        }
        catch (Exception e) {
            logUnknownError(e);
            throw e;
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        try {
            return processor.getSupportedSourceVersion();
        }
        catch (Exception e) {
            logUnknownError(e);
            throw e;
        }
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotationMirror,
        ExecutableElement executableElement, String s) {
        try {
            return processor.getCompletions(element, annotationMirror, executableElement, s);
        }
        catch (Exception e) {
            logUnknownError(e);
            throw e;
        }
    }

    private void logUnknownError(Throwable throwable) {
        logError("Unknown error!");
        logException(throwable);
    }

    private void logException(Throwable throwable) {
        logError("\t- %s\n%s", throwable.getMessage(), collectStackTrace(throwable));
    }

    private void logError(String msg, Object... args) {
        messager.printMessage(Kind.ERROR, String.format(msg, args));
    }

    /* This exception will never be thrown as StringWriter does not throw IOException */
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    private static String collectStackTrace(Throwable throwable) {
        try (Writer writer = new StringWriter();
             PrintWriter printWriter = new PrintWriter(writer)) {
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            return writer.toString();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
