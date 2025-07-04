package com.roommateapp.backend.specification;

import com.roommateapp.backend.model.RoommateProfile;
import org.springframework.data.jpa.domain.Specification;

public class RoommateProfileSpecification {

    public static Specification<RoommateProfile> preferredLocationContains(String location) {
        return (root, query, cb) -> location == null ? null :
                cb.like(cb.lower(root.get("preferredLocation")), "%" + location.toLowerCase() + "%");
    }

    public static Specification<RoommateProfile> budgetGreaterThanOrEqual(String minBudget) {
        return (root, query, cb) -> {
            if (minBudget == null) return null;
            try {
                return cb.greaterThanOrEqualTo(root.get("budget"), minBudget);
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<RoommateProfile> budgetLessThanOrEqual(String maxBudget) {
        return (root, query, cb) -> {
            if (maxBudget == null) return null;
            try {
                return cb.lessThanOrEqualTo(root.get("budget"), maxBudget);
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<RoommateProfile> roommatePreferenceEquals(String genderPref) {
        return (root, query, cb) -> genderPref == null ? null :
                cb.like(cb.lower(root.get("roommatePreferences")), "%" + genderPref.toLowerCase() + "%");
    }
}
