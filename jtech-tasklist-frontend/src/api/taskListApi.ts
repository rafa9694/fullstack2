import http from './http'
import type { TaskList, CreateTaskListPayload } from '@/models'

export const taskListApi = {
  getAll: (): Promise<TaskList[]> =>
    http.get('/task-lists').then((r) => r.data),

  create: (payload: CreateTaskListPayload): Promise<TaskList> =>
    http.post('/task-lists', payload).then((r) => r.data),

  update: (id: number, payload: CreateTaskListPayload): Promise<TaskList> =>
    http.put(`/task-lists/${id}`, payload).then((r) => r.data),

  remove: (id: number): Promise<void> =>
    http.delete(`/task-lists/${id}`).then((r) => r.data)
}
