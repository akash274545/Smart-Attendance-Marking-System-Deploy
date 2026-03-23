# SecurityConfig Documentation

## Overview
The `SecurityConfig` class configures Spring Security for the application. It handles authentication, authorization, password encoding, and security filter chains.

## File Location
`src/main/java/com/attendance/config/SecurityConfig.java`

## Libraries Used

### 1. `org.springframework.context.annotation.Configuration`
- **Purpose**: Marks class as configuration
- **Why**: Spring processes this class for configuration

### 2. `org.springframework.context.annotation.Bean`
- **Purpose**: Defines Spring beans
- **Why**: Creates beans for dependency injection

### 3. `org.springframework.security.config.annotation.web.configuration.EnableWebSecurity`
- **Purpose**: Enables Spring Security
- **Why**: Activates security features

### 4. `org.springframework.security.config.annotation.web.builders.HttpSecurity`
- **Purpose**: Configures HTTP security
- **Why**: Defines access rules and authentication

### 5. `org.springframework.security.core.userdetails.UserDetailsService`
- **Purpose**: Loads user details for authentication
- **Why**: Custom user loading from Firebase

### 6. `org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder`
- **Purpose**: Password hashing algorithm
- **Why**: Securely stores passwords

### 7. `org.springframework.security.web.SecurityFilterChain`
- **Purpose**: Security filter chain configuration
- **Why**: Defines request filtering and authentication flow

## Beans

### `passwordEncoder()`
- **Type**: `PasswordEncoder`
- **Implementation**: `BCryptPasswordEncoder`
- **Purpose**: Encodes passwords before storage
- **Why**: BCrypt is industry standard for password hashing
- **Features**: Automatic salt generation, configurable strength

### `userDetailsService(UserService userService)`
- **Type**: `UserDetailsService`
- **Purpose**: Loads user details for Spring Security
- **Why**: Integrates Firebase users with Spring Security
- **Functionality**:
  1. Finds user by username
  2. Maps roleId to Spring Security roles
  3. Creates UserDetails object
  4. Throws exception if user not found

### `filterChain(HttpSecurity http)`
- **Type**: `SecurityFilterChain`
- **Purpose**: Configures security rules
- **Why**: Defines access control and authentication flow
- **Configuration**:
  - **Public Routes**: `/`, `/login`, `/register`, `/verify-otp`, `/resend-otp`, `/check-student-login`, static resources
  - **Admin Routes**: `/admin/**` requires ROLE_ADMIN
  - **Teacher Routes**: `/teacher/**` requires ROLE_TEACHER
  - **Student Routes**: `/student/**` requires ROLE_STUDENT
  - **Form Login**: Custom login page at `/login`, success redirect to `/dashboard`, failure redirect to `/login?error=true`
  - **Logout**: Logout at `/logout`, redirects to `/login`, clears session and cookies
  - **CSRF**: Disabled (can be enabled for production)

## Role Mapping
- `roleId = 0` → `ROLE_ADMIN`
- `roleId = 1` → `ROLE_TEACHER`
- `roleId = 2` → `ROLE_STUDENT`

## Related Files
- `UserService.java`: Provides user lookup
- `User.java`: User entity
- `HomeController.java`: Handles login/logout

