<template>
    <div>
        <MainNav :currentUsername="username" :currentUserType="userType"/>
    <div class="page-content page-container" id="orderinformation">
        <div class="padding">
            <div class="row container d-flex justify-content-center">
    
    <div class="col-lg-8 grid-margin stretch-card">
            <div class="card">
            <div class="card-body">
                <span v-if="(visitor.username)">
                    <h3>Your Museum Pass Orders</h3>
                </span>
                <span v-else>
                    <h3>Museum Pass Orders</h3>
                </span>
                <!-- Table of orders -->
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Date ordered</th>
                            <th># of passes</th>
                            <th>Total Price</th>
                            <th v-if="!(userType==='V')">Visitor</th>                            
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="order in orders" :key="order.orderID">
                            <td>{{ order.orderID }}</td>
                            <td>{{order.orderDate}}</td>
                            <td>{{order.amount}}</td>
                            <td>{{order.totalPrice}}</td>
                            <td v-if="!(userType==='V')" :key="order.visitor.username">{{order.visitor.username}}</td>
                        </tr>
                        </tbody>
                    </table>
                    <span v-if="(orders.length===0)" align="center">
                        There are no orders to display.
                    </span>
                </div>
                <span v-if="(userType==='V')">
                    <div align="right" >
                        <!-- Button to trigger order creation popup -->
                        <button class="btn btn-outline-primary" v-on:click="placeOrder()">Purchase pass</button>
                    </div>
                </span>
                <!-- Pop-up to create a new order -->
                <b-modal
                v-model="order_modal"
                title="Purchase Pass:"
                hide-footer
                centered
                >
                <span>
                    Pass price:  ${{visitorFee}}
                </span>
                    <div class="mb-2 mt-2">
                        <label for="sb-inline">Amount:</label>
                        <b-form-spinbutton id="sb-inline" v-model="amount" min="1" step="1" inline></b-form-spinbutton>
                    </div>
                    <span>
                        <u>Total Price:</u>  ${{price()}}
                    </span>
                    <div class="mt-3">
                        <b-button v-on:click="createOrder()" variant="primary">Order</b-button>
                        <button class="btn btn-outline-danger" v-on:click="closeModal()">Cancel</button>
                    </div>
                    <div class="mt-1">
                        <span v-if="this.errorOrder" style="color:red">Error: {{this.errorOrder}} </span>
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

export default {
    name: 'OrderInformation',
    data () {
        return {
            order_modal: false,
            amount: 1,
            visitorFee: '',
            totalPrice: 0,
            errorOrder: '',
            orders: [],
            visitor: {
                username: '',
                password: ''
            },
            userType: '',
            username: ''
        }
    },
    components: {
        MainNav
    },
    created: function() {
        this.userType = this.$store.getters.getCurrentUserType
        var password = this.$store.getters.getCurrentPassword 
        this.username = this.$store.getters.getCurrentUsername
        if(this.userType === 'V') {
            this.visitor = {
                username: this.username,
                password: password
            }
            console.log(this.visitor)
            AXIOS.get("/museumInformation/")
            .then((response) => {
                this.visitorFee = response.data.visitorFee
            })
            .catch(e => {
                console.log(e)
            })
            AXIOS.get("/"+this.visitor.username+"/orders/")
            .then((response) => {
                this.orders = response.data
                console.log(response.data)
            })

        } else {
            AXIOS.get('/orders/')
            .then((response) => {
                this.orders = response.data
            })
            .catch(error => {
                console.log(error)
            }) 
        }
    },
    methods: {
        createOrder: function() {
            AXIOS.post("/order", {
                amount: this.amount,
                visitor: this.visitor
            }, {})
            .then((response) => {
                console.log(response)
                this.orders.push(response.data)
                this.order_modal = false
                this.errorOrder = ''
            })
            .catch(e => {
                console.log(e.response.data)
                this.errorOrder = e.response.data
            })
        },
        placeOrder: function() {
            this.order_modal = true
            this.orderError = ''
        },
        closeModal: function() {
            this.order_modal = false
        },
        price: function() {
            return this.visitorFee*this.amount
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

table.scrolling td:first-child,
table.scrolling th:first-child {
    position: -webkit-sticky;
    position: sticky;
    left: 0;
}

.deletebutton {
    padding: 2px 5px;
    font-size: 12px;
}
</style>