package com.booking.hotel.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment {

    private long paymentId;
    private Booking booking;  // using FK
    private BigDecimal amount;
    private String paymentStatus;
    private String transactionRef;
    private Timestamp paidAt;

    public Payment() {
    }

    public Payment(long paymentId, Booking booking, BigDecimal amount,
                   String paymentStatus, String transactionRef,
                   Timestamp paidAt) {
        this.paymentId = paymentId;
        this.booking = booking;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.transactionRef = transactionRef;
        this.paidAt = paidAt;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public Timestamp getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }public <__TMP__> __TMP__ getPaymentMethod() {
        return null;
    } }
