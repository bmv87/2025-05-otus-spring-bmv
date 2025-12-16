<template>
  <router-link v-slot="{ href, navigate, isActive, isExactActive }" :to="to">
    <a
      :id="id"
      :class="[
        !isActive && 'router-link-active',
        !isExactActive && 'router-link-exact-active',
        'text-wrap',
      ]"
      :href="href"
      :style="{ cursor: 'pointer' }"
      @click.stop="navigate"
    >
      {{ text }}
      <v-tooltip activator="parent" :text="tip" />
    </a>
  </router-link>
</template>

<script setup>
  const props = defineProps({
    id: {
      type: [String, Number],
      default: `r-link-${Math.random()}}`,
    },
    tip: {
      type: String,
      default: 'Go to...',
    },
    to: {
      type: [String],
      default: '',
    },
    text: {
      type: String,
      default: '',
    },
  })
</script>
