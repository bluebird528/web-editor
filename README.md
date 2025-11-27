# Web Editor API

RESTful API for content management system with JWT authentication, built with Spring Boot and PostgreSQL.

## Features

- ✅ JWT Authentication & Authorization
- ✅ Spring Boot 3.2.0
- ✅ PostgreSQL Database
- ✅ Automated Testing (Unit & Integration)
- ✅ Static Code Analysis (Checkstyle, SpotBugs)
- ✅ Swagger UI Documentation
- ✅ Content CRUD Operations
- ✅ Pagination & Search
- ✅ RESTful API Design

## Quick Start

### Prerequisites
- Java 17+
- PostgreSQL 12+
- Gradle 8.5+ (or use included wrapper)

### Database Setup
```bash
createdb webeditor
psql webeditor -c "CREATE USER webeditor WITH PASSWORD 'webeditor';"
psql webeditor -c "GRANT ALL PRIVILEGES ON DATABASE webeditor TO webeditor;"
```

### Build & Run
```bash
# Build project
./gradlew build

# Run application
./gradlew bootRun
```

Application will start on `http://localhost:8080`

### Run Tests
```bash
./gradlew test
```

### Static Code Analysis
```bash
# Run Checkstyle
./gradlew checkstyleMain checkstyleTest

# Run SpotBugs
./gradlew spotbugsMain spotbugsTest
```

## API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Authentication
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### Content Management (Requires JWT)
- `POST /api/contents` - Create content
- `GET /api/contents/{id}` - Get content by ID
- `PUT /api/contents/{id}` - Update content
- `DELETE /api/contents/{id}` - Delete content
- `GET /api/contents` - Get all contents (paginated)
- `GET /api/contents/my` - Get my contents
- `GET /api/contents/status/{status}` - Get contents by status
- `GET /api/contents/search?keyword=xxx` - Search contents

## Configuration

Environment variables:
- `DB_USERNAME` - Database username (default: webeditor)
- `DB_PASSWORD` - Database password (default: webeditor)
- `JWT_SECRET` - JWT signing key (required, min 32 chars)

See `src/main/resources/application.yml` for more configuration options.

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Database**: PostgreSQL (production), H2 (testing)
- **Security**: Spring Security + JWT
- **Testing**: JUnit 5, Mockito
- **Documentation**: SpringDoc OpenAPI 3
- **Build**: Gradle 8.5
- **Static Analysis**: Checkstyle, SpotBugs

## Project Structure

```
web-editor/
├── src/
│   ├── main/
│   │   ├── java/com/webeditor/api/
│   │   │   ├── config/        # Configuration
│   │   │   ├── controller/    # REST Controllers
│   │   │   ├── dto/           # Data Transfer Objects
│   │   │   ├── entity/        # JPA Entities
│   │   │   ├── repository/    # Data Access
│   │   │   ├── security/      # JWT Security
│   │   │   └── service/       # Business Logic
│   │   └── resources/
│   │       └── application.yml
│   └── test/                  # Tests
├── config/
│   └── checkstyle/           # Checkstyle config
├── build.gradle
└── README.md
```

## Example Usage

### Register User
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","password":"password123"}'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"password123"}'
```

### Create Content
```bash
curl -X POST http://localhost:8080/api/contents \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"title":"Hello World","body":"My first post","status":"DRAFT"}'
```

## Documentation

For detailed documentation, see [claude.md](./claude.md)

## License

This project is licensed under the Apache License 2.0.

## Support

For issues and questions, please open an issue on GitHub.