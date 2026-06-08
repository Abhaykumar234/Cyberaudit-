# CyberAudit Pro - Architecture Overview

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Frontend (React + Vite)                 │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Pages: Dashboard, Vulnerabilities, Labs, Audit Log  │   │
│  │  Components: Cards, Charts, Tables, Forms            │   │
│  │  State: API layer with Axios clients                 │   │
│  └──────────────────────────────────────────────────────┘   │
│                          ↓ HTTP/REST                         │
│                    CORS-enabled proxy                        │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Backend (Spring Boot 3.3 + Java 21)            │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  REST Controllers (6 endpoints)                      │   │
│  │  ├─ AuditController (/audit/generate)               │   │
│  │  ├─ LabController (/lab/evaluate)                   │   │
│  │  ├─ VulnerabilityController (/vulnerabilities)      │   │
│  │  ├─ AuditLogController (/logs)                      │   │
│  │  ├─ MetricsController (/metrics)                    │   │
│  │  └─ TargetController (/targets)                     │   │
│  └──────────────────────────────────────────────────────┘   │
│                          ↓                                    │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Service Layer (Business Logic)                      │   │
│  │  ├─ ClaudeAiService (AI integration)                │   │
│  │  ├─ VulnerabilityService                            │   │
│  │  ├─ AuditLogService                                 │   │
│  │  ├─ MetricsService                                  │   │
│  │  └─ SimulatedTargetService                          │   │
│  └──────────────────────────────────────────────────────┘   │
│                          ↓                                    │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Repository Layer (Data Access)                      │   │
│  │  ├─ VulnerabilityRepository (JpaRepository)         │   │
│  │  ├─ AuditLogRepository                              │   │
│  │  ├─ SimulatedTargetRepository                       │   │
│  │  └─ UserRepository                                  │   │
│  └──────────────────────────────────────────────────────┘   │
│                          ↓                                    │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Database Layer (JPA/Hibernate)                      │   │
│  │  ├─ Entities: Vulnerability, AuditLog, User, Target │   │
│  │  ├─ Enums: Severity, Status, Role                   │   │
│  │  └─ Relationships: One-to-Many, Many-to-One         │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Database (PostgreSQL / H2)                      │
│  ├─ vulnerabilities (OWASP Top 10 data)                     │
│  ├─ audit_logs (Security events)                            │
│  ├─ simulated_targets (Test environments)                   │
│  ├─ users (User accounts)                                   │
│  └─ spring_session (Session management)                     │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              External Services (Future)                      │
│  ├─ Anthropic Claude API (AI analysis)                      │
│  ├─ AWS/GCP/Azure (Cloud targets)                           │
│  └─ SIEM/Monitoring (Integration)                           │
└─────────────────────────────────────────────────────────────┘
```

## Data Flow

### Audit Generation Flow
```
Frontend (AuditRequest)
    ↓
POST /api/audit/generate
    ↓
AuditController.generateAudit()
    ↓
ClaudeAiService.generateAudit()
    ↓
Claude API (external)
    ↓
Parse response → AuditResponse
    ↓
Save to AuditLog entity
    ↓
Return to Frontend
```

### Vulnerability Retrieval Flow
```
Frontend (Query params)
    ↓
GET /api/vulnerabilities?severity=CRITICAL
    ↓
VulnerabilityController.getVulnerabilitiesBySeverity()
    ↓
VulnerabilityService.getVulnerabilitiesBySeverity()
    ↓
VulnerabilityRepository.findBySeverity()
    ↓
JPA/Hibernate → SQL Query
    ↓
Database (PostgreSQL/H2)
    ↓
Return Page<Vulnerability>
    ↓
JSON serialization
    ↓
Frontend receives data
```

## Entity Relationships

```
User (1) ──────────────── (N) AuditLog
  ├─ id                      ├─ id
  ├─ username                ├─ userId (FK)
  ├─ email                   ├─ action
  ├─ role                    ├─ severity
  └─ enabled                 └─ timestamp

Vulnerability (1) ──────────────── (N) AuditLog
  ├─ id                      ├─ id
  ├─ cveId                   ├─ affectedResource (FK)
  ├─ title                   ├─ category
  ├─ severity                └─ status
  └─ status

SimulatedTarget (1) ──────────────── (N) Audit
  ├─ id                      ├─ id
  ├─ name                    ├─ targetId (FK)
  ├─ environment             ├─ findings
  └─ postureScore            └─ status
```

## Security Architecture

### Authentication & Authorization
```
Request
  ↓
SecurityFilterChain
  ├─ CORS validation
  ├─ CSRF check (disabled for API)
  ├─ Session validation
  └─ Role-based access control (@PreAuthorize)
  ↓
Controller
  ↓
Response
```

### CORS Configuration
- Allowed Origins: `http://localhost:5173`, `http://localhost:3000`
- Allowed Methods: GET, POST, PUT, DELETE, OPTIONS
- Credentials: Enabled (for session cookies)
- Max Age: 3600 seconds

### Session Management
- Type: JDBC-backed sessions
- Storage: Spring Session table in database
- Timeout: Configurable (default 30 minutes)

