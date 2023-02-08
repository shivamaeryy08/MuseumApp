<template>
    <div id="viewLoanRequests">
        <MainNav :currentUsername="username" :currentUserType="userType"/>
    <div class="page-content page-container" >
    <div class="padding">
        <div class="row container d-flex justify-content-center">
        <div class="col-lg-8 grid-margin stretch-card">
            <div class="card">
            <div class="card-body">
                <h3>Loan Requests</h3>
                <!-- Table of loanRequests -->
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Due Date</th>
                            <th>Start Date</th>
                            <th>Status</th>
                            <th>Artwork</th>
                            <th v-if="(userType !== 'V')">Requester</th>
                            <th v-if="(userType !== 'V')">Actions</th> 
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="loanRequest in loanRequests" :key="loanRequest.requestID">
                            <td>{{ loanRequest.requestedEndDate }}</td>
                            <td>{{ loanRequest.requestedStartDate }}</td>
                            <td>
                                <!-- Badge indicating whether shift is upcoming or passed -->
                                <span v-if="(loanRequest.status==='Pending')">
                                  <b-badge pill variant="warning">Pending</b-badge>
                                </span>
                                <span v-else-if="(loanRequest.status==='Rejected')">
                                  <b-badge pill variant="danger">Denied</b-badge>
                                </span>
                                <span v-else>
                                    <b-badge pill variant="success">Approved</b-badge>
                                  </span>
                              </td>
                            <td>{{ loanRequest.artwork.name }}</td>
                            <td v-if="(userType !== 'V')">
                            {{ loanRequest.requester.username }}
                            </td>
                            <td v-if="(userType !== 'V')">                            
                                <b-button class="approvalbutton" v-bind:disabled="loanRequest.status !== 'Pending'" v-on:click="approveRequest(loanRequest.requestID)" variant="outline-success">
                                    Approve
                                </b-button>                            
                                <b-button class="approvalbutton" v-bind:disabled="loanRequest.status !== 'Pending'" v-on:click="denyRequest(loanRequest.requestID)" variant="outline-danger">
                                    Deny
                                </b-button>
                            </td>
                        </tr>
                        </tbody>
                    </table>   
                    <span v-if="(loanRequests.length===0)">
                        There are no loan requests to display.
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
    name: 'LoanRequestManager',
    data () {
        return {
            userType : '',
            username : '',
            loanRequests: []
        }
    },
    components: {
        MainNav
    },
    created: function() {
        this.userType = this.$store.getters.getCurrentUserType
        this.username = this.$store.getters.getCurrentUsername
        if (this.userType === 'V') { // get loan requests specific to currnt visitor only
            AXIOS.get('/loanRequests/' + this.username)
            .then((response) => {
                response.data.forEach(loanRequest => {
                    this.loanRequests.push(loanRequest)
                })
            }).catch(e => {
                    console.log(e)
            })
        } else { // get all loan requests to view/ approve any of them            
            AXIOS.get('/loanRequests')
            .then((response) => {
                console.log(response)
                this.loanRequests = response.data
            }).catch(e => {
                    console.log(e)
            })   
        }
        
        console.log(this.loanRequests)
    },
    methods: {
        updateStatus : function(loanRequestId, moddedLoanRequest) {
            for (let index = 0; index < this.loanRequests.length; index++) {
                const element = this.loanRequests[index];
                if(element.requestID === loanRequestId) {
                    this.loanRequests[index].status = moddedLoanRequest.status
                }
            }  
        },
        denyRequest : function(id) {
            AXIOS.post('/loanRequest/' + id + '/denyLoanRequest', {}, {})
            .then((response) => {
                this.updateStatus(id, response.data)
            })
            .catch(e => {
                console.log(e)
            })
        },
        approveRequest : function(id) {
            AXIOS.post('/loanRequest/' + id + '/approveLoanRequest/', {}, {})
            .then((response) => {
                this.updateStatus(id, response.data)
            })
            .catch(e => {
                console.log(e)
            })
        }
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
.approvalbutton {
    padding: 2px 5px;
    font-size: 12px;
}
</style>
    