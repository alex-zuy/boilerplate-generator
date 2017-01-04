package org.alex.zuy.boilerplate.collector;

import static org.alex.zuy.boilerplate.collector.support.TypeElementsSetMatcher.isSetOfTypeElements;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.support.AnnotationProcessorBase;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.support.TestBuildSetupBuilder;
import org.junit.Test;

public class BasePackageClassesCollectorTest {

    private static final String PACKAGE_TO_COLLECT = "com.example.primary";

    private AnnotationProcessorImpl annotationProcessor;

    private TestBuildSetupBuilder testBuildSetupBuilder;

    private Boolean compileResult;

    @Test
    public void testCollector() throws Exception {
        givenTestSpecificSourcesWillBeBuild();
        whenBuildPerformed();
        thenCollectedTypeElementsShouldBe("com.example.primary.ClassA", "com.example.primary.ClassB");
    }

    private void givenTestSpecificSourcesWillBeBuild() throws IOException {
        annotationProcessor = new AnnotationProcessorImpl();
        testBuildSetupBuilder = TestBuildSetupBuilder.newInstance()
            .addTestSpecificSources(this.getClass())
            .addAnnotationProcessor(annotationProcessor);
    }

    private void whenBuildPerformed() throws Exception {
        compileResult = testBuildSetupBuilder.createCompileTask(null).call();
    }

    private void thenCollectedTypeElementsShouldBe(String... expected) {
        assertTrue(compileResult);
        assertTrue(annotationProcessor.isWasProcessingInvoked());
        assertThat(annotationProcessor.getCollectedTypeElements(), isSetOfTypeElements(expected));
    }

    private static class AnnotationProcessorImpl extends AnnotationProcessorBase {

        private BasePackageClassesCollector collector;

        private Set<TypeElement> collectedTypeElements;

        public Set<TypeElement> getCollectedTypeElements() {
            return collectedTypeElements;
        }

        @Override
        protected void afterInit(ProcessingEnvironment processingEnvironment, ProcessorContext processorContext) {
            super.afterInit(processingEnvironment, processorContext);
            collector = new BasePackageClassesCollector(processorContext);
        }

        @Override
        public boolean processImpl(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
            collectedTypeElements = collector.collect(PACKAGE_TO_COLLECT);
            return false;
        }
    }
}
