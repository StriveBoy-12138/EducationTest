// components/ExamItem/ExamItem.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    exam: Object
  },

  /**
   * 组件的初始数据
   */
  data: {
    courseName: "",
    examName: "",
    finishTime: "",
    status: 0,
    userScore: 0
  },

  observers: {
    exam(value) {
      this.setData({
        courseName: value.classTitle,
        examName: value.paperTitle,
        finishTime: value.finishTime,
        status: value.status,
        userScore: value.userScore
      })
    }
  },

  /**
   * 组件的方法列表
   */
  methods: {

  }
})