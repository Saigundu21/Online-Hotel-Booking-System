package com.booking.hotel.dao;

import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.Review;
import com.booking.hotel.model.User;
import com.booking.hotel.util.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReviewDAOImpl implements ReviewDAO {

    private static final Logger logger =
            Logger.getLogger(ReviewDAOImpl.class.getName());

    private static final String INSERT_SQL =
            "INSERT INTO review (user_id, hotel_id, rating, comment) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT review_id, user_id, hotel_id, rating, comment " +
                    "FROM review WHERE review_id = ?";

    private static final String SELECT_BY_HOTEL_SQL =
            "SELECT review_id, user_id, hotel_id, rating, comment " +
                    "FROM review WHERE hotel_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM review WHERE review_id = ?";

    @Override
    public boolean create(Review review) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, review.getUser().getUserId());
            stmt.setLong(2, review.getHotel().getHotelId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());

            logger.fine("Inserting review for hotel id="
                    + review.getHotel().getHotelId());

            int rows = stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    review.setReviewId(keys.getLong(1));
                }
            }

            if (rows > 0) {
                logger.info("Inserted review id=" + review.getReviewId());
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to insert review", e);
            throw e;
        }
    }

    @Override
    public Review findById(long reviewId) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setLong(1, reviewId);

            logger.fine("Finding review id=" + reviewId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapRow(rs);
                }
            }

            logger.warning("No review found for id=" + reviewId);
            return null;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to find review id=" + reviewId, e);
            throw e;
        }
    }

    @Override
    public List<Review> findByHotelId(long hotelId) throws SQLException {

        List<Review> list = new ArrayList<>();

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_HOTEL_SQL)) {

            stmt.setLong(1, hotelId);

            logger.fine("Finding reviews for hotel id=" + hotelId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

            if (list.isEmpty()) {
                logger.warning("No reviews found for hotel id=" + hotelId);
            }

            return list;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to find reviews for hotel id=" + hotelId, e);
            throw e;
        }
    }

    @Override
    public boolean delete(long reviewId) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(DELETE_SQL)) {

            stmt.setLong(1, reviewId);

            logger.fine("Deleting review id=" + reviewId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("Deleted review id=" + reviewId);
            } else {
                logger.warning("No review found to delete for id=" + reviewId);
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to delete review id=" + reviewId, e);
            throw e;
        }
    }

    @Override
    public List<Review> findByHotel(long hotelId) {
        return List.of();
    }

    private Review mapRow(ResultSet rs) throws SQLException {

        Review review = new Review();

        review.setReviewId(rs.getLong("review_id"));

        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        review.setUser(user);

        Hotel hotel = new Hotel();
        hotel.setHotelId(rs.getLong("hotel_id"));
        review.setHotel(hotel);

        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));

        return review;
    }
}

