# API Documentation Specification

## Purpose
Expose interactive, always-up-to-date API documentation (Swagger / OpenAPI) so developers can explore endpoints and authenticate against protected routes from the browser.

## Requirements

### Requirement: Interactive API docs
The system SHALL serve a Swagger UI describing the available REST endpoints, their parameters, and response schemas.

#### Scenario: Open Swagger UI
- **WHEN** a developer navigates to `/swagger-ui/index.html`
- **THEN** the system serves an interactive UI listing the documented endpoints

### Requirement: Authorize from the docs
The system SHALL allow supplying a bearer token in the Swagger UI so that protected endpoints can be exercised as an authenticated user.

#### Scenario: Authorize and call a protected endpoint
- **WHEN** a developer pastes a valid token into the Swagger "Authorize" dialog and invokes a protected endpoint
- **THEN** the request is sent with the token and the system processes it as authenticated
