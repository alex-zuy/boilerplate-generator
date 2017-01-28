package org.alex.zuy.boilerplate.processor;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Optional;

import org.alex.zuy.boilerplate.domain.BeanClass;
import org.alex.zuy.boilerplate.domain.BeanDomain;
import org.alex.zuy.boilerplate.domain.BeanProperty;
import org.alex.zuy.boilerplate.domain.BeanProperty.AccessModifier;
import org.alex.zuy.boilerplate.domain.types.Type;
import org.alex.zuy.boilerplate.domain.types.Types;
import org.alex.zuy.boilerplate.metadatageneration.ImmutableSupportClassesConfig;
import org.alex.zuy.boilerplate.metadatageneration.SupportClassesGenerator;
import org.alex.zuy.boilerplate.metadatageneration.SupportClassesGeneratorImpl;
import org.alex.zuy.boilerplate.sourcemodel.FieldDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.TypeDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.TypeSetDeclaration;
import org.alex.zuy.boilerplate.utils.CollectionsUtil;
import org.junit.Test;

public class BeanDomainProcessorImplTest {

    private static final String EXAMPLE_PACKAGE_NAME = "com.example";

    private interface PropertyNames {

        String WIDTH = "width";

        String NAME = "name";
    }

    private interface TestTypes {

        Type<?> INT = Types.makeExactType("int");

        Type<?> STRING = Types.makeExactType("java.lang.String", "java.lang");
    }

    @Test
    public void testSingletonDomain() throws Exception {
        BeanClass beanClass = givenBeanClassWithStringAndIntProperties("com.example.SingletonDomain");

        TypeSetDeclaration typeSetDeclaration = whenBeanDomainProcessed(beanClass);

        thenTypeSetShouldContainCountOfTypeDefinitions(typeSetDeclaration, 2);

        TypeDeclaration typeDeclaration = getTypeBySimpleName(typeSetDeclaration, "SingletonDomainProperties");
        thenTypeShouldContainCountOfFields(typeDeclaration, 2);
        thenTypeShouldHaveQualifiedName(typeDeclaration, "com.example.SingletonDomainProperties");
        thenTypeShouldHaveSimpleNameAndPackageName(typeDeclaration, "SingletonDomainProperties", EXAMPLE_PACKAGE_NAME);
        thenShouldHaveFieldsNamed(typeDeclaration, PropertyNames.NAME.toUpperCase(), PropertyNames.WIDTH.toUpperCase());
        thenFieldsShouldHaveType(typeDeclaration, TestTypes.STRING);
    }

    private void thenTypeShouldHaveSimpleNameAndPackageName(TypeDeclaration typeDeclaration, String expectedSimpleName,
        String expectedPackageName) {
        assertEquals(expectedSimpleName, typeDeclaration.getSimpleName());
        assertEquals(expectedPackageName, typeDeclaration.getPackageName());
    }

    private void thenTypeShouldHaveQualifiedName(TypeDeclaration typeDeclaration, String expectedQualifiedName) {
        assertEquals(expectedQualifiedName, typeDeclaration.getQualifiedName());
    }

    private BeanClass givenBeanClassWithStringAndIntProperties(String beanClassName) {
        BeanProperty widthProperty = makePublicProperty(PropertyNames.WIDTH, TestTypes.INT);
        BeanProperty nameProperty = makePublicProperty(PropertyNames.NAME, TestTypes.STRING);
        return new BeanClass(Types.makeExactType(beanClassName, EXAMPLE_PACKAGE_NAME),
            Arrays.asList(widthProperty, nameProperty));
    }

    private TypeSetDeclaration whenBeanDomainProcessed(BeanClass... beanClass) {
        BeanMetadataNamesGenerator namesGenerator = new BeanMetadataNamesGeneratorImpl();
        SupportClassesGenerator supportClassesGenerator = new SupportClassesGeneratorImpl();
        BeanDomainProcessorImpl beanDomainProcessor = new BeanDomainProcessorImpl(namesGenerator,
            supportClassesGenerator);
        SupportClassesGenerator.SupportClassesConfig supportClassesConfig = ImmutableSupportClassesConfig.builder()
            .basePackage(EXAMPLE_PACKAGE_NAME)
            .build();
        return beanDomainProcessor.processDomain(new BeanDomain(CollectionsUtil.asSet(beanClass)),
            supportClassesConfig);
    }

    private void thenTypeSetShouldContainCountOfTypeDefinitions(TypeSetDeclaration typeSetDeclaration, int count) {
        assertThat(typeSetDeclaration.getTypes(), hasSize(count));
    }

    private void thenTypeShouldContainCountOfFields(TypeDeclaration declaration, int count) {
        assertThat(declaration.getFields(), hasSize(count));
    }

    private void thenShouldHaveFieldsNamed(TypeDeclaration typeDeclaration, String... names) {
        for (final String name : names) {
            Optional<FieldDeclaration> field = typeDeclaration.getFields().stream()
                .filter(fieldDeclaration -> name.equals(fieldDeclaration.getName()))
                .findFirst();
            assertTrue(field.isPresent());
        }
    }

    private void thenFieldsShouldHaveType(TypeDeclaration typeDeclaration, Type<?> type) {
        typeDeclaration.getFields().forEach(field -> assertEquals(field.getType(), type));
    }

    private static BeanProperty makePublicProperty(String name, Type<?> type) {
        return new BeanProperty(name, type, AccessModifier.PUBLIC);
    }

    private static TypeDeclaration getTypeBySimpleName(TypeSetDeclaration typeSetDeclaration, String simpleName) {
        return typeSetDeclaration.getTypes().stream()
            .filter(type -> simpleName.equals(type.getSimpleName()))
            .findFirst()
            .orElseThrow(IllegalStateException::new);
    }
}
