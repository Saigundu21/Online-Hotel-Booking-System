package com.booking.hotel.service;

import com.booking.hotel.dao.HotelImageDAO;
import com.booking.hotel.dao.HotelImageDAOImpl;
import com.booking.hotel.model.HotelImage;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HotelImageServiceImpl implements HotelImageService {

    private static final Logger logger = Logger.getLogger(HotelImageServiceImpl.class.getName());
    private final HotelImageDAO hotelImageDAO;

    public HotelImageServiceImpl() {
        this.hotelImageDAO = new HotelImageDAOImpl();
    }

    public HotelImageServiceImpl(HotelImageDAO hotelImageDAO) {
        this.hotelImageDAO = hotelImageDAO;
    }

    @Override
    public boolean addHotelImage(HotelImage image) throws SQLException {
        try {
            if (image.getHotel() == null || image.getHotel().getHotelId() <= 0) {
                throw new IllegalArgumentException("A valid hotel reference is required to add an image.");
            }
            if (image.getImageUrl() == null || image.getImageUrl().trim().isEmpty()) {
                throw new IllegalArgumentException("Image URL cannot be empty.");
            }

            logger.info("Service: Adding image for hotel ID: " + image.getHotel().getHotelId());
            return hotelImageDAO.create(image);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to add hotel image", e);
            throw e;
        }
    }

    @Override
    public HotelImage getHotelImageById(long imageId) throws SQLException {
        try {
            logger.info("Service: Fetching hotel image by ID: " + imageId);
            return hotelImageDAO.findById(imageId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to find hotel image ID: " + imageId, e);
            throw e;
        }
    }

    @Override
    public List<HotelImage> getImagesByHotelId(long hotelId) throws SQLException {
        try {
            logger.info("Service: Fetching images for hotel ID: " + hotelId);
            return hotelImageDAO.findByHotelId(hotelId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to retrieve images for hotel ID: " + hotelId, e);
            throw e;
        }
    }

    @Override
    public List<HotelImage> getOrderedImagesByHotelId(long hotelId) throws SQLException {
        try {
            logger.info("Service: Fetching ordered images for hotel ID: " + hotelId);
            return hotelImageDAO.findByHotel(hotelId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to retrieve ordered images for hotel ID: " + hotelId, e);
            throw e;
        }
    }

    @Override
    public boolean deleteHotelImage(long imageId) throws SQLException {
        try {
            logger.info("Service: Deleting hotel image ID: " + imageId);
            return hotelImageDAO.delete(imageId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to delete hotel image ID: " + imageId, e);
            throw e;
        }
    }
}
