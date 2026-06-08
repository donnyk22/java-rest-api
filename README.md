# ☕ java-rest-api

> Minimal yet feature-rich REST API built with **Java Spring Boot**, implementing industry-standard back-end patterns and security practices.

---

## ✨ Features

### 🔐 Authentication & Security
- **JWT** — Stateless token-based authentication
- **Google OAuth2 / SSO** — Single sign-on via Google
- **MFA / TFA / 2FA** — Multi-factor authentication support
- **Multiple Login Sessions** — Manage concurrent user sessions
- **Brute Force Protection** — Login and sign-up rate limiting
- **ACL** — Access Control List for fine-grained permissions

### 🛡️ Web Security
- **XSS Protection** — Cross-Site Scripting defense
- **Clickjacking Protection** — Frame-busting headers
- **MIME-Sniffing Protection** — Content-type enforcement
- **Referrer Policy** — Controls referrer information
- **CORS Configuration** — Cross-Origin Resource Sharing setup

### 📡 Communication & Performance
- **WebSocket** — Real-time bidirectional communication
- **Message Broker (RabbitMQ)** — Async message queuing
- **Async** — Simple async functions, RabbitMQ integration, max worker & queue config
- **API Caching** — Response caching for performance
- **Rate Limiting** — Request throttling per client
- **Virtual Thread** — Lightweight concurrency with Java virtual threads

### 🗃️ Persistence
- **Dual ORM** — Uses both **JPA/Hibernate** (entity mapping, auditing) and **MyBatis** (SQL-centric mapping with code generation)

### 🗂️ Developer Experience
- **File Upload** — Multipart file handling
- **Excel Export** — Export data to `.xlsx` spreadsheet files
- **Word Export** — Export data to `.docx` document files
- **Compressed File Export** — Export files as `.zip` archives
- **Email Service** — Transactional email support
- **Audit Trail** — Automatic (JPA `@Audited`) and manual logging
- **Request Tracing** — End-to-end request tracking
- **Swagger API Docs** — Interactive API documentation
- **Docker Compose** — Containerized multi-service setup

---

## 🚧 Coming Soon

| Feature | Status |
|---|---|
| Unit Tests | 🔜 Planned |
| Custom Annotation Implementation | 🔜 Planned |
| 3rd Party Services (Telegram, Payment, AI, Mapbox) | 🔜 Planned |


---

## 🧰 Prerequisites

