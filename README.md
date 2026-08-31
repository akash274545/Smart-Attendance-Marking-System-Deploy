# QR Based Smart Attendance and Reporting System

A Java full-stack web application built with Spring Boot, Thymeleaf, JavaScript, and Firebase Realtime Database for managing student attendance using QR codes, role-based access control, location tracking, reports, and email notifications.

---

## 📌 Project Overview

The **QR Based Smart Attendance and Reporting System** is designed to automate and simplify the traditional attendance process.

The system provides separate access and functionality for:

- **Admin**
- **Teacher**
- **Student**

Teachers can generate attendance QR codes, while students can scan the QR code to mark attendance. The system stores attendance information in Firebase Realtime Database and provides dashboards and attendance reports.

The application also supports location information during attendance marking and email-based functionality.

---

## ✨ Features

### 👨‍💼 Admin

- Admin authentication
- Admin dashboard
- View system statistics
- Add and manage teachers
- Add and manage subjects
- Add and manage classes
- Manage system-level information

### 👨‍🏫 Teacher

- Teacher authentication
- Teacher dashboard
- Create subjects with unique subject codes
- Approve registered students
- Generate QR codes for attendance
- View subject-wise attendance
- Monitor student attendance
- Manage assigned subjects

### 👨‍🎓 Student

- Student registration
- Student authentication
- Student approval workflow
- Student dashboard
- Mark attendance using subject codes
- Mark attendance using QR codes
- Location-based attendance information
- View personal attendance records
- View attendance history

### 📱 QR-Based Attendance

- Teachers can generate QR codes for attendance.
- QR codes contain attendance-related token information.
- Attendance tokens can have an expiry time.
- Students scan the QR code to mark attendance.
- The system validates the attendance token before recording attendance.

### 📍 Location Tracking

The system supports location information during attendance marking.

Attendance records can contain:

- Latitude
- Longitude
- Location address

This information can be used for attendance verification.

### 📧 Email Functionality

The application includes Gmail SMTP-based email functionality for sending system-related emails and attendance information.

Email credentials are stored using environment variables and are **not stored in the GitHub repository**.

### 📊 Dashboards and Reports

Role-specific dashboards provide information relevant to each user.

The system supports:

- Attendance records
- Attendance history
- Subject information
- Student information
- Teacher information
- Attendance reporting

---

# 🛠️ Technology Stack

## Backend

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Security**
- **Spring Mail**
- **Firebase Admin SDK**
- **Firebase Realtime Database**
- **Maven**

## Frontend

- **HTML5**
- **CSS3**
- **Bootstrap 5.3**
- **JavaScript**
- **Thymeleaf**
- **Font Awesome**

## Database

- **Firebase Realtime Database**

## Development Tools

- IntelliJ IDEA
- Visual Studio Code
- Git
- GitHub
- Java JDK 21

---

# 🏗️ System Architecture

```text
                         ┌───────────────────┐
                         │      ADMIN        │
                         └─────────┬─────────┘
                                   │
                                   │
                         ┌─────────▼─────────┐
                         │   Spring Boot     │
                         │    Application    │
                         └─────────┬─────────┘
                                   │
                 ┌─────────────────┼─────────────────┐
                 │                 │                 │
                 ▼                 ▼                 ▼
        ┌──────────────┐   ┌──────────────┐   ┌──────────────┐
        │    Teacher   │   │    Student   │   │ Email Service│
        └──────┬───────┘   └──────┬───────┘   └──────────────┘
               │                  │
               │ Generate QR      │ Scan QR
               │                  │
               └────────┬─────────┘
                        ▼
                ┌───────────────┐
                │   Attendance  │
                │    Service    │
                └───────┬───────┘
                        │
                        ▼
              ┌─────────────────────┐
              │ Firebase Realtime   │
              │     Database        │
              └─────────────────────┘
```

---

# 🔄 QR Attendance Workflow

```text
Teacher Login
     │
     ▼
Select Subject
     │
     ▼
Generate QR Code
     │
     ▼
Attendance Token Created
     │
     ▼
Student Scans QR
     │
     ▼
Token Validation
     │
     ├── Invalid / Expired
     │        │
     │        ▼
     │   Attendance Rejected
     │
     └── Valid
          │
          ▼
     Location Information
          │
          ▼
     Attendance Recorded
          │
          ▼
     Firebase Database
```


---

# 📱 Optional React QR Scanner

