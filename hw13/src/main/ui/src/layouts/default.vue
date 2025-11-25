<template>
  <v-app-bar app>
    <template #append>
      <btn-icon-tip
        v-if="!anonymouse"
        icon="mdi-logout"
        icon-color="secondaryiconbtn"
        tip="Logout"
        @click="logout"
      />
    </template>
  </v-app-bar>
  <v-main>
    <v-container fill-height fluid>
      <v-row
        align="center"
        justify="center"
      >
        <v-col
          align="center"
          justify="center"
        >
          <router-view />

        </v-col>
      </v-row>
      <general-error-wrapper />
    </v-container>
    <AuthDialog v-if="showAuthDialog" :show="showAuthDialog" @close="interfaceStore.closeAuthDialog" />
  </v-main>
  <AppFooter />
</template>

<script setup>
  import { storeToRefs } from 'pinia'

  import { onMounted, ref } from 'vue'
  import AuthDialog from '@/components/AuthDialog.vue'
  import GeneralErrorWrapper from '@/components/GeneralErrorWrapper.vue'
  import BtnIconTip from '@/components/ui/BtnIconTip.vue'
  import { useInterfaceStore } from '@/stores/interface'
  const interfaceStore = useInterfaceStore()
  const { showAuthDialog } = storeToRefs(interfaceStore)
  const anonymouse = ref(false)
  function logout () {
    localStorage.removeItem('auth')
    location.reload()
  }
  onMounted(() => {
    anonymouse.value = !localStorage.getItem('auth')
  })
</script>
