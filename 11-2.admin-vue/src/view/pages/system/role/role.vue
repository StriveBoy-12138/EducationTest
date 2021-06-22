<template>
  <div>
    <Card dis-hover :bordered="false">
      <Row align="middle" style="margin-bottom: 10px;">
        <div>
          <Button type="success" style="border-radius: 5px 0px 0px 5px;" icon="md-add" @click="showRoleEditModal()">新增角色</Button>
          <Tooltip content="刷新" placement="bottom">
            <Button style="border-radius: 0px 5px 5px 0px;" icon="ios-refresh" @click="init"></Button>
          </Tooltip>
        </div>
        <div style="margin-left: 1180px">
          <Input v-model="keywords" placeholder="请输入角色名称" style="width: 150px" />
          <ButtonGroup>
            <Button style="background-color: #F5F7FA" @click="init">搜索</Button>
            <Button style="background-color: #F5F7FA" @click="init('reset')">重置</Button>
          </ButtonGroup>
        </div>
      </Row>
      <Row>
        <Table border ref="selection" :columns="columns" :data="tableData" :highlight-row="true" :current="pageIndex"
               @on-selection-change="selectionChange" :loading="loading" :stripe="true"></Table>
      </Row>
      <Row justify="end" style="margin-top: 10px;width: 100%">
        <div style="float:right">
          <Page :total="totalCount" :current="pageIndex" show-total :page-sise="pageSize" loading show-sizer
                @on-change="pageChange" @on-page-size-change="sizeChange">
          </Page>
        </div>
      </Row>
    </Card>
    <Modal v-model="editRoleModal" :closable='true' :mask-closable=false :width="600" @on-visible-change="resetData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">{{!editRoleForm.pkId?'添加角色':'修改角色'}}</h3>
      <Form ref="editRoleForm" :model="editRoleForm" :label-width="100" label-position="right" :rules="RoleValidate" @submit.native.prevent="saveEditRole">
        <FormItem label="角色名称" prop="name">
          <Input v-model="editRoleForm.name" style="width: 250px" placeholder="请输入角色名称"></Input>
        </FormItem>
        <FormItem label="角色描述" prop="des">
          <Input v-model="editRoleForm.des" style="width: 250px" placeholder="请输入角色描述"></Input>
        </FormItem>
        <FormItem label="权限列表" prop="menuList">
          <Tree :data='menuList' show-checkbox @on-check-change="treeCheckChange"></Tree>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="cancelEditRole">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveEditRole">保存</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import API from '@/api'
import {
  treeDataTranslate,
  unique,
  checkDelete
} from '@/libs/util'

