// miniprogram/pages/infoDetail/infoDetail.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    info: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let info = wx.getStorageSync('indexInfo')
    const regex = new RegExp('<img', 'gi');
    const regexTable = new RegExp('<table', 'gi');
    info.info = info.info.replace(regex, `<img style="max-width: 100%;"`);
    info.info = info.info.replace(regexTable, `<table style="width: 100%;border:1px solid grey" border="0" cellspacing="1" cellpadding="0"`);
    this.setData({
      info
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