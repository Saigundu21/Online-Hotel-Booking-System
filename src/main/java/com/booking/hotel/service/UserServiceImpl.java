package com.booking.hotel.service;

import com.booking.hotel.dao.UserDAO;
import com.booking.hotel.dao.UserDAOImpl;
import com.booking.hotel.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    private static final Logger logger =
            Logger.getLogger(UserServiceImpl.class.getName());

    private final UserDAO userDAO;

    // Default constructor instantiates the standard UserDAOImpl
    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    // Overloaded constructor useful for unit testing (Dependency Injection)
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean registerUser(User user) throws SQLException {
        try {
            // Business rule checks can be added here (e.g., verifying email format, hashing password)
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("User email cannot be empty.");
            }

            logger.info("Service: Registering new user with email: " + user.getEmail());
            return userDAO.create(user);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to register user", e);
            throw e;
        }
    }

    @Override
    public User getUserById(long userId) throws SQLException {
        try {
            logger.info("Service: Fetching user by ID: " + userId);
            return userDAO.findById(userId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to find user by ID: " + userId, e);
            throw e;
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        try {
            logger.info("Service: Fetching all users");
            return userDAO.findAll();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to retrieve all users", e);
            throw e;
        }
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        try {
            if (user.getUserId() <= 0) {
                throw new IllegalArgumentException("Invalid user ID for update.");
            }

            logger.info("Service: Updating user ID: " + user.getUserId());
            return userDAO.update(user);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to update user ID: " + user.getUserId(), e);
            throw e;
        }
    }

    @Override
    public boolean deleteUser(long userId) throws SQLException {
        try {
            logger.info("Service: Deleting user ID: " + userId);
            return userDAO.delete(userId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Service failed to delete user ID: " + userId, e);
            throw e;
        }
    }
}
