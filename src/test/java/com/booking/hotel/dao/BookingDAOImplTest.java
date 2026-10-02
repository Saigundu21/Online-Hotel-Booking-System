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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingDAOImplTest {

    private UserDAO userDAO;
    private HotelDAO hotelDAO;
    private RoomDAO roomDAO;
    private BookingDAO bookingDAO;

    private User testUser;
    private Hotel testHotel;
    private Room testRoom;

    private long createdUserId;
    private long createdHotelId;
    private long createdRoomId;
    private long createdBookingId;

    @BeforeEach
    void setUp() throws SQLException {
        userDAO = new UserDAOImpl();
        hotelDAO = new HotelDAOImpl();
        roomDAO = new RoomDAOImpl();
        bookingDAO = new BookingDAOImpl();

        createdUserId = 0;
        createdHotelId = 0;
        createdRoomId = 0;
        createdBookingId = 0;

        testUser = newTestUser();
        userDAO.create(testUser);
        createdUserId = testUser.getUserId();

        testHotel = newTestHotel();
        hotelDAO.create(testHotel);
        createdHotelId = testHotel.getHotelId();

        testRoom = newTestRoom(testHotel);
        roomDAO.create(testRoom);
        createdRoomId = testRoom.getRoomId();
    }

    @AfterEach
    void deleteTestRows() {
        try {
            if (createdBookingId > 0) {
                bookingDAO.delete(createdBookingId);
            }
        } catch (SQLException ignored) {}
        try {
            if (createdRoomId > 0) {
                roomDAO.delete(createdRoomId);
            }
        } catch (SQLException ignored) {}
        try {
            if (createdHotelId > 0) {
                hotelDAO.delete(createdHotelId);
            }
        } catch (SQLException ignored) {}
        try {
            if (createdUserId > 0) {
                userDAO.delete(createdUserId);
            }
        } catch (SQLException ignored) {}
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
        assertEquals(testUser.getUserId(), found.getUser().getUserId());
        assertEquals(testHotel.getHotelId(), found.getHotel().getHotelId());
        assertEquals(testRoom.getRoomId(), found.getRoom().getRoomId());
        assertEquals(booking.getCheckInDate(), found.getCheckInDate());
        assertEquals(booking.getCheckOutDate(), found.getCheckOutDate());
        assertEquals(0, booking.getTotalAmount().compareTo(found.getTotalAmount()));
        assertEquals(booking.getBookingStatus(), found.getBookingStatus());
    }

    @Test
    void findByUserIncludesCreatedBooking() throws SQLException {
        Booking booking = newTestBooking();
        bookingDAO.create(booking);
        createdBookingId = booking.getBookingId();

        List<Booking> bookings = (List<Booking>) bookingDAO.findByUser(testUser.getUserId());

        boolean found = false;
        for (Booking candidate : bookings) {
            if (candidate.getBookingId() == createdBookingId) {
                found = true;
                assertEquals(testUser.getUserId(), candidate.getUser().getUserId());
            }
        }
        assertTrue(found);
    }

    @Test
    void updateStatusChangesBookingStatus() throws SQLException {
        Booking booking = newTestBooking();
        bookingDAO.create(booking);
        createdBookingId = booking.getBookingId();

        assertTrue(bookingDAO.updateStatus(createdBookingId, "CANCELLED"));

        Booking found = bookingDAO.findById(createdBookingId);
        assertNotNull(found);
        assertEquals("CANCELLED", found.getBookingStatus());
    }

    @Test
    void isRoomAvailableDetectsOverlaps() throws SQLException {
        Booking booking = newTestBooking();
        bookingDAO.create(booking);
        createdBookingId = booking.getBookingId();

        // Overlapping date check -> should be false
        boolean availableOverlap = bookingDAO.isRoomAvailable(
                testRoom.getRoomId(),
                Date.valueOf(LocalDate.now().plusDays(2)),
                Date.valueOf(LocalDate.now().plusDays(3))
        );
        assertFalse(availableOverlap);

        // Non-overlapping future date check -> should be true
        boolean availableFree = bookingDAO.isRoomAvailable(
                testRoom.getRoomId(),
                Date.valueOf(LocalDate.now().plusDays(10)),
                Date.valueOf(LocalDate.now().plusDays(12))
        );
        assertTrue(availableFree);
    }

    private User newTestUser() {
        String uniqueEmail = "user-" + UUID.randomUUID() + "@example.com";
        return new User(0, "Test User", uniqueEmail, "test123", "9999999999", "CUSTOMER", "ACTIVE");
    }

    private Hotel newTestHotel() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        Hotel hotel = new Hotel();
        hotel.setName("Hotel " + unique);
        hotel.setDescription("Test hotel");
        hotel.setAddress("1 Test Street");
        hotel.setCity("Test City" + unique);
        hotel.setStarRating(new BigDecimal("4.5"));
        hotel.setAmenities("WiFi");
        hotel.setStatus("ACTIVE");
        return hotel;
    }

    private Room newTestRoom(Hotel hotel) {
        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomNumber("R" + UUID.randomUUID().toString().substring(0, 8));
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("1500.00"));
        room.setStatus("AVAILABLE");
        return room;
    }

    private Booking newTestBooking() {
        Booking booking = new Booking();
        booking.setUser(testUser);
        booking.setHotel(testHotel);
        booking.setRoom(testRoom);
        booking.setCheckInDate(Date.valueOf(LocalDate.now().plusDays(1)));
        booking.setCheckOutDate(Date.valueOf(LocalDate.now().plusDays(4)));
        booking.setGuests(2);
        booking.setTotalAmount(new BigDecimal("4500.00"));
        booking.setPaymentOption("CARD");
        booking.setBookingStatus("CONFIRMED");
        return booking;
    }
}