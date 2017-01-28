package org.alex.zuy.boilerplate.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.lang.model.element.Modifier;

import org.alex.zuy.boilerplate.domain.BeanClass;
import org.alex.zuy.boilerplate.domain.BeanDomain;
import org.alex.zuy.boilerplate.domain.BeanProperty;
import org.alex.zuy.boilerplate.domain.types.Type;
import org.alex.zuy.boilerplate.domain.types.Types;
import org.alex.zuy.boilerplate.sourcemodel.FieldDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.ImmutableFieldDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.ImmutableMethodDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.ImmutableMethodParameterDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.ImmutableTypeDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.ImmutableTypeSetDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.MethodDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.MethodDeclaration.MethodParameterDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.TypeDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.TypeKind;
import org.alex.zuy.boilerplate.sourcemodel.TypeSetDeclaration;

public class BeanDomainProcessorImpl implements BeanDomainProcessor {

    private static final Type<?> TYPE_STRING = Types.makeExactType("java.lang.String", "java.lang");

    private interface Templates {

        String STRING_EXPRESSION = "stringConstant.ftl";

        String METHOD_BODY_RELATIONSHIP_ACCESSOR = "methodBody/propertyClassRelationshipAccessor.ftl";
    }

    private interface ModelRefs {

        String VALUE = "value";
    }

    private interface RelationshipClassMethodBodyTemplates {

        String RELATIONSHIP_CONTINUATION = "methodBody/relationshipClassRelationshipMethod.ftl";

        String RELATIONSHIP_TERMINATOR = "methodBody/propertyChainStop.ftl";

        String FORWARD_SUPER_CTOR_ONE_PARAMETER = "methodBody/forwardSuperCtorOneParameter.ftl";

        String FORWARD_SUPER_CTOR_TWO_PARAMETERS = "methodBody/forwardSuperCtorTwoParameters.ftl";
    }

    private interface MethodsBodyModelRefs {

        String PROPERTY_NAME_CONSTANT_NAME = "propertyNameConstantName";

        String TYPE_PROPERTY_CLASS = "propertiesClassType";

        String TYPE_RELATIONSHIP_CLASS = "relationshipClassType";

        String PARAMETER_PROPERTY = "propertyParameter";

        String PARAMETER_CHAIN_TAIL_NODE = "chainTailNodeParameter";
    }

    /* Hardcoded support class names. TODO */
    private interface SupportClassProperties {

        String PROPERTY_CHAIN_NODE_CLASS_PACKAGE = "com.example";

        String PROPERTY_CHAIN_NODE_CLASS_NAME = "PropertyChainNode";
    }

    private BeanMetadataNamesGenerator namesGenerator;

    @Inject
    public BeanDomainProcessorImpl(BeanMetadataNamesGenerator namesGenerator) {
        this.namesGenerator = namesGenerator;
    }

    @Override
    public TypeSetDeclaration processDomain(BeanDomain beanDomain) {
        Set<TypeDeclaration> metadataClasses = new HashSet<>();

        Set<Type<?>> domainClassesTypes = beanDomain.getBeanClasses().stream()
            .map(BeanClass::getType)
            .collect(Collectors.toSet());

        Type<?> propertyChainNodeType = Types.makeExactType(
            makeClassQualifiedName(SupportClassProperties.PROPERTY_CHAIN_NODE_CLASS_PACKAGE,
                SupportClassProperties.PROPERTY_CHAIN_NODE_CLASS_NAME),
            SupportClassProperties.PROPERTY_CHAIN_NODE_CLASS_PACKAGE);

        for (BeanClass beanClass : beanDomain.getBeanClasses()) {
            BeanClassProcessor beanClassProcessor = new BeanClassProcessor(beanClass, propertyChainNodeType);
            metadataClasses.add(makeBeanPropertiesClass(beanClass, domainClassesTypes, beanClassProcessor));
            metadataClasses.add(makeBeanRelationshipsClass(beanClass, domainClassesTypes, beanClassProcessor));
        }

        return ImmutableTypeSetDeclaration.builder()
            .addAllTypes(metadataClasses)
            .build();
    }

