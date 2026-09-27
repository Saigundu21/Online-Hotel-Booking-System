package com.booking.hotel.service;

import com.booking.hotel.dao.HotelDAO;
import com.booking.hotel.dao.HotelDAOImpl;
import com.booking.hotel.model.Hotel;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HotelServiceImpl implements HotelService {

    private static final Logger logger = Logger.getLogger(HotelServiceImpl.class.getName());
    private final HotelDAO hotelDAO;

    public HotelServiceImpl() {
        this.hotelDAO = new HotelDAOImpl();
    }

    public HotelServiceImpl(HotelDAO hotelDAO) {
        this.hotelDAO = hotelDAO;
    }

    @Override
    public boolean registerHotel(Hotel hotel) throws SQLException {
        try {
            if (hotel.getName() == null || hotel.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Hotel name cannot be empty.");
            }
            logger.info("Service: Registering new hotel: " + hotel.getName());
            return hotelDAO.create(hotel);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to register hotel", e);
            throw e;
        }
    }

    @Override
    public Hotel getHotelById(long hotelId) throws SQLException {
        try {
            logger.info("Service: Fetching hotel by ID: " + hotelId);
            return hotelDAO.findById(hotelId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to find hotel ID: " + hotelId, e);
            throw e;
        }
    }

    @Override
    public List<Hotel> getAllHotels() throws SQLException {
        try {
            logger.info("Service: Fetching all hotels");
            return hotelDAO.findAll();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to retrieve hotels", e);
            throw e;
        }
    }

    @Override
    public boolean updateHotel(Hotel hotel) throws SQLException {
        try {
            if (hotel.getHotelId() <= 0) {
                throw new IllegalArgumentException("Invalid hotel ID for update.");
            }
            logger.info("Service: Updating hotel ID: " + hotel.getHotelId());
            return hotelDAO.update(hotel);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to update hotel ID: " + hotel.getHotelId(), e);
            throw e;
        }
    }

    @Override
    public boolean deleteHotel(long hotelId) throws SQLException {
        try {
            logger.info("Service: Deleting hotel ID: " + hotelId);
            return hotelDAO.delete(hotelId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to delete hotel ID: " + hotelId, e);
            throw e;
        }
    }
}
