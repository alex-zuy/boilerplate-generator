
package com.github.alex.zuy.boilerplate.generatorimpl;

import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import com.github.alex.zuy.boilerplate.api.annotations.BeanMetadataConfiguration;
import com.github.alex.zuy.boilerplate.application.DaggerProcessorComponent;
import com.github.alex.zuy.boilerplate.application.ProcessorComponent;
import com.github.alex.zuy.boilerplate.application.ProcessorContextProviderModule;
import com.github.alex.zuy.boilerplate.config.AnnotationConfigurationProvider;
import com.github.alex.zuy.boilerplate.config.ConfigurationProvider;
import com.github.alex.zuy.boilerplate.config.DomainConfig;
import com.github.alex.zuy.boilerplate.config.SupportClassesConfig;
import com.github.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGenerator;
import com.github.alex.zuy.boilerplate.services.ImmutableProcessorContext;
import com.github.alex.zuy.boilerplate.services.ImmutableRoundContext;
import com.github.alex.zuy.boilerplate.services.ProcessorContext;
import com.github.alex.zuy.boilerplate.services.RoundContext;

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
