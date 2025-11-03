import { defineStore } from 'pinia'


export const useInterfaceStore = defineStore('interface', {
  // convert to a function
  state: ()=> {
    return {
      dialogVisible: false
    }
  },
  actions: {
    // no context as first argument, use `this` instead
    async setDialogVisible (enabled) {
      this.dialogVisible = enabled
    },
    // easily reset state using `$reset`
    reset () {
      this.$reset()
    }
  }
})