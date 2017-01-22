package org.alex.zuy.boilerplate.application;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.processor.BeanDomainProcessor;
import org.alex.zuy.boilerplate.processor.BeanDomainProcessorImpl;
import org.alex.zuy.boilerplate.processor.BeanMetadataNamesGenerator;
import org.alex.zuy.boilerplate.processor.BeanMetadataNamesGeneratorImpl;

@Module(includes = {BeanDomainAnalysisModule.class})
public class BeanDomainProcessingModule {

    @Provides
    BeanDomainProcessor provideBeanDomainProcessor(BeanDomainProcessorImpl impl) {
        return impl;
    }

    @Provides
    BeanMetadataNamesGenerator provideBeanMetadataNamesGenerator() {
        return new BeanMetadataNamesGeneratorImpl();
    }
}
