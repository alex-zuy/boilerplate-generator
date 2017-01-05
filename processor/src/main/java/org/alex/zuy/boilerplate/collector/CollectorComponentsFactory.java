package org.alex.zuy.boilerplate.collector;

import java.util.regex.Pattern;

import org.alex.zuy.boilerplate.collector.filters.FullyQualifiedNamePatternClassFilter;
import org.alex.zuy.boilerplate.collector.filters.TypeAnnotatedClassFilter;
import org.alex.zuy.boilerplate.services.ProcessorContext;

public class CollectorComponentsFactory {

    private final ProcessorContext processorContext;

    public CollectorComponentsFactory(ProcessorContext processorContext) {
        this.processorContext = processorContext;
    }

    public BasePackageClassesCollector makeBasePackageClassesCollector() {
        return new BasePackageClassesCollector(processorContext);
    }

    public TypeAnnotatedClassesCollector makeTypeAnnotatedClassesCollector() {
        return new TypeAnnotatedClassesCollector();
    }

    public PackageInfoAnnotatedClassesCollector makePackageInfoAnnotatedClassesCollector() {
        return new PackageInfoAnnotatedClassesCollector();
    }

    public TypeAnnotatedClassFilter makeTypeAnnotatedClassFilter(String annotation) {
        return new TypeAnnotatedClassFilter(annotation);
    }

    public FullyQualifiedNamePatternClassFilter makeFullyQualifiedNamePatternClassFilter(Pattern pattern) {
        return new FullyQualifiedNamePatternClassFilter(pattern);
    }
}
