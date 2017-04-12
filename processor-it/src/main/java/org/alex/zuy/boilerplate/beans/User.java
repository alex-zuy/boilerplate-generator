package org.alex.zuy.boilerplate.beans;

import org.alex.zuy.boilerplate.IncludeMarker;
import org.alex.zuy.boilerplate.test.Address;

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
