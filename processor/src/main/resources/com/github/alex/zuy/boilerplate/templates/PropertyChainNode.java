package com.example;

public abstract class _PropertyChainNode_ {

    private final _PropertyChainNode_ tail;

    private final String property;

    protected _PropertyChainNode_(String property) {
        this.tail = null;
        this.property = property;
    }

    public _PropertyChainNode_(final _PropertyChainNode_ tail, final String property) {
        this.tail = tail;
        this.property = property;
    }

    public final String path() {
        return buildFullPath(0).toString();
    }

    protected final StringBuilder buildFullPath(int totalLength) {
        if (tail == null) {
            return new StringBuilder(totalLength + property.length() + 1)
                .append(property);
        }
        else {
            return tail.buildFullPath(totalLength + property.length() + 1)
                .append('.').append(property);
        }
    }

    protected final String buildRelativePath(String property) {
        return buildFullPath(property.length() + 1)
            .append('.').append(property)
            .toString();
    }

    @Override
    public final String toString() {
        return path();
    }
}
