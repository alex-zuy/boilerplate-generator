package org.alex.zuy.boilerplate.sourcemodel;

import org.alex.zuy.boilerplate.domain.types.Type;
import org.immutables.value.Value;

@Value.Immutable
public interface FieldDescription extends AbstractDescription, TemplateDescription {

    String getName();

    Type<?> getType();

    boolean hasInitialValue();
}
