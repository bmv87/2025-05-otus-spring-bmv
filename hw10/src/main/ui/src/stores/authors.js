import { defineStore } from 'pinia'
import { http } from '@/api/http'
import { useErrorsStore } from '@/stores/errors'

const API_PATH = 'authors'
export const useAuthorsStore = defineStore('authors', {
  state: () => {
    return {
      authors: [],
    }
  },
  actions: {
    async getAuthors () {
      const errorsStore = useErrorsStore()
      await errorsStore.hideError()

      const requestParams = {
        routePath: API_PATH,
      }
      try {
        const resp = await http.get(requestParams)
        this.authors = resp || []
      } catch (error) {
        const errorsStore = useErrorsStore()
        await errorsStore.showAndThrowError(error)
      }
    },
    clearList () {
      this.authors = null
    },
  },
})
