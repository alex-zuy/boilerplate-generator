package com.github.alex.zuy.boilerplate.metadatageneration;

import com.github.alex.zuy.boilerplate.config.DomainConfig;
import com.github.alex.zuy.boilerplate.config.SupportClassesConfig;
import com.github.alex.zuy.boilerplate.services.RoundContext;

public interface BeanDomainMetadataGenerator {

    void generateDomainMetadataClasses(RoundContext roundContext, DomainConfig domainConfig,
        SupportClassesConfig supportClassesConfig);
}
