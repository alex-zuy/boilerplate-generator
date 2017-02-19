package org.alex.zuy.boilerplate.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

import org.alex.zuy.boilerplate.domain.types.Type;
import org.alex.zuy.boilerplate.domain.types.Types;
import org.alex.zuy.boilerplate.services.ProcessorContext;
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
                        .map(Types::makeExactType)
                        .orElseThrow(() -> new IllegalArgumentException(
                            String.format("Unknown primitive type '%s'", typeMirror)));
                }
                else {
                    throw new UnsupportedTypeException(typeMirror);
                }
        }
    }

    private Type<?> analyseTypeVariable(TypeVariable variable) {
        return Types.makeTypeParameter(variable.asElement().getSimpleName().toString());
    }

    private Type<?> analyseDeclaredType(DeclaredType declaredType) throws UnsupportedTypeException {
        TypeElement typeElement = (TypeElement) processorContext.getTypeUtils().asElement(declaredType);
        String qualifiedTypeName = typeElement.getQualifiedName().toString();

        if (isGenericType(declaredType)) {
            List<Type<?>> typeArguments = new ArrayList<>();
            for (TypeMirror argument : declaredType.getTypeArguments()) {
                typeArguments.add(analyse(argument));
            }
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
