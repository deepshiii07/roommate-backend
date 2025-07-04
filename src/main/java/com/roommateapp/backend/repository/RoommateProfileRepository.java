package com.roommateapp.backend.repository;

import com.roommateapp.backend.model.RoommateProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoommateProfileRepository extends JpaRepository<RoommateProfile, Long> {
    Optional<RoommateProfile> findByUserId(Long userId);
}
