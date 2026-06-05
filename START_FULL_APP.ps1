# ===================================================================
# CyberAudit Pro - Complete Application Startup Script
# ===================================================================
# This script rebuilds the backend with the new real-time scanner
# and starts both backend and frontend servers.
# ===================================================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  CyberAudit Pro - Full Application" -ForegroundColor Cyan
Write-Host "  Real-Time Vulnerability Scanner" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Set Java 21 path (Fixed - correct path found)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "[1/5] Verifying Java 21..." -ForegroundColor Yellow
java -version
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Java 21 not found!" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Java 21 detected" -ForegroundColor Green
Write-Host ""

Write-Host "[2/5] Verifying PostgreSQL..." -ForegroundColor Yellow
try {
    $pgProcess = Get-Process -Name postgres -ErrorAction SilentlyContinue
    if ($pgProcess) {
        Write-Host "✓ PostgreSQL is running" -ForegroundColor Green
    } else {
        Write-Host "WARNING: PostgreSQL process not detected" -ForegroundColor Yellow
        Write-Host "Attempting to connect anyway..." -ForegroundColor Yellow
    }
} catch {
    Write-Host "Cannot verify PostgreSQL status" -ForegroundColor Yellow
}
Write-Host ""

Write-Host "[3/5] Building Backend with Real-Time Scanner..." -ForegroundColor Yellow
Write-Host "This will compile all Java classes including:" -ForegroundColor Cyan
Write-Host "  - VulnerabilityScanner.java (Port scanning, SSL checks, headers)" -ForegroundColor Cyan
Write-Host "  - RealTimeScanService.java (Async scanning service)" -ForegroundColor Cyan
Write-Host "  - ScanController.java (REST API endpoints)" -ForegroundColor Cyan
Write-Host ""

.\mvnw.cmd clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "ERROR: Backend build failed!" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Troubleshooting:" -ForegroundColor Yellow
    Write-Host "1. Check Java version: java -version" -ForegroundColor Cyan
    Write-Host "2. Review compilation errors above" -ForegroundColor Cyan
    Write-Host "3. Try: .\mvnw.cmd clean compile" -ForegroundColor Cyan
    exit 1
}

Write-Host ""
Write-Host "✓ Backend built successfully!" -ForegroundColor Green
Write-Host ""

Write-Host "[4/5] Starting Backend Server..." -ForegroundColor Yellow
$jarFile = "target\cyberaudit-pro-1.0.0-latest.jar"

if (!(Test-Path $jarFile)) {
    Write-Host "ERROR: JAR file not found at $jarFile" -ForegroundColor Red
    exit 1
}

Write-Host "Starting backend on port 8080..." -ForegroundColor Cyan
Start-Process -FilePath "java" -ArgumentList "-jar", $jarFile -NoNewWindow

Write-Host "Waiting for backend to initialize (15 seconds)..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

Write-Host "✓ Backend started" -ForegroundColor Green
Write-Host ""

Write-Host "[5/5] Starting Frontend Development Server..." -ForegroundColor Yellow
Set-Location frontend

Write-Host "Installing frontend dependencies..." -ForegroundColor Cyan
npm install

Write-Host ""
Write-Host "Starting Vite dev server on port 5173..." -ForegroundColor Cyan
Start-Process -FilePath "npm" -ArgumentList "run", "dev" -NoNewWindow

Set-Location ..

Start-Sleep -Seconds 5

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "  APPLICATION READY!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "URLS:" -ForegroundColor Cyan
Write-Host "  Frontend: http://localhost:5173" -ForegroundColor White
Write-Host "  Backend:  http://localhost:8080/api" -ForegroundColor White
Write-Host ""
Write-Host "CREDENTIALS:" -ForegroundColor Cyan
Write-Host "  Username: admin" -ForegroundColor White
Write-Host "  Password: admin123" -ForegroundColor White
Write-Host ""
Write-Host "FEATURES:" -ForegroundColor Cyan
Write-Host "  ✓ Dashboard with metrics" -ForegroundColor White
Write-Host "  ✓ Vulnerability management" -ForegroundColor White
Write-Host "  ✓ Target management" -ForegroundColor White
Write-Host "  ✓ Audit logs" -ForegroundColor White
Write-Host "  ✓ REAL-TIME VULNERABILITY SCANNER" -ForegroundColor Yellow
Write-Host ""
Write-Host "TEST THE SCANNER:" -ForegroundColor Cyan
Write-Host "  1. Login at http://localhost:5173" -ForegroundColor White
Write-Host "  2. Navigate to 'Real-Time Scanner'" -ForegroundColor White
Write-Host "  3. Enter a target URL (e.g., https://example.com)" -ForegroundColor White
Write-Host "  4. Click 'Start Scan' and watch real results!" -ForegroundColor White
Write-Host ""
Write-Host "The scanner performs:" -ForegroundColor Yellow
Write-Host "  • Port scanning (FTP, SSH, HTTP, HTTPS, databases, etc.)" -ForegroundColor White
Write-Host "  • Security header validation" -ForegroundColor White
Write-Host "  • SSL/TLS configuration checks" -ForegroundColor White
Write-Host "  • Common vulnerability detection" -ForegroundColor White
Write-Host "  • Exposed path discovery" -ForegroundColor White
Write-Host ""
Write-Host "Press Ctrl+C to stop both servers" -ForegroundColor Gray
Write-Host ""

# Keep script running
Wait-Event
