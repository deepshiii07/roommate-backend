package com.roommateapp.backend.service;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.repository.RoommateProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoommateProfileService {

    private final RoommateProfileRepository profileRepository;

    @Autowired
    public RoommateProfileService(RoommateProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Optional<RoommateProfile> getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }
}
