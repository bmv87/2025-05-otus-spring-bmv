<template>
  <v-card>
    <v-toolbar :title="title" />
    <v-card-text>
      <v-row>
        <v-col>
          <v-progress-linear :active="isLoading" />
          <general-error-wrapper />
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
  import GeneralErrorWrapper from '@/components/GeneralErrorWrapper.vue'
  import CustomCombobox from '@/components/ui/CustomCombobox.vue'
  import CustomInput from '@/components/ui/CustomInput.vue'
  import useRouterHelpers from '@/composable/useRouterHelpers'
  import { useAuthorsStore } from '@/stores/authors'
  import { useBooksStore } from '@/stores/books'
  import { useErrorsStore } from '@/stores/errors'
  import { useGenresStore } from '@/stores/genres'
  const { goToRoute } = useRouterHelpers()

  const genresStore = useGenresStore()
  const authorsStore = useAuthorsStore()
  const booksStore = useBooksStore()
  const errorsStore = useErrorsStore()
  const { authors } = storeToRefs(authorsStore)
  const { genres } = storeToRefs(genresStore)
  const isLoading = ref(false)
  const book = reactive({
    author: null,
    genres: [],
    title: null,
  })

  const title = computed(() => {
    return 'Create new book'
  })

  async function initialize () {
    try {
      isLoading.value = true
      await genresStore.getGenres()
      await authorsStore.getAuthors()
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
      await booksStore.addBook(book)
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
    initialize()
  })

  onBeforeUnmount(() => {
    clearStore()
  })
</script>
