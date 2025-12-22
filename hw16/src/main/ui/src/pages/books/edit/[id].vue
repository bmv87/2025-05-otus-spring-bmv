<template>
  <v-card class="ma-0 pa-0" elevation="1" max-width="500">
    <v-toolbar :title="title" />
    <v-card-text>
      <v-row>
        <v-col>
          <v-progress-linear :active="isLoading" />
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12">
          <custom-input
            v-model="book.title"
            label="Title"
            prepend-icon="mdi-tag"
            :readonly="isLoading"
            required
          />
        </v-col>
        <v-col cols="12">
          <custom-combobox
            v-model="book.author"
            :clearable="!isLoading"
            :items="authors ?? []"
            label="Author"
            :readonly="isLoading"
            required
          />
        </v-col>
        <v-col cols="12">
          <custom-combobox
            v-model="book.genres"
            :clearable="!isLoading"
            :items="genres ?? []"
            label="Genres"
            multiple
            :readonly="isLoading"
            required
          />
        </v-col>
      </v-row>
    </v-card-text>
    <v-card-actions>
      <VBtnSecondary id="backBtn" text="To book list" @click="goToRoute('/books')" />
      <VBtnSecondary id="saveBtn" text="Save" @click="save" />
    </v-card-actions>
  </v-card>
</template>

<script setup>
  import { storeToRefs } from 'pinia'
  import { computed, onMounted, reactive, ref } from 'vue'
  import { useRoute } from 'vue-router/auto'
  import CustomCombobox from '@/components/ui/CustomCombobox.vue'
  import CustomInput from '@/components/ui/CustomInput.vue'
  import useRouterHelpers from '@/composable/useRouterHelpers'
  import { useAuthorsStore } from '@/stores/authors'
  import { useBooksStore } from '@/stores/books'
  import { useErrorsStore } from '@/stores/errors'
  import { useGenresStore } from '@/stores/genres'
  const route = useRoute()
  const { goToRoute } = useRouterHelpers()

  const genresStore = useGenresStore()
  const authorsStore = useAuthorsStore()
  const booksStore = useBooksStore()
  const errorsStore = useErrorsStore()
  const { authors } = storeToRefs(authorsStore)
  const { genres } = storeToRefs(genresStore)
  const { current } = storeToRefs(booksStore)
  const isLoading = ref(false)
  const bookId = ref(null)
  const book = reactive({
    author: null,
    genres: [],
    title: null,
  })

  watch(
    () => current.value,
    newVal => {
      if (newVal) {
        book.author = newVal.author.id
        book.genres = newVal.genres.map(g => g.id)
        book.title = newVal.title
      }
    },
  )

  const title = computed(() => {
    return bookId.value ? `Edit book #${bookId.value}` : 'Edit book'
  })

  async function initialize () {
    try {
      isLoading.value = true
      await genresStore.getGenres()
      await authorsStore.getAuthors()
      await booksStore.getBook(bookId.value)
    } catch (error) {
      // console.log(error)
      await errorsStore.throwIfNotServerError(error)
    } finally {
      isLoading.value = false
    }
  }

  async function save () {
    try {
      isLoading.value = true
      await booksStore.editBook(bookId.value, book)
      goToRoute('/books')
    } catch (error) {
      await errorsStore.throwIfNotServerError(error)
    } finally {
      isLoading.value = false
    }
  }

  /* Methods */
  function clearStore () {
    booksStore.clearCurrent()
    genresStore.clearList()
    authorsStore.clearList()
  }

  onMounted(() => {
    const { id } = route.params
    bookId.value = id
    initialize()
  })

  onBeforeUnmount(() => {
    clearStore()
  })
</script>
