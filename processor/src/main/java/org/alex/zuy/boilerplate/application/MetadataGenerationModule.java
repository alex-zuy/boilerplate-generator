package org.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGenerator;
import org.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGeneratorImpl;

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
