import { defineStore } from 'pinia'
import { ref } from 'vue'
import { taskApi } from '@/api/taskApi'
import type { Task, CreateTaskPayload, UpdateTaskPayload } from '@/models'

export const useTaskStore = defineStore('task', () => {
  const tasks = ref<Task[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetchByList(taskListId: number) {
    loading.value = true
    error.value = null
    try {
      tasks.value = await taskApi.getAll(taskListId)
    } catch (e: any) {
      error.value = e.response?.data?.message || 'Erro ao carregar tarefas'
    } finally {
      loading.value = false
    }
  }

  async function create(payload: CreateTaskPayload) {
    loading.value = true
    error.value = null
    try {
      const created = await taskApi.create(payload)
      tasks.value.push(created)
      return created
    } catch (e: any) {
      error.value = e.response?.data?.message || 'Erro ao criar tarefa'
      throw e
    } finally {
      loading.value = false
    }
  }

  async function update(id: number, payload: UpdateTaskPayload) {
    loading.value = true
    error.value = null
    try {
      const updated = await taskApi.update(id, payload)
      const index = tasks.value.findIndex((t) => t.id === id)
      if (index !== -1) tasks.value[index] = updated
      return updated
    } catch (e: any) {
      error.value = e.response?.data?.message || 'Erro ao atualizar tarefa'
      throw e
    } finally {
      loading.value = false
    }
  }

  async function complete(id: number) {
    loading.value = true
    error.value = null
    try {
      const completed = await taskApi.complete(id)
      const index = tasks.value.findIndex((t) => t.id === id)
      if (index !== -1) tasks.value[index] = completed
      return completed
    } catch (e: any) {
      error.value = e.response?.data?.message || 'Erro ao concluir tarefa'
      throw e
    } finally {
      loading.value = false
    }
  }

  async function remove(id: number) {
    loading.value = true
    error.value = null
    try {
      await taskApi.remove(id)
      tasks.value = tasks.value.filter((t) => t.id !== id)
    } catch (e: any) {
      error.value = e.response?.data?.message || 'Erro ao remover tarefa'
      throw e
    } finally {
      loading.value = false
    }
  }

  function clearTasks() {
    tasks.value = []
  }

  return { tasks, loading, error, fetchByList, create, update, complete, remove, clearTasks }
})
