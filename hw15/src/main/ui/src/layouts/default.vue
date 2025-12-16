<template>
  <v-main>
    <v-container fill-height fluid>
      <v-row
        align="center"
        justify="center"
      >
        <v-col
          align="center"
          justify="center"
        >
          <router-view />
        </v-col>
      </v-row>
    </v-container>
  </v-main>

  <AppFooter />
</template>

<script setup>
  //
</script>
