package com.booking.hotel;

import com.booking.hotel.controller.BookingConsoleController;
import com.booking.hotel.controller.HotelConsoleController;
import com.booking.hotel.controller.HotelImageConsoleController;
import com.booking.hotel.controller.LocationConsoleController;
import com.booking.hotel.controller.PaymentConsoleController;
import com.booking.hotel.controller.ReviewConsoleController;
import com.booking.hotel.controller.RoomConsoleController;
import com.booking.hotel.controller.UserConsoleController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Starting Hotel Booking System...");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. User Management");
            System.out.println("2. Hotel Management");
            System.out.println("3. Location Management");
            System.out.println("4. Hotel Image Management");
            System.out.println("5. Room Management");
            System.out.println("6. Booking Management");
            System.out.println("7. Payment Management");
            System.out.println("8. Review Management");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    UserConsoleController userController = new UserConsoleController();
                    userController.startUserMenu();
                    break;
                case "2":
                    HotelConsoleController hotelController = new HotelConsoleController();
                    hotelController.startHotelMenu();
                    break;
                case "3":
                    LocationConsoleController locationController = new LocationConsoleController();
                    locationController.startLocationMenu();
                    break;
                case "4":
                    HotelImageConsoleController imageController = new HotelImageConsoleController();
                    imageController.startHotelImageMenu();
                    break;
                case "5":
                    RoomConsoleController roomController = new RoomConsoleController();
                    roomController.startRoomMenu();
                    break;
                case "6":
                    BookingConsoleController bookingController = new BookingConsoleController();
                    bookingController.startBookingMenu();
                    break;
                case "7":
                    PaymentConsoleController paymentController = new PaymentConsoleController();
                    paymentController.startPaymentMenu();
                    break;
                case "8":
                    ReviewConsoleController reviewController = new ReviewConsoleController();
                    reviewController.startReviewMenu();
                    break;
                case "9":
                    running = false;
                    System.out.println("Shutting down application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
}