package org.alex.zuy.boilerplate.beans;

import org.alex.zuy.boilerplate.IncludeMarker;

@IncludeMarker
public class User extends BaseEntity<String> {

    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
