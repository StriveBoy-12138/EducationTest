<template>
  <div class="user-avatar-dropdown">
    <Dropdown @on-click="handleClick">
      <Avatar :src="userAvatar" />
      <Icon :size="18" type="md-arrow-dropdown"></Icon>
      <DropdownMenu slot="list">
        <DropdownItem name="changePassWord">修改密码</DropdownItem>
        <DropdownItem name="logout">退出登录</DropdownItem>
      </DropdownMenu>
    </Dropdown>
    <Modal v-model="editPasswordModal" :closable='false' :mask-closable=false :width="500">
      <h3 slot="header" style="color:#2D8CF0">修改密码</h3>
      <Form ref="editPasswordForm" :model="editPasswordForm" :label-width="100" label-position="right" :rules="passwordValidate">
          <FormItem label="原密码" prop="oldPass" :error="oldPassError">
              <Input v-model="editPasswordForm.oldPass" type="password" placeholder="请输入现在使用的密码" ></Input>
          </FormItem>
          <FormItem label="新密码" prop="newPass">
              <Input v-model="editPasswordForm.newPass" type="password" placeholder="请输入新密码，至少6位字符" ></Input>
          </FormItem>
          <FormItem label="确认新密码" prop="rePass">
              <Input v-model="editPasswordForm.rePass"  type="password" placeholder="请再次输入新密码" ></Input>
          </FormItem>
      </Form>
      <div slot="footer">
          <Button type="text" @click="cancelEditPass">取消</Button>
          <Button type="primary" :loading="savePassLoading" @click="saveEditPass">保存</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import './admin.less'
import {
  mapActions
} from 'vuex'
export default {
  name: 'Admin',
  props: {
    userAvatar: {
      type: String,
      default: ''
    }

  },
  data () {
    const valideRePassword = (rule, value, callback) => {
      if (value !== this.editPasswordForm.newPass) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      editPasswordModal: false, // 修改密码模态框显示
      savePassLoading: false,
      oldPassError: '',
      editPasswordForm: {
        oldPass: '',
        newPass: '',
        rePass: ''
      },
      passwordValidate: {
        oldPass: [
          { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        newPass: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '请至少输入6个字符', trigger: 'blur' },
          { max: 32, message: '最多输入32个字符', trigger: 'blur' }
        ],
        rePass: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { validator: valideRePassword, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    ...mapActions([
      'handleLogOut',
      'handleChangePassWord'
    ]),
    logout () {
      this.handleLogOut().then(() => {
        this.$router.push({
          name: 'login'
        })
      })
    },
    saveEditPass () {
      let vm = this
      this.savePassLoading = true
      let passwordParams = {
        'account': this.$store.state.sys.account,
        'password': this.editPasswordForm.oldPass,
        'newPassword': this.editPasswordForm.newPass
      }
      this.handleChangePassWord(passwordParams).then((res) => {
        if (res.code === 0) {
          vm.$Modal.info({
            content: '修改成功，请重新登录',
            onOk: () => {
              vm.logout()
            }
          })
        } else {
          vm.$Message.error(res.msg)
        }
      })
    },
    cancelEditPass () {
      this.editPasswordModal = false
    },
    handleClick (name) {
      let vm = this
      switch (name) {
        case 'logout':
          this.logout()
          break
        case 'changePassWord': {
          vm.editPasswordModal = true
          break
        }
      }
    }
  }
}
</script>
