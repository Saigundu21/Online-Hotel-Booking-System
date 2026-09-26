package com.booking.hotel.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Booking {

    private long bookingId;
    private User user;  // using Fk
    private Hotel hotel;  // using FK
    private Room room; // USING FK
    private Date checkInDate;
    private Date checkOutDate;
    private int guests;
    private BigDecimal totalAmount;
    private String bookingStatus;

    public Booking() {
    }

    public Booking(long bookingId, User user, Hotel hotel, Room room,
                   Date checkInDate, Date checkOutDate,
                   int guests, BigDecimal totalAmount,
                   String bookingStatus) {
        this.bookingId = bookingId;
        this.user = user;
        this.hotel = hotel;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guests = guests;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public void setPaymentOption(String card) {}public <__TMP__> __TMP__ getPaymentOption() {
        return null;
    }}
