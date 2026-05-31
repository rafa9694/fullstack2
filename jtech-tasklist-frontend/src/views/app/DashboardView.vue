<template>
  <div>
    <div class="d-flex align-center mb-6">
      <div>
        <h1 class="text-h5 font-weight-bold">Dashboard</h1>
        <p class="text-body-2 text-medium-emphasis mt-1">
          Gerencie suas listas de tarefas
        </p>
      </div>
      <v-spacer />
      <v-btn color="primary" prepend-icon="mdi-plus" @click="showCreateDialog = true">
        Nova Lista
      </v-btn>
    </div>

    <!-- Loading state -->
    <div v-if="taskListStore.loading" class="d-flex justify-center py-12">
      <v-progress-circular indeterminate color="primary" size="48" />
    </div>

    <!-- Empty state -->
    <div v-else-if="taskListStore.taskLists.length === 0" class="text-center py-16">
      <v-icon size="80" color="grey-lighten-2">mdi-format-list-bulleted</v-icon>
      <p class="text-h6 text-medium-emphasis mt-4">Nenhuma lista criada ainda</p>
      <p class="text-body-2 text-medium-emphasis">Crie sua primeira lista para começar</p>
      <v-btn color="primary" class="mt-4" prepend-icon="mdi-plus" @click="showCreateDialog = true">
        Criar Lista
      </v-btn>
    </div>

    <!-- Lists Grid -->
    <v-row v-else>
      <v-col
        v-for="list in taskListStore.taskLists"
        :key="list.id"
        cols="12"
        sm="6"
        md="4"
        lg="3"
      >
        <TaskListCard
          :task-list="list"
          @edit="handleEdit"
          @delete="handleDelete"
        />
      </v-col>
    </v-row>

    <!-- Create Dialog -->
    <TaskListFormDialog
      v-model="showCreateDialog"
      title="Nova Lista"
      @save="handleCreate"
    />

    <!-- Edit Dialog -->
    <TaskListFormDialog
      v-model="showEditDialog"
      title="Editar Lista"
      :initial-name="editingList?.name"
      @save="handleUpdate"
    />

    <!-- Delete Confirm Dialog -->
    <ConfirmDialog
      v-model="showDeleteDialog"
      title="Excluir Lista"
      :message="`Deseja excluir a lista '${deletingList?.name}'? Todas as tarefas serão removidas.`"
      confirm-text="Excluir"
      confirm-color="error"
      @confirm="confirmDelete"
    />

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" :timeout="3000">
      {{ snackbar.message }}
    </v-snackbar>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useTaskListStore } from '@/stores/taskListStore'
import type { TaskList } from '@/models'
import TaskListCard from '@/components/tasklist/TaskListCard.vue'
import TaskListFormDialog from '@/components/tasklist/TaskListFormDialog.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'

const taskListStore = useTaskListStore()

const showCreateDialog = ref(false)
const showEditDialog = ref(false)
const showDeleteDialog = ref(false)
const editingList = ref<TaskList | null>(null)
const deletingList = ref<TaskList | null>(null)
const snackbar = ref({ show: false, message: '', color: 'success' })

function handleEdit(list: TaskList) {
  editingList.value = list
  showEditDialog.value = true
}

function handleDelete(list: TaskList) {
  deletingList.value = list
  showDeleteDialog.value = true
}

async function handleCreate(name: string) {
  try {
    await taskListStore.create(name)
    snackbar.value = { show: true, message: 'Lista criada!', color: 'success' }
  } catch {
    snackbar.value = { show: true, message: taskListStore.error || 'Erro ao criar lista', color: 'error' }
  }
}

async function handleUpdate(name: string) {
  if (!editingList.value) return
  try {
    await taskListStore.update(editingList.value.id, name)
    snackbar.value = { show: true, message: 'Lista atualizada!', color: 'success' }
  } catch {
    snackbar.value = { show: true, message: taskListStore.error || 'Erro ao atualizar', color: 'error' }
  }
}

async function confirmDelete() {
  if (!deletingList.value) return
  try {
    await taskListStore.remove(deletingList.value.id)
    snackbar.value = { show: true, message: 'Lista excluída!', color: 'success' }
  } catch {
    snackbar.value = { show: true, message: taskListStore.error || 'Erro ao excluir', color: 'error' }
  }
}

onMounted(() => taskListStore.fetchAll())
</script>
