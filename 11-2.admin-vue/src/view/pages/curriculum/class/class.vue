<template>
  <div>
    <Card dis-hover :bordered="false">
      <Row style="margin-bottom: 10px;">
        <Col>
          <Button type="success" icon="md-add" @click="addButton()">创建课程</Button>
          <Button style="margin-left: 10px" type="primary" icon="md-create" @click="showClassType">维护分类</Button>
          <Tooltip content="刷新" placement="bottom">
            <Button style="border-radius: 5px 0px 0px 5px;margin-left: 10px" icon="ios-refresh" @click="init"></Button>
          </Tooltip>
          <Tooltip content="帮助中心" placement="bottom">
            <Button style="border-radius: 0px;" icon="ios-help-circle"></Button>
          </Tooltip>
          <Button style="border-radius: 0px 5px 5px 0px;" type="success" icon="ios-trash-outline" @click="deleteClass()"></Button>
        </Col>
        <Col style="margin-left: 840px">
          <Select style="width: 150px" placeholder="选择分类" v-model="typeId">
            <Option :value="Number(0)">全部</Option>
            <Option v-for="(item, index) in classTypeList" :key="index" :value="item.pkId">{{item.name}}</Option>
          </Select>
          <Input style="width: 150px" v-model="title" placeholder="请输入课程名称"/>
          <ButtonGroup>
            <Button style="background-color: #F5F7FA" @click="init">搜索</Button>
            <Button style="background-color: #F5F7FA" @click="init('reset')">重置</Button>
          </ButtonGroup>
        </Col>
      </Row>
      <Row>
        <Table ref="selection" :columns="columns" :data="tableData" :highlight-row="true" :current="pageIndex"
               @on-selection-change="selectionChange" :loading="loading">
          <template slot="action" slot-scope="scope">
            <Button type="primary" style="border-radius: 5px 0px 0px 5px;margin-right: 1px" size="small" @click="addButton(scope.row)">编辑</Button>
            <Button type="primary" style="border-radius: 0px;margin-right: 1px" size="small" @click="showClassSet(scope.row)">课程设置</Button>
            <Button type="primary" style="border-radius: 0px;margin-right: 1px" size="small" :disabled="scope.row.status === 2" @click="changeClassStatus(scope.row)">{{scope.row.status === 0 ? '发布课程' : '取消发布'}}</Button>
            <Button type="primary" style="border-radius: 0px;" size="small">学习进度</Button>
            <Dropdown placement="bottom-end" transfer @on-click="rowFunctionChoose">
              <Button style="border-radius: 0px 5px 5px 0px;" size="small">
                <Icon type="ios-arrow-down"/>
              </Button>
              <DropdownMenu slot="list">
                <DropdownItem :name="JSON.stringify({num: 1,class: scope.row})"><Icon type="ios-mail-outline" style="margin-right: 5px"/>发送消息</DropdownItem>
                <DropdownItem :name="JSON.stringify({num: 2,class: scope.row})"><Icon type="md-star" style="margin-right: 5px"/>收藏点赞记录</DropdownItem>
                <DropdownItem :name="JSON.stringify({num: 3,class: scope.row})"><Icon type="ios-paper-outline" style="margin-right: 5px"/>收费记录</DropdownItem>
                <DropdownItem :name="JSON.stringify({num: 4,class: scope.row})"><Icon type="ios-trash-outline" style="margin-right: 5px"/>删除</DropdownItem>
              </DropdownMenu>
            </Dropdown>
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
    <Modal v-model="classTypeModal" :closable='true' :mask-closable=false :width="600" @on-visible-change="resetClassTypeData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">维护分类</h3>
      <Row style="margin-bottom: 10px">
        <Button type="primary" icon="md-add" @click="addClassType">创建分类</Button>
        <ButtonGroup style="margin-left: 20px">
          <Button icon="ios-upload-outline">下载分类模版文件</Button>
          <Button type="success" icon="ios-download-outline">导入分类文件</Button>
        </ButtonGroup>
      </Row>
      <Row v-for="(classType, index) in classTypeList" :key="'classType'+index" :style="'margin-left: 10px;background-color: '  + (currentClassType === index ? '#d5e8fc':'')">
        <Input v-if="isClassTypeEdit && currentClassType === index" :ref="'classTypeName'+index" :value="classType.name" style="width: 100px" @on-blur="saveEditClassType(index)"/>
        <span v-else style="cursor: pointer;" @mouseover="classTypeMouseOver(index)" @mouseout="classTypeMouseOut">
          {{classType.name}}
          <ButtonGroup style="margin-left: 5px">
          <Button type="text" icon="md-create" @click="editClassType()"></Button>
          <Button type="text" icon="ios-trash-outline" @click="deleteClassType(classType.pkId)"></Button>
        </ButtonGroup>
        </span>
      </Row>
      <div slot="footer">
        <Button @click="classTypeModal = false">取消</Button>
      </div>
    </Modal>
    <!--添加/修改对话框-->
    <Modal v-model="addModal" :closable='true' :mask-closable=false :width="800" @on-visible-change="resetFormData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">{{!editForm.pkId?'创建课程':'编辑课程'}}</h3>
      <Form ref="editForm" :model="editForm" label-position="right" :label-width="130"
            :rules="rules" @submit.native.prevent="saveEdit">
        <FormItem label="分类" prop="typeId">
          <Select v-model="editForm.typeId" style="margin-right:5px;width: 150px;" placeholder="请选择分类">
            <Option v-for="item in classTypeList" :value="item.pkId" :key="'chooseClassType'+item.pkId">{{item.name}}</Option>
          </Select>
        </FormItem>
        <FormItem label="课程名称" prop="title">
          <Input v-model="editForm.title" style="width: 300px" :maxlength="32"/>
        </FormItem>
        <FormItem label="学分" prop="credit">
          <InputNumber v-model="editForm.credit" style="width: 150px" :min="1"/> 分
        </FormItem>
        <FormItem label="是否开启防作弊" prop="preventionCheat">
          <RadioGroup v-model="editForm.preventionCheat">
            <Radio :label="Number(1)">关闭</Radio>
            <Radio :label="Number(0)">开启</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="禁止拖动视频进度" prop="videoDrag">
          <RadioGroup v-model="editForm.videoDrag">
            <Radio :label="Number(1)">关闭</Radio>
            <Radio :label="Number(0)">开启</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="学分获取设置" prop="creditAcquisition">
          <RadioGroup v-model="editForm.creditAcquisition">
            <Radio :label="Number(0)">完成时获取学分</Radio>
            <Radio :label="Number(1)">不启用学分</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="开课时间" prop="studyTime">
          <DatePicker :value="editForm.startTime" type="datetime" placeholder="选择开始时间" style="width: 200px" @on-change="startTimeChange"></DatePicker> -
          <DatePicker :value="editForm.endTime" type="datetime" placeholder="选择结束时间" style="width: 200px" @on-change="endTimeChange"></DatePicker>
        </FormItem>
        <FormItem label="封面图片" prop="picture">
          <Upload :action="uploadHost" ref="uploadOss" :show-upload-list="false" :data="uploadData"
                  :format="['jpg','jpeg','png']"
                  :on-success="handleOssSuccess"
                  :max-size="2048"
                  :on-format-error="handleOssFormatError"
                  :on-exceeded-size="handleOssMaxSize"
                  :before-upload="handleOssBeforeUpload">
            <div style="border: 1px dashed #d9d9d9;border-radius: 6px;cursor: pointer;width: 178px;height: 178px">
              <Icon v-if="editForm.picture.length === 0" type="ios-add" size="40" style="color: #8c939d;position: absolute;top: 70px;left: 70px"></Icon>
              <div v-else style="width: 178px;height: 178px">
                <img style="width: 178px;height: 178px" :src="uploadHost + '/' + editForm.picture">
              </div>
            </div>
          </Upload>
          <Icon v-if="editForm.picture.length !== 0" type="md-close-circle" size="30" style="color: red;position: absolute;top: -10px;left: 160px;cursor: pointer" @click.native="handleRemove"></Icon>
        </FormItem>
        <FormItem label="课程描述" prop="des">
          <tinymce-editor :content.sync='editForm.des'></tinymce-editor>
        </FormItem>
        <FormItem label="课程收费" prop="classCharge">
          <RadioGroup v-model="editForm.classCharge">
            <Radio :label="Number(1)">不启用</Radio>
            <Radio :label="Number(0)">启用</Radio>
          </RadioGroup>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="cancel">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveEdit">提交</Button>
      </div>
    </Modal>
    <!--课程设置对话框-->
    <Modal v-model="classSetModal" :closable="true" :mask-closable="false" :width="1200" :footer-hide="false" @on-visible-change="resetFormData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">课程设置</h3>
      <Row style="height: 60px" align="middle">
        <span style="margin-left: 10px;">是否选择成员</span>
        <Button type="success" style="margin-left: 15px;width: 100px" @click="showInviteUser">请邀请成员</Button>
        <Button style="margin-left: 10px;color: #bd93f9" type="text" @click="showInvitedUser">已邀请列表</Button>
      </Row>
      <Row style="height: 60px;border-bottom: 1px solid #e8eaec;" align="middle">
        <span style="margin-left: 10px;">是否关联考试</span>
        <Button type="success" style="margin-left: 15px;width: 100px" @click="showSetPaper">请选择试卷</Button>
        <div v-if="!!editForm.paper">
          <span style="margin-left: 25px">试卷名称：{{editForm.paperName}}</span>
          <span style="margin-left: 20px;color: red;cursor: pointer" @click="saveEditClassPaper(0)">删除</span>
        </div>
      </Row>
      <Row style="height: 450px;margin-bottom: 0px">
        <div style="height: 450px;width: 584px;border-right: 2px solid #e8eaec;overflow: auto">
          <Row style="height: 40px" align="middle">
            <span style="margin-left: 175px;font-size: 15px">课程目录</span>
            <Button style="margin-left: 235px" type="success" icon="md-add" @click="addCatalog">添加目录</Button>
          </Row>
          <Row v-for="(catalog, index) in catalogList" :key="'catalog'+index" style="margin-top: 10px" align="middle">
            <Input :ref="'catalogTitle'+index" :value="catalog.title" style="width: 150px" @on-blur="saveEditCatalog(index)"/>
            <Button type="warning" shape="circle" style="margin-left: 10px" size="small" icon="ios-remove" @click="deleteCatalog(catalog.pkId)"></Button>
            <Button :disabled="index === 0" type="warning" shape="circle" style="margin-left: 10px" size="small" icon="ios-arrow-up" @click="moveCatalog(catalog, 'up')"></Button>
            <Button :disabled="(index+1) === catalogList.length" type="warning" shape="circle" style="margin-left: 10px" size="small" icon="ios-arrow-down" @click="moveCatalog(catalog, 'down')"></Button>
            <Radio style="margin-left: 10px" :ref="'catalogRadio'+index" border @on-change="getCheckCatalog(catalog)">维护课件</Radio>
          </Row>
        </div>
        <div style="height: 450px;width: 584px;overflow: auto">
          <Row style="height: 40px" align="middle">
            <span style="margin-left: 220px;font-size: 15px">课程课件</span>
            <Button style="margin-left: 190px" type="success" icon="md-add" :disabled="!catalogId" @click="addCourseware">添加课件</Button>
          </Row>
          <Row style="margin-left: 10px">
            <Table :columns="catalogCoursewareColumns" :data="catalogCoursewareTableData" :highlight-row="true" :key="timer" style="width: 100%" border :loading="catalogCoursewareTableLoading">
              <template slot="action" slot-scope="scope">
                <Button :disabled="scope.index === 0" type="warning" shape="circle" icon="ios-arrow-up" size="small" @click="moveCatalogCourseware(scope.row, 'up')"></Button>
                <Button :disabled="(scope.index+1) === catalogCoursewareTableData.length" type="warning" shape="circle" style="margin-left: 5px" size="small" icon="ios-arrow-down" @click="moveCatalogCourseware(scope.row, 'down')"></Button>
                <Button type="success" style="margin-left: 5px" size="small" @click="deleteCatalogCourseware(scope.row)">删除</Button>
              </template>
            </Table>
          </Row>
        </div>
      </Row>
      <div slot="footer" style="margin-top: 0px">
        <Button @click="classSetModal = false">取消</Button>
      </div>
    </Modal>
    <!--邀请成员-->
    <Modal v-model="inviteUserModal" :closable="true" :mask-closable="false" :width="850" @on-visible-change="tabPaneNumber = 0">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">团队成员列表</h3>
      <Tabs :animated="false" v-model="tabPaneNumber" @on-click="tabChange">
        <TabPane label="学员邀请">
          <Row>
            <Button icon="md-download">上传邀请名单</Button>
            <Button type="warning" style="margin-left: 5px">高级搜索</Button>
            <Dropdown trigger="click">
              <Input readonly suffix="ios-arrow-down" placeholder="请选择组织架构" style="width: 200px;margin-left: 5px" v-model="checkOrganization"/>
              <DropdownMenu slot="list">
                <Tree :data="selectOrganizationList" show-checkbox style="width: 200px" @on-check-change="getCheckOrganization"></Tree>
              </DropdownMenu>
            </Dropdown>
            <Input style="width: 130px" v-model="userAccount" placeholder="请输入账号"/>
            <Input style="width: 130px" v-model="userName" placeholder="请输入姓名"/>
            <ButtonGroup>
              <Button style="background-color: #F5F7FA" @click="getUser">搜索</Button>
              <Button style="background-color: #F5F7FA" @click="getUser('reset')">重置</Button>
            </ButtonGroup>
          </Row>
          <Row style="margin-top: 10px">
            <Table ref="userSelection" :columns="userColumns" :data="userTableData" :highlight-row="true" :current="userPageIndex" style="width: 100%;" :key="timer" border
                   @on-selection-change="userSelectionChange" :loading="userTableLoading">
            </Table>
          </Row>
          <Row style="margin-top: 10px" justify="end">
            <Page :total="userTotalCount" :current="userPageIndex" :page-sise="userPageSize" loading show-total show-sizer show-elevator
                  @on-change="userPageChange" @on-page-size-change="userSizeChange">
            </Page>
          </Row>
        </TabPane>
        <TabPane label="标签邀请">
          <Table ref="titleSelection" :columns="titleColumns" :data="titleTableData" :highlight-row="true" style="width: 100%;" :key="timer" border
                 @on-selection-change="titleSelectionChange" :loading="titleTableLoading">
          </Table>
        </TabPane>
        <TabPane label="组织架构邀请">
          <Tree :data="organizationList" show-checkbox style="width: 800px;margin-left: 10px" @on-check-change="getInviteOrganization"></Tree>
        </TabPane>
        <TabPane label="全部邀请">
          <span style="margin-left: 10px">是否全部邀请</span>
          <span :style="'margin-left: 10px;' + (inviteAll ? '':'color: #b3d4fc')">否</span>
          <i-switch style="margin-left: 5px" :disabled="!editForm.inviteAll" v-model="inviteAll" />
          <span :style="'margin-left: 5px;' + (inviteAll ? 'color: #b3d4fc':'')">是</span>
        </TabPane>
      </Tabs>
      <div slot="footer">
        <Button @click="inviteUserModal = false">关闭</Button>
        <Button style="margin-left: 10px" type="primary" :loading="saveLoading" @click="saveSetClassUser">确认</Button>
      </div>
    </Modal>
    <!--已邀请成员-->
    <Modal v-model="invitedUserModal" :closable="true" :mask-closable="false" :width="850">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">邀请列表</h3>
      <Tabs :animated="false" v-model="invitedTabPaneNumber">
        <TabPane :label="'已邀学员（'+invitedUserTotalCount+'）人'">
          <Row>
            <ButtonGroup style="margin-left: 5px">
              <Button icon="ios-trash-outline" :loading="saveLoading" @click="deleteInvitedUser()">删除</Button>
              <Button icon="ios-refresh" @click="getInvitedUser" />
            </ButtonGroup>
            <Button type="warning" style="margin-left: 220px">高级搜索</Button>
            <Input style="width: 130px;margin-left: 5px" v-model="invitedUserAccount" placeholder="请输入账号"/>
            <Input style="width: 130px" v-model="invitedUserName" placeholder="请输入姓名"/>
            <ButtonGroup>
              <Button style="background-color: #F5F7FA" @click="getInvitedUser">搜索</Button>
              <Button style="background-color: #F5F7FA" @click="getInvitedUser('reset')">重置</Button>
            </ButtonGroup>
          </Row>
          <Row style="margin-top: 10px">
            <Table ref="invitedUserSelection" :columns="invitedUserColumns" :data="invitedUserTableData" :highlight-row="true" :current="invitedUserPageIndex" :key="timer" style="width: 100%" border
                   @on-selection-change="invitedUserSelectionChange" :loading="invitedUserTableLoading">
            </Table>
          </Row>
          <Row style="margin-top: 10px" justify="end">
            <Page :total="invitedUserTotalCount" :current="invitedUserPageIndex" :page-sise="invitedUserPageSize" loading show-total show-sizer show-elevator
                  @on-change="invitedUserPageChange" @on-page-size-change="invitedUserSizeChange">
            </Page>
          </Row>
        </TabPane>
        <TabPane :label="'已邀标签（'+invitedTitleTableData.length+'）个'">
          <Row>
            <ButtonGroup style="margin-left: 5px">
              <Button icon="ios-trash-outline" :loading="saveLoading" @click="deleteInvitedTitle()">删除</Button>
              <Button icon="ios-refresh" @click="getInvitedTitle" />
            </ButtonGroup>
          </Row>
          <Row style="margin-top: 10px">
            <Table ref="invitedTitleSelection" :columns="invitedTitleColumns" :data="invitedTitleTableData" :highlight-row="true" style="width: 100%" :key="timer" border
                   @on-selection-change="invitedTitleSelectionChange" :loading="invitedTitleTableLoading">
            </Table>
          </Row>
        </TabPane>
        <TabPane :label="'已邀组织架构（'+invitedOrganizationTableData.length+'）个'">
          <Row>
            <ButtonGroup style="margin-left: 5px">
              <Button icon="ios-trash-outline" :loading="saveLoading" @click="deleteInvitedOrganization()">删除</Button>
              <Button icon="ios-refresh" @click="getInvitedOrganization" />
            </ButtonGroup>
          </Row>
          <Row style="margin-top: 10px">
            <Table ref="invitedOrganizationSelection" :columns="invitedOrganizationColumns" :data="invitedOrganizationTableData" :highlight-row="true" style="width: 100%" :key="timer" border
                   @on-selection-change="invitedOrganizationSelectionChange" :loading="invitedOrganizationTableLoading">
            </Table>
          </Row>
        </TabPane>
        <TabPane :label="'全部邀请（'+(inviteAll?1:0)+'）个'">
          <span style="margin-left: 10px">是否全部邀请</span>
          <span :style="'margin-left: 10px;' + (inviteAll ? '':'color: #b3d4fc')">否</span>
          <i-switch style="margin-left: 5px" :disabled="!!editForm.inviteAll" v-model="inviteAll" @on-change="saveEditClassUser(3)"/>
          <span :style="'margin-left: 5px;' + (inviteAll ? 'color: #b3d4fc':'')">是</span>
          <Row style="margin-left: 10px;margin-top: 20px">
            关闭选项将把已邀请删除
          </Row>
        </TabPane>
      </Tabs>
      <div slot="footer">
        <Button @click="invitedUserModal = false">关闭</Button>
      </div>
    </Modal>
    <!--选择试卷-->
    <Modal v-model="setPaperModal" :closable="true" :mask-closable="false" :width="850">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">试卷列表</h3>
      <Row style="border-bottom: 1px solid #e8eaec;height: 50px" align="middle" justify="end">
        <Input style="width: 130px" v-model="paperTitle" placeholder="搜索试卷名称"/>
        <ButtonGroup>
          <Button style="background-color: #F5F7FA" @click="getPaper">搜索</Button>
          <Button style="background-color: #F5F7FA" @click="getPaper('reset')">重置</Button>
        </ButtonGroup>
      </Row>
      <Row style="margin-top: 10px">
        <RadioGroup v-model="editForm.paper">
          <Table :columns="paperColumns" :data="paperTableData" :highlight-row="true" :current="paperPageIndex" style="width: 100%;" :key="timer" border :loading="paperTableLoading">
            <template slot="title" slot-scope="scope">
              <Radio :label="scope.row.pkId">{{scope.row.title}}</Radio>
            </template>
          </Table>
        </RadioGroup>
      </Row>
      <Row style="margin-top: 10px" justify="end">
        <Page :total="paperTotalCount" :current="paperPageIndex" :page-sise="paperPageSize" loading show-total show-sizer show-elevator
              @on-change="paperPageChange" @on-page-size-change="paperSizeChange">
        </Page>
      </Row>
      <div slot="footer">
        <Button @click="setPaperModal = false">关闭</Button>
        <Button style="margin-left: 10px" type="primary" :loading="saveLoading" @click="saveEditClassPaper(editForm.paper)">确认</Button>
      </div>
    </Modal>
    <!--添加课件-->
    <Modal v-model="coursewareModal" :closable="true" :mask-closable="false" :width="1200">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">选择课件</h3>
      <Row style="height: 500px;margin-bottom: 0px">
        <div style="height: 500px;width: 364px;border-right: 2px solid #e8eaec;overflow: auto">
          <Row style="height: 40px" align="middle">
            <span style="margin-left: 135px;font-size: 15px">课件分类</span>
          </Row>
          <Row>
            <Tree style="width: 100%" :data="coursewareTypeData" @on-select-change="getCourseware"/>
          </Row>
        </div>
        <div style="height: 500px;width: 804px;overflow: auto">
          <Row justify="end">
            <Input style="width: 200px" v-model="coursewareTitle" placeholder="请输入课件名称"/>
            <ButtonGroup>
              <Button style="background-color: #F5F7FA" @click="getCourseware()">搜索</Button>
              <Button style="background-color: #F5F7FA" @click="getCourseware('reset')">重置</Button>
            </ButtonGroup>
          </Row>
          <Row style="margin-top: 10px;margin-left: 10px">
            <Table ref="coursewareSelection" :columns="coursewareColumns" :data="coursewareTableData" :highlight-row="true" :current="coursewarePageIndex" style="width: 100%;" :key="timer" border
                   @on-selection-change="coursewareSelectionChange" :loading="coursewareTableLoading">
            </Table>
          </Row>
          <Row style="margin-top: 10px" justify="end">
            <Page :total="coursewareTotalCount" :current="coursewarePageIndex" :page-sise="coursewarePageSize" loading show-total show-sizer show-elevator
                  @on-change="coursewarePageChange" @on-page-size-change="coursewareSizeChange">
            </Page>
          </Row>
        </div>
      </Row>
      <div slot="footer">
        <Button @click="coursewareModal = false">关闭</Button>
        <Button style="margin-left: 10px" type="primary" :loading="saveLoading" @click="saveEditCatalogCourseware">确认</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import API from '@/api'
