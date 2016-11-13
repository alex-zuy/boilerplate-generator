package org.alex.zuy.boilerplate.collector.support;

import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import com.example.Trigger;
import org.alex.zuy.boilerplate.services.ImmutableProcessorContext;
import org.alex.zuy.boilerplate.services.ProcessorContext;

public abstract class AnnotationProcessorBase extends AbstractProcessor {

    private ProcessingEnvironment processingEnvironment;

    private boolean wasProcessingInvoked;

    public boolean isWasProcessingInvoked() {
        return wasProcessingInvoked;
    }

    protected void beforeInit() {

    }

    protected void afterInit(ProcessingEnvironment processingEnvironment, ProcessorContext processorContext) {

    }

    public abstract boolean processImpl(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment);

    @Override
    public final synchronized void init(ProcessingEnvironment processingEnvironment) {
        beforeInit();

        super.init(processingEnvironment);

        this.processingEnvironment = processingEnvironment;

        ProcessorContext processorContext = ImmutableProcessorContext.builder()
            .elementUtils(processingEnvironment.getElementUtils())
            .filer(processingEnvironment.getFiler())
            .locale(processingEnvironment.getLocale())
            .messager(processingEnvironment.getMessager())
            .typeUtils(processingEnvironment.getTypeUtils())
            .build();

        afterInit(processingEnvironment, processorContext);
    }

    @Override
    public final boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        wasProcessingInvoked = true;
        return processImpl(set, roundEnvironment);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Trigger.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
