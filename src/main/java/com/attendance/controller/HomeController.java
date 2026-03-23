package com.attendance.controller;

import com.attendance.entity.User;
import com.attendance.service.UserService;
import com.attendance.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OTPService otpService;
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "otp_required", required = false) String otpRequired,
                       Model model, HttpSession session) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (otpRequired != null) {
            model.addAttribute("otpRequired", true);
            model.addAttribute("email", session.getAttribute("pendingEmail"));
        }
        return "login";
    }
    
    @GetMapping("/verify-otp")
    public String verifyOTPForm(@RequestParam(value = "email", required = false) String email,
                                Model model, HttpSession session) {
        String pendingEmail = (String) session.getAttribute("pendingEmail");
        String pendingUsername = (String) session.getAttribute("pendingUsername");
        
        if (pendingEmail == null && email != null) {
            pendingEmail = email;
            session.setAttribute("pendingEmail", email);
        }
        
        if (pendingEmail == null) {
            return "redirect:/login?error=Session expired";
        }
        
        model.addAttribute("email", pendingEmail);
        model.addAttribute("username", pendingUsername);
        return "verify-otp";
    }
    
    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestParam String email,
                           @RequestParam String otp,
                           @RequestParam(required = false) String username,
                           Model model,
                           HttpSession session) {
        try {
            String pendingUsername = (String) session.getAttribute("pendingUsername");
            if (username == null) {
                username = pendingUsername;
            }
            
            boolean isValid = otpService.validateOTP(email, otp);
            
            if (isValid && username != null) {
                session.setAttribute("otpVerified", true);
                session.setAttribute("verifiedUsername", username);
                session.removeAttribute("pendingEmail");
                session.removeAttribute("pendingUsername");
                // Redirect to login with OTP verified flag
                return "redirect:/login?otp_verified=true&username=" + username;
            }
            
            model.addAttribute("error", "Invalid or expired OTP. Please try again.");
            model.addAttribute("email", email);
            model.addAttribute("username", username);
            return "verify-otp";
            
        } catch (Exception e) {
            model.addAttribute("error", "OTP verification failed: " + e.getMessage());
            model.addAttribute("email", email);
            model.addAttribute("username", username);
            return "verify-otp";
        }
    }
    
    @PostMapping("/resend-otp")
    public String resendOTP(@RequestParam String email,
                           @RequestParam String username,
                           Model model,
                           HttpSession session) {
        try {
            String newOTP = otpService.generateOTP(email, username);
            model.addAttribute("success", "OTP has been resent to your email.");
            model.addAttribute("email", email);
            session.setAttribute("pendingEmail", email);
            return "verify-otp";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to resend OTP: " + e.getMessage());
            model.addAttribute("email", email);
            return "verify-otp";
        }
    }
    
    @PostMapping("/check-student-login")
    public String checkStudentLogin(@RequestParam String username,
                                   Model model,
                                   HttpSession session) {
        try {
            var userOpt = userService.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (user.isStudent() && user.getIsApproved()) {
                    // Generate OTP and send to student's email
                    String otp = otpService.generateOTP(user.getEmail(), username);
                    session.setAttribute("pendingEmail", user.getEmail());
                    session.setAttribute("pendingUsername", username);
                    return "redirect:/verify-otp";
                }
            }
            // If not a student or not approved, proceed with normal login
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login?error=Error checking user";
        }
    }
    
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String email,
                              @RequestParam String fullName,
                              Model model) {
        try {
            if (userService.existsByUsername(username)) {
                model.addAttribute("error", "Username already exists");
                return "register";
            }
            
            if (userService.existsByEmail(email)) {
                model.addAttribute("error", "Email already exists");
                return "register";
            }
            
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setFullName(fullName);
            user.setRoleId(2); // Student role
            user.setIsApproved(false);
            
            userService.saveUser(user);
            model.addAttribute("success", "Registration successful! Please wait for teacher approval.");
            return "login";
            
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElse(null);
        
        if (user == null) {
            return "redirect:/login";
        }
        
        if (user.isAdmin()) {
            return "redirect:/admin/dashboard";
        } else if (user.isTeacher()) {
            return "redirect:/teacher/dashboard";
        } else if (user.isStudent()) {
            return "redirect:/student/dashboard";
        }
        
        return "redirect:/login";
    }
    
    @GetMapping("/change-password")
    public String changePasswordForm(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElse(null);
        
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        return "change-password";
    }
    
    @PostMapping("/change-password")
    public String changePassword(Authentication authentication,
                                @RequestParam String oldPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            
            if (user == null) {
                return "redirect:/login";
            }
            
            // Validate new password and confirm password match
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "New password and confirm password do not match");
                model.addAttribute("user", user);
                return "change-password";
            }
            
            // Validate password length
            if (newPassword.length() < 6) {
                model.addAttribute("error", "New password must be at least 6 characters long");
                model.addAttribute("user", user);
                return "change-password";
            }
            
            // Change password
            boolean success = userService.changePassword(user.getId(), oldPassword, newPassword);
            
            if (success) {
                model.addAttribute("success", "Password changed successfully!");
                model.addAttribute("user", user);
                return "change-password";
            } else {
                model.addAttribute("error", "Old password is incorrect");
                model.addAttribute("user", user);
                return "change-password";
            }
            
        } catch (Exception e) {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            model.addAttribute("error", "Failed to change password: " + e.getMessage());
            model.addAttribute("user", user);
            return "change-password";
        }
    }
}
