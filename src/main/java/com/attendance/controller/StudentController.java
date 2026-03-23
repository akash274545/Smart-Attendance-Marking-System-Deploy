package com.attendance.controller;

import com.attendance.entity.User;
import com.attendance.entity.Subject;
import com.attendance.entity.AttendanceToken;
import com.attendance.service.AttendanceTokenService;
import com.attendance.service.UserService;
import com.attendance.service.SubjectService;
import com.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/student")
public class StudentController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceTokenService attendanceTokenService;
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User student = userService.findByUsername(username).orElse(null);
        
        if (student == null) {
            return "redirect:/login";
        }
        
        if (!student.getIsApproved()) {
            model.addAttribute("message", "Your account is pending approval from teacher.");
            return "student/pending";
        }
        
        model.addAttribute("student", student);
        
        // Get subjects and enrich with teacher information
        List<com.attendance.entity.Subject> subjects = subjectService.findAllSubjects();
        Map<String, User> teacherMap = new HashMap<>();
        for (com.attendance.entity.Subject subject : subjects) {
            if (subject.getTeacherId() != null && !teacherMap.containsKey(subject.getTeacherId())) {
                userService.findById(subject.getTeacherId()).ifPresent(teacher -> 
                    teacherMap.put(subject.getTeacherId(), teacher)
                );
            }
        }
        model.addAttribute("subjects", subjects);
        model.addAttribute("teacherMap", teacherMap);
        
        // Get attendance records and enrich with subject information
        List<com.attendance.entity.Attendance> attendanceList = attendanceService.findByStudent(student);
        Map<String, com.attendance.entity.Subject> subjectMap = new HashMap<>();
        for (com.attendance.entity.Attendance attendance : attendanceList) {
            if (attendance.getSubjectId() != null && !subjectMap.containsKey(attendance.getSubjectId())) {
                subjectService.findById(attendance.getSubjectId()).ifPresent(subj -> 
                    subjectMap.put(attendance.getSubjectId(), subj)
                );
            }
        }
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("subjectMap", subjectMap);
        
        return "student/dashboard";
    }
    
    @GetMapping("/mark-attendance")
    public String markAttendanceForm(Model model) {
        List<com.attendance.entity.Subject> subjects = subjectService.findAllSubjects();
        Map<String, User> teacherMap = new HashMap<>();
        for (com.attendance.entity.Subject subject : subjects) {
            if (subject.getTeacherId() != null && !teacherMap.containsKey(subject.getTeacherId())) {
                userService.findById(subject.getTeacherId()).ifPresent(teacher -> 
                    teacherMap.put(subject.getTeacherId(), teacher)
                );
            }
        }
        model.addAttribute("subjects", subjects);
        model.addAttribute("teacherMap", teacherMap);
        return "student/mark-attendance";
    }
    
    @PostMapping("/mark-attendance")
    public String markAttendance(Authentication authentication,
                               @RequestParam String subjectCode,
                               @RequestParam(required = false) Double latitude,
                               @RequestParam(required = false) Double longitude,
                               @RequestParam(required = false) String locationAddress,
                               Model model) {
        try {
            String username = authentication.getName();
            User student = userService.findByUsername(username).orElse(null);
            
            if (student == null) {
                return "redirect:/login";
            }
            
            if (!student.getIsApproved()) {
                model.addAttribute("error", "Your account is not approved yet");
                List<com.attendance.entity.Subject> subjects = subjectService.findAllSubjects();
                Map<String, User> teacherMap = new HashMap<>();
                for (com.attendance.entity.Subject subj : subjects) {
                    if (subj.getTeacherId() != null && !teacherMap.containsKey(subj.getTeacherId())) {
                        userService.findById(subj.getTeacherId()).ifPresent(teacher -> 
                            teacherMap.put(subj.getTeacherId(), teacher)
                        );
                    }
                }
                model.addAttribute("subjects", subjects);
                model.addAttribute("teacherMap", teacherMap);
                return "student/mark-attendance";
            }
            
            Subject subject = subjectService.findBySubjectCode(subjectCode).orElse(null);
            if (subject == null) {
                model.addAttribute("error", "Invalid subject code");
                List<com.attendance.entity.Subject> subjects = subjectService.findAllSubjects();
                Map<String, User> teacherMap = new HashMap<>();
                for (com.attendance.entity.Subject subj : subjects) {
                    if (subj.getTeacherId() != null && !teacherMap.containsKey(subj.getTeacherId())) {
                        userService.findById(subj.getTeacherId()).ifPresent(teacher -> 
                            teacherMap.put(subj.getTeacherId(), teacher)
                        );
                    }
                }
                model.addAttribute("subjects", subjects);
                model.addAttribute("teacherMap", teacherMap);
                return "student/mark-attendance";
            }
            
            if (attendanceService.hasMarkedAttendanceToday(student, subject)) {
                model.addAttribute("error", "Attendance already marked for today");
                List<com.attendance.entity.Subject> subjects = subjectService.findAllSubjects();
                Map<String, User> teacherMap = new HashMap<>();
                for (com.attendance.entity.Subject subj : subjects) {
                    if (subj.getTeacherId() != null && !teacherMap.containsKey(subj.getTeacherId())) {
                        userService.findById(subj.getTeacherId()).ifPresent(teacher -> 
                            teacherMap.put(subj.getTeacherId(), teacher)
                        );
                    }
                }
                model.addAttribute("subjects", subjects);
                model.addAttribute("teacherMap", teacherMap);
                return "student/mark-attendance";
            }
            
            attendanceService.markAttendance(student, subject,
                    latitude != null ? latitude : 0.0,
                    longitude != null ? longitude : 0.0,
                    locationAddress != null ? locationAddress : "Location not provided");
            return "redirect:/student/dashboard?success=Attendance marked successfully!";
            
        } catch (Exception e) {
            model.addAttribute("error", "Failed to mark attendance: " + e.getMessage());
            List<com.attendance.entity.Subject> subjects = subjectService.findAllSubjects();
            Map<String, User> teacherMap = new HashMap<>();
            for (com.attendance.entity.Subject subj : subjects) {
                if (subj.getTeacherId() != null && !teacherMap.containsKey(subj.getTeacherId())) {
                    userService.findById(subj.getTeacherId()).ifPresent(teacher -> 
                        teacherMap.put(subj.getTeacherId(), teacher)
                    );
                }
            }
            model.addAttribute("subjects", subjects);
            model.addAttribute("teacherMap", teacherMap);
            return "student/mark-attendance";
        }
    }
    
    @GetMapping("/mark-attendance-qr")
    public String markAttendanceQrForm(@RequestParam(value = "token", required = false) String token,
                                       Model model) {
        model.addAttribute("prefilledToken", token);
        return "student/mark-attendance-qr";
    }
    
    @PostMapping("/mark-attendance-qr")
    public String markAttendanceViaQr(Authentication authentication,
                                      @RequestParam String tokenValue,
                                      @RequestParam(required = false) Double latitude,
                                      @RequestParam(required = false) Double longitude,
                                      @RequestParam(required = false) String locationAddress,
                                      Model model) {
        try {
            String username = authentication.getName();
            User student = userService.findByUsername(username).orElse(null);
            if (student == null) {
                return "redirect:/login";
            }
            if (!student.getIsApproved()) {
                model.addAttribute("error", "Your account is not approved yet");
                return "student/mark-attendance-qr";
            }
            
            AttendanceToken attendanceToken = attendanceTokenService.getActiveToken(tokenValue)
                    .orElse(null);
            if (attendanceToken == null) {
                model.addAttribute("error", "Invalid or expired QR token");
                return "student/mark-attendance-qr";
            }
            
            Subject subject = subjectService.findById(attendanceToken.getSubjectId())
                    .orElse(null);
            if (subject == null) {
                model.addAttribute("error", "Subject not found");
                return "student/mark-attendance-qr";
            }
            
            if (attendanceService.hasMarkedAttendanceToday(student, subject)) {
                model.addAttribute("error", "Attendance already marked for today for this subject");
                return "student/mark-attendance-qr";
            }

            // Proximity check: student must be within 100m of teacher's location
            if (attendanceToken.getTeacherLatitude() != null && attendanceToken.getTeacherLongitude() != null) {
                if (latitude == null || longitude == null) {
                    model.addAttribute("error", "Your location is required to mark attendance. Please allow location access.");
                    return "student/mark-attendance-qr";
                }
                double distance = calculateDistance(
                        attendanceToken.getTeacherLatitude(), attendanceToken.getTeacherLongitude(),
                        latitude, longitude
                );
                if (distance > 100) {
                    model.addAttribute("error",
                            String.format("You are not in class. You are %.0f meters away from the classroom (maximum allowed: 100 meters).", distance));
                    return "student/mark-attendance-qr";
                }
            }

            attendanceService.markAttendance(student, subject,
                    latitude != null ? latitude : 0.0,
                    longitude != null ? longitude : 0.0,
                    locationAddress != null ? locationAddress : "Marked via QR");
            
            model.addAttribute("success", "Attendance marked successfully using QR code!");
            model.addAttribute("prefilledToken", "");
            return "student/mark-attendance-qr";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to mark attendance: " + e.getMessage());
            return "student/mark-attendance-qr";
        }
    }
    
    @GetMapping("/view-attendance")
    public String viewAttendance(Authentication authentication, Model model) {
        String username = authentication.getName();
        User student = userService.findByUsername(username).orElse(null);
        
        if (student == null) {
            return "redirect:/login";
        }
        
        // Get attendance records and enrich with subject and teacher information
        List<com.attendance.entity.Attendance> attendanceList = attendanceService.findByStudent(student);
        Map<String, com.attendance.entity.Subject> subjectMap = new HashMap<>();
        Map<String, User> teacherMap = new HashMap<>();
        
        for (com.attendance.entity.Attendance attendance : attendanceList) {
            if (attendance.getSubjectId() != null && !subjectMap.containsKey(attendance.getSubjectId())) {
                subjectService.findById(attendance.getSubjectId()).ifPresent(subj -> {
                    subjectMap.put(attendance.getSubjectId(), subj);
                    // Also get teacher for this subject
                    if (subj.getTeacherId() != null && !teacherMap.containsKey(subj.getTeacherId())) {
                        userService.findById(subj.getTeacherId()).ifPresent(teacher -> 
                            teacherMap.put(subj.getTeacherId(), teacher)
                        );
                    }
                });
            }
        }
        
        model.addAttribute("student", student);
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("subjectMap", subjectMap);
        model.addAttribute("teacherMap", teacherMap);
        
        return "student/view-attendance";
    }

    // Haversine formula — returns distance in meters between two GPS coordinates
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Earth radius in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
