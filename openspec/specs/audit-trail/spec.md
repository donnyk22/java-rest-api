# Audit Trail Specification

## Purpose
Maintain a history of changes to domain data so that who-changed-what-and-when can be reviewed for accountability and debugging. The system supports both automatic (JPA `@Audited`) and manual audit strategies, with read access under `/api/v1/audit-trail`.

## Requirements

### Requirement: Automatic entity auditing
The system SHALL automatically record historical revisions of audited entities when they are created, updated, or deleted, using JPA auditing without per-operation code.

#### Scenario: Audited entity changes
- **WHEN** an audited entity is modified and the change is committed
- **THEN** the system records a revision capturing the prior/new state and revision metadata

### Requirement: Manual audit logging
The system SHALL allow service code to write explicit audit log entries for operations that require fully customized audit records.

#### Scenario: Manual audit entry
- **WHEN** a service method performs an operation that explicitly writes an audit log entry
- **THEN** the system persists a custom audit record with the specified details

### Requirement: Audit trail retrieval
The system SHALL expose audit trail records for authorized review via `GET /api/v1/audit-trail`.

#### Scenario: Read audit records
- **WHEN** an authorized client calls the audit-trail endpoint
- **THEN** the system returns the recorded audit entries
