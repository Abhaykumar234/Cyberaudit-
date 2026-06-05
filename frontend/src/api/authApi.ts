import apiClient, { AuthUser, storeAuth } from './client'

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  email: string
  fullName: string
}

export interface LoginResponse {
  token: string
  username: string
  role: string
  fullName: string
}

export const login = async (request: LoginRequest): Promise<LoginResponse> => {
  const response = await apiClient.post<LoginResponse>('/auth/login', request)
  const data = response.data
  storeAuth(data.token, {
    username: data.username,
    role: data.role,
    fullName: data.fullName,
  })
  return data
}

export const register = async (request: RegisterRequest): Promise<LoginResponse> => {
  const response = await apiClient.post<LoginResponse>('/auth/register', request)
  const data = response.data
  storeAuth(data.token, {
    username: data.username,
    role: data.role,
    fullName: data.fullName,
  })
  return data
}

export const logout = () => {
  localStorage.removeItem('auth_token')
  localStorage.removeItem('auth_user')
}

export type { AuthUser }
