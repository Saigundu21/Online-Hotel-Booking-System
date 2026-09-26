package com.booking.hotel.dao;

import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.Room;
import com.booking.hotel.util.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomDAOImpl implements RoomDAO {

    private static final Logger logger =
            Logger.getLogger(RoomDAOImpl.class.getName());

    private static final String INSERT_SQL =
            "INSERT INTO room (hotel_id, room_number, room_type, capacity, base_price, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT room_id, hotel_id, room_number, room_type, capacity, base_price, status " +
                    "FROM room WHERE room_id = ?";

    private static final String SELECT_BY_HOTEL_SQL =
            "SELECT room_id, hotel_id, room_number, room_type, capacity, base_price, status " +
                    "FROM room WHERE hotel_id = ?";

    private static final String UPDATE_SQL =
            "UPDATE room SET hotel_id = ?, room_number = ?, room_type = ?, capacity = ?, " +
                    "base_price = ?, status = ? WHERE room_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM room WHERE room_id = ?";

    @Override
    public boolean create(Room room) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, room.getHotel().getHotelId());
            stmt.setString(2, room.getRoomNumber());
            stmt.setString(3, room.getRoomType());
            stmt.setInt(4, room.getCapacity());
            stmt.setBigDecimal(5, room.getBasePrice());
            stmt.setString(6, room.getStatus());

            logger.fine("Inserting room: " + room.getRoomNumber());

            int rows = stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    room.setRoomId(keys.getLong(1));
                }
            }

            if (rows > 0) {
                logger.info("Inserted room id=" + room.getRoomId());
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to insert room", e);
            throw e;
        }
    }

    @Override
    public Room findById(long roomId) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setLong(1, roomId);

            logger.fine("Finding room id=" + roomId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapRow(rs);
                }
            }

            logger.warning("No room found for id=" + roomId);
            return null;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to find room id=" + roomId, e);
            throw e;
        }
    }

    @Override
    public List<Room> findByHotelId(long hotelId) throws SQLException {

        List<Room> rooms = new ArrayList<>();

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_HOTEL_SQL)) {

            stmt.setLong(1, hotelId);

            logger.fine("Finding rooms for hotel id=" + hotelId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    rooms.add(mapRow(rs));
                }
            }

            if (rooms.isEmpty()) {
                logger.warning("No rooms found for hotel id=" + hotelId);
            }

            return rooms;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to find rooms for hotel id=" + hotelId, e);
            throw e;
        }
    }

    @Override
    public boolean update(Room room) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(UPDATE_SQL)) {

            stmt.setLong(1, room.getHotel().getHotelId());
            stmt.setString(2, room.getRoomNumber());
            stmt.setString(3, room.getRoomType());
            stmt.setInt(4, room.getCapacity());
            stmt.setBigDecimal(5, room.getBasePrice());
            stmt.setString(6, room.getStatus());
            stmt.setLong(7, room.getRoomId());

            logger.fine("Updating room id=" + room.getRoomId());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("Updated room id=" + room.getRoomId());
            } else {
                logger.warning("No room updated for id=" + room.getRoomId());
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to update room id=" + room.getRoomId(), e);
            throw e;
        }
    }

    @Override
    public boolean delete(long roomId) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(DELETE_SQL)) {

            stmt.setLong(1, roomId);

            logger.fine("Deleting room id=" + roomId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("Deleted room id=" + roomId);
            } else {
                logger.warning("No room found to delete for id=" + roomId);
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to delete room id=" + roomId, e);
            throw e;
        }
    }

    private Room mapRow(ResultSet rs) throws SQLException {

        Room room = new Room();

        room.setRoomId(rs.getLong("room_id"));

        Hotel hotel = new Hotel();
        hotel.setHotelId(rs.getLong("hotel_id"));
        room.setHotel(hotel);

        room.setRoomNumber(rs.getString("room_number"));
        room.setRoomType(rs.getString("room_type"));
        room.setCapacity(rs.getInt("capacity"));
        room.setBasePrice(rs.getBigDecimal("base_price"));
        room.setStatus(rs.getString("status"));

        return room;
    }
}