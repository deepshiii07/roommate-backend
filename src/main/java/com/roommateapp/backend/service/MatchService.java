package com.roommateapp.backend.service;

import com.roommateapp.backend.dto.RoommateMatchResponse;
import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.model.User;
import com.roommateapp.backend.repository.RoommateProfileRepository;
import com.roommateapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.ArrayList;
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

    public List<RoommateMatchResponse> findMatches(Long userId) {
        User me = userRepository.findById(userId).orElse(null);
        RoommateProfile mine = profileRepository.findByUserId(userId).orElse(null);
        if (me == null || mine == null || blank(me.getLocation())) return List.of();

        List<RoommateMatchResponse> results = new ArrayList<>();
        for (User candidate : userRepository.findAll()) {
            if (candidate.getId() == null || candidate.getId().equals(userId)
                    || !sameCity(me.getLocation(), candidate.getLocation())) continue;
            RoommateProfile theirs = profileRepository.findByUserId(candidate.getId()).orElse(null);
            if (theirs == null || !mutualGenderFit(mine.getPreferredGender(), candidate.getGender(),
                    theirs.getPreferredGender(), me.getGender())) continue;

            Score score = score(mine, theirs);
            results.add(new RoommateMatchResponse(candidate.getId(), candidate.getName(),
                candidate.getEmail(), candidate.getPhone(), candidate.getGender(),
                candidate.getLocation(), score.value, score.reasons));
        }
        results.sort(Comparator.comparingInt(RoommateMatchResponse::getCompatibilityScore).reversed());
        return results;
    }

    private Score score(RoommateProfile mine, RoommateProfile theirs) {
        int total = 0;
        List<String> reasons = new ArrayList<>();

        String a = normalize(mine.getPreferredLocation());
        String b = normalize(theirs.getPreferredLocation());
        if (!a.isBlank() && !b.isBlank()) {
            if (a.equals(b)) { total += 25; reasons.add("You both prefer " + mine.getPreferredLocation() + "."); }
            else if (a.contains(b) || b.contains(a)) { total += 18; reasons.add("Your preferred neighbourhoods are closely named."); }
            else { total += 8; reasons.add("You're in the same city, with different preferred areas."); }
        }

        total += comparePreference(mine.getSmokerPreference(), theirs.getLifestyleSmoking(),
            theirs.getSmokerPreference(), mine.getLifestyleSmoking(), 15, "Smoking habits", reasons);
        total += comparePreference(mine.getDrinkerPreference(), theirs.getLifestyleDrinking(),
            theirs.getDrinkerPreference(), mine.getLifestyleDrinking(), 10, "Drinking habits", reasons);
        total += comparePreference(mine.getReligiousPreference(), theirs.getLifestyleReligion(),
            theirs.getReligiousPreference(), mine.getLifestyleReligion(), 10, "Religious preferences", reasons);
        total += comparePreference(mine.getBedtimePreference(), theirs.getLifestyleSleep(),
            theirs.getBedtimePreference(), mine.getLifestyleSleep(), 15, "Sleep schedules", reasons);
        total += comparePreference(mine.getStudyTiming(), theirs.getLifestyleRoutine(),
            theirs.getStudyTiming(), mine.getLifestyleRoutine(), 10, "Daily routines", reasons);
        total += comparePreference(mine.getCleanlinessPreference(), theirs.getLifestyleCleanliness(),
            theirs.getCleanlinessPreference(), mine.getLifestyleCleanliness(), 10, "Cleanliness habits", reasons);
        total += comparePreference(mine.getSocialPreference(), theirs.getLifestyleSocial(),
            theirs.getSocialPreference(), mine.getLifestyleSocial(), 10, "Social lifestyle", reasons);
        total += comparePreference(mine.getGuestPreference(), theirs.getLifestyleGuests(),
            theirs.getGuestPreference(), mine.getLifestyleGuests(), 10, "Guest preferences", reasons);

        if (reasons.isEmpty()) reasons.add("You are in the same city and accept each other's roommate preferences.");
        return new Score(Math.min(100, total), reasons);
    }

    private int comparePreference(String myRequirement, String theirActual,
                                  String theirRequirement, String myActual,
                                  int weight, String label, List<String> reasons) {
        int points = direction(myRequirement, theirActual, weight / 2)
            + direction(theirRequirement, myActual, weight - weight / 2);
        if (points == weight) reasons.add("Compatible " + label.toLowerCase(Locale.ROOT) + ".");
        return points;
    }

    private int direction(String requirement, String actual, int weight) {
        String wanted = normalize(requirement);
        String reality = normalize(actual);
        if (wanted.isBlank() || reality.isBlank()) return 0;
        if (isFlexible(wanted)) return weight;
        return wanted.equals(reality) ? weight : 0;
    }

    private boolean mutualGenderFit(String myPreference, String theirGender,
                                    String theirPreference, String myGender) {
        return genderAccepts(myPreference, theirGender) && genderAccepts(theirPreference, myGender);
    }

    private boolean genderAccepts(String preference, String gender) {
        String p = normalize(preference);
        if (p.isBlank() || isFlexible(p) || p.equals("any gender")) return true;
        String g = normalize(gender);
        if (p.equals("women") || p.equals("woman")) p = "female";
        if (p.equals("men") || p.equals("man")) p = "male";
        if (p.equals("non binary") || p.equals("nonbinary")) p = "other";
        return p.equals(g);
    }

    private boolean sameCity(String first, String second) {
        String a = normalize(first), b = normalize(second);
        if (a.isBlank() || b.isBlank()) return false;
        a = canonicalCity(a); b = canonicalCity(b);
        return a.equals(b);
    }

    private String canonicalCity(String city) {
        return switch (city) {
            case "bangalore", "bengaluru", "blr" -> "bengaluru";
            case "bombay", "mumbai" -> "mumbai";
            case "calcutta", "kolkata" -> "kolkata";
            case "madras", "chennai" -> "chennai";
            case "gurgaon", "gurugram" -> "gurugram";
            default -> city;
        };
    }

    private boolean isFlexible(String value) {
        return value.isBlank() || value.equals("any") || value.equals("flexible")
            || value.equals("doesnt matter") || value.equals("no preference")
            || value.equals("any gender");
    }

    private boolean blank(String value) { return value == null || value.trim().isEmpty(); }

    private String normalize(String value) {
        if (value == null) return "";
        String decomposed = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return decomposed.toLowerCase(Locale.ROOT).trim()
            .replaceAll("[^a-z0-9 ]", "").replaceAll("\\s+", " ");
    }

    private static class Score {
        final int value;
        final List<String> reasons;
        Score(int value, List<String> reasons) { this.value = value; this.reasons = reasons; }
    }
}
