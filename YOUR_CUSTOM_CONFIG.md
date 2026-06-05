# 🎯 Your Custom Configuration (Port 5433)

## ✅ Configuration Updated!

I've updated the application to use **PostgreSQL on port 5433** instead of the default 5432.

---

## 📋 Your Current Setup:

```yaml
Database Configuration:
  Host: localhost
  Port: 5433          ← Your custom port
  Database: cyberaudit_db
  Username: postgres
  Password: postgres  ← Update this with YOUR password!
```

---

## 🔧 Next Steps:

### Step 1: Update Password (IMPORTANT!)

**Open this file:** `src\main\resources\application.yml`

**Find this line (around line 21):**
```yaml
password: postgres
```

**Change it to YOUR PostgreSQL password:**
```yaml
password: your_actual_password_here
```

**Save the file!**

---

### Step 2: Test Database Connection

Open PowerShell and run:

```powershell
psql -U postgres -d cyberaudit_db -h localhost -p 5433
```

**Enter your password** when prompted.

✅ If successful, you'll see: `cyberaudit_db=#`

Type `\q` to exit.

---

### Step 3: Rebuild Application

Since we changed the configuration, rebuild the app:

```powershell
java -cp "C:\Users\kumar\AppData\Local\Temp\maven\lib\*" org.apache.maven.cli.MavenCli clean package -DskipTests
```

Or simply:

```powershell
.\mvnw.cmd clean package -DskipTests
```

⏱️ Takes ~30 seconds. Wait for "BUILD SUCCESS"

---

### Step 4: Start Backend

```powershell
java -jar target\cyberaudit-pro-1.0.0.jar
```

✅ Wait for: "Started CyberAuditApplication"

**Keep this terminal open!**

---

### Step 5: Start Frontend (New Terminal)

```powershell
cd frontend
npm run dev
```

✅ Wait for: "Local: http://localhost:5173/"

**Keep this terminal open!**

---

### Step 6: Test Everything

Open your browser: **http://localhost:5173**

Or test the API:
```powershell
curl http://localhost:8080/api/vulnerabilities
```

---

## 🔍 Verify Port Configuration

To confirm PostgreSQL is running on port 5433:

```powershell
netstat -ano | findstr :5433
```

You should see something like:
```
TCP    0.0.0.0:5433    0.0.0.0:0    LISTENING    <PID>
```

---

## 🎯 Quick Reference

### Your Connection String:
```
jdbc:postgresql://localhost:5433/cyberaudit_db
```

### Connect via psql:
```powershell
psql -U postgres -d cyberaudit_db -h localhost -p 5433
```

### Check if PostgreSQL is running:
```powershell
sc query postgresql-x64-16
```

### Start PostgreSQL (if stopped):
```powershell
net start postgresql-x64-16
```

---

## ✅ Configuration Files Updated:

- ✅ `src\main\resources\application.yml` - Port changed to 5433
- ⚠️ **YOU STILL NEED TO:** Update the password in application.yml

---

## 🆘 Troubleshooting

### Issue: "Connection refused"
**Solution:** Make sure PostgreSQL is running on port 5433:
```powershell
netstat -ano | findstr :5433
```

### Issue: "Password authentication failed"
**Solution:** 
1. Open `application.yml`
2. Update the password line with your actual PostgreSQL password
3. Rebuild: `.\mvnw.cmd clean package -DskipTests`
4. Restart backend

### Issue: "Database does not exist"
**Solution:** Create the database:
```powershell
psql -U postgres -h localhost -p 5433
CREATE DATABASE cyberaudit_db;
\q
```

---

## 🎉 You're Almost There!

**Current Status:**
- ✅ PostgreSQL installed (port 5433)
- ✅ Configuration file updated
- ⚠️ **NEXT:** Update password in application.yml
- ⚠️ **THEN:** Rebuild and run!

---

**Ready?** Follow Steps 1-6 above! 🚀
