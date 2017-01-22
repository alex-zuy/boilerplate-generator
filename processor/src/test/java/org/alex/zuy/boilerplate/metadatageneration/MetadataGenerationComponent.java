package org.alex.zuy.boilerplate.metadatageneration;

import dagger.Component;
import org.alex.zuy.boilerplate.application.MetadataGenerationModule;
import org.alex.zuy.boilerplate.support.ProcessorContextProviderModule;

@Component(modules = {MetadataGenerationModule.class, ProcessorContextProviderModule.class})
public interface MetadataGenerationComponent {

    BeanDomainMetadataGenerator metadataGenerator();
}
