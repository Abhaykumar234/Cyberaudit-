import { Routes, Route, Link, useLocation, Navigate } from 'react-router-dom'
import type { ReactNode } from 'react'
import { useAuth } from './context/AuthContext'
import Dashboard from './pages/Dashboard'
import Vulnerabilities from './pages/Vulnerabilities'
import Labs from './pages/Labs'
import AuditLogs from './pages/AuditLogs'
import Targets from './pages/Targets'
import Scanner from './pages/Scanner'
import Login from './pages/Login'

function ProtectedLayout() {
  const location = useLocation()
  const { user, logout } = useAuth()

  const getActiveNav = () => {
    switch (location.pathname) {
      case '/':
        return 'dashboard'
      case '/scanner':
        return 'scanner'
      case '/vulnerabilities':
        return 'vulnerabilities'
      case '/labs':
        return 'labs'
      case '/targets':
        return 'targets'
      case '/logs':
        return 'logs'
      default:
        return 'dashboard'
    }
  }

  const activeNav = getActiveNav()

  const navItems = [
    { id: 'dashboard', label: 'Dashboard', icon: 'dashboard', path: '/' },
    { id: 'scanner', label: 'Real-Time Scanner', icon: 'radar', path: '/scanner' },
    { id: 'vulnerabilities', label: 'Vulnerability Catalog', icon: 'security', path: '/vulnerabilities' },
    { id: 'labs', label: 'Secure Coding Labs', icon: 'code_blocks', path: '/labs' },
    { id: 'logs', label: 'SOC 2 Audit Trail', icon: 'history_edu', path: '/logs' },
    { id: 'targets', label: 'Audit Targets', icon: 'dns', path: '/targets' },
  ]

  return (
    <div className="flex h-screen bg-background text-text-primary overflow-hidden">
      <header className="fixed top-0 w-full h-[56px] bg-surface-container border-b border-border-glass z-50 flex items-center justify-between px-gutter backdrop-blur-md">
        <div className="flex items-center gap-4">
          <div className="font-h3 text-[20px] font-bold text-primary flex items-center gap-2">
            <span className="material-symbols-outlined" style={{ fontVariationSettings: "'FILL' 1" }}>
              security
            </span>
            CyberAudit Pro
          </div>
        </div>
        <div className="flex items-center gap-4">
          <span className="text-body-sm text-text-muted hidden sm:inline">
            {user?.fullName} ({user?.role})
          </span>
          <button
            onClick={logout}
            className="px-3 py-1.5 rounded-lg text-body-sm text-text-muted hover:text-text-primary hover:bg-surface-container-high transition-colors"
          >
            Sign out
          </button>
        </div>
      </header>

      <nav className="hidden lg:flex fixed left-0 top-0 h-full w-[240px] bg-surface-container border-r border-border-glass flex-col pt-[72px] pb-6 z-40">
        <div className="flex flex-col gap-1 px-3 flex-grow">
          {navItems.map((item) => (
            <Link
              key={item.id}
              to={item.path}
              className={`flex items-center gap-3 px-4 py-2.5 rounded-lg font-body-sm transition-colors ${
                activeNav === item.id
                  ? 'bg-primary/10 text-primary border-r-[3px] border-primary'
                  : 'text-text-muted hover:bg-surface-container-high hover:text-text-primary'
              }`}
            >
              <span className="material-symbols-outlined text-[20px]">{item.icon}</span>
              <span>{item.label}</span>
            </Link>
          ))}
        </div>
      </nav>

      <main className="flex-1 lg:ml-[240px] pt-[56px] overflow-auto">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/scanner" element={<Scanner />} />
          <Route path="/vulnerabilities" element={<Vulnerabilities />} />
          <Route path="/labs" element={<Labs />} />
          <Route path="/targets" element={<Targets />} />
          <Route path="/logs" element={<AuditLogs />} />
        </Routes>
      </main>
    </div>
  )
}

function RequireAuth({ children }: { children: ReactNode }) {
  const { isAuthenticated } = useAuth()
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }
  return <>{children}</>
}

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route
        path="/*"
        element={
          <RequireAuth>
            <ProtectedLayout />
          </RequireAuth>
        }
      />
    </Routes>
  )
}

export default App
