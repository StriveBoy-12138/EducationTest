// miniprogram/pages/exam/exam.js
const $api = require('../../utils/request.js').request

let exam = ''

let answerArr = []
let userScore = 0

Page({

  /**
   * 页面的初始数据
   */
  data: {
    courseName: "实用python程序设计",
    examName: "期末考试",
    finishTime: '',
    status: 0,
    startExam: false,
    activeIndex: 0,
    examData: [],
    hasLastOne: false,
    hasNextOne: true,
    hasQuestions: true,
    userScore: 0
  },

  /***
   * 自定义函数区域
   */
  startExam() {
    let pkId = exam.pkId
    if (this.data.status === 0) {
      $api('POST', '/examination/paper/start', {
        pkId
      }).then(res => {
        if (res.data.code === 0) {
          this.setData({
            startExam: true
          })
        } else {
          console.log(res)
          wx.showModal({
            title: '提示',
            content: res.data.msg,
            cancelColor: 'cancelColor',
            showCancel: false,
            success: () => {
              wx.navigateBack({
                delta: 0,
              })
            }
          })
        }
      }).catch(err => {
        console.log(err)
      })
    } else {
      this.setData({
        startExam: true
      })
    }
  },

  lastOne() {
    let activeIndex = this.data.activeIndex
    if (activeIndex > 0) {
      activeIndex = activeIndex - 1
      this.setData({
        activeIndex,
        hasNextOne: true
      })
      if (activeIndex === 0) {
        this.setData({
          hasLastOne: false
        })
      }
    }
  },

  nextOne() {
    let activeIndex = this.data.activeIndex
    if (activeIndex < this.data.examData.length - 1) {
      activeIndex = activeIndex + 1
      this.setData({
        activeIndex,
        hasLastOne: true
      })
      if (activeIndex === this.data.examData.length - 1) {
        this.setData({
          hasNextOne: false
        })
      }
    }
  },

  getAnswer(e) {
    let index = this.data.activeIndex
    answerArr[index] = e.detail.answer
  },

  checkAnswer() {
    userScore = 0
    for (let i = 0; i < exam.questions.length; i++) {
      if (!answerArr[i]) {
        continue
      }
      if (exam.questions[i].answer.answerId) {
        if (answerArr[i] === exam.questions[i].answer.answerId) {
          userScore = userScore + exam.questions[i].score
          exam.questions[i].userScore = exam.questions[i].score
        }
      }
      if (exam.questions[i].answer.answerIds) {
        if (answerArr[i].toString() === exam.questions[i].answer.answerIds.toString()) {
          userScore = userScore + exam.questions[i].score
          exam.questions[i].userScore = JSON.stringify(exam.questions[i].score)
        }
      }
      exam.questions[i].userAnswer = answerArr[i]
    }
    console.log(`userScore:${userScore}`)
  },

  dateFormat(date) {
    let ret
    let fmt = 'YYYY-mm-dd HH:MM:SS'
    const opt = {
      "Y+": date.getFullYear().toString(),
      "m+": (date.getMonth() + 1).toString(),
      "d+": date.getDate().toString(),
      "H+": date.getHours().toString(),
      "M+": date.getMinutes().toString(),
      "S+": date.getSeconds().toString()
    };
    for (let k in opt) {
      ret = new RegExp("(" + k + ")").exec(fmt);
      if (ret) {
        fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
      };
    };
    return fmt;
  },

  submitAnswer() {
    wx.showModal({
      content: '确认提交答案？',
      success: (res) => {
        if (res.confirm) {
          this.checkAnswer()
          let pkId = exam.pkId
          let questions = JSON.stringify(exam.questions)
          console.log(questions)
          let finishTime = this.dateFormat(new Date())
          let useTime = 10
          wx.showLoading({
            title: '提交中...',
          })
          $api('POST', '/examination/paper/submit', {
            pkId,
            questions,
            userScore,
            finishTime,
            useTime
          }).then(res => {
            console.log(res)
          }).catch(err => {
            console.log(err)
          }).finally(() => {
            setTimeout(() => {
              wx.hideLoading()
              wx.showToast({
                title: '已提交',
              })
              setTimeout(() => {
                wx.navigateBack()
              }, 1500)
            }, 1000)
          })
        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    exam = wx.getStorageSync('exam')
    let courseName = exam.classTitle
    let examName = exam.paperTitle
    let finishTime = exam.finishTime
    let status = exam.status
    this.setData({
      courseName,
      examName,
      finishTime,
      status,
      userScore: exam.userScore
    })
    console.log(exam.questions)
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
    this.setData({
      examData: exam.questions
    })
    if (exam.questions.length === 0) {
      this.setData({
        hasQuestions: false
      })
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

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})