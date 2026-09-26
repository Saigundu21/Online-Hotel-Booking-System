package com.booking.hotel.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcUtil {

    // Records whether opening a database connection succeeded or failed.
    private static final Logger logger =
            Logger.getLogger(JdbcUtil.class.getName());

    private static final String URL =
            "jdbc:mysql://localhost:3306/hotel_booking_system";

    private static final String USER = "root";

    private static final String PASSWORD =
            System.getenv("HOTEL_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null || PASSWORD.isBlank()) {
            logger.severe("HOTEL_DB_PASSWORD environment variable is not set");
            throw new SQLException(
                    "HOTEL_DB_PASSWORD environment variable is not set"
            );
        }

        try {

            logger.info("Attempting to connect to the database");

            Connection connection =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            logger.info("Database connection established successfully");

            return connection;

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Database connection failed for user: " + USER,
                    e
            );

            throw e;
        }
    }

    public static void main(String[] args) {

        try (Connection connection = getConnection()) {

            System.out.println("Database connected successfully!");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}

