# Quick Build Fix - CyberAudit Pro

## Problem
Lombok annotation processing is failing with Java 25. 

## Solution
I'm removing Lombok and using plain Java getters/setters. This will compile immediately.

## Steps to Build

1. **Clean and rebuild:**
```bash
mvn clean install -DskipTests
```

2. **Run the application:**
```bash
mvn spring-boot:run
```

3. **Access the application:**
- Backend API: http://localhost:8080/api
- H2 Console (dev): http://localhost:8080/api/h2-console

## Database Setup

### For Development (H2 - In-Memory)
Already configured in `application-dev.yml`. Just run with:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### For Production (PostgreSQL)
1. Install PostgreSQL
2. Create database:
```sql
CREATE DATABASE cyberaudit_db;
```

3. Update `application.yml` with your credentials
4. Run:
```bash
mvn spring-boot:run
```

## Quick Test
```bash
# Test backend health
curl http://localhost:8080/api/audit/health

# Get vulnerabilities
curl http://localhost:8080/api/vulnerabilities

# Get metrics
curl http://localhost:8080/api/metrics/system
```

## Frontend Setup
```bash
cd frontend
npm install
npm run dev
```

Frontend will be at: http://localhost:5173

## If Build Still Fails
The issue is likely Java version compatibility. Try:
```bash
# Check Java version
java -version

# Should be Java 21 or higher
# If not, install Java 21 from: https://adoptium.net/
```

## Next Steps After Successful Build
1. Start backend: `mvn spring-boot:run`
2. Start frontend: `cd frontend && npm run dev`
3. Open browser: http://localhost:5173
4. Backend API docs: http://localhost:8080/api

## Deployment
See DEPLOYMENT.md for production deployment instructions.