The main **Smart-Attendance-Marking-System** already provides QR-based attendance functionality through the student interface. Students can access the QR scanning feature directly from the main application.

A separate React-based QR Scanner application is also available as an **optional companion project** for students who prefer a dedicated QR scanning interface.

### 🔗 Companion Project

**Repository:** `student-qr-scan`

**GitHub:** https://github.com/akash274545/student-qr-scan

The React QR Scanner is designed specifically for:

- Student-side QR code scanning
- Camera-based QR code detection
- Dedicated QR scanning interface
- Connecting the scanned attendance token with the attendance system

### 💡 Why is the React QR Scanner separate?

The React QR Scanner was developed as a separate client application during the development and testing phase when the main Spring Boot application was running locally.

The architecture can therefore be used in two ways:

**Option 1 — Main Application**

Students can use the QR scanning functionality available directly inside the main **Smart-Attendance-Marking-System**.

```text
Student Login
      ↓
Student QR Scanner
      ↓
Camera Access
      ↓
Scan Teacher's QR Code
      ↓
Token Validation
      ↓
Attendance Marked

```
**Option 2 — Dedicated React Scanner**

Students can use the separate **student-qr-scan** React application as a dedicated QR scanning interface.
```text
Teacher
   ↓
Generate QR
   ↓
Attendance Token
   ↓
Student
   ↓
React QR Scanner
   ↓
Scan QR
   ↓
Attendance System
   ↓
Attendance Marked

```
📌 Important

The Smart-Attendance-Marking-System is the complete attendance management application containing the Admin, Teacher, and Student modules, attendance management, QR token generation and validation, location verification, Firebase integration, reports, and email functionality.

The student-qr-scan project is an **optional companion application** and is not required when using the QR scanning functionality available in the main application.


🔗 Related Repository

The dedicated React QR Scanner source code is available here:

student-qr-scan:
https://github.com/akash274545/student-qr-scan

---

# 📦 Main System Modules

The application consists of the following major modules:

1. **Login & Authentication Module**
2. **Student Management Module**
3. **Teacher Management Module**
4. **QR Code Generation Module**
5. **Attendance Management Module**
6. **Email Generation Module**
7. **Notification Module**
8. **Report Generation Module**
9. **Data Visualization Module**

---

# 👥 Roles and Access Control

| Role | Main Responsibilities |
|------|------------------------|
| **Admin** | Manage teachers, subjects, classes and system information |
| **Teacher** | Manage subjects, approve students, generate QR codes and monitor attendance |
| **Student** | Register, scan QR codes, mark attendance and view attendance history |

---

# 📂 Project Structure

```text
src/
├── main/
│   ├── java/com/attendance/
│   │   ├── AttendanceManagementApplication.java
│   │   │
│   │   ├── config/
│   │   │   ├── DataInitializer.java
│   │   │   ├── FirebaseConfig.java
│   │   │   └── SecurityConfig.java
│   │   │
│   │   ├── controller/
│   │   │   ├── AdminController.java
│   │   │   ├── HomeController.java
│   │   │   ├── StudentController.java
│   │   │   └── TeacherController.java
│   │   │
│   │   ├── entity/
│   │   │   ├── Attendance.java
│   │   │   ├── AttendanceToken.java
│   │   │   ├── ClassEntity.java
│   │   │   ├── Subject.java
│   │   │   └── User.java
│   │   │
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
│   │
│   └── resources/
│       ├── application.properties
│       ├── static/
│       └── templates/
│           ├── login.html
│           ├── register.html
│           │
│           ├── admin/
│           │   ├── dashboard.html
│           │   ├── add-class.html
│           │   ├── add-subject.html
│           │   └── add-teacher.html
│           │
│           ├── teacher/
│           │   ├── dashboard.html
│           │   ├── add-subject.html
│           │   ├── subject-qr.html
│           │   └── view-attendance.html
│           │
│           └── student/
│               ├── dashboard.html
│               ├── mark-attendance.html
│               ├── mark-attendance-qr.html
│               ├── pending.html
│               └── view-attendance.html
│
├── .env.example
├── .gitignore
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

# 🔥 Firebase Configuration

The application uses **Firebase Realtime Database** for storing application data.

## Prerequisites

Before running the application:

1. Create a Firebase project.
2. Enable Firebase Realtime Database.
3. Obtain your Firebase Realtime Database URL.
4. Generate a Firebase Admin SDK service-account key.
5. Keep the service-account JSON file securely on your local machine.

---

## 🔐 Environment Configuration

This project uses environment variables for sensitive configuration.

The repository contains:

```text
.env.example
```

as a safe configuration template.

Create your own local:

```text
.env
```

file.

Example:

```env
FIREBASE_DATABASE_URL=
FIREBASE_SERVICE_ACCOUNT_PATH=
ADMIN_USERNAME=
ADMIN_PASSWORD=
MAIL_USERNAME=
MAIL_PASSWORD=
```

Fill in the values locally.

### Environment Variables

| Variable | Purpose |
|---|---|
| `FIREBASE_DATABASE_URL` | Firebase Realtime Database URL |
| `FIREBASE_SERVICE_ACCOUNT_PATH` | Local path to Firebase Admin SDK JSON |
| `ADMIN_USERNAME` | Application admin username |
| `ADMIN_PASSWORD` | Application admin password |
| `MAIL_USERNAME` | Gmail account used for SMTP |
| `MAIL_PASSWORD` | Gmail App Password used for SMTP |

> **Never commit `.env` to GitHub.**

> **Never commit the Firebase service-account JSON file to GitHub.**

---

# 🔒 Secret Management

Sensitive configuration is intentionally kept outside the Git repository.

The following files are excluded through `.gitignore`:

```text
.env
*.json
target/
.idea/
.vscode/
.claude/
```

The repository contains only the safe configuration references.

For example:

```properties
firebase.database.url=${FIREBASE_DATABASE_URL}
firebase.service.account.path=${FIREBASE_SERVICE_ACCOUNT_PATH}

