package com.github.alex.zuy.boilerplate.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import com.github.alex.zuy.boilerplate.domain.QualifiedName;
import com.github.alex.zuy.boilerplate.domain.types.Type;
import com.github.alex.zuy.boilerplate.domain.types.Types;
import com.github.alex.zuy.boilerplate.services.ProcessorContext;
import com.github.alex.zuy.boilerplate.utils.CollectionUtils;

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

    private static final Set<ElementKind> ENCLOSING_TYPE_KINDS;

    static {
        EnumSet<ElementKind> enclosingTypeKinds = EnumSet.noneOf(ElementKind.class);
        enclosingTypeKinds.add(ElementKind.CLASS);
        enclosingTypeKinds.add(ElementKind.INTERFACE);
        enclosingTypeKinds.add(ElementKind.ENUM);
        enclosingTypeKinds.add(ElementKind.ANNOTATION_TYPE);
        ENCLOSING_TYPE_KINDS = Collections.unmodifiableSet(enclosingTypeKinds);
    }

    private ProcessorContext processorContext;

    @Inject
    public TypeAnalyserImpl(ProcessorContext processorContext) {
        this.processorContext = processorContext;
    }

    @Override
    public Type<?> analyse(TypeMirror typeMirror) throws UnsupportedTypeException {
        TypeKind kind = typeMirror.getKind();
        switch (kind) {
            case ARRAY:
                TypeMirror componentType = ((ArrayType) typeMirror).getComponentType();
                return Types.makeArrayType(analyse(componentType));
            case VOID:
                return Types.makeVoidType();
            case DECLARED:
                return analyseDeclaredType((DeclaredType) typeMirror);
            case TYPEVAR:
                return analyseTypeVariable((TypeVariable) typeMirror);
            default:
                if (kind.isPrimitive()) {
                    return Optional.ofNullable(TYPE_KIND_TO_PRIMITIVE_TYPE_NAME.get(kind))
                        .map(typeName -> Types.makeExactType(new QualifiedName(typeName)))
                        .orElseThrow(() -> new IllegalArgumentException(
                            String.format("Unknown primitive type '%s'", typeMirror)));
                }
                else {
                    throw new UnsupportedTypeException(typeMirror);
                }
        }
    }

    private Type<?> analyseTypeVariable(TypeVariable variable) {
        String simpleName = variable.asElement().getSimpleName().toString();
        return Types.makeTypeParameter(new QualifiedName(simpleName));
    }

    private Type<?> analyseDeclaredType(DeclaredType declaredType) throws UnsupportedTypeException {
        TypeElement typeElement = (TypeElement) processorContext.getTypeUtils().asElement(declaredType);
        QualifiedName qualifiedName = getTypeQualifiedName(typeElement);

        if (isGenericType(declaredType)) {
            List<Type<?>> typeArguments = new ArrayList<>();
            for (TypeMirror argument : declaredType.getTypeArguments()) {
                typeArguments.add(analyse(argument));
            }
            return Types.makeTypeInstance(qualifiedName, typeArguments);
        }
        else {
            return Types.makeExactType(qualifiedName);
        }
    }

    private QualifiedName getTypeQualifiedName(Element typeElement) throws UnsupportedTypeException {
        String simpleName = typeElement.getSimpleName().toString();
        String packageName = getEnclosingPackageName(typeElement);
        if (typeElement.getEnclosingElement().getKind() == ElementKind.PACKAGE) {
            if (packageName != null) {
                return new QualifiedName(simpleName, packageName);
            }
            else {
                return new QualifiedName(simpleName);
            }
        }
        else if (ENCLOSING_TYPE_KINDS.contains(typeElement.getEnclosingElement().getKind())) {
            QualifiedName enclosingTypeName = getTypeQualifiedName(typeElement.getEnclosingElement());
            if (packageName != null) {
                return new QualifiedName(simpleName, packageName, enclosingTypeName);
            }
            else {
                return new QualifiedName(simpleName, enclosingTypeName);
            }
        }
        else {
            throw new UnsupportedTypeException("Unsupported type nesting", typeElement.asType());
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
