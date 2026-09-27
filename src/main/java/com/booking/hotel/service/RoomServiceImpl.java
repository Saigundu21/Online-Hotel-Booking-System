package com.booking.hotel.service;

import com.booking.hotel.dao.RoomDAO;
import com.booking.hotel.dao.RoomDAOImpl;
import com.booking.hotel.model.Room;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomServiceImpl implements RoomService {

    private static final Logger logger = Logger.getLogger(RoomServiceImpl.class.getName());
    private final RoomDAO roomDAO;

    public RoomServiceImpl() {
        this.roomDAO = new RoomDAOImpl();
    }

    public RoomServiceImpl(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    @Override
    public boolean createRoom(Room room) {
        try {
            if (room.getHotel() == null || room.getHotel().getHotelId() <= 0) {
                throw new IllegalArgumentException("A valid hotel reference is required to create a room.");
            }
            if (room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty()) {
                throw new IllegalArgumentException("Room number cannot be empty.");
            }
            if (room.getBasePrice() == null || room.getBasePrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Base price must be greater than zero.");
            }
            if (room.getCapacity() <= 0) {
                throw new IllegalArgumentException("Room capacity must be at least 1.");
            }

            logger.info("Service: Creating room " + room.getRoomNumber() + " for hotel ID: " + room.getHotel().getHotelId());
            return roomDAO.create(room);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to create room", e);
            return false;
        }
    }

    @Override
    public Room getRoomById(long roomId) {
        try {
            logger.info("Service: Fetching room by ID: " + roomId);
            return roomDAO.findById(roomId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to find room ID: " + roomId, e);
            return null;
        }
    }

    @Override
    public List<Room> getRoomsByHotelId(long hotelId) {
        try {
            logger.info("Service: Fetching rooms for hotel ID: " + hotelId);
            return roomDAO.findByHotel(hotelId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to retrieve rooms for hotel ID: " + hotelId, e);
            return List.of();
        }
    }

    @Override
    public boolean updateRoom(Room room) {
        try {
            if (room.getRoomId() <= 0) {
                throw new IllegalArgumentException("Invalid room ID for update.");
            }
            logger.info("Service: Updating room ID: " + room.getRoomId());
            return roomDAO.update(room);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to update room ID: " + room.getRoomId(), e);
            return false;
        }
    }

    @Override
    public boolean deleteRoom(long roomId) {
        try {
            logger.info("Service: Deleting room ID: " + roomId);
            return roomDAO.delete(roomId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to delete room ID: " + roomId, e);
            return false;
        }
    }

    @Override
    public boolean updateRoomStatus(long roomId, String status) {
        try {
            logger.info("Service: Updating status for room ID " + roomId + " to " + status);
            return roomDAO.updateStatus(roomId, status);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to update status for room ID: " + roomId, e);
            return false;
        }
    }
}
