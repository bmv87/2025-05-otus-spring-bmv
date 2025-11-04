import { defineStore } from 'pinia'

export const useInterfaceStore = defineStore('interface', {
  // convert to a function
  state: () => {
    return {
      dialogVisible: false,
      showAuthDialog: false,
    }
  },
  actions: {
    // no context as first argument, use `this` instead
    async setDialogVisible (enabled) {
      this.dialogVisible = enabled
    },
    async closeAuthDialog () {
      this.showAuthDialog = false
    },
    async openAuthDialog () {
      this.showAuthDialog = true
    },
    // easily reset state using `$reset`
    reset () {
      this.$reset()
    },
  },
})
