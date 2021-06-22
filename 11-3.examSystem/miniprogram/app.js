//app.js
const $api = require('./utils/request.js').request

App({
  async checkLoginStatus() {
    await $api('GET', '/user/getCurrentInfo', null).then(res => {
      if (res.data.code != 0) {
        wx.setStorageSync('token', '')
      } else {
        let {
          pkId,
          nickname,
          avatar
        } = res.data.data
        wx.setStorageSync('pkId', pkId)
        wx.setStorageSync('nickname', nickname)
        wx.setStorageSync('avatar', avatar)
        wx.setStorageSync('info', res.data.data)
      }
    }).catch(error => {
      wx.setStorageSync('token', '')
      console.log(error)
    })
  },

  onLaunch: function () {
    // if (!wx.cloud) {
    //   console.error('请使用 2.2.3 或以上的基础库以使用云能力')
    // } else {
    //   wx.cloud.init({
    //     traceUser: true,
    //   })
    // }

    this.checkLoginStatus()

    this.globalData = {}
  }
})