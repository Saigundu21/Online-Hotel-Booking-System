package com.booking.hotel.dao;

import com.booking.hotel.model.*;
import com.booking.hotel.util.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookingDAOImpl implements BookingDAO {

    private static final Logger logger =
            Logger.getLogger(BookingDAOImpl.class.getName());

    private static final String INSERT_SQL =
            "INSERT INTO booking (user_id, hotel_id, room_id, check_in_date, " +
                    "check_out_date, guests, total_amount, booking_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT booking_id, user_id, hotel_id, room_id, check_in_date, " +
                    "check_out_date, guests, total_amount, booking_status " +
                    "FROM booking WHERE booking_id = ?";

    private static final String SELECT_BY_USER_SQL =
            "SELECT booking_id, user_id, hotel_id, room_id, check_in_date, " +
                    "check_out_date, guests, total_amount, booking_status " +
                    "FROM booking WHERE user_id = ?";

    private static final String UPDATE_SQL =
            "UPDATE booking SET user_id = ?, hotel_id = ?, room_id = ?, " +
                    "check_in_date = ?, check_out_date = ?, guests = ?, " +
                    "total_amount = ?, booking_status = ? WHERE booking_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM booking WHERE booking_id = ?";

    @Override
    public boolean create(Booking booking) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, booking.getUser().getUserId());
            stmt.setLong(2, booking.getHotel().getHotelId());
            stmt.setLong(3, booking.getRoom().getRoomId());
            stmt.setDate(4, booking.getCheckInDate());
            stmt.setDate(5, booking.getCheckOutDate());
            stmt.setInt(6, booking.getGuests());
            stmt.setBigDecimal(7, booking.getTotalAmount());
            stmt.setString(8, booking.getBookingStatus());

            logger.fine("Creating booking for user id="
                    + booking.getUser().getUserId());

            int rows = stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {

                if (keys.next()) {
                    booking.setBookingId(keys.getLong(1));
                }
            }

            if (rows > 0) {
                logger.info("Inserted booking id="
                        + booking.getBookingId());
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to insert booking", e);
            throw e;
        }
    }

    @Override
    public Booking findById(long bookingId) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setLong(1, bookingId);

            logger.fine("Finding booking id=" + bookingId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapRow(rs);
                }
            }

            logger.warning("No booking found for id=" + bookingId);
            return null;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to find booking id=" + bookingId, e);
            throw e;
        }
    }

    @Override
    public List<Booking> findByUserId(long userId) throws SQLException {

        List<Booking> list = new ArrayList<>();

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_USER_SQL)) {

            stmt.setLong(1, userId);

            logger.fine("Finding bookings for user id=" + userId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

            if (list.isEmpty()) {
                logger.warning(
                        "No bookings found for user id=" + userId);
            }

            return list;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to find bookings for user id="
                            + userId, e);
            throw e;
        }
    }

    @Override
    public boolean update(Booking booking) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(UPDATE_SQL)) {

            stmt.setLong(1, booking.getUser().getUserId());
            stmt.setLong(2, booking.getHotel().getHotelId());
            stmt.setLong(3, booking.getRoom().getRoomId());
            stmt.setDate(4, booking.getCheckInDate());
            stmt.setDate(5, booking.getCheckOutDate());
            stmt.setInt(6, booking.getGuests());
            stmt.setBigDecimal(7, booking.getTotalAmount());
            stmt.setString(8, booking.getBookingStatus());
            stmt.setLong(9, booking.getBookingId());

            logger.fine("Updating booking id="
                    + booking.getBookingId());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("Updated booking id="
                        + booking.getBookingId());
            } else {
                logger.warning("No booking updated for id="
                        + booking.getBookingId());
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to update booking id="
                            + booking.getBookingId(), e);
            throw e;
        }
    }

    @Override
    public boolean delete(long bookingId) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(DELETE_SQL)) {

            stmt.setLong(1, bookingId);

            logger.fine("Deleting booking id=" + bookingId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("Deleted booking id=" + bookingId);
            } else {
                logger.warning(
                        "No booking found to delete for id="
                                + bookingId);
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to delete booking id=" + bookingId, e);
            throw e;
        }
    }

    private Booking mapRow(ResultSet rs) throws SQLException {

        Booking b = new Booking();

        b.setBookingId(rs.getLong("booking_id"));

        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        b.setUser(user);

        Hotel hotel = new Hotel();
        hotel.setHotelId(rs.getLong("hotel_id"));
        b.setHotel(hotel);

        Room room = new Room();
        room.setRoomId(rs.getLong("room_id"));
        b.setRoom(room);

        b.setCheckInDate(rs.getDate("check_in_date"));
        b.setCheckOutDate(rs.getDate("check_out_date"));
        b.setGuests(rs.getInt("guests"));
        b.setTotalAmount(rs.getBigDecimal("total_amount"));
        b.setBookingStatus(rs.getString("booking_status"));

        return b;
    }
}
