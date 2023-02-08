<template>
    <div id="mainhome">
    <HomeNav />
    <div class="p-3 m-3">
        <h1>Welcome to {{museumName}}</h1><br>
        <div class="d-flex justify-content-center ">
        <b-card-group deck class="p-3 w-75">
            <b-card header="Log In:">
                <b-container class="mt-4">
                    <b-row>
                        <b-col>
                            <b-form-group label="Select account type:" align="left">
                                <b-form-radio v-model="type" name="account_type" value="A">Admin</b-form-radio>
                                <b-form-radio v-model="type" name="account_type" value="E">Employee</b-form-radio>
                                <b-form-radio v-model="type" name="account_type" value="V">Visitor</b-form-radio>
                            </b-form-group>
                        </b-col>
                        <b-col class="mt-3">
                            <b-form-input v-model="username" placeholder="Username" align="left"></b-form-input>
                            <b-form-input class="mt-2" v-model="password" placeholder="Password" type="password" align="left"></b-form-input>
                        </b-col>
                    </b-row>
                </b-container>
                <b-button class="mt-2" variant="outline-primary" v-bind:disabled="((!password)||(!username)||(!type))" v-on:click="userLogIn()">Log In</b-button>
            </b-card>
            <h6 class="mt-5">-OR-</h6>            
            <b-card header="Register for a visitor account:">
                <b-container class="mt-3">
                    <b-form-input v-model="username_r" placeholder="Username"></b-form-input>
                    <b-form-input class="mt-2" v-model="password_r" placeholder="Password" type="password"></b-form-input>
                    <b-form-input class="mt-2" v-model="passwordConfirmation" placeholder="Re-enter password" type="password"></b-form-input>
                </b-container>
                <b-button class="mt-3" variant="outline-primary" v-bind:disabled="((!password_r)||(!username_r)||(!passwordConfirmation))" v-on:click="registerVisitor()">Register</b-button>
            </b-card>
        </b-card-group>
    </div>
        <b-modal v-model="modalShow" title="Message:" hide-footer>
            <div class="d-block text-center">
                {{this.message}}
            </div>
            <div align="right">
                <button type="button" class="btn btn-outline-danger mt-3" block v-on:click="closeModal()">Close</button>
            </div>
        </b-modal>     
    </div>   
    </div>                          
</template>
<script>
import axios, { Axios } from 'axios'
import Router from '../router'
import HomeNav from './HomeNav.vue'
var config = require('../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
    name: 'MainHome',
    data () {
      return {
        museumName: '',
        username: "",
        password: "",
        username_r: "",
        password_r: "",
        passwordConfirmation: "",
        type: "V",
        message: "",
        modalShow: false,
      }
    },
    components: {
        HomeNav
    },
    created: function () {
        AXIOS.get("/museumInformation/")
        .then((response) => {
            this.museumName = response.data.museumName
        })
        .catch((e) => {
            console.log(e)
        })
    },
    methods: {
        userLogIn: function() {
            AXIOS.get("/login/"+this.type+"/"+this.username+"/"+this.password+"/")
            .then((response) => {
                console.log(response.status)
                this.$store.dispatch('setCurrentUsername', this.username)
                this.$store.dispatch('setCurrentPassword', this.password)
                this.$store.dispatch('setCurrentUserType', this.type)
                Router.push('Home')
            })
            .catch(e => {
                console.log(e.response.data)
                this.message = e.response.data
                this.clearFields()
                this.modalShow = true
            })
        },
        registerVisitor: function() {
            if(this.password_r == this.passwordConfirmation) {
                AXIOS.post("/visitor/",{
                    username: this.username_r,
                    password: this.password_r
                    }, {}
                )
                .then((response) => {
                    console.log(response.data)
                    this.clearFields()
                    this.message = "Visitor account has successfully been registered. Please log in to proceed."
                    this.modalShow = true
                })
                .catch(e => {
                    console.log(e.response.data)
                    this.message = e.response.data
                    this.modalShow = true
                    this.clearFields()
                })
            } else {
                this.message = "Passwords do not match!"
                this.modalShow = true
            }

        },
        clearFields: function() {
            this.username_r = ''
            this.username = ''
            this.password = ''
            this.password_r = ''
            this.passwordConfirmation = ''
        },
        closeModal: function() {
            this.modalShow = false
        }
    }
}
</script>
<style>

input[type="text"],
input[type="password"] {
  width: 100%;
  height: 35px;
  font-size: 15px;
  border: 0;
  border-top: 0;
  border-left: 0;
  border-right: 0;
  border-radius: 0;
  border-bottom: 1px solid #aaaaaa;

}

</style>