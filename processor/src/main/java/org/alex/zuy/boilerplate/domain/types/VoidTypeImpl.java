package org.alex.zuy.boilerplate.domain.types;

final class VoidTypeImpl extends AbstractType<Types.VoidType> implements Types.VoidType {

    private static final String VOID_TYPE_NAME = "void";

    VoidTypeImpl() {
        super(VOID_TYPE_NAME, Types.TypeKinds.VOID_TYPE);
    }
}
