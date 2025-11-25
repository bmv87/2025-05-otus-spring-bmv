<template>
  <v-card class="ma-0 pa-0" elevation="1" max-width="500">
    <v-toolbar density="compact">
      <template #title>
        {{ "Genres" }}
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
          :items="genres"
          :mobile="smAndDown"
        >
          <!-- eslint-disable-next-line vue/valid-v-slot -->
          <template #item.id="{ item }">
            {{ item.id }}
          </template>
          <!-- eslint-disable-next-line vue/valid-v-slot -->
          <template #item.title="{ item }">
            {{ item.name }}
          </template>
        </v-data-table>
      </v-col>
    </v-row>
    <v-card-actions>
      <VBtnSecondary id="menuBtn" text="To menu" @click="goToRoute('/')" />
    </v-card-actions>
  </v-card>
</template>

<script setup>
  import { storeToRefs } from 'pinia'
  import { computed, ref } from 'vue'
  import { useDisplay } from 'vuetify'
  import useRouterHelpers from '@/composable/useRouterHelpers'
  import { useErrorsStore } from '@/stores/errors'
  import { useGenresStore } from '@/stores/genres'
  const { goToRoute } = useRouterHelpers()
  const { smAndDown } = useDisplay()
  /* Models */

  /* Props */

  /* Computed */
  const errorsStore = useErrorsStore()
  const genresStore = useGenresStore()

  const { genres: genresList } = storeToRefs(genresStore)
  const genres = computed(() => {
    return genresList.value || []
  })
  const isLoading = ref(false)
  const itemsPerPage = ref(20)

  const headers = computed(() => {
    const hdrs = [
      {
        title: 'ID',
        align: 'start',
        sortable: false,
        key: 'id',
      },
      {
        title: 'Name',
        align: 'start',
        sortable: true,
        key: 'name',
      },
    ]

    return hdrs
  })

  /* Constants */

  /* Methods */

  function clearStore () {
    genresStore.clearList()
  }
  async function getAllGenres () {
    try {
      isLoading.value = true
      await genresStore.getGenres()
    } catch (error) {
      await errorsStore.throwIfNotServerError(error)
    } finally {
      isLoading.value = false
    }
  }
  onMounted(() => {
    getAllGenres()
  })
  onBeforeUnmount(() => {
    clearStore()
  })
</script>
