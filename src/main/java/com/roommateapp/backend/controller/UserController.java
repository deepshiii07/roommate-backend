package com.roommateapp.backend.controller;

import com.roommateapp.backend.dto.LoginRequest;
import com.roommateapp.backend.model.User;
import com.roommateapp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register new user
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();
            
            // ✅ Fix: login() returns a User, NOT Optional<User>
            User user = userService.login(email, password);
            
            return ResponseEntity.ok(user); // you can return DTO instead of full user if needed
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Login failed: " + e.getMessage()));
        }
    }


    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Update user by ID
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
// // Match roommates
//    @GetMapping("/match/{id}")
//    public List<User> matchRoommates(@PathVariable Long id) {
//        return userService.matchRoommates(id);
//    }

}
