package org.alex.zuy.boilerplate.collector;

import static org.alex.zuy.boilerplate.collector.ElementTestUtils.makeElementOfKind;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.utils.CollectionUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class TypeAnnotatedClassesCollectorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private RoundEnvironment roundEnvironment;

    private TypeAnnotatedClassesCollector collector;

    private Set<TypeElement> collectedClasses;

    @Before
    public void setUp() {
        collector = new TypeAnnotatedClassesCollector();
    }

    @Test
    public void testOnlyClassesAnnotatedWithMarkerAnnotationCollected() throws Exception {
        givenDifferentElementsAnnotatedWithMarkerAnnotation();
        whenClassesCollected();
        thenOnlySingleClassCollected();
    }

    private void givenDifferentElementsAnnotatedWithMarkerAnnotation() {
        Set<Element> annotatedElements = new HashSet<>(Arrays.asList(
            makeElementOfKind(TypeElement.class, ElementKind.CLASS),
            makeElementOfKind(TypeElement.class, ElementKind.INTERFACE),
            makeElementOfKind(ExecutableElement.class, ElementKind.METHOD)));

        doReturn(annotatedElements).when(roundEnvironment).getElementsAnnotatedWith(Marker.class);
    }

    private void whenClassesCollected() {
        collectedClasses = collector.collect(Marker.class.getName(), roundEnvironment);
    }

    private void thenOnlySingleClassCollected() {
        assertThat(collectedClasses, hasSize(1));
        assertEquals(ElementKind.CLASS, CollectionUtils.getFirst(collectedClasses).getKind());
    }

    public @interface Marker {

    }
}
