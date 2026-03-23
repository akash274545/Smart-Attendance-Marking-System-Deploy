# EmailService Documentation

## Overview
The `EmailService` class handles sending emails for teacher credentials and OTP verification. It gracefully handles cases where email service is not configured.

## File Location
`src/main/java/com/attendance/service/EmailService.java`

## Libraries Used

### 1. `org.springframework.beans.factory.annotation.Autowired`
- **Purpose**: Dependency injection
- **Why**: Injects JavaMailSender (optional - required=false)
- **Note**: `required=false` allows service to work without email configuration

### 2. `org.springframework.mail.SimpleMailMessage`
- **Purpose**: Simple email message container
- **Why**: Easy way to create and send emails

### 3. `org.springframework.mail.javamail.JavaMailSender`
- **Purpose**: Spring's email sending interface
- **Why**: Abstracts email sending implementation
- **Note**: Can be null if email not configured

### 4. `org.springframework.stereotype.Service`
- **Purpose**: Marks class as Spring service
- **Why**: Enables component scanning and dependency injection

## Methods

### `sendTeacherCredentials(String email, String username, String password, String fullName)`
- **Purpose**: Sends login credentials to newly created teachers
- **Parameters**: 
  - `String email` - Teacher's email address
  - `String username` - Teacher's username
  - `String password` - Teacher's password (plain text - sent only once)
  - `String fullName` - Teacher's full name
- **Why**: Admin creates teachers and they receive credentials via email
- **Functionality**:
  1. Checks if email service is configured
  2. If not configured, prints credentials to console
  3. If configured, creates email with credentials
  4. Sends email or prints to console on failure

### `sendOTP(String email, String otp)`
- **Purpose**: Sends OTP code to user's email
- **Parameters**: 
  - `String email` - User's email address
  - `String otp` - One-time password code
- **Why**: Students need OTP for login verification
- **Functionality**:
  1. Checks if email service is configured
  2. If not configured, prints OTP to console
  3. If configured, creates email with OTP
  4. Sends email or prints to console on failure

### `buildTeacherEmailBody(String username, String password, String fullName)` (Private)
- **Purpose**: Builds email body text for teacher credentials
- **Parameters**: 
  - `String username` - Teacher's username
  - `String password` - Teacher's password
  - `String fullName` - Teacher's full name
- **Returns**: `String` - Formatted email body
- **Why**: Creates professional email content
- **Content**: Welcome message with login credentials and instructions

### `buildOTPEmailBody(String otp)` (Private)
- **Purpose**: Builds email body text for OTP
- **Parameters**: `String otp` - OTP code
- **Returns**: `String` - Formatted email body
- **Why**: Creates professional email content
- **Content**: OTP code with validity information and security notice

## Email Configuration
Email service requires SMTP configuration in `application.properties`:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## Fallback Behavior
If email service is not configured:
- Credentials and OTPs are printed to console
- System continues to function normally
- Useful for development and testing

## Related Files
- `OTPService.java`: Uses EmailService to send OTPs
- `AdminController.java`: Uses EmailService to send teacher credentials
- `HomeController.java`: Uses EmailService indirectly through OTPService

