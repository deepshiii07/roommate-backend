package com.roommateapp.backend.service;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.model.User;
import com.roommateapp.backend.repository.RoommateProfileRepository;
import com.roommateapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchService {
    @Autowired private RoommateProfileRepository profileRepository;
    @Autowired private UserRepository userRepository;

    public List<User> findMatches(Long userId) {
        Optional<RoommateProfile> profileOpt = profileRepository.findByUserId(userId);
        Optional<User> currentUserOpt = userRepository.findById(userId);
        if (profileOpt.isEmpty() || currentUserOpt.isEmpty()) return List.of();

        RoommateProfile preferences = profileOpt.get();
        User currentUser = currentUserOpt.get();
        String city = normalize(currentUser.getLocation());
        String area = normalize(preferences.getPreferredLocation());
        String genderPreference = normalize(preferences.getPreferredGender());

        if (city.isEmpty() || area.isEmpty()) return List.of();

        return userRepository.findAll().stream()
            .filter(candidate -> candidate.getId() != null && !candidate.getId().equals(userId))
            .filter(candidate -> city.equals(normalize(candidate.getLocation())))
            .filter(candidate -> acceptsGender(genderPreference, candidate.getGender()))
            .filter(candidate -> profileRepository.findByUserId(candidate.getId())
                .map(candidateProfile -> area.equals(normalize(candidateProfile.getPreferredLocation())))
                .orElse(false))
            .collect(Collectors.toList());
    }

    private boolean acceptsGender(String preference, String candidateGender) {
        return preference.isEmpty() || preference.equals("any") ||
            preference.equals(normalize(candidateGender));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
