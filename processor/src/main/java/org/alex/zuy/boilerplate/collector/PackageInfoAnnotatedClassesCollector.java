
package org.alex.zuy.boilerplate.collector;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
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
                .map(element -> getPackageClasses((PackageElement) element))
                .reduce(new HashSet<>(), (result, set) -> {
                    result.addAll(set);
                    return result;
                });
        }
        catch (ClassNotFoundException e) {
            throw new DomainClassesCollectorException(e);
        }
    }

    private Set<TypeElement> getPackageClasses(PackageElement packageElement) {
        return packageElement.getEnclosedElements().stream()
            .filter(element -> ElementKind.CLASS.equals(element.getKind()))
            .map(element -> (TypeElement) element)
            .collect(Collectors.toSet());
    }
}
