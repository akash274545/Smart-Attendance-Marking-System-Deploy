package com.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    public void sendTeacherCredentials(String email, String username, String password, String fullName) {
        if (mailSender == null) {
            System.out.println("Email service not configured. Please configure SMTP settings in application.properties");
            System.out.println("Teacher Credentials:");
            System.out.println("Email: " + email);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("Full Name: " + fullName);
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Welcome to Attendance Management System - Your Login Credentials");
            message.setText(buildTeacherEmailBody(username, password, fullName));
            message.setFrom("noreply@attendance.com");
            
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + email);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            System.out.println("Teacher Credentials (Email failed):");
            System.out.println("Email: " + email);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("Full Name: " + fullName);
        }
    }
    
    public void sendOTP(String email, String otp) {
        if (mailSender == null) {
            System.out.println("Email service not configured. OTP for " + email + ": " + otp);
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your OTP for Login - Attendance Management System");
            message.setText(buildOTPEmailBody(otp));
            message.setFrom("noreply@attendance.com");
            
            mailSender.send(message);
            System.out.println("OTP email sent successfully to: " + email);
        } catch (Exception e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            System.out.println("OTP for " + email + ": " + otp);
        }
    }
    
    private String buildTeacherEmailBody(String username, String password, String fullName) {
        return "Dear " + fullName + ",\n\n" +
               "Welcome to the Attendance Management System!\n\n" +
               "Your account has been created successfully. Please find your login credentials below:\n\n" +
               "Username: " + username + "\n" +
               "Password: " + password + "\n" +
               "Email: " + username + "\n\n" +
               "Please login using these credentials and change your password after first login for security purposes.\n\n" +
               "Login URL: http://localhost:8080/login\n\n" +
               "Best regards,\n" +
               "Attendance Management System";
    }
    
    private String buildOTPEmailBody(String otp) {
        return "Dear User,\n\n" +
               "You have requested to login to the Attendance Management System.\n\n" +
               "Your One-Time Password (OTP) is: " + otp + "\n\n" +
               "This OTP is valid for 10 minutes. Please do not share this OTP with anyone.\n\n" +
               "If you did not request this OTP, please ignore this email.\n\n" +
               "Best regards,\n" +
               "Attendance Management System";
    }
}

