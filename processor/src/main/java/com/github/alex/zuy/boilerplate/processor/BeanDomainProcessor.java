package com.github.alex.zuy.boilerplate.processor;

import com.github.alex.zuy.boilerplate.config.MetadataGenerationStyle;
import com.github.alex.zuy.boilerplate.config.SupportClassesConfig;
import com.github.alex.zuy.boilerplate.domain.BeanDomain;
import com.github.alex.zuy.boilerplate.sourcemodel.TypeSetDeclaration;

public interface BeanDomainProcessor {

    TypeSetDeclaration processDomain(BeanDomain beanDomain, SupportClassesConfig supportClassesConfig,
        MetadataGenerationStyle style);
}
