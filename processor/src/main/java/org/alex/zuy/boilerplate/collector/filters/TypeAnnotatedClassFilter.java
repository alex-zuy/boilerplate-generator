package org.alex.zuy.boilerplate.collector.filters;

import java.lang.annotation.Annotation;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.collector.DomainClassesCollectorException;

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
