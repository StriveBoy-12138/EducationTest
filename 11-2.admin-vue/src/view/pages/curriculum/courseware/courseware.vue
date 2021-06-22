<template>
  <div>
    <Card dis-hover :bordered="false">
      <Row style="margin-bottom: 10px;">
        <Col>
          <Button type="success" icon="md-add" @click="addButton()">创建课件</Button>
          <Button style="margin-left: 10px" type="primary" icon="md-create" @click="showCoursewareType">维护分类</Button>
          <Tooltip content="刷新" placement="bottom">
            <Button style="border-radius: 5px 0px 0px 5px;margin-left: 10px" icon="ios-refresh" @click="init"></Button>
          </Tooltip>
          <Tooltip content="帮助中心" placement="bottom">
            <Button style="border-radius: 0px;" icon="ios-help-circle"></Button>
          </Tooltip>
          <Button style="border-radius: 0px 5px 5px 0px;" type="success" icon="ios-trash-outline" @click="deleteCourseware()"></Button>
        </Col>
        <Col style="margin-left: 840px">
          <Select style="width: 150px" placeholder="请选择课件分类" v-model="typeId">
            <Option :value="Number(0)">全部</Option>
            <Option v-for="(item, index) in coursewareTypeList" :key="index" :value="item.pkId">{{item.name}}</Option>
          </Select>
          <Input style="width: 150px" v-model="title" placeholder="请输入课件名称"/>
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
              <Button type="primary" size="small" @click="addButton(scope.row)">编辑课件</Button>
              <Button type="error" size="small" @click="deleteCourseware(scope.row)">删除</Button>
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
    <Modal v-model="coursewareTypeModal" :closable='true' :mask-closable=false :width="600" @on-visible-change="resetCoursewareTypeData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">维护分类</h3>
      <Row style="margin-bottom: 10px">
        <Button type="primary" icon="md-add" @click="addCoursewareType">创建分类</Button>
        <ButtonGroup style="margin-left: 20px">
          <Button icon="ios-upload-outline">下载分类模版文件</Button>
          <Button type="success" icon="ios-download-outline">导入分类文件</Button>
        </ButtonGroup>
      </Row>
      <Row v-for="(coursewareType, index) in coursewareTypeList" :key="'coursewareType'+index" :style="'margin-left: 10px;background-color: '  + (currentCoursewareType === index ? '#d5e8fc':'')">
        <Input v-if="isCoursewareTypeEdit && currentCoursewareType === index" :ref="'coursewareTypeName'+index" :value="coursewareType.name" style="width: 100px" @on-blur="saveEditCoursewareType(index)"/>
        <span v-else style="cursor: pointer;" @mouseover="coursewareTypeMouseOver(index)" @mouseout="coursewareTypeMouseOut">
          {{coursewareType.name}}
          <ButtonGroup style="margin-left: 5px">
          <Button type="text" icon="md-create" @click="editCoursewareType()"></Button>
          <Button type="text" icon="ios-trash-outline" @click="deleteCoursewareType(coursewareType.pkId)"></Button>
        </ButtonGroup>
        </span>
      </Row>
      <div slot="footer">
        <Button @click="coursewareTypeModal = false">取消</Button>
      </div>
    </Modal>
    <!--添加/修改对话框-->
    <Modal v-model="addModal" :closable='true' :mask-closable=false :width="800" @on-visible-change="resetFormData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">{{!editForm.pkId?'创建课件':'编辑课件'}}</h3>
      <Form ref="editForm" :model="editForm" label-position="right" :label-width="100"
            :rules="rules" @submit.native.prevent="saveEdit">
        <FormItem label="课件分类" prop="typeId">
          <Select v-model="editForm.typeId" style="margin-right:5px;width: 150px;" placeholder="请选择课件分类">
            <Option v-for="item in coursewareTypeList" :value="item.pkId" :key="'chooseCoursewareType'+item.pkId">{{item.name}}</Option>
          </Select>
        </FormItem>
        <FormItem label="标题" prop="title">
          <Input v-model="editForm.title" style="width: 300px" :maxlength="32"/>
        </FormItem>
        <FormItem label="学习时长" prop="studyTime">
          <InputNumber v-model="editForm.minute" style="width: 100px" :min="0"/> 分
          <InputNumber v-model="editForm.second" style="width: 100px" :min="0"/> 秒
        </FormItem>
        <FormItem label="简介" prop="introduction">
          <Input v-model="editForm.introduction" type="textarea" :rows="2" placeholder="请输入简介" style="width: 665px" :maxlength="200"/>
        </FormItem>
        <FormItem label="课件类型" prop="coursewareType">
          <RadioGroup v-model="editForm.coursewareType" @on-change="coursewareTypeChange">
            <Radio :disabled="!!editForm.pkId" :label="Number(0)">图文</Radio>
            <Radio style="margin-left: 15px" :disabled="!!editForm.pkId" :label="Number(1)">文档</Radio>
            <Radio style="margin-left: 15px" :disabled="!!editForm.pkId" :label="Number(2)">图片</Radio>
            <Radio style="margin-left: 15px" :disabled="!!editForm.pkId" :label="Number(3)">音频</Radio>
            <Radio style="margin-left: 15px" :disabled="!!editForm.pkId" :label="Number(4)">视频</Radio>
            <Radio style="margin-left: 15px" :disabled="!!editForm.pkId" :label="Number(5)">外部链接</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem v-if="editForm.coursewareType === 0" label="正文" prop="content">
          <tinymce-editor :content.sync='editForm.content'></tinymce-editor>
        </FormItem>
        <FormItem v-else-if="editForm.coursewareType === 1" label="文档" prop="content">
          <Upload :action="uploadHost" ref="uploadOss" :show-upload-list="false" :data="uploadData"
                  :format="['doc','docx','xls','xlsx','ppt','pdf']"
                  :on-success="handleOssSuccess"
                  :max-size="51200"
                  :on-format-error="handleOssFormatError"
                  :on-exceeded-size="handleOssMaxSize"
                  :before-upload="handleOssBeforeUpload"
                  v-show="!editForm.pkId">
            <Button type="primary" :loading="uploadLoading">点击上传</Button>
          </Upload>
          <Row v-show="!editForm.pkId" style="margin-top: 10px">只能上传以.doc/.docx/.xls/.xlsx/.ppt/.pptx/.pdf结尾的文件，最大50MB，且只能上传一个</Row>
          <Row v-show="!editForm.pkId && editForm.content !== ''" style="margin-top: 10px" align="middle">
            <Icon type="ios-paper-outline" />
            <span style="margin-left: 5px">{{uploadFileName}}</span>
          </Row>
          <a :href="uploadHost + '/' + editForm.content">
            <Button v-show="!!editForm.pkId">下载预览</Button>
          </a>
        </FormItem>
        <FormItem v-else-if="editForm.coursewareType === 2" label="图片" prop="content">
          <Upload :action="uploadHost" ref="uploadOss" :show-upload-list="false" :data="uploadData"
                  :format="['jpg','jpeg','png']"
                  :on-success="handleOssSuccess"
                  :max-size="2048"
                  :on-format-error="handleOssFormatError"
                  :on-exceeded-size="handleOssMaxSize"
                  :before-upload="handleOssBeforeUpload"
                  v-show="!editForm.pkId">
            <div style="border: 1px dashed #d9d9d9;border-radius: 6px;cursor: pointer;width: 300px;height: 150px">
              <Icon v-if="editForm.content.length === 0" type="ios-add" size="40" style="color: #8c939d;position: absolute;top: 55px;left: 130px"></Icon>
              <div v-else style="width: 300px;height: 150px">
                <img style="width: 300px;height: 150px" :src="uploadHost + '/' + editForm.content">
              </div>
            </div>
          </Upload>
          <Row v-show="!editForm.pkId" style="margin-top: 10px">只能上传以.png/.jpeg/.jpg文件，最大2MB，且只能上传一张</Row>
          <Row v-show="!editForm.pkId && editForm.content !== ''" style="margin-top: 10px" align="middle">
            <Icon type="ios-paper-outline" />
            <span style="margin-left: 5px">{{uploadFileName}}</span>
          </Row>
          <img v-show="!!editForm.pkId" style="margin-top: 5px" :src="uploadHost + '/' + editForm.content">
        </FormItem>
        <FormItem v-else-if="editForm.coursewareType === 3" label="音频" prop="content">
          <Upload :action="uploadHost" ref="uploadOss" :show-upload-list="false" :data="uploadData"
                  :format="['mp3']"
                  :on-success="handleOssSuccess"
                  :max-size="51200"
                  :on-format-error="handleOssFormatError"
                  :on-exceeded-size="handleOssMaxSize"
                  :before-upload="handleOssBeforeUpload"
                  v-show="!editForm.pkId">
            <Button type="primary" :loading="uploadLoading">点击上传</Button>
          </Upload>
          <Row v-show="!editForm.pkId" style="margin-top: 10px">只能上传Mp3文件，最大50MB，且只能上传一个</Row>
          <Row v-show="!editForm.pkId && editForm.content !== ''" style="margin-top: 10px" align="middle">
            <Icon type="ios-paper-outline" />
            <span style="margin-left: 5px">{{uploadFileName}}</span>
          </Row>
          <aplayer v-show="!!editForm.pkId" style="margin-top: 5px" :playerOptions="{src: uploadHost + '/' +editForm.content}" />
        </FormItem>
        <FormItem v-else-if="editForm.coursewareType === 4" label="视频" prop="content">
          <Upload :action="uploadHost" ref="uploadOss" :show-upload-list="false" :data="uploadData"
                  :format="['mp4']"
                  :on-success="handleOssSuccess"
                  :max-size="512000"
                  :on-format-error="handleOssFormatError"
                  :on-exceeded-size="handleOssMaxSize"
                  :before-upload="handleOssBeforeUpload"
                  v-show="!editForm.pkId">
            <Button type="primary" :loading="uploadLoading">点击上传</Button>
          </Upload>
          <Row v-show="!editForm.pkId" style="margin-top: 10px">只能上传Mp4文件，最大500MB，且只能上传一个</Row>
          <Row v-show="!editForm.pkId && editForm.content !== ''" style="margin-top: 10px" align="middle">
            <Icon type="ios-paper-outline" />
            <span style="margin-left: 5px">{{uploadFileName}}</span>
          </Row>
          <vplayer v-show="!!editForm.pkId" style="margin-top: 10px" :playerOptions = "{src: uploadHost + '/' +editForm.content}" />
        </FormItem>
        <FormItem v-else-if="editForm.coursewareType === 5" label="外部链接" prop="content">
          <Input v-model="editForm.content" type="textarea" :rows="2" placeholder="请输入外部链接" style="width: 665px" :maxlength="200"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="cancel">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveEdit">提交</Button>
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
  name: 'courseware',
  components: { tinymceEditor, aplayer, vplayer },
  data () {
    const validateCoursewareStudyTime = (rule, value, callback) => {
      if (this.editForm.minute === 0 && this.editForm.second === 0) {
        callback(new Error('学习时长不能为空'))
      } else {
        callback()
      }
    }
    const validateCoursewareContent = (rule, value, callback) => {
      if (this.editForm.content === '') {
        let type = ''
        switch (this.editForm.coursewareType) {
          case 0 : type = '正文'; break
          case 1 : type = '文档'; break
          case 2 : type = '图片'; break
          case 3 : type = '音频'; break
          case 4 : type = '视频'; break
          case 5 : type = '外部链接'; break
        }
        callback(new Error(type + '不能为空'))
      } else {
        callback()
      }
    }
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
      timer: '',
      typeId: 0,
      title: '',
      loading: false,
      uploadLoading: false,
      saveLoading: false,
      coursewareTypeModal: false,
      addModal: false,
      isCoursewareTypeEdit: false,
      currentCoursewareType: -1,
      totalCount: 0,
      pageIndex: 1,
      pageSize: 10,
      coursewareTypeList: [],
      editForm: {
        pkId: 0,
        typeId: 0,
        title: '',
        minute: 0,
        second: 0,
        studyTime: 0,
        introduction: '',
        coursewareType: 0,
        size: 0,
        content: ''
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
          title: '课件名称',
          key: 'title',
          align: 'left',
          minWidth: 200
        },
        {
          title: '分类',
          key: 'typeName',
          align: 'center',
          minWidth: 150
        },
        {
          title: '时长',
          key: 'studyTime',
          align: 'center',
          width: 150,
          render: (h, params) => {
            let minute = parseInt(params.row.studyTime / 60)
            let second = params.row.studyTime % 60
            return h('Label', (minute < 10 ? ('0' + minute) : minute) + '分' + (second < 10 ? ('0' + second) : second) + '秒')
          }
        },
        {
          title: '大小(M)',
          key: 'size',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('Label', params.row.size + 'MB')
          }
        },
        {
          title: '课件类型',
          key: 'coursewareType',
          align: 'center',
          width: 100,
          render: (h, params) => {
            switch (params.row.coursewareType) {
              case 0:return h('Label', '图文')
              case 1:return h('Label', '文档')
              case 2:return h('Label', '图片')
              case 3:return h('Label', '音频')
              case 4:return h('Label', '视频')
              case 5:return h('Label', '外部链接')
            }
          }
        },
        {
          title: '更新时间',
          key: 'updateTime',
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
          width: 200
        }
      ],
      tableData: [],
      rules: {
        title: [{
          required: true,
          message: '标题不能为空',
          trigger: 'blur'
        }],
        studyTime: [{
          required: true,
          trigger: 'blur',
          validator: validateCoursewareStudyTime
        }],
        content: [{
          required: true,
          trigger: 'change',
          validator: validateCoursewareContent
        }]
      }
    }
  },
  watch: {
    $route: function (to, from) {
      if (to.name === 'courseware') {
        this.getCoursewareType()
        this.init()
      }
    }
  },
  methods: {
    init (reset) {
      if (reset === 'reset') {
        this.typeId = 0
        this.title = ''
      }
      this.loading = true
      let searchParams = {
        pageSize: this.pageSize,
        pageIndex: this.pageIndex,
        typeId: this.typeId,
        title: this.title
      }
      API.courseware.list(searchParams).then(({ data }) => {
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
    getCoursewareType () {
      API.coursewareType.list().then(({ data }) => {
        if (data && data.code === 0) {
          this.coursewareTypeList = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    coursewareTypeMouseOver (index) {
      if (!this.isCoursewareTypeEdit) {
        this.currentCoursewareType = index
      }
    },
    coursewareTypeMouseOut () {
      if (!this.isCoursewareTypeEdit) {
        this.currentCoursewareType = -1
      }
    },
    editCoursewareType () {
      this.isCoursewareTypeEdit = true
    },
    addButton (params) {
      if (typeof params !== 'undefined') { // 如果是修改状态，则填充必要数据
        this.editForm = JSON.parse(JSON.stringify(params))
        this.editForm.minute = parseInt(this.editForm.studyTime / 60)
        this.editForm.second = this.editForm.studyTime % 60
      }
      this.addModal = true
    },
    showCoursewareType () {
      this.getCoursewareType()
      this.coursewareTypeModal = true
    },
    selectionChange (data) {
      // 记录选中的数据
      this.selection = data
    },
    coursewareTypeChange () {
      this.editForm.size = 0
      this.editForm.content = ''
    },
    pageChange (val) {
      this.pageIndex = val
      this.init()
    },
    sizeChange (size) {
      this.pageSize = size
      this.init()
    },
    cancel () {
      this.addModal = false
    },
    resetCoursewareTypeData () {
      this.isCoursewareTypeEdit = false
      this.currentCoursewareType = -1
    },
    resetFormData (val) {
      if (!val) {
        this.editForm = {
          pkId: 0,
          typeId: 0,
          title: '',
          minute: 0,
          second: 0,
          studyTime: 0,
          introduction: '',
          coursewareType: 0,
          size: 0,
          content: ''
        }
      }
    },
    addCoursewareType () {
      this.coursewareTypeList.push({
        pkId: 0,
        name: ''
      })
      this.currentCoursewareType = this.coursewareTypeList.length - 1
      this.isCoursewareTypeEdit = true
    },
    saveEditCoursewareType (index) {
      if (this.$refs['coursewareTypeName' + index][0].value !== this.$refs['coursewareTypeName' + index][0].currentValue || !this.coursewareTypeList[index].pkId) {
        if (this.$refs['coursewareTypeName' + index][0].currentValue !== '') {
          let params = {
            pkId: this.coursewareTypeList[index].pkId,
            name: this.$refs['coursewareTypeName' + index][0].currentValue
          }
          let link = !this.coursewareTypeList[index].pkId ? API.coursewareType.add(params) : API.coursewareType.update(params)
          link.then(({ data }) => {
            if (data && data.code === 0) {
              this.getCoursewareType()
              this.isCoursewareTypeEdit = false
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
          this.getCoursewareType()
          this.isCoursewareTypeEdit = false
        }
      }
    },
    saveEdit () {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          let params = {
            pkId: this.editForm.pkId || undefined,
            typeId: this.editForm.typeId,
            title: this.editForm.title,
            studyTime: this.editForm.minute * 60 + this.editForm.second,
            introduction: this.editForm.introduction,
            coursewareType: this.editForm.coursewareType,
            size: this.editForm.size,
            content: this.editForm.content,
            authorId: this.$store.state.sys.userId
          }
          this.saveLoading = true
          let tick = !this.editForm.pkId ? API.courseware.add(params) : API.courseware.update(params)
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
    deleteCoursewareType (pkId) {
      let param = {
        pkId: pkId
      }
      this.$Modal.confirm({
        title: '提示',
        content: '<P>确定要删除吗</P>',
        onOk: () => {
          API.coursewareType.del(param).then(({ data }) => {
            if (data && data.code === 0) {
              this.getCoursewareType()
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
    deleteCourseware (params) {
      let coursewareIds = []
      if (typeof params === 'undefined') {
        coursewareIds = this.selection.map(c => {
          return c.pkId
        })
      } else {
        coursewareIds = [params.pkId]
      }
      if (coursewareIds.length === 0) {
        this.$Message.error('请选择课件')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<P>确定要删除吗</P>',
          onOk: () => {
            this.loading = true
            API.courseware.del(coursewareIds).then(({ data }) => {
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
    handleOssSuccess (res) { // 文件上传成功
      this.uploadLoading = false
      if (res.status === 'ok') {
        this.editForm.size = res.size / (1024 * 1024)
        this.editForm.content = this.fileName
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
    }
  },
  mounted () {
    this.init()
    this.getCoursewareType()
  }
}
</script>
<style>
  .ivu-table-header {
    color: #909399;
  }
</style>
