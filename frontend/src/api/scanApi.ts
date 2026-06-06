import apiClient from './client'

export interface ScanResult {
  targetUrl: string
  host: string
  timestamp: string
  status: string
  error?: string
  findings: Finding[]
  severityCount: {
    CRITICAL: number
    HIGH: number
    MEDIUM: number
    LOW: number
  }
}

export interface Finding {
  type: string
  title: string
  description: string
  severity: string
  category: string
  remediation?: string
}

export const scanUrl = (url: string): Promise<ScanResult> => {
  return apiClient.post('/scan/url', { url }).then((response) => response.data)
}

export const scanTarget = (targetId: number): Promise<ScanResult> => {
  return apiClient.post(`/scan/target/${targetId}`).then((response) => response.data)
}

export const getScannerStatus = (): Promise<{ status: string; version: string; capabilities: string }> => {
  return apiClient.get('/scan/status').then((response) => response.data)
}
