package com.github.alex.zuy.boilerplate.application;

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
    public ProcessorContext provideProcessorContext() {
        return processorContext;
    }
}
