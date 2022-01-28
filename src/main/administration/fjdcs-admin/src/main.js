import Vue from 'vue'
import App from './App.vue'
import Vuex from 'vuex'
import VueRouter from 'vue-router'
import VueProgressBar from 'vue-progressbar'
import '@mdi/font/css/materialdesignicons.css'
import './assets/general.sass'
import Vuesax from 'vuesax'
import 'animate.css';
import store from './store/token.js'
import globals from './globals.json'
import _get from 'lodash/get'
import 'boxicons';
import 'vuesax/dist/vuesax.css' 
Vue.use(Vuesax, {
   colors: {
      primary:'#4a83ed',
      success:'rgb(23, 201, 100)',
      danger:'rgb(242, 19, 93)',
      warning:'rgb(255, 130, 0)',
      dark:'rgb(36, 33, 69)'
    }
})
Vue.prototype.$g = (key) => {
  let val = _get(globals, key, '')
  if (!val) console.warn(key, ' is empty in $g')
  return val || key
}
Vue.use(Vuex)
Vue.use(VueRouter)
import router from './router'
Vue.config.productionTip = false
Vue.use(VueProgressBar, {
  color: '#000',
  failedColor: 'red',
  height: '2px'
})
new Vue({
  router,store, 
  render: h => h(App),
}).$mount('#app')