## API Design

### RESTful Endpoints

#### Audit Management
```
POST   /api/audit/generate          Create audit
GET    /api/audit/health            Health check
```

#### Lab Evaluation
```
POST   /api/lab/evaluate            Evaluate code
GET    /api/lab/health              Health check
```

#### Vulnerabilities
```
GET    /api/vulnerabilities         List all (paginated)
GET    /api/vulnerabilities/{id}    Get by ID
GET    /api/vulnerabilities/severity/{severity}  Filter
GET    /api/vulnerabilities/category/{category}  Filter
POST   /api/vulnerabilities         Create
PUT    /api/vulnerabilities/{id}    Update
```

#### Audit Logs
```
GET    /api/logs                    List all (paginated)
GET    /api/logs/user/{userId}      Filter by user
GET    /api/logs/severity/{severity} Filter by severity
GET    /api/logs/category/{category} Filter by category
POST   /api/logs                    Create
```

#### Metrics
```
GET    /api/metrics/system          Get system metrics
```

#### Targets
```
GET    /api/targets                 List all
GET    /api/targets/{id}            Get by ID
GET    /api/targets/environment/{env} Filter by environment
POST   /api/targets                 Create
PUT    /api/targets/{id}            Update
DELETE /api/targets/{id}            Delete
```

### Response Format

#### Success Response (200 OK)
```json
{
  "id": 1,
  "title": "Broken Access Control",
  "severity": "CRITICAL",
  "status": "OPEN",
  "timestamp": "2024-01-15T10:30:00"
}
```

#### Paginated Response (200 OK)
```json
{
  "content": [...],
  "totalElements": 247,
  "totalPages": 25,
  "currentPage": 0,
  "pageSize": 10
}
```

#### Error Response (4xx/5xx)
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Target ID is required",
  "path": "/api/audit/generate"
}
```

## Database Schema

### vulnerabilities table
```sql
CREATE TABLE vulnerabilities (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  cve_id VARCHAR(50) UNIQUE NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  severity VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  category VARCHAR(100) NOT NULL,
  occurrences INT NOT NULL,
  affected_endpoints INT NOT NULL,
  threat_vector TEXT,
  remediation_advice TEXT,
  last_detected TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL
);
```

### audit_logs table
```sql
CREATE TABLE audit_logs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id VARCHAR(100) NOT NULL,
  action VARCHAR(255) NOT NULL,
  description TEXT,
  severity VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  category VARCHAR(100) NOT NULL,
  timestamp TIMESTAMP NOT NULL,
  source_ip VARCHAR(45) NOT NULL,
  affected_resource VARCHAR(255)
);
```

### simulated_targets table
```sql
CREATE TABLE simulated_targets (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  environment VARCHAR(100) NOT NULL,
  target_type VARCHAR(50) NOT NULL,
  endpoint VARCHAR(255) NOT NULL,
  posture_score INT NOT NULL,
  active_findings INT NOT NULL,
  remediation_rate INT NOT NULL,
  assets_in_scope INT NOT NULL,
  description TEXT,
  created_at TIMESTAMP NOT NULL
);
```

## Deployment Architecture

### Development
```
Local Machine
├─ Backend: localhost:8080
├─ Frontend: localhost:5173
└─ Database: H2 in-memory
```

### Production
```
Cloud Platform (AWS/GCP/Azure)
├─ Backend: Spring Boot JAR
│  └─ Container: Docker
│     └─ Orchestration: Kubernetes
├─ Frontend: Static files
│  └─ CDN: CloudFront/Cloud CDN
└─ Database: PostgreSQL
   └─ Managed: RDS/Cloud SQL
```

## Performance Considerations

### Caching
- Database query results cached at service layer
- HTTP caching headers for static assets
- Session caching via Spring Session

### Pagination
- Default page size: 10 items
- Maximum page size: 100 items
- Offset-based pagination for simplicity

### Indexing
- Primary keys on all entities
- Foreign key indexes for relationships
- Composite indexes on frequently filtered columns

## Scalability

### Horizontal Scaling
- Stateless backend services
- Session storage in database (not memory)
- Load balancer for multiple instances

### Vertical Scaling
- Connection pooling (HikariCP)
- Query optimization with JPA
- Lazy loading for relationships

## Monitoring & Logging

### Application Logs
- Root level: INFO
- Application level: DEBUG
- Security level: DEBUG
- Output: Console + File

### Metrics
- System posture score
- Active findings count
- Remediation rate
- Threat level assessment

### Health Checks
- `/api/audit/health` - Audit service
- `/api/lab/health` - Lab service
- Database connectivity
- External API availability

## Future Enhancements

1. **Real-time Updates**: WebSocket for live audit results
2. **Advanced Analytics**: Machine learning for threat prediction
3. **Multi-tenancy**: Support multiple organizations
4. **API Gateway**: Kong or AWS API Gateway
5. **Message Queue**: RabbitMQ for async processing
6. **Caching Layer**: Redis for performance
7. **Search Engine**: Elasticsearch for vulnerability search
8. **Monitoring**: Prometheus + Grafana
