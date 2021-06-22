// miniprogram/pages/account/account.js
import Notify from '../../miniprogram_npm/@vant/weapp/notify/notify';
const $api = require('../../utils/request.js').request
let avatar = ''

Page({

  /**
   * 页面的初始数据
   */
  data: {
    nickname: '',
    account: '',
    password: '',
    avatar: '../../images/logo.png',
    hasAccount: true
  },

  /**
   * 自定义函数区域
   */
  toRegister() {
    this.setData({
      hasAccount: false,
      nickname: '',
      account: '',
      password: ''
    })
    wx.setNavigationBarTitle({
      title: '请您注册'
    })
  },

  toLogin() {
    this.setData({
      hasAccount: true,
      nickname: '',
      account: '',
      password: ''
    })
    wx.setNavigationBarTitle({
      title: '请您登录'
    })
  },

  login() {
    if (this.data.account.trim() === '') {
      Notify({
        type: 'danger',
        message: '账号不能为空'
      })
    } else if (this.data.password.trim() === '') {
      Notify({
        type: 'danger',
        message: '密码不能为空'
      })
    } else {
      let account = this.data.account.trim()
      let password = this.data.password.trim()
      $api('POST', '/user/login', {
        account,
        password
      }).then(res => {
        if (res.data.code === 0) {
          wx.setStorageSync('token', res.data.data.token)
          wx.switchTab({
            url: '../../pages/index/index',
          })
        } else {
          Notify({
            type: 'danger',
            message: res.data.msg
          })
        }
      }).catch(() => {
        Notify({
          type: 'danger',
          message: '网络故障'
        })
      })
    }
  },

  register() {
    if (this.data.nickname.trim() === '') {
      Notify({
        type: 'danger',
        message: '姓名不能为空'
      })
    } else if (this.data.account.trim() === '') {
      Notify({
        type: 'danger',
        message: '账号不能为空'
      })
    } else if (this.data.password.trim() === '') {
      Notify({
        type: 'danger',
        message: '密码不能为空'
      })
    } else {
      let nickname = this.data.nickname.trim()
      let account = this.data.account.trim()
      let password = this.data.password.trim()
      let organizationId = 1
      let roleId = 2
      $api('POST', '/user/register', {
        avatar,
        nickname,
        account,
        password,
        organizationId,
        roleId
      }).then(res => {
        if (res.data.code != 0) {
          Notify({
            type: 'danger',
            message: res.data.msg
          })
        } else {
          this.login()
        }
      }).catch(() => {
        Notify({
          type: 'danger',
          message: '网络故障'
        })
      })
    }
  },

  // 上传头像相关的
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
    wx.setNavigationBarTitle({
      title: '请您登录'
    })
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