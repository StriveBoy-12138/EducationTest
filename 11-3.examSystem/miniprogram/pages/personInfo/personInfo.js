// miniprogram/pages/personInfo/personInfo.js
const $api = require('../../utils/request.js').request
let avatar = ''
let nickname = ''
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    avatar: '',
    nickname
  },
  /**
   * 自定义函数区域
   */
  async updateInfo() {
    wx.showLoading({
      title: '加载中...',
    })
    let nickname = this.data.nickname
    let info = wx.getStorageSync('info')
    let pkId = info.pkId
    let otherInfo = "{}"
    await $api('POST', '/user/update', {
      pkId,
      avatar,
      nickname,
      otherInfo
    }).then(() => {
      wx.hideLoading()
      wx.showToast({
        title: '已更新',
      })
    })
    await app.checkLoginStatus()
    wx.navigateBack({
      delta: 0,
    })
  },

  //添加上传图片
  chooseImageTap() {
    wx.showActionSheet({
      itemList: ['从相册中选择', '拍照'],
      itemColor: "#00000",
      success: res => {
        if (!res.cancel) {
          if (res.tapIndex == 0) {
            this.chooseWxImage('album')
          } else if (res.tapIndex == 1) {
            this.chooseWxImage('camera')
          }
        }
      }
    })
  },

  // 图片本地路径
  chooseWxImage: function (type) {
    wx.chooseImage({
      sizeType: ['original', 'compressed'],
      sourceType: [type],
      success: res => {
        console.log(res.tempFilePaths[0]);
        this.upImgs(res.tempFilePaths[0]) //调用上传方法
      }
    })
  },

  async upImgs(filePath) {
    console.log(filePath)
    wx.showLoading({
      title: 'loading...',
    })
    wx.uploadFile({
      url: 'http://49.234.31.118/examination-admin-api/oss/upload', // 开发者服务器的URL。
      filePath,
      name: 'file', // 必须填file。
      formData: {
        file: filePath
      },
      success: res => {
        console.log(JSON.parse(res.data))
        wx.hideLoading()
        let data = JSON.parse(res.data)
        if (data.code === 0) {
          this.setData({
            avatar: 'http://miaoruixiang.oss-cn-hangzhou.aliyuncs.com/' + data.data.file
          })
          avatar = data.data.file
          wx.showToast({
            title: '上传成功',
          })
        } else {
          wx.showToast({
            title: '出错了',
            icon: 'none'
          })
        }
      },
      fail: err => {
        console.log(err)
        wx.hideLoading()
      },
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    avatar = 'http://miaoruixiang.oss-cn-hangzhou.aliyuncs.com/' + wx.getStorageSync('avatar')
    nickname = wx.getStorageSync('nickname')
    if (avatar != '') {
      this.setData({
        avatar,
        nickname
      })
    } else {
      this.setData({
        nickname
      })
    }
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