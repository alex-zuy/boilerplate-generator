package com.github.alex.zuy.boilerplate.boilerplate.test;

import com.github.alex.zuy.boilerplate.boilerplate.IncludeMarker;

@IncludeMarker
public class Address {

    private String streetName;

    private int buildingNumber;

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(int buildingNumber) {
        this.buildingNumber = buildingNumber;
    }
}
