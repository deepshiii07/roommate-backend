package com.roommateapp.backend.service;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.model.User;
import com.roommateapp.backend.repository.RoommateProfileRepository;
import com.roommateapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PreferenceService {
    private final RoommateProfileRepository profileRepository;
    private final UserRepository userRepository;

    public PreferenceService(RoommateProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public RoommateProfile getPreferencesByUserId(Long userId) {
        return profileRepository.findByUserId(userId).orElse(null);
    }

    public RoommateProfile saveOrUpdatePreferences(Long userId, RoommateProfile incoming) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found."));
        RoommateProfile target = profileRepository.findByUserId(userId).orElseGet(RoommateProfile::new);
        target.setUser(user);
        target.setPreferredGender(incoming.getPreferredGender());
        target.setPreferredLocation(incoming.getPreferredLocation());
        target.setSmokerPreference(incoming.getSmokerPreference());
        target.setDrinkerPreference(incoming.getDrinkerPreference());
        target.setReligiousPreference(incoming.getReligiousPreference());
        target.setBedtimePreference(incoming.getBedtimePreference());
        target.setStudyTiming(incoming.getStudyTiming());
        target.setLifestyleSmoking(incoming.getLifestyleSmoking());
        target.setLifestyleDrinking(incoming.getLifestyleDrinking());
        target.setLifestyleReligion(incoming.getLifestyleReligion());
        target.setLifestyleSleep(incoming.getLifestyleSleep());
        target.setLifestyleRoutine(incoming.getLifestyleRoutine());
        target.setLifestyleCleanliness(incoming.getLifestyleCleanliness());
        target.setLifestyleSocial(incoming.getLifestyleSocial());
        target.setLifestyleGuests(incoming.getLifestyleGuests());
        target.setCleanlinessPreference(incoming.getCleanlinessPreference());
        target.setSocialPreference(incoming.getSocialPreference());
        target.setGuestPreference(incoming.getGuestPreference());
        return profileRepository.save(target);
    }
}
