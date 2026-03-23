package com.attendance.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Subject {
    
    private String id;
    
    @NotBlank(message = "Subject name is required")
    @Size(min = 2, max = 100, message = "Subject name must be between 2 and 100 characters")
    private String subjectName;
    
    @NotBlank(message = "Subject code is required")
    @Size(min = 3, max = 10, message = "Subject code must be between 3 and 10 characters")
    private String subjectCode;
    
    private String description;
    
    private String teacherId; // Store teacher ID instead of User object
    
    private String classId; // Store class ID
    
    private String createdAt;
    
    // Constructors
    public Subject() {
        this.createdAt = java.time.LocalDateTime.now().toString();
    }
    
    public Subject(String subjectName, String subjectCode, String description, String teacherId) {
        this();
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.description = description;
        this.teacherId = teacherId;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public String getSubjectCode() {
        return subjectCode;
    }
    
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
    
    public String getClassId() {
        return classId;
    }
    
    public void setClassId(String classId) {
        this.classId = classId;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

