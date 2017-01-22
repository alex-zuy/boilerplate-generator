package org.alex.zuy.boilerplate.analysis;

import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.element.TypeElement;

import org.alex.zuy.boilerplate.domain.BeanClass;
import org.alex.zuy.boilerplate.domain.BeanDomain;

public class BeanDomainAnalyserImpl implements BeanDomainAnalyser {

    private BeanClassAnalyser classAnalyser;

    public BeanDomainAnalyserImpl(BeanClassAnalyser classAnalyser) {
        this.classAnalyser = classAnalyser;
    }

    @Override
    public BeanDomain analyse(Set<TypeElement> domainTypeElements) {
        Set<BeanClass> beanClasses = domainTypeElements.stream()
            .map(classAnalyser::analyse)
            .collect(Collectors.toSet());
        return new BeanDomain(beanClasses);
    }
}
