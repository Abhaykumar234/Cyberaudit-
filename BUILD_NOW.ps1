# Quick Build Script - Sets correct Java 21 path and builds
Write-Host "Setting Java 21 environment..." -ForegroundColor Yellow

$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "Java Home: $env:JAVA_HOME" -ForegroundColor Cyan
Write-Host ""

Write-Host "Verifying Java version:" -ForegroundColor Yellow
& java -version
Write-Host ""

Write-Host "Building backend with Maven..." -ForegroundColor Yellow
& .\mvnw.cmd clean package -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "  BUILD SUCCESS!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "JAR created: target\cyberaudit-pro-1.0.0-latest.jar" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "To start backend:" -ForegroundColor Yellow
    Write-Host "  java -jar target\cyberaudit-pro-1.0.0-latest.jar" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "BUILD FAILED - Check errors above" -ForegroundColor Red
    Write-Host ""
}
