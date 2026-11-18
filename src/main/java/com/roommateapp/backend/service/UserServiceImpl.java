package com.roommateapp.backend.service;

import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.model.User;
import com.roommateapp.backend.repository.RoommateProfileRepository;
import com.roommateapp.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoommateProfileRepository profileRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository, RoommateProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public User registerUser(User user) {
        String email = normalize(user.getEmail());
        if (email.isBlank()) throw new IllegalArgumentException("Email is required.");
        if (userRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must contain at least 8 characters.");
        }
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmailIgnoreCase(normalize(email))
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        String stored = user.getPassword();
        boolean matches = stored != null && (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$"))
            ? passwordEncoder.matches(password, stored)
            : stored != null && stored.equals(password);

        if (!matches) throw new IllegalArgumentException("Invalid credentials");

        // Upgrade existing plaintext passwords on successful login.
        if (stored != null && !(stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$"))) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() { return userRepository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) { return userRepository.findById(id); }

    @Override
    public User updateUser(Long id, User updated) {
        return userRepository.findById(id).map(user -> {
            if (updated.getName() != null) user.setName(updated.getName().trim());
            if (updated.getEmail() != null && !normalize(updated.getEmail()).equals(normalize(user.getEmail()))) {
                if (userRepository.findByEmailIgnoreCase(normalize(updated.getEmail())).isPresent()) {
                    throw new IllegalArgumentException("That email is already in use.");
                }
                user.setEmail(normalize(updated.getEmail()));
            }
            if (updated.getPhone() != null) user.setPhone(updated.getPhone());
            if (updated.getLocation() != null) user.setLocation(updated.getLocation().trim());
            if (updated.getGender() != null) user.setGender(updated.getGender());
            return userRepository.save(user);
        }).orElse(null);
    }

    @Override
    public void deleteUser(Long id) { userRepository.deleteById(id); }

    @Override
    @Transactional(readOnly = true)
    public List<User> matchRoommates(Long id) {
        User current = userRepository.findById(id).orElse(null);
        RoommateProfile preferences = profileRepository.findByUserId(id).orElse(null);
        if (current == null || preferences == null) return List.of();

        String city = normalize(current.getLocation());
        String area = normalize(preferences.getPreferredLocation());
        if (city.isBlank() || area.isBlank()) return List.of();

        return userRepository.findAll().stream()
            .filter(candidate -> !candidate.getId().equals(id))
            .filter(candidate -> city.equals(normalize(candidate.getLocation())))
            .filter(candidate -> profileRepository.findByUserId(candidate.getId())
                .map(candidateProfile -> area.equals(normalize(candidateProfile.getPreferredLocation()))
                    && mutuallyAcceptGender(preferences, current, candidateProfile, candidate))
                .orElse(false))
            .toList();
    }

    private boolean mutuallyAcceptGender(RoommateProfile myPreferences, User me,
                                         RoommateProfile theirPreferences, User candidate) {
        return accepts(myPreferences.getPreferredGender(), candidate.getGender())
            && accepts(theirPreferences.getPreferredGender(), me.getGender());
    }

    private boolean accepts(String preference, String gender) {
        String wanted = normalize(preference);
        return wanted.isBlank() || wanted.equals("any") || wanted.equals(normalize(gender));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
