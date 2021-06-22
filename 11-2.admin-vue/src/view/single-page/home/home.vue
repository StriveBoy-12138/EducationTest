<template>
  <div>
    <Card dis-hover :bordered="false">
      <Row>
        <h2>统计数据</h2>
      </Row>
      <Row justify="center" style="margin-top: 20px;padding: 10px">
        <div style="text-align: center;">
          <count-to style="color: #EC6946;font-size: 30px;font-weight: 600" :end="statistics.paperNum"/><br>
          <span style="font-size: 18px">总试卷数</span>
        </div>
        <div style="text-align: center;margin-left: 230px">
          <count-to style="color: #EC6946;font-size: 30px;font-weight: 600" :end="statistics.questionNum"/><br>
          <span style="font-size: 18px">总题目数</span>
        </div>
        <div style="text-align: center;margin-left: 230px">
          <count-to style="color: #EC6946;font-size: 30px;font-weight: 600" :end="statistics.userNum"/><br>
          <span style="font-size: 18px">总成员数</span>
        </div>
        <div style="text-align: center;margin-left: 230px">
          <count-to style="color: #EC6946;font-size: 30px;font-weight: 600" :end="statistics.classNum"/><br>
          <span style="font-size: 18px">总课程数</span>
        </div>
        <div style="text-align: center;margin-left: 230px">
          <count-to style="color: #EC6946;font-size: 30px;font-weight: 600" :end="statistics.coursewareNum"/><br>
          <span style="font-size: 18px">总课件数</span>
        </div>
      </Row>
    </Card>
    <Card dis-hover :bordered="false" style="margin-top: 20px">
      <Row>
        <h2>常用功能</h2>
      </Row>
      <Row justify="center" style="margin-top: 20px;padding: 10px">
        <Button type="text" icon="ios-folder-outline" size="large" @click="commonFunction(0)">批量导题</Button>
        <Button type="text" icon="ios-paper-outline" size="large" style="margin-left: 180px" @click="commonFunction(1)">创建试卷</Button>
        <Button type="text" icon="md-book" size="large" style="margin-left: 180px" @click="commonFunction(2)">练习管理</Button>
        <Button type="text" icon="ios-alarm-outline" size="large" style="margin-left: 180px" @click="commonFunction(3)">学习打卡</Button>
        <Button type="text" icon="ios-paper-outline" size="large" style="margin-left: 180px" @click="commonFunction(4)">报名管理</Button>
      </Row>
      <Row justify="center" style="padding: 10px">
        <Button type="text" icon="ios-cloud-upload" size="large" @click="commonFunction(5)">上传课件</Button>
        <Button type="text" icon="ios-paper-outline" size="large" style="margin-left: 180px" @click="commonFunction(6)">创建课程</Button>
        <Button type="text" icon="md-person" size="large" style="margin-left: 180px" @click="commonFunction(7)">录入学生</Button>
        <Button type="text" icon="ios-trophy-outline" size="large" style="margin-left: 180px" @click="commonFunction(8)">证书管理</Button>
        <Button type="text" icon="md-home" size="large" style="margin-left: 180px" @click="commonFunction(9)">首页管理</Button>
      </Row>
    </Card>
    <Card dis-hover :bordered="false" style="margin-top: 20px">
      <Row>
        <h2>考试情况</h2>
       <Row justify="center">
         <div ref="exam" style="width: 1600px;height: 500px;"></div>
       </Row>
      </Row>
    </Card>
  </div>
</template>

<script>
import API from '@/api'
import CountTo from '_c/count-to'
import echarts from 'echarts'

export default {
  name: 'home',
  components: {
    CountTo
  },
  data () {
    return {
      statistics: {
        paperNum: 0,
        questionNum: 0,
        userNum: 0,
        classNum: 0,
        coursewareNum: 0
      }
    }
  },
  watch: {
    $route: function (to, from) {
      if (to.name === 'home') {
        this.init()
        this.setExam()
      }
    }
  },
  methods: {
    init () {
      API.home.statistics().then(({ data }) => {
        if (data && data.code === 0) {
          this.statistics = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    commonFunction (type) {
      switch (type) {
        case 0 : this.$router.push({ name: 'questionBank' }); break
        case 1 : this.$router.push({ name: 'paper' }); break
        case 2 : console.log(type); break
        case 3 : console.log(type); break
        case 4 : console.log(type); break
        case 5 : this.$router.push({ name: 'courseware' }); break
        case 6 : this.$router.push({ name: 'class' }); break
        case 7 : this.$router.push({ name: 'user' }); break
        case 8 : console.log(type); break
        case 9 : console.log(type); break
      }
    },
    setExam () {
      let option = {
        xAxis: {
          type: 'category',
          data: ['3.5', '3.6', '3.7', '3.8', '3.9', '3.10', '3.11']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [50, 78, 60, 56, 88, 79, 99],
          type: 'line',
          smooth: true
        }]
      }
      let exam = echarts.init(this.$refs.exam)
      exam.setOption(option)
    }
  },
  mounted () {
    this.init()
    this.setExam()
  }
}
</script>
