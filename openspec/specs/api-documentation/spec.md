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

### Requirement: Scalar API reference
The system SHALL serve a Scalar API reference UI at `/scalar`, rendered from the same OpenAPI document (`/v3/api-docs`) that backs Swagger UI, so developers can browse the API using either renderer.

#### Scenario: Open the Scalar reference
- **WHEN** a developer navigates to `/scalar`
- **THEN** the system serves the Scalar UI listing the documented endpoints, sourced from `/v3/api-docs`

#### Scenario: Swagger UI remains available
- **WHEN** a developer navigates to `/swagger-ui/index.html` after Scalar is added
- **THEN** the system still serves the existing Swagger UI, unchanged

#### Scenario: Shared API metadata and auth
- **WHEN** the Scalar reference renders the document
- **THEN** it reflects the same title, version, and JWT `BearerAuth` security scheme defined by the existing OpenAPI configuration, allowing the developer to authorize with a bearer token

