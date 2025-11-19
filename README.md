# Student Management CRUD API

Simple Spring Boot service demonstrating clean controller → service → repository structure for managing students.

## Stack
- Java 17
- Spring Boot 3.3.x (Web, Validation, Data JPA)
- H2 in-memory database for dev (Postgres profile template provided)
- Maven build

## Getting Started

### Prerequisites
- JDK 17+
- Maven 3.9+ (or use the Maven wrapper if added later)

### Run locally
```bash
cd student-management
./mvnw spring-boot:run
```
The API exposes Swagger UI via Springdoc at `http://localhost:8080/swagger-ui.html`. H2 console is at `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:studentdb`).

### Profiles
- `dev` (default): uses H2 in-memory database with `ddl-auto=update`.
- `postgres`: sample configuration available in `src/main/resources/application-postgres.yml` (commented). Rename/copy the file or set `SPRING_PROFILES_ACTIVE=postgres` and include a Postgres driver dependency.

## API Summary
Base path: `/api/students`

| Method | Path | Description |
| --- | --- | --- |
| POST | `/api/students` | Create student (returns `201 Created` with `Location` header) |
| GET | `/api/students/{id}` | Fetch student by id (404 if missing or soft-deleted) |
| GET | `/api/students` | List students with optional filters and pagination |
| PUT | `/api/students/{id}` | Full update (replaces mutable fields) |
| PATCH | `/api/students/{id}` | Partial update (non-null fields only) |
| DELETE | `/api/students/{id}` | Soft delete (sets `deleted=true`, `active=false`) |

### Query Parameters (GET list)
- `branch` – exact match, case-insensitive
- `active` – boolean
- `yop` – exact year of passing
- `startYop` / `endYop` – range filters
- `page` (default 0), `size` (default 20)
- `sort` – `field,direction` (defaults to `createdAt,desc`)

## Sample Requests

Create student:
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Ada Lovelace",
    "email": "ada@example.com",
    "phone": "+15551234567",
    "branch": "CSE",
    "yop": 1843,
    "active": true,
    "deleted": false
  }'
```

List students filtered by branch and year range:
```bash
curl 'http://localhost:8080/api/students?branch=CSE&startYop=2018&endYop=2024&page=0&size=10&sort=fullName,asc'
```

Update student:
```bash
curl -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Ada Byron",
    "email": "ada.byron@example.com",
    "phone": "+15557654321",
    "branch": "ECE",
    "yop": 1845,
    "active": true,
    "deleted": false
  }'
```

Soft delete student:
```bash
curl -X DELETE http://localhost:8080/api/students/1
```

## Validation & Error Handling
- Request DTOs enforce constraints via Jakarta Validation (`@NotBlank`, `@Email`, `@Pattern`, `@Min`, `@Max`).
- `GlobalExceptionHandler` centralizes errors, returning JSON bodies with timestamps, HTTP status, and validation details.
- Custom exceptions (`BadRequestException`, `ResourceNotFoundException`) mapped to 400/404.

## Assumptions
- Phone numbers are digits with optional leading `+` and length 7–15.
- `yop` range validation (1900–2100) is sufficient for demo.
- Soft delete preferred to allow recovery/history; `deleted=true` rows stay in DB but excluded from normal queries.
- Security/authentication is out of scope for this assignment.

## Postman Collection
Not included, but requests above can be imported easily. Let me know if a collection is required.

# KodNest
