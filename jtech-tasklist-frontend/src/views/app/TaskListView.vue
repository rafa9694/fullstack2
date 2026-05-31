<template>
  <div>
    <div class="d-flex align-center mb-6">
      <v-btn icon="mdi-arrow-left" variant="text" :to="{ name: 'dashboard' }" class="mr-2" />
      <div>
        <h1 class="text-h5 font-weight-bold">{{ currentList?.name }}</h1>
        <p class="text-body-2 text-medium-emphasis mt-1">
          {{ taskStore.tasks.length }} tarefa(s)
        </p>
      </div>
      <v-spacer />
      <v-btn color="primary" prepend-icon="mdi-plus" @click="showCreateDialog = true">
        Nova Tarefa
      </v-btn>
    </div>

    <!-- Filters -->
    <v-btn-toggle v-model="filter" mandatory class="mb-6" color="primary" variant="outlined" density="compact">
      <v-btn value="all">Todas</v-btn>
      <v-btn value="pending">Pendentes</v-btn>
      <v-btn value="completed">Concluídas</v-btn>
    </v-btn-toggle>

    <!-- Loading -->
    <div v-if="taskStore.loading" class="d-flex justify-center py-12">
      <v-progress-circular indeterminate color="primary" size="48" />
    </div>

    <!-- Empty -->
    <div v-else-if="filteredTasks.length === 0" class="text-center py-16">
      <v-icon size="72" color="grey-lighten-2">mdi-checkbox-marked-circle-outline</v-icon>
      <p class="text-h6 text-medium-emphasis mt-4">
        {{ filter === 'all' ? 'Nenhuma tarefa ainda' : 'Nenhuma tarefa ' + (filter === 'completed' ? 'concluída' : 'pendente') }}
      </p>
      <v-btn v-if="filter === 'all'" color="primary" class="mt-4" prepend-icon="mdi-plus" @click="showCreateDialog = true">
        Criar Tarefa
      </v-btn>
    </div>

    <!-- Task list -->
    <v-row v-else>
      <v-col v-for="task in filteredTasks" :key="task.id" cols="12" sm="6" md="4">
        <TaskCard
          :task="task"
          @complete="handleComplete"
          @edit="handleEdit"
          @delete="handleDelete"
        />
      </v-col>
    </v-row>

    <!-- Create Dialog -->
    <TaskFormDialog
      v-model="showCreateDialog"
      title="Nova Tarefa"
      :task-list-id="listId"
      @save="handleCreate"
    />

    <!-- Edit Dialog -->
    <TaskFormDialog
      v-model="showEditDialog"
      title="Editar Tarefa"
      :task-list-id="listId"
      :initial-task="editingTask"
      @save="handleUpdate"
    />

    <!-- Delete Confirm -->
    <ConfirmDialog
      v-model="showDeleteDialog"
      title="Excluir Tarefa"
      :message="`Deseja excluir '${deletingTask?.title}'?`"
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
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useTaskStore } from '@/stores/taskStore'
import { useTaskListStore } from '@/stores/taskListStore'
import type { Task } from '@/models'
import TaskCard from '@/components/task/TaskCard.vue'
import TaskFormDialog from '@/components/task/TaskFormDialog.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'

const route = useRoute()
const taskStore = useTaskStore()
const taskListStore = useTaskListStore()

const listId = computed(() => Number(route.params.id))
const currentList = computed(() => taskListStore.taskLists.find((tl) => tl.id === listId.value))
const filter = ref<'all' | 'pending' | 'completed'>('all')

const filteredTasks = computed(() => {
  if (filter.value === 'completed') return taskStore.tasks.filter((t) => t.completed)
  if (filter.value === 'pending') return taskStore.tasks.filter((t) => !t.completed)
  return taskStore.tasks
})

const showCreateDialog = ref(false)
const showEditDialog = ref(false)
const showDeleteDialog = ref(false)
const editingTask = ref<Task | null>(null)
const deletingTask = ref<Task | null>(null)
const snackbar = ref({ show: false, message: '', color: 'success' })

function handleEdit(task: Task) { editingTask.value = task; showEditDialog.value = true }
function handleDelete(task: Task) { deletingTask.value = task; showDeleteDialog.value = true }

async function handleComplete(task: Task) {
  try {
    await taskStore.complete(task.id)
  } catch {
    snackbar.value = { show: true, message: 'Erro ao concluir tarefa', color: 'error' }
  }
}

async function handleCreate(payload: { title: string; description: string }) {
  try {
    await taskStore.create({ ...payload, taskListId: listId.value })
    snackbar.value = { show: true, message: 'Tarefa criada!', color: 'success' }
  } catch {
    snackbar.value = { show: true, message: taskStore.error || 'Erro ao criar', color: 'error' }
  }
}

async function handleUpdate(payload: { title: string; description: string }) {
  if (!editingTask.value) return
  try {
    await taskStore.update(editingTask.value.id, payload)
    snackbar.value = { show: true, message: 'Tarefa atualizada!', color: 'success' }
  } catch {
    snackbar.value = { show: true, message: taskStore.error || 'Erro ao atualizar', color: 'error' }
  }
}

async function confirmDelete() {
  if (!deletingTask.value) return
  try {
    await taskStore.remove(deletingTask.value.id)
    snackbar.value = { show: true, message: 'Tarefa excluída!', color: 'success' }
  } catch {
    snackbar.value = { show: true, message: taskStore.error || 'Erro ao excluir', color: 'error' }
  }
}

watch(listId, (id) => { if (id) taskStore.fetchByList(id) })
onMounted(() => taskStore.fetchByList(listId.value))
onUnmounted(() => taskStore.clearTasks())
</script>
