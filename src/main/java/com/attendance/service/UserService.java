package com.attendance.service;

import com.attendance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private FirebaseUserService firebaseUserService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return firebaseUserService.save(user);
    }
    
    public Optional<User> findByUsername(String username) {
        return firebaseUserService.findByUsername(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return firebaseUserService.findByEmail(email);
    }
    
    public List<User> findByRoleId(Integer roleId) {
        return firebaseUserService.findByRoleId(roleId);
    }
    
    public List<User> findPendingStudents() {
        return firebaseUserService.findByRoleIdAndIsApproved(2, false);
    }
    
    public List<User> findApprovedStudents() {
        return firebaseUserService.findByRoleIdAndIsApproved(2, true);
    }
    
    public List<User> findAllTeachers() {
        return firebaseUserService.findByRoleId(1);
    }
    
    public Long countTeachers() {
        return firebaseUserService.countByRoleId(1);
    }
    
    public Long countStudents() {
        return firebaseUserService.countByRoleId(2);
    }
    
    public Long countApprovedStudents() {
        return firebaseUserService.countByRoleIdAndIsApproved(2, true);
    }
    
    public User approveStudent(String studentId) {
        Optional<User> studentOpt = firebaseUserService.findById(studentId);
        if (studentOpt.isPresent()) {
            User student = studentOpt.get();
            student.setIsApproved(true);
            return firebaseUserService.save(student);
        }
        throw new RuntimeException("Student not found with id: " + studentId);
    }
    
    public boolean existsByUsername(String username) {
        return firebaseUserService.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return firebaseUserService.existsByEmail(email);
    }
    
    public User createDefaultAdmin() {
        if (!firebaseUserService.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("Admin@123");
            admin.setEmail("admin@attendance.com");
            admin.setFullName("System Administrator");
            admin.setRoleId(0);
            admin.setIsApproved(true);
            return saveUser(admin);
        }
        return firebaseUserService.findByUsername("admin").orElse(null);
    }
    
    public Optional<User> findById(String id) {
        return firebaseUserService.findById(id);
    }
    
    public void deleteUser(String id) {
        firebaseUserService.delete(id);
    }
    
    public User updateUser(User user) {
        return firebaseUserService.save(user);
    }
    
    public boolean resetPassword(String userId, String newPassword) {
        Optional<User> userOpt = firebaseUserService.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            firebaseUserService.save(user);
            return true;
        }
        return false;
    }

    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = firebaseUserService.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Verify old password
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                // Encode and set new password
                user.setPassword(passwordEncoder.encode(newPassword));
                firebaseUserService.save(user);
                return true;
            }
        }
        return false;
    }
}
