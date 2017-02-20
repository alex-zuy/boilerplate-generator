package org.alex.zuy.boilerplate.test;

import org.alex.zuy.boilerplate.IncludeMarker;

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
