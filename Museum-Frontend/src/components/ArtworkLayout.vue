<template>
    <div id="ArtworkLayout">
        <MainNav :currentUsername="username" :currentUserType="userType"/>
        <div class="padding">
                <div class="d-flex justify-content-center">
                    <div class="row row-cols-1 row-cols-md-1 g-4">
                    <div class="row">
                    <b-card class="mb-3" style="width: 100%;">
                        <b-card-header><h2>Our Artwork</h2></b-card-header>
                        <div class="listOfArtworks">
                            <b-card-body>
                            <table class="table table-image">
                                <thead>
                                    <th v-if="!(userType==='V')">ID</th>
                                    <th >Image</th>
                                    <th >Title</th>
                                    <th >Artist</th>
                                    <th >Description</th>
                                    <th >Year</th>
                                    <th >Loan Price/Day</th>
                                    <th >Loanable</th>
                                    <th >Room details</th>
                                    <th >Action</th>
                                </thead>
                                <tbody>
                                    <tr v-for="artwork in artworks">
                                        <td v-if="!(userType==='V')">{{ artwork.artworkID }}</td>
                                        <td class="w-25">
                                            <img v-bind:src="artwork.imgUrl" class="img-fluid img-thumbnail"
                                                alt="No Picture">
                                        </td>
                                        <td>{{ artwork.name }}</td>
                                        <td>{{ artwork.artist }}</td> 
                                        <td>{{ artwork.description }}</td>
                                        <td>{{ artwork.year }}</td>
                                        <td>${{ artwork.loanPrice }}</td>
                                        <td>
                                            <!-- Badge indicating whether artwork is loanable -->
                                            <span v-if="!artwork.loanable">
                                                <b-badge pill variant="danger">No</b-badge>
                                            </span>
                                            <span v-else>
                                                <b-badge pill variant="success">Yes</b-badge>
                                            </span>
                                        </td>
                                        <td>
                                            <span v-if="!(userType==='V')">
                                                ID: {{ artwork.room.roomId}}<br/>
                                            </span>
                                           
                                            <b-badge variant="outline-dark">{{artwork.room.roomType}}</b-badge>
                                        </td> 
                                        <td v-if="!(userType==='V')">
                                            <b-button class="deletebutton mb-3" pill size="xs" v-on:click="openMoveModal(artwork)" variant="outline-primary">
                                                Move
                                            </b-button> 
                                            <b-button class="deletebutton mb-3" pill size="xs" v-on:click="openUpdateModal(artwork)" variant="outline-success">
                                                Update Loanable
                                            </b-button>                      
                                            <b-button class="deletebutton mb-3" pill size="xs" v-on:click="deleteArtwork(artwork.artworkID)" variant="outline-danger">
                                                Delete
                                            </b-button> 
                                        </td>
                                        <td v-else>
                                            <b-button class="deletebutton" v-bind:disabled="(artwork.loanable===false)" variant="outline-primary" v-on:click="setArtworkToBeLoaned(artwork)">Request to Loan</b-button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            <span v-if="(artworks.length===0)" align="center">
                                There are no artworks to display.
                            </span>
                             </b-card-body>
                             <div align="right">
                                <span v-if="!(userType==='V')" >
                                    <button class="btn btn-outline-primary" v-b-modal.CreateArtworkButton>Add</button>
                                </span>
                             </div>
                            
                        </div>
                    </b-card>
                    </div>
                    <span v-if="!(userType==='V')">
                    <div class="row">
                        <b-card style="width: 100%;">
                            <b-card-header><h2>Museum Rooms</h2></b-card-header>
                            <div class="listOfRooms">
                                <table class="table table-image">
                                    <thead>
                                        <th>Room ID</th>
                                        <th>Room Size</th>
                                        <th>Room Type</th>
                                    </thead>
                                    <tbody>
                                        <tr v-for="room in rooms">
                                            <td>{{ room.roomId }}</td>
                                            <td>
                                                <span v-if="(room.roomType==='Storage')">
                                                    N/A
                                                </span>
                                                <span v-else>
                                                    {{room.roomSize}}
                                                </span>
                                            </td>
                                            <td>{{ room.roomType }}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </b-card>
                    </div>
                    </span>
                </div>
                    <!-- Create Modal -->
                    <b-modal hide-footer v-model="createModal" id="CreateArtworkButton" title="Add Artwork" >
                      <b-form id="CreateArtworkForm">
                        <b-form-input
                            id="Name"
                            v-model="newArtworkName"
                            placeholder="Title"
                            class="mb-2"
                        ></b-form-input><br/>
                        <b-form-input
                            id="newArtist"
                            v-model="newArtist"
                            placeholder="Artist"
                            class="mb-2"
                        ></b-form-input><br/>
                        <b-form-input
                            id="newDescription"
                            v-model="newDescription"
                            class="mb-2"
                            placeholder="Description"
                        ></b-form-input><br/>
                        <b-form-input
                            id="newYear"
                            v-model="newYear"
                            type="number"
                            class="mb-2"
                            placeholder="Year"
                        ></b-form-input><br/>
                        <b-form-input
                            id="LoanPrice"
                            type="number"
                            v-model="newLoanPrice"
                            class="mb-2"
                            placeholder="Loan Price per Day"
                        ></b-form-input><br/>
                        <b-form-input
                            id="Loanable"
                            v-model="newLoanable"
                            class="mb-2"
                            placeholder="Can be loaned (enter true or false)"
                        ></b-form-input> <br/>
                        <b-form-input
                            id="newImageUrl"
                            v-model="newImageUrl"
                            class="mb-2"
                            placeholder="Image URL"
                        ></b-form-input><br/>
                        <b-button v-bind:disabled="(!newLoanPrice || !newArtworkName || !newLoanable || !newArtworkName || !newArtist || !newDescription || !newYear || !newImageUrl)" v-on:click="createArtwork()" type="submit" variant="primary">Submit</b-button>
                      </b-form>
                      <div class="mt-1">
                        <span v-if="errorCreate" style="color:red">Error: {{errorCreate}} </span>
                    </div>
                    </b-modal>
                    <!-- Update Loanability Modal -->
                    <b-modal hide-footer scrollable v-model="updateModal" title="Update Artwork Loanability">
                        <div class="mb-2">
                            Artwork: {{selectedArtwork.artworkID}}<br/>
                        </div>
                        <b-form id="UpdateArtworkForm">
                            <b-form-group label="Select an option:">
                                <b-form-radio v-model="updatedLoanable" name="loanable" value="true">Yes</b-form-radio>
                                <b-form-radio v-model="updatedLoanable" name="loanable" value="false">No</b-form-radio>
                            </b-form-group>
                        <b-button v-on:click="updateArtwork()" variant="primary">Submit</b-button>
                      </b-form>
                      <div class="mt-1">
                        <span v-if="errorUpdate" style="color:red">Error: {{this.errorUpdate}} </span>
                    </div>
                    </b-modal>   
                    <!-- Modal for moving artwork -->
                    <b-modal hide-footer v-model="moveModal" title="Move artwork">
                        <div class="mb-2">
                            Artwork: {{selectedArtwork.artworkID}}<br/>
                            Current Location (Room ID): {{selectedArtwork.room.roomId}}<br/>
                        </div>
                        <div>
                            <div class="dropdown mb-3">
                                <button class="btn btn-outline-secondary dropdown-toggle" @click="toggleDropdownM">
                                    {{dropdownTextM}}
                                </button>
                                <ul v-if="dropdownM" class="dropdown-list mt-1">
                                    <!-- add current capacity to list items -->
                                    <li v-for="room in this.rooms" :key="room.roomId" @click="selectRoom(room)">
                                        ID: {{ room.roomId }}, Type: {{room.roomType}}
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div align="right" >
                            <b-button v-bind:disabled="!selectedRoom " v-on:click="addArtworkToRoom()" variant="primary">Submit</b-button>
                        </div>
                        <div class="mt-1">
                            <span v-if="errorMove" style="color:red">Error: {{errorMove}} </span>
                        </div>
                    </b-modal>    
                    <!-- Loan Request Modal -->
                    <b-modal
                    v-model="loanRequestModal"
                    title="Create Loan Request"
                    hide-footer id="RequestForLoan"
                    centered
                    >
                        <div>
                            <b-form-group>
                                <b-form-datepicker
                                id="startDate-input"
                                placeholder="Select a start date"
                                v-model="startDate"
                                ></b-form-datepicker>
                                <b-form-datepicker
                                class="mt-2"
                                id="endDate-input"
                                placeholder="Select an end date"
                                v-model="endDate"
                                ></b-form-datepicker>
                            </b-form-group>
                        </div>
                        <div>
                            <b-button v-bind:disabled="((!endDate)||(!startDate))" v-on:click="createLoanRequest()" variant="primary">Submit Loan Request</b-button>
                        </div>
                        <div class="mt-1">
                            <span v-if="errorLoan" style="color:red">Error: {{errorLoan}} </span>
                        </div>
                    </b-modal>
                </div>    
        </div>
    </div>
            
