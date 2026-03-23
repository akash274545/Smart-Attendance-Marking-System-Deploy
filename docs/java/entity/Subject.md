# Subject Entity Documentation

## Overview
The `Subject` class represents an academic subject in the Attendance Management System. Each subject is associated with a teacher and optionally a class.

## File Location
`src/main/java/com/attendance/entity/Subject.java`

## Libraries Used

### 1. `jakarta.validation.constraints.*`
- **Purpose**: Provides validation annotations
- **Why**: Ensures data integrity
- **Annotations Used**:
  - `@NotBlank`: Ensures required fields are not empty
  - `@Size`: Validates string length constraints

## Class Structure

### Fields

#### `id` (String)
- **Purpose**: Unique identifier for the subject
- **Why**: Required for Firebase database operations

#### `subjectName` (String)
- **Purpose**: Full name of the subject (e.g., "Mathematics", "Physics")
- **Validation**: 2-100 characters, cannot be blank
- **Why**: Human-readable subject identifier

#### `subjectCode` (String)
- **Purpose**: Short code for the subject (e.g., "MATH101", "PHY101")
- **Validation**: 3-10 characters, cannot be blank
- **Why**: Used for quick reference, filtering, and attendance marking

#### `description` (String)
- **Purpose**: Detailed description of the subject
- **Why**: Provides additional context

#### `teacherId` (String)
- **Purpose**: ID of the teacher who teaches this subject
- **Why**: Links subject to teacher (stored as ID for Firebase compatibility)

#### `classId` (String)
- **Purpose**: ID of the class this subject belongs to
- **Why**: Links subject to class (optional, can be null for default subjects)

#### `createdAt` (String)
- **Purpose**: Timestamp of subject creation
- **Format**: ISO LocalDateTime string
- **Why**: Audit trail

## Constructors

### `Subject()`
- **Purpose**: Default constructor
- **Why**: Required for Firebase deserialization
- **Functionality**: Initializes `createdAt` with current timestamp

### `Subject(String subjectName, String subjectCode, String description, String teacherId)`
- **Purpose**: Parameterized constructor
- **Why**: Convenient way to create subject entities
- **Functionality**: Sets all subject properties

## Methods

### Getters and Setters
- **Purpose**: Standard accessor methods
- **Why**: Required for JavaBean compliance and Firebase serialization

## Usage Example
```java
Subject math = new Subject();
math.setSubjectName("Mathematics");
math.setSubjectCode("MATH101");
math.setDescription("Introduction to Mathematics");
math.setTeacherId(teacherId);
math.setClassId(classId);
```

## Related Files
- `SubjectService.java`: Business logic for subject operations
- `FirebaseSubjectService.java`: Firebase persistence layer
- `TeacherController.java`: Teachers create and manage subjects
- `StudentController.java`: Students view and mark attendance for subjects

