## ADDED Requirements

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
