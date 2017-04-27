package com.github.alex.zuy.boilerplate.boilerplate.beans;

import com.github.alex.zuy.boilerplate.boilerplate.IncludeMarker;
import com.github.alex.zuy.boilerplate.boilerplate.test.Address;

@IncludeMarker
public class User extends BaseEntity<String> {

    private String fullName;

    private Address address;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
