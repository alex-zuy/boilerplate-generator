package org.alex.zuy.boilerplate.domain.types;

final class ExactTypeImpl extends AbstractType<Types.ExactType> implements Types.ExactType {

    ExactTypeImpl(String name) {
        super(name, Types.TypeKinds.EXACT_TYPE);
    }
}
