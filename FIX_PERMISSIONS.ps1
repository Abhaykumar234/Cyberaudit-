# Fix PostgreSQL Database Permissions
Write-Host "=== Fixing Database Permissions ===" -ForegroundColor Cyan
Write-Host ""

Write-Host "This script will grant all permissions to cyberaudit_user" -ForegroundColor Yellow
Write-Host "on tables currently owned by 'abhay'" -ForegroundColor Yellow
Write-Host ""

# Set password for postgres user
$postgresPassword = Read-Host "Enter postgres superuser password" -AsSecureString
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($postgresPassword)
$plainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

$env:PGPASSWORD = $plainPassword

Write-Host "Connecting to database..." -ForegroundColor Cyan

# SQL commands to fix permissions
$sqlCommands = @"
-- Transfer ownership of all tables to cyberaudit_user
ALTER TABLE audit_logs OWNER TO cyberaudit_user;
ALTER TABLE simulated_targets OWNER TO cyberaudit_user;
ALTER TABLE vulnerabilities OWNER TO cyberaudit_user;
ALTER TABLE users OWNER TO cyberaudit_user;

-- Grant all privileges
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO cyberaudit_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO cyberaudit_user;

-- Set default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO cyberaudit_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO cyberaudit_user;

SELECT 'Permissions fixed successfully!' AS result;
"@

# Execute SQL
$result = $sqlCommands | & psql -U postgres -d cyberaudit_db -h localhost -p 5433 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "[OK] Permissions fixed successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "All tables are now owned by cyberaudit_user" -ForegroundColor Green
    Write-Host "You can now start the backend server with: .\START_BACKEND.ps1" -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "[ERROR] Failed to fix permissions" -ForegroundColor Red
    Write-Host ""
    Write-Host "Error output:" -ForegroundColor Yellow
    Write-Host $result -ForegroundColor Red
}

# Clear password from environment
$env:PGPASSWORD = $null

Write-Host ""
