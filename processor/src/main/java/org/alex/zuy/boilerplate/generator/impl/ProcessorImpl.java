
package org.alex.zuy.boilerplate.generator.impl;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.application.DaggerProcessorComponent;
import org.alex.zuy.boilerplate.application.ProcessorComponent;
import org.alex.zuy.boilerplate.application.ProcessorContextProviderModule;
import org.alex.zuy.boilerplate.config.ConfigurationProvider;
import org.alex.zuy.boilerplate.config.ConfigurationProviderImpl;
import org.alex.zuy.boilerplate.config.DomainConfig;
import org.alex.zuy.boilerplate.config.SupportClassesConfig;
import org.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGenerator;
import org.alex.zuy.boilerplate.services.ImmutableProcessorContext;
import org.alex.zuy.boilerplate.services.ImmutableRoundContext;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.services.RoundContext;

public class ProcessorImpl extends AbstractProcessor {

    private static final String OPTION_NAME_CONFIG_PATH = "configPath";

    private ConfigurationProvider configurationProvider;

    private ProcessorComponent processorComponent;

    @Override
    public void init(final ProcessingEnvironment processingEnvironment) {
        Map<String, String> options = processingEnvironment.getOptions();
        if (options.containsKey(OPTION_NAME_CONFIG_PATH)) {
            String configPath = options.get(OPTION_NAME_CONFIG_PATH);
            configurationProvider = new ConfigurationProviderImpl(new File(configPath));

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
        else {
            throw new AbsentOptionException(OPTION_NAME_CONFIG_PATH);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        RoundContext roundContext = ImmutableRoundContext.builder()
            .annotations(set)
            .roundEnvironment(roundEnvironment)
            .build();

        DomainConfig domainConfig = configurationProvider.getDomainConfig();
        SupportClassesConfig supportClassesConfig = configurationProvider.getSupportClassesConfig();
        BeanDomainMetadataGenerator metadataGenerator = processorComponent.getMetadataGenerator();
        metadataGenerator.generateDomainMetadataClasses(roundContext, domainConfig, supportClassesConfig);

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        DomainConfig domainConfig = configurationProvider.getDomainConfig();
        Set<String> annotations = new HashSet<>();
        annotations.addAll(domainConfig.includes().packageInfoAnnotations());
        annotations.addAll(domainConfig.includes().typeAnnotations());
        annotations.addAll(domainConfig.excludes().typeAnnotations());
        return annotations;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Collections.singleton(OPTION_NAME_CONFIG_PATH);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
