# Developer Support Utilities Specification

## Purpose
Provide diagnostic and helper endpoints that assist development and operations — checking infrastructure connectivity, generating encoded passwords, validating credentials, and inspecting registered beans. Endpoints live under `/api/v1/supports`.

## Requirements

### Requirement: Redis connectivity check
The system SHALL provide a way to verify connectivity to Redis via `POST /api/v1/supports/redis-check-connection`.

#### Scenario: Redis reachable
- **WHEN** an authorized client calls the Redis check endpoint and Redis is reachable
- **THEN** the system returns a success indication

#### Scenario: Redis unreachable
- **WHEN** Redis is not reachable
- **THEN** the system returns a failure indication rather than throwing an unhandled error

### Requirement: Password encoding helper
The system SHALL generate an encoded (hashed) password from a plain input via `POST /api/v1/supports/encoded-password-generator` to assist with seeding and testing.

#### Scenario: Generate an encoded password
- **WHEN** an authorized client submits a plain password
- **THEN** the system returns the corresponding encoded hash

### Requirement: Credential validation helper
The system SHALL allow checking whether a given username/password pair is valid via `POST /api/v1/supports/user-check-login-credential`.

#### Scenario: Validate a credential pair
- **WHEN** an authorized client submits a username and password
- **THEN** the system reports whether the credentials are valid

### Requirement: Bean inspection
The system SHALL expose the list of registered Spring beans via `GET /api/v1/supports/system-get-bean-list` for diagnostic purposes.

#### Scenario: List beans
- **WHEN** an authorized client calls the bean-list endpoint
- **THEN** the system returns the names of the registered application beans
