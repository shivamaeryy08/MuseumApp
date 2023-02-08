<template>
<div>
    <MainNav :currentUsername="username" :currentUserType="userType"/>
<div class="page-content page-container" id="adminshiftmanager">
    <div class="padding">
        <div class="row container d-flex justify-content-center">

<div class="col-lg-8 grid-margin stretch-card">
        <div class="card">
        <div class="card-body">
            <h3>Shifts</h3>
            <!-- Table of shifts -->
            <div class="table-responsive">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Shift ID</th>
                        <th>Date</th>
                        <th>Start Time</th>
                        <th>End Time</th>
                        <th>Employee</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="shift in shifts" :key="shift.shiftID">
                        <td>{{ shift.shiftID }}</td>
                        <td>{{shift.shiftDate}}</td>
                        <td>{{shift.startTime}}</td>
                        <td>{{shift.endTime}}</td>
                        <td>{{shift.employee}}</td>
                        <td>                            
                            <b-button class="deletebutton" size="xs" v-on:click="deleteShift(shift.shiftID)" variant="outline-danger">
                                X
                            </b-button>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <span v-if="(shifts.length===0)" align="center">
                    There are no shifts to display.
                </span>
            </div>
            <div align="right" >
                    <!-- Button to trigger shift creation popup -->
                    <button class="btn btn-outline-primary" v-on:click="scheduleShift">Add new shift</button>
            </div>
               <!-- Pop-up to create a new shift -->
            <b-modal
            v-model="shift_modal"
            title="Schedule shift:"
            hide-footer
            centered
            >
                <div>
                    <b-form-group>
                        <div>
                            <div class="dropdown">
                                <button class="btn btn-outline-secondary dropdown-toggle" @click="toggleDropdown">
                                    {{dropdownText}}
                                </button>
                                <ul v-if="dropdown" class="dropdown-list mt-1">
                                  <li v-for="employee in this.employees" :key="employee.username" @click="selectEmployee(employee)">{{ employee.username }}</li>
                                </ul>
                              </div>
                        </div>
                        <b-form-datepicker
                        class="mt-2"
                        :min="min"
                        id="shiftdate-input"
                        placeholder="Select a shift date"
                        v-model="shiftDate"
                        ></b-form-datepicker>
                        <b-form-timepicker
                        class="mt-2"
                        id="starttime-input"
                        placeholder="Select a start time"
                        v-model="startTime"
                        ></b-form-timepicker>
                        <b-form-timepicker
                        class="mt-2"
                        id="endtime-input"
                        placeholder="Select an end time"
                        v-model="endTime"
                        ></b-form-timepicker>
                    </b-form-group>
                </div>
                <div>
                    <b-button v-bind:disabled="((!selectedEmployee)||(!shiftDate)||(!startTime)||(!endTime))" v-on:click="createShift()" variant="primary">Create Shift</b-button>
                </div>
                <div class="mt-1">
                    <span v-if="this.errorShift" style="color:red">Error: {{this.errorShift}} </span>
                </div>
            </b-modal>
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

function EmployeeDto(username, password) {
    this.username = username
    this.password = password
}

function ShiftDto(shiftID, date, start_time, end_time, employee) {
    this.shiftID = shiftID
    this.shiftDate = date
    this.startTime = start_time
    this.endTime = end_time
    this.employee = employee
}

export default {
    name: 'AdminShiftManager',
    data () {
        const now = new Date()
        const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
        const minDate = new Date(today)
        minDate.setDate(today.getDate()+1)
        return {
            shift_modal: false,
            dropdown: false,
            shiftDate: '',
            startTime: '',
            endTime: '',
            min: minDate,
            errorShift: '',
            shifts: [],
            employees: [],
            selectedEmployee: '',
            dropdownText: 'Select employee',
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
        AXIOS.get('/shifts/')
        .then((response) => {
            response.data.forEach(shift => {
                var shiftDto = new ShiftDto(shift.shiftID, shift.shiftDate, shift.startTime, shift.endTime, shift.employee.username)
                this.shifts.push(shiftDto)
            });
        })
        .catch(error => {
            console.log(error.response.data)
        })

        AXIOS.get('/employees/')
        .then((response) => {
            response.data.forEach(employee => {
                var employeeDto = new EmployeeDto(employee.username, employee.password)
                this.employees.push(employeeDto)
            })
        })

    },
    methods: {
        createShift: function() {
            this.dropdownText = 'Select employee'
            AXIOS.post("/shift", {
                shiftDate: this.shiftDate,
                startTime: this.startTime,
                endTime: this.endTime,
                employee: this.selectedEmployee
            })
            .then((response) => {
                console.log(response.status)
                var shift = new ShiftDto(response.data.shiftID, response.data.shiftDate, response.data.startTime, response.data.endTime, response.data.employee.username)
                this.shifts.push(shift)
                this.shift_modal = false
                this.errorShift = ''
            })
            .catch(e => {
                console.log(e.response.data)
                this.errorShift = e.response.data
            })
        },
        deleteShift: function(shiftID) {
            console.log(shiftID)
            for (let index = 0; index < this.shifts.length; index++) {
                const element = this.shifts[index];
                if(element.shiftID === shiftID) {
                    this.shifts.splice(index, 1)
                }
            }
            AXIOS.delete("/shift/"+shiftID)
            .catch(e => {
                console.log(e)
            })
        },
        scheduleShift: function() {
            this.shift_modal = true
            this.shiftError = ''
        },
        toggleDropdown: function() {
            this.dropdown = !this.dropdown;
        },
        selectEmployee: function(employee) {
            this.selectedEmployee = employee
            this.dropdownText = employee.username
            this.dropdown = false
        }
    }
}
</script>
<style>
.dropbtn {
    background-color: #4CAF50;
    color: white;
    padding: 5px;
    font-size: 10px;
    border: none;
    cursor: pointer;
  }
.dropdown {
    position: relative;
    display: inline-block;
  }

  .dropdown-title {
    padding: 10px;
    background-color: #eee;
    color: #666;
    border: 1px solid #ccc;
    border-radius: 4px;
    cursor: pointer;
  }

  .dropdown-icon-open {
    float: right;
  }

  .dropdown-icon-closed {
    float: right;
  }

  .dropdown-list {
    position: absolute;
    background-color: #eee;
    color: #666;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
    min-width: 160px;
    padding: 0;
    margin: 0;
    z-index: 300;
    max-height: 300px;
    overflow-y: scroll;
  }

  .dropdown-list li {
    padding: 10px;
    cursor: pointer;
  }

  .dropdown-list li:hover {
    background-color: #ccc;
  }

  body {
    background-color: #f9f9fa
}

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

.deletebutton {
    padding: 2px 5px;
    font-size: 12px;
}
</style>