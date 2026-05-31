import http from './http'
import type { Task, CreateTaskPayload, UpdateTaskPayload } from '@/models'

export const taskApi = {
  getAll: (taskListId: number): Promise<Task[]> =>
    http.get('/tasks', { params: { taskListId } }).then((r) => r.data),

  getById: (id: number): Promise<Task> =>
    http.get(`/tasks/${id}`).then((r) => r.data),

  create: (payload: CreateTaskPayload): Promise<Task> =>
    http.post('/tasks', payload).then((r) => r.data),

  update: (id: number, payload: UpdateTaskPayload): Promise<Task> =>
    http.put(`/tasks/${id}`, payload).then((r) => r.data),

  complete: (id: number): Promise<Task> =>
    http.patch(`/tasks/${id}/complete`).then((r) => r.data),

  remove: (id: number): Promise<void> =>
    http.delete(`/tasks/${id}`).then((r) => r.data)
}
