import { apiEndpoint, oauth2Endpoint } from "./config";
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import 'bootstrap/dist/css/bootstrap.css'

import './assets/main.css'

const app = createApp(App);

app.config.globalProperties.$apiEndpoint = apiEndpoint;
app.config.globalProperties.$oauthEndpoint = oauth2Endpoint;

app.use(router).mount('#app');

import 'bootstrap/dist/js/bootstrap.js'