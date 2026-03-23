# FirebaseUserService Documentation

## Overview
The `FirebaseUserService` class provides Firebase-specific persistence operations for User entities. It handles all database operations for users.

## File Location
`src/main/java/com/attendance/service/FirebaseUserService.java`

## Libraries Used

### 1. `org.springframework.beans.factory.annotation.Autowired`
- **Purpose**: Dependency injection
- **Why**: Injects FirebaseService dependency

### 2. `org.springframework.stereotype.Service`
- **Purpose**: Marks class as Spring service
- **Why**: Enables component scanning

### 3. `java.util.UUID`
- **Purpose**: Generates unique IDs
- **Why**: Creates IDs for new users if not provided

### 4. `java.util.stream.Collectors`
- **Purpose**: Stream operations
- **Why**: Filters and transforms user collections

## Constants

### `BASE_PATH`
- **Value**: "AttendanceSystem/users"
- **Purpose**: Firebase path for user storage
- **Why**: Centralizes path definition

## Methods

### `save(User user)`
- **Purpose**: Saves user to Firebase
- **Parameters**: `User user` - User to save
- **Returns**: `User` - Saved user with ID set
- **Why**: Persists user data
- **Functionality**: Generates UUID if ID not set, saves to Firebase

### `findById(String id)`
- **Purpose**: Finds user by ID
- **Parameters**: `String id` - User ID
- **Returns**: `Optional<User>` - User if found
- **Why**: Lookup by primary key

### `findByUsername(String username)`
- **Purpose**: Finds user by username
- **Parameters**: `String username` - Username to search
- **Returns**: `Optional<User>` - User if found
- **Why**: Used for authentication
- **Functionality**: Gets all users, filters by username, sets ID from map key

### `findByEmail(String email)`
- **Purpose**: Finds user by email
- **Parameters**: `String email` - Email to search
- **Returns**: `Optional<User>` - User if found
- **Why**: Used for email-based lookups

### `findByRoleId(Integer roleId)`
- **Purpose**: Finds all users with specific role
- **Parameters**: `Integer roleId` - Role ID
- **Returns**: `List<User>` - List of users
- **Why**: Gets all teachers, students, or admins

### `findByRoleIdAndIsApproved(Integer roleId, Boolean isApproved)`
- **Purpose**: Finds users by role and approval status
- **Parameters**: 
  - `Integer roleId` - Role ID
  - `Boolean isApproved` - Approval status
- **Returns**: `List<User>` - List of users
- **Why**: Gets pending or approved students

### `countByRoleId(Integer roleId)`
- **Purpose**: Counts users by role
- **Parameters**: `Integer roleId` - Role ID
- **Returns**: `Long` - Count
- **Why**: Statistics

### `countByRoleIdAndIsApproved(Integer roleId, Boolean isApproved)`
- **Purpose**: Counts users by role and approval
- **Parameters**: 
  - `Integer roleId` - Role ID
  - `Boolean isApproved` - Approval status
- **Returns**: `Long` - Count
- **Why**: Statistics

### `existsByUsername(String username)`
- **Purpose**: Checks if username exists
- **Parameters**: `String username` - Username to check
- **Returns**: `boolean` - True if exists
- **Why**: Validation during registration

### `existsByEmail(String email)`
- **Purpose**: Checks if email exists
- **Parameters**: `String email` - Email to check
- **Returns**: `boolean` - True if exists
- **Why**: Validation during registration

### `findAll()`
- **Purpose**: Gets all users
- **Returns**: `List<User>` - List of all users
- **Why**: Complete user listing

### `delete(String id)`
- **Purpose**: Deletes user
- **Parameters**: `String id` - User ID
- **Why**: Removes user from system

## Related Files
- `UserService.java`: Business logic layer
- `User.java`: Entity class
- `FirebaseService.java`: Generic Firebase operations

