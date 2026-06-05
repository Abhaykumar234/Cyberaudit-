import apiClient from './client'

export interface Metrics {
  postureScore: number
  activeFindings: number
  criticalCount: number
  highCount: number
  mediumCount: number
  totalFindings: number
  remediationRate: number
  assetsInScope: number
  globalThreatLevel: string
  activeEndpoints: number
  engineStatus: string
}

export const getSystemMetrics = (): Promise<Metrics> => {
  return apiClient.get('/metrics/system').then((response) => response.data)
}
