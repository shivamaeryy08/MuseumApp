<template>
<div class="home">
  <MainNav :currentUsername="username" :currentUserType="userType"/>
  <div class="centered">
      <h1 class="w3-jumbo w3-animate-top">Welcome to {{museumName}}</h1>
  </div>
</div>

</template>

<script>
import axios, { Axios } from 'axios'
import MainNav from './MainNav.vue'
var config = require('../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

  export default {
    name: "Home",
    data() {
      return {
        museumName: '',
        userType: '',
        username: ''
      }
    },
    components: {
      MainNav
    },
    created: function () {
      this.userType = this.$store.getters.getCurrentUserType 
      this.username = this.$store.getters.getCurrentUsername
        AXIOS.get("/museumInformation/")
        .then((response) => {
            this.museumName = response.data.museumName
        })
        .catch((e) => {
            console.log(e)
        })
    },
  }
</script>

<style>

body{font-family: "Raleway", sans-serif;}

h1 { color: #ffffff; font-family: 'Raleway',sans-serif; font-size: 60px; font-weight: 600; line-height: 72px; margin: 0 0 24px; text-align: center;}

.home{
  position: fixed;
  color: white;
  background-image: url('https://cdn.britannica.com/51/194651-050-747F0C18/Interior-National-Gallery-of-Art-Washington-DC.jpg');
  height: 100vh;
  width: 100%;
  background-position: center;
  background-size: contain;
  background-attachment: fixed;
  background-repeat: no-repeat;
  text-align: center;
}

.centered {
  height: 100vh;
  position: relative;
  text-align: center;
  top: 200px;
}

</style>