import tinymceEditor from '_c/text-editor/tinymce-editor.vue'
import { ossUrl } from '@/libs/ossUrl'
import { oss } from '@/libs/ossUtil'

export default {
  name: 'class',
  components: { tinymceEditor },
  data () {
    const validateClassDes = (rule, value, callback) => {
      if (this.editForm.des === '') {
        callback(new Error('课程描述不能为空'))
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
      timer: '',
      typeId: 0,
      title: '',
      userAccount: '',
      invitedUserAccount: '',
      userName: '',
      invitedUserName: '',
      paperTitle: '',
      coursewareTitle: '',
      checkOrganization: '',
      checkOrganizationList: [],
      inviteOrganizationList: [],
      inviteAll: false,
      loading: false,
      userTableLoading: false,
      invitedUserTableLoading: false,
      paperTableLoading: false,
      titleTableLoading: false,
      invitedTitleTableLoading: false,
      invitedOrganizationTableLoading: false,
      coursewareTableLoading: false,
      catalogCoursewareTableLoading: false,
      saveLoading: false,
      classTypeModal: false,
      addModal: false,
      classSetModal: false,
      inviteUserModal: false,
      invitedUserModal: false,
      setPaperModal: false,
      coursewareModal: false,
      isClassTypeEdit: false,
      tabPaneNumber: 0,
      invitedTabPaneNumber: 0,
      catalogId: 0,
      currentClassType: -1,
      totalCount: 0,
      userTotalCount: 0,
      invitedUserTotalCount: 0,
      paperTotalCount: 0,
      coursewareTotalCount: 0,
      pageIndex: 1,
      userPageIndex: 1,
      invitedUserPageIndex: 1,
      paperPageIndex: 1,
      coursewarePageIndex: 1,
      pageSize: 10,
      userPageSize: 10,
      invitedUserPageSize: 10,
      paperPageSize: 10,
      coursewarePageSize: 10,
      classTypeList: [],
      catalogList: [],
      selectOrganizationList: [],
      organizationList: [],
      coursewareTypeData: [
        {
          title: '我的课件',
          expand: true,
          children: []
        }
      ],
      editForm: {
        pkId: 0,
        typeId: 0,
        title: '',
        credit: 1,
        preventionCheat: 1,
        videoDrag: 1,
        creditAcquisition: 0,
        studyTime: '',
        startTime: '',
        endTime: '',
        picture: '',
        des: '',
        classCharge: 1
      },
      selection: [],
      userSelection: [],
      invitedUserSelection: [],
      invitedUserIds: [],
      titleSelection: [],
      invitedTitleSelection: [],
      coursewareSelection: [],
      invitedTitleIds: [],
      invitedOrganizationSelection: [],
      invitedOrganizationIds: [],
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
          title: '课程名称',
          key: 'title',
          align: 'left',
          minWidth: 150
        },
        {
          title: '学分',
          key: 'credit',
          align: 'center',
          minWidth: 80
        },
        {
          title: '防作弊',
          key: 'preventionCheat',
          align: 'center',
          minWidth: 100,
          render: (h, params) => {
            switch (params.row.preventionCheat) {
              case 0:return h('Label', '开启')
              case 1:return h('Label', '关闭')
            }
          }
        },
        {
          title: '分类',
          key: 'typeName',
          align: 'center',
          minWidth: 100
        },
        {
          title: '状态',
          key: 'status',
          align: 'center',
          minWidth: 100,
          render: (h, params) => {
            switch (params.row.status) {
              case 0:return h('Tag', { props: { color: 'red' } }, '待发布')
              case 1:return h('Tag', { props: { color: 'green' } }, '已发布')
              case 2:return h('Tag', { props: { color: 'green' } }, '已结课')
            }
          }
        },
        {
          title: '收藏次数',
          key: 'collectNum',
          align: 'center',
          minWidth: 100
        },
        {
          title: '点赞次数',
          key: 'upNum',
          align: 'center',
          minWidth: 100
        },
        {
          title: '开始时间',
          key: 'studyTime',
          align: 'center',
          minWidth: 170,
          render: (h, params) => {
            if (params.row.studyTime !== '') {
              let time = JSON.parse(params.row.studyTime)
              return h('Label', time.startTime)
            }
          }
        },
        {
          title: '结束时间',
          key: 'studyTime',
          align: 'center',
          minWidth: 170,
          render: (h, params) => {
            if (params.row.studyTime !== '') {
              let time = JSON.parse(params.row.studyTime)
              return h('Label', time.endTime)
            }
          }
        },
        {
          title: '发布时间',
          key: 'publishTime',
          align: 'center',
          minWidth: 170,
          render: (h, params) => {
            if (params.row.status === 0) {
              return h('Label', '')
            } else {
              return h('Label', params.row.publishTime)
            }
          }
        },
        {
          title: '更新时间',
          key: 'updateTime',
          align: 'center',
          minWidth: 170
        },
        {
          title: '创建人',
          key: 'authorName',
          align: 'center',
          minWidth: 100
        },
        {
          title: '操作',
          slot: 'action',
          align: 'center',
          minWidth: 330,
          fixed: 'right'
        }
      ],
      userColumns: [
        {
          type: 'index',
          title: ' ',
          width: 50,
          align: 'center'
        },
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '姓名',
          key: 'nickname',
          align: 'center',
          minWidth: 120
        },
        {
          title: '帐号',
          key: 'account',
          align: 'center',
          minWidth: 150
        },
        {
          title: '组织架构',
          key: 'organizationName',
          align: 'center',
          minWidth: 150
        }
      ],
      invitedUserColumns: [
        {
          type: 'index',
          title: ' ',
          width: 50,
          align: 'center'
        },
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '姓名',
          key: 'nickname',
          align: 'center',
          minWidth: 200
        },
        {
          title: '帐号',
          key: 'account',
          align: 'center',
          minWidth: 200
        },
        {
          title: '操作',
          key: 'action',
          align: 'center',
          width: 150,
          render: (h, params) => {
            let deleteBtn = h('Button', {
              props: {
                type: 'error',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              on: {
                click: () => {
                  this.deleteInvitedUser(params.row)
                }
              }
            }, '删除')
            return h('div', [
              deleteBtn
            ])
          }
        }
      ],
      paperColumns: [
        {
          type: 'index',
          title: ' ',
          width: 60,
          align: 'center'
        },
        {
          title: '试卷名称',
          slot: 'title',
          align: 'left',
          width: 410
        },
        {
          title: '创建时间',
          key: 'createTime',
          align: 'center',
          width: 350
        }
      ],
      titleColumns: [
        {
          type: 'index',
          title: ' ',
          width: 50,
          align: 'center'
        },
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '名称',
          key: 'name',
          align: 'center',
          minWidth: 120
        }
      ],
      invitedTitleColumns: [
        {
          type: 'index',
          title: ' ',
          width: 50,
          align: 'center'
        },
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '名称',
          key: 'name',
          align: 'center',
          minWidth: 150
        },
        {
          title: '操作',
          key: 'action',
          align: 'center',
          width: 150,
          render: (h, params) => {
            let deleteBtn = h('Button', {
              props: {
                type: 'error',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              on: {
                click: () => {
                  this.deleteInvitedTitle(params.row)
                }
              }
            }, '删除')
            return h('div', [
              deleteBtn
            ])
          }
        }
      ],
      invitedOrganizationColumns: [
        {
          type: 'index',
          title: ' ',
          width: 50,
          align: 'center'
        },
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '名称',
          key: 'title',
          align: 'center',
          minWidth: 150
        },
        {
          title: '操作',
          key: 'action',
          align: 'center',
          width: 150,
          render: (h, params) => {
            let deleteBtn = h('Button', {
              props: {
                type: 'error',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              on: {
                click: () => {
                  this.deleteInvitedOrganization(params.row)
                }
              }
            }, '删除')
            return h('div', [
              deleteBtn
            ])
          }
        }
      ],
      coursewareColumns: [
        {
          type: 'index',
          title: ' ',
          width: 50,
          align: 'center'
        },
        {
          type: 'selection',
          width: 60,
          align: 'center'
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
          minWidth: 100,
          render: (h, params) => {
            let minute = parseInt(params.row.studyTime / 60)
            let second = params.row.studyTime % 60
            return h('Label', (minute < 10 ? ('0' + minute) : minute) + '分' + (second < 10 ? ('0' + second) : second) + '秒')
          }
        },
        {
          title: '创建时间',
          key: 'createTime',
          align: 'center',
          minWidth: 170
        }
      ],
      catalogCoursewareColumns: [
        {
          title: '课件名称',
          key: 'coursewareTitle',
          align: 'left',
          minWidth: 200
        },
        {
          title: '时长',
          key: 'studyTime',
          align: 'center',
          minWidth: 100,
          render: (h, params) => {
            let minute = parseInt(params.row.studyTime / 60)
            let second = params.row.studyTime % 60
            return h('Label', (minute < 10 ? ('0' + minute) : minute) + '分' + (second < 10 ? ('0' + second) : second) + '秒')
          }
        },
        {
          title: '评论次数',
          key: 'commentTime',
          align: 'center',
          minWidth: 100,
          render: (h, params) => {
            let commentTime = params.row.commentTime
            return h('Label', !commentTime ? '暂无' : commentTime)
          }
        },
        {
          title: '操作',
          slot: 'action',
          align: 'center',
          width: 150
        }
      ],
      tableData: [],
      userTableData: [],
      invitedUserTableData: [],
      paperTableData: [],
      titleTableData: [],
      invitedTitleTableData: [],
      invitedOrganizationTableData: [],
      coursewareTableData: [],
      catalogCoursewareTableData: [],
      rules: {
        title: [{
          required: true,
          message: '课程名称不能为空',
          trigger: 'blur'
        }],
        des: [{
          required: true,
          trigger: 'change',
          validator: validateClassDes
        }
        ]
      }
    }
  },
  watch: {
    $route: function (to, from) {
      if (to.name === 'class') {
        this.getClassType()
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
      API.classManage.list(searchParams).then(({ data }) => {
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
    getClassType () {
      API.classType.list().then(({ data }) => {
        if (data && data.code === 0) {
          this.classTypeList = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getCatalog () {
      let params = {
        classId: this.editForm.pkId
      }
      API.catalog.list(params).then(({ data }) => {
        if (data && data.code === 0) {
          this.catalogList = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getCheckCatalog (catalog) {
      this.catalogList.forEach((item, index) => {
        if (item.pkId !== catalog.pkId) {
          this.$refs['catalogRadio' + index][0].currentValue = false
        }
      })
      this.catalogId = catalog.pkId
      this.getCatalogCourseware()
    },
    getUser (reset) { // 数据初始化、搜索
      if (reset === 'reset') {
        this.userPageIndex = 1
        this.userPageSize = 10
        this.checkOrganizationList = []
        this.userAccount = ''
        this.userName = ''
        this.getOrganization()
      }
      this.userTableLoading = true
      let searchParams = {
        pageSize: this.userPageSize,
        page: this.userPageIndex,
        organizationIds: this.checkOrganizationList.map(organization => { return organization.pkId }),
        userAccount: this.userAccount,
        userName: this.userName,
        exceptUserIds: this.editForm.inviteUser.split(',')
      }
      API.manager.list(searchParams).then(({ data }) => {
        this.userTableLoading = false
        if (data && data.code === 0) {
          this.userTableData = data.data.list
          this.userTotalCount = data.data.totalCount
        } else {
          this.userTableLoading = false
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.userTableLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getPaper (reset) {
      if (reset === 'reset') {
        this.paperTitle = ''
      }
      this.paperTableLoading = true
      let searchParams = {
        pageSize: this.paperPageSize,
        pageIndex: this.paperPageIndex,
        title: this.paperTitle
      }
      API.paper.list(searchParams).then(({ data }) => {
        this.paperTableLoading = false
        if (data && data.code === 0) {
          this.paperTableData = data.data.list
          this.paperTotalCount = data.data.totalCount
          this.timer = new Date().getTime()
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.paperTableLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getInvitedUser (reset) { // 数据初始化、搜索
      if (reset === 'reset') {
        this.invitedUserPageIndex = 1
        this.invitedUserPageSize = 10
        this.invitedUserAccount = ''
        this.invitedUserName = ''
      }
      this.invitedUserTableLoading = true
      let searchParams = {
        pageSize: this.invitedUserPageSize,
        page: this.invitedUserPageIndex,
        userIds: this.editForm.inviteUser.split(','),
        userAccount: this.invitedUserAccount,
        userName: this.invitedUserName
      }
      API.manager.inviteList(searchParams).then(({ data }) => {
        this.invitedUserTableLoading = false
        if (data && data.code === 0) {
          this.invitedUserTableData = data.data.list
          this.invitedUserTotalCount = data.data.totalCount
          this.timer = new Date().getTime()
        } else {
          this.invitedUserTableLoading = false
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.invitedUserTableLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getTitle () {
      this.titleTableLoading = true
      API.title.list().then(({ data }) => {
        this.titleTableLoading = false
        if (data && data.code === 0) {
          this.titleTableData = data.data
        } else {
          this.titleTableLoading = false
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.titleTableLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getInvitedTitle () {
      this.invitedTitleTableLoading = true
      API.title.inviteList({ titleIds: this.editForm.inviteTitle.split(',') }).then(({ data }) => {
        this.invitedTitleTableLoading = false
        if (data && data.code === 0) {
          this.invitedTitleTableData = data.data
          this.timer = new Date().getTime()
        } else {
          this.invitedTitleTableLoading = false
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.invitedTitleTableLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getOrganization () {
      API.organization.list().then(({ data }) => {
        if (data && data.code === 0) {
          this.selectOrganizationList = data.data
          this.organizationList = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getInvitedOrganization () {
      this.invitedOrganizationTableLoading = true
      API.organization.inviteList({ organizationIds: this.editForm.inviteOrganization.split(',') }).then(({ data }) => {
        this.invitedOrganizationTableLoading = false
        if (data && data.code === 0) {
          this.invitedOrganizationTableData = data.data
          this.timer = new Date().getTime()
        } else {
          this.invitedOrganizationTableLoading = false
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.invitedOrganizationTableLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getCoursewareType () {
      API.coursewareType.list().then(({ data }) => {
        if (data && data.code === 0) {
          this.coursewareTypeData[0].children = data.data.map(type => {
            type.title = type.name
            type.expand = false
            type.children = []
            return type
          })
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getCourseware (reset) { // 数据初始化、搜索
      if (reset === 'reset') {
        this.coursewarePageIndex = 1
        this.coursewarePageSize = 10
        this.coursewareTitle = ''
      }
      let typeId = 0
      if (typeof reset === 'object' && reset[0].nodeKey !== 0) {
        typeId = reset[0].pkId
      }
      this.coursewareTableLoading = true
      let searchParams = {
        pageSize: this.coursewarePageSize,
        page: this.coursewarePageIndex,
        typeId: typeId,
        title: this.coursewareTitle
      }
      API.courseware.list(searchParams).then(({ data }) => {
        this.coursewareTableLoading = false
        if (data && data.code === 0) {
          this.coursewareTableData = data.data.list
          this.coursewareTotalCount = data.data.totalCount
          setTimeout(() => {
            Object.values(this.$refs.coursewareSelection.objData).forEach((courseware) => {
              this.catalogCoursewareTableData.forEach((catalogCourseware) => {
                if (courseware.pkId === catalogCourseware.coursewareId) {
                  this.coursewareSelection.push(courseware)
                  courseware._isChecked = true
                }
              })
            })
          }, 200)
        } else {
          this.coursewareTableLoading = false
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.coursewareTableLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getCatalogCourseware () {
      let params = {
        catalogId: this.catalogId
      }
      this.catalogCoursewareTableLoading = true
      API.catalogCourseware.list(params).then(({ data }) => {
        this.catalogCoursewareTableLoading = false
        if (data && data.code === 0) {
          this.catalogCoursewareTableData = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.catalogCoursewareTableLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    classTypeMouseOver (index) {
      if (!this.isClassTypeEdit) {
        this.currentClassType = index
      }
    },
    classTypeMouseOut () {
      if (!this.isClassTypeEdit) {
        this.currentClassType = -1
      }
    },
    editClassType () {
      this.isClassTypeEdit = true
    },
    addButton (params) {
      if (typeof params !== 'undefined') { // 如果是修改状态，则填充必要数据
        this.editForm = JSON.parse(JSON.stringify(params))
        let studyTime = JSON.parse(this.editForm.studyTime)
        this.editForm.startTime = studyTime.startTime
        this.editForm.endTime = studyTime.endTime
      }
      this.addModal = true
    },
    showClassType () {
      this.getClassType()
      this.classTypeModal = true
    },
    showClassSet (params) {
      if (typeof params !== 'undefined') {
        this.editForm = JSON.parse(JSON.stringify(params))
        this.inviteAll = !this.editForm.inviteAll
      }
      this.getCatalog()
      this.classSetModal = true
    },
    showInviteUser () {
      this.getUser('reset')
      this.getOrganization()
      this.getTitle()
      this.userSelection = []
      this.titleSelection = []
      this.inviteAll = !this.editForm.inviteAll
      this.inviteUserModal = true
    },
    showInvitedUser () {
      this.invitedTabPaneNumber = 0
      this.invitedUserTotalCount = 0
      this.titleTableData = []
      if (this.editForm.invitedUser !== '') {
        this.getInvitedUser()
      }
      if (this.editForm.invitedTitle !== '') {
        this.getInvitedTitle()
      }
      if (this.editForm.invitedOrganization !== '') {
        this.getInvitedOrganization()
      }
      this.inviteAll = !this.editForm.inviteAll
      this.invitedUserModal = true
    },
    showSetPaper () {
      this.getPaper('reset')
      this.setPaperModal = true
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
    getInviteOrganization (list, data) {
      this.inviteOrganizationList = list
    },
    rowFunctionChoose (value) {
      let json = JSON.parse(value)
      switch (json.num) {
        case 1 : console.log(json.num); break
        case 2 : console.log(json.num); break
        case 3 : console.log(json.num); break
        case 4 : this.deleteClass(json.class); break
      }
    },
    selectionChange (data) {
      // 记录选中的数据
      this.selection = data
    },
    userSelectionChange (data) {
      this.userSelection = data
    },
    invitedUserSelectionChange (data) {
      this.invitedUserSelection = data
    },
    titleSelectionChange (data) {
      this.titleSelection = data
    },
    invitedTitleSelectionChange (data) {
      this.invitedTitleSelection = data
    },
    invitedOrganizationSelectionChange (data) {
      this.invitedOrganizationSelection = data
    },
    coursewareSelectionChange (data) {
      this.coursewareSelection = data
    },
    tabChange (value) {
      this.userSelection = []
      this.titleSelection = []
      this.inviteOrganizationList = []
      this.timer = new Date().getTime()
      this.getOrganization()
    },
    pageChange (val) {
      this.pageIndex = val
      this.init()
    },
    userPageChange (val) {
      this.userPageIndex = val
      this.getUser()
    },
    invitedUserPageChange (val) {
      this.invitedUserPageIndex = val
      this.getUser()
    },
    paperPageChange (val) {
      this.paperPageIndex = val
      this.getPaper()
    },
    coursewarePageChange (val) {
      this.coursewarePageIndex = val
      this.getCourseware()
    },
    sizeChange (size) {
      this.pageSize = size
      this.init()
    },
    userSizeChange (size) {
      this.userPageSize = size
      this.getUser()
    },
    invitedUserSizeChange (size) {
      this.invitedUserPageSize = size
      this.getInvitedUser()
    },
    paperSizeChange (size) {
      this.paperPageSize = size
      this.getPaper()
    },
    coursewareSizeChange (size) {
      this.coursewarePageSize = size
      this.getCourseware()
    },
    cancel () {
      this.addModal = false
    },
    resetClassTypeData () {
      this.isClassTypeEdit = false
      this.currentClassType = -1
    },
    resetFormData (val) {
      if (!val) {
        for (var i in this.catalogList) {
          this.$refs['catalogRadio' + i][0].currentValue = false
        }
        this.catalogId = 0
        this.catalogCoursewareTableData = []
        this.editForm = {
          pkId: 0,
          typeId: 0,
          title: '',
          credit: 1,
          preventionCheat: 1,
          videoDrag: 1,
          creditAcquisition: 0,
          studyTime: '',
          startTime: '',
          endTime: '',
          picture: '',
          des: '',
          classCharge: 1
        }
      }
    },
    startTimeChange (value) {
      this.editForm.startTime = value
    },
    endTimeChange (value) {
      this.editForm.endTime = value
    },
    addCatalog () {
      this.catalogList.push({
        pkId: 0,
        classId: this.editForm.pkId,
        title: ''
      })
    },
    addCourseware () {
      this.getCoursewareType()
      this.getCourseware()
      this.coursewareModal = true
    },
    addClassType () {
      this.classTypeList.push({
        pkId: 0,
        name: ''
      })
      this.currentClassType = this.classTypeList.length - 1
      this.isClassTypeEdit = true
    },
    saveEditClassType (index) {
      if (this.$refs['classTypeName' + index][0].value !== this.$refs['classTypeName' + index][0].currentValue || !this.classTypeList[index].pkId) {
        if (this.$refs['classTypeName' + index][0].currentValue !== '') {
          let params = {
            pkId: this.classTypeList[index].pkId,
            name: this.$refs['classTypeName' + index][0].currentValue
          }
          let link = !this.classTypeList[index].pkId ? API.classType.add(params) : API.classType.update(params)
          link.then(({ data }) => {
            if (data && data.code === 0) {
              this.getClassType()
              this.isClassTypeEdit = false
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
          this.getClassType()
          this.isClassTypeEdit = false
        }
      }
    },
    saveEditCatalog (index) {
      if (this.$refs['catalogTitle' + index][0].value !== this.$refs['catalogTitle' + index][0].currentValue) {
        let params = {
          pkId: this.catalogList[index].pkId,
          classId: this.catalogList[index].classId,
          title: this.$refs['catalogTitle' + index][0].currentValue
        }
        let tick = !this.catalogList[index].pkId ? API.catalog.add(params) : API.catalog.update(params)
        tick.then(({ data }) => {
          if (data && data.code === 0) {
            this.$Message.success({
              content: data.msg,
              onClose: () => {
                this.getCatalog()
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
    saveEditCatalogCourseware () {
      let catalogCoursewareList = []
      this.coursewareSelection.forEach((courseware) => {
        let catalogCourseware = {
          catalogId: this.catalogId,
          coursewareId: courseware.pkId
        }
        catalogCoursewareList.push(catalogCourseware)
      })
      this.saveLoading = true
      API.catalogCourseware.add(catalogCoursewareList).then(({ data }) => {
        if (data && data.code === 0) {
          this.$Message.success({
            content: data.msg,
            onClose: () => {
              this.saveLoading = false
              this.coursewareModal = false
              this.getCatalogCourseware()
            }
          })
        } else {
          this.saveLoading = false
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    saveEdit () {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          let studyTime = {
            startTime: this.editForm.startTime,
            endTime: this.editForm.endTime
          }
          let params = {
            pkId: this.editForm.pkId || undefined,
            typeId: this.editForm.typeId,
            title: this.editForm.title,
            credit: this.editForm.credit,
            preventionCheat: this.editForm.preventionCheat,
            videoDrag: this.editForm.videoDrag,
            creditAcquisition: this.editForm.creditAcquisition,
            studyTime: JSON.stringify(studyTime),
            picture: this.editForm.picture,
            des: this.editForm.des,
            classCharge: this.editForm.classCharge,
            authorId: this.$store.state.sys.userId
          }
          this.saveLoading = true
          let tick = !this.editForm.pkId ? API.classManage.add(params) : API.classManage.update(params)
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
    saveSetClassUser () {
      let inviteUser = ''
      let inviteTitle = ''
      let inviteOrganization = ''
      this.userSelection.forEach((user, index) => {
        if (!index) {
          inviteUser = user.pkId
        } else {
          inviteUser = inviteUser + ',' + user.pkId
        }
      })
      this.titleSelection.forEach((title, index) => {
        if (!index) {
          inviteTitle = title.pkId
        } else {
          inviteTitle = inviteTitle + ',' + title.pkId
        }
      })
      this.inviteOrganizationList.forEach((organization, index) => {
        if (!index) {
          inviteOrganization = organization.pkId
        } else {
          inviteOrganization = inviteOrganization + ',' + organization.pkId
        }
      })
      let params = {
        pkId: this.editForm.pkId,
        inviteUser: inviteUser,
        inviteTitle: inviteTitle,
        inviteOrganization: inviteOrganization,
        inviteAll: this.inviteAll ? 0 : 1
      }
      this.saveLoading = true
      API.classManage.setUser(params).then(({ data }) => {
        if (data && data.code === 0) {
          this.$Message.success({
            content: data.msg,
            onClose: () => {
              this.saveLoading = false
              this.inviteUserModal = false
              this.tabPaneNumber = 0
              this.editForm = data.data
              this.inviteAll = !this.editForm.inviteAll
              this.init()
            }
          })
        } else {
          this.saveLoading = false
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    saveEditClassUser (type) {
      let invitedUser = ''
      let invitedTitle = ''
      let invitedOrganization = ''
      this.invitedUserIds.forEach((userId, index) => {
        if (!index) {
          invitedUser = userId
        } else {
          invitedUser = invitedUser + ',' + userId
        }
      })
      this.invitedTitleIds.forEach((titleId, index) => {
        if (!index) {
          invitedTitle = titleId
        } else {
          invitedTitle = invitedTitle + ',' + titleId
        }
      })
      this.invitedOrganizationIds.forEach((organizationId, index) => {
        if (!index) {
          invitedOrganization = organizationId
        } else {
          invitedOrganization = invitedOrganization + ',' + organizationId
        }
      })
      let params = {
        pkId: this.editForm.pkId,
        inviteUser: invitedUser,
        inviteTitle: invitedTitle,
        inviteOrganization: invitedOrganization,
        inviteAll: 1,
        isSet: false
      }
      this.saveLoading = true
      API.classManage.setUser(params).then(({ data }) => {
        if (data && data.code === 0) {
          this.$Message.success({
            content: data.msg,
            onClose: () => {
              this.saveLoading = false
              this.invitedUserSelection = []
              this.invitedTitleSelection = []
              this.invitedOrganizationSelection = []
              this.editForm = data.data
              this.inviteAll = !this.editForm.inviteAll
              this.init()
              switch (type) {
                case 0 : this.getInvitedUser(); break
                case 1 : this.getInvitedTitle(); break
                case 2 : this.getInvitedOrganization(); break
              }
            }
          })
        } else {
          this.saveLoading = false
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    saveEditClassPaper (paperId) {
      let params = {
        pkId: this.editForm.pkId,
        paper: paperId
      }
      this.saveLoading = true
      if (!paperId) {
        this.$Modal.confirm({
          title: '提示',
          content: '<P>确定要删除吗</P>',
          onOk: () => {
            API.classManage.setPaper(params).then(({ data }) => {
              if (data && data.code === 0) {
                this.$Message.success({
                  content: data.msg,
                  onClose: () => {
                    this.saveLoading = false
                    this.setPaperModal = false
                    this.editForm = data.data
                    this.init()
                  }
                })
              } else {
                this.saveLoading = false
                this.$Message.error(data.msg)
              }
            }).catch(() => {
              this.$Message.error('连接失败，请检查网络！')
            })
          }
        })
      } else {
        API.classManage.setPaper(params).then(({ data }) => {
          if (data && data.code === 0) {
            this.$Message.success({
              content: data.msg,
              onClose: () => {
                this.saveLoading = false
                this.setPaperModal = false
                this.editForm = data.data
                this.init()
              }
            })
          } else {
            this.saveLoading = false
            this.$Message.error(data.msg)
          }
        }).catch(() => {
          this.$Message.error('连接失败，请检查网络！')
        })
      }
    },
    changeClassStatus (value) {
      let param = {
        pkId: value.pkId,
        status: !value.status ? 1 : 0
      }
      let title = !value.status ? '发布' : '取消发布'
      this.$Modal.confirm({
        title: title,
        content: '<P>确定要' + title + '课程' + value.title + '吗</P>',
        onOk: () => {
          this.loading = true
          API.classManage.status(param).then(({ data }) => {
            if (data && data.code === 0) {
              this.init()
              this.$Message.success({
                content: data.msg
              })
            } else {
              this.loading = false
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.$Message.error('连接失败，请检查网络!')
          })
        }
      })
    },
    deleteClassType (pkId) {
      let param = {
        pkId: pkId
      }
      this.$Modal.confirm({
        title: '提示',
        content: '<P>确定要删除吗</P>',
        onOk: () => {
          API.classType.del(param).then(({ data }) => {
            if (data && data.code === 0) {
              this.getClassType()
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
    deleteCatalog (pkId) {
      let param = {
        pkId: pkId
      }
      this.$Modal.confirm({
        title: '提示',
        content: '<p>确定要删除么</p>',
        onOk: () => {
          API.catalog.del(param).then(({ data }) => {
            if (data && data.code === 0) {
              this.$Message.success({
                content: data.msg,
                onClose: () => {
                  this.getCatalog()
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
    deleteClass (params) {
      let classIds = []
      if (typeof params === 'undefined') {
        classIds = this.selection.map(c => {
          return c.pkId
        })
      } else {
        classIds = [params.pkId]
      }
      if (classIds.length === 0) {
        this.$Message.error('请选择课程')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<P>确定要删除吗</P>',
          onOk: () => {
            this.loading = true
            API.classManage.del(classIds).then(({ data }) => {
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
    deleteInvitedUser (params) {
      let removeUserIds = []
      if (typeof params === 'undefined') {
        removeUserIds = this.invitedUserSelection.map(user => {
          return user.pkId.toString()
        })
      } else {
        removeUserIds = [params.pkId.toString()]
      }
      if (removeUserIds.length === 0) {
        this.$Message.error('请至少选择一条数据')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<P>确定要删除吗</P>',
          onOk: () => {
            let userIds = this.editForm.inviteUser.split(',')
            this.invitedUserIds = userIds.filter((id) => { return removeUserIds.indexOf(id) < 0 })
            this.saveEditClassUser(0)
          }
        })
      }
    },
    deleteInvitedTitle (params) {
      let removeTitleIds = []
      if (typeof params === 'undefined') {
        removeTitleIds = this.invitedTitleSelection.map(title => {
          return title.pkId.toString()
        })
      } else {
        removeTitleIds = [params.pkId.toString()]
      }
      if (removeTitleIds.length === 0) {
        this.$Message.error('请至少选择一条数据')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<P>确定要删除吗</P>',
          onOk: () => {
            let titleIds = this.editForm.inviteTitle.split(',')
            this.invitedTitleIds = titleIds.filter((id) => { return removeTitleIds.indexOf(id) < 0 })
            this.saveEditClassUser(1)
          }
        })
      }
    },
    deleteInvitedOrganization (params) {
      let removeOrganizationIds = []
      if (typeof params === 'undefined') {
        removeOrganizationIds = this.invitedOrganizationSelection.map(organization => {
          return organization.pkId.toString()
        })
      } else {
        removeOrganizationIds = [params.pkId.toString()]
      }
      if (removeOrganizationIds.length === 0) {
        this.$Message.error('请至少选择一条数据')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<P>确定要删除吗</P>',
          onOk: () => {
            let organizationIds = this.editForm.inviteOrganization.split(',')
            this.invitedOrganizationIds = organizationIds.filter((id) => { return removeOrganizationIds.indexOf(id) < 0 })
            this.saveEditClassUser(2)
          }
        })
      }
    },
    deleteCatalogCourseware (params) {
      this.$Modal.confirm({
        title: '提示',
        content: '<P>确定要删除吗</P>',
        onOk: () => {
          API.catalogCourseware.del({ pkId: params.pkId }).then(({ data }) => {
            if (data && data.code === 0) {
              this.getCatalogCourseware()
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
    moveCatalog (catalog, move) {
      let param = {
        classId: catalog.classId,
        sort: catalog.sort,
        move: move
      }
      API.catalog.move(param).then(({ data }) => {
        if (data && data.code === 0) {
          this.getCatalog()
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
    moveCatalogCourseware (catalogCourseware, move) {
      let param = {
        catalogId: catalogCourseware.catalogId,
        sort: catalogCourseware.sort,
        move: move
      }
      API.catalogCourseware.move(param).then(({ data }) => {
        if (data && data.code === 0) {
          this.getCatalogCourseware()
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
    handleOssSuccess (res) { // 文件上传成功
      if (res.status === 'ok') {
        this.editForm.picture = this.fileName
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
      let vm = this
      return oss(file.name, 2).then(res => {
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
    this.getClassType()
  }
}
</script>
<style>
  .ivu-table-header {
    color: #909399;
  }
</style>
