<template>
  <a :id="id" class="wb text-wrap text-link" :href="href" :target="target">
    {{ text }}
    <v-tooltip activator="parent" :text="tip" />
  </a>
</template>

<script setup>
  const props = defineProps({
    id: {
      type: [String, Number],
      default: `r-btn-${Math.random()}}`,
    },
    tip: {
      type: String,
      default: 'Go to...',
    },
    target: {
      type: String,
      default: '_blank',
    },
    text: {
      type: String,
      default: '',
    },
    href: {
      type: String,
      default: '/',
    },
  })
</script>

<style scoped>
.wb {
  word-break: break-word !important;
}
a.text-link {
  color: rgb(var(--v-theme-anchor)) !important;
}
</style>
