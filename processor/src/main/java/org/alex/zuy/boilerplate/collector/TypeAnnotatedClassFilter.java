package org.alex.zuy.boilerplate.collector;

import java.lang.annotation.Annotation;
import javax.lang.model.element.TypeElement;

public class TypeAnnotatedClassFilter implements ClassFilter {

    private final Class<? extends Annotation> annotation;

    public TypeAnnotatedClassFilter(String annotation) {
        try {
            this.annotation = Class.forName(annotation).asSubclass(Annotation.class);
        }
        catch (ClassNotFoundException e) {
            throw new DomainClassesCollectorException(e);
        }
    }

    @Override
    public boolean filter(TypeElement typeElement) {
        return typeElement.getAnnotation(annotation) == null;
    }
}
