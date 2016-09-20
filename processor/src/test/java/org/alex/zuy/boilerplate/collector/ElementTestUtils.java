package org.alex.zuy.boilerplate.collector;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

public class ElementTestUtils {

    public static <T extends Element> T makeElementOfKind(Class<T> clazz, ElementKind kind) {
        T elementMock = mock(clazz);
        when(elementMock.getKind()).thenReturn(kind);
        return elementMock;
    }
}
