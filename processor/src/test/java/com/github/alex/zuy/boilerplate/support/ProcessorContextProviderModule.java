package com.github.alex.zuy.boilerplate.support;

import javax.inject.Singleton;

import com.github.alex.zuy.boilerplate.services.ProcessorContext;
import dagger.Module;
import dagger.Provides;

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
