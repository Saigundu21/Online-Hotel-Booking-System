package com.booking.hotel.service;

import com.booking.hotel.dao.BookingDAO;
import com.booking.hotel.dao.BookingDAOImpl;
import com.booking.hotel.model.Booking;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookingServiceImpl implements BookingService {

    private static final Logger logger = Logger.getLogger(BookingServiceImpl.class.getName());
    private final BookingDAO bookingDAO;

    public BookingServiceImpl() {
        this.bookingDAO = new BookingDAOImpl();
    }

    public BookingServiceImpl(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    @Override
    public boolean createBooking(Booking booking) throws SQLException {
        try {
            // Business rule: Check-out must be after check-in
            if (booking.getCheckInDate() != null && booking.getCheckOutDate() != null) {
                if (!booking.getCheckOutDate().after(booking.getCheckInDate())) {
                    throw new IllegalArgumentException("Check-out date must be strictly after check-in date.");
                }
            } else {
                throw new IllegalArgumentException("Check-in and Check-out dates cannot be null.");
            }

            if (booking.getGuests() <= 0) {
                throw new IllegalArgumentException("Number of guests must be at least 1.");
            }

            logger.info("Service: Creating booking for user ID: " + booking.getUser().getUserId());
            return bookingDAO.create(booking);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to create booking", e);
            throw e;
        }
    }

    @Override
    public Booking getBookingById(long bookingId) throws SQLException {
        try {
            logger.info("Service: Fetching booking by ID: " + bookingId);
            return bookingDAO.findById(bookingId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to find booking ID: " + bookingId, e);
            throw e;
        }
    }

    @Override
    public List<Booking> getBookingsByUserId(long userId) throws SQLException {
        try {
            logger.info("Service: Fetching bookings for user ID: " + userId);
            return bookingDAO.findByUserId(userId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to find bookings for user ID: " + userId, e);
            throw e;
        }
    }

    @Override
    public boolean updateBookingStatus(long bookingId, String status) throws SQLException {
        try {
            logger.info("Service: Updating status for booking ID " + bookingId + " to " + status);
            return bookingDAO.updateStatus(bookingId, status);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to update booking status", e);
            throw e;
        }
    }

    @Override
    public boolean cancelBooking(long bookingId) throws SQLException {
        return updateBookingStatus(bookingId, "CANCELLED");
    }
}
