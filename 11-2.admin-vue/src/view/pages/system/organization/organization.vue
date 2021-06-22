<template>
  <div>
    <Card dis-hover :bordered="false">
      <Row style="margin-bottom: 10px;">
        <div style="float: left">
          <Button type="primary" icon="md-add" @click="add(rootData)">创建组织架构</Button>
          <Button style="margin-left:20px" icon="ios-cloud-download-outline">下载组织架构模版文件</Button>
          <Button type="success" style="margin-left:20px" icon="ios-cloud-upload-outline">导入组织架构文件</Button>
          <Tooltip style="margin-left:20px" content="刷新" placement="bottom">
            <Button style="border-radius: 5px 0px 0px 5px;;" icon="ios-refresh" @click="init"></Button>
          </Tooltip>
          <Tooltip content="帮助中心" placement="bottom">
            <Button style="border-radius: 0px 5px 5px 0px;;" icon="ios-help-circle"></Button>
          </Tooltip>
        </div>
      </Row>
      <Row style="margin-top: 10px">
            <Tree :data='organizationList' :render="renderContent" class="demo-tree-render" style="width: 100%"></Tree>
      </Row>
      </Card>
  </div>
</template>
<script>
import API from '@/api'

export default {
  name: 'organization',
  data () {
    return {
      organizationList: [],
      rootData: {},
      currentNode: 0,
      isEdit: false
    }
  },
  watch: {
    $route: function (to, from) {
      if (to.name === 'organization') {
        this.init()
      }
    }
  },
  methods: {
    init () {
      API.organization.list().then(({ data }) => {
        if (data && data.code === 0) {
          this.organizationList = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    renderContent: function (h, { root, node, data }) {
      if (node.nodeKey === 0) {
        this.rootData = data
        return h('span',
          [
            h('span', {
              style: {
                fontSize: '15px',
                cursor: 'pointer'
              }
            }, data.title)
          ])
      } else {
        return h('span',
          [
            this.$createElement('Input', {
              ref: 'title' + node.nodeKey,
              props: {
                value: data.title
              },
              style: {
                width: '100px',
                marginRight: '10px',
                display: (this.currentNode === node.nodeKey && this.isEdit) ? '' : 'none'
              },
              on: {
                'on-blur': () => {
                  this.isEdit = false
                  data.title = this.$refs['title' + node.nodeKey].currentValue
                  this.saveOrganization(data)
                }
              }
            }),
            h('span', {
              style: {
                cursor: 'pointer'
              },
              on: {
                mouseover: () => {
                  if (!this.isEdit) {
                    this.currentNode = node.nodeKey
                  }
                },
                mouseout: () => {
                  if (!this.isEdit) {
                    this.currentNode = 0
                  }
                }
              }
            },
            [
              h('span', {
                style: {
                  fontSize: '15px',
                  marginRight: '10px',
                  display: (this.currentNode === node.nodeKey && this.isEdit) ? 'none' : ''
                }
              }, data.title),
              h('Button', {
                props: {
                  icon: 'ios-add',
                  type: 'text',
                  size: 'small'
                },
                style: {
                  marginRight: '5px',
                  display: (this.currentNode === node.nodeKey && !this.isEdit) ? '' : 'none'
                },
                on: {
                  click: () => {
                    this.add(data)
                  }
                }
              }),
              h('Button', {
                props: {
                  icon: 'md-create',
                  type: 'text',
                  size: 'small'
                },
                style: {
                  marginRight: '5px',
                  display: (this.currentNode === node.nodeKey && !this.isEdit) ? '' : 'none'
                },
                on: {
                  click: () => {
                    this.edit()
                  }
                }
              }),
              h('Button', {
                props: {
                  icon: 'ios-trash-outline',
                  type: 'text',
                  size: 'small'
                },
                style: {
                  marginRight: '5px',
                  display: (this.currentNode === node.nodeKey && !this.isEdit) ? '' : 'none'
                },
                on: {
                  click: () => {
                    this.remove(root, node, data)
                  }
                }
              })
            ])
          ])
      }
    },
    add (data) {
      data.expand = true
      const children = data.children || []
      children.push({
        pkId: 0,
        parentId: data.pkId,
        title: '',
        expand: false
      })
      this.$set(data, 'children', children)
      setTimeout(() => {
        this.currentNode = data.children[data.children.length - 1].nodeKey
        this.isEdit = true
      }, 100)
    },
    edit () {
      this.isEdit = true
    },
    remove (root, node, data) {
      // const parentKey = root.find(el => el === node).parent
      // const parent = root.find(el => el.nodeKey === parentKey).node
      // const index = parent.children.indexOf(data)
      // parent.children.splice(index, 1)
      this.deleteOrganization(data)
    },
    saveOrganization (organization) {
      let params = {
        pkId: organization.pkId || undefined,
        parentId: organization.parentId,
        name: organization.title
      }
      let tick = !organization.pkId ? API.organization.add(params) : API.organization.update(params)
      tick.then(({ data }) => {
        if (data && data.code === 0) {
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
    deleteOrganization (data) {
      this.$Modal.confirm({
        title: '提示',
        content: '<p>确定要删除么</p>',
        onOk: () => {
          API.organization.del({ pkId: data.pkId }).then(({ data }) => {
            if (data && data.code === 0) {
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
    }
  },
  mounted () {
    this.init()
  }
}
</script>
<style>
  .demo-tree-render .ivu-tree-title{
    width: 100%;
  }
</style>