    private TypeDeclaration makeBeanPropertiesClass(BeanClass beanClass, Set<Type<?>> domainClassesTypes,
        BeanClassProcessor beanClassProcessor) {

        List<FieldDeclaration> fieldDeclarations = beanClass.getProperties().values().stream()
            .map(beanClassProcessor::buildConstantFieldForPropertyName)
            .collect(Collectors.toList());

        List<MethodDeclaration> propertyClassRelationshipAccessors = beanClass.getProperties().values().stream()
            .filter(beanProperty -> domainClassesTypes.contains(beanProperty.getType()))
            .map(beanClassProcessor::buildPropertyRelationshipAccessorMethod)
            .collect(Collectors.toList());

        Type<?> propertiesClassType = beanClassProcessor.getPropertiesClassType();

        return ImmutableTypeDeclaration.builder()
            .addModifiers(Modifier.PUBLIC)
            .kind(TypeKind.CLASS)
            .packageName(propertiesClassType.getPackageName().orElse(null))
            .simpleName(makeTypeSimpleName(propertiesClassType))
            .qualifiedName(propertiesClassType.getName())
            .addAllFields(fieldDeclarations)
            .addAllMethods(propertyClassRelationshipAccessors)
            .build();
    }

    private TypeDeclaration makeBeanRelationshipsClass(BeanClass beanClass, Set<Type<?>> domainClassesTypes,
        BeanClassProcessor beanClassProcessor) {

        MethodDeclaration chainStartCtor = beanClassProcessor.buildRelationshipStartCtor();

        MethodDeclaration chainContinuationCtor = beanClassProcessor.buildRelationshipContinuationCtor();

        List<MethodDeclaration> relationshipsMethods = beanClass.getProperties().values().stream()
            .map(property -> {
                if (domainClassesTypes.contains(property.getType())) {
                    return beanClassProcessor.buildRelationshipContinuationMethod(property);
                }
                else {
                    return beanClassProcessor.buildRelationshipTerminalMethod(property);
                }
            })
            .collect(Collectors.toList());

        Type<?> relationshipsClassType = beanClassProcessor.getRelationshipsClassType();

        return ImmutableTypeDeclaration.builder()
            .kind(TypeKind.CLASS)
            .packageName(relationshipsClassType.getPackageName().orElse(null))
            .simpleName(makeTypeSimpleName(relationshipsClassType))
            .qualifiedName(relationshipsClassType.getName())
            .extendedType(beanClassProcessor.getPropertyChainNodeType())
            .addMethods(chainStartCtor, chainContinuationCtor)
            .addAllMethods(relationshipsMethods)
            .build();
    }

    private String makeTypeSimpleName(Type<?> type) {
        return type.getPackageName()
            .map(packageName -> type.getName().substring(packageName.length() + 1))
            .orElse(type.getName());
    }

    private Type<?> makeRelationshipsClassType(Type<?> type) {
        String simpleName = namesGenerator.makeBeanRelationshipsClassName(makeTypeSimpleName(type));
        String packageName = type.getPackageName().orElse(null);
        return Types.makeExactType(makeClassQualifiedName(packageName, simpleName), packageName);
    }

    private Type<?> makePropertiesClassType(Type<?> beanType) {
        String simpleName = namesGenerator.makeBeanPropertiesClassName(makeTypeSimpleName(beanType));
        String packageName = beanType.getPackageName().orElse(null);
        return Types.makeExactType(makeClassQualifiedName(packageName, simpleName), packageName);
    }

    private String makeClassQualifiedName(String packageName, String simpleName) {
        return packageName != null
            ? String.format("%s.%s", packageName, simpleName)
            : simpleName;
    }

    private class BeanClassProcessor {

        private Type<?> relationshipsClassType;

        private Type<?> propertiesClassType;

        private Type<?> propertyChainNodeType;

        BeanClassProcessor(BeanClass beanClass, Type<?> propertyChainNodeType) {
            relationshipsClassType = makeRelationshipsClassType(beanClass.getType());
            propertiesClassType = makePropertiesClassType(beanClass.getType());
            this.propertyChainNodeType = propertyChainNodeType;
        }

        Type<?> getRelationshipsClassType() {
            return relationshipsClassType;
        }

        Type<?> getPropertiesClassType() {
            return propertiesClassType;
        }

        Type<?> getPropertyChainNodeType() {
            return propertyChainNodeType;
        }

