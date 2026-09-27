package com.booking.hotel.controller;

import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.Review;
import com.booking.hotel.model.User;
import com.booking.hotel.service.ReviewService;
import com.booking.hotel.service.ReviewServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReviewConsoleController {

    private static final Logger logger = Logger.getLogger(ReviewConsoleController.class.getName());
    private final ReviewService reviewService;
    private final Scanner scanner;

    public ReviewConsoleController() {
        this.reviewService = new ReviewServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    public void startReviewMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- REVIEW MANAGEMENT MENU ---");
            System.out.println("1. Add New Review");
            System.out.println("2. View Review by ID");
            System.out.println("3. View Reviews by Hotel ID");
            System.out.println("4. Delete Review");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addReviewPrompt();
                        break;
                    case "2":
                        viewReviewByIdPrompt();
                        break;
                    case "3":
                        viewReviewsByHotelPrompt();
                        break;
                    case "4":
                        deleteReviewPrompt();
                        break;
                    case "5":
                        running = false;
                        System.out.println("Exiting Review Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                logger.log(Level.SEVERE, "Review console controller error", e);
            }
        }
    }

    private void addReviewPrompt() throws SQLException {
        System.out.print("Enter User ID: ");
        long userId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Hotel ID: ");
        long hotelId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Rating (1 to 5): ");
        int rating = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Comment/Feedback: ");
        String comment = scanner.nextLine();

        User user = new User();
        user.setUserId(userId);

        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);

        Review review = new Review();
        review.setUser(user);
        review.setHotel(hotel);
        review.setRating(rating);
        review.setComment(comment);

        boolean success = reviewService.addReview(review);
        if (success) {
            System.out.println("Success! Review added with ID: " + review.getReviewId());
        } else {
            System.out.println("Failed to add review.");
        }
    }

    private void viewReviewByIdPrompt() throws SQLException {
        System.out.print("Enter Review ID: ");
        long reviewId = Long.parseLong(scanner.nextLine());

        Review review = reviewService.getReviewById(reviewId);
        if (review != null) {
            System.out.println("\n[Review Found]");
            System.out.println("Review ID: " + review.getReviewId());
            System.out.println("User ID: " + review.getUser().getUserId());
            System.out.println("Hotel ID: " + review.getHotel().getHotelId());
            System.out.println("Rating: " + review.getRating() + " / 5");
            System.out.println("Comment: " + review.getComment());
        } else {
            System.out.println("No review found with ID: " + reviewId);
        }
    }

    private void viewReviewsByHotelPrompt() throws SQLException {
        System.out.print("Enter Hotel ID: ");
        long hotelId = Long.parseLong(scanner.nextLine());

        List<Review> reviews = reviewService.getReviewsByHotelId(hotelId);
        System.out.println("\n--- REVIEWS FOR HOTEL " + hotelId + " (" + reviews.size() + ") ---");
        for (Review r : reviews) {
            System.out.println("Review ID: " + r.getReviewId() + " | Rating: " + r.getRating() + "/5 | Comment: " + r.getComment());
        }
    }

    private void deleteReviewPrompt() throws SQLException {
        System.out.print("Enter Review ID to delete: ");
        long reviewId = Long.parseLong(scanner.nextLine());

        boolean success = reviewService.deleteReview(reviewId);
        if (success) {
            System.out.println("Review ID " + reviewId + " has been successfully deleted.");
        } else {
            System.out.println("Failed to delete review.");
        }
    }
}
