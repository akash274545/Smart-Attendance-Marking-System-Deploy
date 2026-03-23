# AdminController Documentation

## Overview
The `AdminController` class handles all admin-specific operations including managing teachers, students, classes, and subjects. It provides CRUD operations for all entities.

## File Location
`src/main/java/com/attendance/controller/AdminController.java`

## Libraries Used

### 1. `org.springframework.stereotype.Controller`
- **Purpose**: Marks class as Spring MVC controller
- **Why**: Handles HTTP requests

### 2. `org.springframework.web.bind.annotation.RequestMapping`
- **Purpose**: Base path mapping
- **Value**: `/admin`
- **Why**: All admin routes are under /admin

### 3. `org.springframework.beans.factory.annotation.Autowired`
- **Purpose**: Dependency injection
- **Why**: Injects service dependencies

### 4. `org.springframework.web.servlet.mvc.support.RedirectAttributes`
- **Purpose**: Flash attributes for redirects
- **Why**: Passes messages between redirects

## Methods

### Dashboard

#### `dashboard(Model model)`
- **Mapping**: `GET /admin/dashboard`
- **Purpose**: Displays admin dashboard with all data
- **Returns**: `admin/dashboard` template
- **Why**: Main admin interface
- **Model Attributes**:
  - `teachers`: All teachers
  - `students`: Approved students
  - `pendingStudents`: Pending students
  - `classes`: All classes
  - `subjects`: All subjects

### Teacher Management

#### `addTeacherForm(Model model)`
- **Mapping**: `GET /admin/add-teacher`
- **Purpose**: Displays add teacher form
- **Returns**: `admin/add-teacher` template
- **Why**: Form to create new teachers

#### `addTeacher(...)`
- **Mapping**: `POST /admin/add-teacher`
- **Purpose**: Creates new teacher
- **Parameters**: username, password, email, fullName, classId
- **Returns**: Redirect to dashboard or form with error
- **Why**: Admin creates teacher accounts
- **Functionality**:
  1. Validates username/email uniqueness
  2. Creates user with roleId=1 (Teacher)
  3. Sets isApproved=true
  4. Sends credentials via email
  5. Shows success message

#### `updateTeacher(...)`
- **Mapping**: `POST /admin/update-teacher`
- **Purpose**: Updates teacher information
- **Parameters**: id, fullName, username, email
- **Returns**: Redirect to dashboard
- **Why**: Edit teacher details
- **Functionality**: Validates uniqueness, updates user

#### `deleteTeacher(...)`
- **Mapping**: `POST /admin/delete-teacher`
- **Purpose**: Deletes teacher
- **Parameters**: id
- **Returns**: Redirect to dashboard
- **Why**: Remove teacher from system

### Student Management

#### `updateStudent(...)`
- **Mapping**: `POST /admin/update-student`
- **Purpose**: Updates student information
- **Parameters**: id, fullName, username, email
- **Returns**: Redirect to dashboard
- **Why**: Edit student details

#### `deleteStudent(...)`
- **Mapping**: `POST /admin/delete-student`
- **Purpose**: Deletes student
- **Parameters**: id
- **Returns**: Redirect to dashboard
- **Why**: Remove student from system

### Class Management

#### `addClassForm()`
- **Mapping**: `GET /admin/add-class`
- **Purpose**: Displays add class form
- **Returns**: `admin/add-class` template

#### `addClass(...)`
- **Mapping**: `POST /admin/add-class`
- **Purpose**: Creates new class
- **Parameters**: className, classCode, description
- **Returns**: Redirect to dashboard
- **Why**: Admin creates classes

#### `updateClass(...)`
- **Mapping**: `POST /admin/update-class`
- **Purpose**: Updates class information
- **Parameters**: id, className, classCode, description
- **Returns**: Redirect to dashboard
- **Why**: Edit class details

#### `deleteClass(...)`
- **Mapping**: `POST /admin/delete-class`
- **Purpose**: Deletes class
- **Parameters**: id
- **Returns**: Redirect to dashboard
- **Why**: Remove class from system

### Subject Management

#### `updateSubject(...)`
- **Mapping**: `POST /admin/update-subject`
- **Purpose**: Updates subject information
- **Parameters**: id, subjectName, subjectCode, description
- **Returns**: Redirect to dashboard
- **Why**: Edit subject details

#### `deleteSubject(...)`
- **Mapping**: `POST /admin/delete-subject`
- **Purpose**: Deletes subject
- **Parameters**: id
- **Returns**: Redirect to dashboard
- **Why**: Remove subject from system

## Related Files
- `UserService.java`: User operations
- `ClassService.java`: Class operations
- `SubjectService.java`: Subject operations
- `EmailService.java`: Sends teacher credentials
- `admin/dashboard.html`: Admin dashboard template

