import { defineStore } from 'pinia'
import { http } from '@/api/http'
import { useErrorsStore } from '@/stores/errors'

const API_PATH = 'books'
export const useBooksStore = defineStore('books', {
  state: () => {
    return {
      books: [],
      current: null,
    }
  },
  actions: {
    async getBooks () {
      const errorsStore = useErrorsStore()
      await errorsStore.hideError()

      const requestParams = {
        routePath: API_PATH,
      }
      try {
        const resp = await http.get(requestParams)
        this.books = resp || []
      } catch (error) {
        const errorsStore = useErrorsStore()
        await errorsStore.showAndThrowError(error)
      }
    },
    async getBook (id) {
      const errorsStore = useErrorsStore()
      await errorsStore.hideError()

      const requestParams = {
        routePath: `${API_PATH}/${id}`,
      }
      try {
        const resp = await http.get(requestParams)
        this.current = resp
      } catch (error) {
        const errorsStore = useErrorsStore()
        await errorsStore.showAndThrowError(error)
      }
    },
    async addBook (commentObj) {
      const requestParams = {
        routePath: API_PATH,
        body: commentObj,
      }

      try {
        const resp = await http.post(requestParams)
        this.current = resp
      } catch (error) {
        const errorsStore = useErrorsStore()
        await errorsStore.showAndThrowError(error)
      }
    },
    async editBook (id, commentObj) {
      const requestParams = {
        routePath: `${API_PATH}/${id}`,
        body: commentObj,
      }

      try {
        const resp = await http.put(requestParams)
        this.current = resp
      } catch (error) {
        const errorsStore = useErrorsStore()
        await errorsStore.showAndThrowError(error)
      }
    },
    async deleteBook (id) {
      const requestParams = {
        routePath: `${API_PATH}/${id}`,
      }

      try {
        await http.delete(requestParams)
      } catch (error) {
        const errorsStore = useErrorsStore()
        await errorsStore.showAndThrowError(error)
      }
    },
    clearList () {
      this.books = null
    },
    clearCurrent () {
      this.current = null
    },
  },
})
