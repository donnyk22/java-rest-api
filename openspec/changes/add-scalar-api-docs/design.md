## Context

The API is documented with springdoc-openapi. springdoc produces two separable things:

1. An **OpenAPI document** at `/v3/api-docs` (from `springdoc-openapi-starter-webmvc-api` plus the `OpenAPI apiInfo()` bean in `SwaggerConfig.java`).
2. A **Swagger UI** renderer at `/swagger-ui/index.html` (from `springdoc-openapi-starter-webmvc-ui`).

Scalar is simply an alternative renderer of the same OpenAPI document. The goal is to add it at `/scalar` without disturbing Swagger UI or the document itself.

Constraints discovered in the codebase:
- `SecurityConfig.java` already permits `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`, `/swagger-resources/**`.
- `SecurityConfig.java` sets a strict global CSP: `.contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))`. Under this policy a browser will block any asset Scalar loads from a CDN.
- The project runs Spring Boot 4.0.5 / Java 25, while springdoc is on `2.8.13` (a 2.8.x line that targets Spring Boot 3.x). It currently works, so a minor bump is expected to be safe but must be verified.

## Goals / Non-Goals

**Goals**
- Serve Scalar at `/scalar`, reusing the existing `/v3/api-docs` document and its JWT `BearerAuth` scheme.
- Keep Swagger UI fully working at its current path.
- Keep the strict `default-src 'self'` CSP for the entire application except `/scalar`.

**Non-Goals**
- Replacing or removing Swagger UI.
- Changing `SwaggerConfig.apiInfo()` or any controller annotations.
- Self-hosting (vendoring) the Scalar assets — the chosen approach is CDN-based.
- Customizing Scalar theming beyond defaults (can be a later change).

## Decisions

### Decision 1: Use the first-party springdoc Scalar starter (not the standalone Scalar library)
Add `org.springdoc:springdoc-openapi-starter-webmvc-scalar`. 

**Why over alternatives:**
- The project already depends on the `org.springdoc` ecosystem; this starter shares the same version line and auto-wires against the existing `/v3/api-docs`.
- Alternative A — `com.scalar.maven:scalar` (standalone): a separate vendor library whose docs only claim Spring Boot 3.x / Java 17 support, adding more compatibility uncertainty on Spring Boot 4 / Java 25.
- Alternative B — hand-rolled static HTML loading `@scalar/api-reference`: more control but more code to maintain, and not requested.

### Decision 2: Align all springdoc artifacts to one version
Bump `springdoc-openapi-starter-webmvc-ui` and `springdoc-openapi-starter-webmvc-api` from `2.8.13` to the version that ships the Scalar starter (e.g. `2.8.17`), so all three `org.springdoc` artifacts match. Mixing springdoc versions risks classpath conflicts. Consider a `springdoc.version` Maven property to keep them in sync.

### Decision 3: Path-scoped CSP relaxation, not global
The global CSP stays `default-src 'self'`. Only `/scalar` gets a relaxed policy that allows the Scalar CDN origin (jsdelivr) for the directives Scalar needs (script, style, font, connect/img as required).

**Why over alternatives:**
- Relaxing the global CSP would weaken every endpoint to accommodate one docs page — unacceptable for a security-focused project.
- Implementation options (pick during apply):
  - **Separate `SecurityFilterChain`** with a higher `@Order` and `securityMatcher("/scalar", "/scalar/**")` that sets the relaxed CSP; the existing chain keeps the strict CSP for everything else. Cleanest separation.
  - **Per-request header writer** that emits the relaxed CSP only when the request path is `/scalar`. Smaller change but mixes concerns in one chain.
- Exact CDN origin and directive list must be confirmed empirically by loading `/scalar` with devtools open and reading any CSP violation reports.

### Decision 4: Whitelist `/scalar` in authorization
Add `/scalar` and `/scalar/**` to the `permitAll()` request matchers next to the existing swagger entries. `/v3/api-docs/**` is already permitted, so no change there.

## Risks / Trade-offs

- **Spring Boot 4 / Java 25 compatibility** → Verify with `mvn clean install` and by actually opening `/scalar`. If the 2.8.x line is incompatible, fall back to the static-HTML approach or revisit once a Spring Boot 4-compatible springdoc 3.x line is available. Roll back by removing the dependency and the CSP/whitelist edits.
- **CDN availability / offline Docker** → `/scalar` requires browser internet access to jsdelivr. Acceptable per the chosen CDN approach; documented as a known limitation. (A future change could vendor the assets for offline use.)
- **CSP misconfiguration** → If the relaxed directive list is too narrow, `/scalar` renders blank. Mitigate by iterating against actual browser CSP violation messages. If too broad, it only affects the `/scalar` path, not the app.
- **Version drift** → If the three springdoc artifacts fall out of sync later, classpath errors appear. Mitigate with a shared Maven version property.

## Migration Plan

1. Add dependency + align versions in `pom.xml`.
2. Whitelist `/scalar` and add the path-scoped relaxed CSP in `SecurityConfig.java`.
3. `mvn clean install`, run the app, open `/scalar` and `/swagger-ui/index.html`, confirm both work and Scalar can authorize with a JWT.
4. Rollback (if needed): revert the `pom.xml` and `SecurityConfig.java` changes; nothing else is touched.

## Open Questions

- Exact CDN origin(s) and the minimal CSP directive set the chosen springdoc Scalar starter version requires — to be determined empirically during apply.
- Whether to expose a `scalar.path` / enable property in `application-*.properties` or rely on the `/scalar` default.
- Whether the docs UIs should be disabled in the production profile (`application-prd.properties`) — out of scope unless desired.
