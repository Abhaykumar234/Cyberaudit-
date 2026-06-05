# 🔧 FIXED: Database Connection Issue

## Problem Identified ✅
The JAR file you're running was built with **OLD database credentials**. The error shows:
```
FATAL: password authentication failed for user "postgres"
```

But we need it to connect as `cyberaudit_user` with password `cyberaudit123` on port `5433`.

---

## Solution: Use External Configuration File ✅

I've created an **external `application.yml`** file in your project root that will **override** the configuration inside the JAR. This way you don't need to rebuild!

---

## 🚀 How to Run NOW (2 Steps)

### STEP 1: Test Database Connection
```powershell
.\TEST_DATABASE.ps1
```

**Expected Output:**
```
✓ Port 5433 is OPEN - PostgreSQL is listening
✓ Successfully connected to database!
```

**If this fails**, you need to fix your PostgreSQL setup first.

---

### STEP 2: Start Backend with External Config
```powershell
.\START_BACKEND.ps1
```

This script will:
1. ✅ Check if external `application.yml` exists
2. ✅ Start backend with correct database credentials
3. ✅ Override the old config inside the JAR

**Expected Output:**
```
Started CyberAuditApplication in X.XXX seconds
Tomcat started on port(s): 8080 (http) with context path '/api'
```

---

### STEP 3: Start Frontend (Separate Terminal)
```powershell
cd frontend
npm run dev
```

Then open: **http://localhost:5173**

---

## 🔍 What Was Fixed

### 1. Created External Configuration
**File:** `application.yml` (in project root)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/cyberaudit_db
    username: cyberaudit_user
    password: cyberaudit123
```

### 2. Updated Internal Configuration
**File:** `src/main/resources/application.yml`
- Also updated with correct credentials
- But JAR was already built with old config

### 3. Created Helper Scripts
- ✅ `TEST_DATABASE.ps1` - Test PostgreSQL connection
- ✅ `START_BACKEND.ps1` - Start with external config
- ✅ `BUILD_WITH_JAVA21.ps1` - For future rebuilds

---

## 🐛 If Database Test Fails

### Issue 1: Port 5433 Not Open
```powershell
# Check PostgreSQL service
Get-Service postgresql*

# Start if stopped
Start-Service postgresql-x64-15
```

### Issue 2: Database Doesn't Exist
```powershell
# Connect as postgres superuser
psql -U postgres -h localhost -p 5433

# Create database and user
CREATE DATABASE cyberaudit_db;
CREATE USER cyberaudit_user WITH PASSWORD 'cyberaudit123';
GRANT ALL PRIVILEGES ON DATABASE cyberaudit_db TO cyberaudit_user;
\c cyberaudit_db
GRANT ALL ON SCHEMA public TO cyberaudit_user;
\q
```

### Issue 3: Wrong PostgreSQL Port
If PostgreSQL is on port **5432** (not 5433), edit `application.yml`:
```yaml
datasource:
  url: jdbc:postgresql://localhost:5432/cyberaudit_db
```

---

## 🎯 Quick Command Reference

```powershell
# Test database
.\TEST_DATABASE.ps1

# Start backend (with external config)
.\START_BACKEND.ps1

# Start frontend (in separate terminal)
cd frontend
npm run dev

# Check backend is running
curl http://localhost:8080/api/metrics/system

# View logs
# Just watch the terminal output!
```

---

## 📋 Files Created

| File | Purpose |
|------|---------|
| `application.yml` | External config with correct DB credentials |
| `TEST_DATABASE.ps1` | Test PostgreSQL connection |
| `START_BACKEND.ps1` | Start backend with external config |
| `BUILD_WITH_JAVA21.ps1` | Build script for future |
| `FIX_AND_RUN.md` | This file |

---

## 🔄 Alternative: Rebuild JAR (If You Want)

If the Maven wrapper isn't working, you have options:

### Option A: Install Maven
```powershell
# Using Chocolatey
choco install maven

# Then build
mvn clean package -DskipTests
```

### Option B: Fix Maven Wrapper
The `mvnw.cmd` script has issues with JAVA_HOME. Edit it or use external config instead (recommended).

### Option C: Use External Config (Current Solution)
This is the **easiest** and **works immediately** without rebuilding!

---

## ✅ Summary

**What Changed:**
1. ✅ Created external `application.yml` with correct credentials
2. ✅ External config overrides internal config in JAR
3. ✅ No rebuild needed!

**To Run:**
1. `.\TEST_DATABASE.ps1` - Verify PostgreSQL
2. `.\START_BACKEND.ps1` - Start backend
3. `cd frontend; npm run dev` - Start frontend
4. Open `http://localhost:5173`

**That's it!** 🎉
