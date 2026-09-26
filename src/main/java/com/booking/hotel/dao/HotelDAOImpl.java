package com.booking.hotel.dao;

import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.Location;
import com.booking.hotel.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HotelDAOImpl implements HotelDAO {

    private static final Logger logger =
            Logger.getLogger(HotelDAOImpl.class.getName());

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
             PreparedStatement statement =
                     connection.prepareStatement(
                             SQL_INSERT_HOTEL,
                             Statement.RETURN_GENERATED_KEYS)) {

            setHotelColumns(statement, hotel);

            logger.info("Creating new hotel");

            int rows = statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    hotel.setHotelId(keys.getLong(1));
                }
            }

            if (rows > 0) {
                logger.info("Hotel created successfully. Hotel ID: "
                        + hotel.getHotelId());
            }

            return rows > 0;

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Failed to create hotel",
                    e
            );

            throw e;
        }
    }

    @Override
    public Hotel findById(long hotelId) throws SQLException {

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SQL_FIND_BY_ID)) {

            statement.setLong(1, hotelId);

            logger.info("Searching for hotel with ID: " + hotelId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info("Hotel found with ID: " + hotelId);

                    return mapRow(resultSet);
                }

                logger.warning("No hotel found with ID: " + hotelId);

                return null;
            }

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Failed to find hotel with ID: " + hotelId,
                    e
            );

            throw e;
        }
    }

    @Override
    public List<Hotel> findByCity(String city) throws SQLException {

        // Existing implementation was a placeholder.
        // Logging conversion does not change this method's functionality.
        logger.info("Searching hotels by city: " + city);

        return List.of();
    }

    @Override
    public List<Hotel> findAll() throws SQLException {

        List<Hotel> hotels = new ArrayList<>();

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SQL_FIND_ALL)) {

            logger.info("Fetching all hotels");

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    hotels.add(mapRow(resultSet));
                }
            }

            if (hotels.isEmpty()) {
                logger.warning("No hotels found");
            } else {
                logger.info("Hotels retrieved successfully. Count: "
                        + hotels.size());
            }

            return hotels;

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Failed to retrieve hotels",
                    e
            );

            throw e;
        }
    }

    @Override
    public boolean update(Hotel hotel) throws SQLException {

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SQL_UPDATE)) {

            setHotelColumns(statement, hotel);
            statement.setLong(8, hotel.getHotelId());

            logger.info("Updating hotel with ID: "
                    + hotel.getHotelId());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                logger.info("Hotel updated successfully. Hotel ID: "
                        + hotel.getHotelId());
            } else {
                logger.warning("No hotel was updated for ID: "
                        + hotel.getHotelId());
            }

            return rows > 0;

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Failed to update hotel with ID: "
                            + hotel.getHotelId(),
                    e
            );

            throw e;
        }
    }

    @Override
    public boolean delete(long hotelId) throws SQLException {

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SQL_DELETE)) {

            statement.setLong(1, hotelId);

            logger.info("Deleting hotel with ID: " + hotelId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                logger.info("Hotel deleted successfully. Hotel ID: "
                        + hotelId);
            } else {
                logger.warning("No hotel was deleted for ID: "
                        + hotelId);
            }

            return rows > 0;

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Failed to delete hotel with ID: " + hotelId,
                    e
            );

            throw e;
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

    private void setHotelColumns(
            PreparedStatement statement,
            Hotel hotel) throws SQLException {

        if (hotel.getLocation() == null) {

            statement.setNull(1, Types.BIGINT);

        } else {

            statement.setLong(
                    1,
                    hotel.getLocation().getLocationId()
            );
        }

        statement.setString(2, hotel.getName());
        statement.setString(3, hotel.getDescription());
        statement.setString(4, hotel.getAddress());
        statement.setBigDecimal(5, hotel.getStarRating());
        statement.setString(6, hotel.getAmenities());
        statement.setString(7, hotel.getStatus());
    }
}