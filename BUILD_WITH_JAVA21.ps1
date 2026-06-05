# Build CyberAudit Pro with Java 21
# This script sets JAVA_HOME to Java 21 and builds the project

Write-Host "=== CyberAudit Pro Build Script ===" -ForegroundColor Cyan
Write-Host ""

# Find Java 21 installation
$java21Paths = @(
    "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.9-hotspot",
    "C:\Program Files\Java\jdk-21",
    "C:\Program Files\OpenJDK\jdk-21",
    "C:\Program Files (x86)\Eclipse Adoptium\jdk-21.0.11.9-hotspot"
)

$javaHome = $null
foreach ($path in $java21Paths) {
    if (Test-Path $path) {
        $javaHome = $path
        Write-Host "Found Java 21 at: $javaHome" -ForegroundColor Green
        break
    }
}

if ($null -eq $javaHome) {
    Write-Host "ERROR: Java 21 not found!" -ForegroundColor Red
    Write-Host "Please install Java 21 or update the paths in this script" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Current Java version:" -ForegroundColor Yellow
    java -version
    exit 1
}

# Set JAVA_HOME for this session
$env:JAVA_HOME = $javaHome
$env:PATH = "$javaHome\bin;$env:PATH"

Write-Host ""
Write-Host "Java version being used:" -ForegroundColor Cyan
& "$javaHome\bin\java.exe" -version

Write-Host ""
Write-Host "Building CyberAudit Pro..." -ForegroundColor Cyan
Write-Host ""

# Clean and build
.\mvnw.cmd clean package -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "=== BUILD SUCCESS ===" -ForegroundColor Green
    Write-Host ""
    Write-Host "JAR file created at: target\cyberaudit-pro-1.0.0.jar" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Cyan
    Write-Host "1. Start backend: java -jar target\cyberaudit-pro-1.0.0.jar" -ForegroundColor White
    Write-Host "2. Install frontend deps: cd frontend; npm install" -ForegroundColor White
    Write-Host "3. Start frontend: cd frontend; npm run dev" -ForegroundColor White
    Write-Host "4. Open browser: http://localhost:5173" -ForegroundColor White
} else {
    Write-Host ""
    Write-Host "=== BUILD FAILED ===" -ForegroundColor Red
    Write-Host "Check the error messages above" -ForegroundColor Yellow
    exit 1
}
