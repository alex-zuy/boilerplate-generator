
package org.alex.zuy.boilerplate.collector;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

public class DomainClassesCollector {

    private final DomainConfig config;

    private final BasePackageClassesCollector basePackageCollector;

    private final TypeAnnotatedClassesCollector typeAnnotationCollector;

    private final PackageInfoAnnotatedClassesCollector packageInfoAnnotationCollector;

    private final List<? extends ClassFilter> classFilters;

    private CollectorComponentsFactory factory;

    public DomainClassesCollector(DomainConfig config, CollectorComponentsFactory factory) {
        this.config = config;
        this.factory = factory;
        this.basePackageCollector = factory.makeBasePackageClassesCollector();
        this.typeAnnotationCollector = factory.makeTypeAnnotatedClassesCollector();
        this.packageInfoAnnotationCollector = factory.makePackageInfoAnnotatedClassesCollector();
        this.classFilters = initializeClassFilters(config);
    }

    private List<? extends ClassFilter> initializeClassFilters(DomainConfig domainConfig) {
        return Stream.concat(
            domainConfig.excludes().typeAnnotations().stream()
                .map(factory::makeTypeAnnotatedClassFilter),
            domainConfig.excludes().patterns().stream()
                .map(factory::makeFullyQualifiedNamePatternClassFilter)
        ).collect(Collectors.toList());
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
        ).flatMap(Function.identity())
            .filter(typeElement -> classFilters.stream().allMatch(filter -> filter.filter(typeElement)))
            .collect(Collectors.toSet());
    }
}
