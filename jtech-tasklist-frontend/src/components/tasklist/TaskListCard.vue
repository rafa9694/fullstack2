<template>
  <v-card
    elevation="2"
    rounded="lg"
    hover
    :to="{ name: 'task-list', params: { id: taskList.id } }"
    class="task-list-card"
  >
    <v-card-text class="pb-2">
      <div class="d-flex align-center">
        <v-icon color="primary" class="mr-3">mdi-format-list-bulleted</v-icon>
        <span class="text-h6 font-weight-medium text-truncate">{{ taskList.name }}</span>
      </div>
    </v-card-text>

    <v-card-actions>
      <v-spacer />
      <v-btn
        icon="mdi-pencil"
        size="small"
        variant="text"
        color="primary"
        @click.prevent="$emit('edit', taskList)"
      />
      <v-btn
        icon="mdi-delete"
        size="small"
        variant="text"
        color="error"
        @click.prevent="$emit('delete', taskList)"
      />
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
import type { TaskList } from '@/models'

defineProps<{ taskList: TaskList }>()
defineEmits<{ edit: [list: TaskList]; delete: [list: TaskList] }>()
</script>

<style scoped>
.task-list-card {
  transition: transform 0.2s ease;
}
.task-list-card:hover {
  transform: translateY(-2px);
}
</style>
