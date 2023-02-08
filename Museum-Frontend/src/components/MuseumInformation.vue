<template>
  <div id="museuminformation">
    <span v-if="username">
      <MainNav :currentUsername="username" :currentUserType="userType"/>
    </span>
    <span v-else>
      <HomeNav />
    </span>
  <div class="page-content page-container">
    <div class="padding">
      <div class="row container d-flex justify-content-center">
        <div class="col-lg-8 grid-margin stretch-card">
            <div class="card">
              <div class="card-body">
                <h3>Museum Information</h3>
                  <div class="table-responsive mt-3">
                    <table class="table">
                      <tbody>
                        <tr>
                          <td>Visitor Fee:</td>
                          <td>
                          <span v-if="(updating && adminUser)">
                            <b-form-input class="inputM" size="sm" type='number' v-model="updatedVF" :placeholder="this.museumInformation.visitorFee.toString()"></b-form-input>
                          </span>
                          <span v-else>
                            ${{this.museumInformation.visitorFee}}
                          </span>
                          </td>
                        </tr>
                        <tr>
                          <td>Monday Hours:</td>
                          <td>
                          <span v-if="(updating && adminUser)">
                            <b-form-input class="inputM" size="sm" v-model="updatedM" :placeholder="museumInformation.mondayHours"></b-form-input>
                          </span>
                          <span v-else>
                            {{museumInformation.mondayHours}}
                          </span>
                          </td>
                        </tr>
                        <tr>
                          <td>Tuesday Hours:</td>
                          <td>
                            <span v-if="(updating && adminUser)">
                            <b-form-input class="inputM" size="sm" v-model="updatedT" :placeholder="museumInformation.tuesdayHours"></b-form-input>
                          </span>
                          <span v-else>
                            {{museumInformation.tuesdayHours}}
                          </span>
                          </td>
                          
                        </tr>
                        <tr>
                          <td>Wednesday Hours:</td>
                          <td>
                          <span v-if="(updating && adminUser)">
                            <b-form-input class="inputM" size="sm" v-model="updatedW" :placeholder="museumInformation.wednesdayHours"></b-form-input>
                          </span>
                          <span v-else>
                            {{museumInformation.wednesdayHours}}
                          </span>
                          </td>
                        </tr>
                        <tr>
                          <td>Thursday Hours:</td>
                          <td>
                          <span v-if="(updating && adminUser)">
                            <b-form-input class="inputM" size="sm" v-model="updatedTR" :placeholder="museumInformation.thursdayHours"></b-form-input>
                          </span>
                          <span v-else>
                            {{museumInformation.thursdayHours}}
                          </span>
                        </td>
                        </tr>
                        <tr>
                          <td>Friday Hours:</td>
                          <td>
                          <span v-if="(updating && adminUser)">
                            <b-form-input class="inputM" size="sm" v-model="updatedF" :placeholder="museumInformation.fridayHours"></b-form-input>
                          </span>
                          <span v-else>
                            {{museumInformation.fridayHours}}
                          </span>
                        </td>
                        </tr>
                        <tr>
                          <td>Saturday Hours:</td>
                          <td>
                          <span v-if="(updating && adminUser)">
                            <b-form-input class="inputM" size="sm" v-model="updatedST" :placeholder="museumInformation.saturdayHours"></b-form-input>
                          </span>
                          <span v-else>
                            {{museumInformation.saturdayHours}}
                          </span>
                          </td>
                        </tr>
                        <tr>
                          <td>Sunday Hours:</td>
                          <td>
                          <span v-if="(updating && adminUser)">
                            <b-form-input class="inputM" size="sm" v-model="updatedS" :placeholder="museumInformation.sundayHours"></b-form-input>
                          </span>
                          <span v-else>
                            {{museumInformation.sundayHours}}
                          </span>
                          </td>
                        </tr>
                      </tbody>
                  </table>
                        
                  </div>
                  <div align="right" >
                    <span v-if="(updating && adminUser)">
                      <button type="button" class="btn btn-outline-success" v-on:click="updateMuseumInformation()">Ok</button>
                      <button type="button" class="btn btn-outline-danger" v-on:click="closeAndClear()">Cancel</button>
                    </span>
                    <span v-else-if="adminUser">
                      <button type="button" class="btn btn-outline-primary" v-on:click="toggleUpdating()">Update</button>
                    </span>
                  </div>
                  <b-modal v-model="modalShow" title="Message:" hide-footer>
                    <div class="d-block text-center">
                        {{errorMuseumInformation}}
                    </div>
                    <div>
                        <button type="button" class="btn btn-outline-danger mt-3" block v-on:click="closeModal()">Close</button>
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
  import HomeNav from './HomeNav.vue'
  import MainNav from './MainNav.vue'
  var config = require('../../config')
  
  var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
  var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
  
  var AXIOS = axios.create({
      baseURL: backendUrl,
      headers: { 'Access-Control-Allow-Origin': frontendUrl }
  })
  
  export default{
      name: "MuseumInformation",
      data() {
        return {
            museumInformation: {
              visitorFee: 0,
              museumName: '',
              mondayHours: '',
              tuesdayHours: '',
              wednesdayHours: '',
              thursdayHours: '',
              fridayHours: '',
              saturdayHours: '',
              sundayHours: ''
            },
            museumName: '',
            updatedM: '',
            updatedT:'',
            updatedW: '',
            updatedTR: '',
            updatedF: '',
            updatedST:'',
            updatedS: '',
            updatedVF: null,
            errorMuseumInformation:'',
            updating: false,
            modalShow: false,
            adminUser: false,
            userType: '',
            username: ''
      };
  },
  components: {
    MainNav,
    HomeNav
  },
  created: function() {
     this.userType = this.$store.getters.getCurrentUserType 
     if(this.userType==='A') {
      this.adminUser = true
     }
      this.username = this.$store.getters.getCurrentUsername
      AXIOS.get('/museumInformation/')
      .then((response) => {
          this.museumInformation = response.data
          console.log(this.museumInformation)
      })
      .catch(e => {
          console.log(e)
      })
  },
  
  methods: {
      updateMuseumInformation: function () { 
        var fields = [
          {
            name: 'MondayHours',
            value: this.updatedM
          }, 
          {
            name: 'TuesdayHours',
            value: this.updatedT
          }, 
          {
            name: 'WednesdayHours',
            value: this.updatedW
          }, 
          {
            name: 'ThursdayHours',
            value: this.updatedTR
          }, 
          {
            name: 'FridayHours',
            value: this.updatedF
          }, 
          {
            name: 'SaturdayHours',
            value: this.updatedST
          }, 
          {
            current: 'S',
            name: 'SundayHours',
            value: this.updatedS
          }, 
          {
            name: 'VisitorFee',
            value: parseFloat(this.updatedVF)
          }]
        var err = false
        //Send update request for each field that is not empty
        for(let i = 0; i < fields.length; i++) {
          if(fields[i].value) {
            console.log(fields[i].value)
            AXIOS.post("/museumInformation/update"+fields[i].name,
            {
              visitorFee : (i===7?fields[7].value:this.museumInformation.visitorFee),
              museumName : this.museumInformation.museumName,
              mondayHours : (i===0?fields[0].value:this.museumInformation.mondayHours),
              tuesdayHours : (i===1?fields[1].value:this.museumInformation.tuesdayHours),
              wednesdayHours : (i===2?fields[2].value:this.museumInformation.wednesdayHours),
              thursdayHours : (i===3?fields[3].value:this.museumInformation.thursdayHours),
              fridayHours : (i===4?fields[4].value:this.museumInformation.fridayHours),
              saturdayHours : (i===5?fields[5].value:this.museumInformation.saturdayHours),
              sundayHours : (i===6?fields[6].value:this.museumInformation.sundayHours),

            }, {})
            .then((response) => {
              console.log(response.data)
              this.museumInformation = response.data
            })
            .catch(e => {
              err = true
              console.log(e)
              this.errorMuseumInformation += e.response.data+' '
              this.modalShow = true
            })
          }
          if(!this.modalShow) {
            this.updatedM = ''
            this.updatedT = ''
            this.updatedW = ''
            this.updatedTR = ''
            this.updatedF = ''
            this.updatedST = ''
            this.updatedS = ''
            this.updatedVF = ''
            this.errorMuseumInformation = ''
            this.updating = false
          }
        }
      },
      toggleUpdating: function() {
        this.updating = !this.updating
      },
      closeAndClear: function() {
        this.errorMuseumInformation = ''
        this.updatedM = ''
        this.updatedT = ''
        this.updatedW = ''
        this.updatedTR = ''
        this.updatedF = ''
        this.updatedST = ''
        this.updatedS = ''
        this.updatedVF = ''
        this.errorMuseumInformation = ''
        this.updating = false
      },
      closeModal: function() {
        this.errorMuseumInformation = ''
        this.modalShow = false
      },
      openNameModal: function() {
        this.updatedName = ''
        this.name_modal = true
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

  .inputM {
    border-radius: 0;
    border-top: 0;
    border-left: 0;
    border-right: 0;
  }

  </style>