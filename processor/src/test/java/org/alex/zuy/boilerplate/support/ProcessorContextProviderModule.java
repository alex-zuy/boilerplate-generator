package org.alex.zuy.boilerplate.support;

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
    ProcessorContext provideProcessorContext() {
        return processorContext;
    }
}
