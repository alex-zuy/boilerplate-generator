package com.github.alex.zuy.boilerplate.collector;

import static com.github.alex.zuy.boilerplate.collector.support.TypeElementsSetMatcher.isSetOfTypeElements;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.inject.Singleton;
import javax.lang.model.element.TypeElement;

import com.example.ExcludeMarker;
import com.github.alex.zuy.boilerplate.application.DomainClassesCollectorModule;
import com.github.alex.zuy.boilerplate.config.DomainConfig;
import com.github.alex.zuy.boilerplate.config.DomainConfig.ExcludesConfig;
import com.github.alex.zuy.boilerplate.config.DomainConfig.IncludesConfig;
import com.github.alex.zuy.boilerplate.config.ImmutableDomainConfig;
import com.github.alex.zuy.boilerplate.config.ImmutableExcludesConfig;
import com.github.alex.zuy.boilerplate.config.ImmutableIncludesConfig;
import com.github.alex.zuy.boilerplate.config.ImmutableMetadataGenerationStyle;
import com.github.alex.zuy.boilerplate.config.MetadataGenerationStyle;
import com.github.alex.zuy.boilerplate.services.ProcessorContext;
import com.github.alex.zuy.boilerplate.support.AnnotationProcessorBase;
import com.github.alex.zuy.boilerplate.support.ProcessorContextProviderModule;
import com.github.alex.zuy.boilerplate.support.SingleProcessingRoundAnnotationProcessorWrapper;
import com.github.alex.zuy.boilerplate.support.TestBuildSetupBuilder;
import dagger.Component;
import org.junit.Test;

public class DomainClassesCollectorImplTest {

    private IncludesConfig includesConfig;

    private ExcludesConfig excludesConfig;

    private TestBuildSetupBuilder testBuildSetupBuilder;

    private ProcessorImpl processor;

    private Boolean compileResult;

    @Test
    public void testAllCollectorsAreQueriedForDomainClasses() throws Exception {
        givenExistsThreeClassesIncludedByBasePackage();
        givenIncludesConfigWithBasePackage("com.example");
        giveExcludesConfigWithTypeAnnotationAndPattern(ExcludeMarker.class.getName(), "^.*\\.Excluded.*$");
        whenBuildPerformed();
        thenOneClassMustBeCollected("com.example.IncludedClass");
    }

    private void givenExistsThreeClassesIncludedByBasePackage() throws IOException {
        testBuildSetupBuilder = TestBuildSetupBuilder.newInstance()
            .addTestSpecificSources(this.getClass());
    }


    private void givenIncludesConfigWithBasePackage(String basePackage) {
        includesConfig = ImmutableIncludesConfig.builder()
            .addBasePackages(basePackage)
            .typeAnnotations(Collections.emptyList())
            .packageInfoAnnotations(Collections.emptyList())
            .build();
    }

    private void giveExcludesConfigWithTypeAnnotationAndPattern(String excludesAnnotation, String excludesPattern) {
        excludesConfig = ImmutableExcludesConfig.builder()
            .addTypeAnnotations(excludesAnnotation)
            .addPatterns(Pattern.compile(excludesPattern))
            .build();
    }

    private void whenBuildPerformed() throws Exception {
        MetadataGenerationStyle generationStyle = ImmutableMetadataGenerationStyle.builder()
            .propertyClassNameTemplate("")
            .relationshipsClassNameTemplate("")
            .relationshipsClassTerminalMethodNameTemplate("")
            .stringConstantStyle(MetadataGenerationStyle.StringConstantStyle.CAMELCASE)
            .build();
        DomainConfig domainConfig = ImmutableDomainConfig.builder()
            .includes(includesConfig)
            .excludes(excludesConfig)
            .generationStyle(generationStyle)
            .build();
        processor = new ProcessorImpl(domainConfig);
        compileResult = testBuildSetupBuilder.addAnnotationProcessor(
            SingleProcessingRoundAnnotationProcessorWrapper.newInstance(processor))
            .createCompileTask(null)
            .call();
    }

    private void thenOneClassMustBeCollected(String includedClass) {
        assertTrue(compileResult);
        assertTrue(processor.isWasProcessingInvoked());
        assertThat(processor.getCollectedElements(), isSetOfTypeElements(includedClass));
    }

    @Component(modules = {DomainClassesCollectorModule.class, ProcessorContextProviderModule.class})
    @Singleton
    interface DomainClassesCollectorComponent {

        DomainClassesCollector domainClassesCollector();
    }

    private static final class ProcessorImpl extends AnnotationProcessorBase {

        private DomainConfig domainConfig;

        private DomainClassesCollector collector;

        private Set<TypeElement> collectedElements;

        private ProcessorImpl(DomainConfig domainConfig) {
            this.domainConfig = domainConfig;
        }

        public Set<TypeElement> getCollectedElements() {
            return collectedElements;
        }

        @Override
        protected void afterInit(ProcessingEnvironment processingEnvironment, ProcessorContext processorContext) {
            super.afterInit(processingEnvironment, processorContext);
            DomainClassesCollectorComponent collectorComponent = DaggerDomainClassesCollectorImplTest_DomainClassesCollectorComponent.builder()
                .domainClassesCollectorModule(new DomainClassesCollectorModule())
                .processorContextProviderModule(new ProcessorContextProviderModule(processorContext))
                .build();
            collector = collectorComponent.domainClassesCollector();
        }

        @Override
        public boolean processImpl(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
            collectedElements = collector.collect(domainConfig, roundEnvironment);
            return true;
        }

        @Override
        public Set<String> getSupportedAnnotationTypes() {
            return Collections.singleton("*");
        }
    }
}
