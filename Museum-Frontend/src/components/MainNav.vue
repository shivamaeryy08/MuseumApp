<template>
<div class="fixed-top">
  <b-navbar toggleable="lg" type="light" variant="light">
    <b-navbar-brand to="/home">Hi {{currentUsername}}!</b-navbar-brand>
    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

          <span v-if="currentUserType==='V'">
            <b-navbar-nav horizontal>
              <b-nav-item to="/artworklayout">Artwork</b-nav-item>
              <b-nav-item to="/orderinformation">My Museum Passes</b-nav-item>
              <b-nav-item to="/viewloanrequests">My Loans</b-nav-item>
            </b-navbar-nav>
          </span>
          <span v-else-if="currentUserType==='A'">
            <b-navbar-nav horizontal>
              <b-nav-item to="/artworklayout">Museum Layout</b-nav-item>
              <b-nav-item to="/viewloanrequests">Loan requests</b-nav-item>
              <b-nav-item to="/shiftscheduler">Shift Scheduler</b-nav-item>
              <b-nav-item to="/employeemanager">Employee Manager</b-nav-item>
              <b-nav-item to="/orderinformation">Visitor Passes</b-nav-item>
            </b-navbar-nav>
          </span>
          <span v-else>
            <b-navbar-nav horizontal>
              <b-nav-item to="/artworklayout">Museum Layout</b-nav-item>
              <b-nav-item to="/viewloanrequests">Loan requests</b-nav-item>
              <b-nav-item to="/scheduleviewer">My Shifts</b-nav-item>
              <b-nav-item to="/orderinformation">Visitor Passes</b-nav-item>
            </b-navbar-nav>
          </span>
      <!-- Right aligned nav items -->
      <b-navbar-nav small class="ml-auto">
        <b-nav-item to='/museuminformation'>Hours and Fees</b-nav-item>
        <b-nav-item @click="signOut">Sign Out</b-nav-item>
      </b-navbar-nav>
  </b-navbar>
</div>
</template>
  
<script>
import Router from '../router'

  export default {
    name: "MainNav",
    props: [
      'currentUserType',
      'currentUsername'
    ],
    methods: {
      signOut: function() {
        console.log('hi')
        this.$store.dispatch('logout')
        Router.push('/')
      }
    }
  }
</script>
  
  