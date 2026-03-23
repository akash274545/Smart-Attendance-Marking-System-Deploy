package com.attendance.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClassEntity {
    
    private String id;
    
    @NotBlank(message = "Class name is required")
    @Size(min = 2, max = 50, message = "Class name must be between 2 and 50 characters")
    private String className;
    
    private String classCode;
    
    private String description;
    
    private String createdAt;
    
    // Constructors
    public ClassEntity() {
        this.createdAt = java.time.LocalDateTime.now().toString();
    }
    
    public ClassEntity(String className, String classCode, String description) {
        this();
        this.className = className;
        this.classCode = classCode;
        this.description = description;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getClassCode() {
        return classCode;
    }
    
    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

