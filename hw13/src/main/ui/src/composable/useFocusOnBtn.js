import { nextTick } from 'vue'

const useFocusOnBtn = (objectRef) => {
  const focusOnBtn = async () => {
    await nextTick()
    objectRef.value.$el.focus()
    if (objectRef.value.$refs.btn) {
      objectRef.value.$refs.btn.$el.focus()
    }
  }

  return focusOnBtn
}

export default useFocusOnBtn

export const focusOnBtn = async (objectRef) => {
  await nextTick()
  console.log(objectRef)

  objectRef.value.$el.focus()
  if (objectRef.value.$refs.btn) {
    objectRef.value.$refs.btn.$el.focus()
  }
}