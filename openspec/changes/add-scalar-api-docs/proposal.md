## Why

The project currently ships only Swagger UI for exploring the API. Scalar offers a more modern, readable API reference experience, and developers want to evaluate it side-by-side without losing the familiar Swagger UI. Because both renderers consume the same springdoc-generated OpenAPI document, adding Scalar is low-risk and reuses all existing API metadata (including the JWT `BearerAuth` scheme).

## What Changes

- Add Scalar as a **second** interactive API reference UI, served at `/scalar`, while **keeping** Swagger UI at `/swagger-ui/index.html` (coexist, not replace).
- Integrate via the first-party springdoc Scalar starter (`org.springdoc:springdoc-openapi-starter-webmvc-scalar`) so Scalar reads the existing `/v3/api-docs` document — no changes to `SwaggerConfig.apiInfo()` (title, version, license, JWT `BearerAuth` are reused automatically).
- Align all `org.springdoc` artifacts to a single matching version (currently `2.8.13` → the version that provides the Scalar starter, e.g. `2.8.17`).
- Whitelist the new `/scalar` path(s) in `SecurityConfig` (the `/v3/api-docs/**` path it depends on is already permitted).
- Relax the Content-Security-Policy **only for the `/scalar` path** so the Scalar assets loaded from the jsdelivr CDN are not blocked. The strict global `default-src 'self'` policy remains unchanged for every other endpoint.

No breaking changes: existing Swagger UI, the OpenAPI document, and all API endpoints are unaffected.

## Capabilities

### New Capabilities
<!-- None — this change extends existing capabilities rather than introducing new ones. -->

### Modified Capabilities
- `api-documentation`: Add a requirement that the system also serves a Scalar API reference at `/scalar`, backed by the same OpenAPI document, alongside the existing Swagger UI.
- `web-security`: The Content-Security-Policy requirement changes so that the strict `default-src 'self'` policy still applies globally, except the `/scalar` documentation path, which permits the specific external CDN origin needed to load Scalar.

## Impact

- **Dependencies** (`pom.xml`): add `springdoc-openapi-starter-webmvc-scalar`; bump existing `springdoc-openapi-starter-webmvc-ui` and `springdoc-openapi-starter-webmvc-api` to the same version.
- **Config** (`SecurityConfig.java`): whitelist `/scalar` (and `/scalar/**`); introduce a path-scoped CSP that allows the Scalar CDN origin for `/scalar` only.
- **Config** (`application-*.properties`): optional Scalar properties (e.g. enable flag / path) if defaults are not used.
- **Unaffected**: `SwaggerConfig.java`, all controllers, the `/v3/api-docs` document, and runtime behavior of every API endpoint.
- **Risk to verify at build time**: springdoc `2.8.x` targets Spring Boot 3.x while this project runs Spring Boot 4.0.5 / Java 25; the version bump and new starter must be validated with a real `mvn clean install` and by opening `/scalar`.
