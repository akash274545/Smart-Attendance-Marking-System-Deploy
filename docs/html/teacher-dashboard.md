# teacher/dashboard.html Documentation

## Overview
The `teacher/dashboard.html` file is the main teacher interface. It displays teacher's subjects, pending student approvals, and provides subject management.

## File Location
`src/main/resources/templates/teacher/dashboard.html`

## Libraries Used
Same as login.html (Bootstrap, Font Awesome, Thymeleaf)

## Key Features

### Statistics Cards
- **My Subjects**: Count of teacher's subjects
- **Pending Students**: Count of students awaiting approval
- **Approved Students**: Count of approved students

### My Subjects Table
- Lists all subjects taught by the teacher
- Shows subject name, code, and description
- **Actions**:
  - **View Attendance**: Link to attendance records for subject
  - **Generate QR**: Button to generate QR code for attendance

### Pending Student Approvals Table
- Lists students awaiting approval
- Shows name, username, email, registration date
- **Approve Button**: Form to approve student
- Shows "No pending approvals" if empty

## Related Files
- `TeacherController.java`: Provides dashboard data
- `teacher/view-attendance.html`: Attendance viewing page
- `teacher/subject-qr.html`: QR code display page

