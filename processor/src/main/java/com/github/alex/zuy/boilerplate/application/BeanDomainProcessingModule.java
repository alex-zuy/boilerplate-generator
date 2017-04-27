package com.github.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import com.github.alex.zuy.boilerplate.processor.BeanDomainProcessor;
import com.github.alex.zuy.boilerplate.processor.BeanDomainProcessorImpl;
import com.github.alex.zuy.boilerplate.processor.BeanMetadataNamesGenerator;
import com.github.alex.zuy.boilerplate.processor.BeanMetadataNamesGeneratorImpl;
import dagger.Module;
import dagger.Provides;

@Module(includes = {BeanDomainAnalysisModule.class, StringTemplateModule.class, MetadataSupportModule.class})
public class BeanDomainProcessingModule {

    @Provides
    @Singleton
    BeanDomainProcessor provideBeanDomainProcessor(BeanDomainProcessorImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    BeanMetadataNamesGenerator provideBeanMetadataNamesGenerator(BeanMetadataNamesGeneratorImpl impl) {
        return impl;
    }
}
