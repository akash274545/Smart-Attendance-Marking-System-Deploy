# Attendance Management System

A Java full-stack application built with Spring Boot backend and HTML frontend for managing student attendance with role-based access control.

## Features

### Roles and Access Control
- **Admin (Role ID: 0)**: System administrator with full access
- **Teacher (Role ID: 1)**: Can manage subjects and approve students
- **Student (Role ID: 2)**: Can register and mark attendance

### Key Functionalities
- **User Registration**: Students can register on the website
- **Student Approval**: Teachers can approve registered students
- **Subject Management**: Teachers can create subjects with unique codes
- **Attendance Marking**: Students can mark attendance using subject codes with location tracking
- **Dashboard Views**: Role-specific dashboards with statistics and management options

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (Authentication & Authorization)
- **Firebase Admin SDK** (Firebase Realtime Database operations)
- **Firebase Realtime Database** (NoSQL Database)
- **Thymeleaf** (Template engine)

### Frontend
- **HTML5**
- **Bootstrap 5.3.0** (Styling)
- **Font Awesome 6.0.0** (Icons)
- **JavaScript** (Client-side functionality)

## Firebase Setup

### Prerequisites
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com)
2. Enable Realtime Database in your Firebase project
3. Get your Firebase Realtime Database URL
4. Download Firebase service account JSON file (for authentication)

### Firebase Configuration

