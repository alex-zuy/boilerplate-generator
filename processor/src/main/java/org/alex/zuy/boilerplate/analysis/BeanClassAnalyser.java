package org.alex.zuy.boilerplate.analysis;

import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.domain.BeanClass;

public interface BeanClassAnalyser {

    BeanClass analyse(TypeElement classElement);

}
