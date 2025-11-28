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
- Docker & Docker Compose (recommended) OR PostgreSQL 12+
- Gradle 8.5+ (or use included wrapper)

### Database Setup

#### Option 1: Using Docker (Recommended)
```bash
# Copy environment template
cp .env.example .env

# Start PostgreSQL container
docker-compose up -d postgres

# Verify database is running
docker-compose ps

# View database logs
docker-compose logs -f postgres
```

PostgreSQL will be available at `localhost:5432` with:
- Database: `webeditor`
- Username: `webeditor`
- Password: `webeditor`

Optional: Start pgAdmin for database management
```bash
docker-compose up -d pgadmin
```
Access pgAdmin at `http://localhost:5050` (Email: admin@webeditor.com / Password: admin)

#### Option 2: Local PostgreSQL Installation
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

### Docker Commands
```bash
# Start all services
docker-compose up -d

# Start only PostgreSQL
docker-compose up -d postgres

# Stop all services
docker-compose down

# Stop and remove volumes (WARNING: deletes all data)
docker-compose down -v

# View logs
docker-compose logs -f

# View PostgreSQL logs only
docker-compose logs -f postgres

# Restart PostgreSQL
docker-compose restart postgres

# Access PostgreSQL CLI
docker-compose exec postgres psql -U webeditor -d webeditor
```

### Static Code Analysis
```bash
# Run Checkstyle
./gradlew checkstyleMain checkstyleTest

# Run SpotBugs
./gradlew spotbugsMain spotbugsTest
```

### Git Commit Message Automation
```bash
# Setup Git hooks and commit message template (one-time setup)
./gradlew setupGitHooks
```

This project uses **Conventional Commits** format for commit messages:
- `feat(scope): description` - New features
- `fix(scope): description` - Bug fixes
- `refactor(scope): description` - Code refactoring
- `docs(scope): description` - Documentation updates
- `test(scope): description` - Test additions/updates
- `chore(scope): description` - Build/config changes

The Git hooks will:
- Automatically load commit message template when committing
- Validate commit messages follow Conventional Commits format
- Enforce subject line length limits (72 chars max, 50 recommended)

Example commit messages:
```bash
git commit -m "feat(auth): add JWT token refresh functionality"
git commit -m "fix(content): resolve NPE in content update"
git commit -m "docs: update README with setup instructions"
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

### Environment Variables

Application supports the following environment variables (see `.env.example`):

**Database Configuration:**
- `DB_HOST` - Database host (default: localhost)
- `DB_PORT` - Database port (default: 5432)
- `DB_NAME` - Database name (default: webeditor)
- `DB_USERNAME` - Database username (default: webeditor)
- `DB_PASSWORD` - Database password (default: webeditor)
- `DB_POOL_SIZE` - Connection pool size (default: 10)

**JPA Configuration:**
- `JPA_DDL_AUTO` - Hibernate DDL mode (default: update)
- `JPA_SHOW_SQL` - Show SQL queries (default: true)

**Security:**
- `JWT_SECRET` - JWT signing key (required, min 32 chars)

**Docker PostgreSQL:**
- `POSTGRES_DB` - PostgreSQL database name (default: webeditor)
- `POSTGRES_USER` - PostgreSQL user (default: webeditor)
- `POSTGRES_PASSWORD` - PostgreSQL password (default: webeditor)
- `POSTGRES_PORT` - PostgreSQL port (default: 5432)

**Docker pgAdmin (Optional):**
- `PGADMIN_EMAIL` - pgAdmin login email (default: admin@webeditor.com)
- `PGADMIN_PASSWORD` - pgAdmin password (default: admin)
- `PGADMIN_PORT` - pgAdmin port (default: 5050)

### Configuration Files

- `application.yml` - Main application configuration
- `.env` - Local environment variables (create from `.env.example`)
- `docker-compose.yml` - Docker services configuration

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