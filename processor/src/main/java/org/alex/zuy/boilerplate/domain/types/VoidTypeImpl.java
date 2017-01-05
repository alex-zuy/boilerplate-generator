package org.alex.zuy.boilerplate.domain.types;

final class VoidTypeImpl extends AbstractType<VoidType> implements VoidType {

    private static final String VOID_TYPE_NAME = "void";

    VoidTypeImpl() {
        super(VOID_TYPE_NAME, TypeKinds.VOID_TYPE);
    }
}
