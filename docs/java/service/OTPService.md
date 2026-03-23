# OTPService Documentation

## Overview
The `OTPService` class manages One-Time Password (OTP) generation and validation for student login verification. It stores OTPs temporarily in memory with expiration times.

## File Location
`src/main/java/com/attendance/service/OTPService.java`

## Libraries Used

### 1. `org.springframework.beans.factory.annotation.Autowired`
- **Purpose**: Dependency injection
- **Why**: Injects EmailService dependency

### 2. `org.springframework.stereotype.Service`
- **Purpose**: Marks class as Spring service
- **Why**: Enables component scanning and dependency injection

### 3. `java.util.Map` and `java.util.concurrent.ConcurrentHashMap`
- **Purpose**: Thread-safe map for storing OTPs
- **Why**: Stores OTPs temporarily (in production, should use Redis or database)
- **Note**: `ConcurrentHashMap` ensures thread safety

### 4. `java.util.Random`
- **Purpose**: Generates random numbers for OTP
- **Why**: Creates secure random OTP codes

## Constants

### `OTP_LENGTH`
- **Type**: `int`
- **Value**: 6
- **Purpose**: Length of OTP code
- **Why**: Standard 6-digit OTP for security and usability

### `OTP_VALIDITY_MS`
- **Type**: `long`
- **Value**: 10 minutes (600,000 milliseconds)
- **Purpose**: How long OTP remains valid
- **Why**: Security measure - OTPs expire after 10 minutes

## Inner Class: OTPData

### Purpose
Stores OTP information including code, expiry time, and username.

### Fields
- `otp` (String): The OTP code
- `expiryTime` (long): Timestamp when OTP expires
- `username` (String): Username associated with OTP

### Methods
- `OTPData(String otp, String username)`: Constructor that sets expiry time
- `isValid()`: Checks if OTP is still valid (not expired)

## Methods

### `generateOTP(String email, String username)`
- **Purpose**: Generates and sends a new OTP
- **Parameters**: 
  - `String email` - User's email address
  - `String username` - User's username
- **Returns**: `String` - Generated OTP code
- **Why**: Students need OTP for login verification
- **Functionality**:
  1. Generates random 6-digit OTP
  2. Stores OTP with email as key
  3. Sends OTP via email
  4. Returns OTP code

### `validateOTP(String email, String otp)`
- **Purpose**: Validates an OTP code
- **Parameters**: 
  - `String email` - User's email address
  - `String otp` - OTP code to validate
- **Returns**: `boolean` - True if OTP is valid
- **Why**: Verifies OTP during login process
- **Functionality**:
  1. Retrieves OTP data for email
  2. Checks if OTP exists
  3. Checks if OTP is not expired
  4. Compares provided OTP with stored OTP
  5. Removes OTP after successful validation (one-time use)
  6. Returns true if valid, false otherwise

### `getUsernameForOTP(String email)`
- **Purpose**: Retrieves username associated with an OTP
- **Parameters**: `String email` - Email address
- **Returns**: `String` - Username if OTP exists and is valid, null otherwise
- **Why**: Used to retrieve username after OTP verification

### `clearOTP(String email)`
- **Purpose**: Manually clears an OTP
- **Parameters**: `String email` - Email address
- **Why**: Allows manual OTP cleanup

## OTP Generation Logic
```java
Random random = new Random();
String otp = String.format("%06d", random.nextInt(1000000));
```
- Generates random number between 0-999999
- Formats as 6-digit string with leading zeros

## Security Considerations
1. **Expiration**: OTPs expire after 10 minutes
2. **One-time Use**: OTP is removed after successful validation
3. **Thread Safety**: Uses ConcurrentHashMap for concurrent access
4. **Production Note**: Should use Redis or database instead of in-memory storage

## Related Files
- `EmailService.java`: Sends OTP via email
- `HomeController.java`: Uses OTPService for login verification
- `verify-otp.html`: Frontend for OTP input

