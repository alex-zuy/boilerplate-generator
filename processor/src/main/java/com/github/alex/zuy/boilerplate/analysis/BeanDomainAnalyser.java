package com.github.alex.zuy.boilerplate.analysis;

import java.util.Set;
import javax.lang.model.element.TypeElement;

import com.github.alex.zuy.boilerplate.domain.BeanDomain;

public interface BeanDomainAnalyser {

    BeanDomain analyse(Set<TypeElement> domainTypeElements);
}
