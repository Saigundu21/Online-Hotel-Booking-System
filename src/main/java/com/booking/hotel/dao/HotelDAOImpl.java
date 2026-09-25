package com.booking.hotel.dao;

import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.Location;
import com.booking.hotel.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class HotelDAOImpl implements HotelDAO {

    private static final Logger logger = LoggerFactory.getLogger(HotelDAOImpl.class);

    private static final String SQL_INSERT_HOTEL =
            "INSERT INTO hotel (location_id, name, description, address, "
                    + "star_rating, amenities, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM hotel WHERE hotel_id = ?";

    private static final String SQL_FIND_ALL =
            "SELECT * FROM hotel";

    private static final String SQL_UPDATE =
            "UPDATE hotel SET location_id = ?, name = ?, description = ?, address = ?, "
                    + "star_rating = ?, amenities = ?, status = ? "
                    + "WHERE hotel_id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM hotel WHERE hotel_id = ?";

    @Override
    public boolean create(Hotel hotel) throws SQLException {
        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_HOTEL, Statement.RETURN_GENERATED_KEYS)) {

            setHotelColumns(statement, hotel);

            logger.debug("Inserting hotel with name {}", hotel.getName());
            int rows = statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    hotel.setHotelId(keys.getLong(1));
                }
            }

            if (rows > 0) {
                logger.info("Inserted hotel id={}", hotel.getHotelId());
            }
            return rows > 0;
        }
    }

    @Override
    public Hotel findById(long hotelId) throws SQLException {
        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            statement.setLong(1, hotelId);

            logger.debug("Selecting hotel with id {}", hotelId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
                logger.warn("No hotel found for id {}", hotelId);
                return null;
            }
        }
    }@Override
    public List<Hotel> findByCity(String city) throws SQLException {
        return List.of();
    }

    @Override
    public List<Hotel> findAll() throws SQLException {
        List<Hotel> hotels = new ArrayList<>();

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL)) {

            logger.debug("Selecting all hotels");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    hotels.add(mapRow(resultSet));
                }
            }
        }

        if (hotels.isEmpty()) {
            logger.warn("No hotels found");
        }
        return hotels;
    }

    @Override
    public boolean update(Hotel hotel) throws SQLException {
        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {

            setHotelColumns(statement, hotel);
            statement.setLong(8, hotel.getHotelId());

            logger.debug("Updating hotel with id {}", hotel.getHotelId());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                logger.info("Updated hotel id={}", hotel.getHotelId());
            }
            return rows > 0;
        }
    }

    @Override
    public boolean delete(long hotelId) throws SQLException {
        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {

            statement.setLong(1, hotelId);
            logger.debug("Deleting hotel with id {}", hotelId);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                logger.info("Deleted hotel id={}", hotelId);
            }
            return rows > 0;
        }
    }

    private Hotel mapRow(ResultSet resultSet) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setHotelId(resultSet.getLong("hotel_id"));

        long locationId = resultSet.getLong("location_id");
        if (!resultSet.wasNull()) {
            Location location = new Location();
            location.setLocationId(locationId);
            hotel.setLocation(location);
        }

        hotel.setName(resultSet.getString("name"));
        hotel.setDescription(resultSet.getString("description"));
        hotel.setAddress(resultSet.getString("address"));
        hotel.setStarRating(resultSet.getBigDecimal("star_rating"));
        hotel.setAmenities(resultSet.getString("amenities"));
        hotel.setStatus(resultSet.getString("status"));
        return hotel;
    }

    private void setHotelColumns(PreparedStatement statement, Hotel hotel) throws SQLException {
        if (hotel.getLocation() == null) {
            statement.setNull(1, Types.BIGINT);
        } else {
            statement.setLong(1, hotel.getLocation().getLocationId());
        }
        statement.setString(2, hotel.getName());
        statement.setString(3, hotel.getDescription());
        statement.setString(4, hotel.getAddress());
        statement.setBigDecimal(5, hotel.getStarRating());
        statement.setString(6, hotel.getAmenities());
        statement.setString(7, hotel.getStatus());
    }
}