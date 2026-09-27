package com.booking.hotel.controller;

import com.booking.hotel.model.Booking;
import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.Room;
import com.booking.hotel.model.User;
import com.booking.hotel.service.BookingService;
import com.booking.hotel.service.BookingServiceImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookingConsoleController {

    private static final Logger logger = Logger.getLogger(BookingConsoleController.class.getName());
    private final BookingService bookingService;
    private final Scanner scanner;

    public BookingConsoleController() {
        this.bookingService = new BookingServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    public void startBookingMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- BOOKING MANAGEMENT MENU ---");
            System.out.println("1. Create New Booking");
            System.out.println("2. View Booking by ID");
            System.out.println("3. View Bookings by User ID");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        createBookingPrompt();
                        break;
                    case "2":
                        viewBookingByIdPrompt();
                        break;
                    case "3":
                        viewBookingsByUserPrompt();
                        break;
                    case "4":
                        cancelBookingPrompt();
                        break;
                    case "5":
                        running = false;
                        System.out.println("Exiting Booking Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                logger.log(Level.SEVERE, "Booking console controller error", e);
            }
        }
    }

    private void createBookingPrompt() throws Exception {
        System.out.print("Enter User ID: ");
        long userId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Hotel ID: ");
        long hotelId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Room ID: ");
        long roomId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Check-In Date (YYYY-MM-DD): ");
        Date checkIn = Date.valueOf(scanner.nextLine());

        System.out.print("Enter Check-Out Date (YYYY-MM-DD): ");
        Date checkOut = Date.valueOf(scanner.nextLine());

        System.out.print("Enter Number of Guests: ");
        int guests = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Total Amount: ");
        BigDecimal totalAmount = new BigDecimal(scanner.nextLine());

        System.out.print("Enter Payment Option (e.g., CARD, CASH): ");
        String paymentOption = scanner.nextLine();

        User user = new User();
        user.setUserId(userId);

        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);

        Room room = new Room();
        room.setRoomId(roomId);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setRoom(room);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setGuests(guests);
        booking.setTotalAmount(totalAmount);
        booking.setPaymentOption(paymentOption);
        booking.setBookingStatus("CONFIRMED");

        boolean success = bookingService.createBooking(booking);
        if (success) {
            System.out.println("Success! Booking created with ID: " + booking.getBookingId());
        } else {
            System.out.println("Failed to create booking.");
        }
    }

    private void viewBookingByIdPrompt() throws Exception {
        System.out.print("Enter Booking ID: ");
        long bookingId = Long.parseLong(scanner.nextLine());

        Booking booking = bookingService.getBookingById(bookingId);
        if (booking != null) {
            System.out.println("\n[Booking Found]");
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("User ID: " + booking.getUser().getUserId());
            System.out.println("Hotel ID: " + booking.getHotel().getHotelId());
            System.out.println("Room ID: " + booking.getRoom().getRoomId());
            System.out.println("Check-In: " + booking.getCheckInDate());
            System.out.println("Check-Out: " + booking.getCheckOutDate());
            System.out.println("Total Amount: " + booking.getTotalAmount());
            System.out.println("Status: " + booking.getBookingStatus());
        } else {
            System.out.println("No booking found with ID: " + bookingId);
        }
    }

    private void viewBookingsByUserPrompt() throws Exception {
        System.out.print("Enter User ID: ");
        long userId = Long.parseLong(scanner.nextLine());

        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        System.out.println("\n--- BOOKINGS FOR USER " + userId + " (" + bookings.size() + ") ---");
        for (Booking b : bookings) {
            System.out.println("Booking ID: " + b.getBookingId() + " | Hotel ID: " + b.getHotel().getHotelId() + " | Status: " + b.getBookingStatus());
        }
    }

    private void cancelBookingPrompt() throws Exception {
        System.out.print("Enter Booking ID to cancel: ");
        long bookingId = Long.parseLong(scanner.nextLine());

        boolean success = bookingService.cancelBooking(bookingId);
        if (success) {
            System.out.println("Booking ID " + bookingId + " has been successfully CANCELLED.");
        } else {
            System.out.println("Failed to cancel booking or booking ID not found.");
        }
    }
}
