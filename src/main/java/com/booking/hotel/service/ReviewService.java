package com.booking.hotel.service;

import com.booking.hotel.model.Review;
import java.sql.SQLException;
import java.util.List;

public interface ReviewService {
    boolean addReview(Review review) throws SQLException;
    Review getReviewById(long reviewId) throws SQLException;
    List<Review> getReviewsByHotelId(long hotelId) throws SQLException;
    boolean deleteReview(long reviewId) throws SQLException;
}
