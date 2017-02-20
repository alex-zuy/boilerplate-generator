package org.alex.zuy.boilerplate.beans;

public class BaseEntity<T> {

    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
