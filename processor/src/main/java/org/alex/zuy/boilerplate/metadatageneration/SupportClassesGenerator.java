package org.alex.zuy.boilerplate.metadatageneration;

import org.alex.zuy.boilerplate.codegeneration.TypeGenerator;
import org.immutables.value.Value;

public interface SupportClassesGenerator {

    TypeGenerator.TypeImplementation generateSupportClasses(SupportClassesConfig config);

    @Value.Immutable
    interface SupportClassesConfig {

        String getBasePackage();
    }
}
