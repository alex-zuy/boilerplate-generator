package org.alex.zuy.boilerplate.collector.support;

import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;

import org.alex.zuy.boilerplate.collector.BasePackageClassesCollector;
import org.alex.zuy.boilerplate.services.ImmutableProcessorContext;
import org.alex.zuy.boilerplate.services.ProcessorContext;

public abstract class AnnotationProcessorBase extends AbstractProcessor {

    private ProcessingEnvironment processingEnvironment;

    private BasePackageClassesCollector collector;

    private ProcessorContext processorContext;

    protected void beforeInit() {

    }

    protected void afterInit(ProcessingEnvironment processingEnvironment, ProcessorContext processorContext) {

    }

    @Override
    public final synchronized void init(ProcessingEnvironment processingEnvironment) {
        beforeInit();

        super.init(processingEnvironment);

        this.processingEnvironment = processingEnvironment;

        processorContext = ImmutableProcessorContext.builder()
            .elementUtils(processingEnvironment.getElementUtils())
            .filer(processingEnvironment.getFiler())
            .locale(processingEnvironment.getLocale())
            .messager(processingEnvironment.getMessager())
            .typeUtils(processingEnvironment.getTypeUtils())
            .build();

        afterInit(processingEnvironment, processorContext);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(com.example.primary.Trigger.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
