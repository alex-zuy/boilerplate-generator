package org.alex.zuy.boilerplate.domain;

import java.util.Objects;
import java.util.Optional;

import org.alex.zuy.boilerplate.utils.ObjectsUtil;

public class QualifiedName {

    private static final String DELIMITER_DOT = ".";

    private String simpleName;

    private QualifiedName enclosingType;

    private String packageName;

    public QualifiedName(String simpleName) {
        this.simpleName = simpleName;
    }

    public QualifiedName(String simpleName, String packageName) {
        this.simpleName = simpleName;
        this.packageName = packageName;
    }

    public QualifiedName(String simpleName, String packageName, QualifiedName enclosingType) {
        this(simpleName, packageName);
        this.enclosingType = enclosingType;
    }

    public QualifiedName(String simpleName, String packageName, String enclosingTypeSimpleName) {
        this(simpleName, packageName, new QualifiedName(enclosingTypeSimpleName, packageName));
    }

    public String getSimpleName() {
        return simpleName;
    }

    public Optional<QualifiedName> getEnclosingType() {
        return Optional.ofNullable(enclosingType);
    }

    public Optional<String> getPackageName() {
        return Optional.ofNullable(packageName);
    }

    public String asString() {
        if (enclosingType != null) {
            return String.join(DELIMITER_DOT, enclosingType.asString(), simpleName);
        }
        else {
            return packageName != null ? String.join(DELIMITER_DOT, packageName, simpleName) : simpleName;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(simpleName, enclosingType, packageName);
    }

    @Override
    public boolean equals(Object o) {
        return ObjectsUtil.equals(this, o, (lhs, rhs) ->
            Objects.equals(lhs.simpleName, rhs.simpleName)
                && Objects.equals(lhs.enclosingType, rhs.enclosingType)
                && Objects.equals(lhs.packageName, rhs.packageName));
    }

    @Override
    public String toString() {
        String enclosingTypeSimpleName = enclosingType != null ? enclosingType.getSimpleName() : null;
        return String.format("%s(simpleName=%s,packageName=%s,enclosingType=%s)", getClass().getSimpleName(),
            simpleName, packageName, enclosingTypeSimpleName);
    }
}
