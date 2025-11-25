<template>
  <router-link :id="id" class="route-link v-btn--icon" :to="to">
    <v-btn
      class="mr-1"
      :color="disabled ? '0' : btnColor"
      :density="density"
      :disabled="disabled"
      :elevation="elevation"
      icon
    >
      <v-icon :color="iconColor" :icon="icon" :size="size" />
      <v-tooltip activator="parent" :text="tip" />
    </v-btn>
  </router-link>
</template>

<script setup>
  const props = defineProps({
    disabled: {
      type: Boolean,
      default: false,
    },
    iconColor: {
      type: String,
      default: 'primary',
    },
    btnColor: {
      type: String,
      default: 'transparent',
    },
    size: {
      type: String,
      default: 'default',
    },
    elevation: {
      type: String,
      default: '0',
    },
    density: {
      type: String,
      default: 'default',
    },
    id: {
      type: [String, Number],
      default: `r-btn-${Math.random()}}`,
    },
    tip: {
      type: String,
      default: 'Go to...',
    },
    to: {
      type: [String, Object],
      default: '',
    },
    icon: {
      type: String,
      default: '',
    },
  })
</script>

<style scoped>
.route-link {
  text-decoration: none;
}
</style>
