<template>
  <div>
    <Card dis-hover :bordered="false" :style="'height: '+headerHeight+'px'">
      <div style="width: 100%;cursor: pointer" @click="showHeader">
        <span>题库统计</span>
        <Icon v-if="headerHeight === 50" style="float: right" type="ios-arrow-forward" />
        <Icon v-else style="float: right" type="ios-arrow-down" />
      </div>
    </Card>
    <Card style="margin-top: 10px" dis-hover :bordered="false">
      <Row style="margin-bottom: 10px;">
        <Col>
          <ButtonGroup>
            <Button type="warning" icon="md-add" @click="addButton()">创建题库</Button>
            <Button type="warning" style="margin-left: 1px">批量导题</Button>
          </ButtonGroup>
          <Button style="margin-left: 10px" type="primary" icon="md-create" @click="showQuestionBankType">维护分类</Button>
          <Tooltip content="刷新" placement="bottom">
            <Button style="border-radius: 5px 0px 0px 5px;margin-left: 10px" icon="ios-refresh" @click="init"></Button>
          </Tooltip>
          <Tooltip content="帮助中心" placement="bottom">
            <Button style="border-radius: 0px;" icon="ios-help-circle"></Button>
          </Tooltip>
          <Button style="border-radius: 0px;" type="success" icon="ios-trash-outline" @click="deleteQuestionBank()"></Button>
          <Dropdown placement="bottom-end" @on-click="functionChoose">
            <Button style="border-radius: 0px 5px 5px 0px;" icon="md-arrow-dropdown"/>
            <DropdownMenu slot="list">
              <DropdownItem :name="Number(1)"><Icon type="ios-settings-outline" style="margin-right: 5px"/>知识点管理</DropdownItem>
              <DropdownItem :name="Number(2)"><Icon type="ios-paper-outline" style="margin-right: 5px"/>题型名称管理</DropdownItem>
              <DropdownItem :name="Number(3)"><Icon type="ios-search-outline" style="margin-right: 5px"/>搜索所有题库</DropdownItem>
              <DropdownItem :name="Number(4)"><Icon type="md-paper" style="margin-right: 5px"/>题目查重</DropdownItem>
              <DropdownItem :name="Number(5)"><Icon type="ios-paper-outline" style="margin-right: 5px"/>批量导题记录</DropdownItem>
              <DropdownItem :name="Number(6)"><Icon type="ios-chatboxes-outline" style="margin-right: 5px"/>题目纠错记录</DropdownItem>
            </DropdownMenu>
          </Dropdown>
        </Col>
        <Col style="margin-left: 720px">
          <Select style="width: 150px" placeholder="选择分类" v-model="typeId">
            <Option :value="Number(0)">全部</Option>
            <Option v-for="(item, index) in questionBankTypeList" :key="index" :value="item.pkId">{{item.name}}</Option>
          </Select>
          <Input style="width: 150px" v-model="name" placeholder="请输入题库名称"/>
          <ButtonGroup>
            <Button style="background-color: #F5F7FA" @click="init">搜索</Button>
            <Button style="background-color: #F5F7FA" @click="init('reset')">重置</Button>
          </ButtonGroup>
        </Col>
      </Row>
      <Row>
        <Table ref="selection" :columns="columns" :data="tableData" :highlight-row="true" :current="pageIndex" :key="timer" style="width: 100%"
               @on-selection-change="selectionChange" :loading="loading">
          <template slot="action" slot-scope="scope">
            <ButtonGroup>
              <Button type="primary" size="small" @click="maintenanceTopics(scope.row)">维护题目</Button>
              <Button type="primary" style="margin-left: 1px" size="small" @click="addButton(scope.row)">编辑题库</Button>
              <Dropdown placement="bottom-end" @on-click="rowFunctionChoose" transfer>
                <Button size="small">
                  <Icon type="ios-arrow-down"></Icon>
                </Button>
                <DropdownMenu slot="list">
                  <DropdownItem :name="JSON.stringify({num: 1,bank: scope.row})"><Icon type="ios-copy-outline" style="margin-right: 5px"/>复制题库</DropdownItem>
                  <DropdownItem :name="JSON.stringify({num: 2,bank: scope.row})"><Icon type="ios-trash-outline" style="margin-right: 5px"/>删除题库</DropdownItem>
                </DropdownMenu>
              </Dropdown>
            </ButtonGroup>
          </template>
        </Table>
      </Row>
      <Row style="margin-top: 10px;" justify="end">
        <Page :total="totalCount" :current="pageIndex" :page-sise="pageSize" loading show-total show-sizer show-elevator
              @on-change="pageChange" @on-page-size-change="sizeChange">
        </Page>
      </Row>
    </Card>
    <!--维护分类对话框-->
    <Modal v-model="questionBankTypeModal" :closable='true' :mask-closable=false :width="600" @on-visible-change="resetQuestionBankTypeData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">维护分类</h3>
      <Row style="margin-bottom: 10px">
        <Button type="primary" icon="md-add" @click="addQuestionBankType">创建分类</Button>
        <ButtonGroup style="margin-left: 20px">
          <Button icon="ios-upload-outline">下载分类模版文件</Button>
          <Button type="success" icon="ios-download-outline">导入分类文件</Button>
        </ButtonGroup>
      </Row>
      <Row v-for="(questionBankType, index) in questionBankTypeList" :key="'questionBankType'+index" :style="'margin-left: 10px;background-color: '  + (currentQuestionBankType === index ? '#d5e8fc':'')">
        <Input v-if="isQuestionBankTypeEdit && currentQuestionBankType === index" :ref="'questionBankTypeName'+index" :value="questionBankType.name" style="width: 100px" @on-blur="saveEditQuestionBankType(index)"/>
        <span v-else style="cursor: pointer;" @mouseover="questionBankTypeMouseOver(index)" @mouseout="questionBankTypeMouseOut">
          {{questionBankType.name}}
          <ButtonGroup style="margin-left: 5px">
          <Button type="text" icon="md-create" @click="editQuestionBankType()"></Button>
          <Button type="text" icon="ios-trash-outline" @click="deleteQuestionBankType(questionBankType.pkId)"></Button>
        </ButtonGroup>
        </span>
      </Row>
      <div slot="footer">
        <Button @click="questionBankTypeModal = false">取消</Button>
      </div>
    </Modal>
    <!--添加/修改题库对话框-->
    <Modal v-model="addModal" :closable='true' :mask-closable=false :width="900" @on-visible-change="resetFormData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">{{!editForm.pkId?'创建题库':'编辑题库'}}</h3>
      <Form ref="editForm" :model="editForm" label-position="right" :label-width="120"
            :rules="rules" @submit.native.prevent="saveEdit">
        <FormItem label="分类" prop="typeId">
          <Select v-model="editForm.typeId" style="margin-right:5px;width: 150px;" placeholder="选择分类">
            <Option v-for="item in questionBankTypeList" :value="item.pkId" :key="'chooseQuestionBankType'+item.pkId">{{item.name}}</Option>
          </Select>
        </FormItem>
        <FormItem label="题库名称" prop="name">
          <Input v-model="editForm.name" style="width: 645px" :maxlength="32"/>
        </FormItem>
        <FormItem label="题库描述" prop="des">
          <Input v-model="editForm.des" type="textarea" :rows="2" style="width: 645px" :maxlength="200"/>
        </FormItem>
        <FormItem label="封面图片" prop="picture">
          <Upload :action="uploadHost" ref="uploadOss" :show-upload-list="false" :data="uploadData"
                  :format="['jpg','jpeg','png']"
                  :on-success="handleOssSuccess"
                  :max-size="2048"
                  :on-format-error="handleOssFormatError"
                  :on-exceeded-size="handleOssMaxSize"
                  :before-upload="handleOssBeforeUpload">
            <div style="border: 1px dashed #d9d9d9;border-radius: 6px;cursor: pointer;width: 302px;height: 122px">
              <Icon v-if="editForm.picture.length === 0" type="ios-add" size="40" style="color: #8c939d;position: absolute;top: 45px;left: 130px"></Icon>
              <div v-else style="width: 300px;height: 120px">
                <img style="width: 300px;height: 120px" :src="uploadHost + '/' + editForm.picture">
              </div>
            </div>
          </Upload>
          <Icon v-if="editForm.picture.length !== 0" type="md-close-circle" size="30" style="color: red;position: absolute;top: -10px;left: 285px;cursor: pointer" @click.native="handleRemove"></Icon>
          <Row style="margin-top: 10px" align="middle"><Icon type="ios-alert" style="color: red;margin-right: 5px" />建议图片方向为横向，尺寸比例在5：2至5：3之间（典型尺寸：300x120 与 300x180）</Row>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="cancel">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveEdit">提交</Button>
      </div>
    </Modal>
    <!--知识点管理对话框-->
    <Modal v-model="knowledgePointModal" :closable="true" :mask-closable="false" :width="800">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">知识点</h3>
      <Row>
        <ButtonGroup>
          <Button type="success" icon="md-add" @click="addKnowledgePoint()">创建</Button>
          <Button type="info" style="margin-left: 1px" @click="showKnowledgePointType()">管理分类</Button>
        </ButtonGroup>
        <Select style="width: 150px;margin-left: 120px" placeholder="选择分类" v-model="knowledgePointTypeId">
          <Option :value="Number(0)">全部</Option>
          <Option v-for="(item, index) in knowledgePointTypeList" :key="index" :value="item.pkId">{{item.name}}</Option>
        </Select>
        <Input style="width: 200px;margin-left: 5px" v-model="knowledgePointName" placeholder="请输入知识点名称"/>
        <ButtonGroup>
          <Button style="background-color: #F5F7FA" @click="getKnowledgePoint()">搜索</Button>
          <Button style="background-color: #F5F7FA" @click="getKnowledgePoint('reset')">重置</Button>
        </ButtonGroup>
      </Row>
      <Row style="margin-top: 10px">
        <Table :columns="knowledgePointColumns" :data="knowledgePointTableData" :highlight-row="true" :current="knowledgePointPageIndex" style="width: 100%;" :key="timer" :loading="knowledgePointTableLoading">
          <template slot="action" slot-scope="scope">
            <ButtonGroup>
              <Button type="primary" size="small" @click="addKnowledgePoint(scope.row)">编辑</Button>
              <Button type="error" style="margin-left: 1px" size="small" @click="deleteKnowledgePoint(scope.row.pkId)">删除</Button>
            </ButtonGroup>
          </template>
        </Table>
      </Row>
      <Row style="margin-top: 10px" justify="end">
        <Page :total="knowledgePointTotalCount" :current="knowledgePointPageIndex" :page-sise="knowledgePointPageSize" loading show-total show-sizer show-elevator
              @on-change="knowledgePointPageChange" @on-page-size-change="knowledgePointSizeChange">
        </Page>
      </Row>
      <div slot="footer"></div>
    </Modal>
    <!--管理知识点分类对话框-->
    <Modal v-model="knowledgePointTypeModal" :closable='true' :mask-closable=false :width="600" @on-visible-change="resetKnowledgePointTypeData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">知识点分类</h3>
      <Row style="margin-bottom: 10px">
        <Button type="primary" icon="md-add" @click="addKnowledgePointType">创建分类</Button>
      </Row>
      <Row v-for="(knowledgePointType, index) in knowledgePointTypeList" :key="'knowledgePointType'+index" :style="'margin-left: 10px;background-color: '  + (currentKnowledgePointType === index ? '#d5e8fc':'')">
        <Input v-if="isKnowledgePointTypeEdit && currentKnowledgePointType === index" :ref="'knowledgePointTypeName'+index" :value="knowledgePointType.name" style="width: 100px" @on-blur="saveEditKnowledgePointType(index)"/>
        <span v-else style="cursor: pointer;" @mouseover="knowledgePointTypeMouseOver(index)" @mouseout="knowledgePointTypeMouseOut">
          {{knowledgePointType.name}}
          <ButtonGroup style="margin-left: 5px">
          <Button type="text" icon="md-create" @click="editKnowledgePointType()"></Button>
          <Button type="text" icon="ios-trash-outline" @click="deleteKnowledgePointType(knowledgePointType.pkId)"></Button>
        </ButtonGroup>
        </span>
      </Row>
      <div slot="footer"></div>
    </Modal>
    <!--添加/修改知识点对话框-->
    <Modal v-model="addKnowledgePointModal" :closable='true' :mask-closable=false :width="500" @on-visible-change="resetKnowledgePointFormData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">{{!knowledgePointForm.pkId?'创建知识点':'编辑知识点'}}</h3>
      <Form ref="knowledgePointForm" :model="knowledgePointForm" label-position="right" :label-width="80"
            :rules="knowledgePointRules" @submit.native.prevent="saveKnowledgePointEdit">
        <FormItem label="分类" prop="typeId">
          <Select v-model="knowledgePointForm.typeId" style="margin-right:5px;width: 150px;" placeholder="选择分类">
            <Option v-for="item in knowledgePointTypeList" :value="item.pkId" :key="'chooseKnowledgePointType'+item.pkId">{{item.name}}</Option>
          </Select>
        </FormItem>
        <FormItem label="名称" prop="name">
          <Input v-model="knowledgePointForm.name" type="textarea" :rows="2" style="width: 350px" :maxlength="200"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="addKnowledgePointModal = false">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveKnowledgePointEdit">提交</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import API from '@/api'
