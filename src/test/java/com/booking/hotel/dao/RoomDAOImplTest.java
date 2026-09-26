package com.booking.hotel.dao;

import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class RoomDAOImplTest {

    private RoomDAO roomDAO;
    private long createdRoomId;

    @BeforeEach
    void setUp() {
        roomDAO = new RoomDAOImpl();
        createdRoomId = 0;
    }

    @AfterEach
    void deleteTestRoom() {
        if (createdRoomId <= 0) {
            return;
        }

        try {
            roomDAO.delete(createdRoomId);
        } catch (SQLException ignored) {
        }
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
        assertEquals(room.getRoomNumber(), found.getRoomNumber());
        assertEquals(room.getRoomType(), found.getRoomType());
        assertEquals(room.getCapacity(), found.getCapacity());
        assertEquals(room.getBasePrice(), found.getBasePrice());
        assertEquals(room.getStatus(), found.getStatus());
    }

    @Test
    void findByHotelIdReturnsRooms() throws SQLException {

        Room room = newTestRoom();

        roomDAO.create(room);
        createdRoomId = room.getRoomId();

        long hotelId = room.getHotel().getHotelId();

        var rooms = roomDAO.findByHotelId(hotelId);

        assertNotNull(rooms);
        assertTrue(
                rooms.stream()
                        .anyMatch(r -> r.getRoomId() == createdRoomId)
        );
    }

    @Test
    void updateChangesRoomDetails() throws SQLException {

        Room room = newTestRoom();

        roomDAO.create(room);
        createdRoomId = room.getRoomId();

        room.setRoomNumber("999");
        room.setCapacity(4);
        room.setBasePrice(new BigDecimal("2500.00"));

        assertTrue(roomDAO.update(room));

        Room updated = roomDAO.findById(createdRoomId);

        assertNotNull(updated);
        assertEquals("999", updated.getRoomNumber());
        assertEquals(4, updated.getCapacity());
        assertEquals(new BigDecimal("2500.00"), updated.getBasePrice());
    }

    @Test
    void deleteRemovesRoom() throws SQLException {

        Room room = newTestRoom();

        roomDAO.create(room);
        createdRoomId = room.getRoomId();

        assertTrue(roomDAO.delete(createdRoomId));

        assertNull(roomDAO.findById(createdRoomId));

        createdRoomId = 0;
    }

    private Room newTestRoom() {

        Hotel hotel = new Hotel();

        // Use an existing hotel ID from your database.
        hotel.setHotelId(1);

        Room room = new Room();

        room.setHotel(hotel);
        room.setRoomNumber("TEST-" + System.currentTimeMillis());
        room.setRoomType("DELUXE");
        room.setCapacity(2);
        room.setBasePrice(new BigDecimal("1500.00"));
        room.setStatus("AVAILABLE");

        return room;
    }
}


