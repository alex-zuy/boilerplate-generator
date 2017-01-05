package org.alex.zuy.boilerplate.domain.types;

final class TypeParameterImpl extends AbstractType<TypeParameter> implements TypeParameter {

    TypeParameterImpl(String name) {
        super(name, TypeKinds.TYPE_PARAMETER);
    }
}
