package org.alex.zuy.boilerplate.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import com.example.Trigger;
import org.alex.zuy.boilerplate.application.BeanDomainAnalysisModule;
import org.alex.zuy.boilerplate.domain.BeanClass;
import org.alex.zuy.boilerplate.domain.BeanProperty;
import org.alex.zuy.boilerplate.domain.BeanProperty.AccessModifier;
import org.alex.zuy.boilerplate.domain.types.ExactType;
import org.alex.zuy.boilerplate.domain.types.Types;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.support.AnnotationProcessorBase;
import org.alex.zuy.boilerplate.support.ProcessorContextProviderModule;
import org.alex.zuy.boilerplate.support.ProcessorTestsSteps;
import org.alex.zuy.boilerplate.support.SingleProcessingRoundAnnotationProcessorWrapper;
import org.alex.zuy.boilerplate.support.TestBuildSetupBuilder;
import org.junit.Test;

public class BeanClassAnalyserImplTest {

    public static final String PACKAGE_NAME = "com.example";

    private ProcessorImpl processor = new ProcessorImpl();

    private TestBuildSetupBuilder testBuildSetupBuilder = TestBuildSetupBuilder.newInstance();

    private ProcessorTestsSteps processorTestsSteps = new ProcessorTestsSteps(testBuildSetupBuilder, getClass());

    @Test
    public void testSingleGetterAndSetterPerProperty() throws Exception {
        String fileName = "SingleGetterAndSetterPerProperty";
        BeanProperty heightProperty = new BeanProperty("height", makeIntType(), AccessModifier.PUBLIC);
        BeanClass beanClass = new BeanClass(makeClassType(fileName),
            Arrays.asList(heightProperty));

        givenSourceFiles(fileName);
        whenBeanTypesAnalysed();
        thenBeanClassShouldBe(beanClass);
    }

    private ExactType makeIntType() {return Types.makeExactType("int");}

    @Test
    public void testMultipleSettersPerProperty() throws Exception {
        String fileName = "MultipleSettersPerProperty";
        BeanProperty widthProperty = new BeanProperty("width", makeIntType(), AccessModifier.PROTECTED);
        BeanClass beanClass = new BeanClass(makeClassType(fileName),
            Arrays.asList(widthProperty));

        givenSourceFiles(fileName);
        whenBeanTypesAnalysed();
        thenBeanClassShouldBe(beanClass);
    }

    @Test
    public void testMethodNamesStartWithGetSet() throws Exception {
        String fileName = "MethodNamesStartWithGetSet";
        BeanClass beanClass = new BeanClass(makeClassType(fileName),
            Collections.emptyList());

        givenSourceFiles(fileName);
        whenBeanTypesAnalysed();
        thenBeanClassShouldBe(beanClass);
    }

    @Test
    public void testBooleanPropertyGetterNameCanStartWith_is() throws Exception {
        String fileName = "BooleanPropertyGetterNameCanStartWith";
        BeanProperty beanProperty = new BeanProperty("bean", Types.makeExactType("boolean"), AccessModifier.PUBLIC);
        BeanProperty nullableProperty = new BeanProperty("nullable",
            Types.makeExactType("java.lang.Boolean", "java.lang"), AccessModifier.PUBLIC);
        BeanClass beanClass = new BeanClass(makeClassType(fileName),
            Arrays.asList(beanProperty, nullableProperty));

        givenSourceFiles(fileName);
        whenBeanTypesAnalysed();
        thenBeanClassShouldBe(beanClass);
    }

    private ExactType makeClassType(String fileName) {
        return Types.makeExactType(getClassQualifiedName(fileName), PACKAGE_NAME);
    }

    private String getClassQualifiedName(String fileName) {
        return String.format("%s.%s", PACKAGE_NAME, fileName);
    }

    private void givenSourceFiles(String... fileNames) throws IOException {
        for (final String fileName : fileNames) {
            processorTestsSteps.addTestSpecificSourceFile(PACKAGE_NAME, fileName);
        }
    }

    private void whenBeanTypesAnalysed() throws Exception {
        processor = new ProcessorImpl();
        Boolean result = testBuildSetupBuilder
            .addAnnotationProcessor(SingleProcessingRoundAnnotationProcessorWrapper.newInstance(processor))
            .createCompileTask(null)
            .call();
        assertTrue(result);
    }


    private void thenBeanClassShouldBe(BeanClass expectedBeanClass) {
        BeanClass actualBeanClass = processor.getBeanClass();
        assertEquals(expectedBeanClass.getType(), actualBeanClass.getType());
        assertEquals(expectedBeanClass.getProperties(), actualBeanClass.getProperties());
    }

    private static final class ProcessorImpl extends AnnotationProcessorBase {

        private BeanClassAnalyser analyser;

        private BeanClass beanClass;

        @Override
        protected void afterInit(ProcessingEnvironment processingEnvironment, ProcessorContext processorContext) {
            super.afterInit(processingEnvironment, processorContext);
            BeanDomainAnalysisComponent analysisComponent = DaggerBeanDomainAnalysisComponent.builder()
                .processorContextProviderModule(new ProcessorContextProviderModule(processorContext))
                .beanDomainAnalysisModule(new BeanDomainAnalysisModule())
                .build();
            analyser = analysisComponent.beanClassAnalyser();
        }

        @Override
        public boolean processImpl(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
            beanClass = analyser.analyse(
                (TypeElement) roundEnvironment.getElementsAnnotatedWith(Trigger.class).iterator().next());
            return false;
        }

        public BeanClass getBeanClass() {
            return beanClass;
        }
    }
}
