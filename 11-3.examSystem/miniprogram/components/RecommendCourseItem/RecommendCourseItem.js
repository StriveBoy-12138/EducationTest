// components/RecommendCourseItem/RecommendCourseItem.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    info: Object
  },

  /**
   * 组件的初始数据
   */
  data: {
    infoSrc: 'http://edu-image.nosdn.127.net/EB7E7C37AB8C4A0DFDB0C4BAF154620A.jpg?imageView&thumbnail=426y240&quality=100',
    infoName: '计算机网络',
    infoContent: '南京邮电大学',
    infoTime: '进行至第1周'
  },

  observers: {
    info(value) {
      this.setData({
        infoSrc: 'http://miaoruixiang.oss-cn-hangzhou.aliyuncs.com/' + value.picture,
        infoName: value.title,
        infoContent: value.info,
        infoTime: value.createTime
      })
    }
  },

  /**
   * 组件的方法列表
   */
  methods: {

  }
})