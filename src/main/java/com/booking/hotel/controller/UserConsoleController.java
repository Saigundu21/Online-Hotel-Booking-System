package com.booking.hotel.controller;

import com.booking.hotel.model.User;
import com.booking.hotel.service.UserService;
import com.booking.hotel.service.UserServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserConsoleController {

    private static final Logger logger = Logger.getLogger(UserConsoleController.class.getName());
    private final UserService userService;
    private final Scanner scanner;

    public UserConsoleController() {
        this.userService = new UserServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    // Interactive menu loop for Core Java console testing
    public void startUserMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- USER MANAGEMENT MENU ---");
            System.out.println("1. Register New User");
            System.out.println("2. View User by ID");
            System.out.println("3. View All Users");
            System.out.println("4. Back to Main Menu / Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        registerPrompt();
                        break;
                    case "2":
                        viewUserByIdPrompt();
                        break;
                    case "3":
                        viewAllUsersPrompt();
                        break;
                    case "4":
                        running = false;
                        System.out.println("Exiting User Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                logger.log(Level.SEVERE, "Console controller error", e);
            }
        }
    }

    private void registerPrompt() throws SQLException {
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();

        // Role is securely fixed to CUSTOMER (preventing unauthorized Admin creation)
        User newUser = new User(0, name, email, password, phone, "CUSTOMER", "ACTIVE");
        boolean success = userService.registerUser(newUser);

        if (success) {
            System.out.println("Success! User registered with ID: " + newUser.getUserId());
        } else {
            System.out.println("Failed to register user.");
        }
    }

    private void viewUserByIdPrompt() throws SQLException {
        System.out.print("Enter User ID: ");
        long id = Long.parseLong(scanner.nextLine());

        User user = userService.getUserById(id);
        if (user != null) {
            System.out.println("\n[User Found]");
            System.out.println("ID: " + user.getUserId());
            System.out.println("Name: " + user.getFullName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Phone: " + user.getPhone());
            System.out.println("Role: " + user.getRole());
        } else {
            System.out.println("No user found with ID: " + id);
        }
    }

    private void viewAllUsersPrompt() throws SQLException {
        List<User> users = userService.getAllUsers();
        System.out.println("\n--- ALL USERS (" + users.size() + ") ---");
        for (User u : users) {
            System.out.println("ID: " + u.getUserId() + " | Name: " + u.getFullName() + " | Email: " + u.getEmail() + " | Role: " + u.getRole());
        }
    }
}