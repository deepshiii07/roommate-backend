package com.roommateapp.backend.service;

import com.roommateapp.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    User login(String email, String password);  // ✅ ADD THIS
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User updateUser(Long id, User updatedUser);
    List<User> matchRoommates(Long id);
    void deleteUser(Long id);
}
