<template id="navbar">

  <nav class="navbar navbar-expand-md navbar-dark bg-dark">
    <div class="container-md">
      <a class="navbar-brand mb-0 h1" href="#">DomiBot</a>
      <button type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation" class="navbar-toggler">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
        <ul class="navbar-nav">
          <li class="nav-item">
            <router-link to="/" class="nav-link" active-class="active">Home</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/dashboard" class="nav-link" active-class="active">Dashboard</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/login" class="nav-link" active-class="active">Login</router-link>
          </li>
          <li class="nav-item">
            <img class="pfp nav-link" alt="Pfp" :src="profileIcon">
          </li>
        </ul>
      </div>
    </div>
  </nav>

</template>

<script>
import pfp from "../assets/pfp.png"

export default {
  name: "Navbar",
  data: () => ({
    profileIcon: pfp
  })
}

</script>

<style scoped>

.pfp {
  border-radius: 100%;
  width: 3rem;
  margin-left: 1rem;
}

li.nav-item {
  display: flex;
  align-items: center;
  justify-content: center;
}

.navbar-nav {
  --bs-nav-link-hover-color: var(--bs-nav-link-color);
}

</style>