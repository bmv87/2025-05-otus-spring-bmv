<template>
  <dialog-switcher
    :color="color"
    :max-width="maxWidth"
    :show="dialogProp.show"
    :title="'Confirmation'"
    @close="close"
  >
    <template #card>
      <v-row v-if="dialogProp.text">
        <v-col class="v-label v-label--active text-textcolor font-weight-bold text-wrap">
          {{ dialogProp.text }}
        </v-col>
      </v-row>
      <slot :model="dialogProp.additional" name="additional" />
    </template>
    <template #buttons>
      <VBtnPrimary
        id="continueBtn"
        :color="color"
        :text="'Continue'"
        @click.stop="continueOperation"
      />
    </template>
  </dialog-switcher>
</template>

<script setup>
  import { computed, toRefs, withDefaults } from 'vue'
  import DialogSwitcher from '@/components/DialogSwitcher.vue'

  const emits = defineEmits(['close'])

  const prop = defineProps({
    dialogProp: {
      type: Object,
      default: () => ({
        text: null,
        warning: false,
        additional: null,
        show: false,
        callback: () => {},
      }),
    },
    maxWidth: {
      type: [String, Number],
      default: '600',
    },
  })

  const color = computed(() => {
    return prop.dialogProp.warning ? 'warning' : 'primary'
  })
  function continueOperation () {
    if (prop.dialogProp.callback) {
      prop.dialogProp.callback(prop.dialogProp.additional)
    }
    close()
  }
  function close () {
    emits('close')
  }
</script>
