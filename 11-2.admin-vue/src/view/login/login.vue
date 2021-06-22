<style lang="less">
  @import './login.less';
</style>

<template>
  <div class="login">
    <div class="login-con">
      <Card icon="log-in" :title="title" :bordered="false">
        <div class="form-con">
          <login-form @on-success-valid="handleSubmit" :loading="loading"></login-form>
        </div>
      </Card>
    </div>
  </div>
</template>

<script>
import LoginForm from '_c/login-form'
import { mapActions } from 'vuex'
import config from '@/config'
export default {
  data () {
    return {
      title: '欢迎登录-' + config.title(),
      loading: false
    }
  },
  components: {
    LoginForm
  },
  methods: {
    ...mapActions([
      'handleLogin',
      'getUserInfo'
    ]),
    handleSubmit ({ account, password }) {
      this.loading = true
      this.handleLogin({ account, password }).then(res => {
        if (res.code === 0) {
          this.getUserInfo().then(resData => {
            this.loading = false
            if (resData.code === 0) {
              this.$router.push({
                name: this.$config.homeName
              })
            } else {
              this.$Message.error(resData.msg)
            }
          }).catch((err) => {
            this.loading = false
            this.$Message.error(err)
          })
        } else {
          this.loading = false
          this.$Message.error(res.msg)
        }
      }).catch(() => {
        this.loading = false
        this.$Message.error('网络异常，与服务器通信失败')
      })
    }
  }
}
</script>

<style>

</style>
