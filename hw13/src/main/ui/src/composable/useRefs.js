import { reactive } from 'vue'

export const useRefs = () => {
  const refs = reactive({})
  const toRef = (refName) => (el) => (refs[refName] = el)

  return {
    refs,
    toRef
  }
}

// usage
// <template>
//   <input :ref="toRef('input')" />
// </template>

// <script lang="ts" setup>
// import { onMounted } from 'vue'
// import { useRefs } from '@common/utils/useRefs'

// const { refs, toRef } = useRefs<{
//   input: InstanceType<typeof HTMLInputElement>
// }>()

// onMounted(() => {
//   refs.input.focus()
// })
// </script>