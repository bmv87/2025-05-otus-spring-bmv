import { defineStore } from 'pinia'
import { useInterfaceStore } from '@/stores/interface'
export const useErrorsStore = defineStore('errors', {
  // convert to a function
  state: () => {
    return {
      error: null,
    }
  },
  getters: {
    hasInvalid: state => !!state.error && !!state.error.invalids,
  },
  actions: {
    // no context as first argument, use `this` instead
    async showError (error) {
      this.error = error
    },
    // mutations can now become actions, instead of `state` as first argument use `this`
    async showAndThrowError (error) {
      const errorModel = getErrorDescription(error)

      await this.showError(errorModel)
      throw error
    },
    async hideError () {
      this.error = null
    },
    async throwIfNotServerError (error) {
      console.error(error)
      console.log(error?.name)
      if (error.name !== 'ResponseError'
        && error.name !== 'NetworkError'
        && error.name !== 'UnauthorizedError'
        && error.name !== 'ForbiddenError') {
        throw error
      }
      if (error.name === 'UnauthorizedError') {
        const interfaceStore = useInterfaceStore()
        interfaceStore.openAuthDialog()
      }
    },
    // easily reset state using `$reset`
    reset () {
      this.$reset()
    },
  },
})

function getErrorDescription (error) {
  if (error?.name === 'ResponseError') {
    return error?.error ?? {
      message: error?.message || 'Unexpected error',
    }
  }
  if (error?.name === 'ForbiddenError') {
    return error?.error ?? {
      message: error?.message || 'Access denied',
    }
  }
  if (error?.name === 'NetworkError') {
    return {
      message: error?.message || 'Network Error',
      status: error?.state,
    }
  }

  return {
    message: error?.message || 'Unexpected error',
  }
}
