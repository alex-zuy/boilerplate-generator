package org.alex.zuy.boilerplate.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.alex.zuy.boilerplate.utils.ObjectsUtil;

public class BeanClass {

    private Types.Type<?> type;

    private Map<String, BeanProperty> properties;

    public BeanClass(Types.Type<?> type, List<BeanProperty> properties) {
        this.type = type;
        this.properties = Collections.unmodifiableMap(convertToMap(properties));
    }

    public Types.Type<?> getType() {
        return type;
    }

    public Map<String, BeanProperty> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object other) {
        return ObjectsUtil.equals(this, other, (lhs, rhs) -> lhs.type.equals(rhs.type));
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s(type=%s)", getClass().getSimpleName(), type);
    }

    private static Map<String, BeanProperty> convertToMap(List<BeanProperty> properties) {
        return properties.stream()
            .collect(Collectors.toMap(BeanProperty::getName, Function.identity(), (oneProperty, anotherProperty) -> {
                throw new IllegalArgumentException(String.format("Duplicated property \"%s\"", oneProperty.getName()));
            }, HashMap::new));
    }
}
