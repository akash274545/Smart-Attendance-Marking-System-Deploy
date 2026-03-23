package com.attendance.entity;

public class AttendanceToken {

    private String id;

    private String token;

    private String subjectId; // Store subject ID instead of Subject object

    private String teacherId; // Store teacher ID instead of User object

    private String expiresAt;

    private String createdAt;

    private Boolean isActive = true;

    private Double teacherLatitude;

    private Double teacherLongitude;

    public AttendanceToken() {
        this.createdAt = java.time.LocalDateTime.now().toString();
    }

    public AttendanceToken(String token, String subjectId, String teacherId, String expiresAt) {
        this();
        this.token = token;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.expiresAt = expiresAt;
        this.isActive = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Double getTeacherLatitude() {
        return teacherLatitude;
    }

    public void setTeacherLatitude(Double teacherLatitude) {
        this.teacherLatitude = teacherLatitude;
    }

    public Double getTeacherLongitude() {
        return teacherLongitude;
    }

    public void setTeacherLongitude(Double teacherLongitude) {
        this.teacherLongitude = teacherLongitude;
    }

    public boolean isExpired() {
        if (expiresAt == null) return false;
        try {
            java.time.LocalDateTime expiry = java.time.LocalDateTime.parse(expiresAt);
            return expiry.isBefore(java.time.LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }
}

