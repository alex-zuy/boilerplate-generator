package org.alex.zuy.boilerplate.collector.filters;

import java.util.regex.Pattern;
import javax.lang.model.element.TypeElement;

public class FullyQualifiedNamePatternClassFilter implements ClassFilter {

    private final Pattern classNamePattern;

    public FullyQualifiedNamePatternClassFilter(Pattern classNamePattern) {
        this.classNamePattern = classNamePattern;
    }

    @Override
    public boolean filter(TypeElement typeElement) {
        return !classNamePattern.matcher(typeElement.getQualifiedName()).matches();
    }
}
