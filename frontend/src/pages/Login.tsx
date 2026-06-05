import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { login, register } from '../api/authApi'
import { useAuth } from '../context/AuthContext'

export default function Login() {
  const navigate = useNavigate()
  const { refreshUser } = useAuth()
  const [isRegister, setIsRegister] = useState(false)
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [email, setEmail] = useState('')
  const [fullName, setFullName] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      if (isRegister) {
        await register({ username, password, email, fullName })
      } else {
        await login({ username, password })
      }
      refreshUser()
      navigate('/')
    } catch (err: unknown) {
      const axiosErr = err as { response?: { data?: { error?: string } }; message?: string; code?: string }
      const message =
        axiosErr.response?.data?.error ||
        (axiosErr.code === 'ERR_NETWORK'
          ? 'Cannot reach backend. Start the server on http://localhost:8080 (run .\\START_BACKEND.ps1)'
          : axiosErr.message || 'Authentication failed')
      setError(message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-gutter">
      <div className="glass-panel rounded-xl p-gutter w-full max-w-md space-y-stack-lg">
        <div className="text-center">
          <div className="font-h3 text-[24px] font-bold text-primary flex items-center justify-center gap-2">
            <span className="material-symbols-outlined">security</span>
            CyberAudit Pro
          </div>
          <p className="text-text-muted text-body-sm mt-2">
            {isRegister ? 'Create your account' : 'Sign in to continue'}
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-stack-md">
          {isRegister && (
            <>
              <div>
                <label className="font-label-caps text-label-caps text-primary uppercase block mb-1">
                  Full Name
                </label>
                <input
                  type="text"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  required
                  className="w-full bg-surface-container-high border border-border-glass rounded-lg py-2 px-4 text-body-sm text-text-primary focus:border-primary outline-none"
                />
              </div>
              <div>
                <label className="font-label-caps text-label-caps text-primary uppercase block mb-1">
                  Email
                </label>
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  className="w-full bg-surface-container-high border border-border-glass rounded-lg py-2 px-4 text-body-sm text-text-primary focus:border-primary outline-none"
                />
              </div>
            </>
          )}

          <div>
            <label className="font-label-caps text-label-caps text-primary uppercase block mb-1">
              Username
            </label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              className="w-full bg-surface-container-high border border-border-glass rounded-lg py-2 px-4 text-body-sm text-text-primary focus:border-primary outline-none"
            />
          </div>

          <div>
            <label className="font-label-caps text-label-caps text-primary uppercase block mb-1">
              Password
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              minLength={isRegister ? 8 : 1}
              className="w-full bg-surface-container-high border border-border-glass rounded-lg py-2 px-4 text-body-sm text-text-primary focus:border-primary outline-none"
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
            className="w-full bg-primary text-white py-2.5 rounded-lg hover:opacity-90 transition disabled:opacity-50 font-body-md font-semibold"
          >
            {loading ? 'Please wait...' : isRegister ? 'Register' : 'Sign In'}
          </button>
        </form>

        <p className="text-center text-body-sm text-text-muted">
          {isRegister ? 'Already have an account?' : "Don't have an account?"}{' '}
          <button
            type="button"
            onClick={() => {
              setIsRegister(!isRegister)
              setError(null)
            }}
            className="text-primary font-semibold hover:underline"
          >
            {isRegister ? 'Sign in' : 'Register'}
          </button>
        </p>

        {!isRegister && (
          <p className="text-center text-body-sm text-text-muted">
            Default admin: <code className="text-primary">admin</code> /{' '}
            <code className="text-primary">changeme</code>
          </p>
        )}
      </div>
    </div>
  )
}
