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
      <v-col class="d-inline-flex flex-nowrap" cols="12">
        <v-icon
          v-if="icon"
          class="mr-1"
          :color="iconColor"
          :icon="icon"
          :size="iconSize"
        />
        <label v-if="!!label" :class="labelClass">
          {{ label + separator }}
        </label>
      </v-col>
      <v-col class="mt-0 pt-0">
        <ul class="items-list">
          <li
            v-for="(item, index) in displayValue"
            :key="index"
            :class="`${valueClass} ${icon ? 'ml-7' : ''}`"
          >
            {{ item }}
          </li>
        </ul>
      </v-col>
    </v-row>
  </v-col>
</template>

<script setup>
  import { computed } from 'vue'

  const props = defineProps({
    display: {
      type: Array,
      default: () => [],
    },
    separator: {
      type: String,
      default: ': ',
    },
    flexClass: String,
    labelClass: {
      type: String,
      default: 'v-label v-label--active font-weight-bold text-textcolor',
    },
    valueClass: {
      type: String,
      default: 'value-text text-textcolor',
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
    displayName: {
      type: String,
      default: 'name',
    },
    icon: [String, undefined],
    label: [String, undefined],
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

  const displayValue = computed(() => {
    return props.display?.map(d => d[props.displayName]) || [props.noData]
  })
</script>

<style scoped>
.items-list {
  list-style-type: none;
  margin-left: 0;
  padding-left: 0;
}
.value-text {
  font-size: 1rem;
}
</style>
