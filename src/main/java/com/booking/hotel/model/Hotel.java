package com.booking.hotel.model;

import java.math.BigDecimal;

public class Hotel {

    private long hotelId;
    private Location location;
    private String name;
    private String description;
    private String address;
    private BigDecimal starRating;
    private String amenities;
    private String status;

    public Hotel() {
    }

    public Hotel(long hotelId, Location location, String name,
                 String description, String address,
                 BigDecimal starRating, String amenities,
                 String status) {
        this.hotelId = hotelId;
        this.location = location;
        this.name = name;
        this.description = description;
        this.address = address;
        this.starRating = starRating;
        this.amenities = amenities;
        this.status = status;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getStarRating() {
        return starRating;
    }

    public void setStarRating(BigDecimal starRating) {
        this.starRating = starRating;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}



