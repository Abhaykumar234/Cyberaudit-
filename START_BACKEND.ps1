# Start CyberAudit Pro Backend
$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
if (-not (Test-Path $javaHome)) {
    Write-Host "Java 21 not found at $javaHome" -ForegroundColor Red
    Write-Host "Install from https://adoptium.net/ or update the path in this script." -ForegroundColor Yellow
    exit 1
}

$env:JAVA_HOME = $javaHome
$env:PATH = "$javaHome\bin;$env:PATH"
$env:SPRING_PROFILES_ACTIVE = "dev"

Set-Location $PSScriptRoot

Write-Host "=== CyberAudit Pro Backend ===" -ForegroundColor Cyan
Write-Host "URL:   http://localhost:8080/api" -ForegroundColor Green
Write-Host "Login: admin / changeme" -ForegroundColor Green
Write-Host ""
Write-Host "Stop any old backend window (Ctrl+C) before starting." -ForegroundColor Yellow
Write-Host ""

java "-Dmaven.multiModuleProjectDirectory=$PWD" -cp ".mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain spring-boot:run
