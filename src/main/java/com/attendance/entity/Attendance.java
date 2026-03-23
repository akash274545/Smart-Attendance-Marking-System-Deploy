package com.attendance.entity;

import jakarta.validation.constraints.NotNull;

public class Attendance {
    
    private String id;
    
    private String studentId; // Store student ID instead of User object
    
    private String subjectId; // Store subject ID instead of Subject object
    
    @NotNull(message = "Attendance date is required")
    private String attendanceDate;
    
    private Double latitude;
    
    private Double longitude;
    
    private String locationAddress;
    
    private String status = "PRESENT"; // PRESENT, ABSENT, LATE
    
    private String remarks;
    
    private String createdAt;
    
    // Constructors
    public Attendance() {
        this.createdAt = java.time.LocalDateTime.now().toString();
    }
    
    public Attendance(String studentId, String subjectId, String attendanceDate, 
                     Double latitude, Double longitude, String locationAddress) {
        this();
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.attendanceDate = attendanceDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationAddress = locationAddress;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getSubjectId() {
        return subjectId;
    }
    
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    
    public String getAttendanceDate() {
        return attendanceDate;
    }
    
    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public String getLocationAddress() {
        return locationAddress;
    }
    
    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
