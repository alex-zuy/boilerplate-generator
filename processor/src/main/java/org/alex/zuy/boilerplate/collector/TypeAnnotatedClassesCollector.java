
package org.alex.zuy.boilerplate.collector;

import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.services.ProcessorContext;

public class TypeAnnotatedClassesCollector {

    private ProcessorContext processorContext;

    public TypeAnnotatedClassesCollector(ProcessorContext processorContext) {
        this.processorContext = processorContext;
    }

    public Set<TypeElement> collect(String annotationName, RoundEnvironment environment) {
        TypeElement annotationElement = processorContext.getElementUtils().getTypeElement(annotationName);
        if (annotationElement != null) {
            return environment.getElementsAnnotatedWith(annotationElement).stream()
                .filter(element -> ElementKind.CLASS.equals(element.getKind()))
                .map(element -> (TypeElement) element)
                .collect(Collectors.toSet());
        }
        else {
            throw new DomainClassesCollectorException(
                String.format("Annotation '%s' does not exists.", annotationName));
        }
    }
}
