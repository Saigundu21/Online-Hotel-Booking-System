package com.booking.hotel.model;

public class HotelImage {

    private long imageId;
    private Hotel hotel; //using FK
    private String imageUrl;
    private String caption;
    private int displayOrder;

    public HotelImage() {
    }

    public HotelImage(long imageId, Hotel hotel,
                      String imageUrl, String caption,int displayOrder) {
        this.imageId = imageId;
        this.hotel = hotel;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.displayOrder = displayOrder;

    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;}
    // <-- 3. Add these getter and setter methods
    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    } }


