export interface User {
  id: number
  name: string
  email: string
  createdAt: string
}

export interface AuthTokens {
  accessToken: string
  refreshToken: string
  tokenType: string
}

export interface TaskList {
  id: number
  name: string
  userId: number
}

export interface Task {
  id: number
  title: string
  description: string | null
  completed: boolean
  createdAt: string
  taskListId: number
  userId: number
}

export interface CreateTaskListPayload {
  name: string
}

export interface CreateTaskPayload {
  title: string
  description?: string
  taskListId: number
}

export interface UpdateTaskPayload {
  title?: string
  description?: string
}

export interface ApiError {
  status: number
  message: string
  errors: string[]
  timestamp: string
}
