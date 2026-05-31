<template>
  <v-dialog :model-value="modelValue" max-width="400" @update:model-value="$emit('update:modelValue', $event)">
    <v-card rounded="lg">
      <v-card-title class="text-h6 pa-6 pb-2">{{ title }}</v-card-title>
      <v-card-text class="text-body-1 text-medium-emphasis">{{ message }}</v-card-text>
      <v-card-actions class="pa-4 pt-0">
        <v-spacer />
        <v-btn variant="text" @click="$emit('update:modelValue', false)">Cancelar</v-btn>
        <v-btn :color="confirmColor" variant="flat" @click="handleConfirm">
          {{ confirmText }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
withDefaults(defineProps<{
  modelValue: boolean
  title: string
  message: string
  confirmText?: string
  confirmColor?: string
}>(), { confirmText: 'Confirmar', confirmColor: 'primary' })

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  confirm: []
}>()

function handleConfirm() {
  emit('confirm')
  emit('update:modelValue', false)
}
</script>
