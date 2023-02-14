<template id="dashboard">
  <a href="/logout">Logout</a>

  <h1>Logged in as {{ this.session.name }}#{{ this.session.discriminator }}</h1>

  <ul class="guild-list">
    <li v-for="guild in guilds">
      <div class="guild">
        <div class="guild-icon">
          <div class="guild-name">{{ guild.name }}</div>
        </div>
      </div>
    </li>
  </ul>

</template>
<script>
app.component("dashboard", {
  template: "#dashboard",
  data: () => ({
    session: {},
    guilds: []
  }),
  created() {
    fetch("/api/session")
    .then(res => res.json())
    .then(res => this.session = res)
    fetch("/api/guilds")
        .then(res => res.json())
        .then(res => this.guilds = res)
  }
});
</script>
<style>

</style>