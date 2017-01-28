package org.alex.zuy.boilerplate.metadatageneration;

import org.alex.zuy.boilerplate.collector.DomainConfig;
import org.alex.zuy.boilerplate.services.RoundContext;
import org.immutables.value.Value;

public interface BeanDomainMetadataGenerator {

    void generateDomainMetadataClasses(RoundContext roundContext, DomainConfig domainConfig,
        MetadataGenerationStyle style, SupportClassesGenerator.SupportClassesConfig supportClassesConfig);

    @Value.Immutable
    interface MetadataGenerationStyle {

        String getPropertyClassNameTemplate();

        String getRelationshipsClassNameTemplate();

        StringConstantStyle getStringConstantStyle();

        enum StringConstantStyle {

            CAMELCASE,

            UPPERCASE
        }
    }
}
