<template>

  <v-card class="ma-0 pa-0" elevation="1" max-width="700">
    <v-toolbar density="compact">
      <template #title>
        {{ "Books" }}
      </template>
      <template #append>
        <route-btn-tip
          id="addBtn"
          :disabled="isLoading"
          icon="mdi-plus-box"
          icon-color="textwithbackground"
          :tip="'Add book'"
          :to="{
            path: `/books/create`,
          }"
        />
      </template>
    </v-toolbar>
    <v-row>
      <v-col>
        <v-data-table
          v-model:items-per-page="itemsPerPage"
          class="elevation-1"
          density="compact"
          :headers="headers"
          :is-loading="isLoading"
          item-value="id"
          :items="books"
          :mobile="smAndDown"
        >
          <!-- eslint-disable-next-line vue/valid-v-slot -->
          <template #item._="{ item }">
            {{ item.id }}
          </template>
          <!-- eslint-disable-next-line vue/valid-v-slot -->
          <template #item.title="{ item }">
            {{ item.title }}
          </template>
          <!-- eslint-disable-next-line vue/valid-v-slot -->
          <template #item.author.name="{ item }">
            {{ item.author?.name }}
          </template>
          <!-- eslint-disable-next-line vue/valid-v-slot -->
          <template #item.genres="{ item }">
            <text-array-view
              :display="item.genres"
              display-name="name"
              :label="''"
              :separator="''"
            />
          </template>
          <!-- eslint-disable-next-line vue/valid-v-slot -->
          <template #item.id="{ item }">
            <route-btn-tip
              :id="`viewBtn-${item.id}`"
              :disabled="isLoading"
              icon="mdi-eye"
              :tip="'View'"
              :to="{
                path: `/books/${item.id}`,
              }"
            />

            <route-btn-tip
              :id="`editBtn-${item.id}`"
              icon="mdi-pencil"
              :tip="'Edit'"
              :to="{
                path: `/books/edit/${item.id}`,
              }"
            />
            <btn-icon-tip
              :id="`deleteBtn-${item.id}`"
              icon="mdi-delete"
              :tip="'Delete'"
              @click="openConfirmationBeforDelete(item.id)"
            />
          </template>
        </v-data-table>
      </v-col>
    </v-row>
    <confirm-dialog
      v-if="deleteDialogProps.show"
      :dialog-prop="deleteDialogProps"
      :max-width="400"
      @close="closeDialog(deleteDialogProps)"
    />
    <v-card-actions>
      <VBtnSecondary id="menuBtn" text="To menu" @click="goToRoute('/')" />
    </v-card-actions>
  </v-card>
</template>

<script setup>
  import { storeToRefs } from 'pinia'
  import { computed, reactive, ref, toRefs } from 'vue'
  import { useDisplay } from 'vuetify'
  import ConfirmDialog from '@/components/ConfirmDialog.vue'
  import BtnIconTip from '@/components/ui/BtnIconTip.vue'
  import RouteBtnTip from '@/components/ui/RouteBtnTip.vue'
  import TextArrayView from '@/components/ui/TextArrayView.vue'
  import useCloseDialog from '@/composable/useCloseDialog'
  import useRouterHelpers from '@/composable/useRouterHelpers'
  import { useBooksStore } from '@/stores/books'
  import { useErrorsStore } from '@/stores/errors'
  const { goToRoute } = useRouterHelpers()
  const { smAndDown } = useDisplay()
  const closeDialog = useCloseDialog()
  /* Models */

  /* Props */

  /* Computed */
  const errorsStore = useErrorsStore()
  const booksStore = useBooksStore()

  const { books: booksList } = storeToRefs(booksStore)
  const books = computed(() => {
    return booksList.value || []
  })
  const isLoading = ref(false)
  const itemsPerPage = ref(20)

  const headers = computed(() => {
    const hdrs = [
      {
        title: 'ID',
        align: 'start',
        sortable: false,
        key: '_',
      },
      {
        title: 'Title',
        align: 'start',
        sortable: true,
        key: 'title',
      },
      {
        title: 'Author',
        align: 'start',
        sortable: true,
        key: 'author.name',
      },
      {
        title: 'Genres',
        align: 'start',
        sortable: false,
        key: 'genres',
      },
      {
        title: 'Actions',
        align: 'center',
        sortable: false,
        key: 'id',
      },
    ]

    return hdrs
  })

  /* Constants */

  const deleteDialogProps = reactive({
    show: false,
    text: null,
    additional: null,
    callback: null,
    warning: true,
  })

  /* Methods */

  function openConfirmationBeforDelete (id) {
    deleteDialogProps.additional = id
    deleteDialogProps.text = 'The book will be deleted. Continue?'
    deleteDialogProps.callback = deleteBook
    deleteDialogProps.warning = true
    deleteDialogProps.show = true
  }

  async function deleteBook (id) {
    try {
      isLoading.value = true
      await booksStore.deleteBook(id)
      await booksStore.getBooks()
    } catch (error) {
      await errorsStore.throwIfNotServerError(error)
    } finally {
      isLoading.value = false
    }
  }
  function clearStore () {
    booksStore.clearList()
  }
  async function getAllBooks () {
    try {
      isLoading.value = true
      await booksStore.getBooks()
    } catch (error) {
      await errorsStore.throwIfNotServerError(error)
    } finally {
      isLoading.value = false
    }
  }
  onMounted(() => {
    getAllBooks()
  })
  onBeforeUnmount(() => {
    clearStore()
  })
</script>
