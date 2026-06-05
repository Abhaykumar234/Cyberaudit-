═══════════════════════════════════════════════════════════
  CYBERAUDIT PRO - TESTING GUIDE
═══════════════════════════════════════════════════════════

🚀 STEP-BY-STEP TESTING PROCESS
────────────────────────────────────────────────────────────

STEP 1: START THE APPLICATION
──────────────────────────────

Open PowerShell in the project directory and run:

    .\START_FULL_APP.ps1

Wait for these success messages:
  ✓ Java 21 detected
  ✓ PostgreSQL is running
  ✓ Backend built successfully!
  ✓ Backend started
  ✓ Frontend started

Expected output:
  Frontend: http://localhost:5173
  Backend:  http://localhost:8080/api


STEP 2: LOGIN TO THE APPLICATION
─────────────────────────────────

1. Open browser: http://localhost:5173
2. You'll see a professional dark-themed login page
3. Enter credentials:
   Username: admin
   Password: admin123
4. Click "Sign In"

✓ SUCCESS: You're redirected to the Dashboard


STEP 3: VERIFY DASHBOARD WORKS
───────────────────────────────

You should see:
  • Professional Material Design interface
  • Glass-morphism cards with metrics
  • Posture score (should show ~93%)
  • Recent vulnerabilities list
  • System health indicators

✓ SUCCESS: Dashboard loads with real data from PostgreSQL


STEP 4: TEST THE REAL-TIME SCANNER
───────────────────────────────────

1. Click "Real-Time Scanner" in the left navigation
   (Look for the radar icon 📡)

2. You'll see:
   • Professional scanner interface
   • URL input field
   • "Start Scan" button with blue glow

3. Enter a target URL to scan:
   Try: https://example.com
   Or:  https://github.com
   Or:  http://testphp.vulnweb.com

4. Click "Start Scan"

5. Watch the scanning animation:
   • Animated spinner appears
   • "Scanning Target..." message
   • Progress indicators

6. Wait 5-15 seconds (real scanning takes time!)

7. Results appear showing:
   • Total findings count
   • Severity breakdown (Critical/High/Medium/Low)
   • Detailed vulnerability list with:
     - Severity badges
     - Category icons
     - Description
     - Remediation advice


STEP 5: VERIFY REAL SCANNING (Not Mock Data)
─────────────────────────────────────────────

The scanner performs REAL checks:

Test 1: Scan https://example.com
Expected findings:
  ✓ Missing security headers (X-Frame-Options, CSP)
  ✓ Open HTTP port (80)
  ✓ Open HTTPS port (443)
  ✓ Header configuration issues

Test 2: Scan http://testphp.vulnweb.com
Expected findings:
  ✓ CRITICAL: SSL/TLS not enabled
  ✓ Missing security headers
  ✓ Multiple open ports
  ✓ Potential security misconfigurations

Test 3: Scan your own website
  • Enter your website URL
  • See REAL vulnerabilities in your site
  • Get actionable remediation advice


STEP 6: VERIFY DATABASE PERSISTENCE
────────────────────────────────────

Option A - Via API Test Script:
    .\TEST_SCANNER.ps1

Option B - Via Web Interface:
    1. After scanning, click "Vulnerability Catalog"
    2. Check if new vulnerabilities appear
    3. Look for pseudo-CVE IDs like "SCAN-OP-12345"

Option C - Via Database:
    psql -U abhay -d cyberaudit_db -p 5433
    SELECT cve_id, title, severity FROM vulnerabilities LIMIT 10;

✓ SUCCESS: Scan results saved to database


STEP 7: EXPLORE OTHER FEATURES
───────────────────────────────

Vulnerability Catalog:
  • View all discovered vulnerabilities
  • Filter by severity
  • See occurrence counts
  • Status tracking (OPEN/IN_PROGRESS/FIXED)

Audit Targets:
  • Manage scan targets
  • View posture scores
  • Track active findings

SOC 2 Audit Trail:
  • View all system activities
  • Filter by action type
  • Timestamp tracking

Secure Coding Labs:
  • Practice environments
  • Security challenges
  • Completion tracking


🎯 WHAT TO LOOK FOR (Success Indicators)
────────────────────────────────────────────────────────────

✓ Login works without errors
✓ Dashboard shows metrics (~93% posture score)
✓ Scanner page loads instantly
✓ Scan completes within 5-20 seconds
✓ Real findings appear (not dummy data)
✓ Severity counts match findings list
✓ Each finding shows proper details
✓ Database contains scan results
✓ No console errors in browser (F12)
✓ Backend logs show scan activity