| Tool | Notes |
|---|---|
| **JDK 25** | Required Java version |
| **Maven** | Build tool |
| **MariaDB** | Via XAMPP or standalone |
| **Redis** | Caching & session store |
| **RabbitMQ** | Message broker |

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone <repo-url>
cd java-rest-api
```

### 2. Set Up the Database
Import the provided SQL file into your MariaDB instance:
```
school.sql  ← found in the project root
```
Then, run the MariaDB service.

### 3. (Optional) Generate MyBatis Artifacts
This project uses both **JPA** and **MyBatis**. To simulate / regenerate the MyBatis mappers, entities, and XML, run:
```bash
mvn mybatis-generator:generate
```
> ⚠️ Make sure the database connection is **alive** (MariaDB running and reachable) before running this — the generator introspects the live schema. See the [MyBatis](#-mybatis) section for file locations.

### 4. Configure Environment Variables
Copy the example property files and rename them (remove the `.example` suffix):
```
src/main/resources/application.properties.example
src/main/resources/application-dev.properties.example
src/main/resources/application-prd.properties.example
```
Then update credentials in `application-dev.properties` as needed.

### 5. Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

### 6. Access the App
Two interactive API references are available (both read the same OpenAPI document):
- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Scalar:** [http://localhost:8080/scalar](http://localhost:8080/scalar)

> ℹ️ Scalar loads its assets from a CDN, so the `/scalar` page needs internet access in the browser. A path-scoped Content-Security-Policy allows this for `/scalar` only; every other endpoint keeps the strict `default-src 'self'` policy.

### 7. Default Credentials

| Role | Username | Password |
|---|---|---|
| 👑 Admin | `admin` | `admin123456` |
| 🧑‍🏫 Teacher | `budi.teacher` | `budi123456` |
| 🧑‍🎓 Student | `arya@student.com` | `arya123456` |

After logging in via `POST /api/v1/auth/login`, copy the returned token and set it in whichever docs UI you use:

**🔵 Swagger UI**
1. Click the **Authorize** button (top-right).
2. Paste your token and confirm — it's now sent as `Authorization: Bearer <token>` on every request.

**🟣 Scalar** — set it under **Introduction → Authentication → Bearer token**
1. In the left sidebar, open **Introduction → Authentication**.
2. Select the **Bearer token** scheme (`BearerAuth`).
3. Paste **only the token value** (no `Bearer ` prefix — Scalar adds it for you).
4. The token is now applied to every "Test Request" / "Send" call automatically.

> 💡 Paste the raw JWT only. The `Bearer ` prefix is added automatically by both Swagger UI and Scalar.

---

## 🐳 Docker Setup

### First Run (or after code/config changes)
```bash
docker compose up --build -d
```

### Subsequent Runs
```bash
docker compose up -d
```

### Useful Docker Commands

| Command | Description |
|---|---|
| `docker compose ps` | List all containers and their status |
| `docker logs -f <container_name>` | Stream logs for a specific container |
| `docker compose down` | Stop and remove containers |
| `docker compose down -v` | Stop and remove containers + volumes |
| `docker compose restart <service>` | Restart a specific service |
| `docker compose logs -f` | Stream all service logs |
| `docker compose logs -f app --tail=100` | Last 100 lines from app |
| `docker compose logs -f app --tail=100 --since=1h` | Last 100 lines from the past hour |

---

## 📖 Usage Instructions

### 🐬 MyBatis
Alongside JPA/Hibernate, this project uses **MyBatis** for SQL-centric mapping with code generation.

**File locations:**

| Path | Purpose |
|---|---|
| `src/main/resources/my-batis-generator-config.xml` | MyBatis Generator configuration |
| `src/main/resources/mybatis/` | Hand-written & generated SQL XML mappers |
| `src/main/java/com/github/donnyk22/repositories/mybatis/` | Mapper interfaces (manual + auto-generated) |
| `src/main/java/com/github/donnyk22/models/entities/mybatis/` | MyBatis entity / model classes |
| `src/main/java/com/github/donnyk22/configurations/MyBatisConfig.java` | `@MapperScan` configuration |

**To regenerate the MyBatis artifacts:**
```bash
mvn mybatis-generator:generate
```
> ⚠️ The database connection must be **alive** (MariaDB running and reachable) — the generator reads the live schema. If the files don't appear after running, double-check the DB connection and the target paths in `my-batis-generator-config.xml`.

### 🌐 WebSocket
Open any `WebSocket***.html` file from the `web/` folder directly in your browser.

### 📧 Email Service
Uses [MailTrap](https://mailtrap.io/) by default — emails are captured in a sandbox inbox and **not** delivered to real recipients. Replace the credentials in `application-dev.properties` with your own MailTrap account for best results.

### 📋 Audit Trail
This project supports two audit trail strategies:

- **Automatic** — Uses JPA `@Audited` to auto-generate history tables on entity changes.
  - ✅ Minimal configuration
  - ⚠️ Limited customization, potential database bloat

- **Manual** — Custom audit log entries inserted explicitly in service methods.
  - ✅ Fully customizable
  - ⚠️ Higher maintenance overhead

### 🔑 Google OAuth2 / SSO
Open `OAuth2Test.html` from the `web/` folder in your browser. You must supply your **own** Google API credentials via the [Google Cloud Console](https://console.cloud.google.com/). Configuration lives in `SecurityConfig.java`.

### 🔐 MFA (Multi-Factor Authentication)

Follow these steps to test MFA:

1. Log in via `POST /api/v1/auth/login`
2. Generate a QR code at `GET /api/v1/mfa/qr-code` and scan it with your authenticator app
3. Log out, then log in via `POST /api/v1/mfa/login` — you'll receive a **temporary token**
4. Set the temporary token in Swagger's **Authorize** dialog
5. Verify your OTP at `POST /api/v1/mfa/verify` — you'll receive the **real token**
6. Replace the temporary token with the real token

> ⚠️ The temporary token grants access **only** to `/api/v1/mfa/verify`. Any other endpoint will return `403 Forbidden`.

---

## ⚙️ Configuration

All application settings are managed in:
```
src/main/resources/application-dev.properties
```
