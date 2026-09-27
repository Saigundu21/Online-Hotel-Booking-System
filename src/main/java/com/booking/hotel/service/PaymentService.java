package com.booking.hotel.service;

import com.booking.hotel.model.Payment;
import java.sql.SQLException;
import java.util.List;

public interface PaymentService {
    boolean processPayment(Payment payment) throws SQLException;
    Payment getPaymentById(long paymentId) throws SQLException;
    Payment getPaymentByBookingId(long bookingId) throws SQLException;
    List<Payment> getPaymentsByBookingIdList(long bookingId) throws SQLException;
    boolean updatePayment(Payment payment) throws SQLException;
    boolean refundPayment(long paymentId) throws SQLException;
}

