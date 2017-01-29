package org.alex.zuy.boilerplate.application;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGenerator;
import org.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGeneratorImpl;

@Module(includes = {CodeGenerationModule.class, BeanDomainProcessingModule.class, DomainClassesCollectorModule.class})
public class MetadataGenerationModule {

    @Provides
    BeanDomainMetadataGenerator provideMetadataGenerator(BeanDomainMetadataGeneratorImpl impl) {
        return impl;
    }
}
