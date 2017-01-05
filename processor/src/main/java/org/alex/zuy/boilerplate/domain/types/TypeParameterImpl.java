package org.alex.zuy.boilerplate.domain.types;

final class TypeParameterImpl extends AbstractType<Types.TypeParameter> implements Types.TypeParameter {

    TypeParameterImpl(String name) {
        super(name, Types.TypeKinds.TYPE_PARAMETER);
    }
}
