## MODIFIED Requirements

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
