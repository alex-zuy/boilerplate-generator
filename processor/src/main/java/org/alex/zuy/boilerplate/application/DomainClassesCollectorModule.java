package org.alex.zuy.boilerplate.application;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.collector.DomainClassesCollector;
import org.alex.zuy.boilerplate.collector.DomainClassesCollectorImpl;

@Module
public class DomainClassesCollectorModule {

    @Provides
    DomainClassesCollector provideDomainClassesCollector(DomainClassesCollectorImpl impl) {
        return impl;
    }
}
