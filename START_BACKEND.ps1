# Build and start CyberAudit Pro (uses spring-boot:run — no JAR lock issues)
$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
if (-not (Test-Path $javaHome)) {
    Write-Host "Java 21 not found at $javaHome" -ForegroundColor Red
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
Write-Host "If login still fails, stop any OLD backend window first (Ctrl+C), then rerun this script." -ForegroundColor Yellow
Write-Host ""

java "-Dmaven.multiModuleProjectDirectory=$PWD" -cp ".mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain spring-boot:run "-Dspring-boot.run.arguments=--spring.config.additional-location=file:./application.yml"
