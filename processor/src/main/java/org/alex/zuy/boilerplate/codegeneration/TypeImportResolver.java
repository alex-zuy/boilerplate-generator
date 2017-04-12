package org.alex.zuy.boilerplate.codegeneration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.alex.zuy.boilerplate.domain.QualifiedName;

class TypeImportResolver {

    private static final String PACKAGE_NAME_JAVA_LANG = "java.lang";

    private Map<String, QualifiedName> simpleNameToImportedType = new HashMap<>();

    public Collection<QualifiedName> getImportedTypes() {
        return simpleNameToImportedType.values();
    }

    public String resolveTypeReference(QualifiedName name) {
        if (shouldBeImported(name)) {
            QualifiedName importedType = simpleNameToImportedType.get(name.getSimpleName());
            if (importedType != null) {
                if (importedType.equals(name)) {
                    return name.getSimpleName();
                }
                else {
                    return name.asString();
                }
            }
            else {
                simpleNameToImportedType.put(name.getSimpleName(), name);
                return name.getSimpleName();
            }
        }
        else {
            return name.getSimpleName();
        }
    }

    private boolean shouldBeImported(QualifiedName name) {
        return name.getPackageName().filter(packageName -> !packageName.equals(PACKAGE_NAME_JAVA_LANG)).isPresent();
    }
}
