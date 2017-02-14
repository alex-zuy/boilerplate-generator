package org.alex.zuy.boilerplate.config;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;

import org.junit.Test;

public class ConfigurationProviderImplTest {

    private static final String CONFIGURATION_MINIMAL = "config-minimal.xml";

    @Test
    public void testMinimalConfiguration() throws Exception {
        URL configUrl = getClass().getResource(CONFIGURATION_MINIMAL);
        ConfigurationProvider provider = new ConfigurationProviderImpl(new File(configUrl.toURI()));
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
