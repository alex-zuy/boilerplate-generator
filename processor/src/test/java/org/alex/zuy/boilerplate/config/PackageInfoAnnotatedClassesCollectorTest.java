package org.alex.zuy.boilerplate.config;

import static org.alex.zuy.boilerplate.config.ElementTestUtils.makeElementOfKind;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.collector.PackageInfoAnnotatedClassesCollector;
import org.alex.zuy.boilerplate.utils.CollectionUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class PackageInfoAnnotatedClassesCollectorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private RoundEnvironment roundEnvironment;

    private PackageInfoAnnotatedClassesCollector collector;

    private Set<TypeElement> collectedClasses;

    @Before
    public void setUp() {
        collector = new PackageInfoAnnotatedClassesCollector();
    }

    @Test
    public void testOnlyClassesInPackagesAnnotatedWithMarkerCollected() throws Exception {
        givenPackageAnnotatedWithMarkerAnnotation();
        whenClassesCollected();
        thenOnlySingleClassMustBeCollected();
    }

    private void givenPackageAnnotatedWithMarkerAnnotation() {
        List<Element> packageEnclosedElements = Arrays.asList(
            makeElementOfKind(TypeElement.class, ElementKind.CLASS),
            makeElementOfKind(TypeElement.class, ElementKind.INTERFACE),
            makeElementOfKind(ExecutableElement.class, ElementKind.METHOD));

        PackageElement packageElement = makeElementOfKind(PackageElement.class, ElementKind.PACKAGE);
        doReturn(packageEnclosedElements).when(packageElement).getEnclosedElements();

        doReturn(Collections.singleton(packageElement)).when(roundEnvironment).getElementsAnnotatedWith(Marker.class);
    }

    private void whenClassesCollected() {
        collectedClasses = collector.collect(Marker.class.getName(), roundEnvironment);
    }

    private void thenOnlySingleClassMustBeCollected() {
        assertThat(collectedClasses, hasSize(1));
        assertEquals(ElementKind.CLASS, CollectionUtils.getFirst(collectedClasses).getKind());
    }

    public @interface Marker {

    }
}
