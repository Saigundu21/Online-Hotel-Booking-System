package com.booking.hotel.dao;

import com.booking.hotel.model.User;
import com.booking.hotel.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAOImpl implements UserDAO {

    private static final Logger logger =
            Logger.getLogger(UserDAOImpl.class.getName());

    private static final String SQL_INSERT_USER =
            "INSERT INTO `user` (full_name, email, password_hash, phone, role, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM `user` WHERE user_id = ?";

    private static final String SQL_FIND_ALL =
            "SELECT * FROM `user`";

    private static final String SQL_UPDATE =
            "UPDATE `user` SET full_name = ?, email = ?, password_hash = ?, "
                    + "phone = ?, role = ?, status = ? WHERE user_id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM `user` WHERE user_id = ?";

    @Override
    public boolean create(User user) throws SQLException {

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SQL_INSERT_USER,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getRole());
            statement.setString(6, user.getStatus());

            logger.info("Creating new user");

            int rows = statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setUserId(keys.getLong(1));
                }
            }

            if (rows > 0) {
                logger.info("User created successfully. User ID: "
                        + user.getUserId());
            }

            return rows > 0;

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Failed to create user",
                    e
            );

            throw e;
        }
    }

    @Override
    public User findById(long userId) throws SQLException {

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SQL_FIND_BY_ID)) {

            statement.setLong(1, userId);

            logger.info("Searching for user with ID: " + userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info("User found with ID: " + userId);

                    return mapRow(resultSet);
                }

                logger.warning("No user found with ID: " + userId);

                return null;
            }

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Failed to find user with ID: " + userId,
                    e
            );

            throw e;
        }
    }

    @Override
    public List<User> findAll() throws SQLException {

        List<User> users = new ArrayList<>();

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SQL_FIND_ALL)) {

            logger.info("Fetching all users");

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    users.add(mapRow(resultSet));
                }
            }

            if (users.isEmpty()) {
                logger.warning("No users found");
            } else {
                logger.info("Users retrieved successfully. Count: "
                        + users.size());
            }

            return users;

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Failed to retrieve users",
                    e
            );

            throw e;
        }
    }

    @Override
    public boolean update(User user) throws SQLException {

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SQL_UPDATE)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getRole());
            statement.setString(6, user.getStatus());
            statement.setLong(7, user.getUserId());

            logger.info("Updating user with ID: " + user.getUserId());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                logger.info("User updated successfully. User ID: "
                        + user.getUserId());
            } else {
                logger.warning("No user was updated for ID: "
                        + user.getUserId());
            }

            return rows > 0;

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Failed to update user with ID: "
                            + user.getUserId(),
                    e
            );

            throw e;
        }
    }

    @Override
    public boolean delete(long userId) throws SQLException {

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SQL_DELETE)) {

            statement.setLong(1, userId);

            logger.info("Deleting user with ID: " + userId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                logger.info("User deleted successfully. User ID: " + userId);
            } else {
                logger.warning("No user was deleted for ID: " + userId);
            }

            return rows > 0;

        } catch (SQLException e) {

            logger.log(
                    Level.SEVERE,
                    "Failed to delete user with ID: " + userId,
                    e
            );

            throw e;
        }
    }

    private User mapRow(ResultSet resultSet) throws SQLException {

        User user = new User();

        user.setUserId(resultSet.getLong("user_id"));
        user.setFullName(resultSet.getString("full_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password_hash"));
        user.setPhone(resultSet.getString("phone"));
        user.setRole(resultSet.getString("role"));
        user.setStatus(resultSet.getString("status"));

        return user;
    }
}