import tinymceEditor from '_c/text-editor/tinymce-editor.vue'
import { aplayer, vplayer } from 'vue-hls-player'
import { ossUrl } from '@/libs/ossUrl'
import { oss } from '@/libs/ossUtil'

export default {
  name: 'questionBank',
  components: { tinymceEditor, aplayer, vplayer },
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
      uploadFileName: '',
      headerHeight: 50,
      timer: '',
      typeId: '',
      name: '',
      knowledgePointTypeId: '',
      knowledgePointName: '',
      loading: false,
      knowledgePointTableLoading: false,
      uploadLoading: false,
      saveLoading: false,
      questionBankTypeModal: false,
      knowledgePointModal: false,
      knowledgePointTypeModal: false,
      addModal: false,
      addKnowledgePointModal: false,
      isQuestionBankTypeEdit: false,
      isKnowledgePointTypeEdit: false,
      currentQuestionBankType: -1,
      currentKnowledgePointType: -1,
      totalCount: 0,
      knowledgePointTotalCount: 0,
      pageIndex: 1,
      knowledgePointPageIndex: 1,
      pageSize: 10,
      knowledgePointPageSize: 10,
      questionBankTypeList: [],
      knowledgePointTypeList: [],
      editForm: {
        pkId: 0,
        typeId: 0,
        name: '',
        des: '',
        picture: ''
      },
      knowledgePointForm: {
        pkId: 0,
        typeId: 0,
        name: ''
      },
      selection: [],
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
          title: '题库名称',
          key: 'name',
          align: 'left',
          minWidth: 200
        },
        {
          title: '描述',
          key: 'des',
          align: 'center',
          width: 200
        },
        {
          title: '分类',
          key: 'typeName',
          align: 'center',
          minWidth: 80
        },
        {
          title: '题目数量',
          key: 'questionNum',
          align: 'center',
          width: 100
        },
        {
          title: '创建时间',
          key: 'createTime',
          align: 'center',
          width: 170
        },
        {
          title: '创建人',
          key: 'authorName',
          align: 'center',
          width: 140
        },
        {
          title: '操作',
          slot: 'action',
          align: 'center',
          width: 250
        }
      ],
      knowledgePointColumns: [
        {
          type: 'index',
          title: ' ',
          width: 20,
          align: 'center'
        },
        {
          title: '名称',
          key: 'name',
          align: 'left',
          minWidth: 100
        },
        {
          title: '分类',
          key: 'typeName',
          align: 'center',
          minWidth: 100
        },
        {
          title: '创建时间',
          key: 'createTime',
          align: 'center',
          width: 170
        },
        {
          title: '创建人',
          key: 'authorName',
          align: 'center',
          width: 140
        },
        {
          title: '操作',
          slot: 'action',
          align: 'center',
          width: 150
        }
      ],
      tableData: [],
      knowledgePointTableData: [],
      rules: {
        name: [{
          required: true,
          message: '题库名称不能为空',
          trigger: 'blur'
        }]
      },
      knowledgePointRules: {
        name: [{
          required: true,
          message: '知识点名称不能为空',
          trigger: 'blur'
        }]
      }
    }
  },
  watch: {
    $route: function (to, from) {
      if (to.name === 'questionBank') {
        this.getQuestionBankType()
        this.init()
      }
    }
  },
  methods: {
    showHeader () {
      if (this.headerHeight === 50) {
        this.headerHeight = 300
      } else {
        this.headerHeight = 50
      }
    },
    maintenanceTopics (params) {
      this.$router.push({ name: 'question', query: { id: params.pkId, name: params.name } })
    },
    init (reset) {
      if (reset === 'reset') {
        this.typeId = ''
        this.name = ''
      }
      this.loading = true
      let searchParams = {
        pageSize: this.pageSize,
        pageIndex: this.pageIndex,
        typeId: this.typeId,
        name: this.name
      }
      API.questionBank.list(searchParams).then(({ data }) => {
        this.loading = false
        if (data && data.code === 0) {
          this.tableData = data.data.list
          this.totalCount = data.data.totalCount
          this.timer = new Date().getTime()
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.loading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getQuestionBankType () {
      API.questionBankType.list().then(({ data }) => {
        if (data && data.code === 0) {
          this.questionBankTypeList = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getKnowledgePointType () {
      API.knowledgePointType.list().then(({ data }) => {
        if (data && data.code === 0) {
          this.knowledgePointTypeList = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getKnowledgePoint (reset) {
      if (reset === 'reset') {
        this.knowledgePointTypeId = ''
        this.knowledgePointName = ''
      }
      this.knowledgePointTableLoading = true
      let searchParams = {
        pageSize: this.knowledgePointPageSize,
        pageIndex: this.knowledgePointPageIndex,
        typeId: this.knowledgePointTypeId,
        name: this.knowledgePointName
      }
      API.knowledgePoint.list(searchParams).then(({ data }) => {
        this.knowledgePointTableLoading = false
        if (data && data.code === 0) {
          this.knowledgePointTableData = data.data.list
          this.knowledgePointTotalCount = data.data.totalCount
          this.timer = new Date().getTime()
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.knowledgePointTableLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    questionBankTypeMouseOver (index) {
      if (!this.isQuestionBankTypeEdit) {
        this.currentQuestionBankType = index
      }
    },
    questionBankTypeMouseOut () {
      if (!this.isQuestionBankTypeEdit) {
        this.currentQuestionBankType = -1
      }
    },
    knowledgePointTypeMouseOver (index) {
      if (!this.isKnowledgePointTypeEdit) {
        this.currentKnowledgePointType = index
      }
    },
    knowledgePointTypeMouseOut () {
      if (!this.isKnowledgePointTypeEdit) {
        this.currentKnowledgePointType = -1
      }
    },
    editQuestionBankType () {
      this.isQuestionBankTypeEdit = true
    },
    editKnowledgePointType () {
      this.isKnowledgePointTypeEdit = true
    },
    functionChoose (name) {
      switch (name) {
        case 1 : this.showKnowledgePoint(); break
        case 2 : alert(11); break
        case 3 : alert(111); break
        case 4 : alert(1111); break
        case 5 : alert(name); break
        case 6 : alert(name); break
      }
    },
    rowFunctionChoose (value) {
      let json = JSON.parse(value)
      switch (json.num) {
        case 1 : console.log(json.bank); break
        case 2 : this.deleteQuestionBank(json.bank); break
      }
    },
    addButton (params) {
      if (typeof params !== 'undefined') { // 如果是修改状态，则填充必要数据
        this.editForm = JSON.parse(JSON.stringify(params))
      }
      this.addModal = true
    },
    showQuestionBankType () {
      this.getQuestionBankType()
      this.questionBankTypeModal = true
    },
    showKnowledgePoint () {
      this.getKnowledgePointType()
      this.getKnowledgePoint()
      this.knowledgePointModal = true
    },
    showKnowledgePointType () {
      this.getKnowledgePointType()
      this.knowledgePointTypeModal = true
    },
    addKnowledgePoint (params) {
      if (typeof params !== 'undefined') {
        this.knowledgePointForm = JSON.parse(JSON.stringify(params))
      }

      this.addKnowledgePointModal = true
    },
    selectionChange (data) {
      // 记录选中的数据
      this.selection = data
    },
    pageChange (val) {
      this.pageIndex = val
      this.init()
    },
    knowledgePointPageChange (val) {
      this.knowledgePointPageIndex = val
      this.getKnowledgePoint()
    },
    sizeChange (size) {
      this.pageSize = size
      this.init()
    },
    knowledgePointSizeChange (size) {
      this.knowledgePointPageSize = size
      this.getKnowledgePoint()
    },
    cancel () {
      this.addModal = false
    },
    resetQuestionBankTypeData () {
      this.isQuestionBankTypeEdit = false
      this.currentQuestionBankType = -1
    },
    resetKnowledgePointTypeData () {
      this.isKnowledgePointTypeEdit = false
      this.currentKnowledgePointType = -1
    },
    resetKnowledgePointFormData (val) {
      if (!val) {
        this.knowledgePointForm = {
          pkId: 0,
          typeId: 0,
          name: ''
        }
      }
    },
    resetFormData (val) {
      if (!val) {
        this.editForm = {
          pkId: 0,
          typeId: 0,
          name: '',
          des: '',
          picture: ''
        }
      }
    },
    addQuestionBankType () {
      this.questionBankTypeList.push({
        pkId: 0,
        name: ''
      })
      this.currentQuestionBankType = this.questionBankTypeList.length - 1
      this.isQuestionBankTypeEdit = true
    },
    addKnowledgePointType () {
      this.knowledgePointTypeList.push({
        pkId: 0,
        name: ''
      })
      this.currentKnowledgePointType = this.knowledgePointTypeList.length - 1
      this.isKnowledgePointTypeEdit = true
    },
    saveEditQuestionBankType (index) {
      if (this.$refs['questionBankTypeName' + index][0].value !== this.$refs['questionBankTypeName' + index][0].currentValue || !this.questionBankTypeList[index].pkId) {
        if (this.$refs['questionBankTypeName' + index][0].currentValue !== '') {
          let params = {
            pkId: this.questionBankTypeList[index].pkId,
            name: this.$refs['questionBankTypeName' + index][0].currentValue
          }
          let link = !this.questionBankTypeList[index].pkId ? API.questionBankType.add(params) : API.questionBankType.update(params)
          link.then(({ data }) => {
            if (data && data.code === 0) {
              this.getQuestionBankType()
              this.isQuestionBankTypeEdit = false
              this.$Message.success({
                content: data.msg
              })
            } else {
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.$Message.error('连接失败，请检查网络！')
          })
        } else {
          this.getQuestionBankType()
          this.isQuestionBankTypeEdit = false
        }
      }
    },
    saveEditKnowledgePointType (index) {
      if (this.$refs['knowledgePointTypeName' + index][0].value !== this.$refs['knowledgePointTypeName' + index][0].currentValue || !this.knowledgePointTypeList[index].pkId) {
        if (this.$refs['knowledgePointTypeName' + index][0].currentValue !== '') {
          let params = {
            pkId: this.knowledgePointTypeList[index].pkId,
            name: this.$refs['knowledgePointTypeName' + index][0].currentValue
          }
          let link = !this.knowledgePointTypeList[index].pkId ? API.knowledgePointType.add(params) : API.knowledgePointType.update(params)
          link.then(({ data }) => {
            if (data && data.code === 0) {
              this.getKnowledgePointType()
              this.isKnowledgePointTypeEdit = false
              this.$Message.success({
                content: data.msg
              })
            } else {
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.$Message.error('连接失败，请检查网络！')
          })
        } else {
          this.getKnowledgePointType()
          this.isKnowledgePointTypeEdit = false
        }
      }
    },
    saveKnowledgePointEdit () {
      this.$refs.knowledgePointForm.validate((valid) => {
        if (valid) {
          let params = {
            pkId: this.knowledgePointForm.pkId || undefined,
            typeId: this.knowledgePointForm.typeId,
            name: this.knowledgePointForm.name,
            authorId: this.$store.state.sys.userId
          }
          this.saveLoading = true
          let tick = !this.knowledgePointForm.pkId ? API.knowledgePoint.add(params) : API.knowledgePoint.update(params)
          tick.then(({ data }) => {
            if (data && data.code === 0) {
              this.saveLoading = false
              this.$Message.success({
                content: data.msg,
                onClose: () => {
                  this.addKnowledgePointModal = false
                  this.getKnowledgePoint()
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
    saveEdit () {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          let params = {
            pkId: this.editForm.pkId || undefined,
            typeId: this.editForm.typeId,
            name: this.editForm.name,
            des: this.editForm.des,
            picture: this.editForm.picture,
            authorId: this.$store.state.sys.userId
          }
          this.saveLoading = true
          let tick = !this.editForm.pkId ? API.questionBank.add(params) : API.questionBank.update(params)
          tick.then(({ data }) => {
            if (data && data.code === 0) {
              this.saveLoading = false
              this.$Message.success({
                content: data.msg,
                onClose: () => {
                  this.addModal = false
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
    deleteQuestionBankType (pkId) {
      let param = {
        pkId: pkId
      }
      this.$Modal.confirm({
        title: '提示',
        content: '<P>确定要删除吗</P>',
        onOk: () => {
          API.questionBankType.del(param).then(({ data }) => {
            if (data && data.code === 0) {
              this.getQuestionBankType()
              this.$Message.success({
                content: data.msg
              })
            } else {
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.$Message.error('连接失败，请检查网络!')
          })
        }
      })
    },
    deleteQuestionBank (params) {
      let questionBankIds = []
      if (typeof params === 'undefined') {
        questionBankIds = this.selection.map(c => {
          return c.pkId
        })
      } else {
        questionBankIds = [params.pkId]
      }
      if (questionBankIds.length === 0) {
        this.$Message.error('请选择题库')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<P>确定要删除吗</P>',
          onOk: () => {
            this.loading = true
            API.questionBank.del(questionBankIds).then(({ data }) => {
              if (data && data.code === 0) {
                this.init()
                this.$Message.success({
                  content: data.msg
                })
              } else {
                this.$Message.error(data.msg)
              }
            }).catch(() => {
              this.$Message.error('连接失败，请检查网络!')
            })
          }
        })
      }
    },
    deleteKnowledgePointType (pkId) {
      let param = {
        pkId: pkId
      }
      this.$Modal.confirm({
        title: '提示',
        content: '<P>确定要删除吗</P>',
        onOk: () => {
          API.knowledgePointType.del(param).then(({ data }) => {
            if (data && data.code === 0) {
              this.getKnowledgePointType()
              this.$Message.success({
                content: data.msg
              })
            } else {
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.$Message.error('连接失败，请检查网络!')
          })
        }
      })
    },
    deleteKnowledgePoint (pkId) {
      this.$Modal.confirm({
        title: '提示',
        content: '<P>确定要删除吗</P>',
        onOk: () => {
          API.knowledgePoint.del([pkId]).then(({ data }) => {
            if (data && data.code === 0) {
              this.getKnowledgePoint()
              this.$Message.success({
                content: data.msg
              })
            } else {
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.$Message.error('连接失败，请检查网络!')
          })
        }
      })
    },
    handleOssSuccess (res) { // 文件上传成功
      this.uploadLoading = false
      if (res.status === 'ok') {
        this.editForm.picture = this.fileName
      }
    },
    handleOssFormatError (file) { // 文件格式错误
      this.$Notice.warning({
        title: '文件格式错误',
        desc: '文件【 ' + file.name + ' 】格式不正确'
      })
    },
    handleOssMaxSize (file) { // 文件过大
      this.$Notice.warning({
        title: '文件大小超限',
        desc: '文件【' + file.name + '】过大'
      })
    },
    checkImageWH (file) {
      let self = this
      return new Promise(function (resolve) {
        let filereader = new FileReader()
        filereader.onload = e => {
          let src = e.target.result
          const image = new Image()
          image.onload = function () {
            if (self.GetPercent(this.width, this.height) === 1) {
              oss(file.name, 1).then(res => {
                self.uploadHost = res.host
                self.uploadData = res
              })
              resolve('success')
            } else {
              self.$Notice.warning({
                title: '请上传图片的尺寸比例为1:1',
                desc: '文件【' + file.name + '】尺寸比例不正确.'
              })
            }
          }
          image.src = src
        }
        filereader.readAsDataURL(file)
      })
    },
    GetPercent (width, height) {
      width = parseFloat(width)
      height = parseFloat(height)
      if (isNaN(width) || isNaN(height)) {
        return '-'
      }
      return height <= 0 ? '0%' : (Math.round(width / height * 100) / 100.00)
    },
    handleOssBeforeUpload (file) {
      // return this.checkImageWH(file)
      this.uploadLoading = true
      this.uploadFileName = file.name
      let vm = this
      return oss(file.name, 3).then(res => {
        vm.uploadHost = res.host
        vm.fileName = res.key
        vm.uploadData = res
      })
    },
    handleRemove () {
      this.editForm.picture = ''
    }
  },
  mounted () {
    this.init()
    this.getQuestionBankType()
  }
}
</script>
<style>
  .ivu-table-header {
    color: #909399;
  }
</style>
