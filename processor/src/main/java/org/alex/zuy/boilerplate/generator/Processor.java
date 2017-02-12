
package org.alex.zuy.boilerplate.generator;

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
import org.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGenerator;
import org.alex.zuy.boilerplate.services.ImmutableProcessorContext;
import org.alex.zuy.boilerplate.services.ImmutableRoundContext;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.services.RoundContext;

public class Processor extends AbstractProcessor {

    private static final String OPTION_CONFIG_PATH = "configPath";

    private ConfigurationProvider configurationProvider;

    private ProcessorComponent processorComponent;

    /* TODO */
    @SuppressWarnings("PMD.SystemPrintln")
    @Override
    public void init(final ProcessingEnvironment processingEnvironment) {
        Map<String, String> options = processingEnvironment.getOptions();
        if (options.containsKey(OPTION_CONFIG_PATH)) {
            String configPath = options.get(OPTION_CONFIG_PATH);
            try {
                configurationProvider = new ConfigurationProviderImpl(new File(configPath).toURI().toURL());

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
            catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        else {
            String message = "No config provided!";
            System.err.println(message);
            throw new IllegalStateException(message);
        }
    }

    @Override
    public boolean process(final Set<? extends TypeElement> set,
        final RoundEnvironment roundEnvironment) {
        RoundContext roundContext = ImmutableRoundContext.builder()
            .annotations(set)
            .roundEnvironment(roundEnvironment)
            .build();

        BeanDomainMetadataGenerator metadataGenerator = processorComponent.getMetadataGenerator();
        metadataGenerator.generateDomainMetadataClasses(roundContext, configurationProvider.getDomainConfig(),
            configurationProvider.getSupportClassesConfig());

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
        return Collections.singleton(OPTION_CONFIG_PATH);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
