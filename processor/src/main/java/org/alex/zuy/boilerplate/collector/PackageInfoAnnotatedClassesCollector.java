
package org.alex.zuy.boilerplate.collector;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

public class PackageInfoAnnotatedClassesCollector {

    public Set<TypeElement> collect(String annotationName, RoundEnvironment environment) {
        try {
            final Class<? extends Annotation> annotationClass =
                Class.forName(annotationName).asSubclass(Annotation.class);
            return environment.getElementsAnnotatedWith(annotationClass).stream()
                .filter(element -> ElementKind.PACKAGE.equals(element.getKind()))
                .flatMap(element -> getPackageClasses((PackageElement) element))
                .collect(Collectors.toSet());
        }
        catch (ClassNotFoundException e) {
            throw new DomainClassesCollector.DomainClassesCollectorException(e);
        }
    }

    private Stream<TypeElement> getPackageClasses(PackageElement packageElement) {
        return packageElement.getEnclosedElements().stream()
            .filter(element -> ElementKind.CLASS.equals(element.getKind()))
            .map(element -> (TypeElement) element);
    }
}
