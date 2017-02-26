package org.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import dagger.Component;
import org.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGenerator;
import org.alex.zuy.boilerplate.services.ProcessorContext;

@Component(modules = {MetadataGenerationModule.class, ProcessorContextProviderModule.class})
@Singleton
public interface ProcessorComponent {

    BeanDomainMetadataGenerator getMetadataGenerator();

    ProcessorContext getProcessorContext();
}
