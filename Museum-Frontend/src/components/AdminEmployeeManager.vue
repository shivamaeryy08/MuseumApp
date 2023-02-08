<template>
<div>
    <MainNav :currentUsername="username" :currentUserType="userType"/>
<div class="page-content page-container" id="adminemployeemanager">
    <div class="padding">
        <div class="row container d-flex justify-content-center">

<div class="col-lg-8 grid-margin stretch-card">
        <div class="card">
        <div class="card-body">
            <h3>Employees</h3>
            <!-- Table of employees -->
            <div class="table-responsive">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Username</th>
                        <th>Password</th>
                        <th>Click to fire</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="employee in employees" :key="employee.username">
                        <td>{{ employee.username }}</td>
                        <td>{{employee.password}}</td>
                        <td>                            
                            <b-button class="firebutton" size="xs" v-on:click="deleteEmployee(employee.username)" variant="outline-danger">
                                X
                            </b-button>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <span v-if="(employees.length===0)">
                    There are no employees to display.
                </span>
            </div>
            <div align="right" >
                    <!-- Button to trigger employee registration popup -->
                    <button class="btn btn-outline-primary" v-on:click="openEmployeeModal()" variant="outline">Add New</button>
            </div>
                
            <!-- Employee Registration Popup -->
            <b-modal
            v-model="employee_modal"
            title="Register Employee:"
            hide-footer
            centered
            >
                <div>
                    <b-form-group>
                        <b-form-input
                        id="username-input"
                        v-model="usernameEmployee"
                        placeholder="Enter a username"
                        ></b-form-input>
                        <b-form-input
                        class="mt-2"
                        id="password-input"
                        v-model="passwordEmployee"
                        placeholder="Enter a password"
                        ></b-form-input>
                    </b-form-group>
                </div>
                <div>
                    <button class="btn btn-outline-primary" v-bind:disabled="((!passwordEmployee)||(!usernameEmployee))" v-on:click="registerEmployee()" variant="primary">Register</button>
                </div>
                <div>
                    <span v-if="this.errorEmployee" style="color:red">Error: {{this.errorEmployee}} </span>
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
    this.password= password
}

export default {
    name: 'AdminEmployeeManager',
    data () {
        return {
            usernameEmployee: '',
            passwordEmployee: '',
            employees: [],
            employee_modal: false,
            errorEmployee: '',
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
        AXIOS.get('/employees/')
        .then((response) => {
            this.employees = response.data
        })
    },
    methods: {
        deleteEmployee: function(username) {
            AXIOS.delete('/employee/'+username)
            .catch(e => {
                console.log(e)
            })
            for (let index = 0; index < this.employees.length; index++) {
                const element = this.employees[index];
                if(element.username === username) {
                    this.employees.splice(index, 1)
                }
            }
        },
        registerEmployee: function() {
            console.log(this.passwordEmployee)
            console.log(typeof this.passwordEmployee)
            AXIOS.post("/employee/",{
                username: this.usernameEmployee,
                password: this.passwordEmployee
                }, {}
            )
            .then((response) => {
                console.log(response.data)
                var employee = new EmployeeDto(response.data.username, response.data.passwordEmployee)
                this.employees.push(employee)
                this.employee_modal = false
                this.usernameEmployee = ''
                this.passwordEmployee = ''
                this.errorEmployee = ''

            })
            .catch(e => {
                console.log(e.response.data)
                this.errorEmployee = e.response.data
            })
            
        },
        openEmployeeModal: function() {
            this.employee_modal = true
        },
    }
}
        
</script>
<style>
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

.tbody {
    overflow-y: auto;
    height: 200px;
}

.firebutton {
    padding: 2px 5px;
    font-size: 12px;
}

.plusbutton {
    border-color: #1C4457;
    background: white;
    color: #1C4457;
    padding: 5px 15px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 15px;
    margin: 4px 2px;
    border-radius: 8px;
}
</style>