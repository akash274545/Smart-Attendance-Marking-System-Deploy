package com.attendance.service;

import com.attendance.entity.AttendanceToken;
import com.attendance.entity.Subject;
import com.attendance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttendanceTokenService {

    private static final Duration DEFAULT_VALIDITY = Duration.ofMinutes(2);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    private FirebaseAttendanceTokenService firebaseAttendanceTokenService;

    public AttendanceToken generateToken(User teacher, Subject subject) {
        return generateToken(teacher, subject, null, null, DEFAULT_VALIDITY);
    }

    public AttendanceToken generateToken(User teacher, Subject subject, Double teacherLatitude, Double teacherLongitude) {
        return generateToken(teacher, subject, teacherLatitude, teacherLongitude, DEFAULT_VALIDITY);
    }

    public AttendanceToken generateToken(User teacher, Subject subject, Duration validity) {
        return generateToken(teacher, subject, null, null, validity);
    }

    public AttendanceToken generateToken(User teacher, Subject subject, Double teacherLatitude, Double teacherLongitude, Duration validity) {
        LocalDateTime now = LocalDateTime.now();

        // deactivate expired tokens
        deactivateExpiredTokens(subject);

        AttendanceToken token = new AttendanceToken();
        token.setTeacherId(teacher.getId());
        token.setSubjectId(subject.getId());
        token.setToken(UUID.randomUUID().toString());
        token.setCreatedAt(now.format(DATE_FORMATTER));
        token.setExpiresAt(now.plus(validity).format(DATE_FORMATTER));
        token.setIsActive(true);
        token.setTeacherLatitude(teacherLatitude);
        token.setTeacherLongitude(teacherLongitude);

        return firebaseAttendanceTokenService.save(token);
    }

    public Optional<AttendanceToken> getActiveToken(String tokenValue) {
        Optional<AttendanceToken> tokenOptional = firebaseAttendanceTokenService.findByTokenAndIsActiveTrue(tokenValue);
        tokenOptional.ifPresent(this::deactivateIfExpired);
        return tokenOptional.filter(tok -> !tok.isExpired() && Boolean.TRUE.equals(tok.getIsActive()));
    }

    public Optional<AttendanceToken> getLatestActiveTokenForSubject(Subject subject) {
        List<AttendanceToken> tokens = firebaseAttendanceTokenService.findActiveTokensForSubject(subject, LocalDateTime.now());
        if (tokens.isEmpty()) {
            return Optional.empty();
        }
        AttendanceToken token = tokens.get(0);
        deactivateIfExpired(token);
        if (!Boolean.TRUE.equals(token.getIsActive()) || token.isExpired()) {
            return Optional.empty();
        }
        return Optional.of(token);
    }

    public void deactivateExpiredTokens(Subject subject) {
        LocalDateTime now = LocalDateTime.now();
        List<AttendanceToken> tokens = firebaseAttendanceTokenService
                .findBySubjectAndIsActiveTrueAndExpiresAtBefore(subject, now);
        tokens.forEach(token -> {
            token.setIsActive(false);
            firebaseAttendanceTokenService.save(token);
        });
    }

    public void deactivateToken(AttendanceToken token) {
        token.setIsActive(false);
        firebaseAttendanceTokenService.save(token);
    }

    private void deactivateIfExpired(AttendanceToken token) {
        if (token.isExpired() && Boolean.TRUE.equals(token.getIsActive())) {
            token.setIsActive(false);
            firebaseAttendanceTokenService.save(token);
        }
    }
}

