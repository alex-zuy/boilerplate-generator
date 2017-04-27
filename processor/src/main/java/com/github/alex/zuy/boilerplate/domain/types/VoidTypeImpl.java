package com.github.alex.zuy.boilerplate.domain.types;

import com.github.alex.zuy.boilerplate.domain.QualifiedName;

final class VoidTypeImpl extends AbstractType<VoidType> implements VoidType {

    private static final String VOID_TYPE_NAME = "void";

    VoidTypeImpl() {
        super(new QualifiedName(VOID_TYPE_NAME), TypeKinds.VOID_TYPE);
    }
}
