package com.attendance.controller;

import com.attendance.entity.Subject;
import com.attendance.entity.User;
import com.attendance.entity.AttendanceToken;
import com.attendance.service.UserService;
import com.attendance.service.SubjectService;
import com.attendance.service.AttendanceService;
import com.attendance.service.AttendanceTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private AttendanceTokenService attendanceTokenService;
    
    @Autowired
    private com.attendance.service.ClassService classService;
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        try {
            String username = authentication.getName();
            User teacher = userService.findByUsername(username).orElse(null);
            
            if (teacher == null) {
                return "redirect:/login?error=User not found";
            }
            
            // Verify user is actually a teacher
            if (!teacher.isTeacher()) {
                return "redirect:/login?error=Access denied";
            }
            
            model.addAttribute("teacher", teacher);
            model.addAttribute("subjects", subjectService.findByTeacher(teacher));
            model.addAttribute("pendingStudents", userService.findPendingStudents() != null ? userService.findPendingStudents() : new java.util.ArrayList<>());
            model.addAttribute("approvedStudents", userService.findApprovedStudents() != null ? userService.findApprovedStudents() : new java.util.ArrayList<>());
            
            return "teacher/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login?error=An error occurred";
        }
    }
    
    @PostMapping("/approve-student/{studentId}")
    public String approveStudent(@PathVariable String studentId) {
        try {
            userService.approveStudent(studentId);
            return "redirect:/teacher/dashboard?success=Student approved successfully!";
        } catch (Exception e) {
            return "redirect:/teacher/dashboard?error=Failed to approve student: " + e.getMessage();
        }
    }
    
    @GetMapping("/add-subject")
    public String addSubjectForm(Authentication authentication, Model model) {
        String username = authentication.getName();
        User teacher = userService.findByUsername(username).orElse(null);
        model.addAttribute("teacher", teacher);
        model.addAttribute("classes", classService.findAllClasses());
        model.addAttribute("defaultSubjects", subjectService.findAllDefaultSubjects());
        model.addAttribute("allSubjects", subjectService.findAllSubjects());
        return "teacher/add-subject";
    }
    
    @PostMapping("/add-subject")
    public String addSubject(Authentication authentication,
                           @RequestParam String subjectName,
                           @RequestParam String subjectCode,
                           @RequestParam String description,
                           @RequestParam String classId,
                           Model model) {
        try {
            String username = authentication.getName();
            User teacher = userService.findByUsername(username).orElse(null);
            
            if (teacher == null) {
                return "redirect:/login";
            }
            
            if (subjectService.existsBySubjectCode(subjectCode)) {
                model.addAttribute("error", "Subject code already exists");
                model.addAttribute("teacher", teacher);
                model.addAttribute("classes", classService.findAllClasses());
                return "teacher/add-subject";
            }
            
            Subject subject = new Subject();
            subject.setSubjectName(subjectName);
            subject.setSubjectCode(subjectCode);
            subject.setDescription(description);
            subject.setTeacherId(teacher.getId());
            subject.setClassId(classId);
            subjectService.saveSubject(subject);
            return "redirect:/teacher/dashboard?success=Subject added successfully!";
            
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add subject: " + e.getMessage());
            model.addAttribute("classes", classService.findAllClasses());
            return "teacher/add-subject";
        }
    }
    
    @GetMapping("/view-attendance/{subjectId}")
    public String viewAttendance(@PathVariable String subjectId, Authentication authentication, Model model) {
        User teacher = userService.findByUsername(authentication.getName()).orElse(null);
        if (teacher == null) {
            return "redirect:/login";
        }
        Optional<Subject> subjectOpt = subjectService.findById(subjectId)
                .filter(subject -> subject.getTeacherId() != null && subject.getTeacherId().equals(teacher.getId()));
        if (subjectOpt.isEmpty()) {
            return "redirect:/teacher/dashboard?error=Subject not found or access denied";
        }
        Subject subject = subjectOpt.get();
        model.addAttribute("subject", subject);
        
        // Get attendance records and enrich with student information
        List<com.attendance.entity.Attendance> attendanceList = attendanceService.findBySubject(subject);
        // Create a map to store student info for each attendance record
        Map<String, User> studentMap = new HashMap<>();
        for (com.attendance.entity.Attendance attendance : attendanceList) {
            if (attendance.getStudentId() != null && !studentMap.containsKey(attendance.getStudentId())) {
                userService.findById(attendance.getStudentId()).ifPresent(student -> 
                    studentMap.put(attendance.getStudentId(), student)
                );
            }
        }
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("studentMap", studentMap);
        
        attendanceTokenService.getLatestActiveTokenForSubject(subject)
                .ifPresent(token -> model.addAttribute("activeToken", token));
        
        return "teacher/view-attendance";
    }
    
    @PostMapping("/subjects/{subjectId}/generate-qr")
    public String generateQrCode(@PathVariable String subjectId,
                                 Authentication authentication,
                                 HttpServletRequest request,
                                 @RequestParam(required = false) Double teacherLatitude,
                                 @RequestParam(required = false) Double teacherLongitude,
                                 RedirectAttributes redirectAttributes) {
        User teacher = userService.findByUsername(authentication.getName()).orElse(null);
        if (teacher == null) {
            return "redirect:/login";
        }
        
        Optional<Subject> subjectOpt = subjectService.findById(subjectId)
                .filter(subject -> subject.getTeacherId() != null && subject.getTeacherId().equals(teacher.getId()));
        if (subjectOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Subject not found or access denied");
            return "redirect:/teacher/dashboard";
        }
        
        Subject subject = subjectOpt.get();
        AttendanceToken token = attendanceTokenService.generateToken(teacher, subject, teacherLatitude, teacherLongitude);
        String qrUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath("/student/mark-attendance-qr")
                .replaceQueryParam("token", token.getToken())
                .build()
                .toString();
        
        redirectAttributes.addFlashAttribute("success", "QR code generated successfully!");
        redirectAttributes.addFlashAttribute("qrToken", token.getToken());
        redirectAttributes.addFlashAttribute("qrExpiresAt", token.getExpiresAt());
        redirectAttributes.addFlashAttribute("qrUrl", qrUrl);
        
        return "redirect:/teacher/subjects/" + subject.getId() + "/qr";
    }
    
    @GetMapping("/subjects/{subjectId}/qr")
    public String viewQrPage(@PathVariable String subjectId,
                             Authentication authentication,
                             HttpServletRequest request,
                             Model model) {
        User teacher = userService.findByUsername(authentication.getName()).orElse(null);
        if (teacher == null) {
            return "redirect:/login";
        }
        Optional<Subject> subjectOpt = subjectService.findById(subjectId)
                .filter(subject -> subject.getTeacherId() != null && subject.getTeacherId().equals(teacher.getId()));
        if (subjectOpt.isEmpty()) {
            return "redirect:/teacher/dashboard?error=Subject not found or access denied";
        }
        
        Subject subject = subjectOpt.get();
        model.addAttribute("subject", subject);
        
        if (!model.containsAttribute("qrToken")) {
            attendanceTokenService.getLatestActiveTokenForSubject(subject).ifPresent(token -> {
                model.addAttribute("qrToken", token.getToken());
                model.addAttribute("qrExpiresAt", token.getExpiresAt());
                String qrUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                        .replacePath("/student/mark-attendance-qr")
                        .replaceQueryParam("token", token.getToken())
                        .build()
                        .toString();
                model.addAttribute("qrUrl", qrUrl);
            });
        } else if (!model.containsAttribute("qrUrl")) {
            String tokenValue = (String) model.asMap().get("qrToken");
            if (tokenValue != null) {
                String qrUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                        .replacePath("/student/mark-attendance-qr")
                        .replaceQueryParam("token", tokenValue)
                        .build()
                        .toString();
                model.addAttribute("qrUrl", qrUrl);
            }
        }
        
        return "teacher/subject-qr";
    }
}
