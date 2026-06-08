# Multi-Factor Authentication Specification

## Purpose
Add a second authentication factor (TOTP / authenticator app) on top of credential login so that compromised passwords alone are insufficient to access an account. MFA endpoints live under `/api/v1/mfa`.

## Requirements

### Requirement: MFA enrollment via QR code
The system SHALL generate a TOTP secret for an authenticated user and expose it as a scannable QR code image via `GET /api/v1/mfa/qr-code` so the user can register the account in an authenticator app.

#### Scenario: Generate QR code
- **WHEN** an authenticated user requests the QR code endpoint
- **THEN** the system returns a PNG QR code image encoding the user's TOTP provisioning secret

### Requirement: MFA login issues a temporary token
The system SHALL provide `POST /api/v1/mfa/login` which, on valid primary credentials, issues a temporary token whose scope is limited to completing MFA verification.

#### Scenario: Valid credentials at MFA login
- **WHEN** a user submits valid credentials to the MFA login endpoint
- **THEN** the system returns a temporary token that grants access only to the MFA verify endpoint

#### Scenario: Temporary token cannot access other endpoints
- **WHEN** a client uses the temporary token to call any endpoint other than `POST /api/v1/mfa/verify`
- **THEN** the system rejects the request with `403 Forbidden`

### Requirement: OTP verification issues the real token
The system SHALL verify a user-supplied one-time password via `POST /api/v1/mfa/verify` and, on success, SHALL exchange the temporary token for a full-access token.

#### Scenario: Correct OTP
- **WHEN** a user submits a valid current OTP together with a valid temporary token
- **THEN** the system returns a full-access JWT token

#### Scenario: Incorrect or expired OTP
- **WHEN** a user submits an invalid or expired OTP
- **THEN** the system rejects the request and does not issue a full-access token
