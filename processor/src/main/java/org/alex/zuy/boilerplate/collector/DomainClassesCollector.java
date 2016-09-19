
package org.alex.zuy.boilerplate.collector;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.services.ProcessorContext;

public class DomainClassesCollector {

    private final DomainConfig config;

    private final BasePackageClassesCollector basePackageCollector;

    private final TypeAnnotatedClassesCollector typeAnnotationCollector;

    private final PackageInfoAnnotatedClassesCollector packageInfoAnnotationCollector;

    public DomainClassesCollector(DomainConfig config, ProcessorContext context) {
        this.config = config;
        this.basePackageCollector = new BasePackageClassesCollector(context);
        this.typeAnnotationCollector = new TypeAnnotatedClassesCollector();
        this.packageInfoAnnotationCollector = new PackageInfoAnnotatedClassesCollector();
    }

    public Set<TypeElement> collect(RoundEnvironment environment) {
        return Stream.of(
            config.includes().basePackages().stream()
                .flatMap(basePackage -> basePackageCollector.collect(basePackage).stream()),
            config.includes().packageInfoAnnotations().stream()
                .flatMap(annotationName ->
                    packageInfoAnnotationCollector.collect(annotationName, environment).stream()),
            config.includes().typeAnnotations().stream()
                .flatMap(annotationName ->
                    typeAnnotationCollector.collect(annotationName, environment).stream())
        ).flatMap(Function.identity()).collect(Collectors.toSet());
    }
}
