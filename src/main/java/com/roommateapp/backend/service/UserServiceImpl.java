package com.roommateapp.backend.service;

import com.roommateapp.backend.model.User;
import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.repository.UserRepository;
import com.roommateapp.backend.repository.RoommateProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoommateProfileRepository roommateProfileRepository;

    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) return null;

        User user = userOpt.get();
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setLocation(updatedUser.getLocation());
        user.setGender(updatedUser.getGender());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> matchRoommates(Long id) {
        Optional<User> currentUserOpt = userRepository.findById(id);
        Optional<RoommateProfile> preferencesOpt = roommateProfileRepository.findByUserId(id);
        if (currentUserOpt.isEmpty() || preferencesOpt.isEmpty()) return List.of();

        User currentUser = currentUserOpt.get();
        RoommateProfile preferences = preferencesOpt.get();
        String city = normalize(currentUser.getLocation());
        String area = normalize(preferences.getPreferredLocation());
        String genderPreference = normalize(preferences.getPreferredGender());
        if (city.isEmpty() || area.isEmpty()) return List.of();

        return userRepository.findAll().stream()
                .filter(candidate -> candidate.getId() != null && !candidate.getId().equals(id))
                .filter(candidate -> city.equals(normalize(candidate.getLocation())))
                .filter(candidate -> genderPreference.isEmpty() || genderPreference.equals("any")
                        || genderPreference.equals(normalize(candidate.getGender())))
                .filter(candidate -> roommateProfileRepository.findByUserId(candidate.getId())
                        .map(candidateProfile -> area.equals(normalize(candidateProfile.getPreferredLocation())))
                        .orElse(false))
                .toList();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
