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
    @Autowired private RoommateProfileRepository profileRepository;
    @Autowired private UserRepository userRepository;

    public RoommateProfile getPreferencesByUserId(Long userId) {
        return profileRepository.findByUserId(userId).orElse(null);
    }

    public RoommateProfile saveOrUpdatePreferences(Long userId, RoommateProfile profile) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<RoommateProfile> existing = profileRepository.findByUserId(userId);
        RoommateProfile target = existing.orElse(profile);
        target.setUser(user);
        target.setPreferredGender(profile.getPreferredGender());
        target.setPreferredLocation(profile.getPreferredLocation());
        target.setSmokerPreference(profile.getSmokerPreference());
        target.setDrinkerPreference(profile.getDrinkerPreference());
        target.setReligiousPreference(profile.getReligiousPreference());
        target.setBedtimePreference(profile.getBedtimePreference());
        target.setStudyTiming(profile.getStudyTiming());
        return profileRepository.save(target);
    }
}
