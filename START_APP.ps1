# Start CyberAudit Pro - Complete Startup Script
# Run this script to start both backend and frontend

Write-Host @"
╔════════════════════════════════════════════════════════════════╗
║                  CyberAudit Pro Startup                        ║
║                     Version 1.0.0                              ║
╚════════════════════════════════════════════════════════════════╝
"@ -ForegroundColor Cyan

Write-Host ""

# Step 1: Check Prerequisites
Write-Host "[1/5] Checking Prerequisites..." -ForegroundColor Yellow
Write-Host ""

# Check Java 21
$javaVersion = & java -version 2>&1 | Select-String "version" | Select-Object -First 1
if ($javaVersion -match "21\.") {
    Write-Host "  ✓ Java 21 detected" -ForegroundColor Green
} else {
    Write-Host "  ✗ Java 21 required! Current version: $javaVersion" -ForegroundColor Red
    exit 1
}

# Check Node.js
try {
    $nodeVersion = & node -v 2>&1
    Write-Host "  ✓ Node.js $nodeVersion detected" -ForegroundColor Green
} catch {
    Write-Host "  ✗ Node.js not found!" -ForegroundColor Red
    exit 1
}

# Check PostgreSQL
Write-Host "  ℹ Checking PostgreSQL on port 5433..." -ForegroundColor Cyan
$pgRunning = Test-NetConnection -ComputerName localhost -Port 5433 -WarningAction SilentlyContinue -ErrorAction SilentlyContinue
if ($pgRunning.TcpTestSucceeded) {
    Write-Host "  ✓ PostgreSQL accessible on port 5433" -ForegroundColor Green
} else {
    Write-Host "  ⚠ PostgreSQL not detected on port 5433" -ForegroundColor Yellow
    Write-Host "    Make sure PostgreSQL is running!" -ForegroundColor Yellow
}

Write-Host ""

# Step 2: Check if backend JAR exists
Write-Host "[2/5] Checking Backend Build..." -ForegroundColor Yellow
if (Test-Path "target\cyberaudit-pro-1.0.0.jar") {
    Write-Host "  ✓ Backend JAR found" -ForegroundColor Green
} else {
    Write-Host "  ✗ Backend JAR not found!" -ForegroundColor Red
    Write-Host "    Run: .\BUILD_WITH_JAVA21.ps1" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# Step 3: Check frontend dependencies
Write-Host "[3/5] Checking Frontend Dependencies..." -ForegroundColor Yellow
if (Test-Path "frontend\node_modules") {
    Write-Host "  ✓ Frontend dependencies installed" -ForegroundColor Green
} else {
    Write-Host "  ℹ Installing frontend dependencies..." -ForegroundColor Cyan
    Push-Location frontend
    npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  ✗ Failed to install dependencies!" -ForegroundColor Red
        Pop-Location
        exit 1
    }
    Pop-Location
    Write-Host "  ✓ Frontend dependencies installed" -ForegroundColor Green
}

Write-Host ""

# Step 4: Start Backend
Write-Host "[4/5] Starting Backend Server..." -ForegroundColor Yellow
Write-Host "  ℹ Backend will run on http://localhost:8080/api" -ForegroundColor Cyan
Write-Host ""

$backendJob = Start-Job -ScriptBlock {
    Set-Location $using:PWD
    java -jar target\cyberaudit-pro-1.0.0.jar
}

Write-Host "  ℹ Waiting for backend to start (30 seconds)..." -ForegroundColor Cyan
Start-Sleep -Seconds 30

# Check if backend is running
$backendRunning = $false
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/metrics/system" -TimeoutSec 5 -UseBasicParsing -ErrorAction Stop
    $backendRunning = $true
    Write-Host "  ✓ Backend server is running!" -ForegroundColor Green
} catch {
    Write-Host "  ⚠ Backend may still be starting..." -ForegroundColor Yellow
}

Write-Host ""

# Step 5: Start Frontend
Write-Host "[5/5] Starting Frontend Server..." -ForegroundColor Yellow
Write-Host "  ℹ Frontend will run on http://localhost:5173" -ForegroundColor Cyan
Write-Host ""

Push-Location frontend
$frontendJob = Start-Job -ScriptBlock {
    Set-Location $using:PWD
    npm run dev
}
Pop-Location

Write-Host "  ℹ Waiting for frontend to start (10 seconds)..." -ForegroundColor Cyan
Start-Sleep -Seconds 10

Write-Host "  ✓ Frontend server started!" -ForegroundColor Green
Write-Host ""

# Final Instructions
Write-Host @"
╔════════════════════════════════════════════════════════════════╗
║                    🚀 STARTUP COMPLETE                         ║
╚════════════════════════════════════════════════════════════════╝
"@ -ForegroundColor Green

Write-Host ""
Write-Host "Application is running:" -ForegroundColor Cyan
Write-Host "  Frontend:  http://localhost:5173" -ForegroundColor White
Write-Host "  Backend:   http://localhost:8080/api" -ForegroundColor White
Write-Host ""
Write-Host "Job IDs:" -ForegroundColor Cyan
Write-Host "  Backend Job:  $($backendJob.Id)" -ForegroundColor White
Write-Host "  Frontend Job: $($frontendJob.Id)" -ForegroundColor White
Write-Host ""
Write-Host "To stop servers:" -ForegroundColor Yellow
Write-Host "  Stop-Job $($backendJob.Id) ; Remove-Job $($backendJob.Id)" -ForegroundColor White
Write-Host "  Stop-Job $($frontendJob.Id) ; Remove-Job $($frontendJob.Id)" -ForegroundColor White
Write-Host ""
Write-Host "Or press Ctrl+C and run:" -ForegroundColor Yellow
Write-Host "  Get-Job | Stop-Job ; Get-Job | Remove-Job" -ForegroundColor White
Write-Host ""
Write-Host "Opening browser in 5 seconds..." -ForegroundColor Cyan
Start-Sleep -Seconds 5
Start-Process "http://localhost:5173"

Write-Host ""
Write-Host "Press Ctrl+C to stop this script (servers will continue running)" -ForegroundColor Yellow
Write-Host "Use the Stop-Job commands above to stop the servers" -ForegroundColor Yellow
Write-Host ""

# Keep script running and show logs
Write-Host "=== Backend Logs ===" -ForegroundColor Cyan
Receive-Job -Job $backendJob -Keep
Write-Host ""
Write-Host "=== Frontend Logs ===" -ForegroundColor Cyan
Receive-Job -Job $frontendJob -Keep

# Wait indefinitely
while ($true) {
    Start-Sleep -Seconds 5
    Receive-Job -Job $backendJob -Keep
    Receive-Job -Job $frontendJob -Keep
}
