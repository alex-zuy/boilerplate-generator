package org.alex.zuy.boilerplate.metadatageneration;

import java.util.List;

import org.alex.zuy.boilerplate.domain.types.Type;
import org.alex.zuy.boilerplate.sourcemodel.TypeDefinition;
import org.immutables.value.Value;

public interface SupportClassesGenerator {

    List<TypeDefinition> generateSupportClasses(SupportClassesConfig config);

    SupportClassesTypes getSupportClassesTypes(SupportClassesConfig config);

    @Value.Immutable
    interface SupportClassesConfig {

        String getBasePackage();
    }

    @Value.Immutable
    interface SupportClassesTypes {

        Type<?> getPropertyChainNodeType();
    }
}
