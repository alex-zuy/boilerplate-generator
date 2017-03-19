package org.alex.zuy.boilerplate.codegeneration;

import java.util.List;
import java.util.stream.Collectors;

import org.alex.zuy.boilerplate.domain.types.ArrayType;
import org.alex.zuy.boilerplate.domain.types.ExactType;
import org.alex.zuy.boilerplate.domain.types.Type;
import org.alex.zuy.boilerplate.domain.types.Type.TypeKinds;
import org.alex.zuy.boilerplate.domain.types.TypeInstance;

public class TypeFormatter {

    public String format(Type<?> type) {
        if (type.isOfKind(TypeKinds.EXACT_TYPE)) {
            ExactType exactType = type.getAs(TypeKinds.EXACT_TYPE);
            return exactType.getName().asString();
        }
        else if (type.isOfKind(TypeKinds.ARRAY_TYPE)) {
            ArrayType arrayType = type.getAs(TypeKinds.ARRAY_TYPE);
            return String.format("%s[]", format(arrayType.getElementType()));
        }
        else if (type.isOfKind(TypeKinds.VOID_TYPE)) {
            return type.getName().asString();
        }
        else if (type.isOfKind(TypeKinds.TYPE_INSTANCE)) {
            TypeInstance typeInstance = type.getAs(TypeKinds.TYPE_INSTANCE);
            List<String> formattedParameters = typeInstance.getParameters().stream()
                .map(this::format).collect(Collectors.toList());
            return String.format("%s<%s>", typeInstance.getName().asString(), String.join(",", formattedParameters));
        }
        else {
            throw new IllegalArgumentException(String.format("Unsupported type kind: '%s'", type.getKind()));
        }
    }
}
