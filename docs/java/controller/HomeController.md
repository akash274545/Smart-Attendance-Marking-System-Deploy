# HomeController Documentation

## Overview
The `HomeController` class handles public-facing routes including login, registration, OTP verification, and password changes. It manages authentication flow and user registration.

## File Location
`src/main/java/com/attendance/controller/HomeController.java`

## Libraries Used

### 1. `org.springframework.stereotype.Controller`
- **Purpose**: Marks class as Spring MVC controller
- **Why**: Handles HTTP requests and returns views

### 2. `org.springframework.beans.factory.annotation.Autowired`
- **Purpose**: Dependency injection
- **Why**: Injects UserService and OTPService

### 3. `org.springframework.security.core.Authentication`
- **Purpose**: Spring Security authentication object
- **Why**: Gets current logged-in user information

### 4. `org.springframework.ui.Model`
- **Purpose**: Model for passing data to views
- **Why**: Adds attributes to be displayed in HTML templates

### 5. `jakarta.servlet.http.HttpSession`
- **Purpose**: HTTP session management
- **Why**: Stores temporary data during OTP verification flow

## Methods

### `home()`
- **Mapping**: `GET /`
- **Purpose**: Root endpoint redirects to login
- **Returns**: Redirect to `/login`
- **Why**: Default landing page

### `login(...)`
- **Mapping**: `GET /login`
- **Purpose**: Displays login page
- **Parameters**: 
  - `error` (optional): Error message parameter
  - `otp_required` (optional): Indicates OTP needed
- **Returns**: `login` template
- **Why**: Main login page
- **Functionality**: Adds error messages and OTP requirement to model

### `verifyOTPForm(...)`
- **Mapping**: `GET /verify-otp`
- **Purpose**: Displays OTP verification form
- **Parameters**: 
  - `email` (optional): Email from URL
- **Returns**: `verify-otp` template or redirect if session expired
- **Why**: Students enter OTP after login attempt
- **Functionality**: Retrieves pending email from session

### `verifyOTP(...)`
- **Mapping**: `POST /verify-otp`
- **Purpose**: Validates OTP code
- **Parameters**: 
  - `email`: User's email
  - `otp`: OTP code entered
  - `username` (optional): Username
- **Returns**: Redirect to login or error page
- **Why**: Verifies OTP and allows login
- **Functionality**:
  1. Validates OTP using OTPService
  2. Sets session flags if valid
  3. Redirects to login with verification flag

### `resendOTP(...)`
- **Mapping**: `POST /resend-otp`
- **Purpose**: Resends OTP to user
- **Parameters**: 
  - `email`: User's email
  - `username`: Username
- **Returns**: `verify-otp` template with success message
- **Why**: Users can request new OTP if expired

### `checkStudentLogin(...)`
- **Mapping**: `POST /check-student-login`
- **Purpose**: Checks if user is student and requires OTP
- **Parameters**: `username`: Username to check
- **Returns**: Redirect to verify-otp or login
- **Why**: Determines if OTP flow is needed
- **Functionality**:
  1. Finds user by username
  2. If student and approved, generates OTP
  3. Stores email/username in session
  4. Redirects to OTP verification

### `register()`
- **Mapping**: `GET /register`
- **Purpose**: Displays registration form
- **Returns**: `register` template
- **Why**: Student registration page

### `registerUser(...)`
- **Mapping**: `POST /register`
- **Purpose**: Creates new student account
- **Parameters**: 
  - `username`: Desired username
  - `password`: Password
  - `email`: Email address
  - `fullName`: Full name
- **Returns**: `login` template with success message or `register` with error
- **Why**: Handles student registration
- **Functionality**:
  1. Validates username/email uniqueness
  2. Creates user with roleId=2 (Student)
  3. Sets isApproved=false (requires teacher approval)
  4. Saves user

### `dashboard(Authentication authentication, Model model)`
- **Mapping**: `GET /dashboard`
- **Purpose**: Routes user to appropriate dashboard based on role
- **Parameters**: 
  - `Authentication authentication`: Current user
- **Returns**: Redirect to role-specific dashboard
- **Why**: Central routing after login
- **Functionality**: Checks user role and redirects accordingly

### `changePasswordForm(...)`
- **Mapping**: `GET /change-password`
- **Purpose**: Displays password change form
- **Parameters**: `Authentication authentication`: Current user
- **Returns**: `change-password` template
- **Why**: Users can change their passwords

### `changePassword(...)`
- **Mapping**: `POST /change-password`
- **Purpose**: Changes user password
- **Parameters**: 
  - `Authentication authentication`: Current user
  - `oldPassword`: Current password
  - `newPassword`: New password
  - `confirmPassword`: Password confirmation
- **Returns**: `change-password` template with success/error
- **Why**: Secure password change functionality
- **Functionality**:
  1. Validates new password matches confirmation
  2. Validates password length (min 6 characters)
  3. Verifies old password
  4. Updates password if valid

## Related Files
- `UserService.java`: User operations
- `OTPService.java`: OTP generation and validation
- `login.html`: Login page template
- `register.html`: Registration page template
- `verify-otp.html`: OTP verification template

