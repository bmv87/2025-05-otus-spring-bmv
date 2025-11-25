<template>
  <v-card class="ma-0 pa-0" elevation="1">
    <v-toolbar density="compact">
      <template #append>
        <btn-icon-tip
          :id="`addBtn-${bookId}`"
          :disabled="isLoading"
          icon="mdi-plus-box"
          icon-color="textwithbackground"
          :tip="'Add commen'"
          @click="openAddDialog()"
        />
      </template>
    </v-toolbar>
    <v-row>
      <v-col>
        <v-data-table
          v-model:items-per-page="itemsPerPage"
          density="compact"
          :headers="headers"
          :is-loading="isLoading"
          item-value="id"
          :items="comments"
          :mobile="smAndDown"
        >
          <!-- eslint-disable-next-line vue/valid-v-slot -->
          <template #item._="{ item }">
            {{ item.id }}
          </template>
          <!-- eslint-disable-next-line vue/valid-v-slot -->
          <template #item.title="{ item }">
            {{ item.content }}
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
            <btn-icon-tip
              :id="`editBtn-${item.id}`"
              :disabled="isLoading"
              icon="mdi-pencil"
              icon-color="primary"
              :tip="'Edit'"
              @click="openEditDialog(item)"
            />
            <btn-icon-tip
              :id="`deleteBtn-${item.id}`"
              :disabled="isLoading"
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
    <comment-dialog
      v-if="editDialogProps.show"
      :dialog-prop="editDialogProps"
      :max-width="400"
      @close="closeDialog(editDialogProps)"
    />
  </v-card>
</template>

<script setup>
  import { storeToRefs } from 'pinia'
  import { computed, reactive, ref } from 'vue'
  import { useDisplay } from 'vuetify'
  import CommentDialog from '@/components/comments/CommentDialog.vue'
  import ConfirmDialog from '@/components/ConfirmDialog.vue'
  import BtnIconTip from '@/components/ui/BtnIconTip.vue'
  import TextArrayView from '@/components/ui/TextArrayView.vue'
  import useCloseDialog from '@/composable/useCloseDialog'
  import { useCommentsStore } from '@/stores/comments'
  import { useErrorsStore } from '@/stores/errors'

  const props = defineProps({
    bookId: {
      type: [String],
    },
  })
  const { smAndDown } = useDisplay()
  const closeDialog = useCloseDialog()
  /* Models */

  /* Props */

  /* Computed */
  const errorsStore = useErrorsStore()
  const commentsStore = useCommentsStore()

  const { comments: commentsList } = storeToRefs(commentsStore)
  const comments = computed(() => {
    return commentsList.value || []
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
        title: 'Content',
        align: 'start',
        sortable: true,
        key: 'content',
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

  const editDialogProps = reactive({
    show: false,
    comment: null,
    callback: null,
  })
  /* Methods */

  function openConfirmationBeforDelete (id) {
    deleteDialogProps.additional = id
    deleteDialogProps.text = 'The comment will be deleted. Continue?'
    deleteDialogProps.callback = deleteComment
    deleteDialogProps.warning = true
    deleteDialogProps.show = true
  }

  function openEditDialog (comment) {
    editDialogProps.comment = comment
    editDialogProps.callback = editComment
    editDialogProps.show = true
  }

  function openAddDialog () {
    editDialogProps.comment = {}
    editDialogProps.callback = addComment
    editDialogProps.show = true
  }

  async function addComment (comment) {
    await commentsStore.addComment({ ...comment, bookId: props.bookId })
    getAllComments()
  }

  async function editComment (comment) {
    await commentsStore.editComment(comment.id, comment)
    getAllComments()
  }

  async function deleteComment (id) {
    try {
      isLoading.value = true
      await commentsStore.deleteComment(id)
      await commentsStore.getCommentsByBook(props.bookId)
      isLoading.value = false
    } catch (error) {
      await errorsStore.throwIfNotServerError(error)
    } finally {
      isLoading.value = false
    }
  }

  function clearStore () {
    commentsStore.clearList()
  }

  async function getAllComments () {
    try {
      isLoading.value = true
      await commentsStore.getCommentsByBook(props.bookId)
    } catch (error) {
      await errorsStore.throwIfNotServerError(error)
    } finally {
      isLoading.value = false
    }
  }

  onMounted(() => {
    getAllComments()
  })

  onBeforeUnmount(() => {
    clearStore()
  })
</script>
