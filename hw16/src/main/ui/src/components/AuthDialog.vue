<template>
  <dialog-switcher
    color="primary"
    :max-width="maxWidth"
    :show="show"
    :title="'Enter credentials'"
    @close="close"
  >
    <template #card>
      <custom-input v-model="login" label="Username" required />
      <custom-input
        v-model="password"
        label="Password"
        required
        type="password"
      />
    </template>
    <template #buttons>
      <VBtnPrimary
        id="continueBtn"
        color="primary"
        :disable="!login || !password"
        text="Apply"
        @click.stop="continueOperation"
      />
    </template>
  </dialog-switcher>
</template>

<script setup>
  import DialogSwitcher from '@/components/DialogSwitcher.vue'
  import CustomInput from '@/components/ui/CustomInput.vue'
  const emits = defineEmits(['close'])

  const prop = defineProps({
    show: {
      type: Boolean,
      default: false,
    },
    maxWidth: {
      type: [String, Number],
      default: '600',
    },
  })
  const login = ref(null)
  const password = ref(null)

  async function continueOperation () {
    localStorage.setItem('auth', window.btoa(`${login.value}:${password.value}`))
    location.reload()
  }
  function close () {
    emits('close')
  }
</script>
