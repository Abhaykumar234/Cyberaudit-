import apiClient from './client'

export const getAuditLogs = async (page = 0, size = 50) => {
  const response = await apiClient.get(`/logs?page=${page}&size=${size}`)
  return response.data
}

export const getAuditLogsByUser = async (userId: string, page = 0, size = 50) => {
  const response = await apiClient.get(`/logs/user/${userId}?page=${page}&size=${size}`)
  return response.data
}
