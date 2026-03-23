# admin/dashboard.html Documentation

## Overview
The `admin/dashboard.html` file is the main admin interface. It provides comprehensive management of teachers, students, classes, and subjects with CRUD operations.

## File Location
`src/main/resources/templates/admin/dashboard.html`

## Libraries Used
Same as login.html (Bootstrap, Font Awesome, Thymeleaf)

## Key Features

### Tabs
- **Teachers Tab**: Lists all teachers with CRUD operations
- **Students Tab**: Lists all students (approved and pending) with CRUD operations
- **Classes Tab**: Lists all classes with CRUD operations
- **Subjects Tab**: Lists all subjects with CRUD operations

### CRUD Operations
Each entity supports:
- **View**: Modal to view details
- **Edit**: Modal form to update information
- **Delete**: Confirmation modal to delete

### JavaScript Functions

#### Filter Functions
- `filterTeachers()`: Filters teacher table by search term
- `filterStudents()`: Filters student table by search and status
- `filterClasses()`: Filters class table by search term
- `filterSubjects()`: Filters subject table by search term

#### Reset Functions
- `resetTeacherFilter()`: Clears teacher filter
- `resetStudentFilter()`: Clears student filter
- `resetClassFilter()`: Clears class filter
- `resetSubjectFilter()`: Clears subject filter

#### CRUD Modal Functions
- `viewTeacherFromButton()`: Opens view modal
- `editTeacherFromButton()`: Opens edit modal with data
- `deleteTeacherFromButton()`: Opens delete confirmation
- Similar functions for Student, Class, Subject

### Data Display
- Uses Thymeleaf to iterate over collections
- Displays data in responsive tables
- Shows badges for status (Approved/Pending)

## Related Files
- `AdminController.java`: Handles all admin operations
- Various service classes: Provide data

