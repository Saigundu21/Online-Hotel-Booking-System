package com.booking.hotel.controller;

import com.booking.hotel.model.Hotel;
import com.booking.hotel.service.HotelService;
import com.booking.hotel.service.HotelServiceImpl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HotelConsoleController {

    private static final Logger logger = Logger.getLogger(HotelConsoleController.class.getName());
    private final HotelService hotelService;
    private final Scanner scanner;

    public HotelConsoleController() {
        this.hotelService = new HotelServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    public void startHotelMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- HOTEL MANAGEMENT MENU ---");
            System.out.println("1. Register New Hotel");
            System.out.println("2. View Hotel by ID");
            System.out.println("3. View All Hotels");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        registerHotelPrompt();
                        break;
                    case "2":
                        viewHotelByIdPrompt();
                        break;
                    case "3":
                        viewAllHotelsPrompt();
                        break;
                    case "4":
                        running = false;
                        System.out.println("Exiting Hotel Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                logger.log(Level.SEVERE, "Hotel console controller error", e);
            }
        }
    }

    private void registerHotelPrompt() throws SQLException {
        System.out.print("Enter Hotel Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        System.out.print("Enter City: ");
        String city = scanner.nextLine();

        System.out.print("Enter Star Rating (e.g., 4.5): ");
        BigDecimal rating = new BigDecimal(scanner.nextLine());

        System.out.print("Enter Amenities (e.g., WiFi, AC): ");
        String amenities = scanner.nextLine();

        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setDescription(description);
        hotel.setAddress(address);
        hotel.setCity(city);
        hotel.setStarRating(rating);
        hotel.setAmenities(amenities);
        hotel.setStatus("ACTIVE");

        boolean success = hotelService.registerHotel(hotel);
        if (success) {
            System.out.println("Success! Hotel registered with ID: " + hotel.getHotelId());
        } else {
            System.out.println("Failed to register hotel.");
        }
    }

    private void viewHotelByIdPrompt() throws SQLException {
        System.out.print("Enter Hotel ID: ");
        long id = Long.parseLong(scanner.nextLine());

        Hotel hotel = hotelService.getHotelById(id);
        if (hotel != null) {
            System.out.println("\n[Hotel Found]");
            System.out.println("ID: " + hotel.getHotelId());
            System.out.println("Name: " + hotel.getName());
            System.out.println("Address: " + hotel.getAddress() + ", " + hotel.getCity());
            System.out.println("Rating: " + hotel.getStarRating());
            System.out.println("Status: " + hotel.getStatus());
        } else {
            System.out.println("No hotel found with ID: " + id);
        }
    }

    private void viewAllHotelsPrompt() throws SQLException {
        List<Hotel> hotels = hotelService.getAllHotels();
        System.out.println("\n--- ALL HOTELS (" + hotels.size() + ") ---");
        for (Hotel h : hotels) {
            System.out.println("ID: " + h.getHotelId() + " | Name: " + h.getName() + " | City: " + h.getCity() + " | Rating: " + h.getStarRating());
        }
    }
}