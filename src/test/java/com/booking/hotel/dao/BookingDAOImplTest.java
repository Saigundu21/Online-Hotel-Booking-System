package com.booking.hotel.dao;

import com.booking.hotel.model.Booking;
import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.Room;
import com.booking.hotel.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class BookingDAOImplTest {

    private BookingDAO bookingDAO;
    private long createdBookingId;

    @BeforeEach
    void setUp() {
        bookingDAO = new BookingDAOImpl();
        createdBookingId = 0;
    }

    @AfterEach
    void deleteTestBooking() {

        if (createdBookingId <= 0) {
            return;
        }

        try {
            bookingDAO.delete(createdBookingId);
        } catch (SQLException ignored) {
        }
    }

    @Test
    void createInsertsBookingAndSetsGeneratedId() throws SQLException {

        Booking booking = newTestBooking();

        boolean created = bookingDAO.create(booking);
        createdBookingId = booking.getBookingId();

        assertTrue(created);
        assertTrue(createdBookingId > 0);
    }

    @Test
    void findByIdReturnsInsertedBooking() throws SQLException {

        Booking booking = newTestBooking();

        bookingDAO.create(booking);
        createdBookingId = booking.getBookingId();

        Booking found = bookingDAO.findById(createdBookingId);

        assertNotNull(found);
        assertEquals(createdBookingId, found.getBookingId());
        assertEquals(booking.getGuests(), found.getGuests());
        assertEquals(
                booking.getTotalAmount(),
                found.getTotalAmount()
        );
        assertEquals(
                booking.getBookingStatus(),
                found.getBookingStatus()
        );
    }

    @Test
    void findByUserIdReturnsBookings() throws SQLException {

        Booking booking = newTestBooking();

        bookingDAO.create(booking);
        createdBookingId = booking.getBookingId();

        long userId = booking.getUser().getUserId();

        var bookings = bookingDAO.findByUserId(userId);

        assertNotNull(bookings);

        assertTrue(
                bookings.stream()
                        .anyMatch(b -> b.getBookingId() == createdBookingId)
        );
    }

    @Test
    void updateChangesBookingDetails() throws SQLException {

        Booking booking = newTestBooking();

        bookingDAO.create(booking);
        createdBookingId = booking.getBookingId();

        booking.setGuests(4);
        booking.setTotalAmount(new BigDecimal("5000.00"));
        booking.setBookingStatus("CONFIRMED");

        assertTrue(bookingDAO.update(booking));

        Booking updated = bookingDAO.findById(createdBookingId);

        assertNotNull(updated);
        assertEquals(4, updated.getGuests());
        assertEquals(
                new BigDecimal("5000.00"),
                updated.getTotalAmount()
        );
        assertEquals("CONFIRMED", updated.getBookingStatus());
    }

    @Test
    void deleteRemovesBooking() throws SQLException {

        Booking booking = newTestBooking();

        bookingDAO.create(booking);
        createdBookingId = booking.getBookingId();

        assertTrue(bookingDAO.delete(createdBookingId));

        assertNull(bookingDAO.findById(createdBookingId));

        createdBookingId = 0;
    }

    private Booking newTestBooking() {

        User user = new User();
        user.setUserId(1);

        Hotel hotel = new Hotel();
        hotel.setHotelId(1);

        Room room = new Room();
        room.setRoomId(1);

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setRoom(room);

        booking.setCheckInDate(
                Date.valueOf("2026-10-10")
        );

        booking.setCheckOutDate(
                Date.valueOf("2026-10-12")
        );

        booking.setGuests(2);
        booking.setTotalAmount(
                new BigDecimal("3000.00")
        );
        booking.setBookingStatus("PENDING");

        return booking;
    }
}
