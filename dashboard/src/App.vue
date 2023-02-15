<script setup lang="ts">
import Navbar from "./components/Navbar.vue";
</script>

<template id="app">
  <Navbar />

  <RouterView />
</template>

<style lang="scss">
@use "src/assets/theme.scss";
</style>
