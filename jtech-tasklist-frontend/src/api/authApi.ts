import http from './http'
import type { AuthTokens, User } from '@/models'

export const authApi = {
  register: (name: string, email: string, password: string): Promise<User> =>
    http.post('/auth/register', { name, email, password }).then((r) => r.data),

  login: (email: string, password: string): Promise<AuthTokens> =>
    http.post('/auth/login', { email, password }).then((r) => r.data),

  refresh: (refreshToken: string): Promise<AuthTokens> =>
    http.post('/auth/refresh', { refreshToken }).then((r) => r.data)
}
