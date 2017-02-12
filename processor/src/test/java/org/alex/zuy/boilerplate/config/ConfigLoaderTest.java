
package org.alex.zuy.boilerplate.config;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.alex.zuy.boilerplate.config.generated.Configuration;
import org.alex.zuy.boilerplate.config.generated.Domain;
import org.alex.zuy.boilerplate.config.generated.GenerationStyle;
import org.alex.zuy.boilerplate.config.generated.SupportClasses;
import org.junit.Test;

public class ConfigLoaderTest {

    @Test
    public void testLoadingConfig() throws Exception {
        final URL url = getClass().getResource("config.xml");
        final Configuration config = new ConfigLoader().loadConfig(url);
        assertSupportClassesCorrect(config.getBeanProcessing().getSupportClasses());
        assertGenerationStyleCorrect(config.getBeanProcessing().getGenerationStyle());
        assertDomainCorrect(config.getBeanProcessing().getDomain());
    }

    private void assertSupportClassesCorrect(SupportClasses supportClasses) {
        assertEquals("com.example.generated.support", supportClasses.getBasePackage());
    }

    private void assertGenerationStyleCorrect(GenerationStyle style) {
        assertEquals("${beanClassName}_p", style.getMetadataClasses().getPropertyClassName());
        assertEquals("${beanClassName}_r", style.getMetadataClasses().getRelationshipClassName());
        assertEquals("camelcase", style.getMetadataClasses().getStringConstantNameStyle().value());
        //assertEquals("${beanPropertyName}Property", style.getMetadataClasses());
    }

    private void assertDomainCorrect(final Domain domain) {
        final List<String> includesBasePackages =
                domain.getIncludes().getBasePackages().getPackages();
        assertEquals("com.example.first", includesBasePackages.get(0));
        assertEquals("com.example.second", includesBasePackages.get(1));
        assertEquals("com.example.third", includesBasePackages.get(2));
        final List<String> includesTypeAnnotations =
                domain.getIncludes().getTypeAnnotations().getAnnotations();
        assertEquals("com.example.MarkerFirst", includesTypeAnnotations.get(0));
        assertEquals("com.example.MarkerSecond", includesTypeAnnotations.get(1));
        final List<String> includesPackageAnnotations =
                domain.getIncludes().getPackageInfoAnnotations().getAnnotations();
        assertEquals("com.example.MarkerFirst", includesPackageAnnotations.get(0));
        assertEquals("com.example.MarkerSecond", includesPackageAnnotations.get(1));
        final List<String> excludesTypeAnnotations =
                domain.getExcludes().getTypeAnnotations().getAnnotations();
        assertEquals("com.example.ExcludeMarkerFirst", excludesTypeAnnotations.get(0));
        assertEquals("com.example.ExcludeMarkerSecond", excludesTypeAnnotations.get(1));
        final List<String> excludePatterns = domain.getExcludes().getPatterns().getPatterns();
        assertEquals("regex to exclude classes by FQN", excludePatterns.get(0));
        assertEquals("regex to exclude classes by FQN", excludePatterns.get(1));
    }
}
