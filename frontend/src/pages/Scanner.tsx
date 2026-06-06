import { useState } from 'react'
import { scanUrl, ScanResult, Finding } from '../api/scanApi'

export default function Scanner() {
  const [targetUrl, setTargetUrl] = useState('')
  const [scanning, setScanning] = useState(false)
  const [result, setResult] = useState<ScanResult | null>(null)
  const [error, setError] = useState<string | null>(null)

  const handleScan = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!targetUrl) {
      setError('Please enter a target URL')
      return
    }

    // Basic URL validation
    if (!targetUrl.startsWith('http://') && !targetUrl.startsWith('https://')) {
      setError('URL must start with http:// or https://')
      return
    }

    try {
      setScanning(true)
      setError(null)
      setResult(null)
      
      const scanResult = await scanUrl(targetUrl)
      setResult(scanResult)
    } catch (err: unknown) {
      const message =
        (err as { response?: { data?: { error?: string } } })?.response?.data?.error ||
        (err as Error).message
      setError('Scan failed: ' + message)
      console.error(err)
    } finally {
      setScanning(false)
    }
  }

  const getSeverityColor = (severity: string) => {
    switch (severity) {
      case 'CRITICAL':
        return { bg: 'bg-danger-rose/10', text: 'text-danger-rose', border: 'border-danger-rose/30' }
      case 'HIGH':
        return { bg: 'bg-warning-amber/10', text: 'text-warning-amber', border: 'border-warning-amber/30' }
      case 'MEDIUM':
        return { bg: 'bg-primary/10', text: 'text-primary', border: 'border-primary/30' }
      default:
        return { bg: 'bg-success-emerald/10', text: 'text-success-emerald', border: 'border-success-emerald/30' }
    }
  }

  const getCategoryIcon = (category: string) => {
    switch (category) {
      case 'NETWORK':
        return 'lan'
      case 'SECURITY_HEADERS':
        return 'shield'
      case 'ENCRYPTION':
        return 'lock'
      case 'ACCESS_CONTROL':
        return 'verified_user'
      case 'INJECTION':
        return 'bug_report'
      default:
        return 'warning'
    }
  }

  return (
    <div className="p-gutter bg-background min-h-screen">
      <div className="max-w-container-max mx-auto space-y-stack-lg">
        {/* Header */}
        <div>
          <h1 className="font-h1 text-h1 text-text-primary tracking-tight flex items-center gap-3">
            <span className="material-symbols-outlined text-primary text-[40px]">radar</span>
            Real-Time Vulnerability Scanner
          </h1>
          <p className="font-body-md text-text-muted mt-1">
            Scan any website or web application for security vulnerabilities.
          </p>
        </div>

        {/* Scanner Form */}
        <div className="glass-panel rounded-xl p-gutter">
          <form onSubmit={handleScan} className="space-y-stack-md">
            <div className="space-y-stack-sm">
              <label className="font-label-caps text-label-caps text-primary uppercase">
                Target URL
              </label>
              <div className="flex gap-3">
                <input
                  type="text"
                  value={targetUrl}
                  onChange={(e) => setTargetUrl(e.target.value)}
                  placeholder="https://example.com"
                  className="flex-1 bg-surface-container-high border border-border-glass rounded-lg py-3 px-4 text-body-md text-text-primary focus:border-primary outline-none transition-all placeholder:text-text-muted/30"
                  disabled={scanning}
                />
                <button
                  type="submit"
                  disabled={scanning}
                  className="bg-primary text-white px-8 py-3 rounded-lg hover:opacity-90 transition disabled:opacity-50 disabled:cursor-not-allowed font-body-md font-semibold flex items-center gap-2 shadow-[0_0_15px_rgba(37,99,235,0.3)] active:scale-95"
                >
                  {scanning ? (
                    <>
                      <span className="material-symbols-outlined animate-spin text-[20px]">refresh</span>
                      Scanning...
                    </>
                  ) : (
                    <>
                      <span className="material-symbols-outlined text-[20px]">play_arrow</span>
                      Start Scan
                    </>
                  )}
                </button>
              </div>
            </div>

            {error && (
              <div className="bg-danger-rose/10 border border-danger-rose/30 text-danger-rose p-4 rounded-lg text-body-sm flex items-center gap-2">
                <span className="material-symbols-outlined">error</span>
                {error}
              </div>
            )}
          </form>
        </div>

        {/* Scanning Progress */}
        {scanning && (
          <div className="glass-panel rounded-xl p-gutter">
            <div className="flex flex-col items-center justify-center py-12 gap-4">
              <div className="relative w-24 h-24">
                <svg className="w-full h-full animate-spin" viewBox="0 0 100 100">
                  <circle
                    cx="50"
                    cy="50"
                    r="40"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="4"
                    className="text-surface-container-highest"
                  />
                  <circle
                    cx="50"
                    cy="50"
                    r="40"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="4"
                    strokeDasharray="60 100"
                    strokeLinecap="round"
                    className="text-primary"
                  />
                </svg>
              </div>
              <div className="text-center">
                <p className="text-text-primary font-h3 text-h3">Scanning Target...</p>
                <p className="text-text-muted text-body-sm mt-2">
                  Checking ports, headers, SSL configuration, and common vulnerabilities
                </p>
              </div>
            </div>
          </div>
        )}

        {/* Scan Results */}
        {result && !scanning && (
          <div className="space-y-stack-lg">
            {/* Summary Cards */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-gutter">
              <div className="glass-panel p-gutter rounded-xl">
                <p className="font-label-caps text-text-muted mb-2">TOTAL FINDINGS</p>
                <p className="text-[32px] font-bold text-text-primary">{result.findings.length}</p>
              </div>
              <div className="glass-panel p-gutter rounded-xl">
                <p className="font-label-caps text-danger-rose mb-2">CRITICAL</p>
                <p className="text-[32px] font-bold text-danger-rose">{result.severityCount.CRITICAL}</p>
              </div>
              <div className="glass-panel p-gutter rounded-xl">
                <p className="font-label-caps text-warning-amber mb-2">HIGH</p>
                <p className="text-[32px] font-bold text-warning-amber">{result.severityCount.HIGH}</p>
              </div>
              <div className="glass-panel p-gutter rounded-xl">
                <p className="font-label-caps text-primary mb-2">MEDIUM</p>
                <p className="text-[32px] font-bold text-primary">{result.severityCount.MEDIUM}</p>
              </div>
            </div>

            {/* Findings List */}
            <div className="glass-panel rounded-xl p-gutter">
              <h2 className="font-h2 text-h2 text-text-primary mb-stack-lg flex items-center gap-2">
                <span className="material-symbols-outlined">bug_report</span>
                Discovered Vulnerabilities
              </h2>

              {result.findings.length === 0 ? (
                <div className="text-center py-12">
                  <span className="material-symbols-outlined text-success-emerald text-[48px] mb-4">verified</span>
                  <p className="text-success-emerald font-h3 text-h3">No vulnerabilities found!</p>
                  <p className="text-text-muted text-body-sm mt-2">This target appears to be secure.</p>
                </div>
              ) : (
                <div className="space-y-stack-md">
                  {result.findings.map((finding, index) => {
                    const colors = getSeverityColor(finding.severity)
                    return (
                      <div
                        key={index}
                        className={`glass-panel rounded-xl p-gutter border-l-4 ${colors.border}`}
                      >
                        <div className="flex items-start justify-between gap-4 mb-4">
                          <div className="flex items-start gap-3 flex-1">
                            <span className={`material-symbols-outlined ${colors.text} text-[24px]`}>
                              {getCategoryIcon(finding.category)}
                            </span>
                            <div>
                              <h3 className="font-h3 text-[18px] text-text-primary">{finding.title}</h3>
                              <p className="text-text-muted text-body-sm mt-1">{finding.category}</p>
                            </div>
                          </div>
                          <span
                            className={`${colors.bg} ${colors.text} px-3 py-1 rounded font-code-sm text-[11px] font-bold uppercase border ${colors.border}`}
                          >
                            {finding.severity}
                          </span>
                        </div>

                        <div className="space-y-stack-sm">
                          <div>
                            <p className="font-label-caps text-text-muted text-[10px] mb-1">DESCRIPTION</p>
                            <p className="text-body-sm text-text-primary">{finding.description}</p>
                          </div>

                          {finding.remediation && (
                            <div>
                              <p className="font-label-caps text-success-emerald text-[10px] mb-1">REMEDIATION</p>
                              <p className="text-body-sm text-on-surface-variant">{finding.remediation}</p>
                            </div>
                          )}
                        </div>
                      </div>
                    )
                  })}
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  )
}
