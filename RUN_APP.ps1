# CyberAudit Pro - Quick Run Script
# This script helps you run the application quickly

Write-Host ""
Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║                                                           ║" -ForegroundColor Cyan
Write-Host "║         🚀 CyberAudit Pro - Quick Start                  ║" -ForegroundColor Cyan
Write-Host "║                                                           ║" -ForegroundColor Cyan
Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Check if database password is set
Write-Host "📝 Checking configuration..." -ForegroundColor Yellow
$configFile = "src\main\resources\application.yml"
$configContent = Get-Content $configFile -Raw

if ($configContent -match "password:\s*postgres\s*$") {
    Write-Host ""
    Write-Host "⚠️  WARNING: Default password detected!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please update your PostgreSQL password in:" -ForegroundColor Yellow
    Write-Host "   $configFile" -ForegroundColor White
    Write-Host ""
    Write-Host "Find this line:" -ForegroundColor Yellow
    Write-Host "   password: postgres" -ForegroundColor White
    Write-Host ""
    Write-Host "Change it to YOUR password, then run this script again." -ForegroundColor Yellow
    Write-Host ""
    pause
    exit
}

Write-Host "✅ Configuration looks good!" -ForegroundColor Green
Write-Host ""

# Check if JAR exists
if (-not (Test-Path "target\cyberaudit-pro-1.0.0.jar")) {
    Write-Host "⚠️  Application JAR not found. Building..." -ForegroundColor Yellow
    Write-Host ""
    
    # Try to build
    Write-Host "Running: java -cp maven\lib\* org.apache.maven.cli.MavenCli clean package -DskipTests" -ForegroundColor Cyan
    java -cp "C:\Users\kumar\AppData\Local\Temp\maven\lib\*" org.apache.maven.cli.MavenCli clean package -DskipTests
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host ""
        Write-Host "❌ Build failed. Please check the error above." -ForegroundColor Red
        pause
        exit
    }
    
    Write-Host ""
    Write-Host "✅ Build successful!" -ForegroundColor Green
}

Write-Host ""
Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""
Write-Host "Starting Backend Server..." -ForegroundColor Green
Write-Host ""
Write-Host "Backend URL: http://localhost:8080/api" -ForegroundColor White
Write-Host ""
Write-Host "Keep this window OPEN while using the application." -ForegroundColor Yellow
Write-Host ""
Write-Host "To start the frontend, open a NEW PowerShell window and run:" -ForegroundColor Yellow
Write-Host "   cd frontend" -ForegroundColor White
Write-Host "   npm run dev" -ForegroundColor White
Write-Host ""
Write-Host "Press Ctrl+C to stop the backend." -ForegroundColor Yellow
Write-Host ""
Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

# Start the application
java -jar target\cyberaudit-pro-1.0.0.jar
