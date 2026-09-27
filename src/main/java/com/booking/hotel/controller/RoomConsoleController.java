package com.booking.hotel.controller;

import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.Room;
import com.booking.hotel.service.RoomService;
import com.booking.hotel.service.RoomServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomConsoleController {

    private static final Logger logger = Logger.getLogger(RoomConsoleController.class.getName());
    private final RoomService roomService;
    private final Scanner scanner;

    public RoomConsoleController() {
        this.roomService = new RoomServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    public void startRoomMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- ROOM MANAGEMENT MENU ---");
            System.out.println("1. Add New Room");
            System.out.println("2. View Room by ID");
            System.out.println("3. View Rooms by Hotel ID");
            System.out.println("4. Update Room Status");
            System.out.println("5. Delete Room");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addRoomPrompt();
                        break;
                    case "2":
                        viewRoomByIdPrompt();
                        break;
                    case "3":
                        viewRoomsByHotelPrompt();
                        break;
                    case "4":
                        updateRoomStatusPrompt();
                        break;
                    case "5":
                        deleteRoomPrompt();
                        break;
                    case "6":
                        running = false;
                        System.out.println("Exiting Room Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                logger.log(Level.SEVERE, "Room console controller error", e);
            }
        }
    }

    private void addRoomPrompt() {
        System.out.print("Enter Hotel ID: ");
        long hotelId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Room Number (e.g., R101): ");
        String roomNumber = scanner.nextLine();

        System.out.print("Enter Room Type (e.g., DELUXE, SUITE): ");
        String roomType = scanner.nextLine();

        System.out.print("Enter Capacity (number of guests): ");
        int capacity = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Base Price: ");
        BigDecimal basePrice = new BigDecimal(scanner.nextLine());

        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setCapacity(capacity);
        room.setBasePrice(basePrice);
        room.setStatus("AVAILABLE");

        boolean success = roomService.createRoom(room);
        if (success) {
            System.out.println("Success! Room added with ID: " + room.getRoomId());
        } else {
            System.out.println("Failed to add room.");
        }
    }

    private void viewRoomByIdPrompt() {
        System.out.print("Enter Room ID: ");
        long roomId = Long.parseLong(scanner.nextLine());

        Room room = roomService.getRoomById(roomId);
        if (room != null) {
            System.out.println("\n[Room Found]");
            System.out.println("Room ID: " + room.getRoomId());
            System.out.println("Hotel ID: " + room.getHotel().getHotelId());
            System.out.println("Room Number: " + room.getRoomNumber());
            System.out.println("Type: " + room.getRoomType());
            System.out.println("Capacity: " + room.getCapacity());
            System.out.println("Base Price: " + room.getBasePrice());
            System.out.println("Status: " + room.getStatus());
        } else {
            System.out.println("No room found with ID: " + roomId);
        }
    }

    private void viewRoomsByHotelPrompt() {
        System.out.print("Enter Hotel ID: ");
        long hotelId = Long.parseLong(scanner.nextLine());

        List<Room> rooms = roomService.getRoomsByHotelId(hotelId);
        System.out.println("\n--- ROOMS FOR HOTEL " + hotelId + " (" + rooms.size() + ") ---");
        for (Room r : rooms) {
            System.out.println("Room ID: " + r.getRoomId() + " | Number: " + r.getRoomNumber() + " | Type: " + r.getRoomType() + " | Status: " + r.getStatus());
        }
    }

    private void updateRoomStatusPrompt() {
        System.out.print("Enter Room ID: ");
        long roomId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter New Status (e.g., AVAILABLE, BOOKED, MAINTENANCE): ");
        String status = scanner.nextLine();

        boolean success = roomService.updateRoomStatus(roomId, status);
        if (success) {
            System.out.println("Room ID " + roomId + " status updated to " + status);
        } else {
            System.out.println("Failed to update room status.");
        }
    }

    private void deleteRoomPrompt() {
        System.out.print("Enter Room ID to delete: ");
        long roomId = Long.parseLong(scanner.nextLine());

        boolean success = roomService.deleteRoom(roomId);
        if (success) {
            System.out.println("Room ID " + roomId + " has been successfully deleted.");
        } else {
            System.out.println("Failed to delete room.");
        }
    }
}

