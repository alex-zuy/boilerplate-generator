package org.alex.zuy.boilerplate.collector;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import com.example.primary.Marker;
import org.alex.zuy.boilerplate.collector.support.AnnotationProcessorBase;
import org.alex.zuy.boilerplate.collector.support.SingleProcessingRoundAnnotationProcessorWrapper;
import org.alex.zuy.boilerplate.collector.support.TypeElementsSetMatcher;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.support.TestBuildSetupBuilder;
import org.junit.Test;

public class PackageInfoAnnotatedClassesCollectorJcaTest {

    @Test
    public void testCollector() throws Exception {
        Boolean result = TestBuildSetupBuilder.newInstance()
            .addTestSpecificSources(this.getClass())
            .addAnnotationProcessor(SingleProcessingRoundAnnotationProcessorWrapper.newInstance(new ProcessorImpl()))
            .createCompileTask(null)
            .call();

        assertTrue(result);
    }

    private static final class ProcessorImpl extends AnnotationProcessorBase {

        private PackageInfoAnnotatedClassesCollector collector;

        @Override
        protected void afterInit(ProcessingEnvironment processingEnvironment, ProcessorContext processorContext) {
            super.afterInit(processingEnvironment, processorContext);
            collector = new PackageInfoAnnotatedClassesCollector();
        }

        @Override
        public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
            Set<TypeElement> typeElements = collector.collect(Marker.class.getName(), roundEnvironment);
            assertThat(typeElements, TypeElementsSetMatcher.isSetOfTypeElements("com.example.marked.ClassA"));
            return true;
        }

        @Override
        public Set<String> getSupportedAnnotationTypes() {
            return Collections.singleton(Marker.class.getName());
        }
    }
}
