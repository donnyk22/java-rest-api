# Email Service Specification

## Purpose
Send transactional emails (plain and templated/rich) from the application, using a configurable SMTP provider (MailTrap sandbox by default). Endpoints live under `/api/v1/email`.

## Requirements

### Requirement: Send simple email
The system SHALL send a plain-text email via `POST /api/v1/email/send-simple` to a specified recipient.

#### Scenario: Send plain email
- **WHEN** an authorized client submits a valid recipient, subject, and body to the simple-send endpoint
- **THEN** the system dispatches a plain-text email through the configured SMTP provider

### Requirement: Send rich email
The system SHALL send a richer email (e.g., HTML/templated, with optional attachments) via `POST /api/v1/email/send`.

#### Scenario: Send rich email
- **WHEN** an authorized client submits a valid rich-email request
- **THEN** the system renders and dispatches the email through the configured SMTP provider

### Requirement: Configurable mail provider
The system SHALL read SMTP/mail credentials from application configuration so the provider can be changed without code changes, defaulting to a sandbox provider that does not deliver to real recipients.

#### Scenario: Sandbox by default
- **WHEN** the application runs with the default mail configuration
- **THEN** emails are captured by the sandbox inbox rather than delivered to real recipients
