package org.alex.zuy.boilerplate.domain.types;

final class ExactTypeImpl extends AbstractType<ExactType> implements ExactType {

    ExactTypeImpl(String name, String packageName) {
        super(name, packageName, TypeKinds.EXACT_TYPE);
    }

    ExactTypeImpl(String name) {
        super(name, TypeKinds.EXACT_TYPE);
    }
}
