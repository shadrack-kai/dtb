# Microservices Architecture with RabbitMQ

This setup runs 5 services using Docker Compose:

- RabbitMQ (with management UI)
- Profile Service (depends on RabbitMQ)
- Account Service (depends on Profile Service and RabbitMQ)
- Payment Service (depends on Account Service and RabbitMQ)
- Event Service (depends on Payment Service and RabbitMQ)

### Usage
1. Ensure Docker and Docker Compose are installed
2. Service images will be available locally 

### Steps to Build and Start:
##### Ensure project is built (creates target/your-app.jar):
From project root folder run:
```
mvn clean install -DskipTests
```
##### Run Docker Compose to build image and start services:
```
docker-compose up --build
```
This will build the docker images and start the services

##### Access RabbitMQ UI at `http://localhost:15672` (user/pass: `guest/guest`)

##### Once services are up and running, Swagger/Open API Documentation is available on the below links
- Profile service: - http://localhost:8080/swagger-ui/index.html
- Account service: - http://localhost:8081/swagger-ui/index.html
- Payment service: - http://localhost:8082/swagger-ui/index.html

##### Metrics & Health-Checks
Uses actuator and prometheus
- Profile service: - http://localhost:8080/actuator
- Account service: - http://localhost:8081/actuator
- Payment service: - http://localhost:8082/actuator

##### Testing 
- Postman collection is included
- Swagger is available for testing as well, just hit the login endpoint in profile service and use the access to token to authorize

- Login and access token: profile service creates a default admin
- Account services uses `idNumber` from profile service as the `customerId` when creating account