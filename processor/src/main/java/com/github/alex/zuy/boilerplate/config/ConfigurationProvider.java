package com.github.alex.zuy.boilerplate.config;

public interface ConfigurationProvider {

    DomainConfig getDomainConfig();

    MetadataGenerationStyle getMetadataGenerationStyle();

    SupportClassesConfig getSupportClassesConfig();
}
