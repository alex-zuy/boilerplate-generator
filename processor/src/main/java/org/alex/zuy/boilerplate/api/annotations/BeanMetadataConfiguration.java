package org.alex.zuy.boilerplate.api.annotations;

public @interface BeanMetadataConfiguration {

    SupportClassesConfiguration supportClassesConfiguration();

    GenerationStyle generationStyle() default @GenerationStyle;

    DomainConfiguration domainConfiguration();
}
