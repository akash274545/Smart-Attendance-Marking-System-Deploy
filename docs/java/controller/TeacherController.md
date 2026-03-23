# TeacherController Documentation

## Overview
The `TeacherController` class handles all teacher-specific operations including managing subjects, approving students, generating QR codes, and viewing attendance.

## File Location
`src/main/java/com/attendance/controller/TeacherController.java`

## Libraries Used

### 1. `org.springframework.stereotype.Controller`
- **Purpose**: Marks class as Spring MVC controller
- **Why**: Handles HTTP requests

### 2. `org.springframework.web.bind.annotation.RequestMapping`
- **Purpose**: Base path mapping
- **Value**: `/teacher`
- **Why**: All teacher routes are under /teacher

### 3. `org.springframework.web.servlet.support.ServletUriComponentsBuilder`
- **Purpose**: Builds URLs dynamically
- **Why**: Creates QR code URLs with tokens

## Methods

### `dashboard(Authentication authentication, Model model)`
- **Mapping**: `GET /teacher/dashboard`
- **Purpose**: Displays teacher dashboard
- **Returns**: `teacher/dashboard` template
- **Why**: Main teacher interface
- **Functionality**:
  1. Verifies user is teacher
  2. Gets teacher's subjects
  3. Gets pending and approved students
- **Model Attributes**:
  - `teacher`: Current teacher
  - `subjects`: Teacher's subjects
  - `pendingStudents`: Students awaiting approval
  - `approvedStudents`: Approved students

### `approveStudent(...)`
- **Mapping**: `POST /teacher/approve-student/{studentId}`
- **Purpose**: Approves a pending student
- **Parameters**: `studentId` (path variable)
- **Returns**: Redirect to dashboard with success/error
- **Why**: Teachers approve student registrations
- **Functionality**: Sets student's isApproved to true

### `addSubjectForm(...)`
- **Mapping**: `GET /teacher/add-subject`
- **Purpose**: Displays add subject form
- **Returns**: `teacher/add-subject` template
- **Why**: Form to create new subjects
- **Model Attributes**:
  - `teacher`: Current teacher
  - `classes`: All available classes
  - `defaultSubjects`: Template subjects

### `addSubject(...)`
- **Mapping**: `POST /teacher/add-subject`
- **Purpose**: Creates new subject
- **Parameters**: subjectName, subjectCode, description, classId
- **Returns**: Redirect to dashboard
- **Why**: Teachers create subjects they teach
- **Functionality**:
  1. Validates subject code uniqueness
  2. Creates subject with teacherId
  3. Links to class
  4. Saves subject

### `viewAttendance(...)`
- **Mapping**: `GET /teacher/view-attendance/{subjectId}`
- **Purpose**: Displays attendance for a subject
- **Parameters**: `subjectId` (path variable)
- **Returns**: `teacher/view-attendance` template
- **Why**: Teachers view attendance records for their subjects
- **Functionality**:
  1. Verifies subject belongs to teacher
  2. Gets all attendance records for subject
  3. Enriches with student information
  4. Gets active QR token if exists
- **Model Attributes**:
  - `subject`: Subject information
  - `attendanceList`: All attendance records
  - `studentMap`: Student information
  - `activeToken`: Current QR token (if exists)

### `generateQrCode(...)`
- **Mapping**: `POST /teacher/subjects/{subjectId}/generate-qr`
- **Purpose**: Generates QR code token for attendance
- **Parameters**: `subjectId` (path variable)
- **Returns**: Redirect to QR page
- **Why**: Teachers generate QR codes for students to scan
- **Functionality**:
  1. Verifies subject belongs to teacher
  2. Generates new token
  3. Creates QR URL with token
  4. Stores in flash attributes
  5. Redirects to QR display page

### `viewQrPage(...)`
- **Mapping**: `GET /teacher/subjects/{subjectId}/qr`
- **Purpose**: Displays QR code page
- **Parameters**: `subjectId` (path variable)
- **Returns**: `teacher/subject-qr` template
- **Why**: Shows QR code for students to scan
- **Functionality**:
  1. Verifies subject belongs to teacher
  2. Gets latest active token
  3. Builds QR URL
  4. Displays QR code

## Related Files
- `SubjectService.java`: Subject operations
- `AttendanceTokenService.java`: QR token generation
- `AttendanceService.java`: Attendance operations
- `UserService.java`: Student approval
- `teacher/dashboard.html`: Teacher dashboard template
- `teacher/subject-qr.html`: QR code display page

