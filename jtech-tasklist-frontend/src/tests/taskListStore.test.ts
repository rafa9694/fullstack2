import { describe, it, expect, vi, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useTaskListStore } from '@/stores/taskListStore'
import * as taskListApi from '@/api/taskListApi'

vi.mock('@/api/taskListApi')

const mockLists = [
  { id: 1, name: 'Trabalho', userId: 10 },
  { id: 2, name: 'Pessoal', userId: 10 }
]

describe('taskListStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('deve carregar listas', async () => {
    vi.spyOn(taskListApi.taskListApi, 'getAll').mockResolvedValue(mockLists)
    const store = useTaskListStore()

    await store.fetchAll()

    expect(store.taskLists).toHaveLength(2)
    expect(store.taskLists[0].name).toBe('Trabalho')
    expect(store.loading).toBe(false)
  })

  it('deve criar lista e adicionar ao estado', async () => {
    const newList = { id: 3, name: 'Nova', userId: 10 }
    vi.spyOn(taskListApi.taskListApi, 'create').mockResolvedValue(newList)
    const store = useTaskListStore()
    store.$patch({ taskLists: [...mockLists] })

    await store.create('Nova')

    expect(store.taskLists).toHaveLength(3)
    expect(store.taskLists[2].name).toBe('Nova')
  })

  it('deve remover lista do estado', async () => {
    vi.spyOn(taskListApi.taskListApi, 'remove').mockResolvedValue(undefined)
    const store = useTaskListStore()
    store.$patch({ taskLists: [...mockLists] })

    await store.remove(1)

    expect(store.taskLists).toHaveLength(1)
    expect(store.taskLists[0].id).toBe(2)
  })

  it('deve definir erro quando fetchAll falha', async () => {
    vi.spyOn(taskListApi.taskListApi, 'getAll').mockRejectedValue({
      response: { data: { message: 'Erro de rede' } }
    })
    const store = useTaskListStore()

    await store.fetchAll()

    expect(store.error).toBe('Erro de rede')
    expect(store.loading).toBe(false)
  })
})
