package com.booking.hotel.dao;

import com.booking.hotel.model.Booking;
import java.sql.SQLException;
import java.util.List;

public interface BookingDAO {
    boolean create(Booking booking) throws SQLException;
    Booking findById(long bookingId) throws SQLException;
    List<Booking> findByUserId(long userId) throws SQLException;
    boolean update(Booking booking) throws SQLException;
    boolean delete(long bookingId) throws SQLException;

    <__TMP__> __TMP__ findByUser(long userId);

    boolean updateStatus(long createdBookingId, String cancelled);

}




