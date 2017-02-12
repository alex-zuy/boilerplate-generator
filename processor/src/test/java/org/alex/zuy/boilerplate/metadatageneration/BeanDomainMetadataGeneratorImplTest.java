package org.alex.zuy.boilerplate.metadatageneration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import com.example.Marker;
import org.alex.zuy.boilerplate.application.BeanDomainAnalysisModule;
import org.alex.zuy.boilerplate.application.BeanDomainProcessingModule;
import org.alex.zuy.boilerplate.application.CodeGenerationModule;
import org.alex.zuy.boilerplate.application.DomainClassesCollectorModule;
import org.alex.zuy.boilerplate.application.MetadataGenerationModule;
import org.alex.zuy.boilerplate.config.DomainConfig;
import org.alex.zuy.boilerplate.config.ImmutableDomainConfig;
import org.alex.zuy.boilerplate.config.ImmutableExcludesConfig;
import org.alex.zuy.boilerplate.config.ImmutableIncludesConfig;
import org.alex.zuy.boilerplate.config.ImmutableMetadataGenerationStyle;
import org.alex.zuy.boilerplate.config.ImmutableSupportClassesConfig;
import org.alex.zuy.boilerplate.config.MetadataGenerationStyle;
import org.alex.zuy.boilerplate.config.SupportClassesConfig;
import org.alex.zuy.boilerplate.services.ImmutableRoundContext;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.services.RoundContext;
import org.alex.zuy.boilerplate.support.AnnotationProcessorBase;
import org.alex.zuy.boilerplate.support.ProcessorContextProviderModule;
import org.alex.zuy.boilerplate.support.SingleProcessingRoundAnnotationProcessorWrapper;
import org.alex.zuy.boilerplate.support.TestBuildSetupBuilder;
import org.junit.Test;

public class BeanDomainMetadataGeneratorImplTest {

    private TestBuildSetupBuilder testBuildSetupBuilder;

    @Test
    public void testSingleBeanDomain() throws Exception {
        givenSourceFilesFromTestSpecificSubdirectory("singleBeanDomain");
        whenCompilationPerformed();
        Class<?> personProperties = whenCompiledClassLoaded("com.example.PersonProperties");
        thenClassShouldHavePublicStaticFinalField(personProperties, "WEIGHT", "weight");
        thenClassShouldHavePublicStaticFinalField(personProperties, "HEIGHT", "height");
        thenClassShouldHavePublicStaticFinalField(personProperties, "FULL_NAME", "fullName");
    }

    @Test
    public void testTwoBeansDomain() throws Exception {
        givenSourceFilesFromTestSpecificSubdirectory("twoBeansDomain");
        whenCompilationPerformed();
        Class<?> personProperties = whenCompiledClassLoaded("com.example.PersonProperties");
        thenClassShouldHavePublicStaticFinalField(personProperties, "FULL_NAME", "fullName");
        thenClassShouldHavePublicStaticFinalField(personProperties, "ADDRESS", "address");
        thenEvaluatingMethodChainShouldYield(personProperties, Arrays.asList("address", "streetProperty"),
            "address.street");
    }

    private void givenSourceFilesFromTestSpecificSubdirectory(String subdirectory) throws IOException {
        Processor processor = new ProcessorImpl();
        testBuildSetupBuilder = TestBuildSetupBuilder.newInstance()
            .addTestSpecificSources(getClass(), subdirectory)
            .addAnnotationProcessor(SingleProcessingRoundAnnotationProcessorWrapper.newInstance(processor));
    }

    private void whenCompilationPerformed() throws Exception {
        testBuildSetupBuilder.createCompileTask(null).call();
    }

    private Class<?> whenCompiledClassLoaded(String className) throws ClassNotFoundException {
        return testBuildSetupBuilder.getCompiledClass(className);
    }

