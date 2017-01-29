package org.alex.zuy.boilerplate.processor;

import org.alex.zuy.boilerplate.config.MetadataGenerationStyle;
import org.alex.zuy.boilerplate.domain.BeanDomain;
import org.alex.zuy.boilerplate.metadatageneration.SupportClassesGenerator.SupportClassesConfig;
import org.alex.zuy.boilerplate.sourcemodel.TypeSetDeclaration;

public interface BeanDomainProcessor {

    TypeSetDeclaration processDomain(BeanDomain beanDomain, SupportClassesConfig supportClassesConfig,
        MetadataGenerationStyle style);
}
