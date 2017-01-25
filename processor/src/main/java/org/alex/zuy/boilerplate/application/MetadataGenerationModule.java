package org.alex.zuy.boilerplate.application;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGenerator;
import org.alex.zuy.boilerplate.metadatageneration.BeanDomainMetadataGeneratorImpl;
import org.alex.zuy.boilerplate.metadatageneration.SupportClassesGenerator;
import org.alex.zuy.boilerplate.metadatageneration.SupportClassesGeneratorImpl;

@Module(includes = {CodeGenerationModule.class, BeanDomainProcessingModule.class, DomainClassesCollectorModule.class})
public class MetadataGenerationModule {

    @Provides
    BeanDomainMetadataGenerator provideMetadataGenerator(BeanDomainMetadataGeneratorImpl impl) {
        return impl;
    }

    @Provides
    SupportClassesGenerator provideSupportClassesGenerator(SupportClassesGeneratorImpl impl) {
        return impl;
    }
}
