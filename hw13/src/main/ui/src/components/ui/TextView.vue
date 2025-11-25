<template>
  <v-col
    :class="flexClass"
    :cols="cols"
    :lg="lg"
    :md="md"
    :sm="sm"
    :xl="xl"
    :xxl="xxl"
  >
    <v-row>
      <v-col class="d-inline-flex flex-nowrap" :cols="multiline ? 12 : 'auto'">
        <v-icon
          v-if="icon"
          class="mr-1"
          :color="iconColor"
          :icon="icon"
          :size="iconSize"
          @click="iconClick"
        />
        <label v-if="label" :class="labelClass">
          {{ `${label}${separator} ` }}
        </label>
      </v-col>
      <v-col :class="multiline ? 'mt-0 pt-0' : ''">
        <span v-if="!to" :class="`${valueClass} ${icon ? 'ml-7' : ''}`" @click="click">
          {{ displayValue }}
        </span>
        <router-link v-else :to="{ path: to }">
          {{ displayValue }}
        </router-link>
      </v-col>
    </v-row>
  </v-col>
</template>

<script setup>
  import { computed } from 'vue'

  const emits = defineEmits(['click', 'click:prepend-icon'])

  const props = defineProps({
    display: {
      type: String,
    },
    label: {
      type: String,
    },
    icon: {
      type: String,
    },
    to: {
      type: String,
    },
    separator: {
      type: String,
      default: ': ',
    },
    flexClass: {
      type: String,
      default: 'py-1 my-1',
    },
    labelClass: {
      type: String,
      default: 'v-label v-label--active font-weight-bold text-textcolor text-wrap mr-1',
    },
    valueClass: {
      type: String,
      default: 'v-label v-label--active text-textcolor text-wrap',
    },
    iconColor: {
      type: String,
      default: undefined,
    },
    noData: {
      type: String,
      default: 'no data',
    },
    cols: {
      type: [String, Number],
      default: 12,
    },
    multiline: {
      type: Boolean,
      default: false,
    },
    iconSize: {
      type: [String, Number],
      default: 12,
    },
    md: [String, Number, Boolean],
    lg: [String, Number, Boolean],
    sm: [String, Number, Boolean],
    xl: [String, Number, Boolean],
    xxl: [String, Number, Boolean],
  })

  function click () {
    emits('click')
  }
  function iconClick () {
    emits('click:prepend-icon')
  }
  const displayValue = computed(() => {
    return props.display || props.noData
  })
</script>

<style scoped></style>
