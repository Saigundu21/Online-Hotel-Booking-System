package com.booking.hotel.service;

import com.booking.hotel.model.Booking;
import java.sql.SQLException;
import java.util.List;

public interface BookingService {
    boolean createBooking(Booking booking) throws SQLException;
    Booking getBookingById(long bookingId) throws SQLException;
    List<Booking> getBookingsByUserId(long userId) throws SQLException;
    boolean updateBookingStatus(long bookingId, String status) throws SQLException;
    boolean cancelBooking(long bookingId) throws SQLException;
}
