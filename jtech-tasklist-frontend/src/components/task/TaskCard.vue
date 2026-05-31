<template>
  <v-card elevation="2" rounded="lg" :class="{ 'completed-card': task.completed }">
    <v-card-text>
      <div class="d-flex align-start">
        <v-checkbox
          :model-value="task.completed"
          color="success"
          hide-details
          class="mt-0 pt-0 mr-2"
          :disabled="task.completed"
          @change="$emit('complete', task)"
        />
        <div class="flex-grow-1 overflow-hidden">
          <p
            class="text-body-1 font-weight-medium"
            :class="{ 'text-decoration-line-through text-medium-emphasis': task.completed }"
          >
            {{ task.title }}
          </p>
          <p v-if="task.description" class="text-body-2 text-medium-emphasis mt-1 text-truncate">
            {{ task.description }}
          </p>
          <v-chip
            v-if="task.completed"
            color="success"
            size="x-small"
            class="mt-2"
            prepend-icon="mdi-check"
          >
            Concluída
          </v-chip>
          <p class="text-caption text-medium-emphasis mt-2">
            {{ formattedDate }}
          </p>
        </div>
      </div>
    </v-card-text>

    <v-card-actions class="pt-0">
      <v-spacer />
      <v-btn
        v-if="!task.completed"
        icon="mdi-pencil"
        size="small"
        variant="text"
        color="primary"
        @click="$emit('edit', task)"
      />
      <v-btn
        icon="mdi-delete"
        size="small"
        variant="text"
        color="error"
        @click="$emit('delete', task)"
      />
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Task } from '@/models'

const props = defineProps<{ task: Task }>()
defineEmits<{ complete: [task: Task]; edit: [task: Task]; delete: [task: Task] }>()

const formattedDate = computed(() => {
  return new Date(props.task.createdAt).toLocaleDateString('pt-BR', {
    day: '2-digit', month: 'short', year: 'numeric'
  })
})
</script>

<style scoped>
.completed-card {
  opacity: 0.75;
  background-color: rgb(var(--v-theme-surface));
}
</style>
