package com.github.alex.zuy.boilerplate.metadatageneration;

import javax.inject.Singleton;

import com.github.alex.zuy.boilerplate.application.MetadataGenerationModule;
import com.github.alex.zuy.boilerplate.support.ProcessorContextProviderModule;
import dagger.Component;

@Component(modules = {MetadataGenerationModule.class, ProcessorContextProviderModule.class})
@Singleton
public interface MetadataGenerationComponent {

    BeanDomainMetadataGenerator metadataGenerator();
}
