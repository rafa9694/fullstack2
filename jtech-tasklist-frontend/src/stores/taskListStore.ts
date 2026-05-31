import { defineStore } from 'pinia'
import { ref } from 'vue'
import { taskListApi } from '@/api/taskListApi'
import type { TaskList } from '@/models'

export const useTaskListStore = defineStore('taskList', () => {
  const taskLists = ref<TaskList[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetchAll() {
    loading.value = true
    error.value = null
    try {
      taskLists.value = await taskListApi.getAll()
    } catch (e: any) {
      error.value = e.response?.data?.message || 'Erro ao carregar listas'
    } finally {
      loading.value = false
    }
  }

  async function create(name: string) {
    loading.value = true
    error.value = null
    try {
      const created = await taskListApi.create({ name })
      taskLists.value.push(created)
      return created
    } catch (e: any) {
      error.value = e.response?.data?.message || 'Erro ao criar lista'
      throw e
    } finally {
      loading.value = false
    }
  }

  async function update(id: number, name: string) {
    loading.value = true
    error.value = null
    try {
      const updated = await taskListApi.update(id, { name })
      const index = taskLists.value.findIndex((tl) => tl.id === id)
      if (index !== -1) taskLists.value[index] = updated
      return updated
    } catch (e: any) {
      error.value = e.response?.data?.message || 'Erro ao atualizar lista'
      throw e
    } finally {
      loading.value = false
    }
  }

  async function remove(id: number) {
    loading.value = true
    error.value = null
    try {
      await taskListApi.remove(id)
      taskLists.value = taskLists.value.filter((tl) => tl.id !== id)
    } catch (e: any) {
      error.value = e.response?.data?.message || 'Erro ao remover lista'
      throw e
    } finally {
      loading.value = false
    }
  }

  return { taskLists, loading, error, fetchAll, create, update, remove }
})
