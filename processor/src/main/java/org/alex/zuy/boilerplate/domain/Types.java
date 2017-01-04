package org.alex.zuy.boilerplate.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.alex.zuy.boilerplate.utils.ObjectsUtil;

public final class Types {

    private static final VoidType VOID_TYPE = new VoidTypeImpl();

    private Types() { }

    public static VoidType makeVoidType() {
        return VOID_TYPE;
    }

    public static ExactType makeExactType(String name) {
        return new ExactTypeImpl(name);
    }

    public static ArrayType makeArrayType(Type<?> elementType) {
        return new ArrayTypeImpl(elementType);
    }

    public static TypeInstance makeTypeInstance(String name, List<Type<?>> parameters) {
        return new TypeInstanceImpl(name, parameters);
    }

    public static TypeParameter makeTypeParameter(String name) {
        return new TypeParameterImpl(name);
    }

    public interface Type<T extends Type<T>> {

        String getName();

        TypeKinds<T> getKind();

        boolean isOfKind(TypeKinds<?> kind);

        <S extends Type<S>> S getAs(TypeKinds<S> kind);
    }

    public interface VoidType extends Type<VoidType> {

    }

    public interface ExactType extends Type<ExactType> {

    }

    public interface ArrayType extends Type<ArrayType> {

        Type<?> getElementType();
    }

    public interface TypeInstance extends Type<TypeInstance> {

        List<Type<?>> getParameters();
    }

    public interface TypeParameter extends Type<TypeParameter> {

    }

    public static final class TypeKinds<T extends Type<T>> {

        public static final TypeKinds<VoidType> VOID_TYPE = new TypeKinds<>("VOID_TYPE");

        public static final TypeKinds<ExactType> EXACT_TYPE = new TypeKinds<>("EXACT_TYPE");

        public static final TypeKinds<ArrayType> ARRAY_TYPE = new TypeKinds<>("ARRAY_TYPE");

        public static final TypeKinds<TypeInstance> TYPE_INSTANCE = new TypeKinds<>("TYPE_INSTANCE");

        public static final TypeKinds<TypeParameter> TYPE_PARAMETER = new TypeKinds<>("TYPE_PARAMETER");

        private String typeName;

        private TypeKinds(String typeName) {
            this.typeName = typeName;
        }

        @Override
        public String toString() {
            return String.format("%s(%s)", getClass().getSimpleName(), typeName);
        }

        public static List<TypeKinds<? extends Type>> kinds() {
            return Arrays.asList(EXACT_TYPE, ARRAY_TYPE, TYPE_INSTANCE, TYPE_PARAMETER);
        }
    }

    private abstract static class AbstractType<T extends Type<T>> implements Type<T> {

        private final String name;

        private final TypeKinds<T> kind;

        private AbstractType(String name, TypeKinds<T> kind) {
            this.name = name;
            this.kind = kind;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public TypeKinds<T> getKind() {
            return kind;
        }

        @Override
        public boolean isOfKind(TypeKinds<?> otherKind) {
            return this.kind.equals(otherKind);
        }

        @Override
        public <S extends Type<S>> S getAs(TypeKinds<S> otherKind) {
            return (S) this;
        }

        @Override
        public boolean equals(Object other) {
            return ObjectsUtil.equals(this, other,
                (lhs, rhs) -> Objects.equals(lhs.kind, rhs.kind) && Objects.equals(lhs.name, rhs.name));
        }

        @Override
        public int hashCode() {
            return Objects.hash(kind, name);
        }

        @Override
        public String toString() {
            return String.format("%s(name=%s, kind=%s)", getClass().getSimpleName(), name, kind);
        }
    }

    private static final class VoidTypeImpl extends AbstractType<VoidType> implements VoidType {

        private static final String VOID_TYPE_NAME = "void";

        private VoidTypeImpl() {
            super(VOID_TYPE_NAME, TypeKinds.VOID_TYPE);
        }
    }

    private static final class ExactTypeImpl extends AbstractType<ExactType> implements ExactType {

        private ExactTypeImpl(String name) {
            super(name, TypeKinds.EXACT_TYPE);
        }
    }

    private static final class ArrayTypeImpl extends AbstractType<ArrayType> implements ArrayType {

        private static final String ARRAY_TYPE_NAME = "array";

        private Type<?> elementType;

        private ArrayTypeImpl(Type<?> elementType) {
            super(ARRAY_TYPE_NAME, TypeKinds.ARRAY_TYPE);
            this.elementType = elementType;
        }

        @Override
        public Type<?> getElementType() {
            return elementType;
        }

        @Override
        public boolean equals(Object other) {
            return ObjectsUtil.equals(this, other, (lhs, rhs) ->
                Objects.equals(lhs.getName(), rhs.getName())
                    && Objects.equals(lhs.getKind(), rhs.getKind())
                    && Objects.equals(lhs.elementType, rhs.elementType));
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getKind(), elementType);
        }

        @Override
        public String toString() {
            return String.format("%s(name=%s, kind=%s, elementType=%s)", getClass().getSimpleName(), getName(),
                getKind(), elementType);
        }
    }

    private static final class TypeInstanceImpl extends AbstractType<TypeInstance> implements TypeInstance {

        private List<Type<?>> parameters;

        private TypeInstanceImpl(String name, List<Type<?>> parameters) {
            super(name, TypeKinds.TYPE_INSTANCE);
            this.parameters = Collections.unmodifiableList(new ArrayList<>(parameters));
        }

        @Override
        public List<Type<?>> getParameters() {
            return parameters;
        }

        @Override
        public boolean equals(Object other) {
            return ObjectsUtil.equals(this, other, (lhs, rhs) ->
                Objects.equals(lhs.getName(), rhs.getName())
                    && Objects.equals(lhs.getKind(), rhs.getKind())
                    && Objects.equals(lhs.parameters, rhs.parameters));
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getKind(), parameters);
        }

        @Override
        public String toString() {
            return String.format("%s(name=%s, kind=%s, parameters=%s)", getClass().getSimpleName(), getName(),
                getKind(), parameters);
        }
    }

    private static final class TypeParameterImpl extends AbstractType<TypeParameter> implements TypeParameter {

        private TypeParameterImpl(String name) {
            super(name, TypeKinds.TYPE_PARAMETER);
        }
    }
}
