<template>
  <v-expansion-panels v-model="panel" :eager="false" :multiple="multiple">
    <template v-for="(item, index) in items" :key="index">
      <v-expansion-panel :eager="false" :value="item.id">
        <v-expansion-panel-title>
          <v-row align-content="center">
            <v-col v-if="item.icon" class="d-flex justify-start" cols="1">
              <v-icon :icon="item.icon" />
            </v-col>
            <v-col class="d-flex justify-start" cols="9">
              {{ item.title }}
            </v-col>
            <v-col v-if="!item.valid" class="d-flex justify-start" cols="2">
              <v-icon color="error" icon="mdi-alert-circle" />
            </v-col>
            <slot name="additional" v-bind="item" />
          </v-row>
        </v-expansion-panel-title>
        <v-expansion-panel-text :eager="false">
          <slot :name="item.slot" v-bind="item" />
        </v-expansion-panel-text>
      </v-expansion-panel>
    </template>
  </v-expansion-panels>
</template>

<script setup>
  import { defineProps, ref } from 'vue'

  const props = defineProps({
    items: {
      type: Array,
      default: () => [],
    },
    multiple: {
      type: Boolean,
      default: false,
    },
  })

  const panel = ref([])
  watch(
    () => props.items,
    newVal => {
      if (newVal?.length) {
        panel.value = newVal.filter(v => v.open).map(v => v.id)
      }
    },
    // watch Options
    {
      immediate: true,
    },
  )
</script>
