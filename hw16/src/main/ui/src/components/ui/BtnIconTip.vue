<template>
  <v-btn
    :id="id"
    :ref="toRef('btnIconTip')"
    :class="bClass"
    :color="disabled ? '0' : btnColor"
    :density="density"
    :disabled="disabled"
    :elevation="elevation"
    icon
    @click.stop="click"
  >
    <v-icon :color="iconColor" :icon="icon" :size="size" />
    <v-tooltip activator="parent" :text="tip" />
  </v-btn>
</template>

<script setup>
  import { VBtn } from 'vuetify/components'
  import { useRefs } from '@/composable/useRefs'

  const { refs, toRef } = useRefs()

  const emits = defineEmits(['click'])

  const props = defineProps({
    id: {
      type: [String, Number],
      default: `r-link-${Math.random()}}`,
    },
    tip: {
      type: String,
      default: 'Go to...',
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    btnColor: {
      type: String,
      default: 'transparent',
    },
    iconColor: {
      type: String,
      default: 'primaryiconbtn',
    },
    elevation: {
      type: String,
      default: '0',
    },
    density: {
      type: String,
      default: 'default',
    },
    size: {
      type: String,
      default: 'default',
    },
    bClass: {
      type: String,
      default: 'mr-1',
    },
    icon: {
      type: String,
      default: '',
    },
  })

  function click () {
    emits('click')
  }

  function focus () {
    refs.btnIconTip.$el.focus()
  }
  defineExpose({
    focus,
  })
</script>
