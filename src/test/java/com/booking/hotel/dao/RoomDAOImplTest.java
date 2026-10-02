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

class RoomDAOImplTest {

    private HotelDAO hotelDAO;
    private RoomDAO roomDAO;
    private UserDAO userDAO;
    private BookingDAO bookingDAO;

    private Hotel testHotel;
    private User testUser;

    private long createdHotelId;
    private long createdRoomId;
    private long createdUserId;
    private long createdBookingId;

    @BeforeEach
    void setUp() throws SQLException {
        hotelDAO = new HotelDAOImpl();
        roomDAO = new RoomDAOImpl();
        userDAO = new UserDAOImpl();
        bookingDAO = new BookingDAOImpl();

        createdHotelId = 0;
        createdRoomId = 0;
        createdUserId = 0;
        createdBookingId = 0;

        testHotel = newTestHotel();
        hotelDAO.create(testHotel);
        createdHotelId = testHotel.getHotelId();

        testUser = newTestUser();
        userDAO.create(testUser);
        createdUserId = testUser.getUserId();
    }

    @AfterEach
    void deleteTestRows() {
        try {
            if (createdBookingId > 0) bookingDAO.delete(createdBookingId);
        } catch (SQLException ignored) {}
        try {
            if (createdRoomId > 0) roomDAO.delete(createdRoomId);
        } catch (SQLException ignored) {}
        try {
            if (createdHotelId > 0) hotelDAO.delete(createdHotelId);
        } catch (SQLException ignored) {}
        try {
            if (createdUserId > 0) userDAO.delete(createdUserId);
        } catch (SQLException ignored) {}
    }

    @Test
    void createInsertsRoomAndSetsGeneratedId() throws SQLException {
        Room room = newTestRoom();

        boolean created = roomDAO.create(room);
        createdRoomId = room.getRoomId();

        assertTrue(created);
        assertTrue(createdRoomId > 0);
    }

    @Test
    void findByIdReturnsInsertedRoom() throws SQLException {
        Room room = newTestRoom();
        roomDAO.create(room);
        createdRoomId = room.getRoomId();

        Room found = roomDAO.findById(createdRoomId);

        assertNotNull(found);
        assertEquals(createdRoomId, found.getRoomId());
        assertEquals(testHotel.getHotelId(), found.getHotel().getHotelId());
        assertEquals(room.getRoomNumber(), found.getRoomNumber());
        assertEquals(room.getRoomType(), found.getRoomType());
        assertEquals(room.getCapacity(), found.getCapacity());
        assertEquals(0, room.getBasePrice().compareTo(found.getBasePrice()));
        assertEquals(room.getStatus(), found.getStatus());
    }

    @Test
    void findByHotelIncludesCreatedRoom() throws SQLException {
        Room room = newTestRoom();
        roomDAO.create(room);
        createdRoomId = room.getRoomId();

        List<Room> rooms = roomDAO.findByHotel(testHotel.getHotelId());

        boolean found = false;
        for (Room candidate : rooms) {
            if (candidate.getRoomId() == createdRoomId) {
                found = true;
                assertEquals(room.getRoomNumber(), candidate.getRoomNumber());
                assertEquals(testHotel.getHotelId(), candidate.getHotel().getHotelId());
            }
        }
        assertTrue(found);
    }

    @Test
    void updateStatusChangesRoomStatus() throws SQLException {
        Room room = newTestRoom();
        roomDAO.create(room);
        createdRoomId = room.getRoomId();

        assertTrue(roomDAO.updateStatus(createdRoomId, "BOOKED"));

        Room found = roomDAO.findById(createdRoomId);
        assertNotNull(found);
        assertEquals("BOOKED", found.getStatus());
    }

    @Test
    void findAvailableRoomsByHotelAndDatesExcludesBookedRooms() throws SQLException {
        Room room = newTestRoom();
        roomDAO.create(room);
        createdRoomId = room.getRoomId();

        Booking booking = new Booking();
        booking.setUser(testUser);
        booking.setHotel(testHotel);
        booking.setRoom(room);
        booking.setCheckInDate(Date.valueOf(LocalDate.now().plusDays(1)));
        booking.setCheckOutDate(Date.valueOf(LocalDate.now().plusDays(4)));
        booking.setGuests(2);
        booking.setTotalAmount(new BigDecimal("4500.00"));
        booking.setPaymentOption("CARD");
        booking.setBookingStatus("CONFIRMED");

        bookingDAO.create(booking);
        createdBookingId = booking.getBookingId();

        // During booking dates -> should be excluded
        List<Room> busyRooms = roomDAO.findAvailableRoomsByHotelAndDates(
                testHotel.getHotelId(),
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(3)
        );
        boolean roomFound = busyRooms.stream().anyMatch(r -> r.getRoomId() == room.getRoomId());
        assertFalse(roomFound, "Booked room should not be available during overlapping dates.");

        // Future unbooked dates -> should be included
        List<Room> freeRooms = roomDAO.findAvailableRoomsByHotelAndDates(
                testHotel.getHotelId(),
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12)
        );
        boolean freeRoomFound = freeRooms.stream().anyMatch(r -> r.getRoomId() == room.getRoomId());
        assertTrue(freeRoomFound, "Room should be available on non-overlapping future dates.");
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

    private Room newTestRoom() {
        Room room = new Room();
        room.setHotel(testHotel);
        room.setRoomNumber("R" + UUID.randomUUID().toString().substring(0, 8));
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("1500.00"));
        room.setStatus("AVAILABLE");
        return room;
    }

    private User newTestUser() {
        String uniqueEmail = "user-" + UUID.randomUUID() + "@example.com";
        return new User(0, "Test User", uniqueEmail, "test123", "9999999999", "CUSTOMER", "ACTIVE");
    }
}