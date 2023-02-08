import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    currentUsername: '',
    currentPassword: '',
    currentUserType: ''
  },
  getters: {
    getCurrentUsername(state) {
      return state.currentUsername
    },
    getCurrentPassword(state) {
      return state.currentPassword
    },
    getCurrentUserType(state) {
      return state.currentUserType
    }
  },
  mutations: {
    initializeStore(state) {
      var username = localStorage.getItem('currentUsername')
      if(username) {
        state.currentUsername = username
        var password = localStorage.getItem('currentPassword')
        state.currentPassword = password
        var type = localStorage.getItem('currentUserType')
        state.currentUserType = type
      }
    },
    setCurrentUsername(state, payload) {
      localStorage.setItem('currentUsername', payload)
      state.currentUsername = payload
    },
    setCurrentPassword(state, payload) {
      localStorage.setItem('currentPassword', payload)
      state.currentPassword = payload
    },
    setCurrentUserType(state, payload) {
      localStorage.setItem('currentUserType', payload)
      state.currentUserType = payload
    }
  },
  actions: {
    setCurrentUsername({commit}, payload) {
      commit('setCurrentUsername', payload)
    },
    setCurrentPassword({commit}, payload) {
      commit('setCurrentPassword', payload)
    },
    setCurrentUserType({commit}, payload) {
      commit('setCurrentUserType', payload)
    },
    logout() {
      localStorage.removeItem('currentUserType')
      localStorage.removeItem('currentUsername')
      localStorage.removeItem('currentPassword')
      this.state.currentPassword = ''
      this.state.currentUsername = ''
      this.state.currentUserType = ''
    }
  },
  modules: {
  }
})

