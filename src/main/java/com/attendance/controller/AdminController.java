package com.attendance.controller;

import com.attendance.entity.User;
import com.attendance.entity.ClassEntity;
import com.attendance.entity.Subject;
import com.attendance.service.UserService;
import com.attendance.service.SubjectService;
import com.attendance.service.ClassService;
import com.attendance.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private ClassService classService;
    
    @Autowired
    private EmailService emailService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("teachers", userService.findAllTeachers());
        model.addAttribute("students", userService.findApprovedStudents());
        model.addAttribute("pendingStudents", userService.findPendingStudents());
        model.addAttribute("classes", classService.findAllClasses());
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "admin/dashboard";
    }
    
    @GetMapping("/add-teacher")
    public String addTeacherForm(Model model) {
        model.addAttribute("classes", classService.findAllClasses());
        return "admin/add-teacher";
    }
    
    @PostMapping("/add-teacher")
    public String addTeacher(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           @RequestParam String fullName,
                           @RequestParam(required = false) String classId,
                           Model model) {
        try {
            if (userService.existsByUsername(username)) {
                model.addAttribute("error", "Username already exists");
                model.addAttribute("classes", classService.findAllClasses());
                return "admin/add-teacher";
            }
            
            if (userService.existsByEmail(email)) {
                model.addAttribute("error", "Email already exists");
                model.addAttribute("classes", classService.findAllClasses());
                return "admin/add-teacher";
            }
            
            User teacher = new User();
            teacher.setUsername(username);
            teacher.setPassword(password);
            teacher.setEmail(email);
            teacher.setFullName(fullName);
            teacher.setRoleId(1); // Teacher role
            teacher.setIsApproved(true);
            
            User savedTeacher = userService.saveUser(teacher);
            
            // Send email with credentials
            emailService.sendTeacherCredentials(email, username, password, fullName);
            
            model.addAttribute("success", "Teacher added successfully! Login credentials have been sent to " + email);
            return "redirect:/admin/dashboard";
            
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add teacher: " + e.getMessage());
            model.addAttribute("classes", classService.findAllClasses());
            return "admin/add-teacher";
        }
    }
    
    @GetMapping("/add-class")
    public String addClassForm() {
        return "admin/add-class";
    }
    
    @PostMapping("/add-class")
    public String addClass(@RequestParam String className,
                         @RequestParam String classCode,
                         @RequestParam String description,
                         Model model) {
        try {
            if (classService.existsByClassCode(classCode)) {
                model.addAttribute("error", "Class code already exists");
                return "admin/add-class";
            }
            
            classService.createClass(className, classCode, description);
            model.addAttribute("success", "Class added successfully!");
            return "redirect:/admin/dashboard";
            
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add class: " + e.getMessage());
            return "admin/add-class";
        }
    }
    
    // Update Teacher
    @PostMapping("/update-teacher")
    public String updateTeacher(@RequestParam String id,
                               @RequestParam String fullName,
                               @RequestParam String username,
                               @RequestParam String email,
                               RedirectAttributes redirectAttributes) {
        try {
            Optional<User> userOpt = userService.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                // Check if username is being changed and if it's already taken
                if (!user.getUsername().equals(username) && userService.existsByUsername(username)) {
                    redirectAttributes.addFlashAttribute("error", "Username already exists");
                    return "redirect:/admin/dashboard";
                }
                // Check if email is being changed and if it's already taken
                if (!user.getEmail().equals(email) && userService.existsByEmail(email)) {
                    redirectAttributes.addFlashAttribute("error", "Email already exists");
                    return "redirect:/admin/dashboard";
                }
                user.setFullName(fullName);
                user.setUsername(username);
                user.setEmail(email);
                userService.updateUser(user);
                redirectAttributes.addFlashAttribute("success", "Teacher updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Teacher not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update teacher: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    // Delete Teacher
    @PostMapping("/delete-teacher")
    public String deleteTeacher(@RequestParam String id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Teacher deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete teacher: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    // Update Student
    @PostMapping("/update-student")
    public String updateStudent(@RequestParam String id,
                               @RequestParam String fullName,
                               @RequestParam String username,
                               @RequestParam String email,
                               RedirectAttributes redirectAttributes) {
        try {
            Optional<User> userOpt = userService.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                // Check if username is being changed and if it's already taken
                if (!user.getUsername().equals(username) && userService.existsByUsername(username)) {
                    redirectAttributes.addFlashAttribute("error", "Username already exists");
                    return "redirect:/admin/dashboard";
                }
                // Check if email is being changed and if it's already taken
                if (!user.getEmail().equals(email) && userService.existsByEmail(email)) {
                    redirectAttributes.addFlashAttribute("error", "Email already exists");
                    return "redirect:/admin/dashboard";
                }
                user.setFullName(fullName);
                user.setUsername(username);
                user.setEmail(email);
                userService.updateUser(user);
                redirectAttributes.addFlashAttribute("success", "Student updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Student not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update student: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    // Delete Student
    @PostMapping("/delete-student")
    public String deleteStudent(@RequestParam String id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Student deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete student: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    // Update Class
    @PostMapping("/update-class")
    public String updateClass(@RequestParam String id,
                             @RequestParam String className,
                             @RequestParam String classCode,
                             @RequestParam String description,
                             RedirectAttributes redirectAttributes) {
        try {
            Optional<ClassEntity> classOpt = classService.findByClassCode(classCode);
            if (classOpt.isPresent() && !classOpt.get().getId().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "Class code already exists");
                return "redirect:/admin/dashboard";
            }
            
            ClassEntity classEntity = new ClassEntity();
            classEntity.setId(id);
            classEntity.setClassName(className);
            classEntity.setClassCode(classCode);
            classEntity.setDescription(description);
            classService.saveClass(classEntity);
            redirectAttributes.addFlashAttribute("success", "Class updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update class: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    // Delete Class
    @PostMapping("/delete-class")
    public String deleteClass(@RequestParam String id, RedirectAttributes redirectAttributes) {
        try {
            classService.deleteClass(id);
            redirectAttributes.addFlashAttribute("success", "Class deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete class: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    // Update Subject
    @PostMapping("/update-subject")
    public String updateSubject(@RequestParam String id,
                               @RequestParam String subjectName,
                               @RequestParam String subjectCode,
                               @RequestParam String description,
                               RedirectAttributes redirectAttributes) {
        try {
            Optional<Subject> subjectOpt = subjectService.findBySubjectCode(subjectCode);
            if (subjectOpt.isPresent() && !subjectOpt.get().getId().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "Subject code already exists");
                return "redirect:/admin/dashboard";
            }
            
            Optional<Subject> existingOpt = subjectService.findById(id);
            if (existingOpt.isPresent()) {
                Subject subject = existingOpt.get();
                subject.setSubjectName(subjectName);
                subject.setSubjectCode(subjectCode);
                subject.setDescription(description);
                subjectService.saveSubject(subject);
                redirectAttributes.addFlashAttribute("success", "Subject updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Subject not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update subject: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    // Delete Subject
    @PostMapping("/delete-subject")
    public String deleteSubject(@RequestParam String id, RedirectAttributes redirectAttributes) {
        try {
            subjectService.deleteSubject(id);
            redirectAttributes.addFlashAttribute("success", "Subject deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete subject: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
}
