package org.alex.zuy.boilerplate.collector;

import static org.alex.zuy.boilerplate.collector.support.TypeElementsSetMatcher.isSetOfTypeElements;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import com.example.ExcludeMarker;
import org.alex.zuy.boilerplate.collector.DomainConfig.ExcludesConfig;
import org.alex.zuy.boilerplate.collector.DomainConfig.IncludesConfig;
import org.alex.zuy.boilerplate.support.AnnotationProcessorBase;
import org.alex.zuy.boilerplate.support.SingleProcessingRoundAnnotationProcessorWrapper;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.support.TestBuildSetupBuilder;
import org.junit.Test;

public class DomainClassesCollectorTest {

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
        DomainConfig domainConfig = ImmutableDomainConfig.builder()
            .includes(includesConfig)
            .excludes(excludesConfig)
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
            collector = new DomainClassesCollector(domainConfig, new CollectorComponentsFactory(processorContext));
        }

        @Override
        public boolean processImpl(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
            collectedElements = collector.collect(roundEnvironment);
            return true;
        }

        @Override
        public Set<String> getSupportedAnnotationTypes() {
            return Collections.singleton("*");
        }
    }
}
