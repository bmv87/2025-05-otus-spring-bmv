<template>
  <v-autocomplete
    v-model:search="search"
    :append-icon="appendIcon"
    :clearable="clearable && !readonly"
    :custom-filter="customFilter"
    :density="density"
    eager
    :error-messages="errors"
    :hide-details="hideDetails"
    :hint="hint"
    :item-title="itemTitle"
    :item-value="itemValue"
    :items="items"
    :label="label"
    :loading="loading"
    :model-value="props.modelValue"
    :multiple="multiple"
    :persistent-hint="persistentHint"
    :prepend-inner-icon="prependIcon"
    :readonly="readonly"
    :single-line="singleLine"
    :variant="variant"
    @update:model-value="change"
  >
    <template v-if="required" #prepend-inner>
      <span :class="`text-${requiredColor}`">
        {{ "*" }}
      </span>
    </template>
  </v-autocomplete>
</template>

<script setup>
  import { isEqual } from 'lodash'
  import { nextTick, ref } from 'vue'
  import { IconValue } from 'vuetify/lib/composables/icons.mjs'

  const emits = defineEmits(['update:modelValue', 'change'])

  const props = defineProps({
    modelValue: {
      type: [String, Number, Array, undefined],
      default: undefined,
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
    prependIcon: {
      type: [String, IconValue, undefined],
      default: undefined,
    },
    appendIcon: {
      type: [String, IconValue, undefined],
      default: undefined,
    },
    itemValue: {
      type: String,
      default: 'id',
    },
    itemTitle: {
      type: String,
      default: 'name',
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
    clearable: {
      type: Boolean,
      default: false,
    },
    items: {
      type: [Array],
      default: () => [],
    },
    loading: {
      type: Boolean,
      default: false,
    },
    multiple: {
      type: Boolean,
      default: false,
    },
    label: {
      type: [String, undefined],
      default: undefined,
    },
    errors: {
      type: [String, Array, undefined],
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

  const search = ref(null)

  function customFilter (itemTitle, queryText, item) {
    const values = props.modelValue
      ? (Array.isArray(props.modelValue)
        ? props.modelValue
        : [props.modelValue])
      : []
    const key = item.raw[props.itemValue]
    if (values.includes(key)) {
      return false
    }

    const searchText = queryText.toLowerCase()
    return itemTitle.includes(searchText)
  }

  function change (item) {
    const val = item
    if (!isEqual(val, props.modelValue)) {
      emits('update:modelValue', val)
      nextTick(() => {
        emits('change')
      })
    }
  }

</script>
