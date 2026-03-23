package com.attendance.service;

import com.attendance.entity.Attendance;
import com.attendance.entity.Subject;
import com.attendance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirebaseAttendanceService {

    @Autowired
    private FirebaseService firebaseService;

    private static final String BASE_PATH = "AttendanceSystem/attendance";

    public Attendance save(Attendance attendance) {
        String key = attendance.getId();
        if (key == null || key.isEmpty()) {
            key = UUID.randomUUID().toString();
            attendance.setId(key);
        }
        firebaseService.saveWithKeySync(BASE_PATH, key, attendance);
        return attendance;
    }

    public Optional<Attendance> findById(String id) {
        Attendance attendance = firebaseService.getSync(BASE_PATH + "/" + id, Attendance.class);
        if (attendance != null) {
            attendance.setId(id);
        }
        return Optional.ofNullable(attendance);
    }

    public List<Attendance> findByStudent(User student) {
        Map<String, Attendance> attendanceMap = firebaseService.getAllSync(BASE_PATH, Attendance.class);
        return attendanceMap.entrySet().stream()
                .filter(entry -> {
                    Attendance attendance = entry.getValue();
                    return attendance.getStudentId() != null && attendance.getStudentId().equals(student.getId());
                })
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Attendance> findBySubject(Subject subject) {
        Map<String, Attendance> attendanceMap = firebaseService.getAllSync(BASE_PATH, Attendance.class);
        return attendanceMap.entrySet().stream()
                .filter(entry -> {
                    Attendance attendance = entry.getValue();
                    return attendance.getSubjectId() != null && attendance.getSubjectId().equals(subject.getId());
                })
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Attendance> findByStudentAndSubject(User student, Subject subject) {
        Map<String, Attendance> attendanceMap = firebaseService.getAllSync(BASE_PATH, Attendance.class);
        return attendanceMap.entrySet().stream()
                .filter(entry -> {
                    Attendance attendance = entry.getValue();
                    return attendance.getStudentId() != null && attendance.getStudentId().equals(student.getId()) &&
                           attendance.getSubjectId() != null && attendance.getSubjectId().equals(subject.getId());
                })
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Attendance> findByStudentAndSubjectAndDate(User student, Subject subject, LocalDateTime date) {
        String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<Attendance> all = findByStudentAndSubject(student, subject);
        return all.stream()
                .filter(attendance -> {
                    if (attendance.getAttendanceDate() == null) return false;
                    try {
                        LocalDateTime attDate = LocalDateTime.parse(attendance.getAttendanceDate());
                        return attDate.toLocalDate().equals(date.toLocalDate());
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public Long countByStudentAndSubject(User student, Subject subject) {
        return (long) findByStudentAndSubject(student, subject).size();
    }

    public Long countByStudentAndSubjectAndStatus(User student, Subject subject, String status) {
        List<Attendance> all = findByStudentAndSubject(student, subject);
        return all.stream()
                .filter(attendance -> status.equals(attendance.getStatus()))
                .count();
    }

    public List<Attendance> findAll() {
        Map<String, Attendance> attendanceMap = firebaseService.getAllSync(BASE_PATH, Attendance.class);
        return attendanceMap.entrySet().stream()
                .peek(entry -> entry.getValue().setId(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}

