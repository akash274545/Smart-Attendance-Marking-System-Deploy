package com.attendance.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    
    /**
     * Sends a monthly attendance report HTML email to a student.
     * @param toEmail      student's email
     * @param studentName  student's full name
     * @param monthYear    e.g. "March 2026"
     * @param rows         each entry: {"subject","present","total","percent"}
     */
    public boolean sendMonthlyAttendanceReport(String toEmail, String studentName,
                                               String monthYear,
                                               List<Map<String, String>> rows) {
        if (mailSender == null) {
            System.out.println("Mail sender not configured — skipping report for " + toEmail);
            return false;
        }
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setFrom("${MAIL_USERNAME}");
            helper.setSubject("Monthly Attendance Report — " + monthYear);
            helper.setText(buildReportHtml(studentName, monthYear, rows), true);
            mailSender.send(mime);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to send report to " + toEmail + ": " + e.getMessage());
            return false;
        }
    }

    private String buildReportHtml(String studentName, String monthYear, List<Map<String, String>> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>")
          .append("<style>")
          .append("body{font-family:Arial,sans-serif;background:#f4f6f9;margin:0;padding:20px;}")
          .append(".card{background:#fff;border-radius:12px;padding:30px;max-width:650px;margin:auto;box-shadow:0 4px 15px rgba(0,0,0,0.1);}")
          .append(".header{background:linear-gradient(135deg,#667eea,#764ba2);border-radius:8px;padding:20px;color:#fff;text-align:center;margin-bottom:24px;}")
          .append(".header h2{margin:0;font-size:22px;}")
          .append(".header p{margin:6px 0 0;opacity:.85;font-size:14px;}")
          .append("table{width:100%;border-collapse:collapse;margin-top:16px;}")
          .append("th{background:#667eea;color:#fff;padding:10px 14px;text-align:left;font-size:13px;}")
          .append("td{padding:10px 14px;border-bottom:1px solid #e9ecef;font-size:13px;}")
          .append("tr:last-child td{border-bottom:none;}")
          .append(".low{color:#dc3545;font-weight:bold;}")
          .append(".ok{color:#28a745;font-weight:bold;}")
          .append(".warn{color:#fd7e14;font-weight:bold;}")
          .append(".footer{text-align:center;margin-top:24px;font-size:12px;color:#888;}")
          .append(".badge-warn{background:#fff3cd;color:#856404;padding:8px 14px;border-radius:6px;margin-top:16px;font-size:13px;display:block;}")
          .append("</style></head><body><div class='card'>")
          .append("<div class='header'><h2>&#128197; Monthly Attendance Report</h2>")
          .append("<p>").append(monthYear).append("</p></div>")
          .append("<p>Dear <strong>").append(studentName).append("</strong>,</p>")
          .append("<p>Here is your attendance summary for <strong>").append(monthYear).append("</strong>:</p>")
          .append("<table><tr><th>Subject</th><th>Classes Held</th><th>Present</th><th>Percentage</th></tr>");

        boolean hasLow = false;
        for (Map<String, String> row : rows) {
            double pct = Double.parseDouble(row.getOrDefault("percent", "0"));
            String pctClass = pct >= 75 ? "ok" : (pct >= 60 ? "warn" : "low");
            if (pct < 75) hasLow = true;
            sb.append("<tr>")
              .append("<td>").append(row.get("subject")).append("</td>")
              .append("<td>").append(row.get("total")).append("</td>")
              .append("<td>").append(row.get("present")).append("</td>")
              .append("<td class='").append(pctClass).append("'>").append(String.format("%.1f", pct)).append("%</td>")
              .append("</tr>");
        }

        sb.append("</table>");
        if (hasLow) {
            sb.append("<span class='badge-warn'>&#9888; Warning: Your attendance in one or more subjects is below 75%. ")
              .append("Please ensure regular attendance to avoid academic consequences.</span>");
        }
        sb.append("<div class='footer'>This is an automated report from the Attendance Management System.<br>")
          .append("Please do not reply to this email.</div>")
          .append("</div></body></html>");
        return sb.toString();
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

