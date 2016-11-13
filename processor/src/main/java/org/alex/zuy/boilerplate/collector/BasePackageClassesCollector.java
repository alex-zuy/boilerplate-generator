
package org.alex.zuy.boilerplate.collector;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.alex.zuy.boilerplate.services.ProcessorContext;

public class BasePackageClassesCollector {

    private final Elements elementUtils;

    public BasePackageClassesCollector(final ProcessorContext context) {
        this.elementUtils = context.getElementUtils();
    }

    public Set<TypeElement> collect(String packageName) {
        return Optional.ofNullable(elementUtils.getPackageElement(packageName))
            .map(packageElement -> packageElement.getEnclosedElements().stream()
                .filter(element -> ElementKind.CLASS.equals(element.getKind()))
                .map(element -> (TypeElement) element)
                .collect(Collectors.toSet()))
            .orElse(Collections.emptySet());
    }
}
