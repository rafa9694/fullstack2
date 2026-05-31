import { describe, it, expect, vi, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useAuthStore } from '@/stores/authStore'
import * as authApi from '@/api/authApi'

vi.mock('@/api/authApi')
vi.mock('@/router', () => ({ default: { push: vi.fn() } }))

describe('authStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
    vi.clearAllMocks()
  })

  it('deve iniciar sem autenticação', () => {
    const store = useAuthStore()
    expect(store.isAuthenticated).toBe(false)
    expect(store.accessToken).toBeNull()
  })

  it('deve fazer login e salvar tokens', async () => {
    const store = useAuthStore()
    vi.spyOn(authApi.authApi, 'login').mockResolvedValue({
      accessToken: 'token123',
      refreshToken: 'refresh123',
      tokenType: 'Bearer'
    })

    await store.login('user@test.com', 'password')

    expect(store.accessToken).toBe('token123')
    expect(store.isAuthenticated).toBe(true)
    expect(localStorage.getItem('accessToken')).toBe('token123')
  })

  it('deve fazer logout e limpar estado', async () => {
    const store = useAuthStore()
    store.$patch({ accessToken: 'token123', refreshToken: 'refresh123' })

    store.logout()

    expect(store.accessToken).toBeNull()
    expect(store.isAuthenticated).toBe(false)
    expect(localStorage.getItem('accessToken')).toBeNull()
  })

  it('deve inicializar com token do localStorage', () => {
    localStorage.setItem('accessToken', 'storedToken')
    localStorage.setItem('refreshToken', 'storedRefresh')

    const store = useAuthStore()
    store.initFromStorage()

    expect(store.accessToken).toBe('storedToken')
  })
})
