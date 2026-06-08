# School Management Specification

## Purpose
Provide CRUD management of the core school domain — users, students, teachers, classes, homeroom-teacher assignments, and attendances — backed by JPA/Hibernate. These endpoints live under `/api/v1/school` and represent the primary business data of the application.

## Requirements

### Requirement: Student management
The system SHALL provide create, read, update, and delete operations for students via `/api/v1/school/students`, including a profile picture that is uploaded as multipart form data.

#### Scenario: List and fetch students
- **WHEN** an authorized client calls `GET /api/v1/school/students` or `GET /api/v1/school/students/{studentId}`
- **THEN** the system returns the matching student records, or `404 Not Found` when the id does not exist

#### Scenario: Create a student
- **WHEN** an authorized client submits a valid student payload as multipart form data to `POST /api/v1/school/students`
- **THEN** the system persists the new student and returns the created record

#### Scenario: Update profile picture
- **WHEN** an authorized client uploads an image to `PATCH /api/v1/school/students/{studentId}/profile-pic`
- **THEN** the system stores the new profile picture for that student

#### Scenario: Delete a student
- **WHEN** an authorized client calls `DELETE /api/v1/school/students/{studentId}`
- **THEN** the system removes the student record

### Requirement: Teacher management
The system SHALL provide create, read, update, and delete operations for teachers via `/api/v1/school/teachers`, including a profile picture uploaded as multipart form data.

#### Scenario: Manage teachers
- **WHEN** an authorized client performs list, fetch, create, update, delete, or profile-pic operations under `/api/v1/school/teachers`
- **THEN** the system applies the requested operation and returns the corresponding result

### Requirement: Class management
The system SHALL provide create, read, update, and delete operations for classes via `/api/v1/school/classes`.

#### Scenario: Manage classes
- **WHEN** an authorized client performs list, fetch, create, update, or delete operations under `/api/v1/school/classes`
- **THEN** the system applies the requested operation and returns the corresponding result

### Requirement: Homeroom teacher assignment
The system SHALL allow assigning teachers to classes as homeroom teachers via `/api/v1/school/homeroom-teachers`, supporting list, fetch, create, and delete operations.

#### Scenario: Assign and remove homeroom teachers
- **WHEN** an authorized client creates or deletes a homeroom-teacher assignment
- **THEN** the system records or removes the link between the teacher and the class

### Requirement: Attendance management
The system SHALL record and manage student attendances via `/api/v1/school/attendances`, supporting list, fetch, create, and delete operations.

#### Scenario: Record attendance
- **WHEN** an authorized client submits a valid attendance entry to `POST /api/v1/school/attendances`
- **THEN** the system persists the attendance record

### Requirement: User management (JPA)
The system SHALL provide create, read, update, delete, and password-change operations for application users via `/api/v1/school/users`, with the profile handled as multipart form data and passwords updated through a dedicated endpoint.

#### Scenario: Change user password
- **WHEN** an authorized client calls `PATCH /api/v1/school/users/{userId}/password` with a valid new password
- **THEN** the system updates the stored password as an encoded hash

### Requirement: Authorization enforcement
The system SHALL enforce access control (ACL / role-based permissions) on school-management endpoints so that users may only perform operations permitted to their role (e.g., admin, teacher, student).

#### Scenario: Insufficient permissions
- **WHEN** an authenticated user attempts an operation not permitted by their role
- **THEN** the system rejects the request with `403 Forbidden`
