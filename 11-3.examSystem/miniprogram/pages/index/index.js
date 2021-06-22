// miniprogram/pages/index/index.js
let keywords = ''
const app = getApp()

const $api = require('../../utils/request.js').request
let page = 1
let pageSize = 10

Page({

  /**
   * 页面的初始数据
   */
  data: {
    swiperImgUrls: ['../../images/banner1.png', '../../images/banner2.png',
      '../../images/banner3.png', '../../images/banner4.png', '../../images/banner5.png'
    ],
    keywords: '',
    infoList: []
  },

  /**
   * 自定义函数区域
   */
  onChangeKeywords(e) {
    keywords = e.detail
  },

  async onSearchKeywords() {
    console.log(`search ${keywords}!!!`)
    await this.setData({
      keywords
    })
  },

  getInfo() {
    $api('GET', '/news/list', {
      page,
      pageSize
    }).then(res => {
      if (res.data.code === 0) {
        let infoList = res.data.data.list
        if (infoList.length === 0) {
          wx.showToast({
            title: '无更多内容',
            icon: 'none'
          })
        } else {
          page = page + 1
          this.setData({
            infoList: [...this.data.infoList, ...infoList]
          })
        }
        console.log(this.data.infoList)
      } else {
        wx.showToast({
          title: '出错了',
          icon: 'none'
        })
      }
    }).catch(() => {
      wx.showToast({
        title: '网络故障',
        icon: 'none'
      })
    })
  },

  toInfoDetail(e) {
    let index = parseInt(e.currentTarget.id)
    wx.setStorageSync('indexInfo', this.data.infoList[index])
    wx.navigateTo({
      url: '../../pages/infoDetail/infoDetail',
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getInfo()
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    if (typeof this.getTabBar === 'function' &&
      this.getTabBar()) {
      this.getTabBar().setData({
        selected: 0
      })
    }

    if (wx.getStorageSync('token') === '') {
      wx.showModal({
        title: '提示',
        content: '你暂未登录,请前往登录',
        cancelColor: 'cancelColor',
        showCancel: false,
        success: () => {
          wx.redirectTo({
            url: '../account/account',
          })
        }
      })
    } else {
      app.checkLoginStatus()
    }
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    this.getInfo()
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})