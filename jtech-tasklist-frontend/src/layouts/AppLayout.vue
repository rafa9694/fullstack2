<template>
  <v-layout>
    <!-- Navigation Drawer -->
    <v-navigation-drawer v-model="drawer" :rail="rail" permanent>
      <v-list-item
        prepend-icon="mdi-check-circle"
        title="JTech TaskList"
        nav
      >
        <template #append>
          <v-btn
            :icon="rail ? 'mdi-chevron-right' : 'mdi-chevron-left'"
            variant="text"
            @click="rail = !rail"
          />
        </template>
      </v-list-item>

      <v-divider />

      <v-list density="compact" nav>
        <v-list-item
          prepend-icon="mdi-view-dashboard"
          title="Dashboard"
          :to="{ name: 'dashboard' }"
          :active="$route.name === 'dashboard'"
        />
      </v-list>

      <v-divider />

      <v-list density="compact" nav>
        <v-list-subheader v-if="!rail">MINHAS LISTAS</v-list-subheader>

        <v-list-item
          v-for="list in taskListStore.taskLists"
          :key="list.id"
          :prepend-icon="'mdi-format-list-bulleted'"
          :title="list.name"
          :to="{ name: 'task-list', params: { id: list.id } }"
          :active="$route.params.id === String(list.id)"
        />

        <v-list-item
          prepend-icon="mdi-plus"
          title="Nova Lista"
          @click="showCreateDialog = true"
        />
      </v-list>

      <template #append>
        <v-list density="compact" nav>
          <v-list-item
            prepend-icon="mdi-logout"
            title="Sair"
            @click="authStore.logout()"
          />
        </v-list>
      </template>
    </v-navigation-drawer>

    <!-- Top Bar -->
    <v-app-bar elevation="1">
      <v-app-bar-title>
        <span class="text-h6">{{ pageTitle }}</span>
      </v-app-bar-title>

      <template #append>
        <v-btn icon="mdi-bell" variant="text" />
        <v-avatar color="primary" size="36" class="mr-3" style="cursor:pointer">
          <span class="text-body-2 text-white">{{ userInitials }}</span>
        </v-avatar>
      </template>
    </v-app-bar>

    <!-- Main content -->
    <v-main>
      <v-container fluid class="pa-6">
        <router-view />
      </v-container>
    </v-main>

    <!-- Create List Dialog -->
    <TaskListFormDialog
      v-model="showCreateDialog"
      title="Nova Lista"
      @save="handleCreateList"
    />

    <!-- Snackbar global -->
    <v-snackbar v-model="snackbar.show" :color="snackbar.color" :timeout="3000">
      {{ snackbar.message }}
    </v-snackbar>
  </v-layout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { useTaskListStore } from '@/stores/taskListStore'
import TaskListFormDialog from '@/components/tasklist/TaskListFormDialog.vue'

const authStore = useAuthStore()
const taskListStore = useTaskListStore()
const route = useRoute()

const drawer = ref(true)
const rail = ref(false)
const showCreateDialog = ref(false)

const snackbar = ref({ show: false, message: '', color: 'success' })

const pageTitle = computed(() => {
  if (route.name === 'dashboard') return 'Dashboard'
  if (route.name === 'task-list') {
    const list = taskListStore.taskLists.find((tl) => tl.id === Number(route.params.id))
    return list?.name ?? 'Lista'
  }
  return 'JTech TaskList'
})

const userInitials = computed(() => {
  const name = authStore.user?.name ?? 'U'
  return name.charAt(0).toUpperCase()
})

async function handleCreateList(name: string) {
  try {
    await taskListStore.create(name)
    snackbar.value = { show: true, message: 'Lista criada com sucesso!', color: 'success' }
  } catch {
    snackbar.value = { show: true, message: 'Erro ao criar lista', color: 'error' }
  }
}

onMounted(() => {
  taskListStore.fetchAll()
})
</script>
