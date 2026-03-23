# register.html Documentation

## Overview
The `register.html` file is the student registration page. It allows new students to create accounts that require teacher approval.

## File Location
`src/main/resources/templates/register.html`

## Libraries Used
Same as login.html (Bootstrap, Font Awesome, Thymeleaf)

## Key Features

### Form Fields

#### Username
- **Type**: Text
- **Validation**: Letters only
- **Required**: Yes

#### Email
- **Type**: Email
- **Validation**: Email format
- **Required**: Yes

#### Full Name
- **Type**: Text
- **Required**: Yes

#### Password
- **Type**: Password (with toggle)
- **Validation**: Must contain uppercase, lowercase, number, and special character
- **Required**: Yes

### Form Submission
- **Action**: `/register` (POST)
- **Process**:
  1. Client-side validation
  2. Submits to `HomeController.registerUser()`
  3. Creates student account with `isApproved=false`
  4. Redirects to login with success message

### User Notice
- Displays info alert that account requires teacher approval
- Sets expectations for students

## Related Files
- `HomeController.java`: Handles registration
- `UserService.java`: Creates user account

