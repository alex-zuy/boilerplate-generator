package com.github.alex.zuy.boilerplate.processor;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Optional;

import com.github.alex.zuy.boilerplate.application.BeanDomainProcessingModule;
import com.github.alex.zuy.boilerplate.application.StringTemplateModule;
import com.github.alex.zuy.boilerplate.boilerplate.JdkTypes;
import com.github.alex.zuy.boilerplate.config.ImmutableMetadataGenerationStyle;
import com.github.alex.zuy.boilerplate.config.ImmutableSupportClassesConfig;
import com.github.alex.zuy.boilerplate.config.MetadataGenerationStyle;
import com.github.alex.zuy.boilerplate.config.SupportClassesConfig;
import com.github.alex.zuy.boilerplate.domain.BeanClass;
import com.github.alex.zuy.boilerplate.domain.BeanDomain;
import com.github.alex.zuy.boilerplate.domain.BeanProperty;
import com.github.alex.zuy.boilerplate.domain.BeanProperty.AccessModifier;
import com.github.alex.zuy.boilerplate.domain.QualifiedName;
import com.github.alex.zuy.boilerplate.domain.types.Type;
import com.github.alex.zuy.boilerplate.domain.types.Types;
import com.github.alex.zuy.boilerplate.sourcemodel.FieldDescription;
import com.github.alex.zuy.boilerplate.sourcemodel.TypeDescription;
import com.github.alex.zuy.boilerplate.sourcemodel.TypeSetDeclaration;
import com.github.alex.zuy.boilerplate.utils.CollectionsUtil;
import org.junit.Test;

public class BeanDomainProcessorImplTest {

    private static final String EXAMPLE_PACKAGE_NAME = "com.example";

    private interface PropertyNames {

        String WIDTH = "width";

        String NAME = "name";
    }

    @Test
    public void testSingletonDomain() throws Exception {
        BeanClass beanClass = givenBeanClassWithStringAndIntProperties("SingletonDomain");

        TypeSetDeclaration typeSetDeclaration = whenBeanDomainProcessed(beanClass);

        thenTypeSetShouldContainCountOfTypeDefinitions(typeSetDeclaration, 2);

        TypeDescription typeDeclaration = getTypeBySimpleName(typeSetDeclaration, "SingletonDomainProperties");
        thenTypeShouldContainCountOfFields(typeDeclaration, 2);
        thenTypeShouldHaveQualifiedName(typeDeclaration, "com.example.SingletonDomainProperties");
        thenTypeShouldHaveSimpleNameAndPackageName(typeDeclaration, "SingletonDomainProperties", EXAMPLE_PACKAGE_NAME);
        thenShouldHaveFieldsNamed(typeDeclaration, PropertyNames.NAME.toUpperCase(), PropertyNames.WIDTH.toUpperCase());
        thenFieldsShouldHaveType(typeDeclaration, JdkTypes.JAVA_LANG_STRING);
    }

    private void thenTypeShouldHaveSimpleNameAndPackageName(TypeDescription typeDeclaration, String expectedSimpleName,
        String expectedPackageName) {
        assertEquals(expectedSimpleName, typeDeclaration.getSimpleName());
        assertEquals(expectedPackageName, typeDeclaration.getPackageName());
    }

    private void thenTypeShouldHaveQualifiedName(TypeDescription typeDeclaration, String expectedQualifiedName) {
        assertEquals(expectedQualifiedName, typeDeclaration.getQualifiedName());
    }

    private BeanClass givenBeanClassWithStringAndIntProperties(String beanClassName) {
        BeanProperty widthProperty = makePublicProperty(PropertyNames.WIDTH, JdkTypes.PRIMITIVE_INT);
        BeanProperty nameProperty = makePublicProperty(PropertyNames.NAME, JdkTypes.JAVA_LANG_STRING);
        return new BeanClass(Types.makeExactType(new QualifiedName(beanClassName, EXAMPLE_PACKAGE_NAME)),
            Arrays.asList(widthProperty, nameProperty));
    }

    private TypeSetDeclaration whenBeanDomainProcessed(BeanClass... beanClass) {
        SupportClassesConfig supportClassesConfig = ImmutableSupportClassesConfig.builder()
            .basePackage(EXAMPLE_PACKAGE_NAME)
            .build();
        MetadataGenerationStyle generationStyle = ImmutableMetadataGenerationStyle.builder()
            .propertyClassNameTemplate("${beanClassName}Properties")
            .relationshipsClassNameTemplate("${beanClassName}Relationships")
            .relationshipsClassTerminalMethodNameTemplate("${beanPropertyName}Property")
            .stringConstantStyle(MetadataGenerationStyle.StringConstantStyle.UPPERCASE)
            .build();
        BeanDomainProcessor beanDomainProcessor = DaggerBeanDomainProcessorComponent.builder()
            .beanDomainProcessingModule(new BeanDomainProcessingModule())
            .stringTemplateModule(new StringTemplateModule())
            .build()
            .getBeanDomainProcessor();
        return beanDomainProcessor.processDomain(new BeanDomain(CollectionsUtil.asSet(beanClass)),
            supportClassesConfig, generationStyle);
    }

    private void thenTypeSetShouldContainCountOfTypeDefinitions(TypeSetDeclaration typeSetDeclaration, int count) {
        assertThat(typeSetDeclaration.getTypes(), hasSize(count));
    }

    private void thenTypeShouldContainCountOfFields(TypeDescription declaration, int count) {
        assertThat(declaration.getFields(), hasSize(count));
    }

    private void thenShouldHaveFieldsNamed(TypeDescription typeDeclaration, String... names) {
        for (final String name : names) {
            Optional<FieldDescription> field = typeDeclaration.getFields().stream()
                .filter(fieldDeclaration -> name.equals(fieldDeclaration.getName()))
                .findFirst();
            assertTrue(field.isPresent());
        }
    }

    private void thenFieldsShouldHaveType(TypeDescription typeDeclaration, Type<?> type) {
        typeDeclaration.getFields().forEach(field -> assertEquals(field.getType(), type));
    }

    private static BeanProperty makePublicProperty(String name, Type<?> type) {
        return new BeanProperty(name, type, AccessModifier.PUBLIC);
    }

    private static TypeDescription getTypeBySimpleName(TypeSetDeclaration typeSetDeclaration, String simpleName) {
        return typeSetDeclaration.getTypes().stream()
            .filter(type -> simpleName.equals(type.getSimpleName()))
            .findFirst()
            .orElseThrow(IllegalStateException::new);
    }
}
