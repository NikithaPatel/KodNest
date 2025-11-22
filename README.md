# Student Management CRUD API

Simple Spring Boot service demonstrating clean controller → service → repository structure for managing students.

## Stack
- Java 17
- Spring Boot 3.3.x (Web, Validation, Data JPA, Security)
- Spring Security with JWT authentication
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

## Authentication

All `/api/students` endpoints require authentication. Users must sign up or sign in to obtain a JWT token.

### Authentication Endpoints

| Method | Path | Description |
| --- | --- | --- |
| POST | `/api/auth/signup` | Register a new user account |
| POST | `/api/auth/signin` | Sign in with email and password to get JWT token |

### Using JWT Tokens

After signing up or signing in, you'll receive a JWT token in the response. Include this token in all requests to protected endpoints:

```
Authorization: Bearer <your-jwt-token>
```

## API Summary
Base path: `/api/students` (requires authentication)

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

### Authentication

Sign up (create account):
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

Sign in (get JWT token):
```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

Response will contain:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "email": "user@example.com"
}
```

Save the `token` value and use it in subsequent requests.

### Student CRUD (requires authentication)

Create student:
```bash
TOKEN="<your-jwt-token>"
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
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
TOKEN="<your-jwt-token>"
curl 'http://localhost:8080/api/students?branch=CSE&startYop=2018&endYop=2024&page=0&size=10&sort=fullName,asc' \
  -H "Authorization: Bearer $TOKEN"
```

Update student:
```bash
TOKEN="<your-jwt-token>"
curl -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
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
TOKEN="<your-jwt-token>"
curl -X DELETE http://localhost:8080/api/students/1 \
  -H "Authorization: Bearer $TOKEN"
```

## Validation & Error Handling
- Request DTOs enforce constraints via Jakarta Validation (`@NotBlank`, `@Email`, `@Pattern`, `@Min`, `@Max`).
- `GlobalExceptionHandler` centralizes errors, returning JSON bodies with timestamps, HTTP status, and validation details.
- Custom exceptions (`BadRequestException`, `ResourceNotFoundException`) mapped to 400/404.

## Assumptions
- Phone numbers are digits with optional leading `+` and length 7–15.
- `yop` range validation (1900–2100) is sufficient for demo.
- Soft delete preferred to allow recovery/history; `deleted=true` rows stay in DB but excluded from normal queries.
- JWT tokens expire after 24 hours (86400000 ms) by default; can be configured in `application.yml`.
- Password minimum length is 6 characters.
- All user accounts are created with `USER` role by default.

## Postman Collection
Not included, but requests above can be imported easily. Let me know if a collection is required.

# KodNest
