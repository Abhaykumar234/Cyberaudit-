import { useState } from 'react'
import { evaluateLab } from '../api/labApi'
import Loading from '../components/Loading'

interface LabResult {
  missionId: string
  status: string
  vulnerabilityAnalysis: string
  secureRepairLogic: string
  tacticalHints: string
  testSuite: string
  isVulnerable: boolean
}

export default function Labs() {
  const [mission, setMission] = useState('')
  const [codeSnippet, setCodeSnippet] = useState('')
  const [language, setLanguage] = useState('javascript')
  const [result, setResult] = useState<LabResult | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!mission || !codeSnippet) {
      setError('Please fill in all fields')
      return
    }

    try {
      setLoading(true)
      setError(null)
      const data = await evaluateLab({ mission, codeSnippet, language })
      setResult(data)
    } catch (err) {
      setError('Failed to evaluate lab: ' + (err as Error).message)
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  if (loading && !result) return <Loading />

  return (
    <div className="p-gutter bg-background min-h-screen">
      <div className="max-w-container-max mx-auto space-y-stack-lg">
        {/* Header */}
        <div>
          <h1 className="font-h1 text-h1 text-text-primary tracking-tight">Secure Coding Labs</h1>
          <p className="font-body-md text-text-muted mt-1">
            Interactive penetration testing and code analysis environment.
          </p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-gutter">
          {/* Input Form */}
          <div className="glass-panel rounded-xl p-gutter flex flex-col gap-stack-md">
            <div className="flex items-center gap-2">
              <span className="material-symbols-outlined text-primary text-[24px]">code_blocks</span>
              <h2 className="font-h3 text-h3 text-text-primary">Lab Mission</h2>
            </div>

            <form onSubmit={handleSubmit} className="space-y-stack-md flex flex-col">
              <div className="space-y-stack-sm">
                <label className="font-label-caps text-label-caps text-primary uppercase">
                  Mission Title
                </label>
                <input
                  type="text"
                  value={mission}
                  onChange={(e) => setMission(e.target.value)}
                  placeholder="e.g., Fix XSS vulnerability"
                  className="w-full bg-surface-container-high border border-border-glass rounded-lg py-2 px-4 text-body-sm text-text-primary focus:border-primary outline-none transition-all placeholder:text-text-muted/30"
                />
              </div>

              <div className="space-y-stack-sm">
                <label className="font-label-caps text-label-caps text-primary uppercase">
                  Programming Language
                </label>
                <select
                  value={language}
                  onChange={(e) => setLanguage(e.target.value)}
                  className="w-full bg-surface-container-high border border-border-glass rounded-lg py-2 px-4 text-body-sm text-text-primary focus:border-primary outline-none transition-all"
                >
                  <option value="javascript">JavaScript</option>
                  <option value="typescript">TypeScript</option>
                  <option value="python">Python</option>
                  <option value="java">Java</option>
                  <option value="csharp">C#</option>
                </select>
              </div>

              <div className="space-y-stack-sm flex-grow">
                <label className="font-label-caps text-label-caps text-primary uppercase">
                  Code Snippet
                </label>
                <textarea
                  value={codeSnippet}
                  onChange={(e) => setCodeSnippet(e.target.value)}
                  placeholder="Paste your code here..."
                  rows={12}
                  className="w-full bg-surface-container-high border border-border-glass rounded-lg py-2 px-4 text-code-sm text-text-primary focus:border-primary outline-none transition-all placeholder:text-text-muted/30 font-code-md resize-none"
                />
              </div>

              {error && (
                <div className="bg-danger-rose/10 border border-danger-rose/30 text-danger-rose p-3 rounded-lg text-body-sm">
                  {error}
                </div>
              )}

              <button
                type="submit"
                disabled={loading}
                className="w-full bg-primary text-white py-2.5 rounded-lg hover:opacity-90 transition disabled:opacity-50 disabled:cursor-not-allowed font-body-md font-semibold flex items-center justify-center gap-2 shadow-[0_0_15px_rgba(37,99,235,0.3)] active:scale-95"
              >
                <span className="material-symbols-outlined text-[18px]">
                  {loading ? 'hourglass_bottom' : 'play_arrow'}
                </span>
                {loading ? 'Analyzing...' : 'Execute Code Analysis'}
              </button>
            </form>
          </div>

          {/* Results */}
          <div className="glass-panel rounded-xl p-gutter flex flex-col gap-stack-md">
            <div className="flex items-center gap-2">
              <span className="material-symbols-outlined text-primary text-[24px]">assessment</span>
              <h2 className="font-h3 text-h3 text-text-primary">Analysis Result</h2>
            </div>

            {result ? (
              <div className="space-y-stack-md">
                <div
                  className={`p-4 rounded-lg flex items-center gap-3 border ${
                    result.isVulnerable
                      ? 'bg-danger-rose/10 border-danger-rose/30'
                      : 'bg-success-emerald/10 border-success-emerald/30'
                  }`}
                >
                  <span
                    className={`material-symbols-outlined text-[24px] ${
                      result.isVulnerable ? 'text-danger-rose' : 'text-success-emerald'
                    }`}
                  >
                    {result.isVulnerable ? 'warning' : 'verified'}
                  </span>
                  <p
                    className={`font-semibold ${
                      result.isVulnerable ? 'text-danger-rose' : 'text-success-emerald'
                    }`}
                  >
                    {result.isVulnerable ? 'Vulnerable Code Detected' : 'Code is Secure'}
                  </p>
                </div>

                <div className="space-y-stack-sm">
                  <h3 className="font-label-caps text-label-caps text-primary uppercase">
                    Vulnerability Analysis
                  </h3>
                  <p className="text-body-sm text-text-muted">{result.vulnerabilityAnalysis}</p>
                </div>

                <div className="border-t border-border-glass"></div>

                <div className="space-y-stack-sm">
                  <h3 className="font-label-caps text-label-caps text-primary uppercase">
                    Secure Repair Logic
                  </h3>
                  <div className="code-block rounded-lg overflow-hidden bg-[#000] p-4">
                    <pre className="text-code-sm text-primary-fixed-dim font-code-md whitespace-pre-wrap break-words">
                      {result.secureRepairLogic}
                    </pre>
                  </div>
                </div>

                <div className="border-t border-border-glass"></div>

                <div className="space-y-stack-sm">
                  <h3 className="font-label-caps text-label-caps text-primary uppercase">
                    Tactical Hints
                  </h3>
                  <p className="text-body-sm text-text-muted">{result.tacticalHints}</p>
                </div>

                <div className="border-t border-border-glass"></div>

                <div className="space-y-stack-sm">
                  <h3 className="font-label-caps text-label-caps text-primary uppercase">
                    Test Suite
                  </h3>
                  <div className="code-block rounded-lg overflow-hidden bg-[#000] p-4">
                    <pre className="text-code-sm text-success-emerald font-code-md whitespace-pre-wrap break-words">
                      {result.testSuite}
                    </pre>
                  </div>
                </div>
              </div>
            ) : (
              <div className="flex-grow flex flex-col items-center justify-center text-center py-12 text-text-muted">
                <span className="material-symbols-outlined text-[48px] mb-4 opacity-50">
                  code_blocks
                </span>
                <p className="text-body-md">Submit code to analyze for security vulnerabilities</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
