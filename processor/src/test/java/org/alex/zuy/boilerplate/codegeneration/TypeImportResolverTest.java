package org.alex.zuy.boilerplate.codegeneration;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.alex.zuy.boilerplate.domain.QualifiedName;
import org.junit.Test;

public class TypeImportResolverTest {

    public static final String PACKAGE_COM_EXAMPLE = "com.example";

    private TypeImportResolver importResolver = new TypeImportResolver();

    @Test
    public void testTypesFromPackagesImported() throws Exception {
        QualifiedName someName = givenName("SomeType", PACKAGE_COM_EXAMPLE);
        String resolved = whenNameResolved(someName);
        thenResolvedNameShouldBeSimpleName(resolved, someName);
        thenImportsListShouldBe(someName);
    }

    @Test
    public void testNameClash_TypesFromDifferentPackagesHaveSameSimpleName() throws Exception {
        QualifiedName primaryName = givenName("Type", "com.example.primary");
        QualifiedName secondaryName = givenName("Type", "com.example.secondary");
        String resolvedPrimaryName = whenNameResolved(primaryName);
        String resolverSecondaryName = whenNameResolved(secondaryName);
        thenResolvedNameShouldBeSimpleName(resolvedPrimaryName, primaryName);
        thenResolvedNameShouldBeQualifiedName(resolverSecondaryName, secondaryName);
        thenImportsListShouldBe(primaryName);
    }

    @Test
    public void testNameClash_TypesFromSamePackageHaveSameSimpleName_topLevelClassImportedFirst() throws Exception {
        QualifiedName holderClass = new QualifiedName("HolderClass", PACKAGE_COM_EXAMPLE);
        QualifiedName topLevelTypeName = givenName("Type", PACKAGE_COM_EXAMPLE);
        QualifiedName nestedTypeName = givenName("Type", PACKAGE_COM_EXAMPLE, holderClass);
        String topLevelTypeResolved = whenNameResolved(topLevelTypeName);
        String nestedTypeResolved = whenNameResolved(nestedTypeName);
        thenResolvedNameShouldBeSimpleName(topLevelTypeResolved, topLevelTypeName);
        thenResolvedNameShouldBeQualifiedName(nestedTypeResolved, nestedTypeName);
        thenImportsListShouldBe(topLevelTypeName);
    }

    @Test
    public void testNameClass_TypesFromSamePackageHaveSameSimpleName_nestedClassImportedFirst() throws Exception {
        QualifiedName holderClass = new QualifiedName("HolderClass", PACKAGE_COM_EXAMPLE);
        QualifiedName topLevelTypeName = givenName("Type", PACKAGE_COM_EXAMPLE);
        QualifiedName nestedTypeName = givenName("Type", PACKAGE_COM_EXAMPLE, holderClass);
        String nestedTypeResolved = whenNameResolved(nestedTypeName);
        String topLevelTypeResolved = whenNameResolved(topLevelTypeName);
        thenResolvedNameShouldBeSimpleName(nestedTypeResolved, nestedTypeName);
        thenResolvedNameShouldBeQualifiedName(topLevelTypeResolved, topLevelTypeName);
        thenImportsListShouldBe(nestedTypeName);
    }

    @Test
    public void testSameTypeResolvedTwice() throws Exception {
        QualifiedName name = givenName("Type", PACKAGE_COM_EXAMPLE);
        String firstResolved = whenNameResolved(name);
        String secondResolved = whenNameResolved(name);
        thenResolvedNameShouldBeSimpleName(firstResolved, name);
        thenResolvedNameShouldBeSimpleName(secondResolved, name);
        thenImportsListShouldBe(name);
    }

    @Test
    public void testTypesFromJavaLangPackageNotImported() throws Exception {
        QualifiedName stringName = givenName("String", "java.lang");
        String resolvedName = whenNameResolved(stringName);
        thenResolvedNameShouldBeSimpleName(resolvedName, stringName);
        thenImportsListShouldBe();
    }

    @Test
    public void testPrimitiveTypesNotImported() throws Exception {
        QualifiedName intName = new QualifiedName("int");
        String resolved = whenNameResolved(intName);
        thenResolvedNameShouldBeSimpleName(resolved, intName);
        thenImportsListShouldBe();
    }

    private String whenNameResolved(QualifiedName name) {
        return importResolver.resolveTypeReference(name);
    }

    private void thenImportsListShouldBe(QualifiedName... names) {
        Collection<QualifiedName> importedTypes = importResolver.getImportedTypes();
        assertEquals(new HashSet<>(Arrays.asList(names)), new HashSet<>(importedTypes));
    }

    private void thenResolvedNameShouldBeSimpleName(String resolvedTypeName, QualifiedName name) {
        assertEquals(name.getSimpleName(), resolvedTypeName);
    }

    private void thenResolvedNameShouldBeQualifiedName(String resolvedName, QualifiedName name) {
        assertEquals(name.asString(), resolvedName);
    }

    private QualifiedName givenName(String simpleName, String packageName) {
        return new QualifiedName(simpleName, packageName);
    }

    private QualifiedName givenName(String simpleName, String packageName, QualifiedName enclosingName) {
        return new QualifiedName(simpleName, packageName, enclosingName);
    }
}
