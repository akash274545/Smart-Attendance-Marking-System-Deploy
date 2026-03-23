# UserService Documentation

## Overview
The `UserService` class provides business logic for user management operations. It acts as a service layer between controllers and the Firebase persistence layer, handling password encoding and user operations.

## File Location
`src/main/java/com/attendance/service/UserService.java`

## Libraries Used

### 1. `org.springframework.beans.factory.annotation.Autowired`
- **Purpose**: Dependency injection annotation
- **Why**: Automatically injects required dependencies (FirebaseUserService, PasswordEncoder)

### 2. `org.springframework.security.crypto.password.PasswordEncoder`
- **Purpose**: Password encoding/encryption
- **Why**: Securely hashes passwords using BCrypt before storage

### 3. `org.springframework.stereotype.Service`
- **Purpose**: Marks class as a Spring service component
- **Why**: Enables automatic component scanning and dependency injection

## Methods

### `saveUser(User user)`
- **Purpose**: Saves a new user or updates existing user
- **Parameters**: `User user` - User object to save
- **Returns**: `User` - Saved user with encoded password
- **Why**: Encodes password before saving for security
- **Functionality**:
  1. Encodes password using BCrypt
  2. Delegates to FirebaseUserService for persistence
  3. Returns saved user

### `findByUsername(String username)`
- **Purpose**: Finds user by username
- **Parameters**: `String username` - Username to search
- **Returns**: `Optional<User>` - User if found, empty otherwise
- **Why**: Used for authentication and user lookup
- **Functionality**: Delegates to FirebaseUserService

### `findByEmail(String email)`
- **Purpose**: Finds user by email address
- **Parameters**: `String email` - Email to search
- **Returns**: `Optional<User>` - User if found, empty otherwise
- **Why**: Used for email-based lookups and OTP verification

### `findByRoleId(Integer roleId)`
- **Purpose**: Finds all users with specific role
- **Parameters**: `Integer roleId` - Role ID (0=Admin, 1=Teacher, 2=Student)
- **Returns**: `List<User>` - List of users with that role
- **Why**: Used to get all teachers, students, or admins

### `findPendingStudents()`
- **Purpose**: Finds all students awaiting approval
- **Returns**: `List<User>` - List of pending students
- **Why**: Teachers need to see pending students for approval

### `findApprovedStudents()`
- **Purpose**: Finds all approved students
- **Returns**: `List<User>` - List of approved students
- **Why**: Used to display active students in dashboards

### `findAllTeachers()`
- **Purpose**: Finds all teachers
- **Returns**: `List<User>` - List of all teachers
- **Why**: Admin dashboard displays all teachers

### `countTeachers()`
- **Purpose**: Counts total number of teachers
- **Returns**: `Long` - Count of teachers
- **Why**: Statistics and reporting

### `countStudents()`
- **Purpose**: Counts total number of students
- **Returns**: `Long` - Count of students
- **Why**: Statistics and reporting

### `countApprovedStudents()`
- **Purpose**: Counts approved students only
- **Returns**: `Long` - Count of approved students
- **Why**: Statistics and reporting

### `approveStudent(String studentId)`
- **Purpose**: Approves a pending student
- **Parameters**: `String studentId` - ID of student to approve
- **Returns**: `User` - Updated user with isApproved=true
- **Throws**: `RuntimeException` if student not found
- **Why**: Teachers approve student registrations

### `existsByUsername(String username)`
- **Purpose**: Checks if username already exists
- **Parameters**: `String username` - Username to check
- **Returns**: `boolean` - True if exists
- **Why**: Prevents duplicate usernames during registration

### `existsByEmail(String email)`
- **Purpose**: Checks if email already exists
- **Parameters**: `String email` - Email to check
- **Returns**: `boolean` - True if exists
- **Why**: Prevents duplicate emails during registration

### `createDefaultAdmin()`
- **Purpose**: Creates default admin user on system startup
- **Returns**: `User` - Created or existing admin user
- **Why**: Ensures system always has an admin account
- **Default Credentials**:
  - Username: "admin"
  - Password: "Admin@123"
  - Email: "admin@attendance.com"

### `findById(String id)`
- **Purpose**: Finds user by ID
- **Parameters**: `String id` - User ID
- **Returns**: `Optional<User>` - User if found
- **Why**: Used for user lookups by ID

### `deleteUser(String id)`
- **Purpose**: Deletes a user
- **Parameters**: `String id` - User ID to delete
- **Why**: Admin can remove users from system

### `updateUser(User user)`
- **Purpose**: Updates existing user
- **Parameters**: `User user` - User with updated fields
- **Returns**: `User` - Updated user
- **Why**: Allows editing user information

### `changePassword(String userId, String oldPassword, String newPassword)`
- **Purpose**: Changes user password
- **Parameters**: 
  - `String userId` - User ID
  - `String oldPassword` - Current password for verification
  - `String newPassword` - New password to set
- **Returns**: `boolean` - True if successful, false if old password incorrect
- **Why**: Users can change their passwords securely
- **Functionality**:
  1. Verifies old password matches
  2. Encodes new password
  3. Saves updated user

## Related Files
- `FirebaseUserService.java`: Firebase persistence layer
- `User.java`: Entity class
- `SecurityConfig.java`: Uses UserService for authentication
- `HomeController.java`: Uses UserService for registration and login

