<template>
  <v-row class="text-left px-4">
    <text-view :display="book?.title" label="Title" md="6" multiline />
    <text-view :display="book?.author?.name" label="Author" md="6" multiline />
    <text-array-view :display="book?.genres" display-name="name" label="Genres" />
  </v-row>
</template>

<script setup>
  import { storeToRefs } from 'pinia'
  import TextArrayView from '@/components/ui/TextArrayView.vue'
  import TextView from '@/components/ui/TextView.vue'
  import { useBooksStore } from '@/stores/books'
  const booksStore = useBooksStore()
  const { current: book } = storeToRefs(booksStore)
</script>
