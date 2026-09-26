package com.booking.hotel.dao;

import com.booking.hotel.model.Location;
import com.booking.hotel.util.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocationDAOImpl implements LocationDAO {

    private static final Logger logger =
            Logger.getLogger(LocationDAOImpl.class.getName());

    private static final String INSERT_SQL =
            "INSERT INTO location (name, type, parent_id) VALUES (?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT location_id, name, type, parent_id " +
                    "FROM location WHERE location_id = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT location_id, name, type, parent_id FROM location";

    private static final String UPDATE_SQL =
            "UPDATE location SET name = ?, type = ?, parent_id = ? " +
                    "WHERE location_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM location WHERE location_id = ?";

    @Override
    public boolean create(Location location) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, location.getName());
            stmt.setString(2, location.getType());

            if (location.getParent() == null) {
                stmt.setNull(3, Types.BIGINT);
            } else {
                stmt.setLong(3,
                        location.getParent().getLocationId());
            }

            logger.fine("Inserting location: " + location.getName());

            int rows = stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {

                if (keys.next()) {
                    location.setLocationId(keys.getLong(1));
                }
            }

            if (rows > 0) {
                logger.info("Inserted location id="
                        + location.getLocationId());
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to insert location", e);
            throw e;
        }
    }

    @Override
    public Location findById(long locationId) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setLong(1, locationId);

            logger.fine("Finding location id=" + locationId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapRow(rs);
                }
            }

            logger.warning("No location found for id=" + locationId);
            return null;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to find location id=" + locationId, e);
            throw e;
        }
    }

    @Override
    public List<Location> findAll() throws SQLException {

        List<Location> list = new ArrayList<>();

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {

            logger.fine("Selecting all locations");

            while (rs.next()) {
                list.add(mapRow(rs));
            }

            if (list.isEmpty()) {
                logger.warning("No locations found");
            }

            return list;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to find all locations", e);
            throw e;
        }
    }

    @Override
    public boolean update(Location location) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(UPDATE_SQL)) {

            stmt.setString(1, location.getName());
            stmt.setString(2, location.getType());

            if (location.getParent() == null) {
                stmt.setNull(3, Types.BIGINT);
            } else {
                stmt.setLong(3,
                        location.getParent().getLocationId());
            }

            stmt.setLong(4, location.getLocationId());

            logger.fine("Updating location id="
                    + location.getLocationId());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("Updated location id="
                        + location.getLocationId());
            } else {
                logger.warning("No location updated for id="
                        + location.getLocationId());
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to update location id="
                            + location.getLocationId(), e);
            throw e;
        }
    }

    @Override
    public boolean delete(long locationId) throws SQLException {

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(DELETE_SQL)) {

            stmt.setLong(1, locationId);

            logger.fine("Deleting location id=" + locationId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("Deleted location id=" + locationId);
            } else {
                logger.warning(
                        "No location found to delete for id="
                                + locationId);
            }

            return rows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Failed to delete location id=" + locationId, e);
            throw e;
        }
    }

    @Override
    public <__TMP__> __TMP__ resolveFullPath(long locationId) {
        return null;
    }

    private Location mapRow(ResultSet rs) throws SQLException {

        Location loc = new Location();

        loc.setLocationId(rs.getLong("location_id"));
        loc.setName(rs.getString("name"));
        loc.setType(rs.getString("type"));

        long parentId = rs.getLong("parent_id");

        if (!rs.wasNull()) {

            Location parent = new Location();
            parent.setLocationId(parentId);
            loc.setParent(parent);
        }

        return loc;
    }
}