        FieldDeclaration buildConstantFieldForPropertyName(BeanProperty property) {
            String constantName = namesGenerator.makeBeanPropertyConstantName(property.getName());
            return ImmutableFieldDeclaration.builder()
                .name(constantName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .type(TYPE_STRING)
                .hasInitialValue(true)
                .templateName(Templates.STRING_EXPRESSION)
                .putData(ModelRefs.VALUE, property.getName())
                .build();
        }

        MethodDeclaration buildPropertyRelationshipAccessorMethod(BeanProperty property) {
            Type<?> propertyRelationshipClassType = makeRelationshipsClassType(property.getType());
            String propertyConstantName = namesGenerator.makeBeanPropertyConstantName(property.getName());

            return ImmutableMethodDeclaration.builder()
                .name(property.getName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returnType(propertyRelationshipClassType)
                .templateName(Templates.METHOD_BODY_RELATIONSHIP_ACCESSOR)
                .putData(MethodsBodyModelRefs.TYPE_RELATIONSHIP_CLASS, propertyRelationshipClassType)
                .putData(MethodsBodyModelRefs.PROPERTY_NAME_CONSTANT_NAME, propertyConstantName)
                .build();
        }

        MethodDeclaration buildRelationshipStartCtor() {
            MethodParameterDeclaration propertyParameter = makePropertyParameterDeclaration();
            return ImmutableMethodDeclaration.builder()
                .name(makeTypeSimpleName(relationshipsClassType))
                .addParameters(propertyParameter)
                .templateName(RelationshipClassMethodBodyTemplates.FORWARD_SUPER_CTOR_ONE_PARAMETER)
                .putData(MethodsBodyModelRefs.PARAMETER_PROPERTY, propertyParameter)
                .build();
        }

        MethodDeclaration buildRelationshipContinuationCtor() {
            MethodParameterDeclaration propertyParameter = makePropertyParameterDeclaration();
            MethodParameterDeclaration chainTailNodeParameter = ImmutableMethodParameterDeclaration.builder()
                .name("tail").type(propertyChainNodeType).build();
            return ImmutableMethodDeclaration.builder()
                .name(makeTypeSimpleName(relationshipsClassType))
                .addParameters(chainTailNodeParameter)
                .addParameters(propertyParameter)
                .templateName(RelationshipClassMethodBodyTemplates.FORWARD_SUPER_CTOR_TWO_PARAMETERS)
                .putData(MethodsBodyModelRefs.PARAMETER_PROPERTY, propertyParameter)
                .putData(MethodsBodyModelRefs.PARAMETER_CHAIN_TAIL_NODE, chainTailNodeParameter)
                .build();
        }

        MethodDeclaration buildRelationshipContinuationMethod(BeanProperty property) {
            String propertyConstantName = namesGenerator.makeBeanPropertyConstantName(property.getName());
            Type<?> propertyRelationshipClassType = makeRelationshipsClassType(property.getType());
            return ImmutableMethodDeclaration.builder()
                .name(property.getName())
                .addModifiers(Modifier.PUBLIC)
                .returnType(propertyRelationshipClassType)
                .templateName(RelationshipClassMethodBodyTemplates.RELATIONSHIP_CONTINUATION)
                .putData(MethodsBodyModelRefs.TYPE_RELATIONSHIP_CLASS, propertyRelationshipClassType)
                .putData(MethodsBodyModelRefs.TYPE_PROPERTY_CLASS, propertiesClassType)
                .putData(MethodsBodyModelRefs.PROPERTY_NAME_CONSTANT_NAME, propertyConstantName)
                .build();
        }

        MethodDeclaration buildRelationshipTerminalMethod(BeanProperty property) {
            String propertyConstantName = namesGenerator.makeBeanPropertyConstantName(property.getName());
            return ImmutableMethodDeclaration.builder()
                .name(property.getName())
                .addModifiers(Modifier.PUBLIC)
                .returnType(TYPE_STRING)
                .templateName(RelationshipClassMethodBodyTemplates.RELATIONSHIP_TERMINATOR)
                .putData(MethodsBodyModelRefs.TYPE_PROPERTY_CLASS, propertiesClassType)
                .putData(MethodsBodyModelRefs.PROPERTY_NAME_CONSTANT_NAME, propertyConstantName)
                .build();
        }

        private MethodParameterDeclaration makePropertyParameterDeclaration() {
            return ImmutableMethodParameterDeclaration.builder()
                .name("property").type(TYPE_STRING).build();
        }
    }
}
