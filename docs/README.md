# Attendance Management System - Documentation

## Overview
This documentation provides detailed explanations of all Java files and HTML templates in the Attendance Management System. Each file includes information about:
- Purpose and functionality
- Libraries used and why they're used
- All functions/methods with detailed explanations
- Related files and dependencies

## Documentation Structure

### Java Files

#### Entity Classes (`docs/java/entity/`)
- [User.md](java/entity/User.md) - User entity (Admin, Teacher, Student)
- [ClassEntity.md](java/entity/ClassEntity.md) - Academic class entity
- [Subject.md](java/entity/Subject.md) - Subject entity
- [Attendance.md](java/entity/Attendance.md) - Attendance record entity
- [AttendanceToken.md](java/entity/AttendanceToken.md) - QR code token entity

#### Service Classes (`docs/java/service/`)
- [UserService.md](java/service/UserService.md) - User business logic
- [ClassService.md](java/service/ClassService.md) - Class business logic
- [SubjectService.md](java/service/SubjectService.md) - Subject business logic
- [AttendanceService.md](java/service/AttendanceService.md) - Attendance business logic
- [AttendanceTokenService.md](java/service/AttendanceTokenService.md) - QR token business logic
- [EmailService.md](java/service/EmailService.md) - Email sending service
- [OTPService.md](java/service/OTPService.md) - OTP generation and validation
- [FirebaseService.md](java/service/FirebaseService.md) - Generic Firebase operations
- [FirebaseUserService.md](java/service/FirebaseUserService.md) - Firebase user persistence

#### Controller Classes (`docs/java/controller/`)
- [HomeController.md](java/controller/HomeController.md) - Public routes (login, register, OTP)
- [AdminController.md](java/controller/AdminController.md) - Admin operations
- [StudentController.md](java/controller/StudentController.md) - Student operations
- [TeacherController.md](java/controller/TeacherController.md) - Teacher operations

#### Configuration Classes (`docs/java/config/`)
- [SecurityConfig.md](java/config/SecurityConfig.md) - Spring Security configuration
- [FirebaseConfig.md](java/config/FirebaseConfig.md) - Firebase initialization
- [DataInitializer.md](java/config/DataInitializer.md) - Default data initialization

#### Main Application
- [AttendanceManagementApplication.md](java/AttendanceManagementApplication.md) - Application entry point

### HTML Templates (`docs/html/`)
- [login.md](html/login.md) - Login page
- [register.md](html/register.md) - Student registration page
- [admin-dashboard.md](html/admin-dashboard.md) - Admin dashboard
- [student-dashboard.md](html/student-dashboard.md) - Student dashboard
- [teacher-dashboard.md](html/teacher-dashboard.md) - Teacher dashboard

## Key Technologies

### Backend
- **Spring Boot**: Application framework
- **Spring Security**: Authentication and authorization
- **Firebase Realtime Database**: Data persistence
- **Thymeleaf**: Server-side templating

### Frontend
- **Bootstrap 5**: CSS framework
- **Font Awesome**: Icon library
- **JavaScript**: Client-side validation and interactions

### Libraries Used
- **BCrypt**: Password hashing
- **JavaMail**: Email sending
- **Firebase Admin SDK**: Firebase operations
- **Jakarta Validation**: Data validation

## System Architecture

### Layers
1. **Controller Layer**: Handles HTTP requests
2. **Service Layer**: Business logic
3. **Firebase Service Layer**: Data persistence
4. **Entity Layer**: Data models

### User Roles
- **Admin (roleId=0)**: Full system access
- **Teacher (roleId=1)**: Subject and student management
- **Student (roleId=2)**: Attendance marking and viewing

## Getting Started

1. Review entity classes to understand data models
2. Review service classes to understand business logic
3. Review controllers to understand request handling
4. Review HTML templates to understand UI structure

## Related Documentation
- See individual file documentation for detailed information
- Each file includes "Related Files" section for navigation

