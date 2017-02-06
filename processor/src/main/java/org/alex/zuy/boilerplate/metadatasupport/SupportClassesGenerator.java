package org.alex.zuy.boilerplate.metadatasupport;

import java.util.List;

import org.alex.zuy.boilerplate.config.SupportClassesConfig;
import org.alex.zuy.boilerplate.sourcemodel.TypeDefinition;

public interface SupportClassesGenerator {

    List<TypeDefinition> generateSupportClasses(SupportClassesConfig config);

    SupportClassesTypes getSupportClassesTypes(SupportClassesConfig config);

}
