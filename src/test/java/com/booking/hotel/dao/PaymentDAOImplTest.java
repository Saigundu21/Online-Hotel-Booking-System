package com.booking.hotel.dao;

import com.booking.hotel.model.Booking;
import com.booking.hotel.model.Payment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDAOImplTest {

    private PaymentDAO paymentDAO;
    private long createdPaymentId;

    @BeforeEach
    void setUp() {
        paymentDAO = new PaymentDAOImpl();
        createdPaymentId = 0;
    }

    @AfterEach
    void deleteTestPayment() {

        if (createdPaymentId <= 0) {
            return;
        }

        try {
            paymentDAO.delete(createdPaymentId);
        } catch (SQLException ignored) {
        }
    }

    @Test
    void createInsertsPaymentAndSetsGeneratedId() throws SQLException {

        Payment payment = newTestPayment();

        boolean created = paymentDAO.create(payment);
        createdPaymentId = payment.getPaymentId();

        assertTrue(created);
        assertTrue(createdPaymentId > 0);
    }

    @Test
    void findByIdReturnsInsertedPayment() throws SQLException {

        Payment payment = newTestPayment();

        paymentDAO.create(payment);
        createdPaymentId = payment.getPaymentId();

        Payment found = paymentDAO.findById(createdPaymentId);

        assertNotNull(found);
        assertEquals(createdPaymentId, found.getPaymentId());
        assertEquals(payment.getAmount(), found.getAmount());
        assertEquals(payment.getPaymentStatus(), found.getPaymentStatus());
        assertEquals(payment.getTransactionRef(), found.getTransactionRef());
    }

    @Test
    void findByBookingIdReturnsPayments() throws SQLException {

        Payment payment = newTestPayment();

        paymentDAO.create(payment);
        createdPaymentId = payment.getPaymentId();

        long bookingId = payment.getBooking().getBookingId();

        var payments = paymentDAO.findByBookingId(bookingId);

        assertNotNull(payments);

        assertTrue(
                payments.stream()
                        .anyMatch(p -> p.getPaymentId() == createdPaymentId)
        );
    }

    @Test
    void updateChangesPaymentDetails() throws SQLException {

        Payment payment = newTestPayment();

        paymentDAO.create(payment);
        createdPaymentId = payment.getPaymentId();

        payment.setAmount(new BigDecimal("2500.00"));
        payment.setPaymentStatus("SUCCESS");

        assertTrue(paymentDAO.update(payment));

        Payment updated = paymentDAO.findById(createdPaymentId);

        assertNotNull(updated);
        assertEquals(new BigDecimal("2500.00"), updated.getAmount());
        assertEquals("SUCCESS", updated.getPaymentStatus());
    }

    @Test
    void deleteRemovesPayment() throws SQLException {

        Payment payment = newTestPayment();

        paymentDAO.create(payment);
        createdPaymentId = payment.getPaymentId();

        assertTrue(paymentDAO.delete(createdPaymentId));

        assertNull(paymentDAO.findById(createdPaymentId));

        createdPaymentId = 0;
    }

    private Payment newTestPayment() {

        Booking booking = new Booking();

        // Use an existing booking ID.
        booking.setBookingId(1);

        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmount(new BigDecimal("2000.00"));
        payment.setPaymentStatus("PENDING");
        payment.setTransactionRef(
                "TEST-" + System.currentTimeMillis()
        );
        payment.setPaidAt(new Timestamp(System.currentTimeMillis()));

        return payment;
    }
}
