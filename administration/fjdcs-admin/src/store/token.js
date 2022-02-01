
import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate";
Vue.use(Vuex)
export default new Vuex.Store({
  state: { token: null, domain: null, name:null, username: null, thumbnailPhoto: null, userlevel: null, dn: null },
  mutations: {
    LOGIN_SUCCESS(state, response) {
        state.token = response.token
        state.domain = response.domain
    },
    TOKEN_GET(state, response) {
        state.name = response.cn
        state.thumbnailPhoto = response.thumbnailPhoto
        state.username = response.userPrincipalName
        state.dn = response.dn
    },
    USER_LEVEL(state, response) {
        state.userlevel = response
    },
    LOGOUT(state) {
        state.name = null
        state.username = null
        state.token = null
        state.domain = null
    }
},plugins: [createPersistedState()]
}
)