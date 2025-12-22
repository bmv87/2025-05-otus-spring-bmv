<template>
  <v-container class="fill-height" max-width="900">
    <div>

      <v-row>
        <v-col cols="12">
          <v-card
            class="py-4"
            color="surface-variant"
            prepend-icon="mdi-rocket-launch-outline"
            rounded="lg"
            variant="tonal"
          >
            <template #image>
              <v-img position="top right" />
            </template>

            <template #title>
              <h2 class="text-h5 font-weight-bold">
                Library
              </h2>
            </template>
          </v-card>
        </v-col>

        <v-col v-for="link in links" :key="link.route" cols="6">
          <v-card
            append-icon="mdi-open-in-new"
            class="py-4"
            color="surface-variant"
            :prepend-icon="link.icon"
            rounded="lg"
            :subtitle="link.subtitle"
            :title="link.title"
            :to="link.route"
            variant="tonal"
          />
        </v-col>
      </v-row>
    </div>
  </v-container>
</template>

<script setup>
  const links = [
    {
      route: '/books',
      icon: 'mdi-book-open-variant',
      subtitle: 'List of books.',
      title: 'Books',
    },
    {
      route: '/genres',
      icon: 'mdi-star-circle-outline',
      subtitle: 'List of genres.',
      title: 'Genres',
    },
    {
      route: '/authors',
      icon: 'mdi-account',
      subtitle: 'List of authors.',
      title: 'Authors',
    },
  ]
</script>
