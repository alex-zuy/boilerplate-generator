
package org.alex.zuy.boilerplate.generator.impl;

import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.api.annotations.BeanMetadataConfiguration;
import org.alex.zuy.boilerplate.application.DaggerProcessorComponent;
import org.alex.zuy.boilerplate.application.ProcessorComponent;
import org.alex.zuy.boilerplate.application.ProcessorContextProviderModule;
import org.alex.zuy.boilerplate.config.AnnotationConfigurationProvider;
import org.alex.zuy.boilerplate.config.ConfigurationProvider;
import org.alex.zuy.boilerplate.config.DomainConfig;
import org.alex.zuy.boilerplate.config.SupportClassesConfig;
import org.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGenerator;
import org.alex.zuy.boilerplate.services.ImmutableProcessorContext;
import org.alex.zuy.boilerplate.services.ImmutableRoundContext;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.services.RoundContext;

public class ProcessorImpl extends AbstractProcessor {

    private ProcessorComponent processorComponent;

    private boolean wasProcessingPerformed;

    @Override
    public void init(final ProcessingEnvironment processingEnvironment) {
        ProcessorContext processorContext = ImmutableProcessorContext.builder()
            .elementUtils(processingEnvironment.getElementUtils())
            .typeUtils(processingEnvironment.getTypeUtils())
            .filer(processingEnvironment.getFiler())
            .messager(processingEnvironment.getMessager())
            .locale(processingEnvironment.getLocale())
            .build();

        processorComponent = DaggerProcessorComponent.builder()
            .processorContextProviderModule(new ProcessorContextProviderModule(processorContext))
            .build();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        if (!wasProcessingPerformed) {
            wasProcessingPerformed = true;

            RoundContext roundContext = ImmutableRoundContext.builder()
                .annotations(set)
                .roundEnvironment(roundEnvironment)
                .build();

            ConfigurationProvider configurationProvider = new AnnotationConfigurationProvider(roundContext);

            DomainConfig domainConfig = configurationProvider.getDomainConfig();
            SupportClassesConfig supportClassesConfig = configurationProvider.getSupportClassesConfig();
            BeanDomainMetadataGenerator metadataGenerator = processorComponent.getMetadataGenerator();
            metadataGenerator.generateDomainMetadataClasses(roundContext, domainConfig, supportClassesConfig);
        }

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BeanMetadataConfiguration.class.getName());
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Collections.emptySet();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
