package com.booking.hotel;

import com.booking.hotel.controller.BookingConsoleController;
import com.booking.hotel.controller.HotelConsoleController;
import com.booking.hotel.controller.HotelImageConsoleController;
import com.booking.hotel.controller.LocationConsoleController;
import com.booking.hotel.controller.PaymentConsoleController;
import com.booking.hotel.controller.ReviewConsoleController;
import com.booking.hotel.controller.RoomConsoleController;
import com.booking.hotel.controller.UserConsoleController;
import com.booking.hotel.model.User;
import com.booking.hotel.service.UserService;
import com.booking.hotel.service.UserServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserServiceImpl();
        User currentUser = null;

        printHeader("LUXURY HOTEL RESERVATION & MANAGEMENT SYSTEM");

        // Step 1: Authentication Gate (Login / Register / Exit)
        boolean authenticated = false;
        while (!authenticated) {
            System.out.println("\n==========================================");
            System.out.println("            AUTHENTICATION PORTAL         ");
            System.out.println("==========================================");
            System.out.println("1. Secure Login");
            System.out.println("2. Create New Account (Customer Registration)");
            System.out.println("3. Exit Application");
            System.out.print("Please select an option [1-3]: ");

            String authChoice = scanner.nextLine();

            try {
                switch (authChoice) {
                    case "1":
                        System.out.print("Enter Registered Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter Password: ");
                        String password = scanner.nextLine();

                        currentUser = authenticateUser(userService, email, password);
                        if (currentUser != null) {
                            System.out.println("\n==========================================");
                            System.out.println("          LOGIN SUCCESSFUL                 ");
                            System.out.println("==========================================");
                            System.out.println("Welcome back, " + currentUser.getFullName() + "!");
                            System.out.println("Assigned Role: " + currentUser.getRole().toUpperCase());
                            System.out.println("------------------------------------------");
                            authenticated = true;
                        } else {
                            System.out.println("\n[Authentication Error]: Invalid email or password. Please verify your credentials.");
                        }
                        break;
                    case "2":
                        currentUser = registerUserPrompt(scanner, userService);
                        if (currentUser != null) {
                            System.out.println("\n==========================================");
                            System.out.println("     REGISTRATION & LOGIN SUCCESSFUL      ");
                            System.out.println("==========================================");
                            System.out.println("Welcome to our platform, " + currentUser.getFullName() + "!");
                            System.out.println("Your customer account is active and ready.");
                            System.out.println("------------------------------------------");
                            authenticated = true;
                        }
                        break;
                    case "3":
                        printFooter();
                        scanner.close();
                        return;
                    default:
                        System.out.println("\n[Invalid Selection]: Please enter a valid choice between 1 and 3.");
                }
            } catch (Exception e) {
                System.out.println("\n[System Error]: " + e.getMessage());
                logger.log(Level.SEVERE, "Authentication gateway error", e);
            }
        }

        // Step 2: Role-Based Main Menu (Unlocked after login/registration)
        boolean running = true;
        while (running) {
            String role = currentUser.getRole() != null ? currentUser.getRole().toUpperCase() : "CUSTOMER";

            if ("ADMIN".equals(role)) {
                // --- ADMIN MENU ---
                System.out.println("\n==========================================");
                System.out.println("         ADMIN MANAGEMENT CONSOLE         ");
                System.out.println("==========================================");
                System.out.println("1. User Management");
                System.out.println("2. Hotel Management");
                System.out.println("3. Location Management");
                System.out.println("4. Hotel Image Management");
                System.out.println("5. Room Management");
                System.out.println("6. Booking Management");
                System.out.println("7. Payment Management");
                System.out.println("8. Review Management");
                System.out.println("9. Logout & Exit System");
                System.out.print("Select administrative option [1-9]: ");

                String choice = scanner.nextLine();

                try {
                    switch (choice) {
                        case "1":
                            new UserConsoleController().startUserMenu();
                            break;
                        case "2":
                            new HotelConsoleController().startHotelMenu();
                            break;
                        case "3":
                            new LocationConsoleController().startLocationMenu();
                            break;
                        case "4":
                            new HotelImageConsoleController().startHotelImageMenu();
                            break;
                        case "5":
                            new RoomConsoleController().startRoomMenu();
                            break;
                        case "6":
                            new BookingConsoleController().startBookingMenu();
                            break;
                        case "7":
                            new PaymentConsoleController().startPaymentMenu();
                            break;
                        case "8":
                            new ReviewConsoleController().startReviewMenu();
                            break;
                        case "9":
                            running = false;
                            printFooter();
                            break;
                        default:
                            System.out.println("\n[Invalid Option]: Please choose a valid number from 1 to 9.");
                    }
                } catch (Exception e) {
                    System.out.println("\n[Console Error]: " + e.getMessage());
                    logger.log(Level.SEVERE, "Admin menu runtime exception", e);
                }

            } else {
                // --- CUSTOMER MENU ---
                System.out.println("\n==========================================");
                System.out.println("           CUSTOMER BOOKING HUB           ");
                System.out.println("==========================================");
                System.out.println("1. Room Booking & Search Operations");
                System.out.println("2. Payment Transactions & History");
                System.out.println("3. Hotel Reviews & Ratings");
                System.out.println("4. View Customer Profile Details");
                System.out.println("5. Logout & Exit System");
                System.out.print("Select client option [1-5]: ");

                String choice = scanner.nextLine();

                try {
                    switch (choice) {
                        case "1":
                            new BookingConsoleController().startBookingMenu();
                            break;
                        case "2":
                            new PaymentConsoleController().startPaymentMenu();
                            break;
                        case "3":
                            new ReviewConsoleController().startReviewMenu();
                            break;
                        case "4":
                            printUserProfile(currentUser);
                            break;
                        case "5":
                            running = false;
                            printFooter();
                            break;
                        default:
                            System.out.println("\n[Invalid Option]: Please choose a valid number from 1 to 5.");
                    }
                } catch (Exception e) {
                    System.out.println("\n[Console Error]: " + e.getMessage());
                    logger.log(Level.SEVERE, "Customer menu runtime exception", e);
                }
            }
        }
        scanner.close();
    }

    private static User authenticateUser(UserService userService, String email, String password) throws SQLException {
        List<User> users = userService.getAllUsers();
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    private static User registerUserPrompt(Scanner scanner, UserService userService) throws SQLException {
        System.out.println("\n--- NEW CUSTOMER REGISTRATION ---");
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Email Address: ");
        String email = scanner.nextLine();

        System.out.print("Create Password: ");
        String password = scanner.nextLine();

        System.out.print("Enter Contact Phone: ");
        String phone = scanner.nextLine();

        // Role is securely fixed to CUSTOMER
        User newUser = new User(0, name, email, password, phone, "CUSTOMER", "ACTIVE");
        boolean success = userService.registerUser(newUser);

        if (success) {
            return newUser;
        } else {
            System.out.println("\n[Registration Failed]: Unable to register user. Email might already be in use.");
            return null;
        }
    }

    private static void printUserProfile(User user) {
        System.out.println("\n==========================================");
        System.out.println("            CUSTOMER PROFILE              ");
        System.out.println("==========================================");
        System.out.println(" User ID       : " + user.getUserId());
        System.out.println(" Full Name     : " + user.getFullName());
        System.out.println(" Email Address : " + user.getEmail());
        System.out.println(" Contact Phone : " + user.getPhone());
        System.out.println(" Account Status: " + user.getStatus());
        System.out.println(" Member Role   : " + user.getRole());
        System.out.println("------------------------------------------");
    }

    private static void printHeader(String title) {
        System.out.println("##########################################");
        System.out.println("  " + title);
        System.out.println("##########################################");
    }

    private static void printFooter() {
        System.out.println("\n==========================================");
        System.out.println("  Thank you for using our Reservation App. ");
        System.out.println("       System Successfully Shut Down.     ");
        System.out.println("========================================ED");
    }
}