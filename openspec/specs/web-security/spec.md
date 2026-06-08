# Web Security & Traffic Control Specification

## Purpose
Protect the API against common web vulnerabilities and abusive traffic through security response headers, CORS configuration, per-client rate limiting, and response caching. These are cross-cutting concerns applied via filters and configuration rather than individual endpoints.
## Requirements
### Requirement: Security response headers
The system SHALL set protective HTTP response headers to defend against cross-site scripting, clickjacking, and MIME-sniffing, and SHALL apply a referrer policy. A strict Content-Security-Policy of `default-src 'self'` SHALL apply to all responses, EXCEPT the `/scalar` documentation path, where the policy SHALL be relaxed only enough to permit loading the Scalar API reference assets from the approved external CDN origin.

#### Scenario: Headers present on responses
- **WHEN** any HTTP response is returned by the application
- **THEN** the response includes anti-clickjacking, MIME-sniffing protection, XSS protection, and referrer-policy headers

#### Scenario: Strict CSP on application endpoints
- **WHEN** a response is returned for any path other than `/scalar`
- **THEN** the response carries the strict `default-src 'self'` Content-Security-Policy

#### Scenario: Relaxed CSP scoped to the Scalar docs path
- **WHEN** a response is returned for the `/scalar` documentation path
- **THEN** the Content-Security-Policy permits the approved Scalar CDN origin for the directives Scalar requires (e.g. scripts, styles, fonts, and connections) so the reference renders, without weakening the policy for any other path

### Requirement: CORS configuration
The system SHALL enforce a configured Cross-Origin Resource Sharing policy so that only permitted origins, methods, and headers are allowed for cross-origin requests.

#### Scenario: Disallowed origin
- **WHEN** a browser makes a cross-origin request from an origin not on the allow-list
- **THEN** the system does not return permissive CORS headers for that origin

### Requirement: Request rate limiting
The system SHALL throttle requests per client beyond a configured threshold to protect against abuse and overload.

#### Scenario: Exceeding the rate limit
- **WHEN** a client exceeds the configured request rate
- **THEN** the system rejects further requests with a throttling response until the window resets

### Requirement: API response caching
The system SHALL cache responses for designated read endpoints to improve performance, serving cached results until they expire or are invalidated.

#### Scenario: Cache hit
- **WHEN** a cacheable endpoint is requested again within the cache's validity window
- **THEN** the system serves the cached response instead of recomputing it

