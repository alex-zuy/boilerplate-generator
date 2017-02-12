package org.alex.zuy.boilerplate.support;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.services.ProcessorContext;

@Module
public class ProcessorContextProviderModule {

    private ProcessorContext processorContext;

    public ProcessorContextProviderModule(ProcessorContext processorContext) {
        this.processorContext = processorContext;
    }

    @Provides
    @Singleton
    ProcessorContext provideProcessorContext() {
        return processorContext;
    }
}
