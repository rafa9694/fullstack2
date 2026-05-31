import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/app'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/auth/LoginView.vue'),
      meta: { requiresGuest: true }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/auth/RegisterView.vue'),
      meta: { requiresGuest: true }
    },
    {
      path: '/app',
      component: () => import('@/layouts/AppLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'dashboard',
          component: () => import('@/views/app/DashboardView.vue')
        },
        {
          path: 'lists/:id',
          name: 'task-list',
          component: () => import('@/views/app/TaskListView.vue')
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/app'
    }
  ]
})

router.beforeEach((to, _from, next) => {
  const accessToken = localStorage.getItem('accessToken')
  const isAuthenticated = !!accessToken

  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login')
  } else if (to.meta.requiresGuest && isAuthenticated) {
    next('/app')
  } else {
    next()
  }
})

export default router
