# ClassService Documentation

## Overview
The `ClassService` class provides business logic for class management operations. It acts as a service layer between controllers and the Firebase persistence layer.

## File Location
`src/main/java/com/attendance/service/ClassService.java`

## Libraries Used

### 1. `org.springframework.beans.factory.annotation.Autowired`
- **Purpose**: Dependency injection
- **Why**: Injects FirebaseClassService dependency

### 2. `org.springframework.stereotype.Service`
- **Purpose**: Marks class as Spring service
- **Why**: Enables component scanning and dependency injection

## Methods

### `saveClass(ClassEntity classEntity)`
- **Purpose**: Saves a new class or updates existing class
- **Parameters**: `ClassEntity classEntity` - Class to save
- **Returns**: `ClassEntity` - Saved class
- **Why**: Persists class data to Firebase

### `findAllClasses()`
- **Purpose**: Retrieves all classes
- **Returns**: `List<ClassEntity>` - List of all classes
- **Why**: Used in dropdowns and listings

### `findByClassCode(String classCode)`
- **Purpose**: Finds class by its code
- **Parameters**: `String classCode` - Class code (e.g., "FY", "SY")
- **Returns**: `Optional<ClassEntity>` - Class if found
- **Why**: Used for validation and lookups

### `countAllClasses()`
- **Purpose**: Counts total number of classes
- **Returns**: `Long` - Count of classes
- **Why**: Statistics and reporting

### `existsByClassCode(String classCode)`
- **Purpose**: Checks if class code already exists
- **Parameters**: `String classCode` - Code to check
- **Returns**: `boolean` - True if exists
- **Why**: Prevents duplicate class codes

### `createClass(String className, String classCode, String description)`
- **Purpose**: Creates a new class with provided details
- **Parameters**:
  - `String className` - Full class name
  - `String classCode` - Short code
  - `String description` - Description
- **Returns**: `ClassEntity` - Created class
- **Why**: Convenience method for creating classes

### `findById(String id)`
- **Purpose**: Finds class by ID
- **Parameters**: `String id` - Class ID
- **Returns**: `Optional<ClassEntity>` - Class if found
- **Why**: Used for lookups by ID

### `deleteClass(String id)`
- **Purpose**: Deletes a class
- **Parameters**: `String id` - Class ID to delete
- **Why**: Admin can remove classes

## Related Files
- `FirebaseClassService.java`: Firebase persistence layer
- `ClassEntity.java`: Entity class
- `AdminController.java`: Uses ClassService for class management
- `DataInitializer.java`: Creates default classes on startup

