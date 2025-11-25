<template>
  <v-card class="ma-0 pa-0" elevation="1" max-width="500">
    <v-toolbar :title="title">
      <template #append>
        <route-btn-tip
          :id="`addBtn-${bookId}`"
          :disabled="isLoading"
          icon="mdi-pencil"
          icon-color="textwithbackground"
          :tip="'Edit'"
          :to="{
            path: `/books/edit/${bookId}`,
          }"
        />
      </template>
    </v-toolbar>
    <v-card-text>
      <v-row>
        <v-col>
          <v-progress-linear :active="isLoading" />
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <expansion-panels :items="panelItems" multiple>
            <template #book>
              <book-view />
            </template>
            <template #comments>
              <comments-list :book-id="bookId" />
            </template>
          </expansion-panels>
        </v-col>
      </v-row>
    </v-card-text>
    <v-card-actions>
      <VBtnSecondary
        id="backBtn"
        text="To book list"
        @click="goToRoute('/books')"
      />
    </v-card-actions>
  </v-card>
</template>

<script setup>
  import { computed, onMounted, ref } from 'vue'
  import { useRoute } from 'vue-router/auto'
  import BookView from '@/components/books/BookView.vue'
  import CommentsList from '@/components/comments/CommentsList.vue'
  import ExpansionPanels from '@/components/ui/ExpansionPanels.vue'
  import RouteBtnTip from '@/components/ui/RouteBtnTip.vue'
  import useRouterHelpers from '@/composable/useRouterHelpers'
  import { useBooksStore } from '@/stores/books'
  import { useErrorsStore } from '@/stores/errors'
  const route = useRoute()
  const { goToRoute } = useRouterHelpers()

  const booksStore = useBooksStore()
  const errorsStore = useErrorsStore()

  const isLoading = ref(false)
  const bookId = ref(null)

  const title = computed(() => {
    return bookId.value ? `View book #${bookId.value}` : 'View book'
  })

  const panelItems = computed(() => {
    return [
      {
        id: 'book',
        icon: 'mdi-paperclip',
        slot: 'book',
        title: 'Book info',
        valid: true,
        open: true,
      },
      { id: 'comments',
        icon: 'mdi-paperclip',
        slot: 'comments',
        title: 'Book comments',
        valid: true,
        open: false,
      },
    ]
  })

  /* Methods */
  async function getBook () {
    try {
      isLoading.value = true
      await booksStore.getBook(bookId.value)
    } catch (error) {
      await errorsStore.throwIfNotServerError(error)
    } finally {
      isLoading.value = false
    }
  }

  function clearStore () {
    booksStore.clearCurrent()
  }

  onMounted(() => {
    const { id } = route.params
    bookId.value = id
    getBook()
  })

  onBeforeUnmount(() => {
    clearStore()
  })
</script>
