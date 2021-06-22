import Vue from 'vue'
import Vuex from 'vuex'

import sys from './module/sys'
import app from './module/app'
import iView from 'view-design'
import createPersistedState from 'vuex-persistedstate'
import config from '@/config'

Vue.use(Vuex)
Vue.use(iView)
export default new Vuex.Store({
  state: {
    //
  },
  mutations: {
    //
  },
  actions: {
    //
  },
  modules: {
    sys,
    app
  },
  // vuex 数据持久化
  plugins: [createPersistedState({
    key: config.projectId + 'Vuex',
    storage: window.sessionStorage
  })]
})
