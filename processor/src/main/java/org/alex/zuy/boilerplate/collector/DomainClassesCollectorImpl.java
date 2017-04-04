
package org.alex.zuy.boilerplate.collector;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.RoundEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.collector.filters.ClassFilter;
import org.alex.zuy.boilerplate.collector.filters.FullyQualifiedNamePatternClassFilter;
import org.alex.zuy.boilerplate.collector.filters.TypeAnnotatedClassFilter;
import org.alex.zuy.boilerplate.config.DomainConfig;
import org.alex.zuy.boilerplate.services.ProcessorContext;

public class DomainClassesCollectorImpl implements DomainClassesCollector {

    private static final Set<ElementKind> ELEMENT_KINDS = Collections.unmodifiableSet(
        EnumSet.of(ElementKind.CLASS, ElementKind.INTERFACE));

    private ProcessorContext processorContext;

    private final BasePackageClassesCollector basePackageCollector;

    private final TypeAnnotatedClassesCollector typeAnnotationCollector;

    private final PackageInfoAnnotatedClassesCollector packageInfoAnnotationCollector;

    @Inject
    public DomainClassesCollectorImpl(ProcessorContext processorContext) {
        this.basePackageCollector = new BasePackageClassesCollector(processorContext, ELEMENT_KINDS);
        this.typeAnnotationCollector = new TypeAnnotatedClassesCollector(processorContext, ELEMENT_KINDS);
        this.packageInfoAnnotationCollector = new PackageInfoAnnotatedClassesCollector(processorContext, ELEMENT_KINDS);
        this.processorContext = processorContext;
    }

    @Override
    public Set<TypeElement> collect(DomainConfig domainConfig, RoundEnvironment environment) {
        List<ClassFilter> classFilters = instantiateClassFilters(domainConfig);
        return Stream.of(
            domainConfig.includes().basePackages().stream()
                .flatMap(basePackage -> basePackageCollector.collect(basePackage).stream()),
            domainConfig.includes().packageInfoAnnotations().stream()
                .flatMap(annotationName ->
                    packageInfoAnnotationCollector.collect(annotationName, environment).stream()),
            domainConfig.includes().typeAnnotations().stream()
                .flatMap(annotationName ->
                    typeAnnotationCollector.collect(annotationName, environment).stream())
        ).flatMap(Function.identity())
            .filter(typeElement -> classFilters.stream().allMatch(filter -> filter.filter(typeElement)))
            .collect(Collectors.toSet());
    }

    private List<ClassFilter> instantiateClassFilters(DomainConfig domainConfig) {
        return Stream.concat(
            domainConfig.excludes().typeAnnotations().stream()
                .map(annotation -> new TypeAnnotatedClassFilter(processorContext, annotation)),
            domainConfig.excludes().patterns().stream()
                .map(FullyQualifiedNamePatternClassFilter::new)
        ).collect(Collectors.toList());
    }
}
