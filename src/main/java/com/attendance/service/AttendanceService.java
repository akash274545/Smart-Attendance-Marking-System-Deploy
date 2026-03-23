package com.attendance.service;

import com.attendance.entity.Attendance;
import com.attendance.entity.Subject;
import com.attendance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AttendanceService {
    
    @Autowired
    private FirebaseAttendanceService firebaseAttendanceService;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public Attendance saveAttendance(Attendance attendance) {
        return firebaseAttendanceService.save(attendance);
    }
    
    public List<Attendance> findByStudent(User student) {
        return firebaseAttendanceService.findByStudent(student);
    }
    
    public List<Attendance> findBySubject(Subject subject) {
        return firebaseAttendanceService.findBySubject(subject);
    }
    
    public List<Attendance> findByStudentAndSubject(User student, Subject subject) {
        return firebaseAttendanceService.findByStudentAndSubject(student, subject);
    }
    
    public List<Attendance> findByStudentAndSubjectAndDate(User student, Subject subject, LocalDateTime date) {
        return firebaseAttendanceService.findByStudentAndSubjectAndDate(student, subject, date);
    }
    
    public Long countByStudentAndSubject(User student, Subject subject) {
        return firebaseAttendanceService.countByStudentAndSubject(student, subject);
    }
    
    public Long countByStudentAndSubjectAndStatus(User student, Subject subject, String status) {
        return firebaseAttendanceService.countByStudentAndSubjectAndStatus(student, subject, status);
    }
    
    public Attendance markAttendance(User student, Subject subject, Double latitude, Double longitude, String locationAddress) {
        // Check if attendance already marked for today
        LocalDateTime now = LocalDateTime.now();
        List<Attendance> todayAttendance = findByStudentAndSubjectAndDate(student, subject, now);
        
        if (!todayAttendance.isEmpty()) {
            throw new RuntimeException("Attendance already marked for today");
        }
        
        Attendance attendance = new Attendance();
        attendance.setStudentId(student.getId());
        attendance.setSubjectId(subject.getId());
        attendance.setAttendanceDate(now.format(DATE_FORMATTER));
        attendance.setLatitude(latitude);
        attendance.setLongitude(longitude);
        attendance.setLocationAddress(locationAddress);
        attendance.setStatus("PRESENT");
        
        return saveAttendance(attendance);
    }
    
    public List<Attendance> findAll() {
        return firebaseAttendanceService.findAll();
    }

    public boolean hasMarkedAttendanceToday(User student, Subject subject) {
        LocalDateTime now = LocalDateTime.now();
        List<Attendance> todayAttendance = findByStudentAndSubjectAndDate(student, subject, now);
        return !todayAttendance.isEmpty();
    }
}
