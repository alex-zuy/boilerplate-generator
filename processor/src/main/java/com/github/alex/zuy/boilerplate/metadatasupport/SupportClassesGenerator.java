package com.github.alex.zuy.boilerplate.metadatasupport;

import java.util.List;

import com.github.alex.zuy.boilerplate.config.SupportClassesConfig;
import com.github.alex.zuy.boilerplate.sourcemodel.TypeDefinition;

public interface SupportClassesGenerator {

    List<TypeDefinition> generateSupportClasses(SupportClassesConfig config);

    SupportClassesTypes getSupportClassesTypes(SupportClassesConfig config);

}
