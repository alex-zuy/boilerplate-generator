package org.alex.zuy.boilerplate.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ConfigurationProviderImplTest {

    private static final String CONFIGURATION_MINIMAL = "config-minimal.xml";

    @Test
    public void testMinimalConfiguration() throws Exception {
        ConfigurationProvider provider = new ConfigurationProviderImpl(getClass().getResource(CONFIGURATION_MINIMAL));
        DomainConfig domainConfig = provider.getDomainConfig();
        assertNotNull(domainConfig.includes().typeAnnotations());
        assertNotNull(domainConfig.includes().basePackages());
        assertNotNull(domainConfig.includes().packageInfoAnnotations());
        assertNotNull(domainConfig.excludes().patterns());
        assertNotNull(domainConfig.excludes().typeAnnotations());
        MetadataGenerationStyle style = domainConfig.generationStyle();
        assertNotNull(style.getPropertyClassNameTemplate());
        assertNotNull(style.getRelationshipsClassNameTemplate());
        assertNotNull(style.getStringConstantStyle());
        assertNotNull(style.getRelationshipsClassTerminalMethodNameTemplate());
    }
}
