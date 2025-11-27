# Web Editor API - Claude Documentation

## Project Overview

Web Editor API is a RESTful backend service for managing content with JWT authentication. Built with Spring Boot 3.2.0 and PostgreSQL, it provides secure content creation, retrieval, update, and deletion capabilities.

## Architecture

### Technology Stack
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: PostgreSQL (Production), H2 (Testing)
- **Security**: JWT (JSON Web Tokens)
- **Documentation**: Swagger/OpenAPI 3
- **Build Tool**: Gradle 8.5
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Static Analysis**: Checkstyle, SpotBugs

### Project Structure
```
web-editor/
├── src/
│   ├── main/
│   │   ├── java/com/webeditor/api/
│   │   │   ├── config/           # Configuration classes (Security, OpenAPI)
│   │   │   ├── controller/       # REST API controllers
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── entity/           # JPA entities
│   │   │   ├── repository/       # Data access layer
│   │   │   ├── security/         # JWT security components
│   │   │   ├── service/          # Business logic layer
│   │   │   └── WebEditorApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-test.yml
│   └── test/
│       └── java/com/webeditor/api/
│           ├── controller/       # Controller tests
│           ├── repository/       # Repository tests
│           ├── security/         # Security tests
│           └── service/          # Service tests
├── config/
│   └── checkstyle/
│       └── checkstyle.xml
├── build.gradle
├── settings.gradle
└── gradlew
```

## Core Components

### 1. Authentication & Authorization (JWT)

#### Components
- **JwtTokenProvider**: Generates and validates JWT tokens
- **JwtAuthenticationFilter**: Intercepts requests and validates JWT tokens
- **CustomUserDetailsService**: Loads user details from database
- **SecurityConfig**: Configures Spring Security with JWT

#### How It Works
1. User registers via `/api/auth/signup`
2. User logs in via `/api/auth/login` and receives JWT token
3. Client includes token in `Authorization: Bearer <token>` header
4. JwtAuthenticationFilter validates token on each request
5. Authenticated requests can access protected endpoints

### 2. Content Management

#### Entities
- **User**: Authentication user with username, email, password, role
- **Content**: Main content entity with title, body, status, tags, author, timestamps

#### API Endpoints

**Authentication**
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/login` - Login and receive JWT token

**Content Management** (Requires JWT)
- `POST /api/contents` - Create new content
- `GET /api/contents/{id}` - Get content by ID
- `PUT /api/contents/{id}` - Update content (author only)
- `DELETE /api/contents/{id}` - Delete content (author only)
- `GET /api/contents` - Get all contents (paginated)
- `GET /api/contents/my` - Get current user's contents
- `GET /api/contents/status/{status}` - Get contents by status
- `GET /api/contents/search?keyword=xxx` - Search contents by title

### 3. Database Schema

**users table**
- id (PK, auto-increment)
- username (unique, not null)
- password (not null, encrypted with BCrypt)
- email (unique, not null)
- role (not null, default: ROLE_USER)

**contents table**
- id (PK, auto-increment)
- title (not null)
- body (TEXT, not null)
- status (not null, default: DRAFT)
- author_id (FK to users, not null)
- tags (nullable)
- created_at (timestamp, auto-generated)
- updated_at (timestamp, auto-updated)

## Configuration

### Environment Variables
- `DB_USERNAME`: Database username (default: webeditor)
- `DB_PASSWORD`: Database password (default: webeditor)
- `JWT_SECRET`: JWT signing key (minimum 32 characters, change in production!)

### Application Properties
See `src/main/resources/application.yml` for configuration:
- Database connection settings
- JPA/Hibernate settings
- JWT settings (secret, expiration)
- Swagger UI settings
- Logging levels

## Testing

### Test Coverage
- **Repository Tests**: JPA repository operations
- **Service Tests**: Business logic with mocked dependencies
- **Controller Tests**: REST API endpoints with MockMvc
- **Security Tests**: JWT token generation and validation

### Running Tests
```bash
./gradlew test
```

### Test Profile
Tests use H2 in-memory database (configured in `application-test.yml`)

## Static Code Analysis

### Checkstyle
Enforces Java coding standards and style guidelines.
```bash
./gradlew checkstyleMain
./gradlew checkstyleTest
```

Configuration: `config/checkstyle/checkstyle.xml`

### SpotBugs
Detects potential bugs in Java code.
```bash
./gradlew spotbugsMain
./gradlew spotbugsTest
```

Reports generated in `build/reports/spotbugs/`

## API Documentation

### Swagger UI
Access interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Docs
Raw OpenAPI specification available at:
```
http://localhost:8080/api-docs
```

### Authentication in Swagger
1. Click "Authorize" button in Swagger UI
2. Enter JWT token in format: `Bearer <your-token>`
3. All subsequent requests will include the token

## Development Workflow

### 1. Setup Development Environment
```bash
# Clone repository
git clone <repository-url>
cd web-editor

# Build project
./gradlew build

# Run application
./gradlew bootRun
```

### 2. Database Setup
Install and configure PostgreSQL:
```sql
CREATE DATABASE webeditor;
CREATE USER webeditor WITH PASSWORD 'webeditor';
GRANT ALL PRIVILEGES ON DATABASE webeditor TO webeditor;
```

### 3. Run Application
```bash
./gradlew bootRun
```

Application starts on `http://localhost:8080`

### 4. Testing Workflow
```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport

# Run static analysis
./gradlew checkstyleMain spotbugsMain
```

## Security Considerations

### Authentication
- Passwords are hashed using BCrypt
- JWT tokens expire after 24 hours (configurable)
- Tokens include username claim for user identification

### Authorization
- Users can only update/delete their own content
- All content endpoints require authentication
- Auth endpoints are public

### Best Practices
- Change `JWT_SECRET` in production
- Use strong passwords
- Enable HTTPS in production
- Regularly update dependencies
- Review security audit logs

## Common Operations

### Register a New User
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "password123"
  }'
```

### Create Content
```bash
curl -X POST http://localhost:8080/api/contents \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-token>" \
  -d '{
    "title": "My First Post",
    "body": "This is the content body",
    "status": "DRAFT",
    "tags": "test,example"
  }'
```

### Get All Contents
```bash
curl -X GET "http://localhost:8080/api/contents?page=0&size=10&sortBy=createdAt&sortDirection=DESC" \
  -H "Authorization: Bearer <your-token>"
```

## Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running
- Check database credentials in `application.yml`
- Ensure database exists

### JWT Token Issues
- Verify token is included in Authorization header
- Check token hasn't expired
- Ensure JWT_SECRET matches between token generation and validation

### Build Issues
- Run `./gradlew clean build`
- Check Java version (requires Java 17+)
- Verify all dependencies are downloaded

## Future Enhancements

Potential improvements for the API:
- Role-based access control (ADMIN, USER, EDITOR)
- Content versioning and revision history
- File upload support for images/attachments
- Content categories and taxonomies
- Full-text search with Elasticsearch
- Rate limiting and API throttling
- Content approval workflow
- Audit logging
- Email notifications
- API versioning
- Caching with Redis
- Docker containerization
- CI/CD pipeline integration

## Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [JWT.io](https://jwt.io/)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
