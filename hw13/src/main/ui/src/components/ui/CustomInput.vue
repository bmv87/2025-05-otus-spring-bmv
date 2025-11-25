<template>
  <v-text-field
    v-model.trim="innerValue"
    :append-icon="appendIcon"
    :clearable="!readonly"
    :density="density"
    :error-messages="errors"
    :hide-details="hideDetails"
    :hint="hint"
    :label="label"
    :persistent-hint="persistentHint"
    :prepend-inner-icon="prependIcon"
    :readonly="readonly"
    :single-line="singleLine"
    :type="type"
    :variant="variant"
    @click:append="clickAppend"
    @keyup="keyup"
    @keyup.enter="onEnter"
    @update:model-value="change"
  >
    <template v-if="required" #prepend-inner>
      <span :class="`text-${requiredColor}`">
        {{ "*" }}
      </span>
    </template>
  </v-text-field>
</template>

<script setup>
  import { ref, watch } from 'vue'
  import { IconValue } from 'vuetify/lib/composables/icons.mjs'
  const emits = defineEmits([
    'update:modelValue',
    'change',
    'keyup',
    'click:append',
    'keyup:enter',
  ])

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
    type: {
      type: String,
      default: 'text',
    },
    dataType: {
      type: String,
      default: 'text',
    },
    label: {
      type: [String, undefined],
      default: undefined,
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

  function parseValue (value) {
    switch (props.dataType) {
      case 'int': {
        if (!value && value !== '0' && value !== 0) {
          return null
        }
        return isInt(value) ? Number.parseInt(value) : value
      }
      case 'float': {
        if (!value && value !== '0' && value !== 0) {
          return null
        }
        return Number.isNaN(value) ? value : Number.parseFloat(value)
      }

      default: {
        return value
      }
    }
  }
  function isInt (value) {
    return (
      !Number.isNaN(value)
      && Number.parseInt(value) == value
      && !Number.isNaN(Number.parseInt(value, 10))
    )
  }
  function change (value) {
    emits('update:modelValue', parseValue(value))
    emits('change', parseValue(value))
  }
  function keyup () {
    emits('keyup')
  }
  function onEnter () {
    emits('keyup:enter')
  }
  function clickAppend () {
    emits('click:append')
  }
</script>
