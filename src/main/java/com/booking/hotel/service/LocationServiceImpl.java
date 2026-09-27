package com.booking.hotel.service;

import com.booking.hotel.dao.LocationDAO;
import com.booking.hotel.dao.LocationDAOImpl;
import com.booking.hotel.model.Location;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocationServiceImpl implements LocationService {

    private static final Logger logger = Logger.getLogger(LocationServiceImpl.class.getName());
    private final LocationDAO locationDAO;

    public LocationServiceImpl() {
        this.locationDAO = new LocationDAOImpl();
    }

    public LocationServiceImpl(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }

    @Override
    public boolean createLocation(Location location) {
        try {
            if (location.getName() == null || location.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Location name cannot be empty.");
            }
            if (location.getType() == null || location.getType().trim().isEmpty()) {
                throw new IllegalArgumentException("Location type cannot be empty.");
            }

            logger.info("Service: Creating location: " + location.getName() + " (" + location.getType() + ")");
            return locationDAO.create(location);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to create location", e);
            return false;
        }
    }

    @Override
    public Location getLocationById(long locationId) {
        try {
            logger.info("Service: Fetching location by ID: " + locationId);
            return locationDAO.findById(locationId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to find location ID: " + locationId, e);
            return null;
        }
    }

    @Override
    public List<Location> getAllLocations() {
        try {
            logger.info("Service: Fetching all locations");
            return locationDAO.findAll();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to retrieve all locations", e);
            return List.of();
        }
    }

    @Override
    public boolean updateLocation(Location location) {
        try {
            if (location.getLocationId() <= 0) {
                throw new IllegalArgumentException("Invalid location ID for update.");
            }
            logger.info("Service: Updating location ID: " + location.getLocationId());
            return locationDAO.update(location);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to update location ID: " + location.getLocationId(), e);
            return false;
        }
    }

    @Override
    public boolean deleteLocation(long locationId) {
        try {
            logger.info("Service: Deleting location ID: " + locationId);
            return locationDAO.delete(locationId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to delete location ID: " + locationId, e);
            return false;
        }
    }

    @Override
    public String resolveFullPath(long locationId) {
        try {
            logger.info("Service: Resolving full path for location ID: " + locationId);
            return locationDAO.resolveFullPath(locationId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Service failed to resolve full path for location ID: " + locationId, e);
            return "";
        }
    }
}
