package com.github.alex.zuy.boilerplate.domain.types;

import java.util.Objects;

import com.github.alex.zuy.boilerplate.domain.QualifiedName;
import com.github.alex.zuy.boilerplate.utils.ObjectsUtil;

abstract class AbstractType<T extends Type<T>> implements Type<T> {

    private final QualifiedName name;

    private final TypeKinds<T> kind;

    AbstractType(QualifiedName name, TypeKinds<T> kind) {
        this.name = name;
        this.kind = kind;
    }

    @Override
    public QualifiedName getName() {
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
