package com.github.alex.zuy.boilerplate.metadatasupport;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

import com.github.alex.zuy.boilerplate.config.SupportClassesConfig;
import com.github.alex.zuy.boilerplate.domain.QualifiedName;
import com.github.alex.zuy.boilerplate.domain.types.Type;
import com.github.alex.zuy.boilerplate.domain.types.Types;
import com.github.alex.zuy.boilerplate.sourcemodel.ImmutableTypeDefinition;
import com.github.alex.zuy.boilerplate.sourcemodel.TypeDefinition;
import com.github.alex.zuy.boilerplate.utils.IoUtils;

public class SupportClassesGeneratorImpl implements SupportClassesGenerator {

    private static final String TEMPLATE_PATH = "com/github/alex/zuy/boilerplate/templates";

    private static final String PLACEHOLDER_PROPERTY_CHAIN_NODE_CLASS_NAME = "_PropertyChainNode_";

    private static final String PROPERTY_CHAIN_NODE_CLASS_NAME = "PropertyChainNode";

    private static final String PLACEHOLDER_PACKAGE_STATEMENT = "package com.example;";

    private static final String PACKAGE_STATEMENT_PATTERN = "package %s;";

    /* We`ll fix this soon. */
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    @Override
    public List<TypeDefinition> generateSupportClasses(SupportClassesConfig config) {
        String filePath = String.format("%s/%s.java", TEMPLATE_PATH, PROPERTY_CHAIN_NODE_CLASS_NAME);
        try (Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filePath))) {
            String packageName = config.getBasePackage();
            String packageStatement = String.format(PACKAGE_STATEMENT_PATTERN, packageName);
            String classSource = IoUtils.readToString(reader)
                .replace(PLACEHOLDER_PACKAGE_STATEMENT, packageStatement)
                .replaceAll(PLACEHOLDER_PROPERTY_CHAIN_NODE_CLASS_NAME, PROPERTY_CHAIN_NODE_CLASS_NAME);
            TypeDefinition typeDefinition = ImmutableTypeDefinition.builder()
                .sourceCode(classSource)
                .simpleName(PROPERTY_CHAIN_NODE_CLASS_NAME)
                .packageName(packageName)
                .build();
            return Collections.singletonList(typeDefinition);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SupportClassesTypes getSupportClassesTypes(SupportClassesConfig config) {
        QualifiedName qualifiedName = new QualifiedName(PROPERTY_CHAIN_NODE_CLASS_NAME, config.getBasePackage());
        Type<?> propertyChainNodeType = Types.makeExactType(qualifiedName);
        return ImmutableSupportClassesTypes.builder()
            .propertyChainNodeType(propertyChainNodeType)
            .build();
    }
}
