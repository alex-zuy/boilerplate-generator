package org.alex.zuy.boilerplate.collector.filters;

import javax.lang.model.element.TypeElement;

public interface ClassFilter {

    boolean filter(TypeElement typeElement);
}
