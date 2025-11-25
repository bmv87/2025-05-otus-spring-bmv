// import { useStore } from 'vuex'

import {
  isNavigationFailure, NavigationFailureType,
  useRoute, useRouter,
} from 'vue-router'

function useRouterHelpers () {
  const router = useRouter()
  const route = useRoute()

  const goToRoute = async routePath => {
    if (route.path !== routePath) {
      try {
        await router.push({
          path: routePath,
        })
      } catch (error) {
        if (isNavigationFailure(error, NavigationFailureType.duplicated)) {
          console.log(`Message: ${error.message}`)
          console.log(`From: ${error.from.path}`)
          console.log(`To: ${error.to.toString()}`) // '/admin'
          throw error
          // '/'
        }
      }
    }
  }

  const replaceCurrentRoute = async routePath => {
    if (route.path !== routePath) {
      await router.replace({
        path: routePath,
      })
    }
  }

  return {
    goToRoute,
    replaceCurrentRoute,
  }
}

export default useRouterHelpers
