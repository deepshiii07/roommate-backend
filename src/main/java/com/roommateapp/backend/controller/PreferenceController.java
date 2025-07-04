package com.roommateapp.backend.controller;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
@CrossOrigin(origins = "http://localhost:5173")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    // ✅ GET preferences for a user
    @GetMapping("/{userId}")
    public ResponseEntity<?> getPreferences(@PathVariable Long userId) {
        RoommateProfile profile = preferenceService.getPreferencesByUserId(userId);
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.status(404).body("Preferences not found");
        }
    }

    // ✅ CREATE or UPDATE preferences
    @PostMapping("/{userId}")
    public ResponseEntity<?> setOrUpdatePreferences(@PathVariable Long userId,
                                                    @RequestBody RoommateProfile profile) {
        try {
            RoommateProfile savedProfile = preferenceService.saveOrUpdatePreferences(userId, profile);
            return ResponseEntity.ok(savedProfile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving preferences: " + e.getMessage());
        }
    }
}
