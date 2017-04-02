package org.alex.zuy.boilerplate.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.JdkTypes;
import org.alex.zuy.boilerplate.application.BeanDomainAnalysisModule;
import org.alex.zuy.boilerplate.domain.QualifiedName;
import org.alex.zuy.boilerplate.domain.types.Type;
import org.alex.zuy.boilerplate.domain.types.Types;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.support.AnnotationProcessorBase;
import org.alex.zuy.boilerplate.support.ProcessorContextProviderModule;
import org.alex.zuy.boilerplate.support.ProcessorTestsSteps;
import org.alex.zuy.boilerplate.support.SingleProcessingRoundAnnotationProcessorWrapper;
import org.alex.zuy.boilerplate.support.TestBuildSetupBuilder;
import org.junit.Test;

public class TypeAnalyserImplTest {

    private static final String PACKAGE_NAME = "com.example";

    private TestBuildSetupBuilder testBuildSetupBuilder = TestBuildSetupBuilder.newInstance();

    private ProcessorImpl processor;

    private ProcessorTestsSteps processorTestsSteps = new ProcessorTestsSteps(testBuildSetupBuilder, getClass());

    @Test
    public void testPrimitiveTypes() throws Exception {
        String className = "PrimitiveTypes";
        List<Type<?>> expectedTypes = Stream.of("boolean", "byte", "short", "int", "long", "float", "double")
            .map(primitiveTypeName -> Types.makeExactType(new QualifiedName(primitiveTypeName)))
            .collect(Collectors.toList());

        givenSourceFilesInNamedPackage(className);
        whenReturnTypesAnalysed();
        thenCollectedTypesShouldBe(getFullClassName(className), expectedTypes);
    }

    @Test
    public void testArrayTypes() throws Exception {
        String className = "ArrayTypes";
        List<Type<?>> expectedTypes = new ArrayList<>();
        expectedTypes.add(Types.makeArrayType(JdkTypes.PRIMITIVE_INT));
        expectedTypes.add(Types.makeArrayType(JdkTypes.JAVA_LANG_INTEGER));
        expectedTypes.add(
            Types.makeArrayType(Types.makeArrayType(JdkTypes.JAVA_LANG_OBJECT)));

        givenSourceFilesInNamedPackage(className);
        whenReturnTypesAnalysed();
        thenCollectedTypesShouldBe(getFullClassName(className), expectedTypes);
    }

    @Test
    public void testGenericTypes() throws Exception {
        String className = "GenericTypes";
        List<Type<?>> expectedTypes = new ArrayList<>();
        expectedTypes.add(Types.makeTypeInstance(new QualifiedName("List", "java.util"), Collections.singletonList(
            JdkTypes.JAVA_LANG_INTEGER)));
        expectedTypes.add(Types.makeTypeInstance(new QualifiedName("Map", "java.util"),
            Arrays.asList(JdkTypes.JAVA_LANG_STRING, JdkTypes.JAVA_LANG_INTEGER)));
        expectedTypes.add(Types.makeTypeInstance(new QualifiedName("List", "java.util"),
            Collections.singletonList(Types.makeTypeParameter(new QualifiedName("T")))));
        expectedTypes.add(Types.makeTypeParameter(new QualifiedName("R")));

        givenSourceFilesInNamedPackage(className);
        whenReturnTypesAnalysed();
        thenCollectedTypesShouldBe(getFullClassName(className), expectedTypes);
    }

    @Test
    public void testTypesInUnnamedPackages() throws Exception {
        String declaredClassName = "DeclaredClass";
        String referencingClassName = "ReferencingClass";
        List<Type<?>> expectedTypes = new ArrayList<>();
        expectedTypes.add(Types.makeExactType(new QualifiedName("DeclaredClass")));

        givenSourceFilesInUnnamedPackage(declaredClassName, referencingClassName);
        whenReturnTypesAnalysed();
        thenCollectedTypesShouldBe(referencingClassName, expectedTypes);
    }