🔬 TECHNICAL VERIFICATION
────────────────────────────────────────────────────────────

Verify Backend API:

1. Scanner Status:
   http://localhost:8080/api/scan/status
   Expected: {"status":"ONLINE","version":"1.0.0",...}

2. Metrics API:
   http://localhost:8080/api/metrics/system
   Expected: {"postureScore":93.33,...}

3. Vulnerabilities API:
   http://localhost:8080/api/vulnerabilities
   Expected: Array of vulnerabilities

Verify Frontend:

1. Open browser console (F12)
2. Check Network tab
3. Scan a URL
4. Should see:
   POST /api/scan/url → 200 OK
   Response shows findings array


🐛 COMMON ISSUES & SOLUTIONS
────────────────────────────────────────────────────────────

Issue: "Scanner not responding"
Solution: 
  - Check backend is running: http://localhost:8080/api/scan/status
  - Restart backend: java -jar target\cyberaudit-pro-1.0.0-latest.jar

Issue: "Scan takes forever"
Solution:
  - Some targets may have firewalls/timeouts
  - Try scanning example.com first (usually fast)
  - Port scanning can take 5-15 seconds (this is normal)

Issue: "No findings appear"
Solution:
  - Some sites are well-secured (few/no findings is good!)
  - Try scanning http:// sites (will show SSL issues)
  - Try http://testphp.vulnweb.com (intentionally vulnerable)

Issue: "Login fails"
Solution:
  - Ensure backend initialized data
  - Check backend logs for "DataInitializer" messages
  - Verify database connection

Issue: "Build fails"
Solution:
  - Check Java version: java -version (should be 21)
  - Run: .\REBUILD_BACKEND.ps1
  - Review compilation errors


📊 EXPECTED SCAN RESULTS FOR COMMON SITES
────────────────────────────────────────────────────────────

https://example.com:
  Total findings: 5-8
  Critical: 0
  High: 1-2 (missing HSTS, CSP)
  Medium: 2-3 (missing headers)
  Low: 1-3 (server info disclosure)

http://testphp.vulnweb.com:
  Total findings: 8-12
  Critical: 1 (no SSL/TLS)
  High: 3-4 (headers, exposed paths)
  Medium: 2-4 (ports, config)
  Low: 2-3 (info disclosure)

https://github.com:
  Total findings: 2-5
  Critical: 0
  High: 0-1
  Medium: 1-2
  Low: 1-2
  (GitHub is well-secured!)


🎓 UNDERSTANDING THE RESULTS
────────────────────────────────────────────────────────────

Port Findings:
  - Shows which network ports are open
  - Critical ports: Telnet (23), RDP (3389)
  - High risk: Database ports, FTP
  - Normal: HTTP (80), HTTPS (443)

Header Findings:
  - Missing security headers are common
  - Critical: Missing on banking/financial sites
  - High: Missing CSP, HSTS on HTTPS sites
  - Medium: Missing X-Frame-Options
  - Low: Missing X-XSS-Protection (deprecated)

SSL/TLS Findings:
  - Critical if HTTP is used for login/payments
  - All modern sites should use HTTPS

Exposed Paths:
  - Critical: .git, .env exposure
  - High: /admin, /backup accessible
  - These indicate misconfigurations


💡 PRO TIPS
────────────────────────────────────────────────────────────

• Scan multiple sites to see variety of results
• Compare HTTP vs HTTPS versions of same site
• Scan your own projects to find real issues
• Check backend console for detailed scan logs
• Use browser DevTools to see API calls
• Query database directly to see persistence
• Try the TEST_SCANNER.ps1 for API verification


🎉 SUCCESS CRITERIA
────────────────────────────────────────────────────────────

Your application is working perfectly if:

✓ Build completes without errors
✓ Both servers start successfully  
✓ Login works immediately
✓ Dashboard shows ~93% posture score
✓ Scanner interface loads properly
✓ Scans complete in 5-20 seconds
✓ Real findings appear (varies by target)
✓ Each finding has proper details
✓ Severity counts are accurate
✓ Database contains results
✓ No errors in browser console
✓ No errors in backend console


═══════════════════════════════════════════════════════════
         YOU'RE READY TO TEST! 🎯
═══════════════════════════════════════════════════════════

Run: .\START_FULL_APP.ps1

Then follow this guide step-by-step!
