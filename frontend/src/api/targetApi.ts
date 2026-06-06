import apiClient from './client'

export interface Target {
  id: number
  name: string
  environment: string
  targetType: string
  endpoint: string
  postureScore: number
  activeFindings: number
  remediationRate: number
  assetsInScope: number
  description: string
  createdAt: string
}

export interface CreateTargetRequest {
  name: string
  environment: string
  targetType: string
  endpoint: string
  description?: string
  assetsInScope?: number
}

export const getTargets = async (): Promise<Target[]> => {
  const response = await apiClient.get('/targets')
  return response.data
}

export const createTarget = async (target: CreateTargetRequest) => {
  const response = await apiClient.post('/targets', target)
  return response.data
}

export const deleteTarget = async (id: number) => {
  await apiClient.delete(`/targets/${id}`)
}
