# 🚀 QUICK START - Run CyberAudit Pro NOW

## ✅ All Issues Fixed!

**What was fixed:**
1. ✅ All 6 API services now use `/api` (proxy-enabled)
2. ✅ Database credentials corrected to `cyberaudit_user/cyberaudit123`
3. ✅ Vulnerabilities page import error fixed
4. ✅ No TypeScript errors remaining

---

## 🏃‍♂️ Quick Start (3 Steps)

### STEP 1: Rebuild Backend with Correct Database Credentials
```powershell
cd "c:\Users\kumar\Downloads\Cyberaudit Pro"
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
.\mvnw.cmd clean package -DskipTests
```

**Expected:** `BUILD SUCCESS` + JAR file created

---

### STEP 2: Install Frontend Dependencies (if not done)
```powershell
cd "c:\Users\kumar\Downloads\Cyberaudit Pro\frontend"
npm install
```

---

### STEP 3: Start Both Servers

**Terminal 1 - Backend:**
```powershell
cd "c:\Users\kumar\Downloads\Cyberaudit Pro"
java -jar target\cyberaudit-pro-1.0.0.jar
```

**Terminal 2 - Frontend:**
```powershell
cd "c:\Users\kumar\Downloads\Cyberaudit Pro\frontend"
npm run dev
```

**Browser:**
Open `http://localhost:5173`

---

## ✅ Quick Test Checklist

1. ✅ Dashboard loads with metrics
2. ✅ Vulnerabilities page shows 10 OWASP items
3. ✅ Targets page shows 3 targets
4. ✅ No CORS errors in console (F12)
5. ✅ No 404 errors in Network tab

---

## 🐛 If Something Goes Wrong

### Backend won't start
- Make sure PostgreSQL is running on port 5433
- Test: `psql -U cyberaudit_user -d cyberaudit_db -h localhost -p 5433`
- Password: `cyberaudit123`

### Frontend shows errors
- Check both terminals are running
- Press F12 in browser → Check Console for errors
- Check Network tab for failed API calls

### API calls fail
- Backend must be running FIRST
- Test backend directly: `curl http://localhost:8080/api/metrics/system`
- Should return JSON data

---

## 📖 Full Documentation

Read these for detailed info:
- `RUN_LOCALLY.md` - Complete step-by-step guide
- `FIXES_APPLIED.md` - All fixes applied in detail
- `POSTGRESQL_SETUP_GUIDE.md` - Database setup

---

**YOU'RE READY TO GO!** 🎉

Just run the 3 steps above and your application will be live!
