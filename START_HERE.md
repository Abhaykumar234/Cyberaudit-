# 🚀 START HERE - CyberAudit Pro Quick Start

## ⚡ 3-Step Setup (10 minutes total)

### Step 1: Install Java 21 (5 minutes)

1. **Download Java 21:**
   - Open: https://adoptium.net/temurin/releases/?version=21
   - Select: **Windows x64 JDK .msi**
   - Download and install

2. **Verify Installation:**
   ```powershell
   # Open NEW PowerShell window
   .\INSTALL_JAVA21.ps1
   ```

   You should see:
   ```
   ✓ Found Java 21 at: C:\Program Files\Eclipse Adoptium\jdk-21.x.x
   ✓ Java 21 is ready!
   ```

### Step 2: Build Backend (3 minutes)

```powershell
# Run the build script
.\BUILD_AND_RUN.ps1
```

Or manually:
```powershell
mvn clean install -DskipTests
mvn spring-boot:run
```

**Backend will start on:** http://localhost:8080/api

### Step 3: Start Frontend (2 minutes)

Open a **NEW PowerShell window**:

```powershell
cd frontend
npm install
npm run dev
```

**Frontend will start on:** http://localhost:5173

---

## ✅ Verify Everything Works

### Test Backend:
```powershell
# Health check
curl http://localhost:8080/api/audit/health

# Get vulnerabilities
curl http://localhost:8080/api/vulnerabilities

# Get metrics
curl http://localhost:8080/api/metrics/system
```

### Test Frontend:
Open browser: http://localhost:5173

---

## 📁 Project Structure

```
cyberaudit-pro/
├── Backend (Spring Boot)
│   ├── src/main/java/com/cyberaudit/
│   │   ├── controller/     → REST API endpoints
│   │   ├── service/        → Business logic
│   │   ├── model/          → Database entities
│   │   ├── repository/     → Data access
│   │   └── config/         → Configuration
│   └── src/main/resources/
│       ├── application.yml → Main config
│       └── application-dev.yml → Dev config
│
└── Frontend (React + Vite)
    ├── src/api/            → API clients
    ├── package.json        → Dependencies
    └── vite.config.ts      → Vite config
```

---

## 🗄️ Database Options

### Option A: H2 (In-Memory - Default)
**No setup needed!** Just run:
```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Access H2 Console: http://localhost:8080/api/h2-console
- JDBC URL: `jdbc:h2:mem:cyberaudit_db`
- Username: `sa`
- Password: (blank)

### Option B: PostgreSQL (Production)

1. **Install PostgreSQL:**
   - Download: https://www.postgresql.org/download/windows/
   - Install with default settings

2. **Create Database:**
   ```sql
   CREATE DATABASE cyberaudit_db;
   CREATE USER cyberaudit_user WITH PASSWORD 'your_password';
   GRANT ALL PRIVILEGES ON DATABASE cyberaudit_db TO cyberaudit_user;
   ```

3. **Update Configuration:**
   Edit `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/cyberaudit_db
       username: cyberaudit_user
       password: your_password
   ```

4. **Run:**
   ```powershell
   mvn spring-boot:run
   ```

---

## 🌐 API Endpoints (25+)

Once backend is running, you have access to:

### Audit Management
- `POST /api/audit/generate` - Generate security audit
- `GET /api/audit/health` - Health check

### Lab Evaluation
- `POST /api/lab/evaluate` - Evaluate code for vulnerabilities
- `GET /api/lab/health` - Health check

### Vulnerabilities
- `GET /api/vulnerabilities` - List all (paginated)
- `GET /api/vulnerabilities/{id}` - Get by ID
- `GET /api/vulnerabilities/severity/{severity}` - Filter by severity
- `POST /api/vulnerabilities` - Create new
- `PUT /api/vulnerabilities/{id}` - Update

### Audit Logs
- `GET /api/logs` - List all logs
- `GET /api/logs/user/{userId}` - Filter by user
- `POST /api/logs` - Create log entry

### Metrics
- `GET /api/metrics/system` - Get system metrics

### Targets
- `GET /api/targets` - List all targets
- `POST /api/targets` - Create target
- `PUT /api/targets/{id}` - Update target

---

## 🔧 Troubleshooting

### Issue: "Java 21 not detected"
**Solution:**
```powershell
# Run the setup script
.\INSTALL_JAVA21.ps1

# Or manually set JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.x.x"
```

### Issue: "Port 8080 already in use"
**Solution:**
```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID)
taskkill /PID <PID> /F
```

### Issue: "mvn command not found"
**Solution:**
Maven is already installed in your temp directory. Use:
```powershell
mvn.cmd clean install -DskipTests
```

### Issue: "npm command not found"
**Solution:**
Install Node.js from: https://nodejs.org/

---

## 📚 Documentation

- **README.md** - Complete documentation
- **QUICKSTART.md** - 5-minute guide
- **ARCHITECTURE.md** - System design
- **DEVELOPMENT.md** - Development guidelines
- **DEPLOYMENT_READY.md** - Deployment guide

---

## 🎯 What's Included

✅ **Backend:**
- Spring Boot 3.3 with Java 21
- 25+ REST API endpoints
- PostgreSQL/H2 database support
- Spring Security with CORS
- JPA entities and repositories
- Service layer with business logic
- OWASP Top 10 vulnerability data

✅ **Frontend:**
- React 18 with TypeScript
- Vite for fast development
- Axios API clients
- CORS-enabled proxy

✅ **Database:**
- 5 JPA entities
- Auto-initialization with sample data
- H2 (dev) and PostgreSQL (prod) support

✅ **Security:**
- Spring Security configuration
- CORS enabled for frontend
- Session management
- Role-based access control ready

---

## 🚀 Next Steps

1. ✅ Install Java 21
2. ✅ Build backend: `.\BUILD_AND_RUN.ps1`
3. ✅ Start frontend: `cd frontend && npm run dev`
4. 🎉 **You're ready to develop!**

---

## 🆘 Need Help?

If you encounter any issues:

1. Check Java version: `java -version` (should be 21.x)
2. Check Maven: `mvn.cmd --version`
3. Check Node.js: `node --version`
4. Review error logs in console

---

## ⏱️ Time Breakdown

| Task | Time |
|------|------|
| Install Java 21 | 5 min |
| Build backend | 3 min |
| Start frontend | 2 min |
| **Total** | **10 min** |

---

## 🎉 Success Indicators

You'll know everything is working when:

✅ Backend console shows: `Started CyberAuditApplication`
✅ Frontend shows: `Local: http://localhost:5173/`
✅ Browser opens the application
✅ API calls return data

---

**Ready? Start with Step 1: Install Java 21!**

Download: https://adoptium.net/temurin/releases/?version=21
