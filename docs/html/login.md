# login.html Documentation

## Overview
The `login.html` file is the main login page for the Attendance Management System. It provides a beautiful, animated interface for user authentication with client-side validation.

## File Location
`src/main/resources/templates/login.html`

## Libraries Used

### 1. Bootstrap 5.3.0
- **CDN**: `https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css`
- **Purpose**: CSS framework for responsive design
- **Why**: Provides grid system, components, and utilities

### 2. Font Awesome 6.0.0
- **CDN**: `https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css`
- **Purpose**: Icon library
- **Why**: Provides icons for UI elements

### 3. Thymeleaf
- **Namespace**: `xmlns:th="http://www.thymeleaf.org"`
- **Purpose**: Server-side template engine
- **Why**: Renders dynamic content from Spring controllers

## Key Features

### Visual Design
- **Animated Background**: Gradient background with floating shapes
- **Glassmorphism**: Semi-transparent card with backdrop blur
- **Particle Effects**: Floating particles animation
- **Responsive**: Works on all screen sizes

### Form Elements

#### Username Input
- **Type**: Text
- **Validation**: Letters only (no numbers)
- **Required**: Yes
- **Client-side Validation**: JavaScript validates format

#### Password Input
- **Type**: Password (with toggle visibility)
- **Validation**: Must contain uppercase, lowercase, number, and special character
- **Required**: Yes
- **Toggle Button**: Eye icon to show/hide password

### JavaScript Functions

#### `createParticles()`
- **Purpose**: Creates floating particle animation
- **Why**: Enhances visual appeal
- **Functionality**: Generates 30 particles with random positions and animations

#### `validateUsername(username)`
- **Purpose**: Validates username format
- **Regex**: `/^[a-zA-Z]+$/`
- **Why**: Ensures username contains only letters

#### `validatePassword(password)`
- **Purpose**: Validates password strength
- **Requirements**: 
  - At least one uppercase letter
  - At least one lowercase letter
  - At least one number
  - At least one special character
- **Why**: Security requirement

#### `setupPasswordToggle()`
- **Purpose**: Toggles password visibility
- **Why**: User convenience

#### `checkIfStudent(username, e)`
- **Purpose**: Checks if user is student and requires OTP
- **Why**: Students need OTP verification
- **Functionality**: 
  1. Sends POST to `/check-student-login`
  2. Redirects to OTP page if student
  3. Proceeds with normal login otherwise

## Form Submission
- **Action**: `/login` (POST)
- **Process**:
  1. Client-side validation
  2. Check if student (requires OTP)
  3. Submit to Spring Security
  4. Redirect based on role

## Related Files
- `HomeController.java`: Handles login logic
- `SecurityConfig.java`: Configures authentication
- `verify-otp.html`: OTP verification page