spring.security.user.name=${ADMIN_USERNAME}
spring.security.user.password=${ADMIN_PASSWORD}

spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

This allows the same codebase to be used across different environments without storing credentials in source code.

---

# 📥 Installation and Setup

## Prerequisites

Install:

- Java JDK 21
- Git
- Firebase project
- Firebase Realtime Database
- Firebase Admin SDK service-account JSON
- Internet connection

Maven does not need to be installed separately because the project includes the Maven Wrapper.

---

## 1. Clone the Repository

```bash
git clone https://github.com/akash274545/Smart-Attendance-Marking-System-Deploy.git
```

Move into the project:

```bash
cd Smart-Attendance-Marking-System-Deploy
```

---

## 2. Create `.env`

Copy the structure from:

```text
.env.example
```

Create:

```text
.env
```

and configure your own values.

---

## 3. Configure Firebase

Place your Firebase service-account JSON file on your local machine.

Then configure:

```env
FIREBASE_SERVICE_ACCOUNT_PATH=
```

with the appropriate local path.

Example:

```env
FIREBASE_SERVICE_ACCOUNT_PATH=path/to/firebase-service-account.json
```

Do not commit this JSON file.

---

## 4. Build the Project

Using Maven Wrapper on Windows:

```powershell
.\mvnw.cmd clean package
```

---

## 5. Run the Application

```powershell
.\mvnw.cmd spring-boot:run
```

The application uses port:

```text
8081
```

Open:

```text
http://localhost:8081
```

---

# 🌐 Application Access

After successfully starting the application, open:

```text
http://localhost:8081
```

The application provides separate authentication and dashboards for:

- Admin
- Teacher
- Student

Actual credentials are configured through environment variables and should not be stored in this README.

---

# 🧑‍💼 Admin Functions

The Admin dashboard provides functionality such as:

- View system statistics
- Manage teachers
- Manage subjects
- Manage classes
- Manage system information

---

# 👨‍🏫 Teacher Functions

Teachers can:

1. Log in to the system.
2. Create subjects.
3. Manage subject information.
4. Approve registered students.
5. Generate QR codes.
6. Monitor attendance.
7. View attendance records.

---

# 👨‍🎓 Student Functions

Students can:

1. Register through the application.
2. Log in after approval.
3. View available subjects.
4. Mark attendance using subject codes.
5. Scan QR codes to mark attendance.
6. Provide location information where supported.
7. View attendance history.

---

# 🗄️ Firebase Database Structure

The application uses Firebase Realtime Database with a structure similar to:

