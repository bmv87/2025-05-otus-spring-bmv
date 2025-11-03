import { defineStore } from 'pinia'
import { http } from '@/api/http'
import { useErrorsStore } from '@/stores/errors'

const API_PATH = 'comments'
export const useCommentsStore = defineStore('comments', {
  state: () => {
    return {
      comments: [],
    }
  },
  actions: {
    async getCommentsByBook (bookId) {
      const errorsStore = useErrorsStore()
      await errorsStore.hideError()

      const requestParams = {
        routePath: API_PATH,
        params: { bookId },
      }
      try {
        const resp = await http.get(requestParams)
        this.comments = resp || []
      } catch (error) {
        const errorsStore = useErrorsStore()
        await errorsStore.showAndThrowError(error)
      }
    },
    async getComment (id) {
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
    async addComment (commentObj) {
      const requestParams = {
        routePath: API_PATH,
        body: commentObj,
      }

      try {
        await http.post(requestParams)
      } catch (error) {
        const errorsStore = useErrorsStore()
        await errorsStore.showAndThrowError(error)
      }
    },
    async editComment (id, commentObj) {
      const requestParams = {
        routePath: `${API_PATH}/${id}`,
        body: commentObj,
      }

      try {
        await http.put(requestParams)
      } catch (error) {
        const errorsStore = useErrorsStore()
        await errorsStore.showAndThrowError(error)
      }
    },
    async deleteComment (id) {
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
      this.comments = null
    },
  },
})
