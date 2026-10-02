package com.booking.hotel.service;

import com.booking.hotel.model.Room;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    boolean createRoom(Room room);
    Room getRoomById(long roomId);
    List<Room> getRoomsByHotelId(long hotelId);
    boolean updateRoom(Room room);
    boolean deleteRoom(long roomId);
    boolean updateRoomStatus(long roomId, String status);
    List<Room> findAvailableRoomsByHotelAndDates(long hotelId, LocalDate checkIn, LocalDate checkOut) throws SQLException;
}
