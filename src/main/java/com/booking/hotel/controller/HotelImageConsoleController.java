package com.booking.hotel.controller;

import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.HotelImage;
import com.booking.hotel.service.HotelImageService;
import com.booking.hotel.service.HotelImageServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HotelImageConsoleController {

    private static final Logger logger = Logger.getLogger(HotelImageConsoleController.class.getName());
    private final HotelImageService hotelImageService;
    private final Scanner scanner;

    public HotelImageConsoleController() {
        this.hotelImageService = new HotelImageServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    public void startHotelImageMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- HOTEL IMAGE MANAGEMENT MENU ---");
            System.out.println("1. Add New Hotel Image");
            System.out.println("2. View Image by ID");
            System.out.println("3. View Ordered Images by Hotel ID");
            System.out.println("4. Delete Hotel Image");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addHotelImagePrompt();
                        break;
                    case "2":
                        viewImageByIdPrompt();
                        break;
                    case "3":
                        viewOrderedImagesByHotelPrompt();
                        break;
                    case "4":
                        deleteHotelImagePrompt();
                        break;
                    case "5":
                        running = false;
                        System.out.println("Exiting Hotel Image Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                logger.log(Level.SEVERE, "Hotel image console controller error", e);
            }
        }
    }

    private void addHotelImagePrompt() throws SQLException {
        System.out.print("Enter Hotel ID: ");
        long hotelId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Image URL: ");
        String imageUrl = scanner.nextLine();

        System.out.print("Enter Caption: ");
        String caption = scanner.nextLine();

        System.out.print("Enter Display Order (e.g., 0, 1, 2): ");
        int displayOrder = Integer.parseInt(scanner.nextLine());

        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);

        HotelImage image = new HotelImage();
        image.setHotel(hotel);
        image.setImageUrl(imageUrl);
        image.setCaption(caption);
        image.setDisplayOrder(displayOrder);

        boolean success = hotelImageService.addHotelImage(image);
        if (success) {
            System.out.println("Success! Image added with ID: " + image.getImageId());
        } else {
            System.out.println("Failed to add hotel image.");
        }
    }

    private void viewImageByIdPrompt() throws SQLException {
        System.out.print("Enter Image ID: ");
        long imageId = Long.parseLong(scanner.nextLine());

        HotelImage image = hotelImageService.getHotelImageById(imageId);
        if (image != null) {
            System.out.println("\n[Hotel Image Found]");
            System.out.println("Image ID: " + image.getImageId());
            System.out.println("Hotel ID: " + image.getHotel().getHotelId());
            System.out.println("URL: " + image.getImageUrl());
            System.out.println("Caption: " + image.getCaption());
            System.out.println("Display Order: " + image.getDisplayOrder());
        } else {
            System.out.println("No hotel image found with ID: " + imageId);
        }
    }

    private void viewOrderedImagesByHotelPrompt() throws SQLException {
        System.out.print("Enter Hotel ID: ");
        long hotelId = Long.parseLong(scanner.nextLine());

        List<HotelImage> images = hotelImageService.getOrderedImagesByHotelId(hotelId);
        System.out.println("\n--- ORDERED IMAGES FOR HOTEL " + hotelId + " (" + images.size() + ") ---");
        for (HotelImage img : images) {
            System.out.println("Image ID: " + img.getImageId() + " | Order: " + img.getDisplayOrder() + " | Caption: " + img.getCaption() + " | URL: " + img.getImageUrl());
        }
    }

    private void deleteHotelImagePrompt() throws SQLException {
        System.out.print("Enter Image ID to delete: ");
        long imageId = Long.parseLong(scanner.nextLine());

        boolean success = hotelImageService.deleteHotelImage(imageId);
        if (success) {
            System.out.println("Hotel Image ID " + imageId + " has been successfully deleted.");
        } else {
            System.out.println("Failed to delete hotel image.");
        }
    }
}
