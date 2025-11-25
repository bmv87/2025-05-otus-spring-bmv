<template>
  <v-textarea
    v-model="innerValue"
    :append-icon="appendIcon"
    :density="density"
    :error-messages="errors"
    :hide-details="hideDetails"
    :hint="hint"
    :label="label"
    :persistent-hint="persistentHint"
    :prepend-inner-icon="prependIcon"
    :readonly="readonly"
    :rows="rows"
    :single-line="singleLine"
    :variant="variant"
  >
    <template v-if="required" #prepend-inner>
      <span :class="`text-${requiredColor}`">
        {{ "*" }}
      </span>
    </template>
  </v-textarea>
</template>

<script setup>
  import { ref, watch } from 'vue'
  import { IconValue } from 'vuetify/lib/composables/icons.mjs'

  const props = defineProps({
    modelValue: {
      type: [String, undefined, null],
      default: null,
    },
    required: {
      type: Boolean,
      default: false,
    },
    requiredColor: {
      type: String,
      default: 'requiredfield',
    },
    readonly: {
      type: Boolean,
      default: false,
    },
    singleLine: {
      type: Boolean,
      default: false,
    },
    hideDetails: {
      type: Boolean,
      default: false,
    },
    persistentHint: {
      type: Boolean,
      default: false,
    },
    rows: {
      type: [String, Number],
      default: 3,
    },
    errors: {
      type: [String, Array, undefined],
      default: undefined,
    },
    prependIcon: {
      type: [String, IconValue, undefined],
      default: undefined,
    },
    appendIcon: {
      type: [String, IconValue, undefined],
      default: undefined,
    },
    label: {
      type: [String, undefined],
      default: undefined,
    },
    hint: {
      type: [String, undefined],
      default: undefined,
    },
    variant: {
      type: [String, undefined],
      default: undefined,
    },
    density: {
      type: [String, undefined],
      default: undefined,
    },
    rows: {
      type: [String, Number],
      default: 3,
    },
  })

  const innerValue = ref(null)

  watch(
    // getter
    () => props.modelValue,
    // callback
    newVal => {
      if (newVal !== innerValue.value) {
        innerValue.value = newVal
      }
    },
    // watch Options
    {
      immediate: true,
    },
  )
  const emits = defineEmits(['update:modelValue', 'change'])
  watch(
    // getter
    () => innerValue.value,
    // callback
    newVal => {
      if (newVal !== props.modelValue) {
        emits('update:modelValue', newVal)
        if (newVal !== props.modelValue) {
          emits('change', newVal)
        }
      }
    },
  )
</script>
