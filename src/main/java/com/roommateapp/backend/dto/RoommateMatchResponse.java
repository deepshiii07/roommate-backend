package com.roommateapp.backend.dto;

import java.util.List;

public class RoommateMatchResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String location;
    private int compatibilityScore;
    private List<String> compatibilityReasons;
    private String lifestyleSmoking;
    private String lifestyleDrinking;
    private String lifestyleSleep;
    private String lifestyleRoutine;
    private String lifestyleCleanliness;
    private String lifestyleSocial;
    private String lifestyleGuests;

    public RoommateMatchResponse(Long id, String name, String email, String phone,
                                 String gender, String location, int compatibilityScore,
                                 List<String> compatibilityReasons,
                                 String lifestyleSmoking, String lifestyleDrinking,
                                 String lifestyleSleep, String lifestyleRoutine,
                                 String lifestyleCleanliness, String lifestyleSocial,
                                 String lifestyleGuests) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.location = location;
        this.compatibilityScore = compatibilityScore;
        this.compatibilityReasons = compatibilityReasons;
        this.lifestyleSmoking = lifestyleSmoking;
        this.lifestyleDrinking = lifestyleDrinking;
        this.lifestyleSleep = lifestyleSleep;
        this.lifestyleRoutine = lifestyleRoutine;
        this.lifestyleCleanliness = lifestyleCleanliness;
        this.lifestyleSocial = lifestyleSocial;
        this.lifestyleGuests = lifestyleGuests;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getLocation() { return location; }
    public int getCompatibilityScore() { return compatibilityScore; }
    public List<String> getCompatibilityReasons() { return compatibilityReasons; }
    public String getLifestyleSmoking() { return lifestyleSmoking; }
    public String getLifestyleDrinking() { return lifestyleDrinking; }
    public String getLifestyleSleep() { return lifestyleSleep; }
    public String getLifestyleRoutine() { return lifestyleRoutine; }
    public String getLifestyleCleanliness() { return lifestyleCleanliness; }
    public String getLifestyleSocial() { return lifestyleSocial; }
    public String getLifestyleGuests() { return lifestyleGuests; }
}
