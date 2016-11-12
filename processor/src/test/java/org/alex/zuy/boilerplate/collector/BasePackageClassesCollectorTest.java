package org.alex.zuy.boilerplate.collector;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.collector.support.AnnotationProcessorBase;
import org.alex.zuy.boilerplate.collector.support.TypeElementsSetMatcher;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.support.TestBuildSetupBuilder;
import org.junit.Test;

public class BasePackageClassesCollectorTest {

    @Test
    public void testCollector() throws Exception {
        Boolean result = TestBuildSetupBuilder.newInstance()
            .addTestSpecificSources(this.getClass())
            .addAnnotationProcessor(new AnnotationProcessorImpl())
            .createCompileTask(null).call();
        assertTrue(result);
    }

    public static class AnnotationProcessorImpl extends AnnotationProcessorBase {

        private BasePackageClassesCollector collector;

        @Override
        protected void afterInit(ProcessingEnvironment processingEnvironment, ProcessorContext processorContext) {
            super.afterInit(processingEnvironment, processorContext);
            collector = new BasePackageClassesCollector(processorContext);
        }

        @Override
        public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
            assertThat(collector.collect("com.example.primary"),
                TypeElementsSetMatcher.isSetOfTypeElements("com.example.primary.ClassA", "com.example.primary.ClassB"));
            return false;
        }
    }
}
