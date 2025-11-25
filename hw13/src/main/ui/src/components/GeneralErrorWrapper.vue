<template>
  <general-error v-if="!!error && !dialogVisible" />
</template>

<script setup>
  import { storeToRefs } from 'pinia'
  import GeneralError from '@/components/GeneralError.vue'
  import { useErrorsStore } from '@/stores/errors'
  import { useInterfaceStore } from '@/stores/interface'

  const errorsStore = useErrorsStore()
  const { error } = storeToRefs(errorsStore)
  const interfaceStore = useInterfaceStore()
  const { dialogVisible } = storeToRefs(interfaceStore)

  watch(
    () => error.value,
    newVal => {
      console.log(newVal)
      console.log(dialogVisible.value)
    },
  )
</script>
