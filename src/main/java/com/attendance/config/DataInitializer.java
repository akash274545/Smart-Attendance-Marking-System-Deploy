package com.attendance.config;

import com.attendance.entity.User;
import com.attendance.entity.ClassEntity;
import com.attendance.entity.Subject;
import com.attendance.service.UserService;
import com.attendance.service.ClassService;
import com.attendance.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ClassService classService;
    
    @Autowired
    private SubjectService subjectService;
    
    @Override
    public void run(String... args) throws Exception {
        // Create default admin user
        System.out.println("Checking for admin user...");
        try {
            User admin = userService.createDefaultAdmin();
            if (admin != null) {
                System.out.println("==========================================");
                System.out.println("Default admin user created successfully!");
                System.out.println("Username: admin");
                System.out.println("Password: Admin@123");
                System.out.println("User ID: " + admin.getId());
                System.out.println("==========================================");
            } else {
                System.out.println("Default admin user already exists!");
                // Verify admin exists
                var existingAdmin = userService.findByUsername("admin");
                if (existingAdmin.isPresent()) {
                    System.out.println("Admin user verified. ID: " + existingAdmin.get().getId());
                } else {
                    System.err.println("WARNING: Admin user should exist but was not found!");
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR creating admin user: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Initialize default classes
        System.out.println("Initializing default classes...");
        try {
            initializeDefaultClasses();
            System.out.println("Default classes initialized successfully!");
        } catch (Exception e) {
            System.err.println("ERROR initializing default classes: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Initialize default subjects
        System.out.println("Initializing default subjects...");
        try {
            initializeDefaultSubjects();
            System.out.println("Default subjects initialized successfully!");
        } catch (Exception e) {
            System.err.println("ERROR initializing default subjects: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initializeDefaultClasses() {
        List<ClassEntity> defaultClasses = Arrays.asList(
            new ClassEntity("First Year", "FY", "First Year Undergraduate Class"),
            new ClassEntity("Second Year", "SY", "Second Year Undergraduate Class"),
            new ClassEntity("Third Year", "TY", "Third Year Undergraduate Class"),
            new ClassEntity("Fourth Year", "FOURTH", "Fourth Year Undergraduate Class"),
            new ClassEntity("Master First Year", "MFY", "Master First Year Class"),
            new ClassEntity("Master Second Year", "MSY", "Master Second Year Class"),
            new ClassEntity("PhD", "PHD", "Doctoral Program Class"),
            new ClassEntity("Diploma", "DIP", "Diploma Program Class"),
            new ClassEntity("Certificate", "CERT", "Certificate Program Class"),
            new ClassEntity("Foundation", "FOUND", "Foundation Program Class")
        );
        
        for (ClassEntity classEntity : defaultClasses) {
            if (!classService.existsByClassCode(classEntity.getClassCode())) {
                classService.saveClass(classEntity);
                System.out.println("Created default class: " + classEntity.getClassName() + " (" + classEntity.getClassCode() + ")");
            }
        }
    }
    
    private void initializeDefaultSubjects() {
        List<Subject> defaultSubjects = Arrays.asList(
            new Subject("Mathematics", "MATH101", "Introduction to Mathematics", null),
            new Subject("Physics", "PHY101", "Fundamentals of Physics", null),
            new Subject("Chemistry", "CHEM101", "Basic Chemistry", null),
            new Subject("Computer Science", "CS101", "Introduction to Computer Science", null),
            new Subject("English", "ENG101", "English Language and Literature", null),
            new Subject("Biology", "BIO101", "Introduction to Biology", null),
            new Subject("History", "HIST101", "World History", null),
            new Subject("Geography", "GEO101", "Physical and Human Geography", null),
            new Subject("Economics", "ECO101", "Principles of Economics", null),
            new Subject("Psychology", "PSY101", "Introduction to Psychology", null),
            new Subject("Statistics", "STAT101", "Basic Statistics", null),
            new Subject("Programming", "PROG101", "Programming Fundamentals", null),
            new Subject("Database Management", "DBMS101", "Database Systems", null),
            new Subject("Data Structures", "DS101", "Data Structures and Algorithms", null),
            new Subject("Software Engineering", "SE101", "Software Development Lifecycle", null)
        );
        
        for (Subject subject : defaultSubjects) {
            if (!subjectService.existsDefaultSubjectByCode(subject.getSubjectCode())) {
                // Store in a separate path for default subjects
                subjectService.saveDefaultSubject(subject);
                System.out.println("Created default subject: " + subject.getSubjectName() + " (" + subject.getSubjectCode() + ")");
            }
        }
    }
}
