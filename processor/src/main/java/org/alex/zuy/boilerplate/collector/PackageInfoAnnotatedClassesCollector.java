
package org.alex.zuy.boilerplate.collector;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.services.ProcessorContext;

public class PackageInfoAnnotatedClassesCollector {

    private ProcessorContext processorContext;

    private Set<ElementKind> supportedElementKinds;

    public PackageInfoAnnotatedClassesCollector(ProcessorContext processorContext, Set<ElementKind> elementKinds) {
        this.processorContext = processorContext;
        this.supportedElementKinds = elementKinds;
    }

    public Set<TypeElement> collect(String annotationName, RoundEnvironment environment) {
        TypeElement annotationElement = processorContext.getElementUtils().getTypeElement(annotationName);
        if (annotationElement != null) {
            return environment.getElementsAnnotatedWith(annotationElement).stream()
                .filter(element -> ElementKind.PACKAGE.equals(element.getKind()))
                .flatMap(element -> getPackageClasses((PackageElement) element))
                .collect(Collectors.toSet());
        }
        else {
            throw new DomainClassesCollectorException(
                String.format("Annotation '%s' does not exists.", annotationName));
        }
    }

    private Stream<TypeElement> getPackageClasses(PackageElement packageElement) {
        return packageElement.getEnclosedElements().stream()
            .filter(element -> supportedElementKinds.contains(element.getKind()))
            .map(element -> (TypeElement) element);
    }
}
