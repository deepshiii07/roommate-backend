package com.roommateapp.backend.service;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.model.User;
import com.roommateapp.backend.repository.RoommateProfileRepository;
import com.roommateapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PreferenceService {

    @Autowired
    private RoommateProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public RoommateProfile getPreferencesByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preferences not found for userId: " + userId));
    }

    public RoommateProfile saveOrUpdatePreferences(Long userId, RoommateProfile profile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<RoommateProfile> existing = profileRepository.findByUserId(userId);

        if (existing.isPresent()) {
            RoommateProfile existingProfile = existing.get();
            existingProfile.setPreferredGender(profile.getPreferredGender());
            existingProfile.setPreferredLocation(profile.getPreferredLocation());
            existingProfile.setPreferredAcademicYear(profile.getPreferredAcademicYear());
            existingProfile.setSmokerPreference(profile.getSmokerPreference());
            existingProfile.setDrinkerPreference(profile.getDrinkerPreference());
            existingProfile.setReligiousPreference(profile.getReligiousPreference());
            existingProfile.setBedtimePreference(profile.getBedtimePreference());
            existingProfile.setStudyTiming(profile.getStudyTiming());
            return profileRepository.save(existingProfile);
        } else {
            profile.setUser(user);
            return profileRepository.save(profile);
        }
    }
}
