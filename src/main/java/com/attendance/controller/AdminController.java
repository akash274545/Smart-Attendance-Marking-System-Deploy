package com.attendance.controller;

import com.attendance.entity.Attendance;
import com.attendance.entity.AttendanceToken;
import com.attendance.entity.User;
import com.attendance.entity.ClassEntity;
import com.attendance.entity.Subject;
import com.attendance.service.AttendanceService;
import com.attendance.service.FirebaseAttendanceTokenService;
import com.attendance.service.UserService;
import com.attendance.service.SubjectService;
import com.attendance.service.ClassService;
import com.attendance.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private FirebaseAttendanceTokenService firebaseAttendanceTokenService;

    // ============================================================
    // TIMEZONE
    // ============================================================

    private static final ZoneId IST =
            ZoneId.of("Asia/Kolkata");

    // ============================================================
    // ADMIN DASHBOARD
    // ============================================================

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
                "teachers",
                userService.findAllTeachers()
        );

        model.addAttribute(
                "students",
                userService.findApprovedStudents()
        );

        model.addAttribute(
                "pendingStudents",
                userService.findPendingStudents()
        );

        model.addAttribute(
                "classes",
                classService.findAllClasses()
        );

        model.addAttribute(
                "subjects",
                subjectService.findAllSubjects()
        );

        return "admin/dashboard";
    }

    // ============================================================
    // ADD TEACHER
    // ============================================================

    @GetMapping("/add-teacher")
    public String addTeacherForm(Model model) {

        model.addAttribute(
                "classes",
                classService.findAllClasses()
        );

        return "admin/add-teacher";
    }

    @PostMapping("/add-teacher")
    public String addTeacher(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String fullName,
            @RequestParam(required = false) String classId,
            Model model) {

        try {

            if (userService.existsByUsername(username)) {

                model.addAttribute(
                        "error",
                        "Username already exists"
                );

                model.addAttribute(
                        "classes",
                        classService.findAllClasses()
                );

                return "admin/add-teacher";
            }

            if (userService.existsByEmail(email)) {

                model.addAttribute(
                        "error",
                        "Email already exists"
                );

                model.addAttribute(
                        "classes",
                        classService.findAllClasses()
                );

                return "admin/add-teacher";
            }

            User teacher = new User();

            teacher.setUsername(username);
            teacher.setPassword(password);
            teacher.setEmail(email);
            teacher.setFullName(fullName);

            teacher.setRoleId(1);
            teacher.setIsApproved(true);

            User savedTeacher =
                    userService.saveUser(teacher);

            // Send teacher credentials
            emailService.sendTeacherCredentials(
                    email,
                    username,
                    password,
                    fullName
            );

            model.addAttribute(
                    "success",
                    "Teacher added successfully! Login credentials have been sent to "
                            + email
            );

            return "redirect:/admin/dashboard";

        } catch (Exception e) {

            model.addAttribute(
                    "error",
                    "Failed to add teacher: "
                            + e.getMessage()
            );

            model.addAttribute(
                    "classes",
                    classService.findAllClasses()
            );

            return "admin/add-teacher";
        }
    }

    // ============================================================
    // ADD CLASS
    // ============================================================

    @GetMapping("/add-class")
    public String addClassForm() {
        return "admin/add-class";
    }

    @PostMapping("/add-class")
    public String addClass(
            @RequestParam String className,
            @RequestParam String classCode,
            @RequestParam String description,
            Model model) {

        try {

            if (classService.existsByClassCode(classCode)) {

                model.addAttribute(
                        "error",
                        "Class code already exists"
                );

                return "admin/add-class";
            }

            classService.createClass(
                    className,
                    classCode,
                    description
            );

            model.addAttribute(
                    "success",
                    "Class added successfully!"
            );

            return "redirect:/admin/dashboard";

        } catch (Exception e) {

            model.addAttribute(
                    "error",
                    "Failed to add class: "
                            + e.getMessage()
            );

            return "admin/add-class";
        }
    }

    // ============================================================
    // UPDATE TEACHER
    // ============================================================

    @PostMapping("/update-teacher")
    public String updateTeacher(
            @RequestParam String id,
            @RequestParam String fullName,
            @RequestParam String username,
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {

        try {

            Optional<User> userOpt =
                    userService.findById(id);

            if (userOpt.isPresent()) {

                User user = userOpt.get();

                if (!user.getUsername().equals(username)
                        && userService.existsByUsername(username)) {

                    redirectAttributes.addFlashAttribute(
                            "error",
                            "Username already exists"
                    );

                    return "redirect:/admin/dashboard";
                }

                if (!user.getEmail().equals(email)
                        && userService.existsByEmail(email)) {

                    redirectAttributes.addFlashAttribute(
                            "error",
                            "Email already exists"
                    );

                    return "redirect:/admin/dashboard";
                }

                user.setFullName(fullName);
                user.setUsername(username);
                user.setEmail(email);

                userService.updateUser(user);

                redirectAttributes.addFlashAttribute(
                        "success",
                        "Teacher updated successfully!"
                );

            } else {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Teacher not found"
                );
            }

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to update teacher: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/dashboard";
    }

    // ============================================================
    // DELETE TEACHER
    // ============================================================

    @PostMapping("/delete-teacher")
    public String deleteTeacher(
            @RequestParam String id,
            RedirectAttributes redirectAttributes) {

        try {

            userService.deleteUser(id);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Teacher deleted successfully!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to delete teacher: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/dashboard";
    }

    // ============================================================
    // UPDATE STUDENT
    // ============================================================

    @PostMapping("/update-student")
    public String updateStudent(
            @RequestParam String id,
            @RequestParam String fullName,
            @RequestParam String username,
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {

        try {

            Optional<User> userOpt =
                    userService.findById(id);

            if (userOpt.isPresent()) {

                User user = userOpt.get();

                if (!user.getUsername().equals(username)
                        && userService.existsByUsername(username)) {

                    redirectAttributes.addFlashAttribute(
                            "error",
                            "Username already exists"
                    );

                    return "redirect:/admin/dashboard";
                }

                if (!user.getEmail().equals(email)
                        && userService.existsByEmail(email)) {

                    redirectAttributes.addFlashAttribute(
                            "error",
                            "Email already exists"
                    );

                    return "redirect:/admin/dashboard";
                }

                user.setFullName(fullName);
                user.setUsername(username);
                user.setEmail(email);

                userService.updateUser(user);

                redirectAttributes.addFlashAttribute(
                        "success",
                        "Student updated successfully!"
                );

            } else {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Student not found"
                );
            }

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to update student: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/dashboard";
    }

    // ============================================================
    // DELETE STUDENT
    // ============================================================

    @PostMapping("/delete-student")
    public String deleteStudent(
            @RequestParam String id,
            RedirectAttributes redirectAttributes) {

        try {

            userService.deleteUser(id);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Student deleted successfully!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to delete student: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/dashboard";
    }

    // ============================================================
    // UPDATE CLASS
    // ============================================================

    @PostMapping("/update-class")
    public String updateClass(
            @RequestParam String id,
            @RequestParam String className,
            @RequestParam String classCode,
            @RequestParam String description,
            RedirectAttributes redirectAttributes) {

        try {

            Optional<ClassEntity> classOpt =
                    classService.findByClassCode(classCode);

            if (classOpt.isPresent()
                    && !classOpt.get().getId().equals(id)) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Class code already exists"
                );

                return "redirect:/admin/dashboard";
            }

            ClassEntity classEntity =
                    new ClassEntity();

            classEntity.setId(id);
            classEntity.setClassName(className);
            classEntity.setClassCode(classCode);
            classEntity.setDescription(description);

            classService.saveClass(classEntity);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Class updated successfully!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to update class: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/dashboard";
    }

    // ============================================================
    // DELETE CLASS
    // ============================================================

    @PostMapping("/delete-class")
    public String deleteClass(
            @RequestParam String id,
            RedirectAttributes redirectAttributes) {

        try {

            classService.deleteClass(id);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Class deleted successfully!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to delete class: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/dashboard";
    }

    // ============================================================
    // UPDATE SUBJECT
    // ============================================================

    @PostMapping("/update-subject")
    public String updateSubject(
            @RequestParam String id,
            @RequestParam String subjectName,
            @RequestParam String subjectCode,
            @RequestParam String description,
            RedirectAttributes redirectAttributes) {

        try {

            Optional<Subject> subjectOpt =
                    subjectService.findBySubjectCode(subjectCode);

            if (subjectOpt.isPresent()
                    && !subjectOpt.get().getId().equals(id)) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Subject code already exists"
                );

                return "redirect:/admin/dashboard";
            }

            Optional<Subject> existingOpt =
                    subjectService.findById(id);

            if (existingOpt.isPresent()) {

                Subject subject =
                        existingOpt.get();

                subject.setSubjectName(subjectName);
                subject.setSubjectCode(subjectCode);
                subject.setDescription(description);

                subjectService.saveSubject(subject);

                redirectAttributes.addFlashAttribute(
                        "success",
                        "Subject updated successfully!"
                );

            } else {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Subject not found"
                );
            }

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to update subject: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/dashboard";
    }

    // ============================================================
    // DELETE SUBJECT
    // ============================================================

    @PostMapping("/delete-subject")
    public String deleteSubject(
            @RequestParam String id,
            RedirectAttributes redirectAttributes) {

        try {

            subjectService.deleteSubject(id);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Subject deleted successfully!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to delete subject: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/dashboard";
    }

    // ============================================================
    // SEND MONTHLY ATTENDANCE REPORT
    // ============================================================

    @PostMapping("/send-monthly-report")
    public String sendMonthlyReport(
            @RequestParam int month,
            @RequestParam int year,
            RedirectAttributes redirectAttributes) {

        try {

            List<User> students =
                    userService.findApprovedStudents();

            List<Attendance> allAttendance =
                    attendanceService.findAll();

            List<Subject> allSubjects =
                    subjectService.findAllSubjects();

            List<AttendanceToken> allTokens =
                    firebaseAttendanceTokenService.findAll();

            Map<String, Subject> subjectMap =
                    allSubjects.stream()
                            .collect(Collectors.toMap(
                                    Subject::getId,
                                    s -> s,
                                    (a, b) -> a
                            ));

            // ====================================================
            // CLASS DATES PER SUBJECT
            // QR token generated = class was held
            // ====================================================

            Map<String, List<String>> classDatesPerSubject =
                    new LinkedHashMap<>();

            for (AttendanceToken token : allTokens) {

                if (token.getSubjectId() == null
                        || token.getCreatedAt() == null) {
                    continue;
                }

                LocalDate tokenDate =
                        parseDateInIST(
                                token.getCreatedAt()
                        );

                if (tokenDate == null) {
                    continue;
                }

                if (tokenDate.getMonthValue() != month
                        || tokenDate.getYear() != year) {
                    continue;
                }

                String dateStr =
                        tokenDate.toString();

                classDatesPerSubject
                        .computeIfAbsent(
                                token.getSubjectId(),
                                k -> new ArrayList<>()
                        )
                        .add(dateStr);
            }

            // Remove duplicate dates
            classDatesPerSubject.replaceAll(
                    (subjectId, dates) ->
                            dates.stream()
                                    .distinct()
                                    .sorted()
                                    .collect(Collectors.toList())
            );

            // ====================================================
            // MONTHLY ATTENDANCE RECORDS
            // ====================================================

            List<Attendance> monthlyAttendance =
                    allAttendance.stream()
                            .filter(a -> {

                                if (a.getAttendanceDate() == null) {
                                    return false;
                                }

                                LocalDate attendanceDate =
                                        parseDateInIST(
                                                a.getAttendanceDate()
                                        );

                                if (attendanceDate == null) {
                                    return false;
                                }

                                return attendanceDate.getMonthValue() == month
                                        && attendanceDate.getYear() == year;

                            })
                            .collect(Collectors.toList());

            // ====================================================
            // STUDENT ENROLLED SUBJECTS
            // ====================================================

            Map<String, Set<String>> studentEnrolledSubjects =
                    new LinkedHashMap<>();

            for (Attendance attendance : allAttendance) {

                if (attendance.getStudentId() == null
                        || attendance.getSubjectId() == null) {
                    continue;
                }

                studentEnrolledSubjects
                        .computeIfAbsent(
                                attendance.getStudentId(),
                                k -> new LinkedHashSet<>()
                        )
                        .add(
                                attendance.getSubjectId()
                        );
            }

            String monthYear =
                    Month.of(month)
                            .getDisplayName(
                                    TextStyle.FULL,
                                    Locale.ENGLISH
                            )
                            + " "
                            + year;

            int sent = 0;
            int failed = 0;

            // ====================================================
            // PROCESS EACH STUDENT
            // ====================================================

            for (User student : students) {

                if (student.getEmail() == null
                        || student.getEmail().isBlank()) {
                    continue;
                }

                Set<String> enrolledSubjectIds =
                        studentEnrolledSubjects
                                .getOrDefault(
                                        student.getId(),
                                        new LinkedHashSet<>()
                                );

                // Subjects having classes this month
                Set<String> reportSubjectIds =
                        new LinkedHashSet<>(
                                classDatesPerSubject.keySet()
                        );

                if (!enrolledSubjectIds.isEmpty()) {

                    reportSubjectIds.retainAll(
                            enrolledSubjectIds
                    );
                }

                // If student has never attended anything,
                // show all subjects having classes
                if (reportSubjectIds.isEmpty()
                        && !classDatesPerSubject.isEmpty()) {

                    reportSubjectIds =
                            new LinkedHashSet<>(
                                    classDatesPerSubject.keySet()
                            );
                }

                if (reportSubjectIds.isEmpty()) {
                    continue;
                }

                // ====================================================
                // STUDENT PRESENT DATES
                // ====================================================

                Map<String, Set<String>> studentPresentDates =
                        new LinkedHashMap<>();

                for (Attendance attendance :
                        monthlyAttendance) {

                    if (!student.getId().equals(
                            attendance.getStudentId())) {
                        continue;
                    }

                    if (!"PRESENT".equalsIgnoreCase(
                            attendance.getStatus())) {
                        continue;
                    }

                    if (attendance.getSubjectId() == null) {
                        continue;
                    }

                    LocalDate presentDate =
                            parseDateInIST(
                                    attendance.getAttendanceDate()
                            );

                    if (presentDate == null) {
                        continue;
                    }

                    String dateStr =
                            presentDate.toString();

                    studentPresentDates
                            .computeIfAbsent(
                                    attendance.getSubjectId(),
                                    k -> new LinkedHashSet<>()
                            )
                            .add(dateStr);
                }

                // ====================================================
                // BUILD REPORT ROWS
                // ====================================================

                List<Map<String, String>> rows =
                        new ArrayList<>();

                for (String subjectId :
                        reportSubjectIds) {

                    Subject subject =
                            subjectMap.get(subjectId);

                    if (subject == null) {
                        continue;
                    }

                    List<String> classDates =
                            classDatesPerSubject
                                    .getOrDefault(
                                            subjectId,
                                            new ArrayList<>()
                                    );

                    if (classDates.isEmpty()) {
                        continue;
                    }

                    Set<String> presentDates =
                            studentPresentDates
                                    .getOrDefault(
                                            subjectId,
                                            new LinkedHashSet<>()
                                    );

                    // =================================================
                    // IMPORTANT ATTENDANCE CALCULATION
                    // =================================================

                    long total =
                            classDates.size();

                    long present =
                            presentDates.stream()
                                    .filter(classDates::contains)
                                    .count();

                    long absent =
                            total - present;

                    double percentage =
                            total > 0
                                    ? (present * 100.0 / total)
                                    : 0;

                    // =================================================
                    // DAY-WISE STATUS
                    // =================================================

                    String dayWise =
                            classDates.stream()
                                    .map(date ->
                                            date
                                                    + ":"
                                                    + (
                                                    presentDates.contains(date)
                                                            ? "PRESENT"
                                                            : "ABSENT"
                                            )
                                    )
                                    .collect(
                                            Collectors.joining(",")
                                    );

                    Map<String, String> row =
                            new LinkedHashMap<>();

                    row.put(
                            "subject",
                            subject.getSubjectName()
                                    + " ("
                                    + subject.getSubjectCode()
                                    + ")"
                    );

                    row.put(
                            "total",
                            String.valueOf(total)
                    );

                    row.put(
                            "present",
                            String.valueOf(present)
                    );

                    row.put(
                            "absent",
                            String.valueOf(absent)
                    );

                    row.put(
                            "percent",
                            String.valueOf(percentage)
                    );

                    row.put(
                            "dayWise",
                            dayWise
                    );

                    rows.add(row);
                }

                if (rows.isEmpty()) {
                    continue;
                }

                // ====================================================
                // RECIPIENTS
                // ====================================================

                List<String> recipients =
                        new ArrayList<>();

                recipients.add(
                        student.getEmail()
                );

                if (student.getParentEmail() != null
                        && !student.getParentEmail().isBlank()) {

                    recipients.add(
                            student.getParentEmail()
                    );
                }

                // ====================================================
                // SEND EMAIL
                // ====================================================

                boolean success =
                        emailService.sendMonthlyAttendanceReport(
                                recipients,
                                student.getFullName(),
                                monthYear,
                                rows
                        );

                if (success) {
                    sent++;
                } else {
                    failed++;
                }
            }

            String msg =
                    "Monthly report sent to "
                            + sent
                            + " student(s).";

            if (failed > 0) {

                msg +=
                        " "
                                + failed
                                + " failed.";
            }

            redirectAttributes.addFlashAttribute(
                    "success",
                    msg
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to send reports: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/dashboard";
    }

    // ============================================================
    // SEND OVERALL ATTENDANCE REPORT
    // ============================================================

    @PostMapping("/send-overall-report")
    public String sendOverallReport(
            RedirectAttributes redirectAttributes) {

        try {

            List<User> students =
                    userService.findApprovedStudents();

            List<Attendance> allAttendance =
                    attendanceService.findAll();

            List<Subject> allSubjects =
                    subjectService.findAllSubjects();

            List<AttendanceToken> allTokens =
                    firebaseAttendanceTokenService.findAll();

            Map<String, Subject> subjectMap =
                    allSubjects.stream()
                            .collect(Collectors.toMap(
                                    Subject::getId,
                                    s -> s,
                                    (a, b) -> a
                            ));

            // ====================================================
            // ALL CLASS DATES PER SUBJECT
            // ====================================================

            Map<String, List<String>> allClassDatesPerSubject =
                    new LinkedHashMap<>();

            for (AttendanceToken token : allTokens) {

                if (token.getSubjectId() == null
                        || token.getCreatedAt() == null) {
                    continue;
                }

                LocalDate tokenDate =
                        parseDateInIST(
                                token.getCreatedAt()
                        );

                if (tokenDate == null) {
                    continue;
                }

                String dateStr =
                        tokenDate.toString();

                allClassDatesPerSubject
                        .computeIfAbsent(
                                token.getSubjectId(),
                                k -> new ArrayList<>()
                        )
                        .add(dateStr);
            }

            allClassDatesPerSubject.replaceAll(
                    (subjectId, dates) ->
                            dates.stream()
                                    .distinct()
                                    .sorted()
                                    .collect(Collectors.toList())
            );

            // ====================================================
            // STUDENT ENROLLED SUBJECTS
            // ====================================================

            Map<String, Set<String>> studentEnrolledSubjects =
                    new LinkedHashMap<>();

            for (Attendance attendance :
                    allAttendance) {

                if (attendance.getStudentId() == null
                        || attendance.getSubjectId() == null) {
                    continue;
                }

                studentEnrolledSubjects
                        .computeIfAbsent(
                                attendance.getStudentId(),
                                k -> new LinkedHashSet<>()
                        )
                        .add(
                                attendance.getSubjectId()
                        );
            }

            int sent = 0;
            int failed = 0;

            // ====================================================
            // PROCESS EACH STUDENT
            // ====================================================

            for (User student : students) {

                if (student.getEmail() == null
                        || student.getEmail().isBlank()) {
                    continue;
                }

                Set<String> enrolledSubjectIds =
                        studentEnrolledSubjects
                                .getOrDefault(
                                        student.getId(),
                                        new LinkedHashSet<>()
                                );

                Set<String> reportSubjectIds =
                        new LinkedHashSet<>(
                                allClassDatesPerSubject.keySet()
                        );

                if (!enrolledSubjectIds.isEmpty()) {

                    reportSubjectIds.retainAll(
                            enrolledSubjectIds
                    );
                }

                if (reportSubjectIds.isEmpty()) {
                    continue;
                }

                // ====================================================
                // STUDENT PRESENT DATES
                // ====================================================

                Map<String, Set<String>> studentPresentDates =
                        new LinkedHashMap<>();

                for (Attendance attendance :
                        allAttendance) {

                    if (!student.getId().equals(
                            attendance.getStudentId())) {
                        continue;
                    }

                    if (!"PRESENT".equalsIgnoreCase(
                            attendance.getStatus())) {
                        continue;
                    }

                    if (attendance.getSubjectId() == null) {
                        continue;
                    }

                    LocalDate presentDate =
                            parseDateInIST(
                                    attendance.getAttendanceDate()
                            );

                    if (presentDate == null) {
                        continue;
                    }

                    String dateStr =
                            presentDate.toString();

                    studentPresentDates
                            .computeIfAbsent(
                                    attendance.getSubjectId(),
                                    k -> new LinkedHashSet<>()
                            )
                            .add(dateStr);
                }

                // ====================================================
                // BUILD REPORT ROWS
                // ====================================================

                List<Map<String, String>> rows =
                        new ArrayList<>();

                for (String subjectId :
                        reportSubjectIds) {

                    Subject subject =
                            subjectMap.get(subjectId);

                    if (subject == null) {
                        continue;
                    }

                    List<String> classDates =
                            allClassDatesPerSubject
                                    .getOrDefault(
                                            subjectId,
                                            new ArrayList<>()
                                    );

                    if (classDates.isEmpty()) {
                        continue;
                    }

                    Set<String> presentDates =
                            studentPresentDates
                                    .getOrDefault(
                                            subjectId,
                                            new LinkedHashSet<>()
                                    );

                    long total =
                            classDates.size();

                    long present =
                            presentDates.stream()
                                    .filter(classDates::contains)
                                    .count();

                    long absent =
                            total - present;

                    double percentage =
                            total > 0
                                    ? (present * 100.0 / total)
                                    : 0;

                    String dayWise =
                            classDates.stream()
                                    .map(date ->
                                            date
                                                    + ":"
                                                    + (
                                                    presentDates.contains(date)
                                                            ? "PRESENT"
                                                            : "ABSENT"
                                            )
                                    )
                                    .collect(
                                            Collectors.joining(",")
                                    );

                    Map<String, String> row =
                            new LinkedHashMap<>();

                    row.put(
                            "subject",
                            subject.getSubjectName()
                                    + " ("
                                    + subject.getSubjectCode()
                                    + ")"
                    );

                    row.put(
                            "total",
                            String.valueOf(total)
                    );

                    row.put(
                            "present",
                            String.valueOf(present)
                    );

                    row.put(
                            "absent",
                            String.valueOf(absent)
                    );

                    row.put(
                            "percent",
                            String.valueOf(percentage)
                    );

                    row.put(
                            "dayWise",
                            dayWise
                    );

                    rows.add(row);
                }

                if (rows.isEmpty()) {
                    continue;
                }

                // ====================================================
                // RECIPIENTS
                // ====================================================

                List<String> recipients =
                        new ArrayList<>();

                recipients.add(
                        student.getEmail()
                );

                if (student.getParentEmail() != null
                        && !student.getParentEmail().isBlank()) {

                    recipients.add(
                            student.getParentEmail()
                    );
                }

                // ====================================================
                // SEND EMAIL
                // ====================================================

                boolean success =
                        emailService.sendMonthlyAttendanceReport(
                                recipients,
                                student.getFullName(),
                                "Overall (All Time)",
                                rows
                        );

                if (success) {
                    sent++;
                } else {
                    failed++;
                }
            }

            String msg =
                    "Overall report sent to "
                            + sent
                            + " student(s).";

            if (failed > 0) {

                msg +=
                        " "
                                + failed
                                + " failed.";
            }

            redirectAttributes.addFlashAttribute(
                    "success",
                    msg
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Failed to send overall reports: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/dashboard";
    }

    // ============================================================
    // DATE PARSER - IST
    // ============================================================

    /**
     * Converts different date-time formats into
     * Indian Standard Time calendar date.
     *
     * Supported:
     *
     * 1. LocalDateTime
     *    2026-08-13T10:30:00
     *
     * 2. ISO Instant / UTC
     *    2026-08-13T05:00:00Z
     *
     * 3. OffsetDateTime
     *    2026-08-13T10:30:00+05:30
     */
    private LocalDate parseDateInIST(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        // ========================================================
        // FORMAT 1 - LocalDateTime
        // ========================================================

        try {

            return LocalDateTime
                    .parse(value)
                    .toLocalDate();

        } catch (Exception ignored) {
        }

        // ========================================================
        // FORMAT 2 - UTC / INSTANT
        // ========================================================

        try {

            return Instant
                    .parse(value)
                    .atZone(IST)
                    .toLocalDate();

        } catch (Exception ignored) {
        }

        // ========================================================
        // FORMAT 3 - OFFSET DATETIME
        // ========================================================

        try {

            return OffsetDateTime
                    .parse(value)
                    .atZoneSameInstant(IST)
                    .toLocalDate();

        } catch (Exception ignored) {
        }

        return null;
    }
}