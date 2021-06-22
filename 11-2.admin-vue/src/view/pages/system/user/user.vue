<template>
  <div>
    <Card dis-hover :bordered="false">
      <Row style="margin-bottom: 10px;">
        <Col>
          <Button style="border-radius: 5px 0px 0px 5px;" type="success" icon="md-add" @click="showUserEditModal()">创建成员</Button>
          <Button style="border-radius: 0px;" icon="ios-download-outline">Excel导入</Button>
          <Button style="border-radius: 0px;">学员设置管理</Button>
          <Button style="border-radius: 0px;" @click="showInformationCollectionModal(true)">自定义信息</Button>
          <Button style="border-radius: 0px;" @click="verifyUser">审核成员</Button>
          <Button style="border-radius: 0px;" type="primary" icon="md-share">学员入口</Button>
          <Tooltip content="刷新" placement="bottom">
            <Button style="border-radius: 0px;" icon="ios-refresh" @click="init"></Button>
          </Tooltip>
          <Tooltip content="帮助中心" placement="bottom">
            <Button style="border-radius: 0px;" icon="ios-help-circle"></Button>
          </Tooltip>
          <Button style="border-radius: 0px;" type="success" icon="ios-trash-outline" @click="deleteUser()"></Button>
          <Dropdown placement="bottom-end" @on-click="functionChoose">
          <Button style="border-radius: 0px 5px 5px 0px;" icon="md-arrow-dropdown"/>
          <DropdownMenu slot="list">
            <DropdownItem :name="Number(1)"><Icon type="ios-paper-outline" style="margin-right: 5px"/>标签管理</DropdownItem>
            <DropdownItem :name="Number(2)"><Icon type="ios-add" style="margin-right: 5px"/>成员批量设置标签</DropdownItem>
            <DropdownItem :name="Number(3)"><Icon type="ios-settings-outline" style="margin-right: 5px"/>成员批量设置组织结构</DropdownItem>
            <DropdownItem :name="Number(4)"><Icon type="ios-trash-outline" style="margin-right: 5px"/>积分全部清零</DropdownItem>
            <DropdownItem :name="Number(5)"><Icon type="ios-download-outline" style="margin-right: 5px"/>批量导入人脸照片</DropdownItem>
            <DropdownItem :name="Number(6)"><Icon type="ios-cloud-upload-outline" style="margin-right: 5px"/>导出成员</DropdownItem>
            <DropdownItem :name="Number(7)"><Icon type="ios-briefcase-outline" style="margin-right: 5px"/>登录页自定义</DropdownItem>
            <DropdownItem :name="Number(8)"><Icon type="ios-briefcase-outline" style="margin-right: 5px"/>注册页自定义</DropdownItem>
          </DropdownMenu>
        </Dropdown>
        </Col>
        <Col style="margin-left: 125px">
          <Select style="border-radius: 5px 0px 0px 5px;width: 200px" v-model="title" placeholder="请选择标签">
            <Option v-for="item in titleList" :value="item.pkId" :key="item.pkId">{{ item.name }}</Option>
          </Select>
          <Select style="border-radius: 0px;width: 80px" v-model="isEnabled">
            <Option :value="Number(-1)">全部</Option>
            <Option :value="Number(0)">正常</Option>
            <Option :value="Number(1)">待审核</Option>
          </Select>
          <Dropdown trigger="click">
            <Input readonly suffix="ios-arrow-down" placeholder="请选择组织架构" style="width: 200px" v-model="checkOrganization"/>
            <DropdownMenu slot="list">
              <Tree :data="selectOrganizationList" show-checkbox style="width: 200px" @on-check-change="getCheckOrganization"></Tree>
            </DropdownMenu>
          </Dropdown>
          <Input style="border-radius: 0px;width: 100px" v-model="keywords" placeholder="账号或姓名"/>
          <ButtonGroup>
            <Button style="background-color: #F5F7FA" @click="init">搜索</Button>
            <Button style="background-color: #F5F7FA" @click="init('reset')">重置</Button>
          </ButtonGroup>
        </Col>
      </Row>
      <Row style="margin-top: 10px;">
        <Table ref="selection" :columns="columns" :data="tableData" :highlight-row="true" :current="pageIndex"
          @on-selection-change="selectionChange" :loading="loading">
          <template slot="role" slot-scope="scope">
            <span style="margin-right: 5px">{{!scope.row.roleId ? '学生':scope.row.roleName}}</span>
            <Button size="small" type="primary" @click="showSetUserRoleModal(scope.row)">修改</Button>
          </template>
          <template slot="action" slot-scope="scope">
            <Dropdown placement="bottom-end" @on-click="rowFunctionChoose" transfer>
            <Button type="info" size="small">
              操作
              <Icon type="ios-arrow-down"></Icon>
            </Button>
            <DropdownMenu slot="list">
              <DropdownItem :name="JSON.stringify({num: 1,user: scope.row})">修改</DropdownItem>
              <DropdownItem :name="JSON.stringify({num: 2,user: scope.row})">重置密码</DropdownItem>
              <DropdownItem :name="Number(3)">设置成员有效期</DropdownItem>
              <DropdownItem :name="JSON.stringify({num: 4,user: scope.row})">删除成员</DropdownItem>
              <DropdownItem :name="Number(5)">查看学员记录</DropdownItem>
              <DropdownItem :name="Number(6)">积分清零</DropdownItem>
            </DropdownMenu>
          </Dropdown>
            <Dropdown placement="bottom-end" style="margin-left: 5px" transfer>
              <Button type="info" size="small">
                子管理员设置
                <Icon type="ios-arrow-down"></Icon>
              </Button>
              <DropdownMenu slot="list">
                <DropdownItem :name="Number(1)">升级管理员</DropdownItem>
                <DropdownItem :name="Number(2)">数据权限设置</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </template>
        </Table>
      </Row>
      <Row style="margin-top: 10px" justify="end">
        <Page :total="totalCount" :current="pageIndex" :page-sise="pageSize" loading show-total show-sizer show-elevator
              @on-change="pageChange" @on-page-size-change="sizeChange">
        </Page>
    </Row>
    </Card>
    <Modal v-model="editUserModal" :closable='true' :mask-closable=false :width="600" @on-visible-change="resetUserData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">{{!editUserForm.pkId ? '创建成员':'修编辑成员'}}</h3>
      <Form ref="editUserModal" :model="editUserForm" :label-width="100" label-position="right"
        :rules="userValidate" @submit.native.prevent="saveEditUser">
        <FormItem label="帐号" prop="account">
          <Input v-model="editUserForm.account" style="width: 300px" placeholder="请输入帐号"></Input>
        </FormItem>
        <FormItem label="姓名" prop="nickname">
          <Input v-model="editUserForm.nickname" style="width: 300px" placeholder="请输入姓名"></Input>
        </FormItem>
        <FormItem label="组织架构" prop="organizationId">
          <Dropdown trigger="click">
            <Input readonly suffix="ios-arrow-down" placeholder="请选择组织架构" v-model="editUserForm.organizationName" style="width: 300px"/>
            <DropdownMenu slot="list">
              <Tree :data="userOrganizationList" show-checkbox style="width: 300px" @on-check-change="getUserOrganization"></Tree>
            </DropdownMenu>
          </Dropdown>
        </FormItem>
        <FormItem v-show="!editUserForm.pkId" label="密码" prop="password">
          <Input v-model="editUserForm.password" style="width: 300px" placeholder="请输入密码，默认密码为123456"></Input>
        </FormItem>
        <FormItem label="标签" prop="title">
          <Select style="width: 300px" v-model="editUserForm.title" placeholder="请选择标签">
            <Option v-for="item in titleList" :value="item.pkId" :key="item.pkId">{{ item.name }}</Option>
          </Select>
        </FormItem>
      </Form>
      <Form v-if="informationCollectionList.length !== 0" ref="userOtherInfo" :model="userOtherInfo" :label-width="100" label-position="right">
        <Divider>学员自定义信息</Divider>
        <FormItem :label="infoCollection.title" prop="account" v-for="(infoCollection, index) in informationCollectionList" :key="'infoCollection'+index">
          <Input v-if="infoCollection.type === 0" v-model="userOtherInfo[infoCollection.title]" style="width: 300px"></Input>
          <Select v-if="infoCollection.type === 1" style="width: 300px" v-model="userOtherInfo[infoCollection.title]">
            <Option v-for="item in JSON.parse(infoCollection.option)" :value="item" :key="item">{{ item }}</Option>
          </Select>
          <!--<Dropdown v-if="infoCollection.type === 2" trigger="click">-->
            <!--<Input readonly suffix="ios-arrow-down" placeholder="请选择组织架构" style="width: 300px"/>-->
            <!--<DropdownMenu slot="list">-->
              <!--<Tree :data="organizationList" style="width: 300px"></Tree>-->
            <!--</DropdownMenu>-->
          <!--</Dropdown>-->
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="cancelEditUser">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveEditUser">保存</Button>
      </div>
    </Modal>
    <Modal v-model="resetPasswordModal" :closable='true' :mask-closable=false :width="600" @on-visible-change="resetUserData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">重置密码</h3>
      <Form ref="resetPasswordModal" :model="editUserForm" :label-width="100" label-position="right"
            :rules="passwordValidate" @submit.native.prevent="resetPassword">
        <FormItem label="帐号" prop="account">
          <Input v-model="editUserForm.account" style="width: 300px" placeholder="请输入帐号"></Input>
        </FormItem>
        <FormItem label="姓名" prop="nickname">
          <Input v-model="editUserForm.nickname" style="width: 300px" placeholder="请输入姓名"></Input>
        </FormItem>
        <FormItem label="新密码" prop="password">
          <Input v-model="editUserForm.password" style="width: 300px" placeholder="请输入新密码"></Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="cancelEditUser">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="resetPassword">保存</Button>
      </div>
    </Modal>
    <Modal v-model="titleModal" :closable='true' :mask-closable=false :width="550">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">标签管理</h3>
      <Row>
        <Button type="primary" icon="md-add" @click="showAddTitleModal(true)">添加标签</Button>
      </Row>
      <Row style="margin-top: 8px" v-for="(title, index) in titleList" :key="'title' + index">
        <Input :ref="'titleName'+index" :value="title.name" style="width: 250px" @on-blur="saveEditTitle(index)"/>
        <Button type="primary" shape="circle" style="margin-left: 10px" icon="ios-remove" @click="deleteTitle(title.pkId)"></Button>
        <Button :disabled="index === 0" type="primary" shape="circle" style="margin-left: 10px" icon="ios-arrow-up" @click="moveTitle(title.sort, 'up')"></Button>
        <Button :disabled="(index+1) === titleList.length" type="primary" shape="circle" style="margin-left: 10px" icon="ios-arrow-down" @click="moveTitle(title.sort, 'down')"></Button>
      </Row>
      <div slot="footer">
        <Button @click="showTitleModal(false)">取消</Button>
      </div>
    </Modal>
    <Modal v-model="titleAddModal" :closable='true' :mask-closable=false :width="550" @on-visible-change="resetTitleData">
      <h3 slot="header">添加标签</h3>
      <Form ref="editTitleForm" :model="editTitleForm" :label-width="80" label-position="right"
            :rules="titleValidate" @submit.native.prevent="saveTitle">
        <FormItem label="标签名称" prop="name">
          <Input v-model="editTitleForm.name" placeholder="请输入标签名称"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="showAddTitleModal(false)">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveTitle">保存</Button>
      </div>
    </Modal>
    <Modal v-model="informationCollectionModal" :closable='true' :mask-closable=false :width="900">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">想要了解学员的什么信息？（注册后填写）</h3>
      <Row>
        <Button type="primary" @click="saveInformationCollection">添加信息项</Button>
      </Row>
      <Row style="margin-top: 8px" align="middle" v-for="(info, index) in informationCollectionList" :key="'title' + index">
        <Select v-model="info.type" style="margin-left: 15px;width: 150px" @on-change="saveEditInformationCollection(...arguments, 0, info, index)">
          <Option :value="Number(0)">文本</Option>
          <Option :value="Number(1)">下拉</Option>
          <!--<Option :value="Number(2)">组织结构</Option>-->
        </Select>
        <Input :readonly="info.type === 2" :ref="'info'+index" :value="info.title" style="margin-left: 15px;width: 150px" @on-blur="saveEditInformationCollection('', 1, info, index)"/>
        <Select v-model="info.isRequired" style="margin-left: 15px;width: 100px" @on-change="saveEditInformationCollection(...arguments, 2, info, index)">
          <Option :value="Number(1)">选填</Option>
          <Option :value="Number(0)">必填</Option>
        </Select>
        <Checkbox v-model="info.isAble" style="margin-left: 15px;" @on-change="saveEditInformationCollection(...arguments, 3, info, index)">学员是否可填写</Checkbox>
        <Button type="primary" shape="circle" style="margin-left: 10px" icon="ios-remove" @click="deleteInformationCollection(info.pkId)"></Button>
        <Button :disabled="index === 0" type="primary" shape="circle" style="margin-left: 10px" icon="ios-arrow-up" @click="moveInformationCollection(info, 'up')"></Button>
        <Button :disabled="(index+1) === informationCollectionList.length" type="primary" shape="circle" style="margin-left: 10px" icon="ios-arrow-down" @click="moveInformationCollection(info, 'down')"></Button>
        <Button v-show="info.type === 1" icon="ios-create-outline" type="success" style="margin-left: 10px" size="small" @click="showAddOptionModal(info)">编辑下拉选项</Button>
      </Row>
      <div slot="footer">
        <Button @click="showInformationCollectionModal(false)">关闭</Button>
      </div>
    </Modal>
    <Modal v-model="optionModal" :closable='true' :mask-closable=false :width="600">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">编辑下拉选项</h3>
      <Row>
        <Button type="info" icon="md-add" @click="saveOption(0)">添加选项</Button>
      </Row>
      <Row style="margin-top: 8px" v-for="(option, index) in optionList" :key="'option' + index">
        <Input :ref="'option'+index" :value="option" style="width: 250px" @on-blur="saveOption(1, index)"/>
        <Button type="primary" shape="circle" style="margin-left: 10px" icon="ios-remove" @click="saveOption(2,index)"></Button>
        <Button :disabled="index === 0" type="primary" shape="circle" style="margin-left: 10px" icon="ios-arrow-up" @click="saveOption(3, index)"></Button>
        <Button :disabled="(index+1) === optionList.length" type="primary" shape="circle" style="margin-left: 10px" icon="ios-arrow-down" @click="saveOption(4, index)"></Button>
      </Row>
      <div slot="footer">
        <Button @click="optionModal = false">取消</Button>
      </div>
    </Modal>
    <Modal v-model="setUserRoleModal" :closable='true' :mask-closable=false :width="400">
      <h3 slot="header">设置成员角色</h3>
      <Form :model="editUserForm" :label-width="100" label-position="right" @submit.native.prevent="saveTitle">
        <FormItem label="角色" prop="roleId">
          <Select style="width: 200px" v-model="editUserForm.roleId" placeholder="请选择角色">
            <Option :value="Number(0)">学生</Option>
            <Option v-for="item in roleList" :value="item.pkId" :key="item.pkId">{{ item.name }}</Option>
          </Select>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="setUserRoleModal = false">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveSetUserRole">保存</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import API from '@/api'
