package com.github.alex.zuy.boilerplate.boilerplate.beans;

public class BaseEntity<T> {

    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
