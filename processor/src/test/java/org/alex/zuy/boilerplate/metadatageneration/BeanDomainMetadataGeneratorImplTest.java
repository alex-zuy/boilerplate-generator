package org.alex.zuy.boilerplate.metadatageneration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
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
import org.alex.zuy.boilerplate.collector.DomainConfig;
import org.alex.zuy.boilerplate.collector.ImmutableDomainConfig;
import org.alex.zuy.boilerplate.collector.ImmutableExcludesConfig;
import org.alex.zuy.boilerplate.collector.ImmutableIncludesConfig;
import org.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGenerator.MetadataGenerationStyle;
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

    private void thenClassShouldHavePublicStaticFinalField(Class<?> personProperties, String fieldName,
        String fieldValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = personProperties.getField(fieldName);
        int modifiers = field.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertTrue(Modifier.isStatic(modifiers));
        assertTrue(Modifier.isFinal(modifiers));
        assertEquals(fieldValue, field.get(null));
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
            DomainConfig.IncludesConfig includesConfig = ImmutableIncludesConfig.builder()
                .addTypeAnnotations(Marker.class.getName())
                .build();
            DomainConfig.ExcludesConfig excludesConfig = ImmutableExcludesConfig.builder()
                .build();
            DomainConfig domainConfig = ImmutableDomainConfig.builder()
                .includes(includesConfig)
                .excludes(excludesConfig)
                .build();

            //TODO: options are not set because it`s support is not implemented
            MetadataGenerationStyle generationStyle = ImmutableMetadataGenerationStyle.builder()
                .stringConstantStyle(MetadataGenerationStyle.StringConstantStyle.UPPERCASE)
                .propertyClassNameTemplate("")
                .relationshipsClassNameTemplate("")
                .build();

            beanDomainMetadataGenerator.generateDomainMetadataClasses(roundContext, domainConfig, generationStyle);

            return false;
        }

        @Override
        public Set<String> getSupportedAnnotationTypes() {
            return Collections.singleton(Marker.class.getName());
        }
    }
}
