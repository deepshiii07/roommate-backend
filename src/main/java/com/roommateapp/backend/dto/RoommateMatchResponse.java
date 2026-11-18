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

    public RoommateMatchResponse(Long id, String name, String email, String phone,
                                 String gender, String location, int compatibilityScore,
                                 List<String> compatibilityReasons) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.location = location;
        this.compatibilityScore = compatibilityScore;
        this.compatibilityReasons = compatibilityReasons;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getLocation() { return location; }
    public int getCompatibilityScore() { return compatibilityScore; }
    public List<String> getCompatibilityReasons() { return compatibilityReasons; }
}
