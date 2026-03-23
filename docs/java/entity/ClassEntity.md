# ClassEntity Documentation

## Overview
The `ClassEntity` class represents an academic class or grade level in the Attendance Management System (e.g., First Year, Second Year, etc.).

## File Location
`src/main/java/com/attendance/entity/ClassEntity.java`

## Libraries Used

### 1. `jakarta.validation.constraints.*`
- **Purpose**: Provides validation annotations
- **Why**: Ensures data integrity before persistence
- **Annotations Used**:
  - `@NotBlank`: Ensures class name is not empty
  - `@Size`: Validates class name length (2-50 characters)

## Class Structure

### Fields

#### `id` (String)
- **Purpose**: Unique identifier for the class
- **Why**: Required for Firebase database operations

#### `className` (String)
- **Purpose**: Full name of the class (e.g., "First Year", "Second Year")
- **Validation**: 2-50 characters, cannot be blank
- **Why**: Human-readable class identifier

#### `classCode` (String)
- **Purpose**: Short code for the class (e.g., "FY", "SY")
- **Why**: Used for quick reference and filtering

#### `description` (String)
- **Purpose**: Detailed description of the class
- **Why**: Provides additional context about the class

#### `createdAt` (String)
- **Purpose**: Timestamp of class creation
- **Format**: ISO LocalDateTime string
- **Why**: Audit trail

## Constructors

### `ClassEntity()`
- **Purpose**: Default constructor
- **Why**: Required for Firebase deserialization
- **Functionality**: Initializes `createdAt` with current timestamp

### `ClassEntity(String className, String classCode, String description)`
- **Purpose**: Parameterized constructor
- **Why**: Convenient way to create class entities
- **Functionality**: Sets all class properties

## Methods

### Getters and Setters
- **Purpose**: Standard accessor methods
- **Why**: Required for JavaBean compliance and Firebase serialization

## Usage Example
```java
ClassEntity firstYear = new ClassEntity();
firstYear.setClassName("First Year");
firstYear.setClassCode("FY");
firstYear.setDescription("First Year Undergraduate Class");
```

## Related Files
- `ClassService.java`: Business logic for class operations
- `FirebaseClassService.java`: Firebase persistence layer
- `DataInitializer.java`: Creates default classes on startup

