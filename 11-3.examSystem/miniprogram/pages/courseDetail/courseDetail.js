// miniprogram/pages/courseDetail/courseDetail.js
const $api = require('../../utils/request.js').request
let regex = /(<([^>]+)>)/ig
let classId = -1

Page({

  /**
   * 页面的初始数据
   */
  data: {
    active: 0,
    courseImg: '',
    courseName: '',
    courseDes: '',
    courseGoal: '暂未上传',
    catalogList: [],
    // 学分
    credit: '2',
    activeNames: [],
  },

  /**
   * 自定义函数区域
   */
  onChangeTab(e) {
    if (e.detail.index != this.data.active) {
      this.setData({
        active: e.detail.index
      })
    }
  },

  onChangeCollapse(e) {
    this.setData({
      activeNames: e.detail,
    });
  },

  getCatalog() {
    $api('GET', '/catalog/list', {
      classId
    }).then(res => {
      this.setData({
        catalogList: res.data.data
      })
      for (let i = 0; i < this.data.catalogList.length; i++) {
        this.getCourseWare(this.data.catalogList[i].pkId, i)
      }
    })
  },

  getCourseWare(catalogId, index) {
    $api('GET', '/catalog/courseware/list', {
      catalogId
    }).then(res => {
      this.data.catalogList[index].courseWare = res.data.data
      this.setData({
        catalogList: this.data.catalogList
      })
    })
  },

  toWareDetail(e) {
    console.log(e)
    let content = e.currentTarget.dataset.content
    wx.setStorageSync('content', content)
    let type = e.currentTarget.dataset.type
    wx.navigateTo({
      url: `../../pages/courseWare/courseWare?type=${type}`,
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let courseImg = wx.getStorageSync('courseImg')
    let courseName = wx.getStorageSync('courseName')
    let courseDes = wx.getStorageSync('courseDes')
    let credit = wx.getStorageSync('credit')
    courseDes = courseDes.replace(regex, '')
    this.setData({
      courseImg,
      courseName,
      courseDes,
      credit
    })
    classId = parseInt(options.classId)
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
    wx.setNavigationBarTitle({
      title: '课程详情'
    })

    // this.getCourseWare()
    this.getCatalog()
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

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})