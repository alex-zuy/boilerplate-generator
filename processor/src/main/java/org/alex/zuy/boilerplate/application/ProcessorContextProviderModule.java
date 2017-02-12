package org.alex.zuy.boilerplate.application;

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
    public ProcessorContext provideProcessorContext() {
        return processorContext;
    }
}
