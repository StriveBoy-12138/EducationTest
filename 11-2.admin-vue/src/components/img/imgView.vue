<template>
    <div>
        <viewer>
            <img v-for="(item, index) in imgList" :src="urlHead + item" :key="index" style="width:40px;height: 40px;cursor:pointer">
        </viewer>
    </div>
</template>

<script>
export default {
  components: {},
  props: {
    imgList: Array
  },
  data () {
    return {
      urlHead: 'https://weibaokangfu.oss-cn-hangzhou.aliyuncs.com/'
    }
  },
  computed: {},
  methods: {
  },
  created () {},
  mounted () {}
}
</script>
<style scoped>
</style>
