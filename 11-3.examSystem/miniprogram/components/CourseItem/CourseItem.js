// components/CourseItem/CourseItem.js
let regex = /(<([^>]+)>)/ig

Component({
  /**
   * 组件的属性列表
   */
  properties: {
    course: Object
  },

  /**
   * 组件的初始数据
   */
  data: {
    courseSrc: '',
    courseName: '实用Python程序设计hhhhhhhhhhhhh',
    courseType: '北京大学',
    courseSchedule: '进行至第13周，共19周',
    credit: '',
  },

  observers: {
    course(value) {
      console.log(value)
      this.setData({
        courseSrc: 'http://miaoruixiang.oss-cn-hangzhou.aliyuncs.com/' + value.picture,
        courseName: value.title,
        courseType: value.typeName,
        courseSchedule: value.des.replace(regex, ''),
        credit: value.credit,
      })
    }
  },

  /**
   * 组件的方法列表
   */
  methods: {

  }
})