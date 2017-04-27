package com.github.alex.zuy.boilerplate.domain.types;

import com.github.alex.zuy.boilerplate.domain.QualifiedName;

final class TypeParameterImpl extends AbstractType<TypeParameter> implements TypeParameter {

    TypeParameterImpl(QualifiedName name) {
        super(name, TypeKinds.TYPE_PARAMETER);
    }
}
