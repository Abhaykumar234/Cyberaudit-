# Start CyberAudit Pro — backend + frontend
$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
if (-not (Test-Path $javaHome)) {
    Write-Host "Java 21 not found. Install from https://adoptium.net/" -ForegroundColor Red
    exit 1
}

$env:JAVA_HOME = $javaHome
$env:PATH = "$javaHome\bin;$env:PATH"
$env:SPRING_PROFILES_ACTIVE = "dev"

Set-Location $PSScriptRoot

Write-Host "=== CyberAudit Pro ===" -ForegroundColor Cyan
Write-Host "Backend:  http://localhost:8080/api" -ForegroundColor Green
Write-Host "Frontend: http://localhost:5173" -ForegroundColor Green
Write-Host "Login:    admin / changeme" -ForegroundColor Green
Write-Host ""

# Start backend in background
$backendJob = Start-Job -ScriptBlock {
    param($dir, $javaHome)
    $env:JAVA_HOME = $javaHome
    $env:PATH = "$javaHome\bin;$env:PATH"
    $env:SPRING_PROFILES_ACTIVE = "dev"
    Set-Location $dir
    java "-Dmaven.multiModuleProjectDirectory=$dir" -cp ".mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain spring-boot:run 2>&1
} -ArgumentList $PWD, $javaHome

Write-Host "Waiting for backend to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 25

# Start frontend
Set-Location "$PSScriptRoot\frontend"
if (-not (Test-Path "node_modules")) {
    npm install
}
npm run dev

# Cleanup on exit
Stop-Job $backendJob -ErrorAction SilentlyContinue
Remove-Job $backendJob -ErrorAction SilentlyContinue
