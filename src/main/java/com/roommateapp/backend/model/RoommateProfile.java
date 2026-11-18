package com.roommateapp.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "roommate_profiles")
public class RoommateProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;
    private String preferredGender;
    @Column(name = "preferred_location")
    private String preferredLocation;

    // Existing fields represent what the user wants in a roommate.
    private String smokerPreference;
    private String drinkerPreference;
    private String religiousPreference;
    private String bedtimePreference;
    private String studyTiming;

    // Actual lifestyle information about this profile owner.
    private String lifestyleSmoking;
    private String lifestyleDrinking;
    private String lifestyleReligion;
    private String lifestyleSleep;
    private String lifestyleRoutine;
    private String lifestyleCleanliness;
    private String lifestyleSocial;
    private String lifestyleGuests;

    // Additional ideal-roommate preferences.
    private String cleanlinessPreference;
    private String socialPreference;
    private String guestPreference;

    // Legacy database compatibility only.
    @JsonIgnore
    @Column(name = "preferred_academic_year")
    private Integer legacyPreferredAcademicYear = 0;

    @PrePersist @PreUpdate
    private void applyLegacyDefaults() {
        if (legacyPreferredAcademicYear == null) legacyPreferredAcademicYear = 0;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User value) { user = value; }
    public String getPreferredGender() { return preferredGender; }
    public void setPreferredGender(String value) { preferredGender = value; }
    public String getPreferredLocation() { return preferredLocation; }
    public void setPreferredLocation(String value) { preferredLocation = value; }
    public String getSmokerPreference() { return smokerPreference; }
    public void setSmokerPreference(String value) { smokerPreference = value; }
    public String getDrinkerPreference() { return drinkerPreference; }
    public void setDrinkerPreference(String value) { drinkerPreference = value; }
    public String getReligiousPreference() { return religiousPreference; }
    public void setReligiousPreference(String value) { religiousPreference = value; }
    public String getBedtimePreference() { return bedtimePreference; }
    public void setBedtimePreference(String value) { bedtimePreference = value; }
    public String getStudyTiming() { return studyTiming; }
    public void setStudyTiming(String value) { studyTiming = value; }
    public String getLifestyleSmoking() { return lifestyleSmoking; }
    public void setLifestyleSmoking(String value) { lifestyleSmoking = value; }
    public String getLifestyleDrinking() { return lifestyleDrinking; }
    public void setLifestyleDrinking(String value) { lifestyleDrinking = value; }
    public String getLifestyleReligion() { return lifestyleReligion; }
    public void setLifestyleReligion(String value) { lifestyleReligion = value; }
    public String getLifestyleSleep() { return lifestyleSleep; }
    public void setLifestyleSleep(String value) { lifestyleSleep = value; }
    public String getLifestyleRoutine() { return lifestyleRoutine; }
    public void setLifestyleRoutine(String value) { lifestyleRoutine = value; }
    public String getLifestyleCleanliness() { return lifestyleCleanliness; }
    public void setLifestyleCleanliness(String value) { lifestyleCleanliness = value; }
    public String getLifestyleSocial() { return lifestyleSocial; }
    public void setLifestyleSocial(String value) { lifestyleSocial = value; }
    public String getLifestyleGuests() { return lifestyleGuests; }
    public void setLifestyleGuests(String value) { lifestyleGuests = value; }
    public String getCleanlinessPreference() { return cleanlinessPreference; }
    public void setCleanlinessPreference(String value) { cleanlinessPreference = value; }
    public String getSocialPreference() { return socialPreference; }
    public void setSocialPreference(String value) { socialPreference = value; }
    public String getGuestPreference() { return guestPreference; }
    public void setGuestPreference(String value) { guestPreference = value; }
}
