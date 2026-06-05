Write-Host "=== CyberAudit Pro - Starting Application ===" -ForegroundColor Cyan
Write-Host ""

# Check if backend is already running
$javaProcess = Get-Process -Name "java" -ErrorAction SilentlyContinue
if ($javaProcess) {
    Write-Host "✓ Backend is already running" -ForegroundColor Green
} else {
    Write-Host "Starting Backend..." -ForegroundColor Yellow
    Start-Process powershell -ArgumentList "-NoExit", "-File", ".\START_BACKEND.ps1"
    Start-Sleep -Seconds 5
}

# Check if frontend is already running
$nodeProcess = Get-Process -Name "node" -ErrorAction SilentlyContinue | Where-Object { $_.MainWindowTitle -like "*vite*" -or $_.Path -like "*npm*" }
if ($nodeProcess) {
    Write-Host "✓ Frontend is already running" -ForegroundColor Green
} else {
    Write-Host "Starting Frontend..." -ForegroundColor Yellow
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd frontend; npm run dev"
    Start-Sleep -Seconds 5
}

Write-Host ""
Write-Host "=== Application Started ===" -ForegroundColor Green
Write-Host ""
Write-Host "Backend:  http://localhost:8080/api" -ForegroundColor Cyan
Write-Host "Frontend: http://localhost:5173" -ForegroundColor Cyan
Write-Host ""
Write-Host "Press Ctrl+C to stop this script (servers will keep running)" -ForegroundColor Yellow
Write-Host ""

# Wait and test connection
Start-Sleep -Seconds 3
Write-Host "Testing connection..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "http://localhost:5173/api/metrics/system" -TimeoutSec 10
    Write-Host "✓ Connection successful! Posture Score: $($response.postureScore)%" -ForegroundColor Green
} catch {
    Write-Host "✗ Connection test failed. Servers may still be starting..." -ForegroundColor Red
}

Write-Host ""
Write-Host "Open http://localhost:5173 in your browser to use the app" -ForegroundColor Cyan