    private void thenClassShouldHavePublicStaticFinalField(Class<?> propertiesClass, String fieldName,
        String fieldValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = propertiesClass.getField(fieldName);
        int modifiers = field.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertTrue(Modifier.isStatic(modifiers));
        assertTrue(Modifier.isFinal(modifiers));
        assertEquals(fieldValue, field.get(null));
    }

    private void thenEvaluatingMethodChainShouldYield(Class<?> propertiesClass, List<String> methodNamesChain,
        Object expectedValue) throws ReflectiveOperationException {
        MethodChainEvaluator evaluator = MethodChainEvaluator.newInstance(propertiesClass, null);
        for (String methodName : methodNamesChain) {
            evaluator = evaluator.evaluate(methodName);
        }
        assertEquals(expectedValue, evaluator.getValue());
    }

    private static class ProcessorImpl extends AnnotationProcessorBase {

        private BeanDomainMetadataGenerator beanDomainMetadataGenerator;

        @Override
        protected void afterInit(ProcessingEnvironment processingEnvironment, ProcessorContext processorContext) {
            super.afterInit(processingEnvironment, processorContext);

            MetadataGenerationComponent generationComponent = DaggerMetadataGenerationComponent.builder()
                .processorContextProviderModule(new ProcessorContextProviderModule(processorContext))
                .beanDomainAnalysisModule(new BeanDomainAnalysisModule())
                .beanDomainProcessingModule(new BeanDomainProcessingModule())
                .domainClassesCollectorModule(new DomainClassesCollectorModule())
                .codeGenerationModule(new CodeGenerationModule())
                .metadataGenerationModule(new MetadataGenerationModule())
                .build();

            beanDomainMetadataGenerator = generationComponent.metadataGenerator();
        }

        @Override
        public boolean processImpl(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
            RoundContext roundContext = ImmutableRoundContext.builder()
                .roundEnvironment(roundEnvironment)
                .annotations(set)
                .build();
            MetadataGenerationStyle generationStyle = ImmutableMetadataGenerationStyle.builder()
                .propertyClassNameTemplate("${beanClassName}Properties")
                .relationshipsClassNameTemplate("${beanClassName}Relationships")
                .relationshipsClassTerminalMethodNameTemplate("${beanPropertyName}Property")
                .stringConstantStyle(MetadataGenerationStyle.StringConstantStyle.UPPERCASE)
                .build();
            DomainConfig.IncludesConfig includesConfig = ImmutableIncludesConfig.builder()
                .addTypeAnnotations(Marker.class.getName())
                .build();
            DomainConfig.ExcludesConfig excludesConfig = ImmutableExcludesConfig.builder()
                .build();
            DomainConfig domainConfig = ImmutableDomainConfig.builder()
                .includes(includesConfig)
                .excludes(excludesConfig)
                .generationStyle(generationStyle)
                .build();

            SupportClassesConfig supportClassesConfig = ImmutableSupportClassesConfig.builder()
                .basePackage("com.example").build();
            beanDomainMetadataGenerator.generateDomainMetadataClasses(roundContext, domainConfig,
                supportClassesConfig);

            return false;
        }

        @Override
        public Set<String> getSupportedAnnotationTypes() {
            return Collections.singleton(Marker.class.getName());
        }
    }

    private static class MethodChainEvaluator {

        private Class<?> clazz;

        private Object instance;

        private MethodChainEvaluator(Class<?> clazz, Object instance) {
            this.clazz = clazz;
            this.instance = instance;
        }

        public MethodChainEvaluator evaluate(String methodName)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method method = clazz.getMethod(methodName);
            Object resultInstance = method.invoke(instance);
            return new MethodChainEvaluator(method.getReturnType(), resultInstance);
        }

        public Object getValue() {
            return instance;
        }

        public static MethodChainEvaluator newInstance(Class<?> clazz, Object clazzInstance) {
            return new MethodChainEvaluator(clazz, clazzInstance);
        }
    }
}
