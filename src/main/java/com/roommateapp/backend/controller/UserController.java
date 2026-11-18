package com.roommateapp.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.roommateapp.backend.dto.LoginRequest;
import com.roommateapp.backend.model.User;
import com.roommateapp.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody JsonNode request) {
        String name = text(request, "name");
        String email = text(request, "email").toLowerCase();
        String password = text(request, "password");
        if (name.isBlank() || email.isBlank() || password.length() < 8) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Name and email are required; password must contain at least 8 characters."
            ));
        }
        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPassword(password);
        user.setPhone(text(request, "phone"));
        user.setGender(text(request, "gender"));
        user.setLocation(text(request, "location").trim());
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(user));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(userService.login(request.getEmail(), request.getPassword()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password."));
        }
    }

    @GetMapping
    public List<User> getAllUsers() { return userService.getAllUsers(); }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User updated = userService.updateUser(id, updatedUser);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private String text(JsonNode node, String key) {
        return node.hasNonNull(key) ? node.get(key).asText("") : "";
    }
}
