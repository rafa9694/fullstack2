<template>
  <v-dialog :model-value="modelValue" max-width="480" @update:model-value="$emit('update:modelValue', $event)">
    <v-card rounded="lg">
      <v-card-title class="text-h6 pa-6 pb-2">{{ title }}</v-card-title>

      <v-card-text class="pt-2">
        <v-form ref="formRef" @submit.prevent="handleSave">
          <v-text-field
            v-model="form.title"
            label="Título"
            prepend-inner-icon="mdi-checkbox-marked-outline"
            :rules="[(v: string) => !!v || 'Título é obrigatório']"
            variant="outlined"
            class="mb-2"
            autofocus
          />
          <v-textarea
            v-model="form.description"
            label="Descrição (opcional)"
            prepend-inner-icon="mdi-text"
            variant="outlined"
            rows="3"
            auto-grow
          />
        </v-form>
      </v-card-text>

      <v-card-actions class="pa-4 pt-0">
        <v-spacer />
        <v-btn variant="text" @click="$emit('update:modelValue', false)">Cancelar</v-btn>
        <v-btn color="primary" variant="flat" @click="handleSave">Salvar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import type { Task } from '@/models'

const props = withDefaults(defineProps<{
  modelValue: boolean
  title: string
  taskListId: number
  initialTask?: Task | null
}>(), { initialTask: null })

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  save: [payload: { title: string; description: string }]
}>()

const formRef = ref()
const form = reactive({ title: '', description: '' })

watch(() => props.modelValue, (open) => {
  if (open) {
    form.title = props.initialTask?.title || ''
    form.description = props.initialTask?.description || ''
  }
})

async function handleSave() {
  const { valid } = await formRef.value.validate()
  if (!valid) return
  emit('save', { title: form.title.trim(), description: form.description.trim() })
  emit('update:modelValue', false)
}
</script>
