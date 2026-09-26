package com.booking.hotel.dao;

import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.Review;
import com.booking.hotel.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ReviewDAOImplTest {

    private ReviewDAO reviewDAO;
    private long createdReviewId;

    @BeforeEach
    void setUp() {
        reviewDAO = new ReviewDAOImpl();
        createdReviewId = 0;
    }

    @AfterEach
    void deleteTestReview() {

        if (createdReviewId <= 0) {
            return;
        }

        try {
            reviewDAO.delete(createdReviewId);
        } catch (SQLException ignored) {
        }
    }

    @Test
    void createInsertsReviewAndSetsGeneratedId() throws SQLException {

        Review review = newTestReview();

        boolean created = reviewDAO.create(review);
        createdReviewId = review.getReviewId();

        assertTrue(created);
        assertTrue(createdReviewId > 0);
    }

    @Test
    void findByIdReturnsInsertedReview() throws SQLException {

        Review review = newTestReview();

        reviewDAO.create(review);
        createdReviewId = review.getReviewId();

        Review found = reviewDAO.findById(createdReviewId);

        assertNotNull(found);
        assertEquals(createdReviewId, found.getReviewId());
        assertEquals(review.getRating(), found.getRating());
        assertEquals(review.getComment(), found.getComment());
    }

    @Test
    void findByHotelIdReturnsReviews() throws SQLException {

        Review review = newTestReview();

        reviewDAO.create(review);
        createdReviewId = review.getReviewId();

        long hotelId = review.getHotel().getHotelId();

        var reviews = reviewDAO.findByHotelId(hotelId);

        assertNotNull(reviews);

        assertTrue(
                reviews.stream()
                        .anyMatch(r -> r.getReviewId() == createdReviewId)
        );
    }

    @Test
    void deleteRemovesReview() throws SQLException {

        Review review = newTestReview();

        reviewDAO.create(review);
        createdReviewId = review.getReviewId();

        assertTrue(reviewDAO.delete(createdReviewId));

        assertNull(reviewDAO.findById(createdReviewId));

        createdReviewId = 0;
    }

    private Review newTestReview() {

        User user = new User();
        user.setUserId(1);

        Hotel hotel = new Hotel();
        hotel.setHotelId(1);

        Review review = new Review();

        review.setUser(user);
        review.setHotel(hotel);
        review.setRating(5);
        review.setComment("Excellent test review");

        return review;
    }
}
