package com.booking.hotel;

import java.sql.Connection;
import java.sql.SQLException;

import static com.booking.hotel.util.JdbcUtil.getConnection;
import static com.mysql.cj.conf.PropertyKey.PASSWORD;

public class Main {

    public static void main(String[] args) {
        System.out.println("HOTEL_DB_PASSWORD = SET");

        try (Connection connection = getConnection()) {

            System.out.println("Database connected successfully!");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

}
