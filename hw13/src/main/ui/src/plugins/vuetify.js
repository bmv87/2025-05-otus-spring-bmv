/**
 * plugins/vuetify.js
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Composables
import { createVuetify } from 'vuetify'
import { aliases, mdi } from 'vuetify/iconsets/mdi'

import colors from 'vuetify/util/colors'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { VBtn } from 'vuetify/components/VBtn'
// Styles
import '@mdi/font/css/materialdesignicons.css'

import 'vuetify/styles'
console.log(colors)
// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      mdi
    }
  },
  components: {
    ...components
  },
  directives,
  defaults: {
    VAlert: {
      class: 'text-center ma-1',
      density: 'compact'
    },
    VBtnPrimary: {
      class: 'text-textwithbackground',
      variant: 'elevated',
      color: 'primary',
      density: 'comfortable'
    },
    VBtnSecondary: {
      class: 'text-textwithbackground',
      variant: 'elevated',
      color: 'secondary',
      density: 'comfortable'
    },
    VToolbar: {
      class: 'text-textwithbackground',
      color: 'primary',
      density: 'compact'
    },
    VProgressLinear: {
      color: 'loader',
      indeterminate: true,
      height: 5
    },
    VProgressCircular: {
      color: 'loader',
      indeterminate: true
    },
    VTooltip: {
      location: 'bottom'
    },
    VSwitch: {
      color: 'primary'
    },
    VSelect: {
      variant: 'underlined',
      density: 'default'
    },
    VTextField: {
      variant: 'underlined',
      density: 'default'
    },
    VCombobox: {
      variant: 'underlined',
      density: 'default'
    },
    VTextarea: {
      variant: 'underlined',
      density: 'default'
    },
    VSnackbar: {
      variant: 'flat',
      location: 'top',
      multiLine: true,
      VBtn: {
        color: 'red',
        variant: 'tonal',
        density: 'comfortable'
      }
    },
    VCard: {
      VToolbar: {
        flat: true,
        color: 'primary',
        density: 'compact',
        VToolbarTitle: {
          color: 'textwithbackground'
        }
      },
      VBtnSecondary: {
        variant: 'elevated',
        color: 'secondary',
        density: 'comfortable'
      },
      VBtn: {
        variant: 'elevated',
        color: 'primary',
        density: 'comfortable'
      }
    },
  },
  aliases: {
    VBtnPrimary: VBtn,
    VBtnSecondary: VBtn,
    VBtnApprove: VBtn,
    VBtnReject: VBtn
  },
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        dark: false,
        colors: {
          primary: colors.blue.darken2,
          secondary: colors.grey.darken1,
          accent: colors.blue.darken3,
          error: colors.red.accent2,
          info: colors.blue.darken1,
          success: colors.green.base,
          warning: colors.orange.darken2,
          anchor: colors.blue.base,
          loader: colors.blue.lighten3,
          primaryiconbtn: colors.orange.darken3,
          secondaryiconbtn: colors.shades.white,
          addbtn: colors.green.base,
          requiredfield: colors.red.base,
          textcolor: colors.grey.darken4,
          inactiveitem: colors.grey.darken1,
          textwithbackground: colors.shades.white
        }
      }
    }
  },
})
