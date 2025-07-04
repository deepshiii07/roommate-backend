package com.roommateapp.backend.repository;

import com.roommateapp.backend.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Preference findByUserId(Long userId);
}
