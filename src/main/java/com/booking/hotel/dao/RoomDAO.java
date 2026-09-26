package com.booking.hotel.dao;

import com.booking.hotel.model.Room;
import java.sql.SQLException;
import java.util.List;

public interface RoomDAO {
    boolean create(Room room) throws SQLException;
    Room findById(long roomId) throws SQLException;
    List<Room> findByHotelId(long hotelId) throws SQLException;
    boolean update(Room room) throws SQLException;
    boolean delete(long roomId) throws SQLException;
boolean updateStatus(long createdRoomId, String booked);
List<Room> findByHotel(long hotelId);}


