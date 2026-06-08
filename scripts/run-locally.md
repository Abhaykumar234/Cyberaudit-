# 🚀 Run CyberAudit Pro Locally - Complete Guide

## Prerequisites Checklist
- ✅ Java 21 installed (`java -version` shows 21.x.x)
- ✅ Maven installed (`mvn -version`)
- ✅ Node.js installed (`node -v` shows v16+)
- ✅ PostgreSQL running on port 5433
- ✅ Database `cyberaudit_db` created
- ✅ User `cyberaudit_user` with password `cyberaudit123` configured

---

## 🔧 STEP 1: Rebuild Backend (Required - Database Credentials Updated)

Open PowerShell in project root and run:

```powershell
cd "c:\Users\kumar\Downloads\Cyberaudit Pro"
mvn clean package -DskipTests
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] cyberaudit-pro-1.0.0.jar created
```

**If Build Fails:**
- Verify Java 21: `java -version`
- Check JAVA_HOME: `echo $env:JAVA_HOME`
- Try: `mvn clean install -DskipTests -U`

---

## 🎨 STEP 2: Install Frontend Dependencies

Open PowerShell and run:

```powershell
cd "c:\Users\kumar\Downloads\Cyberaudit Pro\frontend"
npm install
```

**Expected Output:**
```
added 500+ packages in 30s
```

**This installs:**
- React 18.2.0
- React Router 6.20.0
- Axios 1.6.0
- Tailwind CSS 3.4.0
- Vite 5.0.0
- TypeScript 5.3.0

---

## 🗄️ STEP 3: Verify PostgreSQL

Open PowerShell and test connection:

```powershell
psql -U cyberaudit_user -d cyberaudit_db -h localhost -p 5433
```

**Enter password:** `cyberaudit123`

**Expected:** You should connect successfully to the database.

**If Connection Fails:**
```powershell
# Check if PostgreSQL is running
Get-Service -Name postgresql*

# If not running, start it:
Start-Service -Name postgresql-x64-15  # or your version
```

---

## 🚀 STEP 4: Start Backend Server

Open **Terminal 1** (PowerShell):

```powershell
cd "c:\Users\kumar\Downloads\Cyberaudit Pro"
java -jar target\cyberaudit-pro-1.0.0.jar
```