```text
AttendanceSystem/
│
├── users/
│   └── {userId}/
│       ├── id
│       ├── username
│       ├── password
│       ├── email
│       ├── fullName
│       ├── roleId
│       ├── isApproved
│       └── createdAt
│
├── classes/
│   └── {classId}/
│       ├── id
│       ├── className
│       ├── classCode
│       ├── description
│       └── createdAt
│
├── subjects/
│   └── {subjectId}/
│       ├── id
│       ├── subjectName
│       ├── subjectCode
│       ├── description
│       ├── teacherId
│       └── createdAt
│
├── attendance/
│   └── {attendanceId}/
│       ├── id
│       ├── studentId
│       ├── subjectId
│       ├── attendanceDate
│       ├── latitude
│       ├── longitude
│       ├── locationAddress
│       ├── status
│       ├── remarks
│       └── createdAt
│
└── attendanceTokens/
    └── {tokenId}/
        ├── id
        ├── token
        ├── subjectId
        ├── teacherId
        ├── expiresAt
        ├── createdAt
        └── isActive
```

---

# 🔐 Security Features

The application includes:

- Spring Security authentication
- Role-based access control
- BCrypt password hashing
- Secure session management
- QR attendance tokens
- Time-limited attendance tokens
- Environment-variable based credentials
- Firebase Admin SDK authentication
- Sensitive files excluded from Git
- Separate configuration for local/deployment environments

> For production deployment, Firebase Database Rules and application security settings should be reviewed and configured appropriately.

---

# 📍 Location Tracking

The system supports basic location information for attendance.

Attendance records may contain:

```text
Latitude
Longitude
Location Address
```

This information can be used to provide additional verification for attendance records.

---

# 📧 Email Service

The application supports Gmail SMTP for email functionality.

SMTP configuration is supplied through environment variables:

```env
MAIL_USERNAME=
MAIL_PASSWORD=
```

The Gmail App Password should be stored securely and should never be committed to GitHub.

---

# 🧪 Testing Checklist

Before deployment, verify:

- [ ] Admin login works
- [ ] Teacher login works
- [ ] Student registration works
- [ ] Student approval works
- [ ] Subject creation works
- [ ] QR generation works
- [ ] QR attendance works
- [ ] Manual attendance works
- [ ] Location information works
- [ ] Attendance records are saved to Firebase
- [ ] Attendance records can be viewed
- [ ] Email functionality works
- [ ] `.env` is not tracked by Git
- [ ] Firebase JSON is not tracked by Git
- [ ] No credentials are present in source code

---

# 🛠️ Troubleshooting

## Firebase Connection Error

Check:

- Firebase Realtime Database is enabled.
- `FIREBASE_DATABASE_URL` is correct.
- `FIREBASE_SERVICE_ACCOUNT_PATH` points to a valid JSON file.
- The Firebase service-account credentials are valid.
- The application has permission to access the database.

---

## Authentication Problems

Check:

- `ADMIN_USERNAME` is configured.
- `ADMIN_PASSWORD` is configured.
- Firebase is accessible.
- The application started without configuration errors.

---

## Port 8080 Already in Use

If port 8080 is already being used, stop the application/process using the port or run the application on another port.

For example:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

Then open:

```text
http://localhost:8081
```

---

## Data Not Appearing in Firebase

Check:

1. Firebase URL.
2. Service-account JSON.
3. Firebase permissions.
4. Application logs.
5. Firebase Realtime Database configuration.

---

# ☁️ Deployment

The project is structured so that sensitive configuration can be supplied separately from the source code.

For deployment, configure the required environment variables on the hosting platform:

```text
FIREBASE_DATABASE_URL
FIREBASE_SERVICE_ACCOUNT_PATH
ADMIN_USERNAME
ADMIN_PASSWORD
MAIL_USERNAME
MAIL_PASSWORD
```

The exact Firebase service-account configuration depends on the hosting platform.

> Never upload secrets to GitHub simply because a deployment platform requires them. Configure secrets through the platform's environment/secret management system whenever supported.

---

# 🔮 Future Enhancements

Possible future improvements include:

- Advanced attendance analytics
- Real-time attendance monitoring
- Mobile application
- Push notifications
- Advanced reporting
- Improved notification system
- Face-recognition based attendance
- Firebase Authentication integration
- External calendar integration
- Multi-college support
- More advanced attendance verification

---

# 📈 Firebase Benefits

The project uses Firebase Realtime Database because it provides:

- NoSQL data storage
- Real-time synchronization
- Cloud-based database
- Flexible data structure
- Scalable infrastructure
- Firebase security capabilities

---

# 👨‍💻 Developer

**Akash Narayankar**

GitHub:

https://github.com/akash274545

Project Repository:

https://github.com/akash274545/Smart-Attendance-Marking-System-Deploy

---

# 📄 License
This project is licensed under the MIT License.

See the [LICENSE](LICENSE) file for details.