export default {
  name: 'role_page',
  data () {
    return {
      loading: false,
      keywords: '',
      totalCount: 0,
      pageSize: 10,
      pageIndex: 1,
      selection: [], // 多选选中的值
      editRoleModal: false, // 新增模态框
      saveLoading: false,
      menuList: [],
      menuIdList: [],
      firstMenuList: [],
      treeValue: [], // 当前选中的树
      menuListModal: false,
      editMenuModal: false,
      isParentId: 0,
      firstMenu: '',
      editRoleForm: {
        pkId: 0,
        name: '',
        des: ''
      },
      RoleValidate: {
        name: [{
          required: true,
          message: '请输入角色名称',
          trigger: 'blur'
        }],
        des: [{
          required: true,
          message: '请输入角色描述',
          trigger: 'blur'
        }]
      },
      columns: [
        {
          title: '角色名称',
          key: 'name',
          align: 'center',
          minWidth: 160
        },
        {
          title: '角色描述',
          key: 'des',
          align: 'center',
          minWidth: 160
        },
        {
          title: '创建时间',
          key: 'createTime',
          align: 'center',
          minWidth: 160
        },
        {
          title: '操作',
          key: 'action',
          align: 'center',
          fixed: 'right',
          width: 160,
          render: (h, params) => {
            let editBtn = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              on: {
                click: () => {
                  this.showRoleEditModal(params)
                }
              }
            }, '修改')
            let deleteBtn = h('Button', {
              props: {
                type: 'error',
                size: 'small'
              },
              on: {
                click: () => {
                  this.delete(params)
                }
              }
            }, '删除')

            return h('div', [
              editBtn, deleteBtn
            ])
          }
        }
      ],
      tableData: []
    }
  },
  methods: {
    init (reset) { // 数据初始化、搜索
      if (reset === 'reset') {
        this.pageSize = 10
        this.pageIndex = 1
        this.keywords = ''
      }
      this.loading = true
      let searchParams = {
        pageSize: this.pageSize,
        page: this.pageIndex,
        keywords: this.keywords
      }
      API.role.list(searchParams).then(({ data }) => {
        this.loading = false
        if (data && data.code === 0) {
          this.tableData = data.data.list
          this.totalCount = data.data.totalCount
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.loading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    resetData (val) { // 表单数据重置
      if (!val) {
        this.editRoleForm = {
          pkId: 0,
          name: '',
          des: '',
          menuIds: ''
        }
        this.menuIdList = []
        this.treeValue = []
        this.saveLoading = false
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
      this.selection = data
    },
    showRoleEditModal (params) { // 显示模态框
      let menuList = []
      API.role.listMenu().then(({ data }) => {
        menuList = data.data.list
      }).then(() => {
        if (typeof params !== 'undefined') { // 如果是修改状态，则填充必要数据
          this.editRoleForm = JSON.parse(JSON.stringify(params.row))
          API.role.info(this.editRoleForm.pkId).then(({
            data
          }) => {
            if (data && data.code === 0) {
              this.menuIdList = data.data.menuIdList
              this.treeValue = this.menuIdList
              this.menuList = treeDataTranslate(menuList, this.menuIdList, 'pkId')
            }
          }).then(() => {
            // 弹出编辑框
            this.editRoleModal = true
          })
        } else {
          this.menuList = treeDataTranslate(menuList, [], 'pkId')
          // 弹出编辑框
          this.editRoleModal = true
        }
      }).catch(() => {
      })
    },
    cancelEditRole () {
      this.editRoleModal = false
    },
    treeCheckChange (val) {
      this.treeValue = val
    },
    saveEditRole () {
      this.$refs.editRoleForm.validate((valid) => {
        if (valid) {
          this.menuIdList = []
          this.treeValue.forEach((item) => {
            this.menuIdList.push(typeof item.pkId === 'number' ? item.pkId : item)
          })
          let tempList = unique(this.menuIdList)
          this.menuIdList = tempList.length === 0 ? tempList : this.treeValue.map((item) => {
            return typeof item.pkId === 'number' ? item.pkId : item
          })
          // 添加角色
          let params = {
            pkId: this.editRoleForm.pkId || -1,
            name: this.editRoleForm.name,
            des: this.editRoleForm.des,
            menuIds: this.menuIdList
          }
          this.saveLoading = true
          let msg = typeof params.pkId === 'undefined' ? '添加成功' : '修改成功'
          let tick = !this.editRoleForm.pkId ? API.role.add(params) : API.role.update(params)
          tick.then(({ data }) => {
            if (data && data.code === 0) {
              this.saveLoading = false
              this.$Message.success({
                content: msg,
                onClose: () => {
                  this.init()
                  this.editRoleModal = false
                  this.saveLoading = false
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
    batchDel () { // 批量删除
      let pkIds = []
      // 批量删除
      this.selection.forEach((item) => {
        pkIds.push(item.pkId)
      })
      if (pkIds.length === 0) {
        this.$Message.error('请至少选中一行数据！')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<p>确定要删除么</p>',
          onOk: () => {
            API.role.del(pkIds).then(({
              data
            }) => {
              if (data && data.code === 0) {
                this.$Message.success({
                  content: data.msg,
                  onClose: () => {
                    this.pageIndex = checkDelete(this.pageIndex, this.pageSize, this.totalCount - pkIds.length)
                    this.selection = [] // 勾选数据初始化
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
    delete (params) { // 删除
      let pkIds = [params.row.pkId]
      this.$Modal.confirm({
        title: '提示',
        content: '<p>确定要删除么</p>',
        onOk: () => {
          API.role.del(pkIds).then(({
            data
          }) => {
            if (data && data.code === 0) {
              this.pageIndex = checkDelete(this.pageIndex, this.pageSize, this.totalCount - pkIds.length)
              this.$Message.success({
                content: data.msg,
                onClose: () => {
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
    },
    changeParent (val) {
      this.isParentId = val
    }
  },
  mounted () {
    this.init() // 初始化
  }
}
</script>
