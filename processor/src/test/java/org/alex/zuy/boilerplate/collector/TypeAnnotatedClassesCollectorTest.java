package org.alex.zuy.boilerplate.collector;

import static org.alex.zuy.boilerplate.collector.support.TypeElementsSetMatcher.isSetOfTypeElements;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import com.example.Marker;
import org.alex.zuy.boilerplate.support.AnnotationProcessorBase;
import org.alex.zuy.boilerplate.support.SingleProcessingRoundAnnotationProcessorWrapper;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.support.TestBuildSetupBuilder;
import org.junit.Test;

public class TypeAnnotatedClassesCollectorTest {

    private TestBuildSetupBuilder testBuildSetupBuilder;

    private Boolean compileResult;

    private ProcessorImpl processor;

    @Test
    public void testCollector() throws Exception {
        givenTestSpecificSourceWillBeBuild();
        whenBuildPerformed();
        thenCollectedTypeElementsShouldBe("com.example.marked.ClassA");
    }

    private void givenTestSpecificSourceWillBeBuild() throws IOException {
        processor = new ProcessorImpl();
        testBuildSetupBuilder = TestBuildSetupBuilder.newInstance()
            .addTestSpecificSources(this.getClass())
            .addAnnotationProcessor(SingleProcessingRoundAnnotationProcessorWrapper.newInstance(processor));
    }

    private void whenBuildPerformed() throws Exception {
        compileResult = testBuildSetupBuilder.createCompileTask(null).call();
    }

    private void thenCollectedTypeElementsShouldBe(String... expected) {
        assertTrue(compileResult);
        assertTrue(processor.isWasProcessingInvoked());
        assertThat(processor.getCollectedTypeElements(), isSetOfTypeElements("com.example.first.ClassA"));
    }

    private static final class ProcessorImpl extends AnnotationProcessorBase {

        private TypeAnnotatedClassesCollector collector;

        private Set<TypeElement> collectedTypeElements;

        public Set<TypeElement> getCollectedTypeElements() {
            return collectedTypeElements;
        }

        @Override
        protected void afterInit(ProcessingEnvironment processingEnvironment, ProcessorContext processorContext) {
            super.afterInit(processingEnvironment, processorContext);
            collector = new TypeAnnotatedClassesCollector(processorContext);
        }

        @Override
        public boolean processImpl(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
            collectedTypeElements = collector.collect(Marker.class.getName(), roundEnvironment);
            return true;
        }

        @Override
        public Set<String> getSupportedAnnotationTypes() {
            return Collections.singleton(Marker.class.getName());
        }
    }
}
