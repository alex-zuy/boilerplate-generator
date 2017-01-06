package org.alex.zuy.boilerplate.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.alex.zuy.boilerplate.domain.types.Type;
import org.alex.zuy.boilerplate.domain.types.Types;
import org.alex.zuy.boilerplate.utils.CollectionUtils;

public class TypeAnalyserImpl implements TypeAnalyser {

    private static final Map<TypeKind, String> TYPE_KIND_TO_PRIMITIVE_TYPE_NAME =
        CollectionUtils.<TypeKind, String>getMapBuilder(HashMap::new)
            .put(TypeKind.BOOLEAN, "boolean")
            .put(TypeKind.BYTE, "byte")
            .put(TypeKind.CHAR, "char")
            .put(TypeKind.SHORT, "short")
            .put(TypeKind.INT, "int")
            .put(TypeKind.LONG, "long")
            .put(TypeKind.FLOAT, "float")
            .put(TypeKind.DOUBLE, "double")
            .build();

    private javax.lang.model.util.Types typeUtils;

    public TypeAnalyserImpl(javax.lang.model.util.Types typeUtils) {
        this.typeUtils = typeUtils;
    }

    @Override
    public Type<?> analyse(TypeMirror typeMirror) {
        TypeKind kind = typeMirror.getKind();
        switch (kind) {
            case ARRAY:
                TypeMirror componentType = ((ArrayType) typeMirror).getComponentType();
                return Types.makeArrayType(analyse(componentType));
            case VOID:
                return Types.makeVoidType();
            case DECLARED:
                return analyseDeclaredType((DeclaredType) typeMirror);
            default:
                return Optional.ofNullable(TYPE_KIND_TO_PRIMITIVE_TYPE_NAME.get(kind))
                    .map(Types::makeExactType)
                    .orElseThrow(() -> new UnsupportedTypeException(typeMirror));
        }
    }

    private Type<?> analyseDeclaredType(DeclaredType declaredType) {
        TypeElement typeElement = (TypeElement) typeUtils.asElement(declaredType);
        String qualifiedTypeName = typeElement.getQualifiedName().toString();

        if (isGenericType(declaredType)) {
            List<Type<?>> typeArguments = declaredType.getTypeArguments().stream()
                .map(this::analyse)
                .collect(Collectors.toList());
            return Types.makeTypeInstance(qualifiedTypeName, getEnclosingPackageName(typeElement), typeArguments);
        }
        else {
            return Types.makeExactType(qualifiedTypeName, getEnclosingPackageName(typeElement));
        }
    }

    private String getEnclosingPackageName(Element element) {
        if (element == null) {
            return null;
        }
        else if (element.getKind().equals(ElementKind.PACKAGE)) {
            PackageElement packageElement = (PackageElement) element;
            return packageElement.isUnnamed() ? null : packageElement.getQualifiedName().toString();
        }
        else {
            return getEnclosingPackageName(element.getEnclosingElement());
        }
    }

    private boolean isGenericType(DeclaredType declaredType) {
        return !declaredType.getTypeArguments().isEmpty();
    }
}
