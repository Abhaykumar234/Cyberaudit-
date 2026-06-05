import apiClient from './client'

export interface LabRequest {
  mission: string
  codeSnippet: string
  language?: string
  vulnerabilityType?: string
}

export interface LabResponse {
  missionId: string
  status: string
  vulnerabilityAnalysis: string
  secureRepairLogic: string
  tacticalHints: string
  testSuite: string
  isVulnerable: boolean
}

export const evaluateLab = (request: LabRequest): Promise<LabResponse> => {
  return apiClient.post('/lab/evaluate', request).then((response) => response.data)
}

export const getLabHealth = (): Promise<string> => {
  return apiClient.get('/lab/health').then((response) => response.data)
}
