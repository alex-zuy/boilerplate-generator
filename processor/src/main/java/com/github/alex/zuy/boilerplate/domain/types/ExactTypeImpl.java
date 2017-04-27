package com.github.alex.zuy.boilerplate.domain.types;

import com.github.alex.zuy.boilerplate.domain.QualifiedName;

final class ExactTypeImpl extends AbstractType<ExactType> implements ExactType {

    ExactTypeImpl(QualifiedName name) {
        super(name, TypeKinds.EXACT_TYPE);
    }
}
