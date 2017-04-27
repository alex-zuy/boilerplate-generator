package com.github.alex.zuy.boilerplate.boilerplate.test;

import com.github.alex.zuy.boilerplate.boilerplate.IncludeMarker;

@IncludeMarker
public class Person {

    private String name;

    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