import { ossUrl } from '@/libs/ossUrl'
import { oss } from '@/libs/ossUtil'

export default {
  name: 'user',
  data () {
    return {
      // 附件上传路径
      uploadHost: ossUrl,
      // 附件上传携带参数
      uploadData: {},
      // 上传后返回的存储的文件名
      fileName: '',
      // 上传后返回的文件存储地址
      filePath: '',
      loading: false,
      title: 0,
      isEnabled: -1,
      keywords: '',
      totalCount: 0,
      pageSize: 10,
      pageIndex: 1,
      checkOrganization: '',
      checkOrganizationList: [],
      selection: [], // 多选选中的值
      editUserModal: false, // 编辑模态框
      resetPasswordModal: false, // 重置密码
      titleModal: false, // 标签管理
      titleAddModal: false, // 添加标签
      informationCollectionModal: false,
      optionModal: false,
      setUserRoleModal: false,
      saveLoading: false,
      roleList: [],
      titleList: [],
      informationCollectionList: [],
      optionList: [],
      uploadOssList: [],
      editUserForm: {
        pkId: 0,
        account: '',
        nickname: '',
        organizationName: '',
        organizationId: 0,
        password: '',
        title: 0
      },
      userOtherInfo: {},
      informationCollectionForm: {},
      editTitleForm: {
        name: ''
      },
      userValidate: {
        account: [{
          required: true,
          message: '账号不能为空',
          trigger: 'blur'
        }]
      },
      passwordValidate: {
        password: [{
          required: true,
          message: '密码不能为空',
          trigger: 'blur'
        }]
      },
      titleValidate: {
        name: [{
          required: true,
          message: '请输入标签名称',
          trigger: 'blur'
        }]
      },
      columns: [
        {
          type: 'index',
          title: ' ',
          width: 20,
          align: 'center'
        },
        {
          type: 'selection',
          width: 60,
          align: 'right'
        },
        {
          title: '姓名',
          key: 'nickname',
          align: 'left',
          minWidth: 120
        },
        {
          title: '组织架构',
          key: 'organizationName',
          align: 'center',
          minWidth: 150
        },
        {
          title: '标签',
          key: 'titleName',
          align: 'center',
          minWidth: 120
        },
        {
          title: '用户类型',
          key: 'type',
          align: 'center',
          minWidth: 100,
          render: (h, params) => {
            switch (params.row.type) {
              case 0:return h('Label', '管理员')
              case 1:return h('Label', '子管理员')
              case 2:return h('Label', '学生')
            }
          }
        },
        {
          title: '管理员帐号',
          key: 'account',
          align: 'center',
          minWidth: 150
        },
        {
          title: '角色',
          slot: 'role',
          align: 'center',
          minWidth: 160
        },
        {
          title: '状态',
          key: 'isEnabled',
          render: (h, params) => {
            const row = params.row
            const color = row.isEnabled === 0 ? 'green' : 'red'
            const text = row.isEnabled === 0 ? '正常' : '待审核'
            return h('Tag', {
              props: {
                color: color
              }
            }, text)
          },
          align: 'center',
          minWidth: 100
        },
        {
          title: '创建时间',
          key: 'createTime',
          align: 'center',
          minWidth: 170
        },
        {
          title: '更新时间',
          key: 'updateTime',
          align: 'center',
          minWidth: 170
        },
        {
          title: '头像',
          key: 'avatar',
          align: 'center',
          width: 80,
          render: (h, params) => {
            return h('Avatar', {
              props: {
                src: params.row.avatar === '' ? '' : (this.uploadHost + '/' + params.row.avatar)
              }
            })
          }
        },
        {
          title: '操作',
          slot: 'action',
          align: 'center',
          minWidth: 250,
          fixed: 'right'
        }
      ],
      tableData: [],
      selectOrganizationList: [],
      userOrganizationList: []
    }
  },
  watch: {
    $route: function (to, from) {
      if (to.name === 'user') {
        this.getOrganization()
        this.getRole()
        this.getTitle()
        this.getInformationCollection()
        this.init()
      }
    }
  },
  methods: {
    init (reset) { // 数据初始化、搜索
      if (reset === 'reset') {
        this.pageIndex = 1
        this.pageSize = 10
        this.title = 0
        this.isEnabled = -1
        this.checkOrganization = ''
        this.checkOrganizationList = []
        this.keywords = ''
        this.getOrganization()
      }
      this.loading = true
      let searchParams = {
        pageSize: this.pageSize,
        page: this.pageIndex,
        title: this.title,
        isEnabled: this.isEnabled,
        organizationIds: this.checkOrganizationList.map(organization => { return organization.pkId }),
        keywords: this.keywords
      }

      API.manager.list(searchParams).then(({ data }) => {
        this.loading = false
        if (data && data.code === 0) {
          this.tableData = data.data.list
          this.totalCount = data.data.totalCount
        } else {
          this.loading = false
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.loading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getOrganization () {
      API.organization.list().then(({ data }) => {
        if (data && data.code === 0) {
          this.selectOrganizationList = data.data
          this.userOrganizationList = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getRole () {
      API.role.all().then(({ data }) => {
        if (data && data.code === 0) {
          this.roleList = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getTitle () {
      API.title.list().then(({ data }) => {
        if (data && data.code === 0) {
          this.titleList = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getInformationCollection () {
      API.informationCollection.list().then(({ data }) => {
        if (data && data.code === 0) {
          this.informationCollectionList = data.data.map(info => {
            info.isAble = !info.isAble
            return info
          })
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getCheckOrganization (list, data) {
      this.checkOrganizationList = list
      this.checkOrganization = ''
      list.forEach((organization) => {
        if (this.checkOrganization === '') {
          this.checkOrganization = organization.parentId === 0 ? '' : organization.title
        } else {
          this.checkOrganization += ';' + organization.title
        }
      })
    },
    getUserOrganization (list, data) {
      if (data.checked) {
        this.editUserForm.organizationName = data.title
        this.editUserForm.organizationId = data.pkId
      } else {
        this.editUserForm.organizationName = ''
        this.editUserForm.organizationId = 0
      }
      this.setCheckAble(this.userOrganizationList)
    },
    setCheckAble (data) {
      for (var i in data) {
        data[i].expand = false
        if (data[i].parentId !== 0 && !this.editUserForm.organizationId) {
          data[i].disableCheckbox = false
        } else {
          if (data[i].pkId === this.editUserForm.organizationId) {
            data[i].checked = true
            data[i].disableCheckbox = false
          } else {
            data[i].disableCheckbox = true
          }
        }
        if (data[i].children) {
          this.setCheckAble(data[i].children)
        }
      }
    },
    pageChange (val) { // 页数改变监听
      this.pageIndex = val
      this.init()
    },
    sizeChange (size) { // 每页个数改变监听
      this.pageSize = size
      this.init()
    },
    selectionChange (data) {
      // 表格选中数据监听
      // 记录选中的数据
      this.selection = data
    },
    functionChoose (name) {
      switch (name) {
        case 1 : this.showTitleModal(true); break
        case 2 : console.log(name); break
        case 3 : console.log(name); break
        case 4 : console.log(name); break
        case 5 : console.log(name); break
        case 6 : console.log(name); break
        case 7 : console.log(name); break
        case 8 : console.log(name); break
      }
    },
    rowFunctionChoose (value) {
      let json = JSON.parse(value)
      switch (json.num) {
        case 1 : this.showUserEditModal(json.user); break
        case 2 : this.showResetPasswordModal(json.user); break
        case 3 : console.log(111); break
        case 4 : this.deleteUser(json.user); break
        case 5 : console.log(name); break
        case 6 : console.log(name); break
        case 7 : console.log(name); break
      }
    },
    showTitleModal (show) {
      this.getTitle()
      this.titleModal = show
    },
    showAddTitleModal (show) {
      this.titleAddModal = show
    },
    showInformationCollectionModal (show) {
      this.getInformationCollection()
      this.informationCollectionModal = show
    },
    showAddOptionModal (info) {
      this.informationCollectionForm = info
      this.optionList = JSON.parse(info.option)
      this.optionModal = true
    },
    showUserEditModal (params) { // 显示模态框
      this.getOrganization()
      if (typeof params !== 'undefined') { // 如果是修改状态，则填充必要数据
        this.editUserForm = JSON.parse(JSON.stringify(params))
        this.userOtherInfo = JSON.parse(this.editUserForm.otherInfo)
      }
      setTimeout(() => {
        this.setCheckAble(this.userOrganizationList)
      }, 200)
      // 弹出编辑框
      this.editUserModal = true
    },
    showSetUserRoleModal (params) {
      this.getRole()
      if (typeof params !== 'undefined') { // 如果是修改状态，则填充必要数据
        this.editUserForm = JSON.parse(JSON.stringify(params))
      }
      // 弹出编辑框
      this.setUserRoleModal = true
    },
    showResetPasswordModal (params) {
      if (typeof params !== 'undefined') {
        this.editUserForm = JSON.parse(JSON.stringify(params))
      }
      this.resetPasswordModal = true
    },
    cancelEditUser () {
      this.editUserModal = false
    },
    resetUserData (val) { // 表单数据重置
      if (!val) {
        this.getOrganization()
        this.editUserForm = {
          pkId: 0,
          account: '',
          nickname: '',
          organizationName: '',
          organizationId: 0,
          password: '',
          title: 0
        }
        this.userOtherInfo = {}
      }
    },
    resetTitleData () { // 表单数据重置
      this.editTitleForm = {
        name: ''
      }
    },
    moveTitle (sort, move) {
      let param = {
        sort: sort,
        move: move
      }
      API.title.move(param).then(({ data }) => {
        if (data && data.code === 0) {
          this.getTitle()
          this.$Message.success({
            content: data.msg
          })
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络!')
      })
    },
    moveInformationCollection (info, move) {
      let param = {
        relationId: info.relationId,
        sort: info.sort,
        move: move
      }
      API.informationCollection.move(param).then(({ data }) => {
        if (data && data.code === 0) {
          this.getInformationCollection()
          this.$Message.success({
            content: data.msg
          })
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络!')
      })
    },
    saveEditUser () {
      this.$refs.editUserModal.validate((valid) => {
        if (valid) {
          // 添加成员
          let params = {
            pkId: this.editUserForm.pkId || undefined,
            account: this.editUserForm.account,
            nickname: this.editUserForm.nickname,
            organizationId: this.editUserForm.organizationId,
            password: !this.editUserForm.pkId && this.editUserForm.password !== '' ? this.editUserForm.password : undefined,
            title: this.editUserForm.title,
            otherInfo: JSON.stringify(this.userOtherInfo)
          }
          this.saveLoading = true
          let tick = !this.editUserForm.pkId ? API.manager.add(params) : API.manager.update(params)
          tick.then(({ data }) => {
            if (data && data.code === 0) {
              this.saveLoading = false
              this.$Message.success({
                content: data.msg,
                onClose: () => {
                  this.editUserModal = false
                  this.init()
                }
              })
            } else {
              this.saveLoading = false
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.saveLoading = false
            this.$Message.error('连接失败，请检查网络！')
          })
        }
      })
    },
    saveSetUserRole () {
      let params = {
        pkId: this.editUserForm.pkId,
        roleId: this.editUserForm.roleId
      }
      this.saveLoading = true
      API.manager.setUserRole(params).then(({ data }) => {
        if (data && data.code === 0) {
          this.saveLoading = false
          this.setUserRoleModal = false
          this.init()
          this.$Message.success({
            content: data.msg
          })
        } else {
          this.saveLoading = false
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.saveLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    resetPassword () {
      this.$refs.resetPasswordModal.validate((valid) => {
        if (valid) {
          // 重置密码
          let params = {
            pkId: this.editUserForm.pkId,
            account: this.editUserForm.account,
            nickname: this.editUserForm.nickname,
            password: this.editUserForm.password
          }
          this.saveLoading = true
          API.manager.resetPassword(params).then(({ data }) => {
            if (data && data.code === 0) {
              this.saveLoading = false
              this.$Message.success({
                content: data.msg,
                onClose: () => {
                  this.resetPasswordModal = false
                  this.init()
                }
              })
            } else {
              this.saveLoading = false
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.saveLoading = false
            this.$Message.error('连接失败，请检查网络！')
          })
        }
      })
    },
    saveTitle () {
      this.$refs.editTitleForm.validate((valid) => {
        if (valid) {
          // 添加标签
          let params = {
            name: this.editTitleForm.name
          }
          this.saveLoading = true
          API.title.add(params).then(({ data }) => {
            if (data && data.code === 0) {
              this.saveLoading = false
              this.$Message.success({
                content: data.msg,
                onClose: () => {
                  this.titleAddModal = false
                  this.getTitle()
                }
              })
            } else {
              this.saveLoading = false
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.saveLoading = false
            this.$Message.error('连接失败，请检查网络！')
          })
        }
      })
    },
    saveEditTitle (index) {
      if (this.$refs['titleName' + index][0].value !== this.$refs['titleName' + index][0].currentValue) {
        let params = {
          pkId: this.titleList[index].pkId,
          name: this.$refs['titleName' + index][0].currentValue
        }
        API.title.update(params).then(({ data }) => {
          if (data && data.code === 0) {
            this.$Message.success({
              content: data.msg,
              onClose: () => {
                this.getTitle()
              }
            })
          } else {
            this.$Message.error(data.msg)
          }
        }).catch(() => {
          this.$Message.error('连接失败，请检查网络！')
        })
      }
    },
    saveInformationCollection () {
      let params = {
        relationId: 0,
        type: 0,
        title: '信息名称',
        isRequired: 1,
        isAble: 1
      }
      API.informationCollection.add(params).then(({ data }) => {
        if (data && data.code === 0) {
          this.$Message.success({
            content: data.msg,
            onClose: () => {
              this.getInformationCollection()
            }
          })
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    saveEditInformationCollection (value, type, info, index) {
      let params = {
        pkId: info.pkId,
        type: info.type,
        title: info.title,
        isRequired: info.isRequired,
        isAble: info.isAble ? 0 : 1,
        option: info.option
      }
      switch (type) {
        case 0 : params.type = value; if (params.type === 2) { this.$refs['info' + index][0].currentValue = '组织架构'; params.title = '组织架构' } break
        case 1 : params.title = this.$refs['info' + index][0].value !== this.$refs['info' + index][0].currentValue ? this.$refs['info' + index][0].currentValue : undefined; break
        case 2 : params.isRequired = value; break
        case 3 : params.isAble = value ? 0 : 1; break
      }
      if (typeof params.title !== 'undefined') {
        API.informationCollection.update(params).then(({ data }) => {
          if (data && data.code === 0) {
            this.$Message.success({
              content: data.msg,
              onClose: () => {
                this.getInformationCollection()
              }
            })
          } else {
            this.$Message.error(data.msg)
          }
        }).catch(() => {
          this.$Message.error('连接失败，请检查网络！')
        })
      }
    },
    saveOption (type, index) {
      let save = true
      switch (type) {
        case 0 : let option = '选项' + (this.optionList.length + 1); if (this.optionList.indexOf(option) === -1) { this.optionList.push(option) } else { this.$Message.warning({ content: '选项重复' }); save = false }; break
        case 1 : if (this.$refs['option' + index][0].value !== this.$refs['option' + index][0].currentValue) { this.optionList[index] = this.$refs['option' + index][0].currentValue } else { save = false }; break
        case 2 : this.optionList.splice(index, 1); break
        case 3 : this.moveOption(index, index - 1); break
        case 4 : this.moveOption(index, index + 1); break
      }
      let params = {
        pkId: this.informationCollectionForm.pkId,
        type: this.informationCollectionForm.type,
        title: this.informationCollectionForm.title,
        isRequired: this.informationCollectionForm.isRequired,
        isAble: this.informationCollectionForm.isAble ? 0 : 1,
        option: JSON.stringify(this.optionList)
      }
      if (save) {
        API.informationCollection.update(params).then(({ data }) => {
          if (data && data.code === 0) {
            this.$Message.success({
              content: data.msg,
              onClose: () => {
                this.getInformationCollection()
              }
            })
          } else {
            this.$Message.error(data.msg)
          }
        }).catch(() => {
          this.$Message.error('连接失败，请检查网络！')
        })
      }
    },
    moveOption (index1, index2) {
      this.optionList[index1] = this.optionList.splice(index2, 1, this.optionList[index1])[0]
    },
    deleteTitle (pkId) {
      let param = {
        pkId: pkId
      }
      this.$Modal.confirm({
        title: '提示',
        content: '<p>确定要删除么</p>',
        onOk: () => {
          API.title.del(param).then(({ data }) => {
            if (data && data.code === 0) {
              this.$Message.success({
                content: data.msg,
                onClose: () => {
                  this.getTitle()
                }
              })
            } else {
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.$Message.error('连接失败，请检查网络！')
          })
        },
        onCancel: () => {

        }
      })
    },
    deleteInformationCollection (pkId) {
      let param = {
        pkId: pkId
      }
      this.$Modal.confirm({
        title: '提示',
        content: '<p>确定要删除么</p>',
        onOk: () => {
          API.informationCollection.del(param).then(({ data }) => {
            if (data && data.code === 0) {
              this.$Message.success({
                content: data.msg,
                onClose: () => {
                  this.getInformationCollection()
                }
              })
            } else {
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.$Message.error('连接失败，请检查网络！')
          })
        },
        onCancel: () => {

        }
      })
    },
    verifyUser () {
      let userIds = this.selection.map(user => {
        return user.pkId
      })
      if (userIds.length === 0) {
        this.$Message.error('请选择成员')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<p>确定审核</p>',
          onOk: () => {
            API.manager.verify(userIds).then(({ data }) => {
              if (data && data.code === 0) {
                this.$Message.success({
                  content: data.msg,
                  onClose: () => {
                    this.selection = []
                    this.init()
                  }
                })
              } else {
                this.$Message.error(data.msg)
              }
            }).catch(() => {
              this.$Message.error('连接失败，请检查网络！')
            })
          },
          onCancel: () => {
          }
        })
      }
    },
    deleteUser (params) {
      let userIds = []
      if (typeof params === 'undefined') {
        userIds = this.selection.map(user => {
          return user.pkId
        })
      } else {
        userIds = [params.pkId]
      }
      if (userIds.length === 0) {
        this.$Message.error('请选择成员')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<p>确定要删除么</p>',
          onOk: () => {
            API.manager.del(userIds).then(({ data }) => {
              if (data && data.code === 0) {
                this.$Message.success({
                  content: data.msg,
                  onClose: () => {
                    this.selection = []
                    this.init()
                  }
                })
              } else {
                this.$Message.error(data.msg)
              }
            }).catch(() => {
              this.$Message.error('连接失败，请检查网络！')
            })
          },
          onCancel: () => {
          }
        })
      }
    },
    handleOssSuccess (res) { // 文件上传成功
      /* 上传成功后，文件服务器会返回上传文件在oss上存储位置、文件名及相关信息
                  {
                    filename: "test/file-dir/JdzYDhdrtF.jpg"
                    height: "683"
                    mimeType: "image/jpeg"
                    size: "186142"
                    url: "http://xxxx.xxx.com/test/file-dir/JdzYDhdrtF.jpg"
                    width: "1024"
                    code: "0"
                    status: "Ok"
                  }
      */
      if (res.status === 'ok') {
        this.editUserForm.avatar = res.url
      }
    },
    handleOssFormatError (file) { // 文件格式错误
      this.$Notice.warning({
        title: '文件格式错误',
        desc: '文件【 ' + file.name + ' 】格式不正确，请选择jpg，png或gif格式.'
      })
    },
    handleOssMaxSize (file) { // 文件过大
      this.$Notice.warning({
        title: '文件大小超限',
        desc: '文件【' + file.name + '】过大,最大支持2M.'
      })
    },
    handleOssBeforeUpload (file) {
      let vm = this
      return oss(file.name, 0).then(res => {
        vm.uploadHost = res.host
        vm.uploadData = res
      })
    }
  },
  mounted () {
    this.getOrganization()
    this.getRole()
    this.getTitle()
    this.getInformationCollection()
    this.init()
  }
}
</script>
<style>
  .ivu-table-header {
    color: #909399;
  }
</style>
