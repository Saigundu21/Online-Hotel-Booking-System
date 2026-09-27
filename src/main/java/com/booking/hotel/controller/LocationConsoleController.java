package com.booking.hotel.controller;

import com.booking.hotel.model.Location;
import com.booking.hotel.service.LocationService;
import com.booking.hotel.service.LocationServiceImpl;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocationConsoleController {

    private static final Logger logger = Logger.getLogger(LocationConsoleController.class.getName());
    private final LocationService locationService;
    private final Scanner scanner;

    public LocationConsoleController() {
        this.locationService = new LocationServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    public void startLocationMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- LOCATION MANAGEMENT MENU ---");
            System.out.println("1. Add New Location");
            System.out.println("2. View Location by ID");
            System.out.println("3. View All Locations");
            System.out.println("4. Resolve Full Path (e.g., City, State, Country)");
            System.out.println("5. Delete Location");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addLocationPrompt();
                        break;
                    case "2":
                        viewLocationByIdPrompt();
                        break;
                    case "3":
                        viewAllLocationsPrompt();
                        break;
                    case "4":
                        resolveFullPathPrompt();
                        break;
                    case "5":
                        deleteLocationPrompt();
                        break;
                    case "6":
                        running = false;
                        System.out.println("Exiting Location Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                logger.log(Level.SEVERE, "Location console controller error", e);
            }
        }
    }

    private void addLocationPrompt() {
        System.out.print("Enter Location Name (e.g., New York): ");
        String name = scanner.nextLine();

        System.out.print("Enter Location Type (e.g., COUNTRY, STATE, CITY): ");
        String type = scanner.nextLine();

        System.out.print("Does this location have a parent ID? (y/n): ");
        String hasParent = scanner.nextLine();

        Location parent = null;
        if (hasParent.equalsIgnoreCase("y")) {
            System.out.print("Enter Parent Location ID: ");
            long parentId = Long.parseLong(scanner.nextLine());
            parent = new Location();
            parent.setLocationId(parentId);
        }

        Location location = new Location();
        location.setName(name);
        location.setType(type);
        location.setParent(parent);

        boolean success = locationService.createLocation(location);
        if (success) {
            System.out.println("Success! Location added with ID: " + location.getLocationId());
        } else {
            System.out.println("Failed to add location.");
        }
    }

    private void viewLocationByIdPrompt() {
        System.out.print("Enter Location ID: ");
        long locationId = Long.parseLong(scanner.nextLine());

        Location location = locationService.getLocationById(locationId);
        if (location != null) {
            System.out.println("\n[Location Found]");
            System.out.println("Location ID: " + location.getLocationId());
            System.out.println("Name: " + location.getName());
            System.out.println("Type: " + location.getType());
            if (location.getParent() != null) {
                System.out.println("Parent ID: " + location.getParent().getLocationId());
            } else {
                System.out.println("Parent ID: None (Top-level)");
            }
        } else {
            System.out.println("No location found with ID: " + locationId);
        }
    }

    private void viewAllLocationsPrompt() {
        List<Location> locations = locationService.getAllLocations();
        System.out.println("\n--- ALL LOCATIONS (" + locations.size() + ") ---");
        for (Location loc : locations) {
            String parentInfo = loc.getParent() != null ? " | Parent ID: " + loc.getParent().getLocationId() : "";
            System.out.println("ID: " + loc.getLocationId() + " | Name: " + loc.getName() + " | Type: " + loc.getType() + parentInfo);
        }
    }

    private void resolveFullPathPrompt() {
        System.out.print("Enter Location ID (e.g., City ID): ");
        long locationId = Long.parseLong(scanner.nextLine());

        String path = locationService.resolveFullPath(locationId);
        if (path != null && !path.isEmpty()) {
            System.out.println("\nResolved Full Path: " + path);
        } else {
            System.out.println("Could not resolve path or location not found.");
        }
    }

    private void deleteLocationPrompt() {
        System.out.print("Enter Location ID to delete: ");
        long locationId = Long.parseLong(scanner.nextLine());

        boolean success = locationService.deleteLocation(locationId);
        if (success) {
            System.out.println("Location ID " + locationId + " has been successfully deleted.");
        } else {
            System.out.println("Failed to delete location.");
        }
    }
}
