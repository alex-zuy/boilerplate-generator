package com.github.alex.zuy.boilerplate.sourcemodel;

import java.util.List;

import com.github.alex.zuy.boilerplate.Nullable;
import com.github.alex.zuy.boilerplate.domain.types.Type;
import org.immutables.value.Value;

@Value.Immutable
public interface TypeDescription extends AbstractDescription {

    String getSimpleName();

    @Nullable
    String getPackageName();

    String getQualifiedName();

    TypeKind getKind();

    @Nullable
    Type<?> getExtendedType();

    List<Type<?>> getImplementedTypes();

    List<FieldDescription> getFields();

    List<MethodDescription> getMethods();

    List<TypeDescription> getNestedTypes();
}
