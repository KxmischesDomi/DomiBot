import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// import './assets/main.css'

import 'bootstrap/dist/css/bootstrap.css'

const app = createApp(App)
    .use(router)
    .mount('#app')

import 'bootstrap/dist/js/bootstrap.js'