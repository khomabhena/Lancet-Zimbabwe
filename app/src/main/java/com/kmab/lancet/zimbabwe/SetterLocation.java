package com.kmab.lancet.zimbabwe;

public class SetterLocation {

    public SetterLocation() {
    }

    private String key, building, address, city;

    public SetterLocation(String key, String building, String address, String city) {
        this.key = key;
        this.building = building;
        this.address = address;
        this.city = city;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
