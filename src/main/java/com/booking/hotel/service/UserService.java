package com.booking.hotel.service;

import com.booking.hotel.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    boolean registerUser(User user) throws SQLException;
    User getUserById(long userId) throws SQLException;
    List<User> getAllUsers() throws SQLException;
    boolean updateUser(User user) throws SQLException;
    boolean deleteUser(long userId) throws SQLException;
}


