# java-rest-api

Minimal and best practice of Rest API Java Spring with implementing some industry standards of back-end utility

<b>Features:</b>
- JWT
- Google OAuth2/SSO
- MFA/TFA/2FA
- File Upload
- Multiple login sessions
- ACL
- Docker Compose
- Swagger API Documentation
- Brute force login and sign-up protection
- Email service
- Audit Trail
- Request Tracing
- API Caching
- Rate Limiting
- Message Broker (RabbitMQ)
- Async (simple async function, RabbitMQ implementation, max worker and max queue config)
- Web Socket
- Virtual Thread
- CORS Configuration
- XSS (Cross-Site Scripting) Protection
- Clickjacking Protection
- MIME-Sniffing Protection
- Referrer Policy

<b>Coming Soon:</b>
- Unit Test
- Custom annotation implementation
- 3rd Party service (Telegram bot, Payment gateway, AI Chatbot, Mapbox, etc)
- Excel & Word export
- ZIP export
- etc

=============================================

<b>Prerequisites:</b>
- JDK 25
- Maven
- MariaDB (XAMPP)
- Redis
- RabbitMQ

<b>Setup:</b>
- Clone repo
- Import DB from this project folder (school.sql)
- Make the environment variables file from the example files (src > main > resources > application.properties.example, application-dev.properties.example, application-prd.properties.example) or just rename all those files by removing the .example suffix
- Change credentials if needed (src > main > resources > application-dev.properties)
- run "mvn clean install"
- run "mvn spring-boot:run"
- Open http://localhost:8080/swagger-ui/index.html to access Swagger
- Register/Login on the Authentication endpoint
- Available credentials:<br/>
  <b>[Admin]</b> admin / admin123456<br/>
  <b>[Teacher]</b> budi.teacher / budi123456<br/>
  <b>[Student]</b> arya@student.com / arya123456
- Input token in the Swagger's Authorize section
- Start to use the app

<b>Instructions:</b>
- To use WebSockets, open the HTML page "WebSocket***.html" in the web folder (can be opened directly in the browser)
- Email service is using my MailTrap (https://mailtrap.io/) credentials. The email will not be sent to the real recipient, but will go into my MailTrap inbox. Better change into your own credentials.
- The audit trail in this project has two features. JPA automatically handles the first one with the @Audited endpoint, and the other one is manual by inserting into the database table.
  The @Audited is creating history from the entity's transaction into a new table. Pros: automatic and minimal configuration, cons: difficult to customize and database bloat.
  The other one is a highly customizable audit trail log with manual insert on every needed service. The cons is hard to maintain.
- For testing OAuth2/SSO login with Google, you can open "OAuth2Test.html" in the web folder (can be opened directly in the browser). The configuration is in "SecurityConfig.java".
  For Credentials, you must use your own private key (too risky to share my private key lol) by creating/using your existing project in the Google console (https://console.cloud.google.com/)
- For testing MFA, you must log in first in /api/v1/auth/login > then go to /api/v1/mfa/qr-code to generate a QR code and scan it with your authenticator app >
  Then you can log out and log in via /api/v1/mfa/login > you get the temporary token, set the token in the Swagger authorize button >
  then verify the MFA with your authenticator app in the /api/v1/mfa/verify > finally, you successfully log in and get the real token. Replace the temporary token with this new token,
  because the temporary token can only access this /api/v1/mfa/verify endpoint. If you try to access the other endpoint, you will get a 403 forbidden error.
- All configurations are available in the application-dev.properties

<b>Docker Instruction</b>
- Go to project folder, and run these commands:
- docker compose up --build -d (For build and run in background. Do this perform when you run for the first time, or change the Dockerfile, program code, or pom.xml)
- docker compose up -d (For run in background without build)
- docker compose ps (For view list of containers. Make sure all status is "Up")
- docker logs -f container_name (For view logs of app you want to check. Example: docker logs -f spring_app_container)
- docker compose down (For stop and remove containers)

- Other commands:
- docker compose down -v (For stop and remove containers and volumes)
- docker compose restart docker_service (For restart specific docker service. Example: docker compose restart db)
- docker compose logs -f (For view all logs)
- docker compose logs -f app --tail=100 (For view last 100 lines of logs)
- docker compose logs -f app --tail=100 --since=1h (For view last 100 lines of logs in last 1 hour)
