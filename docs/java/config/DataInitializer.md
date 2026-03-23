# DataInitializer Documentation

## Overview
The `DataInitializer` class initializes default data when the application starts. It creates a default admin user, default classes, and default subjects.

## File Location
`src/main/java/com/attendance/config/DataInitializer.java`

## Libraries Used

### 1. `org.springframework.boot.CommandLineRunner`
- **Purpose**: Interface for code to run on startup
- **Why**: Executes initialization after Spring context loads

### 2. `org.springframework.stereotype.Component`
- **Purpose**: Marks class as Spring component
- **Why**: Enables automatic component scanning

### 3. `org.springframework.beans.factory.annotation.Autowired`
- **Purpose**: Dependency injection
- **Why**: Injects service dependencies

### 4. `java.util.Arrays`
- **Purpose**: Array utilities
- **Why**: Creates lists of default entities

## Methods

### `run(String... args)`
- **Interface**: `CommandLineRunner`
- **Purpose**: Main initialization method
- **Why**: Runs automatically on application startup
- **Functionality**:
  1. Creates default admin user
  2. Initializes default classes
  3. Initializes default subjects
  4. Handles errors gracefully with logging

### `initializeDefaultClasses()`
- **Purpose**: Creates default class entities
- **Why**: Provides common classes for the system
- **Default Classes**:
  - First Year (FY)
  - Second Year (SY)
  - Third Year (TY)
  - Fourth Year (FOURTH)
  - Master First Year (MFY)
  - Master Second Year (MSY)
  - PhD (PHD)
  - Diploma (DIP)
  - Certificate (CERT)
  - Foundation (FOUND)
- **Functionality**: Only creates if class code doesn't exist

### `initializeDefaultSubjects()`
- **Purpose**: Creates default subject templates
- **Why**: Provides common subjects teachers can reference
- **Default Subjects**:
  - Mathematics (MATH101)
  - Physics (PHY101)
  - Chemistry (CHEM101)
  - Computer Science (CS101)
  - English (ENG101)
  - Biology (BIO101)
  - History (HIST101)
  - Geography (GEO101)
  - Economics (ECO101)
  - Psychology (PSY101)
  - Statistics (STAT101)
  - Programming (PROG101)
  - Database Management (DBMS101)
  - Data Structures (DS101)
  - Software Engineering (SE101)
- **Functionality**: Only creates if subject code doesn't exist

## Default Admin Credentials
- **Username**: `admin`
- **Password**: `Admin@123`
- **Email**: `admin@attendance.com`
- **Role**: Admin (roleId = 0)
- **Note**: Only created if admin doesn't exist

## Error Handling
- Catches and logs errors for each initialization step
- Continues execution even if one step fails
- Prints helpful error messages

## Related Files
- `UserService.java`: Creates admin user
- `ClassService.java`: Creates default classes
- `SubjectService.java`: Creates default subjects

