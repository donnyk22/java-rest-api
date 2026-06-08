## 1. Dependencies

- [ ] 1.1 In `pom.xml`, add `org.springdoc:springdoc-openapi-starter-webmvc-scalar` (version matching the other springdoc artifacts, e.g. `2.8.17`)
- [ ] 1.2 Bump `springdoc-openapi-starter-webmvc-ui` and `springdoc-openapi-starter-webmvc-api` from `2.8.13` to the same version; optionally introduce a `<springdoc.version>` property to keep all three in sync
- [ ] 1.3 Run `mvn clean install` and confirm the project still compiles and starts on Spring Boot 4.0.5 / Java 25

## 2. Security: authorization whitelist

- [ ] 2.1 In `SecurityConfig.java`, add `/scalar` and `/scalar/**` to the existing `permitAll()` request matchers (next to the swagger entries)
- [ ] 2.2 Confirm `/v3/api-docs/**` is still permitted (already present — no change expected)

## 3. Security: path-scoped CSP

- [ ] 3.1 Implement a relaxed Content-Security-Policy that applies ONLY to `/scalar` (preferred: a separate `SecurityFilterChain` with `securityMatcher("/scalar", "/scalar/**")` and higher `@Order`; alternative: a per-request header writer keyed on the path)
- [ ] 3.2 Keep the global `default-src 'self'` CSP unchanged for all other paths
- [ ] 3.3 Determine the exact CDN origin and minimal directive set by loading `/scalar` with browser devtools and reading CSP violation reports; widen only the directives Scalar actually needs (script/style/font/connect/img)

## 4. Optional configuration

- [ ] 4.1 (Optional) Add Scalar properties to `application-dev.properties` / `.example` (e.g. enable flag or custom path) if not relying on the `/scalar` default

## 5. Verification

- [ ] 5.1 Run the app and open `/scalar` — confirm the Scalar reference renders the endpoints with no CSP violations in the console
- [ ] 5.2 Open `/swagger-ui/index.html` — confirm Swagger UI still works unchanged (coexistence)
- [ ] 5.3 In Scalar, authorize with a JWT bearer token and confirm a protected endpoint can be called
- [ ] 5.4 Hit a non-docs endpoint and confirm the response still carries the strict `default-src 'self'` CSP header
- [ ] 5.5 Update `README.md` to mention the Scalar docs URL alongside the Swagger UI URL
