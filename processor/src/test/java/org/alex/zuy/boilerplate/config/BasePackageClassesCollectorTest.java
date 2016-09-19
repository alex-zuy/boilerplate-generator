package org.alex.zuy.boilerplate.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.alex.zuy.boilerplate.ProcessorContextImpl;
import org.alex.zuy.boilerplate.collector.BasePackageClassesCollector;
import org.alex.zuy.boilerplate.services.ProcessorContext;
import org.alex.zuy.boilerplate.utils.CollectionUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class BasePackageClassesCollectorTest {

    private static final String PACKAGE_NAME = "com.example.main";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private RoundEnvironment roundEnvironment;

    @Mock
    private Elements elementUtils;

    private BasePackageClassesCollector collector;

    private Set<TypeElement> collectedClasses;

    @Before
    public void setUp() {
        ProcessorContext processorContext = new ProcessorContextImpl(elementUtils, null, null,
            null, null);
        collector = new BasePackageClassesCollector(processorContext);
    }

    @Test
    public void testCollectorReturnsOnlyClasses() throws Exception {
        givenPackageWithDifferentTypesOfElements();
        whenPackageElementsProcessed();
        thenCollectorShouldContainOnlyClass();
    }

    private void givenPackageWithDifferentTypesOfElements() {
        List<Element> packageElements = Arrays.asList(
            makeElementMock(TypeElement.class, ElementKind.CLASS),
            makeElementMock(TypeElement.class, ElementKind.INTERFACE),
            makeElementMock(TypeElement.class, ElementKind.ENUM),
            makeElementMock(TypeElement.class, ElementKind.ANNOTATION_TYPE),
            makeElementMock(ExecutableElement.class, ElementKind.METHOD),
            makeElementMock(ExecutableElement.class, ElementKind.CONSTRUCTOR)
        );
        PackageElement packageElement = mock(PackageElement.class);
        doReturn(packageElements).when(packageElement).getEnclosedElements();
        when(elementUtils.getPackageElement(PACKAGE_NAME)).thenReturn(packageElement);
    }

    private void whenPackageElementsProcessed() {
        collectedClasses = collector.collect(PACKAGE_NAME);
    }

    private void thenCollectorShouldContainOnlyClass() {
        assertThat(collectedClasses, hasSize(1));
        assertEquals(ElementKind.CLASS, CollectionUtils.getFirst(collectedClasses).getKind());
    }

    private static <T extends Element> T makeElementMock(Class<T> clazz, ElementKind kind) {
        T elementMock = mock(clazz);
        when(elementMock.getKind()).thenReturn(kind);
        return elementMock;
    }
}
