package com.booking.hotel.controller;

import com.booking.hotel.model.Booking;
import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.Room;
import com.booking.hotel.model.User;
import com.booking.hotel.service.BookingService;
import com.booking.hotel.service.BookingServiceImpl;
import com.booking.hotel.service.HotelService;
import com.booking.hotel.service.HotelServiceImpl;
import com.booking.hotel.service.RoomService;
import com.booking.hotel.service.RoomServiceImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookingConsoleController {

    private static final Logger logger = Logger.getLogger(BookingConsoleController.class.getName());
    private final BookingService bookingService;
    private final HotelService hotelService;
    private final RoomService roomService;
    private final Scanner scanner;

    public BookingConsoleController() {
        this.bookingService = new BookingServiceImpl();
        this.hotelService = new HotelServiceImpl();
        this.roomService = new RoomServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    public void startBookingMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- BOOKING MANAGEMENT MENU ---");
            System.out.println("1. Create New Booking (Search Dates, Rooms & Book)");
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
                        viewBookingsByUserIdPrompt();
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
                logger.log(Level.SEVERE, "Booking controller error", e);
            }
        }
    }

    private void createBookingPrompt() throws SQLException {
        Logger.getLogger("com.booking.hotel").setLevel(Level.SEVERE);

        System.out.println("\n--- ENTER DESIRED STAY DATES ---");
        System.out.print("Enter Check-In Date (YYYY-MM-DD): ");
        Date checkIn = Date.valueOf(scanner.nextLine());

        System.out.print("Enter Check-Out Date (YYYY-MM-DD): ");
        Date checkOut = Date.valueOf(scanner.nextLine());

        if (!checkOut.after(checkIn)) {
            System.out.println("Error: Check-out date must be strictly after check-in date.");
            return;
        }

        System.out.println("\n--- AVAILABLE HOTELS & ROOMS FOR YOUR DATES ---");
        List<Hotel> hotels = hotelService.getAllHotels();
        if (hotels.isEmpty()) {
            System.out.println("No hotels available at the moment.");
            return;
        }

        for (Hotel h : hotels) {
            try {
                // Fetch ONLY rooms available for these dates
                List<Room> availableRooms = roomService.findAvailableRoomsByHotelAndDates(
                        h.getHotelId(), checkIn.toLocalDate(), checkOut.toLocalDate()
                );

                if (availableRooms != null && !availableRooms.isEmpty()) {
                    System.out.println("\n[Hotel ID: " + h.getHotelId() + "] " + h.getName() + " - " + h.getCity());
                    System.out.println("   --- Available Rooms & Rates ---");
                    for (Room r : availableRooms) {
                        System.out.println("      * Room ID: " + r.getRoomId()
                                + " | Type: " + r.getRoomType()
                                + " | Number: " + r.getRoomNumber()
                                + " | Price/Night: $" + r.getBasePrice());
                    }
                }
            } catch (Exception e) {
                // Skip hotel if error occurs during fetch
            }
        }

        System.out.println("\n--- ENTER BOOKING DETAILS ---");
        System.out.print("Enter Your User ID: ");
        long userId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Hotel ID from list above: ");
        long hotelId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Room ID from list above: ");
        long roomId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Number of Guests: ");
        int guests = Integer.parseInt(scanner.nextLine());

        // Fetch selected room to calculate nights and total cost automatically
        Room selectedRoom = roomService.getRoomById(roomId);
        if (selectedRoom == null) {
            System.out.println("Invalid Room ID selected.");
            return;
        }

        long nights = ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());
        if (nights == 0) nights = 1;
        BigDecimal expectedTotal = selectedRoom.getBasePrice().multiply(BigDecimal.valueOf(nights));

        System.out.println("\n--- PAYMENT SUMMARY ---");
        System.out.println("Room Type: " + selectedRoom.getRoomType() + " ($" + selectedRoom.getBasePrice() + " /night)");
        System.out.println("Duration: " + nights + " night(s)");
        System.out.println("Total Amount Due: $" + expectedTotal);

        System.out.print("Enter FULL payment amount to confirm booking ($): ");
        double paidAmount = Double.parseDouble(scanner.nextLine());

        if (BigDecimal.valueOf(paidAmount).compareTo(expectedTotal) < 0) {
            System.out.println("Booking Failed! You must pay the full amount of $" + expectedTotal);
            return;
        }

        System.out.print("Enter Payment Option (e.g. CREDIT_CARD, UPI, CASH): ");
        String paymentOption = scanner.nextLine();

        Booking newBooking = new Booking();

        User user = new User();
        user.setUserId(userId);
        newBooking.setUser(user);

        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);
        newBooking.setHotel(hotel);

        Room room = new Room();
        room.setRoomId(roomId);
        newBooking.setRoom(room);

        newBooking.setCheckInDate(checkIn);
        newBooking.setCheckOutDate(checkOut);
        newBooking.setGuests(guests);
        newBooking.setPaymentOption(paymentOption);
        newBooking.setBookingStatus("CONFIRMED");

        // Service layer will handle the total amount setting, pricing, and concurrency validation
        boolean success = bookingService.createBooking(newBooking);

        if (success) {
            System.out.println("\nSuccess! Booking created with ID: " + newBooking.getBookingId()
                    + " | Total Paid: $" + newBooking.getTotalAmount());
        } else {
            System.out.println("Failed to create booking.");
        }
    }

    private void viewBookingByIdPrompt() throws SQLException {
        System.out.print("Enter Booking ID: ");
        long id = Long.parseLong(scanner.nextLine());

        Booking booking = bookingService.getBookingById(id);
        if (booking != null) {
            System.out.println("\n[Booking Found]");
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("User ID: " + (booking.getUser() != null ? booking.getUser().getUserId() : "N/A"));
            System.out.println("Hotel ID: " + (booking.getHotel() != null ? booking.getHotel().getHotelId() : "N/A"));
            System.out.println("Room ID: " + (booking.getRoom() != null ? booking.getRoom().getRoomId() : "N/A"));
            System.out.println("Check-In: " + booking.getCheckInDate());
            System.out.println("Check-Out: " + booking.getCheckOutDate());
            System.out.println("Guests: " + booking.getGuests());
            System.out.println("Total Amount: $" + booking.getTotalAmount());
            System.out.println("Payment Option: " + booking.getPaymentOption());
            System.out.println("Status: " + booking.getBookingStatus());
        } else {
            System.out.println("No booking found with ID: " + id);
        }
    }

    private void viewBookingsByUserIdPrompt() throws SQLException {
        System.out.print("Enter User ID: ");
        long userId = Long.parseLong(scanner.nextLine());

        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        System.out.println("\n--- BOOKINGS FOR USER " + userId + " (" + bookings.size() + ") ---");
        for (Booking b : bookings) {
            System.out.println("ID: " + b.getBookingId()
                    + " | Hotel ID: " + (b.getHotel() != null ? b.getHotel().getHotelId() : "N/A")
                    + " | Room ID: " + (b.getRoom() != null ? b.getRoom().getRoomId() : "N/A")
                    + " | Dates: " + b.getCheckInDate() + " to " + b.getCheckOutDate()
                    + " | Amount: $" + b.getTotalAmount()
                    + " | Status: " + b.getBookingStatus());
        }
    }

    private void cancelBookingPrompt() throws SQLException {
        System.out.print("Enter Booking ID to Cancel: ");
        long id = Long.parseLong(scanner.nextLine());

        boolean success = bookingService.cancelBooking(id);
        if (success) {
            System.out.println("Booking cancelled successfully.");
        } else {
            System.out.println("Failed to cancel booking. Check if ID exists.");
        }
    }
}