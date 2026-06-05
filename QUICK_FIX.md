# QUICK FIX - Java 25 Compatibility Issue

## Problem
Lombok doesn't support Java 25. You're using Java 25.0.3.

## Solution Options

### Option 1: Downgrade to Java 21 (RECOMMENDED - 5 minutes)
1. Download Java 21 from: https://adoptium.net/temurin/releases/?version=21
2. Install it
3. Set JAVA_HOME:
```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.x.x-hotspot"
```
4. Build:
```bash
mvn clean install -DskipTests
```

### Option 2: Remove Lombok (TAKES LONGER - 30 minutes)
I'll need to rewrite all 30+ files to remove Lombok annotations and add getters/setters manually.

## RECOMMENDED: Use Java 21

Your current Java: **25.0.3**
Required: **21.x.x**

Download link: https://adoptium.net/temurin/releases/?version=21

After installing Java 21:
```powershell
# Set JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot"

# Verify
java -version
# Should show: openjdk version "21.0.x"

# Build
mvn clean install -DskipTests

# Run
mvn spring-boot:run
```

## Why Java 21?
- Spring Boot 3.3 is optimized for Java 21
- Lombok fully supports Java 21
- All dependencies are tested with Java 21
- Production-ready and stable

## After Fixing
1. Backend will start on: http://localhost:8080/api
2. Frontend: `cd frontend && npm install && npm run dev`
3. Frontend will be on: http://localhost:5173

## Need Help?
If you can't install Java 21, let me know and I'll remove Lombok from all files (will take 30 minutes).
