
package org.alex.zuy.boilerplate.config;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.alex.zuy.boilerplate.config.generated.Configuration;
import org.alex.zuy.boilerplate.config.generated.Domain;
import org.alex.zuy.boilerplate.config.generated.DomainList;
import org.alex.zuy.boilerplate.config.generated.GenerationStyleList;
import org.alex.zuy.boilerplate.config.generated.Style;
import org.alex.zuy.boilerplate.config.generated.SupportClasses;
import org.junit.Test;

public class ConfigLoaderTest {

    @Test
    public void testLoadingConfig() throws Exception {
        final URL url = getClass().getResource("config.xml");
        final Configuration config = new ConfigLoader().loadConfig(url);
        assertEquals(1, config.getBeanProcessing().getDomains().getDomains().size());
        assertSupportClassesCorrect(config.getBeanProcessing().getSupportClasses());
        assertGenerationStylesCorrect(config.getBeanProcessing().getGenerationStyles());
        assertDomainsCorrect(config.getBeanProcessing().getDomains());
    }

    private void assertSupportClassesCorrect(SupportClasses supportClasses) {
        assertEquals("com.example.generated.support", supportClasses.getBasePackage());
    }

    private void assertGenerationStylesCorrect(GenerationStyleList styles) {
        assertEquals(1, styles.getStyles().size());
        assertGenerationStyleMainCorrect(styles.getStyles().get(0));
    }

    private void assertGenerationStyleMainCorrect(Style style) {
        assertEquals("main", style.getId());
        assertEquals("${beanClassName}_p", style.getMetadataClasses().getPropertyClassName());
        assertEquals("${beanClassName}_r", style.getMetadataClasses().getRelationshipClassName());
        assertEquals("camelcase", style.getMetadataClasses().getStringConstantNameStyle().value());
    }

    private void assertDomainsCorrect(DomainList domains) {
        assertEquals(1, domains.getDomains().size());
        assertDomainCorrect(domains.getDomains().get(0));
    }

    private void assertDomainCorrect(final Domain domain) {
        assertEquals("main", ((Style) domain.getStyle()).getId());
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
