// miniprogram/pages/courseWare/courseWare.js
let type = -1
let content = ''
Page({

  /**
   * 页面的初始数据
   */
  data: {
    type: -1,
    content: ''
  },

  /**
   * 自定义函数区域
   */
  /**
   * 下载文件并预览
   */
  openDoc(url) {
    wx.downloadFile({
      url,
      header: {},
      success: res => {
        let filePath = res.tempFilePath;
        console.log(filePath);
        wx.openDocument({
          filePath: filePath,
          success: () => {
            console.log('打开文档成功')
          },
          fail: res => {
            console.log(res);
          },
          complete: res => {
            console.log(res);
          }
        })
      },
      fail: () => {
        console.log('文件下载失败');
      },
      complete: () => {},
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    type = parseInt(options.type)
    content = wx.getStorageSync('content')
    if (type != 5 && type != 0) {
      content = 'http://miaoruixiang.oss-cn-hangzhou.aliyuncs.com/' + content
    }
    this.setData({
      type,
      content
    })
    if (type === 1) {
      this.openDoc(content)
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