package com.roommateapp.backend.service;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.model.User;
import com.roommateapp.backend.repository.RoommateProfileRepository;
import com.roommateapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class MatchService {
    private final RoommateProfileRepository profileRepository;
    private final UserRepository userRepository;

    public MatchService(RoommateProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public List<User> findMatches(Long userId) {
        User currentUser = userRepository.findById(userId).orElse(null);
        RoommateProfile currentPreferences = profileRepository.findByUserId(userId).orElse(null);
        if (currentUser == null || currentPreferences == null) return List.of();

        String city = normalize(currentUser.getLocation());
        String area = normalize(currentPreferences.getPreferredLocation());
        if (city.isBlank() || area.isBlank()) return List.of();

        return userRepository.findAll().stream()
            .filter(candidate -> candidate.getId() != null && !candidate.getId().equals(userId))
            .filter(candidate -> city.equals(normalize(candidate.getLocation())))
            .filter(candidate -> profileRepository.findByUserId(candidate.getId())
                .map(candidatePreferences ->
                    area.equals(normalize(candidatePreferences.getPreferredLocation()))
                    && accepts(currentPreferences.getPreferredGender(), candidate.getGender())
                    && accepts(candidatePreferences.getPreferredGender(), currentUser.getGender()))
                .orElse(false))
            .toList();
    }

    private boolean accepts(String preference, String gender) {
        String wanted = normalize(preference);
        return wanted.isBlank() || wanted.equals("any") || wanted.equals(normalize(gender));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
