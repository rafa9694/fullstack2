import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/authApi'
import type { User } from '@/models'
import router from '@/router'

export const useAuthStore = defineStore(
  'auth',
  () => {
    const user = ref<User | null>(null)
    const accessToken = ref<string | null>(null)
    const refreshToken = ref<string | null>(null)

    const isAuthenticated = computed(() => !!accessToken.value)

    async function register(name: string, email: string, password: string) {
      const registeredUser = await authApi.register(name, email, password)
      await login(email, password)
      return registeredUser
    }

    async function login(email: string, password: string) {
      const tokens = await authApi.login(email, password)
      accessToken.value = tokens.accessToken
      refreshToken.value = tokens.refreshToken
      localStorage.setItem('accessToken', tokens.accessToken)
      localStorage.setItem('refreshToken', tokens.refreshToken)
      await router.push('/app')
    }

    function logout() {
      user.value = null
      accessToken.value = null
      refreshToken.value = null
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
      router.push('/login')
    }

    function initFromStorage() {
      const stored = localStorage.getItem('accessToken')
      const storedRefresh = localStorage.getItem('refreshToken')
      if (stored && storedRefresh) {
        accessToken.value = stored
        refreshToken.value = storedRefresh
      }
    }

    return { user, accessToken, refreshToken, isAuthenticated, register, login, logout, initFromStorage }
  },
  { persist: true }
)
