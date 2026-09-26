package com.booking.hotel.dao;

import com.booking.hotel.model.Hotel;
import com.booking.hotel.model.HotelImage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class HotelImageDAOImplTest {

    private HotelImageDAO hotelImageDAO;
    private long createdImageId;

    @BeforeEach
    void setUp() {
        hotelImageDAO = new HotelImageDAOImpl();
        createdImageId = 0;
    }

    @AfterEach
    void deleteTestImage() {

        if (createdImageId <= 0) {
            return;
        }

        try {
            hotelImageDAO.delete(createdImageId);
        } catch (SQLException ignored) {
        }
    }

    @Test
    void createInsertsImageAndSetsGeneratedId() throws SQLException {

        HotelImage image = newTestImage();

        boolean created = hotelImageDAO.create(image);
        createdImageId = image.getImageId();

        assertTrue(created);
        assertTrue(createdImageId > 0);
    }

    @Test
    void findByIdReturnsInsertedImage() throws SQLException {

        HotelImage image = newTestImage();

        hotelImageDAO.create(image);
        createdImageId = image.getImageId();

        HotelImage found = hotelImageDAO.findById(createdImageId);

        assertNotNull(found);
        assertEquals(createdImageId, found.getImageId());
        assertEquals(image.getImageUrl(), found.getImageUrl());
        assertEquals(image.getCaption(), found.getCaption());
    }

    @Test
    void findByHotelIdReturnsImages() throws SQLException {

        HotelImage image = newTestImage();

        hotelImageDAO.create(image);
        createdImageId = image.getImageId();

        long hotelId = image.getHotel().getHotelId();

        var images = hotelImageDAO.findByHotelId(hotelId);

        assertNotNull(images);

        assertTrue(
                images.stream()
                        .anyMatch(i -> i.getImageId() == createdImageId)
        );
    }

    @Test
    void deleteRemovesImage() throws SQLException {

        HotelImage image = newTestImage();

        hotelImageDAO.create(image);
        createdImageId = image.getImageId();

        assertTrue(hotelImageDAO.delete(createdImageId));

        assertNull(hotelImageDAO.findById(createdImageId));

        createdImageId = 0;
    }

    private HotelImage newTestImage() {

        Hotel hotel = new Hotel();
        hotel.setHotelId(1);

        HotelImage image = new HotelImage();

        image.setHotel(hotel);
        image.setImageUrl(
                "https://example.com/test-" + System.currentTimeMillis() + ".jpg"
        );
        image.setCaption("Test hotel image");

        return image;
    }
}
