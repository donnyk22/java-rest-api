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

### 🗂️ Developer Experience
- **File Upload** — Multipart file handling
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
| Excel & Word Export | 🔜 Planned |
| ZIP Export | 🔜 Planned |

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

### 3. Run the Other Services
- Run your Redis service
- Run your RabbitMQ service
```
Both can be run via Docker
```

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
Open Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### 7. Default Credentials

| Role | Username | Password |
|---|---|---|
| 👑 Admin | `admin` | `admin123456` |
| 🧑‍🏫 Teacher | `budi.teacher` | `budi123456` |
| 🧑‍🎓 Student | `arya@student.com` | `arya123456` |

> After logging in, copy your token and paste it into the **Authorize** button in Swagger UI.

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
