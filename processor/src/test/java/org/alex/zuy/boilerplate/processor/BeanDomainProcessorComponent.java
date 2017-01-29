package org.alex.zuy.boilerplate.processor;

import dagger.Component;
import org.alex.zuy.boilerplate.application.BeanDomainProcessingModule;

@Component(modules = BeanDomainProcessingModule.class)
public interface BeanDomainProcessorComponent {

    BeanDomainProcessor getBeanDomainProcessor();
}
