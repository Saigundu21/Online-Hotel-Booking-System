package com.booking.hotel.dao;

import com.booking.hotel.model.Hotel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HotelDAOImplTest {

    private HotelDAO hotelDAO;
    private long createdHotelId;

    @BeforeEach
    void setUp() {
        hotelDAO = new HotelDAOImpl();
        createdHotelId = 0;
    }

    @AfterEach
    void deleteTestHotel() {
        if (createdHotelId <= 0) {
            return;
        }
        try {
            hotelDAO.delete(createdHotelId);
        } catch (SQLException ignored) {
        }
    }

    @Test
    void createInsertsHotelAndSetsGeneratedId() throws SQLException {
        Hotel hotel = newTestHotel();

        boolean created = hotelDAO.create(hotel);
        createdHotelId = hotel.getHotelId();

        assertTrue(created);
        assertTrue(createdHotelId > 0);
    }

    @Test
    void findByIdReturnsInsertedHotel() throws SQLException {
        Hotel hotel = newTestHotel();
        hotelDAO.create(hotel);
        createdHotelId = hotel.getHotelId();

        Hotel found = hotelDAO.findById(createdHotelId);

        assertNotNull(found);
        assertEquals(createdHotelId, found.getHotelId());
        assertEquals(hotel.getName(), found.getName());
        assertEquals(hotel.getDescription(), found.getDescription());
        assertEquals(hotel.getAddress(), found.getAddress());
        assertEquals(0, hotel.getStarRating().compareTo(found.getStarRating()));
        assertEquals(hotel.getAmenities(), found.getAmenities());
        assertEquals(hotel.getStatus(), found.getStatus());
    }

    private Hotel newTestHotel() {
        String unique = UUID.randomUUID().toString();
        Hotel hotel = new Hotel();
        hotel.setName("Hotel " + unique);
        hotel.setDescription("Test hotel");
        hotel.setAddress("1 Test Street");
        hotel.setStarRating(new BigDecimal("4.5"));
        hotel.setAmenities("WiFi");
        hotel.setStatus("ACTIVE");
        return hotel;
    }
}

