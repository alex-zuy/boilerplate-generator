package org.alex.zuy.boilerplate.sourcemodel;

import java.util.List;

import org.alex.zuy.boilerplate.Nullable;
import org.alex.zuy.boilerplate.domain.types.Type;
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
