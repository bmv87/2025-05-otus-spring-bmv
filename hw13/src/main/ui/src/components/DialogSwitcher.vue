<template>
  <v-dialog :max-width="maxWidth" :model-value="show" persistent>
    <v-card class="elevation-12">
      <v-toolbar :color="color" :title="title">
        <template #append>
          <btn-icon-tip
            :disabled="loading"
            icon="mdi-close-box-outline"
            icon-color="secondaryiconbtn"
            :tip="'Close'"
            @click="close"
          />
        </template>
      </v-toolbar>

      <v-row v-if="(!!error || loading) && !loadingBottom" class="my-0 py-0 mx-1">
        <v-col v-if="loading" cols="12">
          <v-progress-linear :active="loading" />
        </v-col>
        <v-col v-if="!!error" class="mx-1">
          <general-error v-if="!!error" />
        </v-col>
      </v-row>
      <v-card-text class="pa-3">
        <slot name="card" />
        <v-row v-if="(!!error || loading) && loadingBottom" class="my-0 py-0 mx-1">
          <v-col v-if="loading" cols="12">
            <v-progress-linear :active="loading" />
          </v-col>
          <v-col v-if="!!error" class="mx-1">
            <general-error v-if="!!error" />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <slot name="buttons" />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
  import { storeToRefs } from 'pinia'
  import { onBeforeUnmount, onMounted, toRefs, withDefaults } from 'vue'
  import GeneralError from '@/components/GeneralError.vue'
  import BtnIconTip from '@/components/ui/BtnIconTip.vue'
  import { useErrorsStore } from '@/stores/errors'
  import { useInterfaceStore } from '@/stores/interface'

  const emits = defineEmits(['close'])

  const props = defineProps({
    show: {
      type: Boolean,
      default: false,
    },
    loading: {
      type: Boolean,
      default: false,
    },
    loadingBottom: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: '',
    },
    color: {
      type: String,
      default: 'primary',
    },
    maxWidth: {
      type: [String, Number],
      default: '600',
    },
  })

  const errorsStore = useErrorsStore()
  const { error } = storeToRefs(errorsStore)
  const interfaceStore = useInterfaceStore()
  function close () {
    emits('close')
  }
  onMounted(() => {
    errorsStore.hideError()
    interfaceStore.setDialogVisible(true)
  })

  onBeforeUnmount(() => {
    errorsStore.hideError()
    interfaceStore.setDialogVisible(false)
  })
</script>
