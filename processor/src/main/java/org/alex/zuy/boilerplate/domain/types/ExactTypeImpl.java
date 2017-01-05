package org.alex.zuy.boilerplate.domain.types;

final class ExactTypeImpl extends AbstractType<ExactType> implements ExactType {

    ExactTypeImpl(String name) {
        super(name, TypeKinds.EXACT_TYPE);
    }
}
