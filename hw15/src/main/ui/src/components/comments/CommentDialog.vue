<template>
  <dialog-switcher
    :color="color"
    :max-width="maxWidth"
    :show="dialogProp.show"
    :title="dialogProp.comment ? 'Edit comment' : 'Add comment'"
    @close="close"
  >
    <template #card>
      <v-row v-if="dialogProp.text">
        <v-col class="v-label v-label--active text-textcolor font-weight-bold text-wrap">
          {{ dialogProp.text }}
        </v-col>
      </v-row>
      <custom-textarea v-model="content" label="Content" :readonly="isLoading" required />
    </template>
    <template #buttons>
      <VBtnPrimary
        id="continueBtn"
        :color="color"
        :text="'Save'"
        @click.stop="continueOperation"
      />
    </template>
  </dialog-switcher>
</template>

<script setup>
  import { computed } from 'vue'
  import DialogSwitcher from '@/components/DialogSwitcher.vue'
  import CustomTextarea from '@/components/ui/CustomTextarea.vue'
  import { useErrorsStore } from '@/stores/errors'
  const emits = defineEmits(['close'])
  const errorsStore = useErrorsStore()

  const prop = defineProps({
    dialogProp: {
      type: Object,
      default: () => ({
        text: null,
        warning: false,
        comment: null,
        show: false,
        callback: () => {},
      }),
    },
    maxWidth: {
      type: [String, Number],
      default: '600',
    },
  })
  const isLoading = ref(false)
  const content = ref(null)
  watch(
    () => prop?.dialogProp?.comment?.content,
    newVal => {
      if (newVal && newVal !== content.value) {
        content.value = newVal
      }
    },
    // watch Options
    {
      immediate: true,
    },
  )
  const color = computed(() => {
    return prop.dialogProp.warning ? 'warning' : 'primary'
  })
  async function continueOperation () {
    try {
      if (prop.dialogProp.callback) {
        isLoading.value = true
        await prop.dialogProp.callback({
          ...prop?.dialogProp?.comment,
          content: content.value,
        })
      }
    } catch (error) {
      await errorsStore.throwIfNotServerError(error)
    } finally {
      isLoading.value = false
    }
    close()
  }
  function close () {
    emits('close')
  }
</script>
