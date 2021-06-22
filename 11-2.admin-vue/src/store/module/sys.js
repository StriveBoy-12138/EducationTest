import {
  login,
  logout,
  getUserInfo,
  changePassWord
} from '@/api/module/sys'
import { setToken, getToken } from '@/libs/util'

export default {
  state: {
    userName: '',
    userId: '',
    account: '',
    avatarImgPath: '',
    token: getToken(),
    access: '',
    hasGetInfo: false,
    unreadCount: 0,
    messageUnreadList: [],
    messageReadedList: [],
    messageTrashList: [],
    messageContentStore: {}
  },
  mutations: {
    setAvatar (state, avatarPath) {
      state.avatarImgPath = avatarPath
    },
    setUserId (state, id) {
      state.userId = id
    },
    setUserName (state, name) {
      state.userName = name
    },
    setAccess (state, access) {
      state.access = access
    },
    setToken (state, token) {
      state.token = token
      setToken(token)
    },
    setAccount (state, account) {
      state.account = account
    },
    setHasGetInfo (state, status) {
      state.hasGetInfo = status
    }
  },
  actions: {
    // 登录
    handleLogin ({ commit }, { account, password }) {
      account = account.trim()
      return new Promise((resolve, reject) => {
        login({
          account,
          password
        }).then(res => {
          const data = res.data
          if (data.code === 0) {
            commit('setToken', data.data.token)
          }
          resolve(data)
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 主动退出登录
    handleLogOut ({ state, commit }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('setToken', '')
          commit('setAccess', [])
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 强制退出
    forceLogOut ({ commit }) {
      return new Promise((resolve, reject) => {
        commit('setToken', '')
        commit('setAccess', [])
        resolve()
      })
    },
    // 获取用户相关信息
    getUserInfo ({ state, commit }) {
      return new Promise((resolve, reject) => {
        try {
          getUserInfo(state.userId).then(res => {
            const data = res.data
            if (data.code === 0) {
              commit('setAvatar', data.data.avatar)
              commit('setUserName', data.data.nickname)
              commit('setUserId', data.data.pkId)
              commit('setAccount', data.data.account)
              commit('setAccess', data.data.access)
              commit('setHasGetInfo', true)
            }
            resolve(data)
          }).catch(err => {
            reject(err)
          })
        } catch (error) {
          reject(error)
        }
      })
    },
    handleChangePassWord ({ state, commit }, { account, password, newPassword }) {
      return new Promise((resolve, reject) => {
        try {
          changePassWord({ account, password, newPassword }).then(res => {
            resolve(res.data)
          }).catch(err => {
            reject(err)
          })
        } catch (error) {
          reject(error)
        }
      })
    }
  }
}
