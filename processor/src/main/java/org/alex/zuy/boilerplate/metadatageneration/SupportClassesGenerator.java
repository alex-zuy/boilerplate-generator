package org.alex.zuy.boilerplate.metadatageneration;

import java.util.List;

import org.alex.zuy.boilerplate.sourcemodel.TypeDefinition;
import org.immutables.value.Value;

public interface SupportClassesGenerator {

    List<TypeDefinition> generateSupportClasses(SupportClassesConfig config);

    @Value.Immutable
    interface SupportClassesConfig {

        String getBasePackage();
    }
}
