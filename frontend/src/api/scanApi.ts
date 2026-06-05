import axios from 'axios'

const API_BASE_URL = '/api'

const scanApi = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true
})

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

export interface ScanRequest {
  url: string
}

export const scanUrl = (url: string): Promise<ScanResult> => {
  return scanApi.post('/scan/url', { url })
    .then(response => response.data)
}

export const scanTarget = (targetId: number): Promise<ScanResult> => {
  return scanApi.post(`/scan/target/${targetId}`)
    .then(response => response.data)
}

export const getScannerStatus = (): Promise<any> => {
  return scanApi.get('/scan/status')
    .then(response => response.data)
}

export default scanApi
