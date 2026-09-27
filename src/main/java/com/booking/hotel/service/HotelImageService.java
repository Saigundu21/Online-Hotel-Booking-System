package com.booking.hotel.service;

import com.booking.hotel.model.HotelImage;
import java.sql.SQLException;
import java.util.List;

public interface HotelImageService {
    boolean addHotelImage(HotelImage image) throws SQLException;
    HotelImage getHotelImageById(long imageId) throws SQLException;
    List<HotelImage> getImagesByHotelId(long hotelId) throws SQLException;
    List<HotelImage> getOrderedImagesByHotelId(long hotelId) throws SQLException;
    boolean deleteHotelImage(long imageId) throws SQLException;
}
