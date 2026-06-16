const API_URL = import.meta.env.VITE_API_URL || ''

async function request(endpoint, options = {}) {
  const url = `${API_URL}${endpoint}`
  const config = {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    credentials: 'include',
    ...options,
  }

  if (!config.body) delete config.body

  const res = await fetch(url, config)

  if (res.status === 204) return null

  const data = await res.json()

  if (!res.ok) {
    throw new Error(data.error || data.message || 'Error en la solicitud')
  }

  return data
}

export const api = {
  get: (endpoint) => request(endpoint, { method: 'GET' }),
  post: (endpoint, body) => request(endpoint, { method: 'POST', body: JSON.stringify(body) }),
  put: (endpoint, body) => request(endpoint, { method: 'PUT', body: JSON.stringify(body) }),
  delete: (endpoint) => request(endpoint, { method: 'DELETE' }),

  auth: {
    login: (email, password) => {
      const params = new URLSearchParams({ username: email, password })
      return fetch(`${API_URL}/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        credentials: 'include',
        redirect: 'manual',
      })
    },
    registro: (data) => api.post('/api/auth/registro', data),
    me: () => api.get('/api/auth/me'),
    logout: () => fetch(`${API_URL}/logout`, { method: 'POST', credentials: 'include' }),
  },
}

export default api
