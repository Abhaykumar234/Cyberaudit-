import { useState, useEffect } from 'react'
import { getTargets, createTarget, deleteTarget, Target } from '../api/targetApi'
import Loading from '../components/Loading'

const emptyForm = {
  name: '',
  environment: 'Production',
  targetType: 'API',
  endpoint: '',
  description: '',
  assetsInScope: 1,
}

export default function Targets() {
  const [targets, setTargets] = useState<Target[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [filter, setFilter] = useState('all')
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState(emptyForm)
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    fetchTargets()
  }, [])

  const fetchTargets = async () => {
    try {
      setLoading(true)
      const data = await getTargets()
      setTargets(Array.isArray(data) ? data : [])
      setError(null)
    } catch (err) {
      setError('Failed to fetch targets: ' + (err as Error).message)
    } finally {
      setLoading(false)
    }
  }

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      setSaving(true)
      await createTarget({
        ...form,
        postureScore: 100,
        activeFindings: 0,
        remediationRate: 100,
      })
      setForm(emptyForm)
      setShowForm(false)
      await fetchTargets()
    } catch (err) {
      setError('Failed to create target: ' + (err as Error).message)
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async (id: number) => {
    if (!confirm('Delete this audit target?')) return
    try {
      await deleteTarget(id)
      await fetchTargets()
    } catch (err) {
      setError('Failed to delete target: ' + (err as Error).message)
    }
  }

  const filteredTargets = targets.filter((t) => {
    if (filter === 'production') return t.environment === 'Production'
    if (filter === 'staging') return t.environment === 'Staging'
    if (filter === 'development') return t.environment === 'Development'
    return true
  })

  if (loading) return <Loading />

  return (
    <div className="p-gutter bg-background min-h-screen">
      <div className="max-w-container-max mx-auto space-y-stack-lg">
        <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4">
          <div>
            <h1 className="font-h1 text-h1 text-text-primary tracking-tight">Audit Targets</h1>
            <p className="font-body-md text-text-muted mt-1">
              Register real infrastructure endpoints for AI security scanning.
            </p>
          </div>
          <button
            onClick={() => setShowForm(!showForm)}
            className="bg-primary px-4 py-2 rounded-lg text-white font-body-sm font-semibold hover:opacity-90"
          >
            {showForm ? 'Cancel' : 'Add Target'}
          </button>
        </div>

        {error && (
          <div className="bg-danger-rose/10 border border-danger-rose/30 text-danger-rose p-3 rounded-lg text-body-sm">
            {error}
          </div>
        )}

        {showForm && (
          <form onSubmit={handleCreate} className="glass-panel rounded-xl p-gutter space-y-stack-md">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-gutter">
              <input
                placeholder="Target name"
                value={form.name}
                onChange={(e) => setForm({ ...form, name: e.target.value })}
                required
                className="bg-surface-container-high border border-border-glass rounded-lg py-2 px-4 text-body-sm"
              />
              <input
                placeholder="Endpoint URL or host"
                value={form.endpoint}
                onChange={(e) => setForm({ ...form, endpoint: e.target.value })}
                required
                className="bg-surface-container-high border border-border-glass rounded-lg py-2 px-4 text-body-sm"
              />
              <select
                value={form.environment}
                onChange={(e) => setForm({ ...form, environment: e.target.value })}
                className="bg-surface-container-high border border-border-glass rounded-lg py-2 px-4 text-body-sm"
              >
                <option>Production</option>
                <option>Staging</option>
                <option>Development</option>
              </select>
              <input
                placeholder="Target type (API, Web, Cloud...)"
                value={form.targetType}
                onChange={(e) => setForm({ ...form, targetType: e.target.value })}
                required
                className="bg-surface-container-high border border-border-glass rounded-lg py-2 px-4 text-body-sm"
              />
            </div>
            <textarea
              placeholder="Description"
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
              rows={2}
              className="w-full bg-surface-container-high border border-border-glass rounded-lg py-2 px-4 text-body-sm"
            />
            <button
              type="submit"
              disabled={saving}
              className="bg-primary text-white px-6 py-2 rounded-lg font-semibold disabled:opacity-50"
            >
              {saving ? 'Saving...' : 'Save Target'}
            </button>
          </form>
        )}

        <div className="flex gap-3 flex-wrap">
          {[
            { id: 'all', label: 'All' },
            { id: 'production', label: 'Production' },
            { id: 'staging', label: 'Staging' },
            { id: 'development', label: 'Development' },
          ].map((btn) => (
            <button
              key={btn.id}
              onClick={() => setFilter(btn.id)}
              className={`px-4 py-2 rounded-lg font-body-sm ${
                filter === btn.id
                  ? 'bg-primary text-white font-semibold'
                  : 'bg-surface-container-high text-text-muted border border-border-glass'
              }`}
            >
              {btn.label}
            </button>
          ))}
        </div>

        {filteredTargets.length === 0 ? (
          <p className="text-text-muted text-center py-12">
            No audit targets yet. Add one to start scanning.
          </p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-gutter">
            {filteredTargets.map((target) => (
              <div key={target.id} className="glass-panel rounded-xl p-gutter flex flex-col gap-stack-md">
                <div className="flex justify-between items-start">
                  <div>
                    <h3 className="font-h3 text-[20px] text-text-primary">{target.name}</h3>
                    <p className="text-text-muted text-body-sm mt-1">{target.endpoint}</p>
                  </div>
                  <span className="font-label-caps text-[10px] px-2 py-1 rounded font-bold uppercase bg-primary/10 text-primary">
                    {target.environment}
                  </span>
                </div>
                <p className="text-body-sm text-text-muted">{target.description}</p>
                <div className="flex gap-2 mt-auto">
                  <span className="text-body-sm text-text-muted">{target.targetType}</span>
                  <button
                    onClick={() => handleDelete(target.id)}
                    className="ml-auto text-danger-rose text-body-sm hover:underline"
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
