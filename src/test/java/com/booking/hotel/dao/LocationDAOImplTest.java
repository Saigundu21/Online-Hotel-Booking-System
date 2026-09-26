package com.booking.hotel.dao;

import com.booking.hotel.model.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LocationDAOImplTest {

    private LocationDAO locationDAO;
    private long createdLocationId;

    @BeforeEach
    void setUp() {
        locationDAO = new LocationDAOImpl();
        createdLocationId = 0;
    }

    @AfterEach
    void deleteTestLocation() {

        if (createdLocationId <= 0) {
            return;
        }

        try {
            locationDAO.delete(createdLocationId);
        } catch (SQLException ignored) {
        }
    }

    @Test
    void createInsertsLocationAndSetsGeneratedId() throws SQLException {

        Location location = newTestLocation();

        boolean created = locationDAO.create(location);
        createdLocationId = location.getLocationId();

        assertTrue(created);
        assertTrue(createdLocationId > 0);
    }

    @Test
    void findByIdReturnsInsertedLocation() throws SQLException {

        Location location = newTestLocation();

        locationDAO.create(location);
        createdLocationId = location.getLocationId();

        Location found = locationDAO.findById(createdLocationId);

        assertNotNull(found);
        assertEquals(createdLocationId, found.getLocationId());
        assertEquals(location.getName(), found.getName());
        assertEquals(location.getType(), found.getType());
    }

    @Test
    void findAllReturnsLocations() throws SQLException {

        Location location = newTestLocation();

        locationDAO.create(location);
        createdLocationId = location.getLocationId();

        var locations = locationDAO.findAll();

        assertNotNull(locations);

        assertTrue(
                locations.stream()
                        .anyMatch(l -> l.getLocationId() == createdLocationId)
        );
    }

    @Test
    void updateChangesLocation() throws SQLException {

        Location location = newTestLocation();

        locationDAO.create(location);
        createdLocationId = location.getLocationId();

        location.setName("Updated Location");
        location.setType("CITY");

        assertTrue(locationDAO.update(location));

        Location updated = locationDAO.findById(createdLocationId);

        assertNotNull(updated);
        assertEquals("Updated Location", updated.getName());
        assertEquals("CITY", updated.getType());
    }

    @Test
    void deleteRemovesLocation() throws SQLException {

        Location location = newTestLocation();

        locationDAO.create(location);
        createdLocationId = location.getLocationId();

        assertTrue(locationDAO.delete(createdLocationId));

        assertNull(locationDAO.findById(createdLocationId));

        createdLocationId = 0;
    }

    private Location newTestLocation() {

        Location location = new Location();

        location.setName("Test Location " + System.currentTimeMillis());
        location.setType("CITY");
        location.setParent(null);

        return location;
    }
}
