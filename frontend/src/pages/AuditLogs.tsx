import { useState, useEffect } from 'react'
import { getAuditLogs } from '../api/auditLogApi'
import Loading from '../components/Loading'

interface AuditLog {
  id: number
  userId: string
  action: string
  description: string
  severity: string
  status: string
  category: string
  timestamp: string
  sourceIp: string
  affectedResource: string
}

export default function AuditLogs() {
  const [logs, setLogs] = useState<AuditLog[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetchLogs()
  }, [])

  const fetchLogs = async () => {
    try {
      setLoading(true)
      const data = await getAuditLogs()
      setLogs(data.content || [])
      setError(null)
    } catch (err) {
      setError('Failed to fetch audit logs: ' + (err as Error).message)
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const getSeverityColor = (severity: string) => {
    switch (severity) {
      case 'CRITICAL':
        return { bg: 'bg-danger-rose', text: 'text-white', icon: 'warning' }
      case 'HIGH':
        return { bg: 'bg-warning-amber', text: 'text-on-surface', icon: 'alert' }
      default:
        return { bg: 'bg-success-emerald', text: 'text-white', icon: 'check_circle' }
    }
  }

  if (loading) return <Loading />
  if (error) return <div className="p-8 text-danger-rose">{error}</div>

  return (
    <div className="p-gutter bg-background min-h-screen">
      <div className="max-w-container-max mx-auto space-y-stack-lg">
        {/* Header */}
        <div className="flex justify-between items-end">
          <div>
            <h1 className="font-h1 text-h1 text-text-primary tracking-tight">SOC 2 Audit Trail</h1>
            <p className="font-body-md text-text-muted mt-1">
              Real-time immutable ledger of system-wide security events.
            </p>
          </div>
          <button className="flex items-center gap-2 px-6 py-2.5 rounded bg-primary text-white font-body-sm font-semibold hover:opacity-90 transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] active:scale-95">
            <span className="material-symbols-outlined text-[18px]">sync</span>
            Force Integrity Ledger Sync
          </button>
        </div>

        {/* Terminal Panel */}
        <div className="flex-1 bg-surface-container-lowest rounded-xl border border-border-glass shadow-[0_12px_40px_rgba(0,0,0,0.6)] flex flex-col overflow-hidden max-h-[600px]">
          {/* Terminal Header */}
          <div className="bg-surface-container px-gutter py-3 flex justify-between items-center shrink-0 border-b border-border-glass">
            {/* Window Controls */}
            <div className="flex gap-2">
              <div className="w-3 h-3 rounded-full bg-danger-rose/80"></div>
              <div className="w-3 h-3 rounded-full bg-warning-amber/80"></div>
              <div className="w-3 h-3 rounded-full bg-success-emerald/80"></div>
            </div>
            {/* Filename */}
            <div className="font-label-caps text-label-caps text-on-surface-variant flex items-center gap-2">
              <span className="material-symbols-outlined text-[16px]">description</span>
              audit_trace.log
            </div>
            {/* Status */}
            <div className="flex items-center gap-2 bg-success-emerald/10 border border-success-emerald/20 px-3 py-1 rounded-full">
              <div className="w-2 h-2 rounded-full bg-success-emerald animate-pulse"></div>
              <span className="font-label-caps text-label-caps text-success-emerald tracking-widest text-[10px]">
                TRACE BLOCK STREAM: SECURE
              </span>
            </div>
          </div>

          {/* Log List */}
          <div className="flex-1 overflow-y-auto p-gutter font-code-sm text-code-sm space-y-2">
            {logs.slice(0, 10).map((log) => {
              const severity = log.severity || 'INFO'
              const colors = getSeverityColor(severity)
              const bgColor =
                severity === 'CRITICAL'
                  ? 'bg-danger-rose/10'
                  : severity === 'HIGH'
                    ? 'bg-warning-amber/10'
                    : 'bg-surface-container-low/30'
              const borderColor =
                severity === 'CRITICAL'
                  ? 'border-transparent hover:border-danger-rose'
                  : severity === 'HIGH'
                    ? 'border-transparent hover:border-warning-amber'
                    : 'border-transparent hover:border-success-emerald'

              return (
                <div
                  key={log.id}
                  className={`flex items-start gap-4 p-3 hover:${bgColor} rounded transition-colors group border-l-2 ${borderColor}`}
                >
                  {/* Timestamp */}
                  <div className="text-text-muted shrink-0 w-[160px] font-medium">
                    {new Date(log.timestamp).toLocaleString('en-US', {
                      year: 'numeric',
                      month: '2-digit',
                      day: '2-digit',
                      hour: '2-digit',
                      minute: '2-digit',
                      second: '2-digit',
                    })}
                  </div>

                  {/* Log ID */}
                  <div className="text-outline-variant shrink-0 w-[100px] font-mono">
                    ID:{String(log.id).padStart(7, '0')}
                  </div>

                  {/* Category Badge */}
                  <div className="shrink-0 w-[140px]">
                    <span className="bg-surface-container-high border border-border-glass text-primary px-2 py-0.5 rounded text-[10px] font-bold uppercase tracking-wider">
                      {log.category || 'SYSTEM'}
                    </span>
                  </div>

                  {/* Status Indicator */}
                  <div className="flex items-center justify-center shrink-0 w-6 pt-1">
                    <div
                      className={`w-2 h-2 rounded-full shadow-[0_0_8px_rgba(37,99,235,0.6)] ${
                        severity === 'CRITICAL'
                          ? 'bg-danger-rose shadow-[0_0_12px_rgba(244,63,94,0.8)]'
                          : severity === 'HIGH'
                            ? 'bg-warning-amber shadow-[0_0_8px_rgba(245,158,11,0.6)]'
                            : 'bg-success-emerald shadow-[0_0_8px_rgba(16,185,129,0.6)]'
                      }`}
                    ></div>
                  </div>

                  {/* Log Content */}
                  <div className="text-on-surface-variant flex-1 break-words font-mono">
                    <span className="text-primary-fixed-dim">{log.action}:</span>
                    <span className="ml-1">{log.description}</span>
                    {log.sourceIp && (
                      <span className="text-secondary ml-1">
                        from IP{' '}
                        <span className="text-on-surface font-bold underline">{log.sourceIp}</span>
                      </span>
                    )}
                  </div>
                </div>
              )
            })}

            {/* Padding */}
            <div className="h-12"></div>
          </div>

          {/* Terminal Overlay Glow */}
          <div className="absolute bottom-0 left-0 w-full h-24 bg-gradient-to-t from-surface-container-lowest to-transparent pointer-events-none"></div>
        </div>

        {/* Refresh Button */}
        <div className="flex justify-center">
          <button
            onClick={fetchLogs}
            className="bg-surface-container-high px-6 py-2.5 rounded font-body-md text-text-primary border border-border-glass hover:bg-surface-container-highest transition-colors flex items-center gap-2"
          >
            <span className="material-symbols-outlined">refresh</span>
            Load More Events
          </button>
        </div>
      </div>
    </div>
  )
}