**Expected Output:**
```
  ____      _                 _             _ _ _   
 / ___|   _| |__   ___ _ __  / \  _   _  __| (_) |_ 
| |  | | | | '_ \ / _ \ '__|/ _ \| | | |/ _` | | __|
| |__| |_| | |_) |  __/ |  / ___ \ |_| | (_| | | |_ 
 \____\__, |_.__/ \___|_| /_/   \_\__,_|\__,_|_|\__|
      |___/                                          

CyberAudit Pro v1.0.0

Started CyberAuditApplication in 5.234 seconds
Tomcat started on port(s): 8080 (http) with context path '/api'
```

**Backend will be available at:** `http://localhost:8080/api`

**Key Backend Endpoints:**
- `/api/metrics/system` - System metrics
- `/api/vulnerabilities` - OWASP Top 10 vulnerabilities
- `/api/targets` - Security targets
- `/api/logs` - Audit logs
- `/api/lab/evaluate` - Code evaluation

**Leave this terminal running!**

---

## 🎨 STEP 5: Start Frontend Dev Server

Open **Terminal 2** (PowerShell) - Keep Terminal 1 running:

```powershell
cd "c:\Users\kumar\Downloads\Cyberaudit Pro\frontend"
npm run dev
```

**Expected Output:**
```
VITE v5.0.0  ready in 500 ms

➜  Local:   http://localhost:5173/
➜  Network: use --host to expose
```

**Frontend will be available at:** `http://localhost:5173`

**Leave this terminal running!**

---

## 🌐 STEP 6: Open Application in Browser

Open your web browser and navigate to:

```
http://localhost:5173
```

**You should see:**
- **Dashboard** - System metrics with real-time data
- **Vulnerabilities** - OWASP Top 10 vulnerability catalog
- **Labs** - Secure code evaluator
- **Targets** - Security targets management
- **Audit Logs** - Activity audit trail

---

## 🧪 Testing Frontend-Backend Integration

### Test 1: Dashboard Loads Metrics
1. Click **Dashboard** in sidebar
2. You should see metrics cards with:
   - Posture Score (0-100)
   - Active Findings count
   - Critical/High counts
   - Remediation Rate

**If metrics don't load:**
- Check Terminal 1 (backend) for errors
- Open browser DevTools (F12) → Console tab
- Check Network tab for failed API calls

### Test 2: Vulnerabilities List
1. Click **Vulnerabilities** in sidebar
2. You should see OWASP Top 10 vulnerabilities:
   - SQL Injection
   - XSS (Cross-Site Scripting)
   - CSRF (Cross-Site Request Forgery)
   - etc.

**Expected:** 10 vulnerability cards with severity badges

### Test 3: Targets Management
1. Click **Targets** in sidebar
2. You should see 3 pre-loaded targets:
   - Production API Gateway
   - Dev Environment
   - Staging Cluster

**Expected:** Target cards with risk scores

### Test 4: Labs Evaluator
1. Click **Labs** in sidebar
2. Enter test code snippet:
   ```python
   query = "SELECT * FROM users WHERE id=" + user_input
   ```
3. Click **Evaluate Code**
4. You should get AI-powered analysis with vulnerability detection

### Test 5: Audit Logs
1. Click **Audit Logs** in sidebar
2. You should see activity logs
3. Try filtering by severity (CRITICAL, HIGH, etc.)

---

## 🔧 Configuration Summary

### Backend Configuration
- **Port:** 8080
- **Context Path:** `/api`
- **Database:** PostgreSQL on port 5433
- **Database Name:** `cyberaudit_db`
- **DB User:** `cyberaudit_user`
- **DB Password:** `cyberaudit123`
- **Security:** CORS enabled for `localhost:5173`

### Frontend Configuration
- **Port:** 5173
- **Framework:** React 18 + TypeScript
- **Routing:** React Router v6
- **Styling:** Tailwind CSS
- **HTTP Client:** Axios with proxy
- **Proxy:** All `/api` requests → `http://localhost:8080`

### Proxy Configuration
The Vite proxy forwards all `/api` requests to the backend:
```
Frontend: http://localhost:5173/api/metrics/system
    ↓ (proxied to)
Backend:  http://localhost:8080/api/metrics/system
```

---

## 🐛 Troubleshooting

### Backend Won't Start

**Problem:** Port 8080 already in use
```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual number)
taskkill /PID <PID> /F
```

**Problem:** Database connection error
- Verify PostgreSQL is running on port 5433
- Check credentials in `src/main/resources/application.yml`
- Test connection: `psql -U cyberaudit_user -d cyberaudit_db -h localhost -p 5433`

### Frontend Won't Start

**Problem:** Port 5173 already in use
- Close other Vite dev servers
- Or change port in `frontend/vite.config.ts`

**Problem:** Dependencies not installed
```powershell
cd frontend
rm -r node_modules
rm package-lock.json
npm install
```

### API Calls Fail (CORS/Proxy Issues)

**Problem:** API calls return 404 or CORS errors

**Check 1 - Backend is running:**
```powershell
# Test backend directly
curl http://localhost:8080/api/metrics/system
```

**Check 2 - Proxy configuration:**
Open `frontend/vite.config.ts` and verify:
```typescript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
    secure: false
  }
}
```

**Check 3 - API base URLs:**
All API files should use `const API_BASE_URL = '/api'` (not `http://localhost:8080/api`)

**Check 4 - Browser DevTools:**
- Open F12 → Network tab
- Look for failed requests
- Check request URL and response

### No Data Displayed

**Problem:** Components load but show no data

**Check backend logs (Terminal 1):**
- Look for SQL errors
- Check if DataInitializer ran successfully
- Verify tables were created

**Manually check database:**
```sql
psql -U cyberaudit_user -d cyberaudit_db -h localhost -p 5433

-- Check if tables exist
\dt

-- Check if data was loaded
SELECT COUNT(*) FROM vulnerabilities;
SELECT COUNT(*) FROM simulated_targets;
```

---

## 📊 Sample Data Loaded on Startup

The application auto-loads:

1. **10 OWASP Top 10 Vulnerabilities:**
   - SQL Injection
   - XSS (Stored & Reflected)
   - CSRF
   - Authentication Bypass
   - Sensitive Data Exposure
   - XXE (XML External Entity)
   - Broken Access Control
   - Security Misconfiguration
   - Insecure Deserialization

2. **3 Simulated Targets:**
   - Production API Gateway (risk: 85/100)
   - Development Environment (risk: 45/100)
   - Staging Cluster (risk: 60/100)

---

## 🎯 Next Steps

1. ✅ Verify both servers are running
2. ✅ Test all 5 pages in the frontend
3. ✅ Check that data loads from backend
4. ✅ Test Labs feature with code evaluation
5. ✅ Monitor logs in both terminals for errors

---

## 📝 Quick Reference

| Component | URL | Port |
|-----------|-----|------|
| Frontend | http://localhost:5173 | 5173 |
| Backend API | http://localhost:8080/api | 8080 |
| PostgreSQL | localhost:5433 | 5433 |

| Terminal | Command | Purpose |
|----------|---------|---------|
| Terminal 1 | `java -jar target\cyberaudit-pro-1.0.0.jar` | Backend server |
| Terminal 2 | `npm run dev` (in frontend/) | Frontend dev server |

---

## ✅ Success Criteria

Your application is running correctly when:
- ✅ Terminal 1 shows "Started CyberAuditApplication"
- ✅ Terminal 2 shows "ready in Xms"
- ✅ Browser loads http://localhost:5173
- ✅ Dashboard displays metrics
- ✅ Vulnerabilities page shows OWASP Top 10
- ✅ Targets page shows 3 targets
- ✅ No CORS errors in browser console
- ✅ No 404 errors for API calls

---

**Need Help?** Check logs in both terminals for error messages!
