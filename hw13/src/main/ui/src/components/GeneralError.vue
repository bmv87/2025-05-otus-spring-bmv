<template>
  <v-alert v-if="!!error" type="error">
    <p>
      {{ error?.message }}
    </p>
    <div v-if="hasInvalid">
      <ul>
        <li v-for="(item, index) in error?.invalids" :key="`invDescription-${index}`">
          {{ `${item.field} - ${item.message}` }}
        </li>
      </ul>
    </div>
    <div v-if="!!error?.description">
      <p>
        {{ error?.description }}
      </p>
    </div>
  </v-alert>
</template>
<script setup>
  import { storeToRefs } from 'pinia'
  import { onBeforeUnmount } from 'vue'
  import { useErrorsStore } from '@/stores/errors'

  const errorsStore = useErrorsStore()
  const { error, hasInvalid } = storeToRefs(errorsStore)

  onBeforeUnmount(() => {
    errorsStore.hideError()
  })
</script>

<style scoped>
ul {
  list-style-type: none;
}
</style>
