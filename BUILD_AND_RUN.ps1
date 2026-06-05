# CyberAudit Pro - Build and Run Script

Write-Host "=== CyberAudit Pro - Build and Run ===" -ForegroundColor Cyan
Write-Host ""

# Check Java version
Write-Host "Checking Java version..." -ForegroundColor Yellow
$javaVersion = java -version 2>&1 | Select-String "version" | Select-Object -First 1
Write-Host $javaVersion -ForegroundColor Gray

if ($javaVersion -match "21\.") {
    Write-Host "✓ Java 21 detected!" -ForegroundColor Green
    Write-Host ""
    
    # Clean and build
    Write-Host "Building project..." -ForegroundColor Yellow
    Write-Host "Running: mvn clean install -DskipTests" -ForegroundColor Gray
    Write-Host ""
    
    mvn.cmd clean install -DskipTests
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✓ Build successful!" -ForegroundColor Green
        Write-Host ""
        Write-Host "Starting Spring Boot application..." -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Backend will be available at: http://localhost:8080/api" -ForegroundColor Cyan
        Write-Host "Press Ctrl+C to stop the server" -ForegroundColor Gray
        Write-Host ""
        
        mvn.cmd spring-boot:run
    } else {
        Write-Host ""
        Write-Host "✗ Build failed!" -ForegroundColor Red
        Write-Host "Check the error messages above." -ForegroundColor Yellow
    }
    
} else {
    Write-Host "✗ Java 21 not detected!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please run: .\INSTALL_JAVA21.ps1" -ForegroundColor Yellow
    Write-Host "Or install Java 21 from: https://adoptium.net/temurin/releases/?version=21" -ForegroundColor Cyan
}
