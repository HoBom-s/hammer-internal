# hammer-internal

Internal admin service for the Hammer platform. Provides management APIs for users, quizzes, notification templates, and error log tracking.

## Tech Stack

- Java 21
- Spring Boot 3.5
- PostgreSQL
- Flyway (migrations)
- JPA / Hibernate
- JUnit 5 + Testcontainers

## Architecture

Hexagonal architecture (Ports & Adapters) with strict module boundaries enforced by ArchUnit.

```
{module}/
├── domain/           # Pure domain models, value objects, enums
├── application/
│   ├── port/in/      # Use case interfaces (inbound)
│   ├── port/out/     # Persistence port interfaces (outbound)
│   ├── dto/          # Commands, query results
│   └── service/      # Use case implementations
└── adapter/
    ├── in/web/       # REST controllers
    └── out/persistence/  # JPA entities, repositories, adapters
```

Modules: `user`, `quiz`, `notification`, `errorlog`, `common`

## Prerequisites

- JDK 21+
- PostgreSQL 15+
- Environment variables (or `.env` file):

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | `localhost` | Database host |
| `DB_PORT` | `5432` | Database port |
| `DB_NAME` | `bear` | Database name |
| `DB_USERNAME` | `hobom` | Database user |
| `DB_PASSWORD` | _(empty)_ | Database password |
| `SERVER_PORT` | `8090` | Application port |

## Running

```bash
./gradlew bootRun
```

## Testing

```bash
# Unit tests
./gradlew test

# Integration tests (requires Docker for Testcontainers)
./gradlew integrationTest
```

## Code Quality

```bash
# Format code
./gradlew spotlessApply

# Coverage report (build/reports/jacoco/test/html)
./gradlew jacocoTestReport

# Coverage verification (70% overall, 80% service/domain)
./gradlew jacocoTestCoverageVerification
```

## API Documentation

Swagger UI is available at:

```
http://localhost:8090/swagger-ui.html
```

## API Endpoints

### Users
| Method | Path | Description |
|--------|------|-------------|
| GET | `/internal/users` | List users (paged, status filter) |
| GET | `/internal/users/{id}` | Get user by ID |

### Quizzes
| Method | Path | Description |
|--------|------|-------------|
| GET | `/internal/quizzes` | List quizzes (paged) |
| GET | `/internal/quizzes/{id}` | Get quiz by ID |
| POST | `/internal/quizzes` | Create quiz |
| PUT | `/internal/quizzes/{id}` | Update quiz |
| DELETE | `/internal/quizzes/{id}` | Delete quiz |

### Notification Templates
| Method | Path | Description |
|--------|------|-------------|
| GET | `/internal/notification-templates` | List templates |
| GET | `/internal/notification-templates/{id}` | Get template by ID |
| POST | `/internal/notification-templates` | Create template |
| PUT | `/internal/notification-templates/{id}` | Update template |
| DELETE | `/internal/notification-templates/{id}` | Delete template |

### Error Logs
| Method | Path | Description |
|--------|------|-------------|
| GET | `/internal/error-logs` | List error logs (paged, status filter) |

### Actuator
| Method | Path | Description |
|--------|------|-------------|
| GET | `/actuator/health` | Health check |
| GET | `/actuator/metrics` | Metrics |
| GET | `/actuator/prometheus` | Prometheus metrics |

## Database Schemas

- `hammer` — users, error_logs
- `auction` — quizzes, notification_templates

Migrations are managed by Flyway under `src/main/resources/db/migration/`.
