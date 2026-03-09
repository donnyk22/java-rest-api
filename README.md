# java-rest-api

Minimal and best practice of Rest API Java Spring with implementing some industry standard of back-end utility

<b>Features:</b>

- JWT
- File Upload
- Multiple login sessions
- ACL
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
- MySQL
- Redis
- RabbitMQ

<b>Setup:</b>

- Clone repo
- Import DB from this folder project (school.sql)
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

- To use WebSockets, open the HTML page in the "web" folder (can be opened directly in the browser)
- The audit trail in this project has two features. JPA automatically handles the first one with the @Audited endpoint, and the other one is manual by inserting into the database table.
  The @Audited is creating history from the entity's transaction into a new table. Pros: automatic and minimal configuration, cons: difficult to customize and database bloat.
  The other one is a highly customizable audit trail log with manual insert on every needed service. The cons is hard to maintain.
- All configurations are available in the application-dev.properties
