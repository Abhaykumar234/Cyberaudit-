import { useState, useEffect } from 'react'
import { getSystemMetrics } from '../api/metricsApi'
import { getAllVulnerabilities } from '../api/vulnerabilityApi'
import { generateAudit } from '../api/auditApi'
import { getTargets } from '../api/targetApi'
import Loading from '../components/Loading'

interface Metrics {
  postureScore: number
  activeFindings: number
  criticalCount: number
  highCount: number
  mediumCount: number
  remediationRate: number
}

interface Vulnerability {
  id: number
  cveId: string
  title: string
  description: string
  severity: string
  occurrences: number
  affectedEndpoints: number
}

const severityStyles: Record<string, { bg: string; text: string }> = {
  CRITICAL: { bg: 'bg-danger-rose/10', text: 'text-danger-rose' },
  HIGH: { bg: 'bg-warning-amber/10', text: 'text-warning-amber' },
  MEDIUM: { bg: 'bg-primary/10', text: 'text-primary' },
  LOW: { bg: 'bg-success-emerald/10', text: 'text-success-emerald' },
}

export default function Dashboard() {
  const [metrics, setMetrics] = useState<Metrics | null>(null)
  const [vulnerabilities, setVulnerabilities] = useState<Vulnerability[]>([])
  const [loading, setLoading] = useState(true)
  const [scanning, setScanning] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [scanMessage, setScanMessage] = useState<string | null>(null)

  useEffect(() => {
    fetchData()
  }, [])

  const fetchData = async () => {
    try {
      setLoading(true)
      const [metricsData, vulnData] = await Promise.all([
        getSystemMetrics(),
        getAllVulnerabilities(0, 3),
      ])
      setMetrics(metricsData)
      setVulnerabilities(vulnData.content || [])
      setError(null)
    } catch (err) {
      setError('Failed to fetch data: ' + (err as Error).message)
    } finally {
      setLoading(false)
    }
  }

  const runScan = async () => {
    try {
      setScanning(true)
      setScanMessage(null)
      setError(null)
      const targets = await getTargets()
      const targetId = targets.length > 0 ? String(targets[0].id) : 'default'
      const result = await generateAudit({
        targetId,
        auditType: 'FULL',
        scope: 'application',
        includeRemediationAdvice: true,
      })
      setScanMessage(`Scan complete: ${result.findingsCount} findings (${result.criticalCount} critical)`)
      await fetchData()
    } catch (err: unknown) {
      const message =
        (err as { response?: { data?: { error?: string } } })?.response?.data?.error ||
        (err as Error).message
      setError('Scan failed: ' + message)
    } finally {
      setScanning(false)
    }
  }

  if (loading) return <Loading />
  if (error && !metrics) return <div className="p-8 text-danger-rose">{error}</div>
  if (!metrics) return <div className="p-8 text-text-muted">No data available</div>

  return (
    <div className="p-gutter min-h-screen bg-background">
      <div className="max-w-container-max mx-auto space-y-stack-lg">
        <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4">
          <div>
            <h1 className="font-h1 text-h1 text-text-primary tracking-tight">
              Vulnerability Intelligence
            </h1>
            <p className="font-body-md text-text-muted mt-1">
              Live metrics from your audit targets and findings.
            </p>
          </div>
          <button
            onClick={runScan}
            disabled={scanning}
            className="bg-primary px-6 py-2 rounded font-body-sm text-white font-semibold hover:opacity-90 transition-opacity flex items-center gap-2 disabled:opacity-50"
          >
            <span className="material-symbols-outlined text-[18px]">
              {scanning ? 'hourglass_bottom' : 'radar'}
            </span>
            {scanning ? 'Scanning...' : 'Run AI Security Scan'}
          </button>
        </div>

        {scanMessage && (
          <div className="bg-success-emerald/10 border border-success-emerald/30 text-success-emerald p-3 rounded-lg text-body-sm">
            {scanMessage}
          </div>
        )}
        {error && (
          <div className="bg-danger-rose/10 border border-danger-rose/30 text-danger-rose p-3 rounded-lg text-body-sm">
            {error}
          </div>
        )}

        <div className="grid grid-cols-1 md:grid-cols-3 gap-gutter">
          <div className="glass-panel p-gutter rounded-xl">
            <p className="font-label-caps text-text-muted mb-2">POSTURE SCORE</p>
            <span className="font-code-md text-[32px] font-bold text-success-emerald">
              {metrics.postureScore.toFixed(1)}%
            </span>
          </div>
          <div className="glass-panel p-gutter rounded-xl">
            <p className="font-label-caps text-text-muted mb-4">ACTIVE FINDINGS</p>
            <span className="font-code-md text-[40px] font-bold text-text-primary">
              {metrics.activeFindings}
            </span>
            <div className="flex gap-2 mt-2">
              <span className="text-danger-rose bg-danger-rose/10 px-2 py-0.5 rounded text-[11px]">
                {metrics.criticalCount} CRITICAL
              </span>
              <span className="text-warning-amber bg-warning-amber/10 px-2 py-0.5 rounded text-[11px]">
                {metrics.highCount} HIGH
              </span>
            </div>
          </div>
          <div className="glass-panel p-gutter rounded-xl">
            <p className="font-label-caps text-text-muted mb-4">REMEDIATION RATE</p>
            <span className="font-code-md text-[40px] font-bold text-text-primary">
              {metrics.remediationRate}%
            </span>
          </div>
        </div>

        <div className="glass-panel p-gutter rounded-xl">
          <p className="font-label-caps text-text-muted mb-gutter">SEVERITY DISTRIBUTION</p>
          <div className="flex gap-6 text-body-sm font-code-sm">
            <span>Critical: {metrics.criticalCount}</span>
            <span>High: {metrics.highCount}</span>
            <span>Medium: {metrics.mediumCount ?? 0}</span>
          </div>
        </div>

        <div>
          <h2 className="font-h2 text-h2 text-text-primary mb-stack-lg">Recent Findings</h2>
          {vulnerabilities.length === 0 ? (
            <p className="text-text-muted">
              No findings yet. Add an audit target and run an AI security scan.
            </p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-gutter">
              {vulnerabilities.map((vuln) => {
                const styles = severityStyles[vuln.severity] || severityStyles.MEDIUM
                return (
                  <div key={vuln.id} className="glass-panel rounded-xl overflow-hidden flex flex-col p-gutter">
                    <div className="flex justify-between items-start mb-4">
                      <span className="font-code-sm bg-surface-container-high px-2 py-1 rounded text-primary border border-border-glass">
                        {vuln.cveId}
                      </span>
                      <span className={`font-code-sm ${styles.bg} ${styles.text} px-2 py-0.5 rounded`}>
                        {vuln.severity}
                      </span>
                    </div>
                    <h3 className="font-h3 text-[20px] text-text-primary mb-2">{vuln.title}</h3>
                    <p className="font-body-sm text-text-muted line-clamp-3">{vuln.description}</p>
                  </div>
                )
              })}
            </div>
          )}
        </div>

        <div className="flex justify-center pt-stack-lg">
          <button
            onClick={fetchData}
            className="bg-surface-container-high px-6 py-2.5 rounded font-body-md text-text-primary border border-border-glass hover:bg-surface-container-highest transition-colors flex items-center gap-2"
          >
            <span className="material-symbols-outlined">refresh</span>
            Refresh
          </button>
        </div>
      </div>
    </div>
  )
}
