package org.alex.zuy.boilerplate.metadatageneration;

import javax.inject.Singleton;

import dagger.Component;
import org.alex.zuy.boilerplate.application.MetadataGenerationModule;
import org.alex.zuy.boilerplate.support.ProcessorContextProviderModule;

@Component(modules = {MetadataGenerationModule.class, ProcessorContextProviderModule.class})
@Singleton
public interface MetadataGenerationComponent {

    BeanDomainMetadataGenerator metadataGenerator();
}
