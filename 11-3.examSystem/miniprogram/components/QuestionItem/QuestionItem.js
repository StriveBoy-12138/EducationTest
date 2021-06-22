// components/QuestionItem/QuestionItem.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    examIndex: Number,
    question: {
      type: Object
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    questionTypeMap: {
      0: '单选题',
      1: '多选题',
      2: '填空题',
      3: '判断题',
      4: '简答题'
    },
    radio: '',
    checkbox: [],
    content: '',
    answer: ''
  },

  lifetimes: {
    attached: function () {
      // 在组件实例进入页面节点树时执行
      console.log(this.properties.question)
    },
    detached: function () {
      // 在组件实例被从页面节点树移除时执行
    },
  },

  /**
   * 组件的方法列表
   */
  methods: {
    onChangeRadio(e) {
      this.setData({
        radio: e.detail,
        answer: e.detail
      })
      this.emitAnswer()
    },
    onChangeCheckbox(e) {
      this.setData({
        checkbox: e.detail,
        answer: e.detail
      })
      this.emitAnswer()
    },
    getContent(e) {
      console.log(e);
      this.setData({
        content: e.detail.value,
        answer: e.detail
      })
      this.emitAnswer()
    },
    emitAnswer() {
      let answer = {
        answer: this.data.answer
      }
      this.triggerEvent('emitAnswer', answer)
    }
  }
})