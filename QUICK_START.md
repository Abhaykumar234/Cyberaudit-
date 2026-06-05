# 🚀 CyberAudit Pro - Quick Start

## Prerequisites Checklist

Before you start, make sure you have:
- [ ] Java 21 installed and working (`java -version`)
- [ ] PostgreSQL installed and running
- [ ] Node.js installed (`node --version`)
- [ ] Database created: `cyberaudit_db`

---

## 1️⃣ Configure Database (First Time Only)

**Edit:** `src\main\resources\application.yml`

Find this section and update with YOUR password:

```yaml
datasource:
  url: jdbc:postgresql://localhost:5432/cyberaudit_db
  username: postgres
  password: YOUR_PASSWORD_HERE  # ← Change this!
```

---

## 2️⃣ Build the Application

```powershell
.\mvnw.cmd clean package -DskipTests
```

⏱️ Takes ~30 seconds

---

## 3️⃣ Start Backend

```powershell
java -jar target\cyberaudit-pro-1.0.0.jar
```

✅ Backend ready when you see: "Started CyberAuditApplication"  
🌐 Backend URL: http://localhost:8080/api

**Keep this terminal open!**

---

## 4️⃣ Start Frontend (New Terminal)

```powershell
cd frontend
npm install  # Only first time
npm run dev
```

✅ Frontend ready when you see: "Local: http://localhost:5173/"  
🌐 Frontend URL: http://localhost:5173

**Keep this terminal open!**

---

## 5️⃣ Test It!

### Option A: Browser
Open: http://localhost:5173

### Option B: Test API
```powershell
curl http://localhost:8080/api/vulnerabilities
```

---

## Common Issues & Quick Fixes

### ❌ "Port 8080 already in use"
```powershell
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### ❌ "Port 5173 already in use"
```powershell
netstat -ano | findstr :5173
taskkill /PID <PID> /F
```

### ❌ "Password authentication failed"
Update the password in `application.yml` to match your PostgreSQL password

### ❌ "JAVA_HOME error"
Your Java 21 might not be set correctly. Run:
```powershell
java -version
```
Should show Java 21, not 25.

---

## Stop Everything

**Stop Backend:** Press `Ctrl+C` in backend terminal  
**Stop Frontend:** Press `Ctrl+C` in frontend terminal

---

## Architecture Overview

```
┌─────────────────────────────────────────────────┐
│  Frontend (React + Vite)                        │
│  http://localhost:5173                          │
└────────────────┬────────────────────────────────┘
                 │ API Calls
                 ▼
┌─────────────────────────────────────────────────┐
│  Backend (Spring Boot)                          │
│  http://localhost:8080/api                      │
└────────────────┬────────────────────────────────┘
                 │ JDBC
                 ▼
┌─────────────────────────────────────────────────┐
│  PostgreSQL Database                            │
│  localhost:5432/cyberaudit_db                   │
└─────────────────────────────────────────────────┘
```

---

## API Endpoints (25+)

### Vulnerabilities
- `GET /api/vulnerabilities` - List all
- `GET /api/vulnerabilities/{id}` - Get by ID
- `GET /api/vulnerabilities/severity/{severity}` - Filter by severity
- `POST /api/vulnerabilities` - Create new
- `PUT /api/vulnerabilities/{id}` - Update

### Audit
- `POST /api/audit/generate` - Generate audit
- `GET /api/audit/health` - Health check

### Lab
- `POST /api/lab/evaluate` - Evaluate code
- `GET /api/lab/health` - Health check

### Logs
- `GET /api/logs` - List all logs
- `GET /api/logs/user/{userId}` - By user
- `POST /api/logs` - Create log

### Metrics
- `GET /api/metrics/system` - System metrics

### Targets
- `GET /api/targets` - List all
- `POST /api/targets` - Create
- `PUT /api/targets/{id}` - Update

---

## Project Structure

```
Cyberaudit Pro/
├── src/main/java/com/cyberaudit/     # Backend code
│   ├── controller/                    # REST endpoints
│   ├── service/                       # Business logic
│   ├── model/                         # Database entities
│   ├── repository/                    # Data access
│   ├── dto/                          # Request/Response objects
│   └── config/                        # Configuration
├── src/main/resources/
│   ├── application.yml               # Main config
│   └── application-dev.yml           # Dev config (H2)
├── frontend/
│   ├── src/api/                      # API clients
│   ├── package.json                   # Dependencies
│   └── vite.config.ts                # Vite config
├── target/
│   └── cyberaudit-pro-1.0.0.jar      # Executable JAR
└── pom.xml                            # Maven config
```

---

## Development Workflow

### Make Changes to Backend:
1. Edit Java files in `src/main/java/`
2. Rebuild: `.\mvnw.cmd clean package -DskipTests`
3. Restart backend: `java -jar target\cyberaudit-pro-1.0.0.jar`

### Make Changes to Frontend:
1. Edit files in `frontend/src/`
2. Vite auto-reloads (no restart needed!)
3. Just refresh your browser

---

## Access Database Directly

### Using psql:
```powershell
psql -U postgres -d cyberaudit_db
```

### Using pgAdmin:
1. Open pgAdmin 4
2. Expand Servers → PostgreSQL 16 → Databases → cyberaudit_db
3. Right-click cyberaudit_db → Query Tool

### Useful SQL Queries:
```sql
-- List all vulnerabilities
SELECT * FROM vulnerabilities;

-- Count vulnerabilities by severity
SELECT severity, COUNT(*) FROM vulnerabilities GROUP BY severity;

-- List all targets
SELECT * FROM simulated_targets;

-- Check audit logs
SELECT * FROM audit_logs ORDER BY timestamp DESC LIMIT 10;
```

---

## 🎯 What's Included

✅ **32 Java classes** - Complete backend
✅ **25+ REST APIs** - Full CRUD operations  
✅ **PostgreSQL** - Production database
✅ **React + Vite** - Modern frontend
✅ **Spring Security** - Authentication ready
✅ **OWASP Top 10** - Pre-loaded vulnerability data
✅ **Sample targets** - Ready to test
✅ **CORS enabled** - Frontend ↔ Backend communication
✅ **Auto-initialization** - Tables created automatically

---

## 📚 Documentation Files

- **POSTGRESQL_SETUP_GUIDE.md** ← Read this first!
- **QUICK_START.md** ← You are here
- **START_HERE.md** - Detailed setup guide
- **README.md** - Complete documentation
- **ARCHITECTURE.md** - System design
- **DEVELOPMENT.md** - Development guidelines

---

## 🆘 Need More Help?

1. **PostgreSQL setup:** Read `POSTGRESQL_SETUP_GUIDE.md`
2. **Detailed guide:** Read `START_HERE.md`
3. **Full documentation:** Read `README.md`
4. **Check logs:** Look at terminal output for errors

---

**Ready to start? Follow the 5 steps above! 🚀**
