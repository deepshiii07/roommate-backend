package com.roommateapp.backend.service;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.model.User;
import com.roommateapp.backend.repository.RoommateProfileRepository;
import com.roommateapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Autowired
    private RoommateProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public List<User> findMatches(Long userId) {
        Optional<RoommateProfile> profileOpt = profileRepository.findByUserId(userId);

        if (profileOpt.isEmpty()) {
            return List.of(); // No preferences found, return empty match list
        }

        RoommateProfile profile = profileOpt.get();

        return userRepository.findAll()
                .stream()
                .filter(user ->
                        !user.getId().equals(userId) && // exclude self
                        user.getGender().equalsIgnoreCase(profile.getPreferredGender()) &&
                        user.getLocation().equalsIgnoreCase(profile.getPreferredLocation()) &&
                        user.getAcademicYear() == profile.getPreferredAcademicYear()
                )
                .collect(Collectors.toList());
    }
}
