package com.github.alex.zuy.boilerplate.analysis;

import javax.lang.model.element.TypeElement;

import com.github.alex.zuy.boilerplate.domain.BeanClass;

public interface BeanClassAnalyser {

    BeanClass analyse(TypeElement classElement);

}
