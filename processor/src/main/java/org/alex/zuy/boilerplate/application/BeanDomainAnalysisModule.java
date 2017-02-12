package org.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import org.alex.zuy.boilerplate.analysis.BeanClassAnalyser;
import org.alex.zuy.boilerplate.analysis.BeanClassAnalyserImpl;
import org.alex.zuy.boilerplate.analysis.BeanDomainAnalyser;
import org.alex.zuy.boilerplate.analysis.BeanDomainAnalyserImpl;
import org.alex.zuy.boilerplate.analysis.TypeAnalyser;
import org.alex.zuy.boilerplate.analysis.TypeAnalyserImpl;

@Module
public class BeanDomainAnalysisModule {

    @Provides
    @Singleton
    TypeAnalyser provideTypeAnalyser(TypeAnalyserImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    BeanClassAnalyser provideBeanClassAnalyser(BeanClassAnalyserImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    BeanDomainAnalyser provideBeanDomainAnalyser(BeanDomainAnalyserImpl impl) {
        return impl;
    }
}
