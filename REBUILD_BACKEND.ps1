# ===================================================================
# CyberAudit Pro - Backend Rebuild Script
# ===================================================================
# Rebuilds the backend to include new scanner implementation
# Use this if you want to rebuild without starting servers
# ===================================================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Rebuilding Backend" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Set Java 21 path (Fixed - correct path found)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "Java Version:" -ForegroundColor Yellow
java -version
Write-Host ""

Write-Host "Building with Maven..." -ForegroundColor Yellow
Write-Host "Compiling:" -ForegroundColor Cyan
Write-Host "  ✓ VulnerabilityScanner.java" -ForegroundColor White
Write-Host "  ✓ RealTimeScanService.java" -ForegroundColor White
Write-Host "  ✓ ScanController.java" -ForegroundColor White
Write-Host "  ✓ All other backend components" -ForegroundColor White
Write-Host ""

.\mvnw.cmd clean package -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "  BUILD SUCCESSFUL!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "JAR Location:" -ForegroundColor Cyan
    Write-Host "  target\cyberaudit-pro-1.0.0-latest.jar" -ForegroundColor White
    Write-Host ""
    Write-Host "To start the backend:" -ForegroundColor Cyan
    Write-Host "  java -jar target\cyberaudit-pro-1.0.0-latest.jar" -ForegroundColor White
    Write-Host ""
    Write-Host "Or use the full startup script:" -ForegroundColor Cyan
    Write-Host "  .\START_FULL_APP.ps1" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "  BUILD FAILED!" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Check the errors above and fix them." -ForegroundColor Yellow
    Write-Host ""
    exit 1
}
