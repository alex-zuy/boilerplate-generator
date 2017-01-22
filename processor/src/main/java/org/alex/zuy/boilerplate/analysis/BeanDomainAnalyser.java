package org.alex.zuy.boilerplate.analysis;

import java.util.Set;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.domain.BeanDomain;

public interface BeanDomainAnalyser {

    BeanDomain analyse(Set<TypeElement> domainTypeElements);
}
