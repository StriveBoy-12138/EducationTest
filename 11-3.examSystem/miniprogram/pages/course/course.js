// miniprogram/pages/course/course.js
const $api = require('../../utils/request.js').request

Page({

  /**
   * 页面的初始数据
   */
  data: {
    active: 0,
    noExam: false,
    courseList: [],
    examList: []
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

  toCourseDetail(e) {
    console.log(e)
    wx.setStorageSync('courseImg', 'http://miaoruixiang.oss-cn-hangzhou.aliyuncs.com/' + e.currentTarget.dataset.courseimg)
    wx.setStorageSync('courseDes', e.currentTarget.dataset.coursedes)
    wx.setStorageSync('courseName', e.currentTarget.dataset.coursename)
    wx.setStorageSync('credit', e.currentTarget.dataset.credit)
    wx.navigateTo({
      url: `../../pages/courseDetail/courseDetail?classId=${e.currentTarget.id}`,
    })
  },

  toExam(e) {
    let index = e.currentTarget.dataset.index
    let exam = this.data.examList[index]
    wx.setStorageSync('exam', exam)
    if (exam.status === 2) {
      wx.showToast({
        title: '考试结束'
      })
    } else {
      wx.navigateTo({
        url: '../../pages/exam/exam'
      })
    }
  },

  getCourseList() {
    $api('GET', '/class/list', {
      userId: wx.getStorageSync('pkId')
    }).then(res => {
      this.setData({
        courseList: res.data.data.list
      })
      console.log(res.data.data.list)
    })
  },

  getExamList() {
    $api('GET', '/examination/paper/list', {
      userId: wx.getStorageSync('pkId')
    }).then(res => {
      console.log(res);
      let examList = res.data.data.list
      examList = examList.map(item => {
        item.questions = JSON.parse(item.questions)
        return item
      })
      for (let i = 0; i < examList.length; i++) {
        examList[i].questions = examList[i].questions.map(item => {
          if (typeof item.answer === 'string') {
            item.answer = JSON.parse(item.answer)
          } else {
            item.answer = JSON.parse(JSON.stringify(item.answer))
          }
          // item.answer = JSON.parse(item.answer)
          return item
        })
      }
      this.setData({
        examList
      })
      console.log(examList);
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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
        selected: 1
      })
    }

    this.getCourseList()
    this.getExamList()
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