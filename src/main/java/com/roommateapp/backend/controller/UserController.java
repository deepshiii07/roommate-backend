package com.roommateapp.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
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
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "https://roomiesync.vercel.app"})
public class UserController {
    @Autowired private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody JsonNode request) {
        String password = request.path("password").asText("");
        String name = request.path("name").asText("");
        String email = request.path("email").asText("");
        if (password.isBlank() || name.isBlank() || email.isBlank()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap(
                    "error", "Name, email and password are required."));
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(request.path("phone").asText(""));
        user.setGender(request.path("gender").asText(""));
        user.setLocation(request.path("location").asText(""));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Login failed: " + e.getMessage()));
        }
    }

    @GetMapping
    public List<User> getAllUsers() { return userService.getAllUsers(); }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) { return userService.getUserById(id); }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) { userService.deleteUser(id); }
}
