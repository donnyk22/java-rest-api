# MyBatis User Management Specification

## Purpose
Provide an alternative, SQL-centric implementation of user CRUD using MyBatis (with code generation) alongside the JPA-based path, demonstrating the project's dual-ORM strategy. These endpoints live under `/api/v1/mybatis`.

## Requirements

### Requirement: MyBatis-backed user CRUD
The system SHALL provide create, read, update, and delete operations for users via `/api/v1/mybatis/users`, executed through MyBatis SQL mappers rather than JPA repositories.

#### Scenario: List and fetch users
- **WHEN** an authorized client calls `GET /api/v1/mybatis/users` or `GET /api/v1/mybatis/users/{userId}`
- **THEN** the system returns the matching user records via MyBatis mappers, or `404 Not Found` when the id does not exist

#### Scenario: Create a user
- **WHEN** an authorized client submits a valid user payload as multipart form data to `POST /api/v1/mybatis/users`
- **THEN** the system persists the new user via MyBatis and returns the created record

#### Scenario: Update a user
- **WHEN** an authorized client submits valid updates to `PUT /api/v1/mybatis/users/{userId}`
- **THEN** the system updates the user record

#### Scenario: Delete a user
- **WHEN** an authorized client calls `DELETE /api/v1/mybatis/users/{userId}`
- **THEN** the system removes the user record

### Requirement: MyBatis password change
The system SHALL allow changing a user's password via `PATCH /api/v1/mybatis/users/{userId}/password`, storing the new password as an encoded hash.

#### Scenario: Change password
- **WHEN** an authorized client submits a valid new password
- **THEN** the system updates the stored password hash via MyBatis

### Requirement: MyBatis artifact generation
The system SHALL support regenerating MyBatis mappers, model classes, and XML from the live database schema using the MyBatis Generator configuration.

#### Scenario: Regenerate from live schema
- **WHEN** a developer runs `mvn mybatis-generator:generate` with a reachable database
- **THEN** the generator introspects the schema and produces the mapper interfaces, entities, and XML at the configured target paths
