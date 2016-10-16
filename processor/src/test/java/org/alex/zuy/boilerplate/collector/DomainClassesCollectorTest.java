package org.alex.zuy.boilerplate.collector;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.regex.Pattern;

import javax.lang.model.element.TypeElement;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class DomainClassesCollectorTest {

    private static final String INCLUDES_BASE_PACKAGE = "com.example.main";

    private static final String INCLUDES_TYPE_ANNOTATION = "com.example.type.Marker";

    private static final String INCLUDES_PACKAGE_INFO_ANNOTATION = "com.example.package.Marker";

    private static final String EXCLUDES_TYPE_ANNOTATION = "com.example.type.exclude.Marker";

    private static final String EXCLUDES_PATTERN = "^.*Excluded$";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BasePackageClassesCollector basePackageClassesCollector;

    @Mock
    private TypeAnnotatedClassesCollector typeAnnotatedClassesCollector;

    @Mock
    private PackageInfoAnnotatedClassesCollector packageInfoAnnotatedClassesCollector;

    @Mock
    private CollectorComponentsFactory componentsFactory;

    private ImmutableIncludesConfig includesConfig;

    private ImmutableExcludesConfig excludesConfig;


    @Test
    public void testAllCollectorsAreQueriedForDomainClasses() throws Exception {
        givenIncludesConfigWithBasePackageAndTypeAnnotationAndPackageInfoAnnotation();
        giveExcludesConfigWithPatternAndTypeAnnotation();
        givenExistsTwoClassesIncludedByBasePackage();
    }

    private void givenExistsTwoClassesIncludedByBasePackage() {
        final TypeElement mock = mock(TypeElement.class);
    }

    private void givenIncludesConfigWithBasePackageAndTypeAnnotationAndPackageInfoAnnotation() {
        includesConfig = ImmutableIncludesConfig.builder()
            .addBasePackages(INCLUDES_BASE_PACKAGE)
            .addTypeAnnotations(INCLUDES_TYPE_ANNOTATION)
            .addPackageInfoAnnotations(INCLUDES_PACKAGE_INFO_ANNOTATION)
            .build();
    }

    private void giveExcludesConfigWithPatternAndTypeAnnotation() {
        excludesConfig = ImmutableExcludesConfig.builder()
            .addTypeAnnotations(EXCLUDES_TYPE_ANNOTATION)
            .addPatterns(Pattern.compile(EXCLUDES_PATTERN))
            .build();
    }
}