1. **Create Firebase Project**
   - Go to [Firebase Console](https://console.firebase.google.com)
   - Click "Add project" and follow the setup wizard
   - Enable Realtime Database (choose your preferred location)

2. **Get Database URL**
   - In Firebase Console, go to Realtime Database
   - Copy your database URL (format: `https://YOUR_PROJECT_ID-default-rtdb.firebaseio.com/`)

3. **Download Service Account Key**
   - Go to Project Settings → Service Accounts
   - Click "Generate new private key"
   - Save the JSON file securely

4. **Update Configuration**
   Update the following in `src/main/resources/application.properties`:

   ```properties
   # Firebase Configuration
   firebase.database.url=https://YOUR_PROJECT_ID-default-rtdb.firebaseio.com/
   firebase.service.account.path=/path/to/your/serviceAccountKey.json
   ```

   **Note**: You can also set the `GOOGLE_APPLICATION_CREDENTIALS` environment variable instead of specifying the path in properties.

5. **Set Database Rules** (Optional for development)
   In Firebase Console → Realtime Database → Rules, you can set:
   ```json
   {
     "rules": {
       "AttendanceSystem": {
         ".read": true,
         ".write": true
       }
     }
   }
   ```
   **Warning**: These rules allow full access. For production, implement proper security rules.

## Installation and Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Firebase account and project
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd AttendanceSystem
   ```

2. **Set up Firebase**
   - Create a Firebase project (see [Firebase Setup](#firebase-setup) section above)
   - Download your service account JSON file
   - Update `application.properties` with your Firebase database URL
   - Set the service account path or `GOOGLE_APPLICATION_CREDENTIALS` environment variable

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - Open browser and navigate to `http://localhost:8080`
   - Default admin credentials: `admin` / `Admin@123`
   - The default admin user will be created automatically on first run

## Usage Guide

### Default Admin Account
- **Username**: `admin`
- **Password**: `Admin@123`
- **Role**: Administrator

### Admin Functions
1. **Dashboard**: View statistics (teachers, students, subjects, classes)
2. **Add Teacher**: Create new teacher accounts
3. **Add Subject**: Create subjects with unique codes
4. **Add Class**: Create class entities

### Teacher Functions
1. **Dashboard**: View assigned subjects and pending students
2. **Approve Students**: Approve registered student accounts
3. **Add Subject**: Create subjects with unique codes
4. **View Attendance**: Monitor student attendance records

### Student Functions
1. **Registration**: Self-register on the website
2. **Dashboard**: View available subjects and attendance history
3. **Mark Attendance**: Use subject codes to mark attendance with location
4. **View Attendance**: Check personal attendance records

## Project Structure

```
src/
├── main/
│   ├── java/com/attendance/
│   │   ├── AttendanceManagementApplication.java
│   │   ├── config/
│   │   │   ├── DataInitializer.java
│   │   │   ├── FirebaseConfig.java
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   ├── AdminController.java
│   │   │   ├── HomeController.java
│   │   │   ├── StudentController.java
│   │   │   └── TeacherController.java
│   │   ├── entity/
│   │   │   ├── Attendance.java
│   │   │   ├── AttendanceToken.java
│   │   │   ├── ClassEntity.java
│   │   │   ├── Subject.java
│   │   │   └── User.java
│   │   └── service/
│   │       ├── AttendanceService.java
│   │       ├── AttendanceTokenService.java
│   │       ├── ClassService.java
│   │       ├── FirebaseService.java
│   │       ├── FirebaseAttendanceService.java
│   │       ├── FirebaseAttendanceTokenService.java
│   │       ├── FirebaseClassService.java
│   │       ├── FirebaseSubjectService.java
│   │       ├── FirebaseUserService.java
│   │       ├── SubjectService.java
│   │       └── UserService.java
│   └── resources/
│       ├── application.properties
│       └── templates/
│           ├── login.html
│           ├── register.html
│           ├── admin/
│           │   ├── dashboard.html
│           │   ├── add-class.html
│           │   ├── add-subject.html
│           │   └── add-teacher.html
│           ├── teacher/
│           │   ├── dashboard.html
│           │   ├── add-subject.html
│           │   ├── subject-qr.html
│           │   └── view-attendance.html
│           └── student/
│               ├── dashboard.html
│               ├── mark-attendance.html
│               ├── mark-attendance-qr.html
│               ├── pending.html
│               └── view-attendance.html
└── pom.xml
```

## Firebase Database Structure

The application uses Firebase Realtime Database with the following structure:

```
AttendanceSystem/
├── users/
│   └── {userId}/
│       ├── id: String
│       ├── username: String
│       ├── password: String (BCrypt hashed)
│       ├── email: String
│       ├── fullName: String
│       ├── roleId: Integer (0=Admin, 1=Teacher, 2=Student)
│       ├── isApproved: Boolean
│       └── createdAt: String
├── classes/
│   └── {classId}/
│       ├── id: String
│       ├── className: String
│       ├── classCode: String
│       ├── description: String
│       └── createdAt: String
├── subjects/
│   └── {subjectId}/
│       ├── id: String
│       ├── subjectName: String
│       ├── subjectCode: String
│       ├── description: String
│       ├── teacherId: String (reference to user)
│       └── createdAt: String
├── attendance/
│   └── {attendanceId}/
│       ├── id: String
│       ├── studentId: String (reference to user)
│       ├── subjectId: String (reference to subject)
│       ├── attendanceDate: String
│       ├── latitude: Double
│       ├── longitude: Double
│       ├── locationAddress: String
│       ├── status: String (PRESENT, ABSENT, LATE)
│       ├── remarks: String
│       └── createdAt: String
└── attendanceTokens/
    └── {tokenId}/
        ├── id: String
        ├── token: String
        ├── subjectId: String (reference to subject)
        ├── teacherId: String (reference to user)
        ├── expiresAt: String
        ├── createdAt: String
        └── isActive: Boolean
```

**Note**: All data is automatically created and managed by the application. No manual database setup is required.

## Security Features

- **Password Encryption**: BCrypt password hashing
- **Role-based Access**: Different access levels for Admin, Teacher, Student
- **Session Management**: Secure login/logout functionality
- **CSRF Protection**: Disabled for development (enable for production)

## Location Tracking

The system includes basic location tracking for attendance:
- Students can mark attendance with GPS coordinates
- Location address can be manually entered
- Coordinates are stored for verification purposes

## Development Notes

- **Firebase Integration**: Uses Firebase Realtime Database for all data persistence
- **NoSQL Structure**: Data stored as JSON documents in Firebase
- **Default Admin**: Created automatically on first run (username: `admin`, password: `Admin@123`)
- **Student Approval**: Required before students can mark attendance
- **Subject Codes**: Unique codes shared by teachers for attendance marking
- **QR Code Support**: Teachers can generate QR codes for quick attendance marking
- **Real-time Updates**: Firebase provides real-time synchronization capabilities

## Troubleshooting

### Common Issues

1. **Firebase Connection Error**
   - Verify Firebase database URL is correct in `application.properties`
   - Check that your Firebase project has Realtime Database enabled
   - Ensure service account JSON file path is correct or `GOOGLE_APPLICATION_CREDENTIALS` is set
   - Verify Firebase service account has proper permissions

2. **Authentication Issues**
   - Ensure Firebase service account JSON file is valid
   - Check that the service account has "Firebase Realtime Database Admin" role
   - Verify `GOOGLE_APPLICATION_CREDENTIALS` environment variable if not using file path

3. **Database Rules Error**
   - Check Firebase Realtime Database rules in Firebase Console
   - For development, you may need to allow read/write access
   - For production, implement proper security rules based on authentication

4. **Port Already in Use**
   - Change port in `application.properties`: `server.port=8081`

5. **Login Issues**
   - Use default admin credentials: `admin` / `Admin@123`
   - Check if user account is approved (for students)
   - Verify Firebase connection is working (check application logs)

6. **Data Not Persisting**
   - Check Firebase Console to verify data is being written
   - Review application logs for Firebase errors
   - Verify database rules allow write operations

## Firebase Benefits

- **NoSQL Flexibility**: Easy to add new fields without schema migrations
- **Real-time Sync**: Automatic synchronization across clients
- **Scalability**: Firebase handles scaling automatically
- **Cloud-based**: No local database setup required
- **Security**: Built-in authentication and security rules

## Future Enhancements

- Email notifications for student approval
- Advanced reporting and analytics
- Mobile app integration with Firebase SDK
- Real-time attendance monitoring dashboard
- Push notifications for attendance reminders
- Integration with external calendar systems
- Firebase Authentication integration for enhanced security

## License

This project is created for educational and development purposes.
