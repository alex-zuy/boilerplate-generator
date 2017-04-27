package com.github.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import com.github.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGenerator;
import com.github.alex.zuy.boilerplate.services.ProcessorContext;
import dagger.Component;

@Component(modules = {MetadataGenerationModule.class, ProcessorContextProviderModule.class})
@Singleton
public interface ProcessorComponent {

    BeanDomainMetadataGenerator getMetadataGenerator();

    ProcessorContext getProcessorContext();
}
