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
import org.alex.zuy.boilerplate.JdkTypes;
import org.alex.zuy.boilerplate.application.BeanDomainAnalysisModule;
import org.alex.zuy.boilerplate.domain.BeanClass;
import org.alex.zuy.boilerplate.domain.BeanProperty;
import org.alex.zuy.boilerplate.domain.BeanProperty.AccessModifier;
import org.alex.zuy.boilerplate.domain.QualifiedName;
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

    private static final String PACKAGE_NAME = "com.example";

    private ProcessorImpl processor = new ProcessorImpl();

    private TestBuildSetupBuilder testBuildSetupBuilder = TestBuildSetupBuilder.newInstance();

    private ProcessorTestsSteps processorTestsSteps = new ProcessorTestsSteps(testBuildSetupBuilder, getClass());

    @Test
    public void testSingleGetterAndSetterPerProperty() throws Exception {
        String fileName = "SingleGetterAndSetterPerProperty";
        BeanProperty heightProperty = new BeanProperty("height", JdkTypes.JAVA_LANG_INTEGER, AccessModifier.PUBLIC);
        BeanClass beanClass = new BeanClass(makeClassType(fileName),
            Arrays.asList(heightProperty));

        givenSourceFiles(fileName);
        whenBeanTypesAnalysed();
        thenBeanClassShouldBe(beanClass);
    }

    @Test
    public void testMultipleSettersPerProperty() throws Exception {
        String fileName = "MultipleSettersPerProperty";
        BeanProperty widthProperty = new BeanProperty("width", JdkTypes.PRIMITIVE_INT, AccessModifier.PROTECTED);
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
        BeanProperty beanProperty = new BeanProperty("bean", JdkTypes.PRIMITIVE_BOOLEAN, AccessModifier.PUBLIC);
        BeanProperty nullableProperty = new BeanProperty("nullable",
            JdkTypes.JAVA_LANG_BOOLEAN, AccessModifier.PUBLIC);
        BeanClass beanClass = new BeanClass(makeClassType(fileName),
            Arrays.asList(beanProperty, nullableProperty));

        givenSourceFiles(fileName);
        whenBeanTypesAnalysed();
        thenBeanClassShouldBe(beanClass);
    }

    @Test
    public void testBeanWithDirectlyInheritedProperties() throws Exception {
        BeanClass beanClass = makeBeanClassWithSingleNameProperty();

        givenSourceFilesInDirectory("directlyInheritedProperties");
        whenBeanTypesAnalysed();
        thenBeanClassShouldBe(beanClass);
    }

    @Test
    public void testBeanWithPropertiesInheritedFromSuperSuperType() throws Exception {
        BeanClass beanClass = makeBeanClassWithSingleNameProperty();

        givenSourceFilesInDirectory("propertiesInheritedFromSuperSuperType");
        whenBeanTypesAnalysed();
        thenBeanClassShouldBe(beanClass);
    }

    @Test
    public void testBeanWithPropertiesInheritedFromInterface() throws Exception {
        BeanClass beanClass = makeBeanClassWithSingleNameProperty();

        givenSourceFilesInDirectory("propertiesInheritedFromInterface");
        whenBeanTypesAnalysed();
        thenBeanClassShouldBe(beanClass);
    }

    @Test
    public void testBeanImplementsInterfaceWithProperties() throws Exception {
        BeanClass beanClass = makeBeanClassWithSingleNameProperty();

        givenSourceFilesInDirectory("beanImplementsInterfaceWithProperties");
        whenBeanTypesAnalysed();
        thenBeanClassShouldBe(beanClass);
    }


    @Test
    public void testBeanImplementsInterfaceUsingInheritedMethods() throws Exception {
        BeanClass beanClass = makeBeanClassWithSingleNameProperty();

        givenSourceFilesInDirectory("beanImplementsInterfaceUsingInheritedMethods");
        whenBeanTypesAnalysed();
        thenBeanClassShouldBe(beanClass);
    }

    @Test
    public void testBeanInheritsGenericBean() throws Exception {
        BeanClass beanClass = makeBeanClassWithSingleNameProperty();

        givenSourceFilesInDirectory("beanInheritsGenericBean");
        whenBeanTypesAnalysed();
        thenBeanClassShouldBe(beanClass);
    }

    private void givenSourceFilesInDirectory(String directory) throws IOException {
        processorTestsSteps.addTestSpecificSourceFilesInDirectory(directory);
    }

    private ExactType makeClassType(String fileName) {
        return Types.makeExactType(new QualifiedName(fileName, PACKAGE_NAME));
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
        assertEquals(expectedBeanClass.getProperties().size(), actualBeanClass.getProperties().size());
        expectedBeanClass.getProperties().forEach((name, expectedProperty) -> {
            assertPropertiesEquals(expectedProperty, actualBeanClass.getProperties().get(name));
        });
    }

    private void assertPropertiesEquals(BeanProperty expectedProperty, BeanProperty actualProperty) {
        assertEquals(expectedProperty.getName(), actualProperty.getName());
        assertEquals(expectedProperty.getType(), actualProperty.getType());
    }

    private BeanClass makeBeanClassWithSingleNameProperty() {
        return new BeanClass(makeClassType("Bean"),
            Collections.singletonList(new BeanProperty("name", JdkTypes.JAVA_LANG_STRING, AccessModifier.PUBLIC)));
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
