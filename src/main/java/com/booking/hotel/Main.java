package com.booking.hotel;

import com.booking.hotel.controller.UserConsoleController;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Hotel Booking System...");

        UserConsoleController controller = new UserConsoleController();
        controller.startUserMenu();
    }
}




