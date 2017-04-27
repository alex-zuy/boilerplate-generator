package com.github.alex.zuy.boilerplate.processor;

import javax.inject.Singleton;

import com.github.alex.zuy.boilerplate.application.BeanDomainProcessingModule;
import dagger.Component;

@Component(modules = BeanDomainProcessingModule.class)
@Singleton
public interface BeanDomainProcessorComponent {

    BeanDomainProcessor getBeanDomainProcessor();
}
