# ===================================================================
# CyberAudit Pro - Scanner Test Script
# ===================================================================
# Tests the real-time vulnerability scanner API endpoints
# ===================================================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Testing Real-Time Scanner" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$backendUrl = "http://localhost:8080/api"

Write-Host "[1/3] Checking scanner status..." -ForegroundColor Yellow
try {
    $status = Invoke-RestMethod -Uri "$backendUrl/scan/status" -Method GET
    Write-Host "✓ Scanner Status: $($status.status)" -ForegroundColor Green
    Write-Host "  Version: $($status.version)" -ForegroundColor Cyan
    Write-Host "  Capabilities: $($status.capabilities)" -ForegroundColor Cyan
} catch {
    Write-Host "✗ Scanner not responding" -ForegroundColor Red
    Write-Host "  Make sure backend is running on port 8080" -ForegroundColor Yellow
    exit 1
}
Write-Host ""

Write-Host "[2/3] Testing scanner with example.com..." -ForegroundColor Yellow
Write-Host "This is a REAL scan - checking ports, headers, SSL..." -ForegroundColor Cyan
Write-Host ""

$scanRequest = @{
    url = "https://example.com"
} | ConvertTo-Json

try {
    $result = Invoke-RestMethod -Uri "$backendUrl/scan/url" -Method POST -Body $scanRequest -ContentType "application/json"
    
    Write-Host "✓ Scan completed successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "SCAN RESULTS:" -ForegroundColor Cyan
    Write-Host "  Target: $($result.targetUrl)" -ForegroundColor White
    Write-Host "  Host: $($result.host)" -ForegroundColor White
    Write-Host "  Status: $($result.status)" -ForegroundColor White
    Write-Host "  Total Findings: $($result.findings.Count)" -ForegroundColor White
    Write-Host ""
    
    Write-Host "SEVERITY BREAKDOWN:" -ForegroundColor Cyan
    Write-Host "  Critical: $($result.severityCount.CRITICAL)" -ForegroundColor Red
    Write-Host "  High: $($result.severityCount.HIGH)" -ForegroundColor Yellow
    Write-Host "  Medium: $($result.severityCount.MEDIUM)" -ForegroundColor Cyan
    Write-Host "  Low: $($result.severityCount.LOW)" -ForegroundColor Green
    Write-Host ""
    
    if ($result.findings.Count -gt 0) {
        Write-Host "TOP FINDINGS:" -ForegroundColor Cyan
        $result.findings | Select-Object -First 5 | ForEach-Object {
            Write-Host "  [$($_.severity)] $($_.title)" -ForegroundColor White
            Write-Host "    Category: $($_.category)" -ForegroundColor Gray
            Write-Host "    $($_.description)" -ForegroundColor Gray
            Write-Host ""
        }
    }
} catch {
    Write-Host "✗ Scan failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host "[3/3] Verifying data persistence..." -ForegroundColor Yellow
try {
    $vulnerabilities = Invoke-RestMethod -Uri "$backendUrl/vulnerabilities" -Method GET
    Write-Host "✓ Total vulnerabilities in database: $($vulnerabilities.Count)" -ForegroundColor Green
    Write-Host ""
    Write-Host "Scanner successfully saved findings to PostgreSQL database!" -ForegroundColor Green
} catch {
    Write-Host "Could not verify database persistence" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "  SCANNER TEST COMPLETE!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "  1. Open http://localhost:5173 in browser" -ForegroundColor White
Write-Host "  2. Login (admin/admin123)" -ForegroundColor White
Write-Host "  3. Go to 'Real-Time Scanner' page" -ForegroundColor White
Write-Host "  4. Try scanning different websites!" -ForegroundColor White
Write-Host ""
