# CyberAudit Pro - Java 21 Setup Script
# Run this after installing Java 21

Write-Host "=== CyberAudit Pro - Java 21 Setup ===" -ForegroundColor Cyan
Write-Host ""

# Find Java 21 installation
$java21Paths = @(
    "C:\Program Files\Eclipse Adoptium\jdk-21*",
    "C:\Program Files\Java\jdk-21*",
    "C:\Program Files\OpenJDK\jdk-21*"
)

$javaHome = $null
foreach ($pattern in $java21Paths) {
    $found = Get-Item $pattern -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($found) {
        $javaHome = $found.FullName
        break
    }
}

if ($javaHome) {
    Write-Host "✓ Found Java 21 at: $javaHome" -ForegroundColor Green
    
    # Set JAVA_HOME for current session
    $env:JAVA_HOME = $javaHome
    $env:PATH = "$javaHome\bin;$env:PATH"
    
    Write-Host ""
    Write-Host "Setting JAVA_HOME..." -ForegroundColor Yellow
    Write-Host "JAVA_HOME = $javaHome" -ForegroundColor Gray
    
    # Verify Java version
    Write-Host ""
    Write-Host "Verifying Java version..." -ForegroundColor Yellow
    java -version
    
    Write-Host ""
    Write-Host "✓ Java 21 is ready!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Cyan
    Write-Host "1. Run: mvn clean install -DskipTests" -ForegroundColor White
    Write-Host "2. Run: mvn spring-boot:run" -ForegroundColor White
    Write-Host "3. Open: http://localhost:8080/api" -ForegroundColor White
    Write-Host ""
    
} else {
    Write-Host "✗ Java 21 not found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install Java 21 from:" -ForegroundColor Yellow
    Write-Host "https://adoptium.net/temurin/releases/?version=21" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "After installation, run this script again." -ForegroundColor Yellow
}
