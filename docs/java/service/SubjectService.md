# SubjectService Documentation

## Overview
The `SubjectService` class provides business logic for subject management operations. It handles both regular subjects and default subjects (template subjects).

## File Location
`src/main/java/com/attendance/service/SubjectService.java`

## Libraries Used

### 1. `org.springframework.beans.factory.annotation.Autowired`
- **Purpose**: Dependency injection
- **Why**: Injects FirebaseSubjectService dependency

### 2. `org.springframework.stereotype.Service`
- **Purpose**: Marks class as Spring service
- **Why**: Enables component scanning and dependency injection

## Methods

### `saveSubject(Subject subject)`
- **Purpose**: Saves a new subject or updates existing subject
- **Parameters**: `Subject subject` - Subject to save
- **Returns**: `Subject` - Saved subject
- **Why**: Persists subject data to Firebase

### `findAllSubjects()`
- **Purpose**: Retrieves all subjects
- **Returns**: `List<Subject>` - List of all subjects
- **Why**: Used in listings and student dashboards

### `findByTeacher(User teacher)`
- **Purpose**: Finds all subjects taught by a specific teacher
- **Parameters**: `User teacher` - Teacher user object
- **Returns**: `List<Subject>` - List of teacher's subjects
- **Why**: Teacher dashboard shows only their subjects

### `findById(String id)`
- **Purpose**: Finds subject by ID
- **Parameters**: `String id` - Subject ID
- **Returns**: `Optional<Subject>` - Subject if found
- **Why**: Used for lookups by ID

### `findBySubjectCode(String subjectCode)`
- **Purpose**: Finds subject by its code
- **Parameters**: `String subjectCode` - Subject code (e.g., "MATH101")
- **Returns**: `Optional<Subject>` - Subject if found
- **Why**: Used for validation and student attendance marking

### `countAllSubjects()`
- **Purpose**: Counts total number of subjects
- **Returns**: `Long` - Count of subjects
- **Why**: Statistics and reporting

### `countByTeacher(User teacher)`
- **Purpose**: Counts subjects for a specific teacher
- **Parameters**: `User teacher` - Teacher user object
- **Returns**: `Long` - Count of teacher's subjects
- **Why**: Statistics for teacher dashboard

### `existsBySubjectCode(String subjectCode)`
- **Purpose**: Checks if subject code already exists
- **Parameters**: `String subjectCode` - Code to check
- **Returns**: `boolean` - True if exists
- **Why**: Prevents duplicate subject codes

### `createSubject(String subjectName, String subjectCode, String description, User teacher)`
- **Purpose**: Creates a new subject with provided details
- **Parameters**:
  - `String subjectName` - Full subject name
  - `String subjectCode` - Short code
  - `String description` - Description
  - `User teacher` - Teacher who teaches this subject
- **Returns**: `Subject` - Created subject
- **Why**: Convenience method for creating subjects

### `deleteSubject(String id)`
- **Purpose**: Deletes a subject
- **Parameters**: `String id` - Subject ID to delete
- **Why**: Admin can remove subjects

### `saveDefaultSubject(Subject subject)`
- **Purpose**: Saves a default/template subject
- **Parameters**: `Subject subject` - Default subject to save
- **Returns**: `Subject` - Saved default subject
- **Why**: Stores template subjects that teachers can reference when creating their own subjects

### `findAllDefaultSubjects()`
- **Purpose**: Retrieves all default subjects
- **Returns**: `List<Subject>` - List of default subjects
- **Why**: Teachers can see available default subjects when creating new subjects

### `existsDefaultSubjectByCode(String subjectCode)`
- **Purpose**: Checks if default subject code exists
- **Parameters**: `String subjectCode` - Code to check
- **Returns**: `boolean` - True if exists
- **Why**: Prevents duplicate default subject codes

## Related Files
- `FirebaseSubjectService.java`: Firebase persistence layer
- `Subject.java`: Entity class
- `TeacherController.java`: Teachers create and manage subjects
- `StudentController.java`: Students view subjects and mark attendance
- `DataInitializer.java`: Creates default subjects on startup

