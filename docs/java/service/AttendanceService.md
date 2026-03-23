# AttendanceService Documentation

## Overview
The `AttendanceService` class provides business logic for attendance management operations. It handles marking attendance, checking for duplicate attendance, and querying attendance records.

## File Location
`src/main/java/com/attendance/service/AttendanceService.java`

## Libraries Used

### 1. `org.springframework.beans.factory.annotation.Autowired`
- **Purpose**: Dependency injection
- **Why**: Injects FirebaseAttendanceService dependency

### 2. `org.springframework.stereotype.Service`
- **Purpose**: Marks class as Spring service
- **Why**: Enables component scanning and dependency injection

### 3. `java.time.LocalDateTime` and `java.time.format.DateTimeFormatter`
- **Purpose**: Date and time handling
- **Why**: Formats and parses attendance dates consistently

## Constants

### `DATE_FORMATTER`
- **Type**: `DateTimeFormatter`
- **Format**: ISO_LOCAL_DATE_TIME
- **Purpose**: Standardizes date format for attendance records
- **Why**: Ensures consistent date formatting across the system

## Methods

### `saveAttendance(Attendance attendance)`
- **Purpose**: Saves an attendance record
- **Parameters**: `Attendance attendance` - Attendance object to save
- **Returns**: `Attendance` - Saved attendance record
- **Why**: Persists attendance data to Firebase

### `findByStudent(User student)`
- **Purpose**: Finds all attendance records for a specific student
- **Parameters**: `User student` - Student user object
- **Returns**: `List<Attendance>` - List of student's attendance records
- **Why**: Student dashboard shows their attendance history

### `findBySubject(Subject subject)`
- **Purpose**: Finds all attendance records for a specific subject
- **Parameters**: `Subject subject` - Subject object
- **Returns**: `List<Attendance>` - List of attendance records for that subject
- **Why**: Teachers can view attendance for their subjects

### `findByStudentAndSubject(User student, Subject subject)`
- **Purpose**: Finds attendance records for a specific student in a specific subject
- **Parameters**: 
  - `User student` - Student user object
  - `Subject subject` - Subject object
- **Returns**: `List<Attendance>` - List of attendance records
- **Why**: Used to check attendance history for a student in a subject

### `findByStudentAndSubjectAndDate(User student, Subject subject, LocalDateTime date)`
- **Purpose**: Finds attendance records for a student in a subject on a specific date
- **Parameters**: 
  - `User student` - Student user object
  - `Subject subject` - Subject object
  - `LocalDateTime date` - Date to check
- **Returns**: `List<Attendance>` - List of attendance records for that date
- **Why**: Used to prevent duplicate attendance marking on the same day

### `countByStudentAndSubject(User student, Subject subject)`
- **Purpose**: Counts total attendance records for a student in a subject
- **Parameters**: 
  - `User student` - Student user object
  - `Subject subject` - Subject object
- **Returns**: `Long` - Count of attendance records
- **Why**: Statistics and reporting

### `countByStudentAndSubjectAndStatus(User student, Subject subject, String status)`
- **Purpose**: Counts attendance records with specific status
- **Parameters**: 
  - `User student` - Student user object
  - `Subject subject` - Subject object
  - `String status` - Status to count (PRESENT, ABSENT, LATE)
- **Returns**: `Long` - Count of records with that status
- **Why**: Statistics for attendance percentage calculations

### `markAttendance(User student, Subject subject, Double latitude, Double longitude, String locationAddress)`
- **Purpose**: Marks attendance for a student in a subject
- **Parameters**: 
  - `User student` - Student user object
  - `Subject subject` - Subject object
  - `Double latitude` - GPS latitude
  - `Double longitude` - GPS longitude
  - `String locationAddress` - Location address
- **Returns**: `Attendance` - Created attendance record
- **Throws**: `RuntimeException` if attendance already marked for today
- **Why**: Main method for students to mark attendance
- **Functionality**:
  1. Checks if attendance already marked for today
  2. Creates new Attendance object
  3. Sets current date/time
  4. Sets location data
  5. Sets status to "PRESENT"
  6. Saves to Firebase

### `hasMarkedAttendanceToday(User student, Subject subject)`
- **Purpose**: Checks if student has already marked attendance today
- **Parameters**: 
  - `User student` - Student user object
  - `Subject subject` - Subject object
- **Returns**: `boolean` - True if attendance already marked today
- **Why**: Prevents duplicate attendance marking
- **Functionality**: Uses `findByStudentAndSubjectAndDate` with current date

## Related Files
- `FirebaseAttendanceService.java`: Firebase persistence layer
- `Attendance.java`: Entity class
- `StudentController.java`: Students mark attendance
- `TeacherController.java`: Teachers view attendance records

