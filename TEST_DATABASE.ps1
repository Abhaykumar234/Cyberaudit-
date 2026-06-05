# Test PostgreSQL Connection
Write-Host "=== Testing PostgreSQL Connection ===" -ForegroundColor Cyan
Write-Host ""

# Test 1: Check if PostgreSQL is running on port 5433
Write-Host "[1] Testing if port 5433 is open..." -ForegroundColor Yellow
$portTest = Test-NetConnection -ComputerName localhost -Port 5433 -WarningAction SilentlyContinue -ErrorAction SilentlyContinue

if ($portTest.TcpTestSucceeded) {
    Write-Host "  [OK] Port 5433 is OPEN - PostgreSQL is listening" -ForegroundColor Green
} else {
    Write-Host "  [ERROR] Port 5433 is CLOSED - PostgreSQL may not be running" -ForegroundColor Red
    Write-Host ""
    Write-Host "To start PostgreSQL:" -ForegroundColor Yellow
    Write-Host "  Get-Service postgresql* | Start-Service" -ForegroundColor White
    exit 1
}

Write-Host ""

# Test 2: Try to connect using psql
Write-Host "[2] Testing database connection with psql..." -ForegroundColor Yellow
Write-Host "  Database: cyberaudit_db" -ForegroundColor Cyan
Write-Host "  User: cyberaudit_user" -ForegroundColor Cyan
Write-Host "  Password: cyberaudit123" -ForegroundColor Cyan
Write-Host "  Port: 5433" -ForegroundColor Cyan
Write-Host ""

$env:PGPASSWORD = "cyberaudit123"
$result = & psql -U cyberaudit_user -d cyberaudit_db -h localhost -p 5433 -c "SELECT 1;" 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "  [OK] Successfully connected to database!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Database connection successful!" -ForegroundColor Green
} else {
    Write-Host "  [ERROR] Failed to connect to database" -ForegroundColor Red
    Write-Host ""
    Write-Host "Error:" -ForegroundColor Yellow
    Write-Host $result -ForegroundColor Red
    Write-Host ""
    Write-Host "Possible issues:" -ForegroundColor Yellow
    Write-Host "  1. Database 'cyberaudit_db' doesn't exist" -ForegroundColor White
    Write-Host "  2. User 'cyberaudit_user' doesn't exist" -ForegroundColor White
    Write-Host "  3. Password is incorrect" -ForegroundColor White
    Write-Host "  4. PostgreSQL is not configured to accept connections on port 5433" -ForegroundColor White
    Write-Host ""
    Write-Host "To create database and user, run:" -ForegroundColor Yellow
    Write-Host "  psql -U postgres -h localhost -p 5433" -ForegroundColor White
    Write-Host "  Then execute:" -ForegroundColor White
    Write-Host "    CREATE DATABASE cyberaudit_db;" -ForegroundColor White
    Write-Host "    CREATE USER cyberaudit_user WITH PASSWORD 'cyberaudit123';" -ForegroundColor White
    Write-Host "    GRANT ALL PRIVILEGES ON DATABASE cyberaudit_db TO cyberaudit_user;" -ForegroundColor White
    exit 1
}

Write-Host ""

# Test 3: Check if tables exist
Write-Host "[3] Checking for existing tables..." -ForegroundColor Yellow
$tablesQuery = "\dt"
$tables = & psql -U cyberaudit_user -d cyberaudit_db -h localhost -p 5433 -c $tablesQuery 2>&1

if ($tables -match "No relations found") {
    Write-Host "  [INFO] Database is empty (no tables yet)" -ForegroundColor Cyan
    Write-Host "  This is normal - Spring Boot will create tables on startup" -ForegroundColor Cyan
} else {
    Write-Host "  [OK] Found existing tables:" -ForegroundColor Green
    Write-Host $tables -ForegroundColor White
}

Write-Host ""
Write-Host "=== Database Test Complete ===" -ForegroundColor Green
Write-Host ""
Write-Host "You can now start the backend server:" -ForegroundColor Cyan
Write-Host "  .\START_BACKEND.ps1" -ForegroundColor White
Write-Host ""
