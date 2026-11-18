package com.roommateapp.backend.service;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.model.User;
import com.roommateapp.backend.repository.RoommateProfileRepository;
import com.roommateapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Comparator;
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
        if (city.isBlank()) return List.of();

        return userRepository.findAll().stream()
            .filter(candidate -> candidate.getId() != null && !candidate.getId().equals(userId))
            .filter(candidate -> samePlace(city, normalize(candidate.getLocation())))
            .filter(candidate -> profileRepository.findByUserId(candidate.getId())
                .map(candidatePreferences ->
                    mutuallyAcceptGender(currentPreferences, currentUser, candidatePreferences, candidate))
                .orElse(false))
            .sorted(Comparator
                .comparingInt((User candidate) -> compatibilityScore(
                    currentPreferences,
                    profileRepository.findByUserId(candidate.getId()).orElse(null)))
                .reversed())
            .toList();
    }

    // City is the hard location boundary. Preferred areas influence ranking,
    // rather than excluding people who are considering a nearby neighbourhood.
    private boolean samePlace(String first, String second) {
        if (first.isBlank() || second.isBlank()) return false;
        return first.equals(second) || first.contains(second) || second.contains(first);
    }

    private boolean mutuallyAcceptGender(RoommateProfile mine, User me,
                                         RoommateProfile theirs, User candidate) {
        return accepts(mine.getPreferredGender(), candidate.getGender())
            && accepts(theirs.getPreferredGender(), me.getGender());
    }

    private boolean accepts(String preference, String gender) {
        String wanted = normalize(preference);
        if (wanted.isBlank() || wanted.equals("any") || wanted.equals("doesn't matter")
                || wanted.equals("doesnt matter")) return true;
        return wanted.equals(normalize(gender));
    }

    private int compatibilityScore(RoommateProfile mine, RoommateProfile theirs) {
        if (theirs == null) return 0;
        int score = 0;
        String myArea = normalize(mine.getPreferredLocation());
        String theirArea = normalize(theirs.getPreferredLocation());
        if (!myArea.isBlank() && !theirArea.isBlank()) {
            if (myArea.equals(theirArea)) score += 40;
            else if (myArea.contains(theirArea) || theirArea.contains(myArea)) score += 25;
            else score += 5; // Same city still makes this a possible match.
        }
        score += preferenceSimilarity(mine.getSmokerPreference(), theirs.getSmokerPreference(), 15);
        score += preferenceSimilarity(mine.getDrinkerPreference(), theirs.getDrinkerPreference(), 15);
        score += preferenceSimilarity(mine.getReligiousPreference(), theirs.getReligiousPreference(), 10);
        score += preferenceSimilarity(mine.getBedtimePreference(), theirs.getBedtimePreference(), 10);
        score += preferenceSimilarity(mine.getStudyTiming(), theirs.getStudyTiming(), 10);
        return score;
    }

    private int preferenceSimilarity(String first, String second, int weight) {
        String a = normalize(first);
        String b = normalize(second);
        if (a.isBlank() || b.isBlank()) return 0;
        if (a.equals(b)) return weight;
        if (isFlexible(a) || isFlexible(b)) return weight / 2;
        return 0;
    }

    private boolean isFlexible(String value) {
        return value.equals("any") || value.equals("flexible")
            || value.equals("doesn't matter") || value.equals("doesnt matter");
    }

    private String normalize(String value) {
        if (value == null) return "";
        String cleaned = Normalizer.normalize(value, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "");
        return cleaned.toLowerCase(Locale.ROOT).trim()
            .replaceAll("[^a-z0-9 ]", "")
            .replaceAll("\\s+", " ");
    }
}
