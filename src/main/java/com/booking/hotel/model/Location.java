package com.booking.hotel.model;

public class Location {

    private long locationId;
    private String name;
    private String type;
    private Location parent;  //using FK

    public Location() {
    }

    public Location(long locationId, String name,
                    String type, Location parent) {
        this.locationId = locationId;
        this.name = name;
        this.type = type;
        this.parent = parent;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Location getParent() {
        return parent;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }
}