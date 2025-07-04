package com.roommateapp.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roommate_profiles")
public class RoommateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String preferredGender;
    private String preferredLocation;
    private Integer preferredAcademicYear;
    private String smokerPreference;
    private String drinkerPreference;
    private String religiousPreference;
    private String bedtimePreference;
    private String studyTiming;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(String preferredGender) {
        this.preferredGender = preferredGender;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public Integer getPreferredAcademicYear() {
        return preferredAcademicYear;
    }

    public void setPreferredAcademicYear(Integer preferredAcademicYear) {
        this.preferredAcademicYear = preferredAcademicYear;
    }

    public String getSmokerPreference() {
        return smokerPreference;
    }

    public void setSmokerPreference(String smokerPreference) {
        this.smokerPreference = smokerPreference;
    }

    public String getDrinkerPreference() {
        return drinkerPreference;
    }

    public void setDrinkerPreference(String drinkerPreference) {
        this.drinkerPreference = drinkerPreference;
    }

    public String getReligiousPreference() {
        return religiousPreference;
    }

    public void setReligiousPreference(String religiousPreference) {
        this.religiousPreference = religiousPreference;
    }

    public String getBedtimePreference() {
        return bedtimePreference;
    }

    public void setBedtimePreference(String bedtimePreference) {
        this.bedtimePreference = bedtimePreference;
    }

    public String getStudyTiming() {
        return studyTiming;
    }

    public void setStudyTiming(String studyTiming) {
        this.studyTiming = studyTiming;
    }
}
