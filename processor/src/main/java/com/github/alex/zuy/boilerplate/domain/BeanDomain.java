package com.github.alex.zuy.boilerplate.domain;

import java.util.Set;

public class BeanDomain {

    private Set<BeanClass> beanClasses;

    public BeanDomain(Set<BeanClass> beanClasses) {
        this.beanClasses = beanClasses;
    }

    public Set<BeanClass> getBeanClasses() {
        return beanClasses;
    }
}
