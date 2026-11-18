package com.roommateapp.backend.controller;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.service.PreferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {
    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPreferences(@PathVariable Long userId) {
        RoommateProfile profile = preferenceService.getPreferencesByUserId(userId);
        return profile == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(profile);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> savePreferences(@PathVariable Long userId, @RequestBody RoommateProfile profile) {
        try {
            return ResponseEntity.ok(preferenceService.saveOrUpdatePreferences(userId, profile));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
