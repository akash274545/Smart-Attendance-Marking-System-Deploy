package com.attendance.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    // ============================================================
    // TEACHER CREDENTIAL EMAIL
    // ============================================================

    public void sendTeacherCredentials(
            String email,
            String username,
            String password,
            String fullName) {

        if (mailSender == null) {
            System.out.println(
                    "Email service not configured. Please configure SMTP settings in application.properties"
            );

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
            message.setSubject(
                    "Welcome to Attendance Management System - Your Login Credentials"
            );
            message.setText(
                    buildTeacherEmailBody(
                            username,
                            password,
                            fullName
                    )
            );
            message.setFrom(mailUsername);

            mailSender.send(message);

            System.out.println(
                    "Email sent successfully to: " + email
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send email: " + e.getMessage()
            );

            System.out.println(
                    "Teacher Credentials (Email failed):"
            );

            System.out.println("Email: " + email);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("Full Name: " + fullName);
        }
    }

    // ============================================================
    // OTP EMAIL
    // ============================================================

    public void sendOTP(String email, String otp) {

        if (mailSender == null) {
            System.out.println(
                    "Email service not configured. OTP for "
                            + email + ": " + otp
            );
            return;
        }

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(email);

            message.setSubject(
                    "Your OTP for Login - Attendance Management System"
            );

            message.setText(
                    buildOTPEmailBody(otp)
            );

            message.setFrom(mailUsername);

            mailSender.send(message);

            System.out.println(
                    "OTP email sent successfully to: " + email
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send OTP email: "
                            + e.getMessage()
            );

            System.out.println(
                    "OTP for " + email + ": " + otp
            );
        }
    }

    // ============================================================
    // TEACHER EMAIL BODY
    // ============================================================

    private String buildTeacherEmailBody(
            String username,
            String password,
            String fullName) {

        return "Dear " + fullName + ",\n\n"
                + "Welcome to the Attendance Management System!\n\n"
                + "Your account has been created successfully. "
                + "Please find your login credentials below:\n\n"
                + "Username: " + username + "\n"
                + "Password: " + password + "\n"
                + "Email: " + username + "\n\n"
                + "Please login using these credentials and "
                + "change your password after first login "
                + "for security purposes.\n\n"
                + "Login URL: http://localhost:8081/login\n\n"
                + "Best regards,\n"
                + "Attendance Management System";
    }

    // ============================================================
    // MONTHLY / OVERALL ATTENDANCE REPORT
    // ============================================================

    /**
     * Sends attendance report HTML email to a student.
     *
     * @param toEmails     student and optional parent email
     * @param studentName  student's full name
     * @param monthYear    e.g. "August 2026"
     * @param rows         each entry:
     *                     subject, present, total, absent,
     *                     percent, dayWise
     */
    public boolean sendMonthlyAttendanceReport(
            List<String> toEmails,
            String studentName,
            String monthYear,
            List<Map<String, String>> rows) {

        if (mailSender == null) {

            System.out.println(
                    "Mail sender not configured — skipping report for "
                            + studentName
            );

            return false;
        }

        try {

            MimeMessage mime =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mime,
                            true,
                            "UTF-8"
                    );

            helper.setTo(
                    toEmails.toArray(new String[0])
            );

            helper.setFrom(mailUsername);

            helper.setSubject(
                    "Monthly Attendance Report — "
                            + monthYear
            );

            helper.setText(
                    buildReportHtml(
                            studentName,
                            monthYear,
                            rows
                    ),
                    true
            );

            mailSender.send(mime);

            System.out.println(
                    "Attendance report sent successfully to "
                            + studentName
            );

            return true;

        } catch (Exception e) {

            System.err.println(
                    "Failed to send report for "
                            + studentName
                            + ": "
                            + e.getMessage()
            );

            return false;
        }
    }

    // ============================================================
    // BUILD HTML REPORT
    // ============================================================

    private String buildReportHtml(
            String studentName,
            String monthYear,
            List<Map<String, String>> rows) {

        StringBuilder sb =
                new StringBuilder();

        sb.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append("<meta charset='UTF-8'>")

                .append("<style>")

                .append(
                        "body{"
                                + "font-family:Arial,sans-serif;"
                                + "background:#f4f6f9;"
                                + "margin:0;"
                                + "padding:20px;"
                                + "}"
                )

                .append(
                        ".card{"
                                + "background:#fff;"
                                + "border-radius:12px;"
                                + "padding:30px;"
                                + "max-width:700px;"
                                + "margin:auto;"
                                + "box-shadow:0 4px 15px rgba(0,0,0,0.1);"
                                + "}"
                )

                .append(
                        ".header{"
                                + "background:linear-gradient(135deg,#667eea,#764ba2);"
                                + "border-radius:8px;"
                                + "padding:20px;"
                                + "color:#fff;"
                                + "text-align:center;"
                                + "margin-bottom:24px;"
                                + "}"
                )

                .append(
                        ".header h2{"
                                + "margin:0;"
                                + "font-size:22px;"
                                + "}"
                )

                .append(
                        ".header p{"
                                + "margin:6px 0 0;"
                                + "opacity:.85;"
                                + "font-size:14px;"
                                + "}"
                )

                .append(
                        "table{"
                                + "width:100%;"
                                + "border-collapse:collapse;"
                                + "margin-top:8px;"
                                + "}"
                )

                .append(
                        "th{"
                                + "background:#667eea;"
                                + "color:#fff;"
                                + "padding:10px 12px;"
                                + "text-align:left;"
                                + "font-size:12px;"
                                + "}"
                )

                .append(
                        "td{"
                                + "padding:9px 12px;"
                                + "border-bottom:1px solid #e9ecef;"
                                + "font-size:12px;"
                                + "}"
                )

                .append(
                        "tr:last-child td{"
                                + "border-bottom:none;"
                                + "}"
                )

                .append(
                        ".low{"
                                + "color:#dc3545;"
                                + "font-weight:bold;"
                                + "}"
                )

                .append(
                        ".ok{"
                                + "color:#28a745;"
                                + "font-weight:bold;"
                                + "}"
                )

                .append(
                        ".warn{"
                                + "color:#fd7e14;"
                                + "font-weight:bold;"
                                + "}"
                )

                .append(
                        ".footer{"
                                + "text-align:center;"
                                + "margin-top:24px;"
                                + "font-size:12px;"
                                + "color:#888;"
                                + "}"
                )

                .append(
                        ".badge-warn{"
                                + "background:#fff3cd;"
                                + "color:#856404;"
                                + "padding:8px 14px;"
                                + "border-radius:6px;"
                                + "margin-top:16px;"
                                + "font-size:13px;"
                                + "}"
                )

                .append(
                        ".section-title{"
                                + "font-weight:bold;"
                                + "font-size:14px;"
                                + "margin:20px 0 6px;"
                                + "color:#333;"
                                + "border-bottom:2px solid #667eea;"
                                + "padding-bottom:4px;"
                                + "}"
                )

                .append(
                        ".day-table th{"
                                + "background:#764ba2;"
                                + "}"
                )

                .append(
                        ".present-day{"
                                + "color:#28a745;"
                                + "font-weight:600;"
                                + "}"
                )

                .append(
                        ".absent-day{"
                                + "color:#dc3545;"
                                + "font-weight:600;"
                                + "}"
                )

                .append(
                        ".total-row{"
                                + "background:#f0f3ff;"
                                + "font-weight:bold;"
                                + "}"
                )

                .append(
                        ".subject-name{"
                                + "font-size:11px;"
                                + "color:#777;"
                                + "margin-top:3px;"
                                + "}"
                )

                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<div class='card'>");

        // ========================================================
        // HEADER
        // ========================================================

        sb.append(
                        "<div class='header'>"
                )
                .append(
                        "<h2>&#128197; Monthly Attendance Report</h2>"
                )
                .append("<p>")
                .append(monthYear)
                .append("</p>")
                .append("</div>");

        sb.append("<p>Dear <strong>")
                .append(studentName)
                .append("</strong>,</p>")

                .append("<p>")
                .append("Here is your attendance summary for ")
                .append("<strong>")
                .append(monthYear)
                .append("</strong>:")
                .append("</p>");

        // ========================================================
        // ATTENDANCE SUMMARY
        // ========================================================

        sb.append(
                        "<div class='section-title'>"
                                + "&#128203; Attendance Summary"
                                + "</div>"
                )

                .append("<table>")
                .append("<tr>")
                .append("<th>Subject</th>")
                .append("<th>Classes Held</th>")
                .append("<th>Present</th>")
                .append("<th>Absent</th>")
                .append("<th>Percentage</th>")
                .append("</tr>");

        boolean hasLow = false;

        long grandTotal = 0;
        long grandPresent = 0;
        long grandAbsent = 0;

        for (Map<String, String> row : rows) {

            double pct =
                    Double.parseDouble(
                            row.getOrDefault(
                                    "percent",
                                    "0"
                            )
                    );

            String pctClass =
                    pct >= 75
                            ? "ok"
                            : (pct >= 60
                            ? "warn"
                            : "low");

            if (pct < 75) {
                hasLow = true;
            }

            long total =
                    Long.parseLong(
                            row.getOrDefault(
                                    "total",
                                    "0"
                            )
                    );

            long present =
                    Long.parseLong(
                            row.getOrDefault(
                                    "present",
                                    "0"
                            )
                    );

            long absent =
                    Long.parseLong(
                            row.getOrDefault(
                                    "absent",
                                    "0"
                            )
                    );

            grandTotal += total;
            grandPresent += present;
            grandAbsent += absent;

            sb.append("<tr>")

                    .append("<td>")
                    .append(row.getOrDefault(
                            "subject",
                            "Unknown Subject"
                    ))
                    .append("</td>")

                    .append("<td>")
                    .append(total)
                    .append("</td>")

                    .append("<td class='ok'>")
                    .append(present)
                    .append("</td>")

                    .append("<td class='low'>")
                    .append(absent)
                    .append("</td>")

                    .append("<td class='")
                    .append(pctClass)
                    .append("'>")
                    .append(String.format(
                            "%.1f",
                            pct
                    ))
                    .append("%</td>")

                    .append("</tr>");
        }

        // ========================================================
        // OVERALL TOTAL
        // ========================================================

        double overallPct =
                grandTotal > 0
                        ? (grandPresent * 100.0 / grandTotal)
                        : 0;

        String overallClass =
                overallPct >= 75
                        ? "ok"
                        : (overallPct >= 60
                        ? "warn"
                        : "low");

        sb.append("<tr class='total-row'>")

                .append("<td>")
                .append("&#128313; Overall Total")
                .append("</td>")

                .append("<td>")
                .append(grandTotal)
                .append("</td>")

                .append("<td class='ok'>")
                .append(grandPresent)
                .append("</td>")

                .append("<td class='low'>")
                .append(grandAbsent)
                .append("</td>")

                .append("<td class='")
                .append(overallClass)
                .append("'>")
                .append(String.format(
                        "%.1f",
                        overallPct
                ))
                .append("%</td>")

                .append("</tr>")

                .append("</table>");

        // ========================================================
        // LOW ATTENDANCE WARNING
        // ========================================================

        if (hasLow) {

            sb.append(
                            "<div class='badge-warn'>"
                    )

                    .append(
                            "&#9888; Warning: Your attendance "
                                    + "in one or more subjects is below 75%. "
                                    + "Please ensure regular attendance "
                                    + "to avoid academic consequences."
                    )

                    .append("</div>");
        }

        // ========================================================
        // DAY-WISE ATTENDANCE
        //
        // IMPORTANT:
        // All subjects are combined first.
        // Then records are sorted by DATE.
        //
        // Example:
        //
        // Research Methodology -> 2026-08-12
        // Chemistry             -> 2026-08-13
        // OOP                   -> 2026-08-12
        //
        // Final:
        //
        // 2026-08-12
        // 2026-08-12
        // 2026-08-13
        // ========================================================

        sb.append(
                "<div class='section-title'>"
                        + "&#128197; Day-wise Attendance"
                        + "</div>"
        );

        /*
         * Store every day-wise attendance record from
         * every subject in ONE list.
         */
        List<Map<String, String>> allDayWiseRecords =
                new ArrayList<>();

        for (Map<String, String> row : rows) {

            String subject =
                    row.getOrDefault(
                            "subject",
                            "Unknown Subject"
                    );

            String dayWise =
                    row.getOrDefault(
                            "dayWise",
                            ""
                    );

            if (dayWise.isBlank()) {
                continue;
            }

            /*
             * Example dayWise:
             *
             * 2026-08-12:PRESENT,
             * 2026-08-13:ABSENT
             */
            for (String entry : dayWise.split(",")) {

                String[] parts =
                        entry.split(":");

                if (parts.length < 2) {
                    continue;
                }

                String date =
                        parts[0].trim();

                String status =
                        parts[1].trim();

                Map<String, String> dayRecord =
                        new LinkedHashMap<>();

                dayRecord.put(
                        "date",
                        date
                );

                dayRecord.put(
                        "status",
                        status
                );

                dayRecord.put(
                        "subject",
                        subject
                );

                allDayWiseRecords.add(
                        dayRecord
                );
            }
        }

        /*
         * SORT ALL RECORDS BY DATE
         *
         * Because date format is yyyy-MM-dd,
         * normal String sorting works correctly.
         */
        allDayWiseRecords.sort(
                Comparator.comparing(
                        record ->
                                record.getOrDefault(
                                        "date",
                                        ""
                                )
                )
        );

        // ========================================================
        // ONE COMMON DAY-WISE TABLE
        // ========================================================

        sb.append(
                        "<table class='day-table'>"
                )

                .append("<tr>")
                .append("<th>Date</th>")
                .append("<th>Status</th>")
                .append("</tr>");

        for (Map<String, String> record
                : allDayWiseRecords) {

            String date =
                    record.getOrDefault(
                            "date",
                            ""
                    );

            String status =
                    record.getOrDefault(
                            "status",
                            ""
                    );

            String subject =
                    record.getOrDefault(
                            "subject",
                            ""
                    );

            String statusClass =
                    "PRESENT".equalsIgnoreCase(
                            status
                    )
                            ? "present-day"
                            : "absent-day";

            String icon =
                    "PRESENT".equalsIgnoreCase(
                            status
                    )
                            ? "&#9989;"
                            : "&#10060;";

            sb.append("<tr>")

                    .append("<td>")
                    .append(date)
                    .append("</td>")

                    .append("<td class='")
                    .append(statusClass)
                    .append("'>")

                    .append(icon)
                    .append(" ")
                    .append(status)

                    /*
                     * Subject name is displayed below
                     * PRESENT / ABSENT.
                     */
                    .append(
                            "<div class='subject-name'>"
                    )
                    .append(subject)
                    .append("</div>")

                    .append("</td>")

                    .append("</tr>");
        }

        sb.append("</table>");

        // ========================================================
        // FOOTER
        // ========================================================

        sb.append(
                        "<div class='footer'>"
                )

                .append(
                        "This is an automated report from "
                                + "the Attendance Management System."
                )

                .append("<br>")

                .append(
                        "Please do not reply to this email."
                )

                .append("</div>")

                .append("</div>")
                .append("</body>")
                .append("</html>");

        return sb.toString();
    }

    // ============================================================
    // OTP EMAIL BODY
    // ============================================================

    private String buildOTPEmailBody(String otp) {

        return "Dear User,\n\n"
                + "You have requested to login to the "
                + "Attendance Management System.\n\n"
                + "Your One-Time Password (OTP) is: "
                + otp + "\n\n"
                + "This OTP is valid for 10 minutes. "
                + "Please do not share this OTP with anyone.\n\n"
                + "If you did not request this OTP, "
                + "please ignore this email.\n\n"
                + "Best regards,\n"
                + "Attendance Management System";
    }
}