<template>
  <v-container fluid class="fill-height" style="background: linear-gradient(135deg, #1976D2 0%, #1565C0 100%)">
    <v-row justify="center" align="center">
      <v-col cols="12" sm="8" md="5" lg="4">
        <v-card elevation="8" rounded="lg" class="pa-4">
          <v-card-title class="text-center pt-6 pb-2">
            <v-icon size="48" color="primary" class="mb-2">mdi-check-circle</v-icon>
            <div class="text-h5 font-weight-bold">JTech TaskList</div>
            <div class="text-body-2 text-medium-emphasis mt-1">Entre na sua conta</div>
          </v-card-title>

          <v-card-text class="pt-4">
            <v-alert v-if="errorMessage" type="error" variant="tonal" class="mb-4" closable @click:close="errorMessage = ''">
              {{ errorMessage }}
            </v-alert>

            <v-form ref="formRef" @submit.prevent="handleLogin">
              <v-text-field
                v-model="form.email"
                label="Email"
                type="email"
                prepend-inner-icon="mdi-email"
                :rules="emailRules"
                variant="outlined"
                class="mb-2"
                autofocus
              />

              <v-text-field
                v-model="form.password"
                label="Senha"
                :type="showPassword ? 'text' : 'password'"
                prepend-inner-icon="mdi-lock"
                :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                :rules="passwordRules"
                variant="outlined"
                @click:append-inner="showPassword = !showPassword"
              />

              <v-btn
                type="submit"
                color="primary"
                size="large"
                block
                class="mt-4"
                :loading="loading"
              >
                Entrar
              </v-btn>
            </v-form>
          </v-card-text>

          <v-card-actions class="justify-center pb-6">
            <span class="text-body-2 text-medium-emphasis">Não tem conta?</span>
            <v-btn variant="text" color="primary" size="small" :to="{ name: 'register' }">
              Cadastre-se
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useAuthStore } from '@/stores/authStore'

const authStore = useAuthStore()

const formRef = ref()
const loading = ref(false)
const showPassword = ref(false)
const errorMessage = ref('')

const form = reactive({ email: '', password: '' })

const emailRules = [
  (v: string) => !!v || 'Email é obrigatório',
  (v: string) => /.+@.+\..+/.test(v) || 'Email inválido'
]

const passwordRules = [
  (v: string) => !!v || 'Senha é obrigatória'
]

async function handleLogin() {
  const { valid } = await formRef.value.validate()
  if (!valid) return

  loading.value = true
  errorMessage.value = ''
  try {
    await authStore.login(form.email, form.password)
  } catch (e: any) {
    errorMessage.value = e.response?.data?.message || 'Email ou senha incorretos'
  } finally {
    loading.value = false
  }
}
</script>
