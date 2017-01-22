package org.alex.zuy.boilerplate.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;

import org.alex.zuy.boilerplate.domain.BeanClass;
import org.alex.zuy.boilerplate.domain.BeanDomain;
import org.alex.zuy.boilerplate.domain.types.Type;
import org.alex.zuy.boilerplate.domain.types.Types;
import org.alex.zuy.boilerplate.sourcemodel.FieldDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.ImmutableFieldDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.ImmutableTypeDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.ImmutableTypeSetDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.TypeDeclaration;
import org.alex.zuy.boilerplate.sourcemodel.TypeKind;
import org.alex.zuy.boilerplate.sourcemodel.TypeSetDeclaration;

public class BeanDomainProcessorImpl implements BeanDomainProcessor {

    private static final Type<?> TYPE_STRING = Types.makeExactType("java.lang.String", "java.lang");

    private interface Templates {

        String STRING_EXPRESSION = "stringConstant.ftl";
    }

    private interface ModelRefs {

        String VALUE = "value";
    }

    private BeanMetadataNamesGenerator namesGenerator;

    public BeanDomainProcessorImpl(BeanMetadataNamesGenerator namesGenerator) {
        this.namesGenerator = namesGenerator;
    }

    @Override
    public TypeSetDeclaration processDomain(BeanDomain beanDomain) {
        Set<TypeDeclaration> metadataClasses = new HashSet<>();

        for (final BeanClass beanClass : beanDomain.getBeanClasses()) {
            List<FieldDeclaration> fieldDeclarations = beanClass.getProperties().values().stream()
                .map(property -> {
                    String constantName = namesGenerator.makeBeanPropertyConstantName(property.getName());
                    return makePublicStaticFinalStringField(constantName, property.getName());
                })
                .collect(Collectors.toList());

            String propertiesClassPackageName = beanClass.getType().getPackageName().orElse(null);
            String propertiesClassSimpleName = makeBeanPropertiesClassSimpleName(beanClass.getType());
            TypeDeclaration typeDeclaration = ImmutableTypeDeclaration.builder()
                .addModifiers(Modifier.PUBLIC)
                .kind(TypeKind.CLASS)
                .packageName(propertiesClassPackageName)
                .simpleName(propertiesClassSimpleName)
                .qualifiedName(makeClassQualifiedName(propertiesClassPackageName, propertiesClassSimpleName))
                .addAllFields(fieldDeclarations)
                .build();

            metadataClasses.add(typeDeclaration);
        }

        return ImmutableTypeSetDeclaration.builder()
            .addAllTypes(metadataClasses)
            .build();
    }

    private String makeBeanPropertiesClassSimpleName(Type<?> type) {
        String simpleName = type.getPackageName()
            .map(packageName -> type.getName().substring(packageName.length() + 1))
            .orElse(type.getName());
        return namesGenerator.makeBeanPropertiesClassName(simpleName);
    }

    private String makeClassQualifiedName(String packageName, String simpleName) {
        return packageName != null
            ? String.format("%s.%s", packageName, simpleName)
            : simpleName;
    }

    private FieldDeclaration makePublicStaticFinalStringField(String fieldName, Object fieldValue) {
        return ImmutableFieldDeclaration.builder()
            .name(fieldName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .type(TYPE_STRING)
            .hasInitialValue(true)
            .templateName(Templates.STRING_EXPRESSION)
            .putData(ModelRefs.VALUE, fieldValue)
            .build();
    }
}
