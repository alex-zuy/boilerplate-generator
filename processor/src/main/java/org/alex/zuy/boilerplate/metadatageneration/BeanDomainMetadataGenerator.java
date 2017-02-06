package org.alex.zuy.boilerplate.metadatageneration;

import org.alex.zuy.boilerplate.config.DomainConfig;
import org.alex.zuy.boilerplate.config.SupportClassesConfig;
import org.alex.zuy.boilerplate.services.RoundContext;

public interface BeanDomainMetadataGenerator {

    void generateDomainMetadataClasses(RoundContext roundContext, DomainConfig domainConfig,
        SupportClassesConfig supportClassesConfig);
}
