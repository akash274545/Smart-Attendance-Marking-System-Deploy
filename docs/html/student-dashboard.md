# student/dashboard.html Documentation

## Overview
The `student/dashboard.html` file is the main student interface. It displays available subjects, attendance statistics, and recent attendance records.

## File Location
`src/main/resources/templates/student/dashboard.html`

## Libraries Used
Same as login.html (Bootstrap, Font Awesome, Thymeleaf)

## Key Features

### Statistics Cards
- **Available Subjects**: Count of all subjects
- **Total Attendance**: Count of student's attendance records
- **Today's Date**: Current date display

### Quick Actions
- **Scan QR**: Link to QR scanning page
- **View Attendance**: Link to attendance history
- **Refresh**: Reloads dashboard

### Available Subjects Table
- Lists all subjects in the system
- Shows subject name, code, teacher, and description
- Uses teacherMap to display teacher names

### Recent Attendance Table
- Shows student's attendance history
- Displays subject, date, time, status, and location
- Uses subjectMap to display subject names
- Shows "No attendance records" if empty

## Related Files
- `StudentController.java`: Provides dashboard data
- `student/mark-attendance-qr.html`: QR scanning page
- `student/view-attendance.html`: Attendance history page

