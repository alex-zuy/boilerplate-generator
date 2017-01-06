package org.alex.zuy.boilerplate.domain.types;

import java.util.Objects;
import java.util.Optional;

import org.alex.zuy.boilerplate.utils.ObjectsUtil;

abstract class AbstractType<T extends Type<T>> implements Type<T> {

    private final String name;

    private final String packageName;

    private final TypeKinds<T> kind;

    AbstractType(String name, String packageName, TypeKinds<T> kind) {
        this.name = name;
        this.packageName = packageName;
        this.kind = kind;
    }

    AbstractType(String name, TypeKinds<T> kind) {
        this(name, null, kind);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getPackageName() {
        return Optional.ofNullable(packageName);
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
            (lhs, rhs) -> Objects.equals(lhs.kind, rhs.kind) && Objects.equals(lhs.name, rhs.name)
                && Objects.equals(lhs.packageName, rhs.packageName));
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, name, packageName);
    }

    @Override
    public String toString() {
        return String.format("%s(name=%s, kind=%s)", getClass().getSimpleName(), name, kind);
    }
}
