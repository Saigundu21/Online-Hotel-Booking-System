package com.booking.hotel.service;

import com.booking.hotel.model.Location;
import java.util.List;

public interface LocationService {
    boolean createLocation(Location location);
    Location getLocationById(long locationId);
    List<Location> getAllLocations();
    boolean updateLocation(Location location);
    boolean deleteLocation(long locationId);
    String resolveFullPath(long locationId);
}