</template>

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
    padding: 3px 6px;
    font-size: 14px;
}

</style>

<script>
import MainNav from './MainNav.vue'
import axios from 'axios'
var config = require('../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
    name: 'ArtworkLayout',
    data() {
        return {
            artworks: [],
            rooms: [],
            errorArtwork: '',
            errorRoom: '',

            createModal: false,
            newLoanPrice: '',
            newLoanable: '',
            newArtworkName: '',
            newArtist: '',
            newDescription: '',
            newYear: '',
            newImageUrl: '',
            errorCreate: '',

            selectedArtwork: {
                artworkID: 0,
                artworkName: '',
                artist: '',
                loanPrice: '',
                description: '',
                year: '',
                imageUrl: '',
                loanable: false,
                room: {
                    roomId: 0,
                    roomType: '',
                    roomSize: ''
                }
            },
            selectedRoom: {
                roomId: 0,
                roomType: '',
                roomSize: ''
            },
            moveModal: false,
            dropdownM: false,
            dropdownTextM: 'Select room',
            updateModal: false,
            updatedLoanable: 'true',
            errorMove: '',
            errorUpdate: '',

            loanRequestModal: false,
            startDate: '',
            endDate: '',
            errorLoan: '',
            
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
        AXIOS.get('/artworks').then(response => {
            this.artworks = response.data;
        })
        .catch(e => {
            console.log(e.response.data)
        })
            
        AXIOS.get('/rooms').then(response => {
            this.rooms = response.data;

        })
        .catch(e => {
            console.log(e.response.data)

        }) 
    },
    methods: {
        createArtwork: function () {
            AXIOS.post('/artwork/', {
                loanPrice: this.newLoanPrice,
                loanable: this.newLoanable,
                name: this.newArtworkName,
                artist: this.newArtist,
                description: this.newDescription,
                year: this.newYear,
                imgUrl: this.newImageUrl,
                roomDto: {
                    RoomSize: null,
                    RoomType:null,
                }
            }, {})
            .then(response => {
                this.artworks.push(response.data);
                this.createModal = false
                this.newLoanPrice= ''
                this.newLoanable= ''
                this.newArtworkName= ''
                this.newArtist= ''
                this.newDescription= ''
                this.newYear= ''
                this.newImageUrl= ''
            })
            .catch(e => {
                this.errorCreate = e.response.data;
                console.log(errorMsg);
            })
        },
        updateArtwork: function () {
            const artwork = {
                loanPrice: this.selectedArtwork.loanPrice,
                name: this.selectedArtwork.name,
                artist: this.selectedArtwork.artist,
                description: this.selectedArtwork.description,
                year: this.selectedArtwork.year,
                imgUrl: this.selectedArtwork.imgUrl,
            }
            AXIOS.post('/artwork/' + this.selectedArtwork.artworkID + '/update', {
                loanPrice: artwork.loanPrice,
                loanable: this.updatedLoanable,
                name: artwork.name,
                artist: artwork.artist,
                description: artwork.description,
                year: artwork.year,
                imgUrl: artwork.imgUrl,
                roomDto: null
            }, {})
            .then((response) => {
                for (let index = 0; index < this.artworks.length; index++) {
                    const element = this.artworks[index];
                    if(element.artworkID === this.selectedArtwork.artworkID) {
                        this.artworks[index] = response.data
                    }
                }
                this.updateModal = false
                this.updatedLoanable= ''
            })
            .catch(e => {
                this.errorUpdate = e.response.data;
            })
        },
        deleteArtwork: function (artworkID) {
            AXIOS.delete('/artwork/'+artworkID)
            .then(response => 
            {
                for (let index = 0; index < this.artworks.length; index++) {
                    const element = this.artworks[index];
                    if(element.artworkID === artworkID) {
                        this.artworks.splice(index, 1)
                    }
                }
                console.log(response)
            })
            .catch(e => 
            {
                var errorMsg = e.response.data;
                console.log(errorMsg);
                alert(errorMsg)
                this.errorArtwork = errorMsg;
            })
        },
        addArtworkToRoom: function () {
            AXIOS.post('/artwork/' + this.selectedArtwork.artworkID + '/room/add', 
            {
                roomId: this.selectedRoom.roomId,
                roomSize: this.selectedRoom.roomSize,
                roomType: this.selectedRoom.roomType
            }, {})
            .then((response) => {
                this.moveModal = false
                for (let index = 0; index < this.artworks.length; index++) {
                    const element = this.artworks[index];
                    if(element.artworkID === this.selectedArtwork.artworkID) {
                        element.room = this.selectedRoom
                    }
                }
                this.selectedRoom = {}
            })
            .catch(e => {
                this.errorMove = e.response.data;
            })
            
        },
        openUpdateModal: function(artwork) {
            this.selectedArtwork = artwork
            this.updateModal = true
        },
        openMoveModal: function(artwork) {
            this.selectedArtwork = artwork
            this.moveModal = true
        },
        selectRoom: function(room) {
            this.selectedRoom = room
            this.dropdownTextM = 'ID: '+room.roomId+' Type: '+room.roomType
        },
        toggleDropdownM: function() {
            this.dropdownM = !this.dropdownM;
        },
        setArtworkToBeLoaned : function(artwork) {
         this.loanRequestArtwork = artwork;
         console.log(this.loanRequestArtwork)
         this.loanRequestModal = true;
        },
        createLoanRequest : function() {
            console.log(this.startDate)
            console.log(this.endDate)
            AXIOS.post('/loanRequest', {
            requestedEndDate: this.endDate,
            requestedStartDate: this.startDate,
            requester: {
                username: this.$store.getters.getCurrentUsername,
                password: this.$store.getters.getCurrentPassword
            },
            artwork : {
                    artworkID: this.loanRequestArtwork.artworkID,
                    loanPrice: this.loanRequestArtwork.loanPrice,
                    loanable: this.loanRequestArtwork.loanable,
                    name: this.loanRequestArtwork.name,
                    artist: this.loanRequestArtwork.artist,
                    description: this.loanRequestArtwork.description,
                    year: this.loanRequestArtwork.year,
                    imgUrl: this.loanRequestArtwork.imgUrl,
                    room: {
                        roomId: this.loanRequestArtwork.room.roomId,
                        roomSize: this.loanRequestArtwork.room.RoomSize,
                        roomType: this.loanRequestArtwork.room.RoomType
                    }
            },
            },
            {}).
            then(response => {
                console.log(response);
                console.log(this.loanRequestArtwork)
                this.loanRequestModal = false;
                this.startDate = '';
                this.endDate = '';
            })
            .catch(e => {
                this.errorLoan = e.response.data
                console.log(e)
            })
        }
    } 
}
</script>