package com.roommateapp.backend.controller;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
@CrossOrigin(origins = {"http://localhost:5173", "https://roomiesync.vercel.app"})
public class PreferenceController {
    @Autowired private PreferenceService preferenceService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPreferences(@PathVariable Long userId) {
        RoommateProfile profile = preferenceService.getPreferencesByUserId(userId);
        return profile == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(profile);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> setOrUpdatePreferences(@PathVariable Long userId,
                                                     @RequestBody RoommateProfile profile) {
        try {
            return ResponseEntity.ok(preferenceService.saveOrUpdatePreferences(userId, profile));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving preferences: " + e.getMessage());
        }
    }
}
