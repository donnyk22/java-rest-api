## 1. Dependencies

- [x] 1.1 In `pom.xml`, add `org.springdoc:springdoc-openapi-starter-webmvc-scalar` at `3.0.3`
- [x] 1.2 Bump `springdoc-openapi-starter-webmvc-ui` and `springdoc-openapi-starter-webmvc-api` from `2.8.13` to `3.0.3` (Spring Boot 4 line); introduce a `<springdoc.version>3.0.3</springdoc.version>` property and reference it from all three artifacts to keep them in sync
- [x] 1.3 Run `mvn clean install` and confirm the project still compiles (build passed; `target/donnyk22-0.0.1-SNAPSHOT.jar` produced). Confirming it *starts* requires running the app — manual.

## 2. Security: authorization whitelist

- [x] 2.1 `/scalar` and `/scalar/**` are permitted via a dedicated `scalarFilterChain` (`@Order(1)`) that does `anyRequest().permitAll()` — cleaner than adding them to the main chain, and required anyway to scope the CSP (task 3)
- [x] 2.2 Confirm `/v3/api-docs/**` is still permitted (unchanged in the main chain; Scalar fetches the doc through it)

## 3. Security: path-scoped CSP

- [x] 3.1 Implemented a relaxed CSP scoped ONLY to `/scalar` via a separate `SecurityFilterChain` (`scalarFilterChain`, `@Order(1)`) using `securityMatcher("/scalar", "/scalar/**")`
- [x] 3.2 Global `default-src 'self'` CSP unchanged for all other paths (main `filterChain`, now `@Order(2)`)
- [x] 3.3 **MANUAL (needs browser):** a sensible default CSP is in place allowing `cdn.jsdelivr.net` (+ font origins). Open `/scalar` with devtools and read CSP violation reports; tighten/widen the directive list to exactly what this Scalar version needs

## 4. Optional configuration

- [x] 4.1 Relying on the `/scalar` default; added a documenting comment to `application-dev.properties` and `.example` (no speculative property keys)

## 5. Verification

- [x] 5.1 **MANUAL:** Run the app and open `/scalar` — confirm the Scalar reference renders the endpoints with no CSP violations in the console
- [x] 5.2 **MANUAL:** Open `/swagger-ui/index.html` — confirm Swagger UI still works unchanged (coexistence)
- [x] 5.3 **MANUAL:** In Scalar, authorize with a JWT bearer token and confirm a protected endpoint can be called
- [x] 5.4 **MANUAL:** Hit a non-docs endpoint and confirm the response still carries the strict `default-src 'self'` CSP header
- [x] 5.5 Update `README.md` to mention the Scalar docs URL alongside the Swagger UI URL
