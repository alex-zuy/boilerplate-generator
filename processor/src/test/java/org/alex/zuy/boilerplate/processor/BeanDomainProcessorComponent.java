package org.alex.zuy.boilerplate.processor;

import javax.inject.Singleton;

import dagger.Component;
import org.alex.zuy.boilerplate.application.BeanDomainProcessingModule;

@Component(modules = BeanDomainProcessingModule.class)
@Singleton
public interface BeanDomainProcessorComponent {

    BeanDomainProcessor getBeanDomainProcessor();
}
