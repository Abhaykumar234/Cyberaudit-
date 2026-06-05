import { useState, useEffect } from 'react'
import { getAllVulnerabilities } from '../api/vulnerabilityApi'
import Loading from '../components/Loading'

interface Vulnerability {
  id: number
  cveId: string
  title: string
  description: string
  severity: string
  status: string
  category: string
  occurrences: number
  affectedEndpoints: number
  threatVector: string
  remediationAdvice: string
  lastDetected: string
  createdAt: string
}

export default function Vulnerabilities() {
  const [vulnerabilities, setVulnerabilities] = useState<Vulnerability[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [filter, setFilter] = useState('all')

  useEffect(() => {
    fetchVulnerabilities()
  }, [])

  const fetchVulnerabilities = async () => {
    try {
      setLoading(true)
      const data = await getAllVulnerabilities(0, 100)
      setVulnerabilities(data.content || [])
      setError(null)
    } catch (err) {
      setError('Failed to fetch vulnerabilities: ' + (err as Error).message)
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const filteredVulnerabilities = vulnerabilities.filter((v) => {
    if (filter === 'critical') return v.severity === 'CRITICAL'
    if (filter === 'high') return v.severity === 'HIGH'
    if (filter === 'medium') return v.severity === 'MEDIUM'
    return true
  })

  const getSeverityStyles = (severity: string) => {
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

  if (loading) return <Loading />
  if (error) return <div className="p-8 text-danger-rose">{error}</div>

  return (
    <div className="p-gutter bg-background min-h-screen">
      <div className="max-w-container-max mx-auto space-y-stack-lg">
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4">
          <div>
            <h1 className="font-h1 text-h1 text-text-primary tracking-tight">
              Vulnerability Catalog
            </h1>
            <p className="font-body-md text-text-muted mt-1">
              OWASP Top 10 and detected security issues
            </p>
          </div>
          <div className="text-right">
            <p className="text-text-muted text-sm">Total Issues</p>
            <p className="text-3xl font-bold text-primary">{vulnerabilities.length}</p>
          </div>
        </div>

        {/* Filters */}
        <div className="flex gap-3 flex-wrap">
          {[
            { id: 'all', label: 'All', count: vulnerabilities.length },
            { id: 'critical', label: 'Critical', count: vulnerabilities.filter((v) => v.severity === 'CRITICAL').length },
            { id: 'high', label: 'High', count: vulnerabilities.filter((v) => v.severity === 'HIGH').length },
            { id: 'medium', label: 'Medium', count: vulnerabilities.filter((v) => v.severity === 'MEDIUM').length },
          ].map((btn) => (
            <button
              key={btn.id}
              onClick={() => setFilter(btn.id)}
              className={`px-4 py-2 rounded-lg font-body-sm transition-all ${
                filter === btn.id
                  ? 'bg-primary text-white font-semibold'
                  : 'bg-surface-container-high text-text-muted hover:text-text-primary border border-border-glass'
              }`}
            >
              {btn.label} ({btn.count})
            </button>
          ))}
        </div>

        {/* Vulnerabilities Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-gutter">
          {filteredVulnerabilities.map((vuln) => {
            const styles = getSeverityStyles(vuln.severity)
            return (
              <div
                key={vuln.id}
                className={`glass-panel rounded-xl overflow-hidden flex flex-col border-t-2 ${
                  vuln.severity === 'CRITICAL'
                    ? 'border-t-danger-rose'
                    : vuln.severity === 'HIGH'
                      ? 'border-t-warning-amber'
                      : 'border-t-primary'
                }`}
              >
                <div className="p-gutter flex-grow">
                  <div className="flex justify-between items-start mb-4">
                    <span className="font-code-sm bg-surface-container-high px-2 py-1 rounded text-primary border border-border-glass">
                      {vuln.cveId}
                    </span>
                    <span
                      className={`font-code-sm ${styles.bg} ${styles.text} px-2 py-0.5 rounded flex items-center gap-1 border ${styles.border}`}
                    >
                      {vuln.severity}
                    </span>
                  </div>
                  <h3 className="font-h3 text-[20px] text-text-primary mb-2">{vuln.title}</h3>
                  <p className="font-body-sm text-text-muted line-clamp-3">{vuln.description}</p>
                  <div className="mt-6 space-y-2 border-t border-border-glass/50 pt-4">
                    <div className="flex items-center justify-between text-body-sm font-code-sm">
                      <span className="text-text-muted">Occurrences</span>
                      <span className="text-text-primary">{vuln.occurrences}</span>
                    </div>
                    <div className="flex items-center justify-between text-body-sm font-code-sm">
                      <span className="text-text-muted">Affected Endpoints</span>
                      <span className="text-text-primary">{vuln.affectedEndpoints}</span>
                    </div>
                  </div>
                </div>
                <div className="bg-surface-container-high px-gutter py-3 flex justify-between items-center">
                  <span className="font-code-sm text-text-muted text-[12px]">Last detected: 2m ago</span>
                  <button className="text-primary font-semibold text-body-sm flex items-center gap-1 hover:gap-2 transition-all">
                    Investigate
                    <span className="material-symbols-outlined text-[16px]">arrow_forward</span>
                  </button>
                </div>
              </div>
            )
          })}
        </div>

        {filteredVulnerabilities.length === 0 && (
          <div className="text-center py-12">
            <p className="text-text-muted text-lg">No vulnerabilities found</p>
          </div>
        )}

        {/* Load More */}
        <div className="flex justify-center">
          <button
            onClick={fetchVulnerabilities}
            className="bg-surface-container-high px-6 py-2.5 rounded font-body-md text-text-primary border border-border-glass hover:bg-surface-container-highest transition-colors flex items-center gap-2"
          >
            <span className="material-symbols-outlined">refresh</span>
            Load More Findings
          </button>
        </div>
      </div>
    </div>
  )
}
