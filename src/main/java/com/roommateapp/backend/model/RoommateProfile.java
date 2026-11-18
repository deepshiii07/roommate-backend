package com.roommateapp.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "roommate_profiles")
public class RoommateProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    private String preferredGender;

    // The existing column is retained as the preferred neighbourhood field.
    @Column(name = "preferred_location")
    private String preferredLocation;

    // Legacy database compatibility only. The field is not part of the product or API.
    @JsonIgnore
    @Column(name = "preferred_academic_year")
    private Integer legacyPreferredAcademicYear = 0;

    private String smokerPreference;
    private String drinkerPreference;
    private String religiousPreference;
    private String bedtimePreference;
    private String studyTiming;

    @PrePersist
    @PreUpdate
    private void applyLegacyDefaults() {
        if (legacyPreferredAcademicYear == null) legacyPreferredAcademicYear = 0;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getPreferredGender() { return preferredGender; }
    public void setPreferredGender(String preferredGender) { this.preferredGender = preferredGender; }
    public String getPreferredLocation() { return preferredLocation; }
    public void setPreferredLocation(String preferredLocation) { this.preferredLocation = preferredLocation; }
    public String getSmokerPreference() { return smokerPreference; }
    public void setSmokerPreference(String smokerPreference) { this.smokerPreference = smokerPreference; }
    public String getDrinkerPreference() { return drinkerPreference; }
    public void setDrinkerPreference(String drinkerPreference) { this.drinkerPreference = drinkerPreference; }
    public String getReligiousPreference() { return religiousPreference; }
    public void setReligiousPreference(String religiousPreference) { this.religiousPreference = religiousPreference; }
    public String getBedtimePreference() { return bedtimePreference; }
    public void setBedtimePreference(String bedtimePreference) { this.bedtimePreference = bedtimePreference; }
    public String getStudyTiming() { return studyTiming; }
    public void setStudyTiming(String studyTiming) { this.studyTiming = studyTiming; }
}
