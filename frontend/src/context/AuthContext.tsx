import { useState, useEffect, createContext, useContext, ReactNode } from 'react'
import { getStoredUser, AuthUser, clearAuth } from '../api/client'

interface AuthContextType {
  user: AuthUser | null
  isAuthenticated: boolean
  logout: () => void
  refreshUser: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(getStoredUser())

  const refreshUser = () => setUser(getStoredUser())

  const logout = () => {
    clearAuth()
    setUser(null)
  }

  useEffect(() => {
    setUser(getStoredUser())
  }, [])

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!localStorage.getItem('auth_token'),
        logout,
        refreshUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return context
}
