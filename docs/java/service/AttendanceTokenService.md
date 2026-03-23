# AttendanceTokenService Documentation

## Overview
The `AttendanceTokenService` class provides business logic for QR code token management. It generates tokens for teachers, validates tokens for students, and handles token expiration.

## File Location
`src/main/java/com/attendance/service/AttendanceTokenService.java`

## Libraries Used

### 1. `org.springframework.beans.factory.annotation.Autowired`
- **Purpose**: Dependency injection
- **Why**: Injects FirebaseAttendanceTokenService dependency

### 2. `org.springframework.stereotype.Service`
- **Purpose**: Marks class as Spring service
- **Why**: Enables component scanning and dependency injection

### 3. `java.time.Duration` and `java.time.LocalDateTime`
- **Purpose**: Time handling for token expiration
- **Why**: Manages token validity periods

### 4. `java.time.format.DateTimeFormatter`
- **Purpose**: Date formatting
- **Why**: Standardizes date format for token timestamps

### 5. `java.util.UUID`
- **Purpose**: Generates unique token strings
- **Why**: Creates cryptographically secure random tokens for QR codes

## Constants

### `DEFAULT_VALIDITY`
- **Type**: `Duration`
- **Value**: 2 minutes
- **Purpose**: Default expiration time for tokens
- **Why**: Tokens expire quickly for security

### `DATE_FORMATTER`
- **Type**: `DateTimeFormatter`
- **Format**: ISO_LOCAL_DATE_TIME
- **Purpose**: Standardizes date format
- **Why**: Ensures consistent date formatting

## Methods

### `generateToken(User teacher, Subject subject)`
- **Purpose**: Generates a new attendance token with default validity
- **Parameters**: 
  - `User teacher` - Teacher generating the token
  - `Subject subject` - Subject for which token is valid
- **Returns**: `AttendanceToken` - Generated token
- **Why**: Teachers generate QR codes for students to scan
- **Functionality**: Calls overloaded method with DEFAULT_VALIDITY

### `generateToken(User teacher, Subject subject, Duration validity)`
- **Purpose**: Generates a new attendance token with custom validity
- **Parameters**: 
  - `User teacher` - Teacher generating the token
  - `Subject subject` - Subject for which token is valid
  - `Duration validity` - How long token is valid
- **Returns**: `AttendanceToken` - Generated token
- **Why**: Allows custom expiration times
- **Functionality**:
  1. Deactivates expired tokens for the subject
  2. Creates new token with UUID
  3. Sets expiration time
  4. Marks as active
  5. Saves to Firebase

### `getActiveToken(String tokenValue)`
- **Purpose**: Retrieves and validates an active token
- **Parameters**: `String tokenValue` - Token string from QR code
- **Returns**: `Optional<AttendanceToken>` - Token if valid and active
- **Why**: Students scan QR codes and system validates the token
- **Functionality**:
  1. Finds token by value
  2. Checks if expired and deactivates if so
  3. Returns token only if active and not expired

### `getLatestActiveTokenForSubject(Subject subject)`
- **Purpose**: Gets the most recent active token for a subject
- **Parameters**: `Subject subject` - Subject to check
- **Returns**: `Optional<AttendanceToken>` - Latest token if exists
- **Why**: Teachers can see current QR code for their subject
- **Functionality**:
  1. Finds all active tokens for subject
  2. Gets the latest one (sorted by creation date)
  3. Checks if expired and deactivates if so
  4. Returns token only if active and not expired

### `deactivateExpiredTokens(Subject subject)`
- **Purpose**: Deactivates all expired tokens for a subject
- **Parameters**: `Subject subject` - Subject to check
- **Why**: Cleanup method to mark expired tokens as inactive
- **Functionality**:
  1. Finds all active tokens that expired before now
  2. Sets isActive to false
  3. Saves updated tokens

### `deactivateToken(AttendanceToken token)`
- **Purpose**: Manually deactivates a token
- **Parameters**: `AttendanceToken token` - Token to deactivate
- **Why**: Allows manual token revocation

### `deactivateIfExpired(AttendanceToken token)` (Private)
- **Purpose**: Helper method to deactivate token if expired
- **Parameters**: `AttendanceToken token` - Token to check
- **Why**: Internal method to ensure expired tokens are marked inactive
- **Functionality**: Checks expiration and deactivates if needed

## Related Files
- `FirebaseAttendanceTokenService.java`: Firebase persistence layer
- `AttendanceToken.java`: Entity class
- `TeacherController.java`: Teachers generate QR tokens
- `StudentController.java`: Students scan QR tokens

