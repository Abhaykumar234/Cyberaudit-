# 📦 PostgreSQL Setup Guide for CyberAudit Pro

## Step 1: Download PostgreSQL

1. **Go to:** https://www.postgresql.org/download/windows/
2. **Click:** "Download the installer"
3. **Select:** PostgreSQL 16.x (latest stable version)
4. **Download:** Windows x86-64 installer

---

## Step 2: Install PostgreSQL

1. **Run the installer** you just downloaded
2. **Follow these settings:**

   - **Installation Directory:** `C:\Program Files\PostgreSQL\16` (default is fine)
   - **Select Components:** Check ALL boxes:
     - ✅ PostgreSQL Server
     - ✅ pgAdmin 4 (GUI tool)
     - ✅ Stack Builder
     - ✅ Command Line Tools
   
   - **Data Directory:** `C:\Program Files\PostgreSQL\16\data` (default is fine)
   
   - **Password:** Enter a password for the PostgreSQL superuser (postgres)
     - **IMPORTANT:** Remember this password! 
     - **Suggestion:** Use `postgres123` for local development (easy to remember)
   
   - **Port:** `5432` (default, DO NOT CHANGE)
   
   - **Locale:** Default (your system locale)

3. **Click Next** through the remaining screens
4. **Uncheck** "Stack Builder" at the end (not needed)
5. **Click Finish**

---

## Step 3: Verify PostgreSQL Installation

Open **PowerShell** or **Command Prompt** and run:

```powershell
psql --version
```

You should see something like:
```
psql (PostgreSQL) 16.x
```

If you get "command not found", add PostgreSQL to PATH:
- Search Windows for "Environment Variables"
- Click "Environment Variables" button
- Under "System variables", find "Path"
- Click "Edit" → "New"
- Add: `C:\Program Files\PostgreSQL\16\bin`
- Click OK, restart PowerShell

---

## Step 4: Create Database and User

### Option A: Using Command Line (Recommended)

1. **Open PowerShell as Administrator**

2. **Connect to PostgreSQL:**
   ```powershell
   psql -U postgres -h localhost
   ```
   
3. **Enter the password** you set during installation

4. **Create the database:**
   ```sql
   CREATE DATABASE cyberaudit_db;
   ```

5. **Create a user (optional but recommended):**
   ```sql
   CREATE USER cyberaudit_user WITH PASSWORD 'cyberaudit123';
   ```

6. **Grant privileges:**
   ```sql
   GRANT ALL PRIVILEGES ON DATABASE cyberaudit_db TO cyberaudit_user;
   ```

7. **Connect to the new database:**
   ```sql
   \c cyberaudit_db
   ```

8. **Grant schema privileges:**
   ```sql
   GRANT ALL ON SCHEMA public TO cyberaudit_user;
   ```

9. **Exit:**
   ```sql
   \q
   ```

### Option B: Using pgAdmin 4 (GUI)

1. **Open pgAdmin 4** (installed with PostgreSQL)
2. **Enter master password** if prompted (set one if asked)
3. **Right-click "Databases"** → **Create** → **Database**
4. **Database name:** `cyberaudit_db`
5. **Owner:** postgres
6. **Click Save**

---

## Step 5: Configure CyberAudit Pro

Now that PostgreSQL is installed, you need to update the application configuration.

**Open this file:** `src\main\resources\application.yml`

**Update these lines:**

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cyberaudit_db
    username: cyberaudit_user
    password: cyberaudit123
    driver-class-name: org.postgresql.Driver
```

**If you used the postgres superuser instead:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cyberaudit_db
    username: postgres
    password: postgres123
    driver-class-name: org.postgresql.Driver
```

---

## Step 6: Test Connection

Run this command to test the connection:

```powershell
psql -U cyberaudit_user -d cyberaudit_db -h localhost
```

Or if using postgres user:
```powershell
psql -U postgres -d cyberaudit_db -h localhost
```

If successful, you'll see:
```
cyberaudit_db=#
```

Type `\q` to exit.

---

## Step 7: Rebuild and Run the Application

### Rebuild the Application:
```powershell
.\mvnw.cmd clean package -DskipTests
```

### Run the Backend:
```powershell
java -jar target\cyberaudit-pro-1.0.0.jar
```

### Run the Frontend (in a separate terminal):
```powershell
cd frontend
npm run dev
```

---

## Quick Reference

### Connection Details:
- **Host:** localhost
- **Port:** 5432
- **Database:** cyberaudit_db
- **Username:** cyberaudit_user (or postgres)
- **Password:** cyberaudit123 (or your chosen password)

### Common Commands:
```powershell
# Start PostgreSQL service (if not running)
net start postgresql-x64-16

# Stop PostgreSQL service
net stop postgresql-x64-16

# Check PostgreSQL status
sc query postgresql-x64-16

# Connect to database
psql -U postgres -d cyberaudit_db

# List all databases
\l

# List all tables
\dt

# Describe table structure
\d table_name

# Exit psql
\q
```

---

## Troubleshooting

### Issue: "psql: command not found"
**Solution:** Add PostgreSQL bin directory to PATH (see Step 3)

### Issue: "password authentication failed"
**Solution:** 
1. Open pgAdmin 4
2. Right-click server → Properties → Connection
3. Verify the password or reset it

### Issue: "could not connect to server"
**Solution:** 
1. Check if PostgreSQL service is running:
   ```powershell
   sc query postgresql-x64-16
   ```
2. Start it if stopped:
   ```powershell
   net start postgresql-x64-16
   ```

### Issue: Port 5432 already in use
**Solution:**
1. Find what's using the port:
   ```powershell
   netstat -ano | findstr :5432
   ```
2. Either change PostgreSQL port or stop the conflicting service

---

## Next Steps After Setup

Once PostgreSQL is configured and running:

1. ✅ Rebuild the application
2. ✅ Start the backend (will automatically create tables)
3. ✅ Start the frontend
4. ✅ Open browser: http://localhost:5173
5. ✅ Test the API: http://localhost:8080/api/vulnerabilities

The application will automatically:
- Create all database tables
- Insert sample vulnerability data
- Insert sample target data
- Be ready to use!

---

## 🎉 You're Done!

After completing these steps, your CyberAudit Pro application will be running with a proper PostgreSQL database!

**Need help?** Check the error messages and refer to the Troubleshooting section above.
