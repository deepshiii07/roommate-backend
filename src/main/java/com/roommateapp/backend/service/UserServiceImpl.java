package com.roommateapp.backend.service;

import com.roommateapp.backend.model.User;
import com.roommateapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.roommateapp.backend.model.RoommateProfile;
import com.roommateapp.backend.repository.RoommateProfileRepository;


import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            user.setLocation(updatedUser.getLocation());
            user.setAcademicYear(updatedUser.getAcademicYear());
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
  
    @Autowired
    private RoommateProfileRepository roommateProfileRepository;

    public List<User> matchRoommates(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) return List.of();

        User user = userOpt.get();
        Optional<RoommateProfile> prefOpt = roommateProfileRepository.findByUserId(id);
        if (prefOpt.isEmpty()) return List.of();

        RoommateProfile pref = prefOpt.get();

        return userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(id)) // exclude self
                .filter(u -> pref.getPreferredGender().equalsIgnoreCase("Any") ||
                             u.getGender().equalsIgnoreCase(pref.getPreferredGender()))
                .filter(u -> u.getLocation().equalsIgnoreCase(pref.getPreferredLocation()))
                .filter(u -> u.getAcademicYear() == pref.getPreferredAcademicYear())
                .toList();
    }

}
