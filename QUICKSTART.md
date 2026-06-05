# CyberAudit Pro - Quick Start Guide

Get the full-stack application running in 5 minutes.

## Prerequisites Check

```bash
# Check Java version (need 21+)
java -version

# Check Maven
mvn -version

# Check Node.js
node -v
npm -v
```

## Step 1: Start Backend (Terminal 1)

```bash
# From project root
mvn spring-boot:run
```

Expected output:
```
Started CyberAuditApplication in X seconds
```

Backend runs on: `http://localhost:8080/api`

## Step 2: Start Frontend (Terminal 2)

```bash
cd frontend
npm install
npm run dev
```

Expected output:
```
VITE v5.0.0 ready in XXX ms
➜  Local:   http://localhost:5173/
```

Frontend runs on: `http://localhost:5173`

## Step 3: Verify Setup

### Test Backend
```bash
curl http://localhost:8080/api/audit/health
# Response: "Audit service is running"
```

### Test Frontend
Open browser: `http://localhost:5173`

### Test API Integration
```bash
curl -X POST http://localhost:8080/api/audit/generate \
  -H "Content-Type: application/json" \
  -d '{
    "targetId": "prod-api-v2",
    "auditType": "OWASP_TOP_10",
    "scope": "Full Infrastructure"
  }'
```

## Step 4: Access Database (Development Only)

H2 Console: `http://localhost:8080/api/h2-console`

- JDBC URL: `jdbc:h2:mem:cyberaudit_db`
- Username: `sa`
- Password: (leave blank)

## Sample API Calls

### Get System Metrics
```bash
curl http://localhost:8080/api/metrics/system
```

### List Vulnerabilities
```bash
curl http://localhost:8080/api/vulnerabilities?page=0&size=10
```

### Get Audit Logs
```bash
curl http://localhost:8080/api/logs?page=0&size=10
```

### List Targets
```bash
curl http://localhost:8080/api/targets
```

## Troubleshooting

### Port 8080 Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill process (macOS/Linux)
kill -9 <PID>

# Or change port in src/main/resources/application.yml
server:
  port: 8081
```

### Port 5173 Already in Use
```bash
# Change in frontend/vite.config.ts
server: {
  port: 5174
}
```

### Dependencies Not Installing
```bash
# Clear npm cache
npm cache clean --force

# Reinstall
cd frontend
rm -rf node_modules package-lock.json
npm install
```

### Database Connection Error
- Ensure you're using development profile (H2 is default)
- Check `application.yml` for correct settings
- For production, ensure PostgreSQL is running

## Next: Integrate Claude AI

1. Get API key from Anthropic
2. Add to `application.yml`:
   ```yaml
   anthropic:
     api-key: your-api-key-here
   ```
3. Update `ClaudeAiService.java` to use real API calls

## Project Structure Overview

```
Backend (Spring Boot)
├── Controllers → REST endpoints
├── Services → Business logic
├── Models → Database entities
├── Repositories → Data access
└── Config → Security, CORS, etc.

Frontend (React + Vite)
├── API layer → Axios clients
├── Components → React UI
├── Pages → Route views
└── Styles → Design system
```

## Common Commands

```bash
# Backend
mvn clean install          # Build
mvn spring-boot:run        # Run
mvn test                   # Run tests

# Frontend
npm install                # Install deps
npm run dev                # Dev server
npm run build              # Production build
npm run preview            # Preview build
npm run lint               # Lint code
```

## What's Included

✅ Spring Boot 3.3 backend with REST APIs
✅ React 18 frontend with Vite
✅ PostgreSQL/H2 database setup
✅ Spring Security with CORS
✅ OWASP Top 10 vulnerability data
✅ Simulated targets for testing
✅ Audit logging system
✅ Metrics dashboard endpoints
✅ Secure coding lab framework
✅ API integration layer

## What's Next

1. **Frontend UI**: Build React components for each page
2. **Claude Integration**: Connect to Anthropic API
3. **Authentication**: Add JWT or session-based auth
4. **Testing**: Add unit and integration tests
5. **Deployment**: Docker, Kubernetes, or cloud platform

## Support

Check `README.md` for detailed documentation and API reference.