    @Test
    public void testNestedTypesInNamedPackage() throws Exception {
        String className = "NestedTypesInNamedPackage";
        QualifiedName enclosingType = new QualifiedName(className, PACKAGE_NAME);
        QualifiedName innerClassHolder = new QualifiedName("InnerClassHolder", PACKAGE_NAME, enclosingType);
        List<Type<?>> expectedTypes = new ArrayList<>();
        expectedTypes.add(Types.makeExactType(new QualifiedName("InnerClass", PACKAGE_NAME, enclosingType)));
        expectedTypes.add(
            Types.makeExactType(new QualifiedName("InnerClassHolderInnerClass", PACKAGE_NAME, innerClassHolder)));

        givenSourceFilesInNamedPackage(className);
        whenReturnTypesAnalysed();
        thenCollectedTypesShouldBe(getFullClassName(className), expectedTypes);
    }

    @Test
    public void testNestedTypesInUnnamedPackage() throws Exception {
        String className = "NestedTypesInUnnamedPackage";
        QualifiedName enclosingType = new QualifiedName(className);
        List<Type<?>> expectedTypes = new ArrayList<>();
        expectedTypes.add(Types.makeExactType(new QualifiedName("InnerClass", enclosingType)));

        givenSourceFilesInNamedPackage(className);
        whenReturnTypesAnalysed();
        thenCollectedTypesShouldBe(className, expectedTypes);
    }

    private void givenSourceFilesInNamedPackage(String... fileNames) throws IOException {
        for (final String fileName : fileNames) {
            processorTestsSteps.addTestSpecificSourceFile(PACKAGE_NAME, fileName);
        }
    }

    private void givenSourceFilesInUnnamedPackage(String... fileNames) throws IOException {
        for (final String fileName : fileNames) {
            processorTestsSteps.addTestSpecificSourceFile(fileName);
        }
    }

    private void whenReturnTypesAnalysed() throws Exception {
        processor = new ProcessorImpl();
        Boolean result = testBuildSetupBuilder
            .addAnnotationProcessor(SingleProcessingRoundAnnotationProcessorWrapper.newInstance(processor))
            .createCompileTask(null)
            .call();
        assertTrue(result);
    }

    private void thenCollectedTypesShouldBe(String fullClassName, List<Type<?>> expectedTypes) {
        List<Type<?>> actualTypes = processor.getTypes().get(fullClassName);
        assertEquals(expectedTypes, actualTypes);
    }

    private String getFullClassName(String simpleName) {
        return String.format("%s.%s", PACKAGE_NAME, simpleName);
    }

    private static final class ProcessorImpl extends AnnotationProcessorBase {

        private Map<String, List<Type<?>>> types = new HashMap<>();

        private TypeAnalyser analyser;

        @Override
        protected void afterInit(ProcessingEnvironment processingEnvironment, ProcessorContext processorContext) {
            super.afterInit(processingEnvironment, processorContext);
            BeanDomainAnalysisComponent analysisComponent = DaggerBeanDomainAnalysisComponent.builder()
                .processorContextProviderModule(new ProcessorContextProviderModule(processorContext))
                .beanDomainAnalysisModule(new BeanDomainAnalysisModule())
                .build();
            analyser = analysisComponent.typeAnalyser();
        }

        @Override
        public boolean processImpl(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
            List<ElementKind> elementKinds = Arrays.asList(ElementKind.CLASS, ElementKind.INTERFACE);
            set.stream()
                .flatMap(annotation -> roundEnvironment.getElementsAnnotatedWith(annotation).stream())
                .filter(element -> elementKinds.contains(element.getKind()))
                .map(element -> (TypeElement) element)
                .forEach(typeElement -> {
                    List<Type<?>> typeList = typeElement.getEnclosedElements().stream()
                        .filter(element -> element.getKind().equals(ElementKind.METHOD))
                        .map(element -> (ExecutableElement) element)
                        .map(method -> {
                            try {
                                return analyser.analyse(method.getReturnType());
                            }
                            catch (UnsupportedTypeException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());
                    types.put(typeElement.getQualifiedName().toString(), typeList);
                });
            return false;
        }

        public Map<String, List<Type<?>>> getTypes() {
            return types;
        }
    }
}
