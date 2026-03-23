# User Entity Documentation

## Overview
The `User` class represents a user entity in the Attendance Management System. It can be an Admin, Teacher, or Student.

## File Location
`src/main/java/com/attendance/entity/User.java`

## Libraries Used

### 1. `com.fasterxml.jackson.annotation.JsonIgnoreProperties`
- **Purpose**: Used to ignore unknown properties during JSON deserialization
- **Why**: Prevents errors when Firebase returns additional fields not defined in the class

### 2. `jakarta.validation.constraints.*`
- **Purpose**: Provides validation annotations for data integrity
- **Why**: Ensures data quality at the entity level before persistence
- **Annotations Used**:
  - `@NotBlank`: Ensures field is not null or empty
  - `@Size`: Validates string length constraints
  - `@Email`: Validates email format

## Class Structure

### Fields

#### `id` (String)
- **Purpose**: Unique identifier for the user
- **Why**: Required for Firebase database operations and entity relationships

#### `username` (String)
- **Purpose**: Unique username for login
- **Validation**: 3-50 characters, letters only
- **Why**: Used for authentication and user identification

#### `password` (String)
- **Purpose**: Encrypted password for authentication
- **Validation**: Minimum 6 characters
- **Why**: Stores BCrypt-encoded password for secure authentication

#### `email` (String)
- **Purpose**: User's email address
- **Validation**: Must be valid email format
- **Why**: Used for communication and OTP verification

#### `fullName` (String)
- **Purpose**: User's full name
- **Validation**: Cannot be blank
- **Why**: Display name for UI and reports

#### `roleId` (Integer)
- **Purpose**: Defines user role
- **Values**: 
  - `0` = Admin
  - `1` = Teacher
  - `2` = Student
- **Why**: Determines access permissions and available features

#### `isApproved` (Boolean)
- **Purpose**: Approval status for students
- **Default**: `false`
- **Why**: Students require teacher approval before accessing the system

#### `createdAt` (String)
- **Purpose**: Timestamp of user creation
- **Format**: ISO LocalDateTime string
- **Why**: Audit trail and registration tracking

## Constructors

### `User()`
- **Purpose**: Default constructor
- **Why**: Required for Firebase deserialization and object instantiation
- **Functionality**: Initializes `createdAt` with current timestamp

### `User(String username, String password, String email, String fullName, Integer roleId)`
- **Purpose**: Parameterized constructor
- **Why**: Convenient way to create user objects with initial values
- **Functionality**: Sets all basic user properties

## Methods

### Getters and Setters
- **Purpose**: Standard accessor methods for all fields
- **Why**: Required for JavaBean compliance and Firebase serialization

### Helper Methods

#### `isAdmin()`
- **Purpose**: Checks if user is an admin
- **Returns**: `boolean`
- **Why**: Simplifies role checking in business logic

#### `isTeacher()`
- **Purpose**: Checks if user is a teacher
- **Returns**: `boolean`
- **Why**: Simplifies role checking in business logic

#### `isStudent()`
- **Purpose**: Checks if user is a student
- **Returns**: `boolean`
- **Why**: Simplifies role checking in business logic

## Usage Example
```java
User student = new User();
student.setUsername("john");
student.setEmail("john@example.com");
student.setFullName("John Doe");
student.setRoleId(2); // Student
student.setIsApproved(false);
```

## Related Files
- `UserService.java`: Business logic for user operations
- `FirebaseUserService.java`: Firebase persistence layer
- `SecurityConfig.java`: Uses User for authentication

