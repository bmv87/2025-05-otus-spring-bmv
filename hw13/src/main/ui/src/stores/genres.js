import { defineStore } from 'pinia'
import { http } from '@/api/http'
import { useErrorsStore } from '@/stores/errors'

const API_PATH = 'genres'
export const useGenresStore = defineStore('genres', {
  state: () => {
    return {
      genres: [],
    }
  },
  actions: {
    async getGenres () {
      const errorsStore = useErrorsStore()
      await errorsStore.hideError()

      const requestParams = {
        routePath: API_PATH,
      }
      try {
        const resp = await http.get(requestParams)
        this.genres = resp || []
      } catch (error) {
        const errorsStore = useErrorsStore()
        await errorsStore.showAndThrowError(error)
      }
    },
    clearList () {
      this.genres = null
    },
  },
})
