package com.github.alex.zuy.boilerplate.application;

import javax.inject.Singleton;

import com.github.alex.zuy.boilerplate.analysis.BeanClassAnalyser;
import com.github.alex.zuy.boilerplate.analysis.BeanClassAnalyserImpl;
import com.github.alex.zuy.boilerplate.analysis.BeanDomainAnalyser;
import com.github.alex.zuy.boilerplate.analysis.BeanDomainAnalyserImpl;
import com.github.alex.zuy.boilerplate.analysis.TypeAnalyser;
import com.github.alex.zuy.boilerplate.analysis.TypeAnalyserImpl;
import dagger.Module;
import dagger.Provides;

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
