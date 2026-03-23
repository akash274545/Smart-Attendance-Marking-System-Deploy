# Attendance Entity Documentation

## Overview
The `Attendance` class represents a single attendance record for a student in a subject on a specific date. It includes location tracking for verification.

## File Location
`src/main/java/com/attendance/entity/Attendance.java`

## Libraries Used

### 1. `jakarta.validation.constraints.*`
- **Purpose**: Provides validation annotations
- **Why**: Ensures data integrity
- **Annotations Used**:
  - `@NotNull`: Ensures attendance date is not null

## Class Structure

### Fields

#### `id` (String)
- **Purpose**: Unique identifier for the attendance record
- **Why**: Required for Firebase database operations

#### `studentId` (String)
- **Purpose**: ID of the student who marked attendance
- **Why**: Links attendance to student (stored as ID for Firebase compatibility)

#### `subjectId` (String)
- **Purpose**: ID of the subject for which attendance is marked
- **Why**: Links attendance to subject (stored as ID for Firebase compatibility)

#### `attendanceDate` (String)
- **Purpose**: Date and time when attendance was marked
- **Format**: ISO LocalDateTime string
- **Validation**: Cannot be null
- **Why**: Tracks when attendance was recorded

#### `latitude` (Double)
- **Purpose**: GPS latitude coordinate
- **Why**: Location verification to prevent remote attendance marking

#### `longitude` (Double)
- **Purpose**: GPS longitude coordinate
- **Why**: Location verification to prevent remote attendance marking

#### `locationAddress` (String)
- **Purpose**: Human-readable address of attendance location
- **Why**: Provides location context for verification

#### `status` (String)
- **Purpose**: Attendance status
- **Default**: "PRESENT"
- **Possible Values**: "PRESENT", "ABSENT", "LATE"
- **Why**: Tracks attendance status for reporting

#### `remarks` (String)
- **Purpose**: Additional notes about the attendance
- **Why**: Allows teachers to add context or notes

#### `createdAt` (String)
- **Purpose**: Timestamp of record creation
- **Format**: ISO LocalDateTime string
- **Why**: Audit trail

## Constructors

### `Attendance()`
- **Purpose**: Default constructor
- **Why**: Required for Firebase deserialization
- **Functionality**: Initializes `createdAt` with current timestamp

### `Attendance(String studentId, String subjectId, String attendanceDate, Double latitude, Double longitude, String locationAddress)`
- **Purpose**: Parameterized constructor
- **Why**: Convenient way to create attendance records
- **Functionality**: Sets all attendance properties

## Methods

### Getters and Setters
- **Purpose**: Standard accessor methods
- **Why**: Required for JavaBean compliance and Firebase serialization

## Usage Example
```java
Attendance attendance = new Attendance();
attendance.setStudentId(studentId);
attendance.setSubjectId(subjectId);
attendance.setAttendanceDate(LocalDateTime.now().toString());
attendance.setLatitude(40.7128);
attendance.setLongitude(-74.0060);
attendance.setLocationAddress("Classroom A");
attendance.setStatus("PRESENT");
```

## Related Files
- `AttendanceService.java`: Business logic for attendance operations
- `FirebaseAttendanceService.java`: Firebase persistence layer
- `StudentController.java`: Students mark attendance
- `TeacherController.java`: Teachers view attendance records

