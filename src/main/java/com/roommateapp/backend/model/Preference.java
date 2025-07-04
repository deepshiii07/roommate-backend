package com.roommateapp.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "preferences")
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "preferred_gender")
    private String preferredGender;

    @Column(name = "preferred_location")
    private String preferredLocation;

    @Column(name = "preferred_academic_year")
    private String preferredAcademicYear;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    // Constructors
    public Preference() {
    }

    public Preference(String preferredGender, String preferredLocation, String preferredAcademicYear, User user) {
        this.preferredGender = preferredGender;
        this.preferredLocation = preferredLocation;
        this.preferredAcademicYear = preferredAcademicYear;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPreferredAcademicYear() {
        return preferredAcademicYear;
    }

    public void setPreferredAcademicYear(String preferredAcademicYear) {
        this.preferredAcademicYear = preferredAcademicYear;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
