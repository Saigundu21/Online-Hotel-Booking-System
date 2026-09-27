package com.booking.hotel.service;

import com.booking.hotel.dao.ReviewDAO;
import com.booking.hotel.dao.ReviewDAOImpl;
import com.booking.hotel.model.Review;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = Logger.getLogger(ReviewServiceImpl.class.getName());
    private final ReviewDAO reviewDAO;

    public ReviewServiceImpl() {
        this.reviewDAO = new ReviewDAOImpl();
    }

    public ReviewServiceImpl(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @Override
    public boolean addReview(Review review) {
        try {
            if (review.getRating() < 1 || review.getRating() > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5 stars.");
            }
            if (review.getUser() == null || review.getUser().getUserId() <= 0) {
                throw new IllegalArgumentException("A valid user reference is required for a review.");
            }
            if (review.getHotel() == null || review.getHotel().getHotelId() <= 0) {
                throw new IllegalArgumentException("A valid hotel reference is required for a review.");
            }

            logger.info("Service: Adding review for hotel ID: " + review.getHotel().getHotelId());
            return reviewDAO.create(review);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to add review", e);
            return false;
        }
    }

    @Override
    public Review getReviewById(long reviewId) {
        try {
            logger.info("Service: Fetching review by ID: " + reviewId);
            return reviewDAO.findById(reviewId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to find review ID: " + reviewId, e);
            return null;
        }
    }

    @Override
    public List<Review> getReviewsByHotelId(long hotelId) {
        try {
            logger.info("Service: Fetching reviews for hotel ID: " + hotelId);
            return reviewDAO.findByHotel(hotelId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to retrieve reviews for hotel ID: " + hotelId, e);
            return List.of();
        }
    }

    @Override
    public boolean deleteReview(long reviewId) {
        try {
            logger.info("Service: Deleting review ID: " + reviewId);
            return reviewDAO.delete(reviewId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to delete review ID: " + reviewId, e);
            return false;
        }
    }
}