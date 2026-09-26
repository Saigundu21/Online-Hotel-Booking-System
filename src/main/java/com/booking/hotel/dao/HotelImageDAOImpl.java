package com.booking.hotel.dao;

import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.HotelImage;
import com.booking.hotel.util.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HotelImageDAOImpl implements HotelImageDAO {

    private static final Logger logger =
            Logger.getLogger(HotelImageDAOImpl.class.getName());

    private static final String INSERT_SQL =
            "INSERT INTO hotel_image (hotel_id, image_url, caption) " +
                    "VALUES (?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT image_id, hotel_id, image_url, caption " +
                    "FROM hotel_image WHERE image_id = ?";

    private static final String SELECT_BY_HOTEL_SQL =
            "SELECT image_id, hotel_id, image_url, caption " +
                    "FROM hotel_image WHERE hotel_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM hotel_image WHERE image_id = ?";

    @Override
    public boolean create(HotelImage image) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, image.getHotel().getHotelId());
            stmt.setString(2, image.getImageUrl());
            stmt.setString(3, image.getCaption());

            logger.fine("Creating image for hotel id="
                    + image.getHotel().getHotelId());

            int rows = stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {

                if (keys.next()) {
                    image.setImageId(keys.getLong(1));
                }
            }

            if (rows > 0) {
                logger.info("Inserted hotel image id="
                        + image.getImageId());
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to insert hotel image", e);
            throw e;
        }
    }

    @Override
    public HotelImage findById(long imageId) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setLong(1, imageId);

            logger.fine("Finding hotel image id=" + imageId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapRow(rs);
                }
            }

            logger.warning(
                    "No hotel image found for id=" + imageId);

            return null;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to find hotel image id=" + imageId, e);
            throw e;
        }
    }

    @Override
    public List<HotelImage> findByHotelId(long hotelId)
            throws SQLException {

        List<HotelImage> list = new ArrayList<>();

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_HOTEL_SQL)) {

            stmt.setLong(1, hotelId);

            logger.fine("Finding images for hotel id=" + hotelId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

            if (list.isEmpty()) {
                logger.warning(
                        "No images found for hotel id=" + hotelId);
            }

            return list;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to find images for hotel id="
                            + hotelId, e);
            throw e;
        }
    }

    @Override
    public boolean delete(long imageId) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(DELETE_SQL)) {

            stmt.setLong(1, imageId);

            logger.fine("Deleting hotel image id=" + imageId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("Deleted hotel image id=" + imageId);
            } else {
                logger.warning(
                        "No hotel image found to delete for id="
                                + imageId);
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to delete hotel image id="
                            + imageId, e);
            throw e;
        }
    }

    @Override
    public <__TMP__> __TMP__ findByHotel(long hotelId) {
        return null;
    }

    private HotelImage mapRow(ResultSet rs) throws SQLException {

        HotelImage img = new HotelImage();

        img.setImageId(rs.getLong("image_id"));

        Hotel hotel = new Hotel();
        hotel.setHotelId(rs.getLong("hotel_id"));
        img.setHotel(hotel);

        img.setImageUrl(rs.getString("image_url"));
        img.setCaption(rs.getString("caption"));

        return img;
    }
}
