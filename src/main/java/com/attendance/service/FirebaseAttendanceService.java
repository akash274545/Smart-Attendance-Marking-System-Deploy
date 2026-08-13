package com.attendance.service;

import com.attendance.entity.Attendance;
import com.attendance.entity.Subject;
import com.attendance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.OffsetDateTime;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirebaseAttendanceService {

    @Autowired
    private FirebaseService firebaseService;

    private static final String BASE_PATH =
            "AttendanceSystem/attendance";

    public Attendance save(Attendance attendance) {

        String key = attendance.getId();

        if (key == null || key.isEmpty()) {
            key = UUID.randomUUID().toString();
            attendance.setId(key);
        }

        firebaseService.saveWithKeySync(
                BASE_PATH,
                key,
                attendance
        );

        return attendance;
    }

    public Optional<Attendance> findById(String id) {

        Attendance attendance =
                firebaseService.getSync(
                        BASE_PATH + "/" + id,
                        Attendance.class
                );

        if (attendance != null) {
            attendance.setId(id);
        }

        return Optional.ofNullable(attendance);
    }

    public List<Attendance> findByStudent(User student) {

        Map<String, Attendance> attendanceMap =
                firebaseService.getAllSync(
                        BASE_PATH,
                        Attendance.class
                );

        return attendanceMap.entrySet()
                .stream()
                .filter(entry -> {

                    Attendance attendance =
                            entry.getValue();

                    return attendance.getStudentId() != null
                            && attendance.getStudentId()
                            .equals(student.getId());
                })
                .peek(entry ->
                        entry.getValue()
                                .setId(entry.getKey())
                )
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Attendance> findBySubject(Subject subject) {

        Map<String, Attendance> attendanceMap =
                firebaseService.getAllSync(
                        BASE_PATH,
                        Attendance.class
                );

        return attendanceMap.entrySet()
                .stream()
                .filter(entry -> {

                    Attendance attendance =
                            entry.getValue();

                    return attendance.getSubjectId() != null
                            && attendance.getSubjectId()
                            .equals(subject.getId());
                })
                .peek(entry ->
                        entry.getValue()
                                .setId(entry.getKey())
                )
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Attendance> findByStudentAndSubject(
            User student,
            Subject subject) {

        Map<String, Attendance> attendanceMap =
                firebaseService.getAllSync(
                        BASE_PATH,
                        Attendance.class
                );

        return attendanceMap.entrySet()
                .stream()
                .filter(entry -> {

                    Attendance attendance =
                            entry.getValue();

                    return attendance.getStudentId() != null
                            && attendance.getStudentId()
                            .equals(student.getId())

                            && attendance.getSubjectId() != null
                            && attendance.getSubjectId()
                            .equals(subject.getId());
                })
                .peek(entry ->
                        entry.getValue()
                                .setId(entry.getKey())
                )
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    /**
     * Finds attendance for the same calendar date.
     *
     * Supports:
     * 1. LocalDateTime
     * 2. ISO Instant with Z
     * 3. OffsetDateTime
     *
     * All dates are interpreted using Asia/Kolkata.
     */
    public List<Attendance> findByStudentAndSubjectAndDate(
            User student,
            Subject subject,
            LocalDateTime date) {

        java.time.LocalDate targetDate =
                date.toLocalDate();

        List<Attendance> all =
                findByStudentAndSubject(
                        student,
                        subject
                );

        return all.stream()
                .filter(attendance -> {

                    if (attendance.getAttendanceDate() == null) {
                        return false;
                    }

                    java.time.LocalDate attendanceDate =
                            parseLocalDate(
                                    attendance.getAttendanceDate()
                            );

                    return attendanceDate != null
                            && attendanceDate.equals(targetDate);
                })
                .collect(Collectors.toList());
    }

    /**
     * Converts different date-time formats into
     * Indian calendar date.
     */
    private static java.time.LocalDate parseLocalDate(
            String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        ZoneId ist =
                ZoneId.of("Asia/Kolkata");

        // Format 1:
        // 2026-08-13T10:30:00
        try {
            return LocalDateTime
                    .parse(value)
                    .toLocalDate();

        } catch (Exception ignored) {
        }

        // Format 2:
        // 2026-08-13T10:30:00Z
        try {
            return Instant
                    .parse(value)
                    .atZone(ist)
                    .toLocalDate();

        } catch (Exception ignored) {
        }

        // Format 3:
        // 2026-08-13T10:30:00+05:30
        try {
            return OffsetDateTime
                    .parse(value)
                    .atZoneSameInstant(ist)
                    .toLocalDate();

        } catch (Exception ignored) {
        }

        return null;
    }

    public Long countByStudentAndSubject(
            User student,
            Subject subject) {

        return (long)
                findByStudentAndSubject(
                        student,
                        subject
                ).size();
    }

    public Long countByStudentAndSubjectAndStatus(
            User student,
            Subject subject,
            String status) {

        List<Attendance> all =
                findByStudentAndSubject(
                        student,
                        subject
                );

        return all.stream()
                .filter(attendance ->
                        status.equals(
                                attendance.getStatus()
                        )
                )
                .count();
    }

    public List<Attendance> findAll() {

        Map<String, Attendance> attendanceMap =
                firebaseService.getAllSync(
                        BASE_PATH,
                        Attendance.class
                );

        return attendanceMap.entrySet()
                .stream()
                .peek(entry ->
                        entry.getValue()
                                .setId(entry.getKey())
                )
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}