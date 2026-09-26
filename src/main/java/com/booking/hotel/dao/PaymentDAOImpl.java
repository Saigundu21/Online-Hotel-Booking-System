package com.booking.hotel.dao;

import com.booking.hotel.model.Booking;
import com.booking.hotel.model.Payment;
import com.booking.hotel.util.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentDAOImpl implements PaymentDAO {

    private static final Logger logger =
            Logger.getLogger(PaymentDAOImpl.class.getName());

    private static final String INSERT_SQL =
            "INSERT INTO payment (booking_id, amount, payment_status, " +
                    "transaction_ref, paid_at) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT payment_id, booking_id, amount, payment_status, " +
                    "transaction_ref, paid_at FROM payment WHERE payment_id = ?";

    private static final String SELECT_BY_BOOKING_SQL =
            "SELECT payment_id, booking_id, amount, payment_status, " +
                    "transaction_ref, paid_at FROM payment WHERE booking_id = ?";

    private static final String UPDATE_SQL =
            "UPDATE payment SET booking_id = ?, amount = ?, " +
                    "payment_status = ?, transaction_ref = ?, paid_at = ? " +
                    "WHERE payment_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM payment WHERE payment_id = ?";

    @Override
    public boolean create(Payment payment) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, payment.getBooking().getBookingId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentStatus());
            stmt.setString(4, payment.getTransactionRef());
            stmt.setTimestamp(5, payment.getPaidAt());

            logger.fine("Creating payment for booking id="
                    + payment.getBooking().getBookingId());

            int rows = stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {

                if (keys.next()) {
                    payment.setPaymentId(keys.getLong(1));
                }
            }

            if (rows > 0) {
                logger.info("Inserted payment id="
                        + payment.getPaymentId());
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to insert payment", e);
            throw e;
        }
    }

    @Override
    public Payment findById(long paymentId) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setLong(1, paymentId);

            logger.fine("Finding payment id=" + paymentId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapRow(rs);
                }
            }

            logger.warning("No payment found for id=" + paymentId);
            return null;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to find payment id=" + paymentId, e);
            throw e;
        }
    }

    @Override
    public List<Payment> findByBookingId(long bookingId)
            throws SQLException {

        List<Payment> list = new ArrayList<>();

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_BOOKING_SQL)) {

            stmt.setLong(1, bookingId);

            logger.fine("Finding payments for booking id="
                    + bookingId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

            if (list.isEmpty()) {
                logger.warning(
                        "No payments found for booking id="
                                + bookingId);
            }

            return list;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to find payments for booking id="
                            + bookingId, e);
            throw e;
        }
    }

    @Override
    public boolean update(Payment payment) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(UPDATE_SQL)) {

            stmt.setLong(1, payment.getBooking().getBookingId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentStatus());
            stmt.setString(4, payment.getTransactionRef());
            stmt.setTimestamp(5, payment.getPaidAt());
            stmt.setLong(6, payment.getPaymentId());

            logger.fine("Updating payment id="
                    + payment.getPaymentId());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("Updated payment id="
                        + payment.getPaymentId());
            } else {
                logger.warning("No payment updated for id="
                        + payment.getPaymentId());
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to update payment id="
                            + payment.getPaymentId(), e);
            throw e;
        }
    }

    @Override
    public boolean delete(long paymentId) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(DELETE_SQL)) {

            stmt.setLong(1, paymentId);

            logger.fine("Deleting payment id=" + paymentId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("Deleted payment id=" + paymentId);
            } else {
                logger.warning(
                        "No payment found to delete for id="
                                + paymentId);
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to delete payment id=" + paymentId, e);
            throw e;
        }
    }

    private Payment mapRow(ResultSet rs) throws SQLException {

        Payment p = new Payment();

        p.setPaymentId(rs.getLong("payment_id"));

        Booking b = new Booking();
        b.setBookingId(rs.getLong("booking_id"));
        p.setBooking(b);

        p.setAmount(rs.getBigDecimal("amount"));
        p.setPaymentStatus(rs.getString("payment_status"));
        p.setTransactionRef(rs.getString("transaction_ref"));
        p.setPaidAt(rs.getTimestamp("paid_at"));

        return p;
    }
}
