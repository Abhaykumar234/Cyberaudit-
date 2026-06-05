# CyberAudit Pro - Full Stack Application

A comprehensive security audit and vulnerability management platform built with Spring Boot and React.

## Project Structure

```
cyberaudit-pro/
├── pom.xml                          # Maven configuration
├── src/main/java/com/cyberaudit/
│   ├── CyberAuditApplication.java   # Main Spring Boot application
│   ├── config/                      # Configuration classes
│   │   ├── SecurityConfig.java      # Spring Security & CORS
│   │   ├── RestTemplateConfig.java  # REST client configuration
│   │   └── DataInitializer.java     # Database initialization
│   ├── controller/                  # REST API endpoints
│   │   ├── AuditController.java
│   │   ├── LabController.java
│   │   ├── VulnerabilityController.java
│   │   ├── AuditLogController.java
│   │   ├── MetricsController.java
│   │   └── TargetController.java
│   ├── service/                     # Business logic
│   │   ├── ClaudeAiService.java     # Claude AI integration
│   │   ├── AuditLogService.java
│   │   ├── VulnerabilityService.java
│   │   ├── MetricsService.java
│   │   └── SimulatedTargetService.java
│   ├── model/                       # JPA entities
│   │   ├── AuditLog.java
│   │   ├── Vulnerability.java
│   │   ├── SimulatedTarget.java
│   │   ├── User.java
│   │   └── enums/
│   │       ├── Role.java
│   │       ├── Severity.java
│   │       └── Status.java
│   ├── dto/                         # Data Transfer Objects
│   │   ├── AuditRequest.java
│   │   ├── AuditResponse.java
│   │   ├── LabRequest.java
│   │   ├── LabResponse.java
│   │   ├── FindingDto.java
│   │   └── MetricsDto.java
│   └── repository/                  # JPA repositories
│       ├── AuditLogRepository.java
│       ├── VulnerabilityRepository.java
│       ├── SimulatedTargetRepository.java
│       └── UserRepository.java
├── src/main/resources/
│   ├── application.yml              # Main configuration
│   └── application-dev.yml          # Development configuration
├── frontend/                        # React + Vite frontend
│   ├── package.json
│   ├── vite.config.ts
│   └── src/
│       └── api/
│           ├── auditApi.ts
│           ├── labApi.ts
│           ├── metricsApi.ts
│           └── vulnerabilityApi.ts
└── README.md
```

## Technology Stack

### Backend
- **Java 21** with Spring Boot 3.3
- **Spring Security** for authentication & authorization
- **Spring Data JPA** for database access
- **PostgreSQL** (production) / H2 (development)
- **Maven** for dependency management
- **Lombok** for boilerplate reduction

### Frontend
- **React 18** with TypeScript
- **Vite** for fast development
- **Axios** for HTTP requests
- **React Router** for navigation

## Prerequisites

- Java 21 or higher
- Maven 3.8+
- Node.js 18+ and npm
- PostgreSQL 14+ (for production)

## Setup Instructions

### Backend Setup

1. **Clone and navigate to project:**
   ```bash
   cd cyberaudit-pro
   ```

2. **Configure database (application.yml):**
   - For development: Uses H2 in-memory database (default)
   - For production: Update PostgreSQL connection details

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080/api`

5. **Access H2 Console (dev only):**
   ```
   http://localhost:8080/api/h2-console
   ```

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start development server:**
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:5173`

## API Endpoints

### Audit Management
- `POST /api/audit/generate` - Generate security audit
- `GET /api/audit/health` - Check audit service health

### Lab Evaluation
- `POST /api/lab/evaluate` - Evaluate secure coding lab
- `GET /api/lab/health` - Check lab service health

### Vulnerabilities
- `GET /api/vulnerabilities` - List all vulnerabilities
- `GET /api/vulnerabilities/{id}` - Get vulnerability details
- `GET /api/vulnerabilities/severity/{severity}` - Filter by severity
- `GET /api/vulnerabilities/category/{category}` - Filter by category
- `POST /api/vulnerabilities` - Create vulnerability
- `PUT /api/vulnerabilities/{id}` - Update vulnerability

### Audit Logs
- `GET /api/logs` - List all audit logs
- `GET /api/logs/user/{userId}` - Get logs by user
- `GET /api/logs/severity/{severity}` - Filter by severity
- `GET /api/logs/category/{category}` - Filter by category
- `POST /api/logs` - Create audit log

### Metrics
- `GET /api/metrics/system` - Get system metrics

### Targets
- `GET /api/targets` - List all targets
- `GET /api/targets/{id}` - Get target details
- `GET /api/targets/environment/{environment}` - Filter by environment
- `POST /api/targets` - Create target
- `PUT /api/targets/{id}` - Update target
- `DELETE /api/targets/{id}` - Delete target

## Configuration

### Security
- CORS enabled for `http://localhost:5173` and `http://localhost:3000`
- Session-based authentication (Spring Session)
- CSRF protection disabled for API endpoints

### Database
- **Development**: H2 in-memory database with auto-schema creation
- **Production**: PostgreSQL with connection pooling

### Logging
- Root level: INFO
- Application level: DEBUG
- Security level: DEBUG

## Development Workflow

1. **Backend changes:**
   - Modify Java files in `src/main/java`
   - Spring Boot will auto-reload on save
   - Check logs for errors

2. **Frontend changes:**
   - Modify React components in `frontend/src`
   - Vite will hot-reload automatically
   - API calls proxy to backend via Vite config

3. **Database changes:**
   - Modify entity classes
   - Hibernate will auto-update schema (dev mode)
   - For production, use migration tools

## Building for Production

### Backend
```bash
mvn clean package
java -jar target/cyberaudit-pro-1.0.0.jar
```

### Frontend
```bash
cd frontend
npm run build
# Output in frontend/dist/
```

## Environment Variables

Create `.env` file in frontend directory:
```
VITE_API_URL=http://localhost:8080/api
```

## Troubleshooting

### Port Already in Use
- Backend: Change `server.port` in `application.yml`
- Frontend: Change port in `vite.config.ts`

### Database Connection Issues
- Ensure PostgreSQL is running (production)
- Check connection details in `application.yml`
- For H2 (dev), no setup needed

### CORS Errors
- Verify frontend URL is in `corsConfigurationSource()` in SecurityConfig
- Check browser console for specific error

### API Not Responding
- Verify backend is running: `curl http://localhost:8080/api/audit/health`
- Check firewall settings
- Review application logs

## Next Steps

1. **Integrate Claude AI API:**
   - Add Anthropic SDK to pom.xml
   - Implement actual API calls in `ClaudeAiService`
   - Add API key configuration

2. **Add Authentication:**
   - Implement JWT tokens
   - Add user registration/login endpoints
   - Secure sensitive endpoints

3. **Frontend Components:**
   - Build React components for each page
   - Implement state management (Redux/Zustand)
   - Add styling with design system colors

4. **Testing:**
   - Add unit tests for services
   - Add integration tests for controllers
   - Add E2E tests for frontend

## License

Proprietary - CyberAudit Pro
