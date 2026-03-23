package com.attendance.service;

import com.attendance.entity.AttendanceToken;
import com.attendance.entity.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirebaseAttendanceTokenService {

    @Autowired
    private FirebaseService firebaseService;

    private static final String BASE_PATH = "AttendanceSystem/attendanceTokens";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public AttendanceToken save(AttendanceToken token) {
        String key = token.getId();
        if (key == null || key.isEmpty()) {
            key = UUID.randomUUID().toString();
            token.setId(key);
        }
        firebaseService.saveWithKeySync(BASE_PATH, key, token);
        return token;
    }

    public Optional<AttendanceToken> findById(String id) {
        AttendanceToken token = firebaseService.getSync(BASE_PATH + "/" + id, AttendanceToken.class);
        if (token != null) {
            token.setId(id);
        }
        return Optional.ofNullable(token);
    }

    public Optional<AttendanceToken> findByTokenAndIsActiveTrue(String tokenValue) {
        Map<String, AttendanceToken> tokensMap = firebaseService.getAllSync(BASE_PATH, AttendanceToken.class);
        return tokensMap.entrySet().stream()
                .filter(entry -> {
                    AttendanceToken token = entry.getValue();
                    return tokenValue.equals(token.getToken()) &&
                           Boolean.TRUE.equals(token.getIsActive());
                })
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public List<AttendanceToken> findActiveTokensForSubject(Subject subject, LocalDateTime now) {
        Map<String, AttendanceToken> tokensMap = firebaseService.getAllSync(BASE_PATH, AttendanceToken.class);
        
        return tokensMap.entrySet().stream()
                .filter(entry -> {
                    AttendanceToken token = entry.getValue();
                    if (!Boolean.TRUE.equals(token.getIsActive())) return false;
                    if (!subject.getId().equals(token.getSubjectId())) return false;
                    if (token.getExpiresAt() == null) return false;
                    try {
                        LocalDateTime expiresAt = LocalDateTime.parse(token.getExpiresAt());
                        return !expiresAt.isBefore(now);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .sorted((a, b) -> {
                    try {
                        LocalDateTime dateA = LocalDateTime.parse(a.getValue().getCreatedAt());
                        LocalDateTime dateB = LocalDateTime.parse(b.getValue().getCreatedAt());
                        return dateB.compareTo(dateA); // Descending order
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<AttendanceToken> findBySubjectAndIsActiveTrueAndExpiresAtBefore(Subject subject, LocalDateTime now) {
        Map<String, AttendanceToken> tokensMap = firebaseService.getAllSync(BASE_PATH, AttendanceToken.class);
        
        return tokensMap.entrySet().stream()
                .filter(entry -> {
                    AttendanceToken token = entry.getValue();
                    if (!Boolean.TRUE.equals(token.getIsActive())) return false;
                    if (!subject.getId().equals(token.getSubjectId())) return false;
                    if (token.getExpiresAt() == null) return false;
                    try {
                        LocalDateTime expiresAt = LocalDateTime.parse(token.getExpiresAt());
                        return expiresAt.isBefore(now);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<AttendanceToken> findAll() {
        Map<String, AttendanceToken> tokensMap = firebaseService.getAllSync(BASE_PATH, AttendanceToken.class);
        return tokensMap.entrySet().stream()
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}

