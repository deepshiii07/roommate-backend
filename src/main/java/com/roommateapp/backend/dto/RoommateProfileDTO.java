package com.roommateapp.backend.dto;

public class RoommateProfileDTO {
    private Long id;
    private Long userId;
    private String bio;
    private String preferredLocation;
    private String budget;
    private String roommatePreferences;
    private String availability;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }
    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public String getBudget() {
        return budget;
    }
    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getRoommatePreferences() {
        return roommatePreferences;
    }
    public void setRoommatePreferences(String roommatePreferences) {
        this.roommatePreferences = roommatePreferences;
    }

    public String getAvailability() {
        return availability;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
