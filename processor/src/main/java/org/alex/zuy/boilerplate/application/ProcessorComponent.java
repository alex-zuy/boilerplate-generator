package org.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import dagger.Component;
import org.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGenerator;

@Component(modules = {MetadataGenerationModule.class, ProcessorContextProviderModule.class})
@Singleton
public interface ProcessorComponent {

    BeanDomainMetadataGenerator getMetadataGenerator();
}
