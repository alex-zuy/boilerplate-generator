package org.alex.zuy.boilerplate.collector;

import javax.lang.model.element.TypeElement;

public interface ClassFilter {

    boolean filter(TypeElement typeElement);
}
