const useCloseDialog = () => {
  const closeDialog = (dialogOptions) => {
    if (dialogOptions) {
      Object.keys(dialogOptions).forEach(key => {
        dialogOptions[key] = key === 'show' ? false : null
      })
    }
  }
  return closeDialog
}

export default useCloseDialog
