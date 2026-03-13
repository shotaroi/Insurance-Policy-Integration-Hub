# Insurance Policy Integration Hub

An enterprise integration service for insurance companies that sits between external partners and internal insurance systems. Demonstrates backend Java integration skills for insurance domain roles.

## Architecture

```
┌─────────────────┐     REST/SOAP      ┌──────────────────────────────┐
│ External Systems │ ◄────────────────► │  Insurance Policy Integration │
│ (Partners, MQ)   │                    │           Hub                  │
└─────────────────┘                    │  ┌────────────────────────┐   │
                                       │  │  REST API (Policy CRUD) │   │
                                       │  │  SOAP (Legacy)          │   │
                                       │  └───────────┬────────────┘   │
                                       │              │                │
                                       │  ┌───────────▼────────────┐   │
                                       │  │  Service Layer          │   │
                                       │  │  - Validation           │   │
                                       │  │  - Transformation       │   │
                                       │  └───────────┬────────────┘   │
                                       │              │                │
                                       │  ┌───────────▼────────────┐   │
                                       │  │  PostgreSQL │ RabbitMQ   │   │
                                       │  └────────────────────────┘   │
                                       └──────────────────────────────┘
```

### Package Structure

```
com.shotaroi.integrationhub
├── controller/       # REST endpoints
├── service/          # Business logic
├── repository/       # JPA data access
├── entity/           # Domain models
├── dto/              # Request/response DTOs
├── mapper/           # DTO ↔ Entity transformation
├── messaging/        # RabbitMQ publisher & consumer
├── soap/             # SOAP endpoint & types
├── config/           # Configuration beans
└── exception/        # Global error handling
```

## Tech Stack

- **Java 21** / **Spring Boot 3.2**
- **PostgreSQL** + **Flyway** migrations
- **RabbitMQ** (simulates IBM MQ)
- **Spring Web Services** (SOAP)
- **JUnit 5**, **Mockito**, **RestAssured**, **WireMock**

## Quick Start

### Prerequisites

- Java 21+
- Maven 3.9+
- Docker (optional, for PostgreSQL & RabbitMQ)

### Run with Docker

```bash
# Start PostgreSQL and RabbitMQ
docker-compose up -d

# Run the application
mvn spring-boot:run
```

### Run without Docker

Ensure PostgreSQL is running and create database `integration_hub`. RabbitMQ is optional; the app will start without it (messaging disabled).

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--DB_HOST=localhost --DB_PORT=5432 --DB_NAME=integration_hub --DB_USER=integration_user --DB_PASSWORD=integration_pass"
```

### Run Tests

```bash
mvn test
```

## API Endpoints

### REST API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/integration/policies` | Create policy |
| GET | `/api/integration/policies/{id}` | Get policy by ID |
| PATCH | `/api/integration/policies/{id}/status` | Update policy status |

### SOAP (Legacy)

- **WSDL**: `http://localhost:8080/ws/policy.wsdl`
- **Operations**: `GetPolicyByNumber`, `CreatePolicyLegacy`

### API Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## Example Requests

### Create Policy (REST)

```bash
curl -X POST http://localhost:8080/api/integration/policies \
  -H "Content-Type: application/json" \
  -d '{
    "policyNumber": "POL-001",
    "customerId": "CUST-001",
    "policyType": "LIFE",
    "premiumAmount": 150.00,
    "startDate": "2025-01-01",
    "status": "PENDING"
  }'
```

### Update Status

```bash
curl -X PATCH http://localhost:8080/api/integration/policies/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "ACTIVE"}'
```

## Message Queue Events

When policies are created or updated, events are published to RabbitMQ:

- `POLICY_CREATED`
- `POLICY_UPDATED`
- `POLICY_CANCELLED`

RabbitMQ Management UI: http://localhost:15672 (guest/guest)

## Configuration

| Variable | Default | Description |
|----------|---------|-------------|
| DB_HOST | localhost | PostgreSQL host |
| DB_PORT | 5432 | PostgreSQL port |
| DB_NAME | integration_hub | Database name |
| RABBITMQ_HOST | localhost | RabbitMQ host |
| RABBITMQ_PORT | 5672 | RabbitMQ port |

## License

MIT
