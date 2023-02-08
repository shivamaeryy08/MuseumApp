<template>
  <div>
    <MainNav :currentUsername="username" :currentUserType="userType"/>
  <div class="page-content page-container" id="shiftscheduleviewer">
    <div class="padding">
        <div class="row container d-flex justify-content-center">

    <div class="col-lg-8 grid-margin stretch-card">
        <div class="card">
          <div class="card-body">
            <h3>Your Shifts</h3>
            <!-- Table of shifts -->
            <div class="table-responsive">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Shift ID</th>
                        <th>Date</th>
                        <th>Start Time</th>
                        <th>End Time</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                        <tr v-for="shift in shifts" :key="shift.shiftID">
                            <td>{{shift.shiftID }}</td>
                            <td>{{shift.shiftDate}}</td>
                            <td>{{shift.startTime}}</td>
                            <td>{{shift.endTime}}</td>
                            <td>
                              <!-- Badge indicating whether shift is upcoming or passed -->
                              <span v-if="isPastDate(shift.date)">
                                <b-badge pill variant="dark">passed</b-badge>
                              </span>
                              <span v-else>
                                <b-badge pill variant="success">upcoming</b-badge>
                              </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <span v-if="(shifts.length===0)">
                  There are no shifts to display.
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
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
    name: 'ShiftScheduleViewer',
    data () {   
      return {
        shifts: [],
        userType: '',
        username: ''
      }
    },
    components: {
        MainNav
    },
    created: function() {
      this.userType = this.$store.getters.getCurrentUserType 
      this.username = this.$store.getters.getCurrentUsername

      //Retrieve shifts
      console.log(this.username)
      AXIOS.get('/shifts/'+this.username+'/')
      .then((response) => {
        this.shifts = response.data
        console.log(response.data)
      })
      .catch(e => {
        console.log(e)
      })
    },
    methods: {
      isPastDate: function(date) {
        var today = new Date()
        var d = new Date(date)
        return today.getTime()<d.getTime()
      }
    }
}
</script>
<style>
.flex {
  -webkit-box-flex: 1;
  -ms-flex: 1 1 auto;
  flex: 1 1 auto
}

@media (max-width:991.98px) {
  .padding {
      padding: 1.5rem
  }
}

@media (max-width:767.98px) {
  .padding {
      padding: 1rem
  }
}

.padding {
  padding: 5rem
}

.card {
  box-shadow: none;
  -webkit-box-shadow: none;
  -moz-box-shadow: none;
  -ms-box-shadow: none
}

.pl-3,
.px-3 {
  padding-left: 1rem !important
}

.card {
  position: relative;
  display: flex;
  flex-direction: column;
  min-width: 0;
  word-wrap: break-word;
  background-color: #fff;
  background-clip: border-box;
  border: 1px solid #d2d2dc;
  border-radius: 0
}

.card .card-title {
 color: #000000;
 margin-bottom: 0.625rem;
 text-transform: capitalize;
 font-size: 0.875rem;
 font-weight: 500;
}

.card .card-description {
 margin-bottom: .875rem;
 font-weight: 400;
 color: #76838f;
}

p {
 font-size: 0.875rem;
 margin-bottom: .5rem;
 line-height: 1.5rem;
}

.table-responsive {
 display: block;
 width: 100%;
 overflow-x: auto;
}

.table, .jsgrid .jsgrid-table {
 width: 100%;
 max-width: 100%;
 margin-bottom: 1rem;
 background-color: transparent;
}

.table thead th, .jsgrid .jsgrid-table thead th {
 border-top: 0;
 border-bottom-width: 1px;
 border-bottom: #92D47A;
 font-weight: 500;
 font-size: .875rem;
 text-transform: uppercase;
}

.table td, .jsgrid .jsgrid-table td {
 font-size: 0.875rem;
 padding: .875rem 0.9375rem;
}
</style>