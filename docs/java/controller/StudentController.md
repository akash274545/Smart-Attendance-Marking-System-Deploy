# StudentController Documentation

## Overview
The `StudentController` class handles all student-specific operations including viewing dashboard, marking attendance (manual and QR), and viewing attendance history.

## File Location
`src/main/java/com/attendance/controller/StudentController.java`

## Libraries Used

### 1. `org.springframework.stereotype.Controller`
- **Purpose**: Marks class as Spring MVC controller
- **Why**: Handles HTTP requests

### 2. `org.springframework.web.bind.annotation.RequestMapping`
- **Purpose**: Base path mapping
- **Value**: `/student`
- **Why**: All student routes are under /student

### 3. `org.springframework.security.core.Authentication`
- **Purpose**: Gets current user
- **Why**: Identifies logged-in student

## Methods

### `dashboard(Authentication authentication, Model model)`
- **Mapping**: `GET /student/dashboard`
- **Purpose**: Displays student dashboard
- **Returns**: `student/dashboard` or `student/pending` template
- **Why**: Main student interface
- **Functionality**:
  1. Checks if student is approved
  2. Gets all subjects with teacher info
  3. Gets student's attendance records
  4. Enriches data with subject/teacher maps
- **Model Attributes**:
  - `student`: Current student
  - `subjects`: All available subjects
  - `teacherMap`: Map of teacher IDs to User objects
  - `attendanceList`: Student's attendance records
  - `subjectMap`: Map of subject IDs to Subject objects

### `markAttendanceForm(Model model)`
- **Mapping**: `GET /student/mark-attendance`
- **Purpose**: Displays manual attendance marking form
- **Returns**: `student/mark-attendance` template
- **Why**: Form to manually enter subject code

### `markAttendance(...)`
- **Mapping**: `POST /student/mark-attendance`
- **Purpose**: Marks attendance manually
- **Parameters**: 
  - `subjectCode`: Subject code
  - `latitude` (optional): GPS latitude
  - `longitude` (optional): GPS longitude
  - `locationAddress` (optional): Location address
- **Returns**: Redirect to dashboard or form with error
- **Why**: Students mark attendance by entering subject code
- **Functionality**:
  1. Validates student is approved
  2. Finds subject by code
  3. Checks if already marked today
  4. Creates attendance record
  5. Redirects with success

### `markAttendanceQrForm(...)`
- **Mapping**: `GET /student/mark-attendance-qr`
- **Purpose**: Displays QR code scanning page
- **Parameters**: `token` (optional): Pre-filled token from QR
- **Returns**: `student/mark-attendance-qr` template
- **Why**: Page for scanning QR codes

### `markAttendanceViaQr(...)`
- **Mapping**: `POST /student/mark-attendance-qr`
- **Purpose**: Marks attendance using QR token
- **Parameters**: 
  - `tokenValue`: Token from scanned QR code
  - `latitude` (optional): GPS coordinates
  - `longitude` (optional): GPS coordinates
  - `locationAddress` (optional): Location
- **Returns**: `student/mark-attendance-qr` template with success/error
- **Why**: Students scan QR code to mark attendance
- **Functionality**:
  1. Validates student is approved
  2. Validates token is active and not expired
  3. Finds subject from token
  4. Checks if already marked today
  5. Creates attendance record
  6. Shows success message

### `viewAttendance(Authentication authentication, Model model)`
- **Mapping**: `GET /student/view-attendance`
- **Purpose**: Displays student's attendance history
- **Returns**: `student/view-attendance` template
- **Why**: Students view their attendance records
- **Functionality**:
  1. Gets all attendance records for student
  2. Enriches with subject and teacher information
  3. Displays in table format
- **Model Attributes**:
  - `student`: Current student
  - `attendanceList`: All attendance records
  - `subjectMap`: Subject information
  - `teacherMap`: Teacher information

## Related Files
- `AttendanceService.java`: Attendance operations
- `AttendanceTokenService.java`: QR token validation
- `SubjectService.java`: Subject lookups
- `UserService.java`: User operations
- `student/dashboard.html`: Student dashboard template
- `student/mark-attendance-qr.html`: QR scanning page

