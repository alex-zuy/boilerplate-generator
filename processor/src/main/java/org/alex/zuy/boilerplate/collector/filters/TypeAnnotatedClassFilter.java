package org.alex.zuy.boilerplate.collector.filters;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.collector.DomainClassesCollectorException;
import org.alex.zuy.boilerplate.services.ProcessorContext;

public class TypeAnnotatedClassFilter implements ClassFilter {

    private final TypeElement annotationElement;

    private ProcessorContext processorContext;

    public TypeAnnotatedClassFilter(ProcessorContext processorContext, String annotationName) {
        this.processorContext = processorContext;
        TypeElement annotation = processorContext.getElementUtils().getTypeElement(annotationName);
        if (annotation != null) {
            annotationElement = annotation;
        }
        else {
            throw new DomainClassesCollectorException(
                String.format("Annotation '%s' does not exists.", annotationName));
        }
    }

    @Override
    public boolean filter(TypeElement typeElement) {
        return typeElement.getAnnotationMirrors().stream()
            .noneMatch(annotationMirror -> isSameAnnotation(annotationMirror, annotationElement));
    }

    private boolean isSameAnnotation(AnnotationMirror mirror, TypeElement element) {
        return processorContext.getTypeUtils().isSameType(mirror.getAnnotationType(), element.asType());
    }
}
