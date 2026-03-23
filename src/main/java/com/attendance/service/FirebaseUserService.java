package com.attendance.service;

import com.attendance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirebaseUserService {

    @Autowired
    private FirebaseService firebaseService;

    private static final String BASE_PATH = "AttendanceSystem/users";

    public User save(User user) {
        String key = user.getId();
        if (key == null || key.isEmpty()) {
            key = UUID.randomUUID().toString();
            user.setId(key);
        }
        firebaseService.saveWithKeySync(BASE_PATH, key, user);
        return user;
    }

    public Optional<User> findById(String id) {
        User user = firebaseService.getSync(BASE_PATH + "/" + id, User.class);
        if (user != null) {
            user.setId(id);
        }
        return Optional.ofNullable(user);
    }

    public Optional<User> findByUsername(String username) {
        try {
            Map<String, User> usersMap = firebaseService.getAllSync(BASE_PATH, User.class);
            if (usersMap == null || usersMap.isEmpty()) {
                return Optional.empty();
            }
            return usersMap.entrySet().stream()
                    .filter(entry -> {
                        User user = entry.getValue();
                        return user != null && username != null && username.equals(user.getUsername());
                    })
                    .peek(entry -> entry.getValue().setId(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst();
        } catch (Exception e) {
            System.err.println("Error finding user by username: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            Map<String, User> usersMap = firebaseService.getAllSync(BASE_PATH, User.class);
            if (usersMap == null || usersMap.isEmpty()) {
                return Optional.empty();
            }
            return usersMap.entrySet().stream()
                    .filter(entry -> {
                        User user = entry.getValue();
                        return user != null && email != null && email.equals(user.getEmail());
                    })
                    .peek(entry -> entry.getValue().setId(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst();
        } catch (Exception e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<User> findByRoleId(Integer roleId) {
        Map<String, User> usersMap = firebaseService.getAllSync(BASE_PATH, User.class);
        return usersMap.entrySet().stream()
                .filter(entry -> entry.getValue().getRoleId() != null && entry.getValue().getRoleId().equals(roleId))
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<User> findByRoleIdAndIsApproved(Integer roleId, Boolean isApproved) {
        Map<String, User> usersMap = firebaseService.getAllSync(BASE_PATH, User.class);
        return usersMap.entrySet().stream()
                .filter(entry -> {
                    User user = entry.getValue();
                    return user.getRoleId() != null && user.getRoleId().equals(roleId) &&
                           user.getIsApproved() != null && user.getIsApproved().equals(isApproved);
                })
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public Long countByRoleId(Integer roleId) {
        return (long) findByRoleId(roleId).size();
    }

    public Long countByRoleIdAndIsApproved(Integer roleId, Boolean isApproved) {
        return (long) findByRoleIdAndIsApproved(roleId, isApproved).size();
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public List<User> findAll() {
        Map<String, User> usersMap = firebaseService.getAllSync(BASE_PATH, User.class);
        return usersMap.entrySet().stream()
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
    
    public void delete(String id) {
        firebaseService.deleteSync(BASE_PATH + "/" + id);
    }

}

