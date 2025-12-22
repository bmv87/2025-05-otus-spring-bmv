import { Env } from '@/env'
class ResponseError extends Error {
  constructor (serverError) {
    super(serverError?.message || 'Server error')
    this.name = 'ResponseError'
    this.error = serverError?.message || serverError?.title || serverError?.code
      ? { ...serverError }
      : undefined
  }
}
class UnauthorizedError extends Error {
  constructor (status) {
    super('Unauthorized Error')
    this.name = 'UnauthorizedError'
    this.status = status
  }
}
class ForbiddenError extends Error {
  constructor (status) {
    super('Access denied')
    this.name = 'ForbiddenError'
    this.status = status
  }
}
class NetworkError extends Error {
  constructor (message, status) {
    super(message || 'Network Error')
    this.name = 'NetworkError'
    this.status = status
  }
}
async function readResponse (response) {
  const contentType = response.headers.get('content-type')
  const contentLength = response.headers.get('content-length')

  if (contentLength === 0) {
    return {}
  }
  return contentType && contentType.includes('application/json')
    ? await response.json()
    : {}
}
async function send ({ routePath, params, body, options }) {
  const credentials = localStorage.getItem('auth')
  const qs = params ? new URLSearchParams(params).toString() : ''
  let resp = null
  try {
    resp = await fetch(`${Env.API_BASE_URL}/${routePath}${qs ? '?' + qs : ''}`, {
      ...options,
      body: body ? JSON.stringify(body) : undefined,
      headers: {
        'content-type': 'application/json;charset=utf-8',
        ...options.headers,
        'Authorization': credentials ? `Basic ${credentials}` : undefined,
      },
      // mode: 'no-cors',
    })
  } catch (error) {
    throw new NetworkError(error)
  }

  if (resp?.ok) {
    return await readResponse(resp)
  }
  if (resp?.status === 401) {
    throw new UnauthorizedError(401)
  }
  if (resp?.status === 403) {
    throw new ForbiddenError(403)
  }
  try {
    const error = await resp.json()
    throw new ResponseError(error)
  } catch (error) {
    if (error.name === 'ResponseError') {
      throw error
    }
    throw new NetworkError(resp.statusText, resp.status)
  }
}

async function get ({ routePath, params, headers }) {
  return send({ routePath, params, headers, options: { method: 'GET' } })
}
async function deletefN ({ routePath, headers }) {
  return send({ routePath, headers, options: { method: 'DELETE' } })
}
async function put ({ routePath, headers, body }) {
  return send({ routePath, headers, body, options: { method: 'PUT' } })
}
async function post ({ routePath, headers, body }) {
  return send({ routePath, headers, body, options: { method: 'POST' } })
}

export const http = {
  get,
  put,
  post,
  delete: deletefN,
}
