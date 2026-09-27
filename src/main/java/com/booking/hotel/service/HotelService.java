package com.booking.hotel.service;

import com.booking.hotel.model.Hotel;
import java.sql.SQLException;
import java.util.List;

public interface HotelService {
    boolean registerHotel(Hotel hotel) throws SQLException;
    Hotel getHotelById(long hotelId) throws SQLException;
    List<Hotel> getAllHotels() throws SQLException;
    boolean updateHotel(Hotel hotel) throws SQLException;
    boolean deleteHotel(long hotelId) throws SQLException;
}