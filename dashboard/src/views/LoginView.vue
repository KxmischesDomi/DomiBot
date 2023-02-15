<template>
  <div class="login">
  </div>
</template>
<script lang="ts">

import {inject} from "vue";
import type {VueCookies} from "vue-cookies";

export default {
  beforeCreate() {
    const $cookies = inject<VueCookies>('$cookies');

    if (!$cookies?.isKey("token")) {
      //@ts-ignore
      window.location.href = this.$oauthEndpoint;
    } else {

      var token = $cookies?.get("token");



    }
  }
}

</script>
<style>

</style>