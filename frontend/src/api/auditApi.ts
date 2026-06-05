import apiClient from './client'

export interface AuditRequest {
  targetId: string
  auditType: string
  scope?: string
  includeRemediationAdvice?: boolean
}

export interface AuditResponse {
  id: number
  targetId: string
  auditType: string
  status: string
  startTime: string
  endTime: string
  findingsCount: number
  criticalCount: number
  highCount: number
  summary: string
  findings: Finding[]
}

export interface Finding {
  id: string
  title: string
  severity: string
  category: string
  description: string
  threatVector: string
  remediationAdvice: string
  occurrences: number
  affectedEndpoints: number
}

export const generateAudit = (request: AuditRequest): Promise<AuditResponse> => {
  return apiClient.post('/audit/generate', request).then((response) => response.data)
}

export const getAuditHealth = (): Promise<string> => {
  return apiClient.get('/audit/health').then((response) => response.data)
}
