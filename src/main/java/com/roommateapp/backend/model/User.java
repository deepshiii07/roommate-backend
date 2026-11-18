package com.roommateapp.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private String phone;
    private String gender;
    // Current city. Kept on the existing location column for database compatibility.
    private String location;
    // Retained only so existing database rows and older clients remain compatible.
    @Column(name = "academic_year")
    private int academicYear;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private RoommateProfile roommateProfile;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getAcademicYear() { return academicYear; }
    public void setAcademicYear(int academicYear) { this.academicYear = academicYear; }
    public RoommateProfile getRoommateProfile() { return roommateProfile; }
    public void setRoommateProfile(RoommateProfile roommateProfile) { this.roommateProfile = roommateProfile; }
}
