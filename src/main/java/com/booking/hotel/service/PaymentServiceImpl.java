package com.booking.hotel.service;

import com.booking.hotel.dao.PaymentDAO;
import com.booking.hotel.dao.PaymentDAOImpl;
import com.booking.hotel.model.Payment;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = Logger.getLogger(PaymentServiceImpl.class.getName());
    private final PaymentDAO paymentDAO;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAOImpl();
    }

    public PaymentServiceImpl(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @Override
    public boolean processPayment(Payment payment) throws SQLException {
        try {
            if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Payment amount must be greater than zero.");
            }
            if (payment.getBooking() == null || payment.getBooking().getBookingId() <= 0) {
                throw new IllegalArgumentException("A valid booking reference is required for payment.");
            }

            logger.info("Service: Processing payment for booking ID: " + payment.getBooking().getBookingId());
            return paymentDAO.create(payment);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to process payment", e);
            throw e;
        }
    }

    @Override
    public Payment getPaymentById(long paymentId) throws SQLException {
        try {
            logger.info("Service: Fetching payment by ID: " + paymentId);
            return paymentDAO.findById(paymentId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to find payment ID: " + paymentId, e);
            throw e;
        }
    }

    @Override
    public Payment getPaymentByBookingId(long bookingId) throws SQLException {
        try {
            logger.info("Service: Fetching payment for booking ID: " + bookingId);
            return paymentDAO.findByBooking(bookingId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to find payment for booking ID: " + bookingId, e);
            throw e;
        }
    }

    @Override
    public List<Payment> getPaymentsByBookingIdList(long bookingId) throws SQLException {
        try {
            logger.info("Service: Fetching payment list for booking ID: " + bookingId);
            return paymentDAO.findByBookingId(bookingId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to retrieve payments list for booking ID: " + bookingId, e);
            throw e;
        }
    }

    @Override
    public boolean updatePayment(Payment payment) throws SQLException {
        try {
            if (payment.getPaymentId() <= 0) {
                throw new IllegalArgumentException("Invalid payment ID for update.");
            }
            logger.info("Service: Updating payment ID: " + payment.getPaymentId());
            return paymentDAO.update(payment);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to update payment ID: " + payment.getPaymentId(), e);
            throw e;
        }
    }

    @Override
    public boolean refundPayment(long paymentId) throws SQLException {
        try {
            Payment payment = paymentDAO.findById(paymentId);
            if (payment == null) {
                throw new IllegalArgumentException("Payment not found for ID: " + paymentId);
            }
            payment.setPaymentStatus("REFUNDED");
            logger.info("Service: Refunding payment ID: " + paymentId);
            return paymentDAO.update(payment);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to refund payment ID: " + paymentId, e);
            throw e;
        }
    }
}
