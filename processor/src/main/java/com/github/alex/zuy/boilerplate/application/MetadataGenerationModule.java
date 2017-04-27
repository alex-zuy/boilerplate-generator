package com.github.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import com.github.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGenerator;
import com.github.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGeneratorImpl;
import dagger.Module;
import dagger.Provides;

@Module(includes = {
    CodeGenerationModule.class,
    BeanDomainProcessingModule.class,
    DomainClassesCollectorModule.class,
    MetadataSupportModule.class})
public class MetadataGenerationModule {

    @Provides
    @Singleton
    BeanDomainMetadataGenerator provideMetadataGenerator(BeanDomainMetadataGeneratorImpl impl) {
        return impl;
    }
}
