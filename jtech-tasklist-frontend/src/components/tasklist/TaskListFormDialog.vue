<template>
  <v-dialog :model-value="modelValue" max-width="400" @update:model-value="$emit('update:modelValue', $event)">
    <v-card rounded="lg">
      <v-card-title class="text-h6 pa-6 pb-2">{{ title }}</v-card-title>

      <v-card-text class="pt-2">
        <v-form ref="formRef" @submit.prevent="handleSave">
          <v-text-field
            v-model="name"
            label="Nome da lista"
            prepend-inner-icon="mdi-format-list-bulleted"
            :rules="[(v: string) => !!v || 'Nome é obrigatório']"
            variant="outlined"
            autofocus
          />
        </v-form>
      </v-card-text>

      <v-card-actions class="pa-4 pt-0">
        <v-spacer />
        <v-btn variant="text" @click="handleClose">Cancelar</v-btn>
        <v-btn color="primary" variant="flat" @click="handleSave">Salvar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

const props = withDefaults(defineProps<{
  modelValue: boolean
  title: string
  initialName?: string
}>(), { initialName: '' })

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  save: [name: string]
}>()

const formRef = ref()
const name = ref(props.initialName || '')

watch(() => props.modelValue, (open) => {
  if (open) name.value = props.initialName || ''
})

async function handleSave() {
  const { valid } = await formRef.value.validate()
  if (!valid) return
  emit('save', name.value.trim())
  emit('update:modelValue', false)
}

function handleClose() {
  emit('update:modelValue', false)
}
</script>
