<template>
  <div>
    <Card dis-hover :bordered="false" :style="'height: '+headerHeight+'px'">
      <div style="width: 100%;cursor: pointer" @click="showHeader">
        <span>试卷统计</span>
        <Icon v-if="headerHeight === 50" style="float: right" type="ios-arrow-forward" />
        <Icon v-else style="float: right" type="ios-arrow-down" />
      </div>
    </Card>
    <Card style="margin-top: 10px" dis-hover :bordered="false">
      <Row style="margin-bottom: 10px;">
        <Col>
          <Button type="success" icon="md-add" @click="addButton()">创建试卷</Button>
          <Button style="margin-left: 10px" type="primary" icon="md-create" @click="showPaperType">维护分类</Button>
          <Tooltip content="刷新" placement="bottom">
            <Button style="border-radius: 5px 0px 0px 5px;margin-left: 10px" icon="ios-refresh" @click="init"></Button>
          </Tooltip>
          <Tooltip content="帮助中心" placement="bottom">
            <Button style="border-radius: 0px;" icon="ios-help-circle"></Button>
          </Tooltip>
          <Button style="border-radius: 0px 5px 5px 0px;" type="success" icon="ios-trash-outline" @click="deletePaper()"></Button>
        </Col>
        <Col style="margin-left: 1175px">
          <Dropdown trigger="click" @on-click="functionChoose">
            <Button>搜索<Icon style="margin-left: 5px" type="ios-arrow-down" /></Button>
            <DropdownMenu slot="list">
              <div style="width: 300px">
                <Row style="margin-top: 15px;margin-left: 10px" align="middle">
                  <span>是否归档</span>
                  <i-switch style="margin-left: 20px" v-model="placeFile" />
                </Row>
                <Row style="margin-top: 15px;margin-left: 10px" align="middle">
                  <span>分类</span>
                  <Select style="width: 150px;margin-left: 48px" placeholder="选择分类" v-model="typeId">
                    <Option :value="Number(0)">全部</Option>
                    <Option v-for="(item, index) in paperTypeList" :key="index" :value="item.pkId">{{item.name}}</Option>
                  </Select>
                </Row>
                <Row style="margin-top: 15px;margin-left: 10px" align="middle">
                  <span>发布状态</span>
                  <Select style="width: 150px;margin-left: 20px" placeholder="发布状态" v-model="status">
                    <Option :value="Number(1)">已发布</Option>
                    <Option :value="Number(0)">待发布</Option>
                  </Select>
                </Row>
                <Row style="margin-top: 15px;margin-left: 10px" align="middle">
                  <span>试卷类型</span>
                  <Select style="width: 150px;margin-left: 20px" placeholder="试卷类型" v-model="isOpen">
                    <Option :value="Number(0)">公开</Option>
                    <Option :value="Number(1)">非公开</Option>
                  </Select>
                </Row>
                <Row style="margin-top: 15px;margin-left: 10px" align="middle">
                  <span>出题类型</span>
                  <Select style="width: 150px;margin-left: 20px" placeholder="出题类型" v-model="isFixed">
                    <Option :value="Number(0)">固定试卷</Option>
                    <Option :value="Number(1)">随机试卷</Option>
                  </Select>
                </Row>
                <Row style="margin-top: 15px;margin-left: 10px" align="middle">
                  <span>搜索试卷</span>
                  <Input style="width: 200px;margin-left: 20px" v-model="title" placeholder="搜索试卷"/>
                </Row>
                <Row style="margin-top: 15px;margin-left: 20px;margin-right: 10px;border-top: 1px solid #e8eaec" align="middle">
                  <div style="margin-top: 10px;margin-bottom: 10px">
                    <Button type="text" style="color: deepskyblue;margin-left: 40px" icon="md-checkmark" @click="init">搜索</Button>
                    <Button type="text" style="color: deepskyblue;margin-left: 15px" icon="md-refresh" @click="init('reset')">重置</Button>
                  </div>
                </Row>
              </div>
            </DropdownMenu>
          </Dropdown>
        </Col>
      </Row>
      <Row>
        <Table ref="selection" :columns="columns" :data="tableData" :highlight-row="true" :current="pageIndex" :key="timer" style="width: 100%"
               @on-selection-change="selectionChange" :loading="loading">
          <template slot="action" slot-scope="scope">
            <ButtonGroup>
              <Button type="primary" size="small" @click="editPaper(scope.row)">编辑</Button>
              <Button v-if="scope.row.isOpen === 1" type="primary" style="margin-left: 1px" size="small" @click="showInviteUser(scope.row)">邀请考生</Button>
              <Button v-else type="primary" style="margin-left: 1px" size="small" @click="showInformationCollectionModal(scope.row)">采集信息</Button>
              <Dropdown placement="bottom-end" @on-click="rowFunctionChoose" transfer>
                <Button size="small">
                  <Icon type="ios-arrow-down"></Icon>
                </Button>
                <div slot="list">
                  <DropdownItem :name="JSON.stringify({num: 1,paper: scope.row})"><Icon type="md-share" style="margin-right: 5px"/>考试入口</DropdownItem>
                  <DropdownItem :name="JSON.stringify({num: 2,paper: scope.row})"><Icon type="ios-copy-outline" style="margin-right: 5px"/>复制试卷</DropdownItem>
                  <DropdownItem :name="JSON.stringify({num: 3,paper: scope.row})" v-show="scope.row.isOpen === 1"><Icon type="ios-stats-outline" style="margin-right: 5px"/>按组织架构统计</DropdownItem>
                  <div style="border-bottom: 1px solid #e8eaec"></div>
                  <div style="border-bottom: 1px solid #e8eaec" v-show="scope.row.isOpen === 1">
                    <DropdownItem :name="JSON.stringify({num: 4,paper: scope.row})"><Icon type="ios-copy-outline" style="margin-right: 5px"/>复制补考试卷</DropdownItem>
                    <DropdownItem :name="JSON.stringify({num: 5,paper: scope.row})"><Icon type="ios-mail-outline" style="margin-right: 5px"/>发送消息</DropdownItem>
                  </div>
                  <DropdownItem :name="JSON.stringify({num: 6,paper: scope.row})"><Icon type="ios-trash-outline" style="margin-right: 5px"/>删除试卷</DropdownItem>
                  <DropdownItem :name="JSON.stringify({num: 7,paper: scope.row})"><Icon type="ios-filing-outline" style="margin-right: 5px"/>{{!scope.row.placeFile ? '试卷归档':'取消试卷归档'}}</DropdownItem>
                </div>
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
    <Modal v-model="paperTypeModal" :closable='true' :mask-closable=false :width="600" @on-visible-change="resetPaperTypeData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">维护分类</h3>
      <Row style="margin-bottom: 10px">
        <Button type="primary" icon="md-add" @click="addPaperType">创建分类</Button>
        <ButtonGroup style="margin-left: 20px">
          <Button icon="ios-upload-outline">下载分类模版文件</Button>
          <Button type="success" icon="ios-download-outline">导入分类文件</Button>
        </ButtonGroup>
      </Row>
      <Row v-for="(paperType, index) in paperTypeList" :key="'paperType'+index" :style="'margin-left: 10px;background-color: '  + (currentPaperType === index ? '#d5e8fc':'')">
        <Input v-if="isPaperTypeEdit && currentPaperType === index" :ref="'paperTypeName'+index" :value="paperType.name" style="width: 100px" @on-blur="saveEditPaperType(index)"/>
        <span v-else style="cursor: pointer;" @mouseover="paperTypeMouseOver(index)" @mouseout="paperTypeMouseOut">
          {{paperType.name}}
          <ButtonGroup style="margin-left: 5px">
          <Button type="text" icon="md-create" @click="editPaperType()"></Button>
          <Button type="text" icon="ios-trash-outline" @click="deletePaperType(paperType.pkId)"></Button>
        </ButtonGroup>
        </span>
      </Row>
      <div slot="footer">
        <Button @click="paperTypeModal = false">取消</Button>
      </div>
    </Modal>
    <!--添加/修改试卷对话框-->
    <Modal v-model="addModal" :closable='true' :mask-closable=false :width="650" @on-visible-change="resetFormData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">{{!editForm.pkId?'创建试卷':'编辑试卷'}}</h3>
      <Form ref="editForm" :model="editForm" label-position="right" :label-width="100"
            :rules="rules" @submit.native.prevent="saveEdit">
        <FormItem label="分类：" prop="typeId">
          <Select v-model="editForm.typeId" style="margin-right:5px;width: 150px;" placeholder="选择分类">
            <Option v-for="item in paperTypeList" :value="item.pkId" :key="'choosePaperType'+item.pkId">{{item.name}}</Option>
          </Select>
        </FormItem>
        <FormItem label="试卷类型：" prop="isOpen">
          <RadioGroup v-model="editForm.isOpen">
            <Row>
              <Poptip trigger="hover" placement="right">
                <Radio style="margin-top: 10px" :label="Number(1)" border>非公开试卷</Radio>
                <div slot="content">
                  <span>需要登录之后才能进入考试，管理员可以批量导<br>入考生帐号，考生自己也可以直接注册帐号，支<br>持微信一键注册登录。</span>
                  <h4>适用场景：</h4>
                  <Tag color="orange">员工考核</Tag><Tag color="orange">员工调查</Tag><Tag color="orange">电子作业</Tag><Tag color="orange">培训考试</Tag><br>
                  <Tag color="orange">单位内部考试</Tag><Tag color="orange">360度评估</Tag><Tag color="orange">学校无纸化考试</Tag>
                </div>
              </Poptip>
            </Row>
            <Row>
              <Poptip trigger="hover" placement="right">
                <Radio style="margin-top: 10px" :label="Number(0)" border>公开试卷</Radio>
                <div slot="content">
                  <span>无需登录，考生录入采集信息后就可以直接进入<br>考试，采集信息可自定义。</span>
                  <h4>适用场景：</h4>
                  <Tag color="orange">知识竞赛</Tag><Tag color="orange">问卷调查</Tag><Tag color="orange">测评分析</Tag><Tag color="orange">心理测试</Tag><br>
                  <Tag color="orange">招聘考试</Tag><Tag color="orange">投票评选</Tag><Tag color="orange">闯关答题</Tag><Tag color="orange">市场调研</Tag>
                </div>
              </Poptip>
            </Row>
          </RadioGroup>
        </FormItem>
        <FormItem label="出题方式：" prop="isFixed">
          <RadioGroup v-model="editForm.isFixed">
            <Row>
              <Poptip trigger="hover" placement="right">
                <Radio style="margin-top: 10px" :label="Number(0)" border>固定试卷</Radio>
                <div slot="content">
                  <span>每个考生考试的题目都是相同的，可设置题<br>目和选项随机。</span>
                </div>
              </Poptip>
            </Row>
            <Row>
              <Poptip trigger="hover" placement="right">
                <Radio style="margin-top: 10px" :label="Number(1)" border>随机试卷</Radio>
                <div slot="content">
                  <span>通过配置随机规则，随机从题库里抽取题<br>目，每个考生考试的题目都不同。</span>
                </div>
              </Poptip>
            </Row>
          </RadioGroup>
        </FormItem>
        <FormItem label="试卷名称：" prop="title">
          <Input v-model="editForm.title" style="width: 500px" :maxlength="32"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="cancel">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveEdit">提交</Button>
      </div>
    </Modal>
    <!--邀请考生-->
    <Modal v-model="inviteUserModal" :closable="true" :mask-closable="false" :width="850" @on-visible-change="resetFormData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">录入考生</h3>
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
        <Button style="margin-left: 10px" type="primary" :loading="saveLoading" @click="saveSetPaperUser">导入</Button>
        <Button @click="inviteUserModal = false;tabPaneNumber = 0">关闭</Button>
      </div>
    </Modal>
    <!--查看考生-->
    <Modal v-model="examUserModal" :closable="true" :mask-closable="false" :width="1100" @on-visible-change="resetFormData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">考试结果</h3>
      <Tabs :animated="false" v-model="examTabPaneNumber" @on-click="examTabChange">
        <TabPane label="已考">
          <Row align="middle">
            <Col>
              <ButtonGroup>
                <Button icon="md-cloud-upload">导出</Button>
                <Button icon="ios-trash-outline" @click="deleteExamPaper()" />
                <Button icon="ios-refresh" @click="getExamUser"/>
              </ButtonGroup>
              <ButtonGroup style="margin-left: 5px">
                <Button @click="finishTimeOrder">完成时间<Icon :type="finishTime === 'down' ? 'ios-arrow-round-down':'ios-arrow-round-up'" /></Button>
                <Button @click="scoresOrder">成绩<Icon :type="scores === 'down' ? 'ios-arrow-round-down':'ios-arrow-round-up'" /></Button>
              </ButtonGroup>
              <Button style="margin-left: 5px" icon="md-add">批量阅卷</Button>
              <Poptip v-show="!!editForm.isOpen" trigger="hover" :transfer="true" placement="top-start">
                <div slot="content">
                  <span style="font-size: 18px;">分开显示</span><br><br>
                  <span>同一个考生考多次，在查看成绩<br>时显示全部成绩。</span>
                </div>
                <Icon type="ios-help-circle"/>
              </Poptip>
              <Select v-show="!!editForm.isOpen" style="width: 150px" v-model="scoreType">
                <Option :value="Number(0)">分开显示</Option>
                <Option :value="Number(1)">取考试次数平均分</Option>
                <Option :value="Number(2)">取最高分</Option>
                <Option :value="Number(3)">取试卷次数平均分</Option>
                <Option :value="Number(4)">取考试总分</Option>
              </Select>
              <Button icon="ios-paper-outline" style="margin-left: 5px">证书列表</Button>
            </Col>
            <Col style="margin-top: 10px">
              <Select style="width: 80px;" v-model="isJudge">
                <Option :value="Number(-1)">全部</Option>
                <Option :value="Number(0)">已判分</Option>
                <Option :value="Number(1)">未判分</Option>
              </Select>
              <Select style="width: 80px;margin-left: 5px" v-model="isMark">
                <Option :value="Number(-1)">全部</Option>
                <Option :value="Number(0)">已阅卷</Option>
                <Option :value="Number(1)">未阅卷</Option>
              </Select>
              <DatePicker :transfer="true" type="datetime" placeholder="开始时间" :value="startTime" @on-change="startTime=$event" style="width: 200px;margin-left: 5px"></DatePicker>至
              <DatePicker :transfer="true" type="datetime" placeholder="结束时间" :value="endTime" @on-change="endTime=$event" style="width: 200px"></DatePicker>
              <Input v-if="!editForm.isOpen" style="width: 200px;margin-left: 5px" v-model="userInfo" placeholder="采集信息"/>
              <Input v-else style="width: 200px;margin-left: 5px" v-model="keyWord" placeholder="帐号/姓名"/>
              <ButtonGroup>
                <Button style="background-color: #F5F7FA" @click="getExamUser">搜索</Button>
                <Button style="background-color: #F5F7FA" @click="getExamUser('reset')">重置</Button>
              </ButtonGroup>
            </Col>
          </Row>
          <Row style="margin-top: 10px">
            <Table ref="examUserSelection" :columns="!editForm.isOpen ? openExamUserColumns:examUserColumns" :data="examUserTableData" :highlight-row="true" :current="examUserPageIndex" style="width: 100%;" :key="examUserTimer"
                   @on-selection-change="examUserSelectionChange" :loading="examUserTableLoading">
            </Table>
          </Row>
          <Row style="margin-top: 10px" justify="end">
            <Page :total="examUserTotalCount" :current="examUserPageIndex" :page-sise="examUserPageSize" loading show-total show-sizer show-elevator
                  @on-change="examUserPageChange" @on-page-size-change="examUserSizeChange">
            </Page>
          </Row>
        </TabPane>
        <TabPane v-if="!!editForm.isOpen" label="未考">
          <Row align="middle">
           <Col>
             <ButtonGroup>
               <Button icon="md-download">导出未参考人员</Button>
               <Button icon="ios-refresh" @click="getExamUser"/>
             </ButtonGroup>
           </Col>
           <Col style="margin-left: 615px">
             <Input style="width: 150px" v-model="keyWord" placeholder="姓名/账号"/>
             <ButtonGroup>
               <Button style="background-color: #F5F7FA" @click="getExamUser">搜索</Button>
               <Button style="background-color: #F5F7FA" @click="getExamUser('reset')">重置</Button>
             </ButtonGroup>
           </Col>
          </Row>
          <Row style="margin-top: 10px">
            <Table :columns="unExamUserColumns" :data="examUserTableData" :highlight-row="true" :current="examUserPageIndex" style="width: 100%;" :key="examUserTimer" :loading="examUserTableLoading"></Table>
          </Row>
          <Row style="margin-top: 10px" justify="end">
            <Page :total="examUserTotalCount" :current="examUserPageIndex" :page-sise="examUserPageSize" loading show-total show-sizer show-elevator
                  @on-change="examUserPageChange" @on-page-size-change="examUserSizeChange">
            </Page>
          </Row>
        </TabPane>
      </Tabs>
      <div slot="footer">
        <Button @click="examUserModal = false">关闭</Button>
      </div>
    </Modal>
    <!--采集信息-->
    <Modal v-model="informationCollectionModal" :closable='true' :mask-closable=false :width="900" @on-visible-change="resetFormData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">想要了解考生的什么信息？（答题之前填写）</h3>
      <Row>
        <Button type="primary" @click="saveInformationCollection">添加信息项</Button>
      </Row>
      <Row style="margin-top: 8px" align="middle" v-for="(info, index) in informationCollectionList" :key="'informationCollection' + index">
        <Select v-model="info.type" style="margin-left: 15px;width: 150px" @on-change="saveEditInformationCollection(...arguments, 0, info, index)">
          <Option :value="Number(0)">文本</Option>
          <Option :value="Number(1)">下拉</Option>
        </Select>
        <Input :readonly="info.type === 2" :ref="'info'+index" :value="info.title" style="margin-left: 15px;width: 150px" @on-blur="saveEditInformationCollection('', 1, info, index)"/>
        <Select v-model="info.isRequired" style="margin-left: 15px;width: 100px" @on-change="saveEditInformationCollection(...arguments, 2, info, index)">
          <Option :value="Number(1)">选填</Option>
          <Option :value="Number(0)">必填</Option>
        </Select>
        <Poptip trigger="hover" :transfer="true" placement="top-start">
          <Icon type="ios-help-circle" style="margin-left: 10px;" />
          <div slot="content">
            <span style="font-size: 18px;">是否数据唯一</span><br><br>
            <span>数据唯一：表示此信息在所有考生<br>中是唯一的。</span>
          </div>
        </Poptip>
        <Checkbox v-model="info.isAble" style="margin-left: 10px;" @on-change="saveEditInformationCollection(...arguments, 3, info, index)">是否数据唯一</Checkbox>
        <Button type="primary" shape="circle" style="margin-left: 10px" icon="ios-remove" @click="deleteInformationCollection(info.pkId)"></Button>
        <Button :disabled="index === 0" type="primary" shape="circle" style="margin-left: 10px" icon="ios-arrow-up" @click="moveInformationCollection(info, 'up')"></Button>
        <Button :disabled="(index+1) === informationCollectionList.length" type="primary" shape="circle" style="margin-left: 10px" icon="ios-arrow-down" @click="moveInformationCollection(info, 'down')"></Button>
        <Button v-show="info.type === 1" icon="ios-create-outline" type="success" style="margin-left: 10px" size="small" @click="showAddOptionModal(info)">编辑下拉选项</Button>
      </Row>
      <div slot="footer">
        <Button @click="informationCollectionModal = false">取消</Button>
      </div>
    </Modal>
    <!--编辑信息采集下拉框-->
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
  </div>
</template>

<script>
import API from '@/api'
import tinymceEditor from '_c/text-editor/tinymce-editor.vue'

export default {
  name: 'paper',
  components: { tinymceEditor },
  data () {
    return {
      headerHeight: 50,
      timer: '',
      examUserTimer: '',
      placeFile: false,
      typeId: '',
      status: '',
      isOpen: '',
      isFixed: '',
      title: '',
      userAccount: '',
      userName: '',
      examPaperStatus: 2,
      finishTime: 'down',
      scores: 'down',
      scoreType: 0,
      isJudge: -1,
      isMark: -1,
      startTime: '',
      endTime: '',
      keyWord: '',
      userInfo: '',
      loading: false,
      userTableLoading: false,
      examUserTableLoading: false,
      titleTableLoading: false,
      uploadLoading: false,
      saveLoading: false,
      paperTypeModal: false,
      addModal: false,
      inviteUserModal: false,
      examUserModal: false,
      informationCollectionModal: false,
      optionModal: false,
      isPaperTypeEdit: false,
      tabPaneNumber: 0,
      examTabPaneNumber: 0,
      currentPaperType: -1,
      totalCount: 0,
      userTotalCount: 0,
      examUserTotalCount: 0,
      pageIndex: 1,
      userPageIndex: 1,
      examUserPageIndex: 1,
      pageSize: 10,
      userPageSize: 10,
      examUserPageSize: 10,
      paperTypeList: [],
      selectOrganizationList: [],
      organizationList: [],
      informationCollectionList: [],
      informationCollectionForm: {},
      optionList: [],
      editForm: {
        pkId: 0,
        typeId: 0,
        isOpen: 1,
        isFixed: 0,
        title: ''
      },
      selection: [],
      userSelection: [],
      titleSelection: [],
      examUserSelection: [],
      checkOrganization: '',
      checkOrganizationList: [],
      inviteOrganizationList: [],
      inviteAll: false,
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
          title: '试卷名称',
          key: 'title',
          align: 'left',
          minWidth: 200
        },
        {
          title: '已考次数',
          key: 'testedTime',
          align: 'center',
          minWidth: 100,
          render: (h, params) => {
            let searchBtn = h('Button', {
              props: {
                icon: 'ios-search',
                size: 'small'
              },
              style: {
                marginRight: '5px'
              },
              on: {
                click: () => {
                  this.showExamUser(params.row)
                }
              }
            }, params.row.testedTime)
            return h('div', [
              searchBtn
            ])
          }
        },
        {
          title: '考试中',
          key: 'inExam',
          align: 'center',
          minWidth: 100,
          render: (h, params) => {
            let searchBtn = h('Button', {
              props: {
                icon: 'ios-search',
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
            }, params.row.inExam)
            return h('div', [
              searchBtn
            ])
          }
        },
        {
          title: '状态',
          key: 'status',
          align: 'center',
          minWidth: 140,
          render: (h, params) => {
            let statusTag = h('Tag', {
              props: {
                color: !params.row.status ? 'red' : 'green'
              },
              style: {
                marginRight: '5px'
              }
            }, !params.row.status ? '待发布' : '已发布')
            let statusBtn = h('Button', {
              props: {
                type: !params.row.status ? 'warning' : 'success',
                size: 'small'
              },
              on: {
                click: () => {
                  this.setStatus(params.row.pkId, !params.row.status ? 1 : 0)
                }
              }
            }, !params.row.status ? '发布' : '取消')
            return h('div', [
              statusTag, statusBtn
            ])
          }
        },
        {
          title: '分类',
          key: 'typeName',
          align: 'center',
          minWidth: 80
        },
        {
          title: '试卷类型',
          key: 'isOpen',
          align: 'center',
          minWidth: 110,
          render: (h, params) => {
            switch (params.row.isOpen) {
              case 0:return h('Label', '公开试卷')
              case 1:return h('Label', '非公开试卷')
            }
          }
        },
        {
          title: '出题类型',
          key: 'isFixed',
          align: 'center',
          minWidth: 100,
          render: (h, params) => {
            switch (params.row.isFixed) {
              case 0:return h('Label', '固定试卷')
              case 1:return h('Label', '随机试卷')
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
          width: 200,
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
      examUserColumns: [
        {
          type: 'index',
          title: ' ',
          width: 20,
          align: 'center'
        },
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '帐号',
          key: 'userAccount',
          align: 'left',
          minWidth: 110
        },
        {
          title: '姓名',
          key: 'userName',
          align: 'center',
          minWidth: 110
        },
        {
          title: '得分',
          key: 'userScore',
          align: 'center',
          minWidth: 80
        },
        {
          title: '完成时间',
          key: 'finishTime',
          align: 'center',
          minWidth: 170
        },
        {
          title: '用时',
          key: 'useTime',
          align: 'center',
          width: 80,
          render: (h, params) => {
            let minute = parseInt(params.row.useTime / 60)
            let second = params.row.useTime % 60
            return h('Label', (minute < 10 ? ('0' + minute) : minute) + '\'' + (second < 10 ? ('0' + second) : second) + '"')
          }
        },
        {
          title: '已判分',
          key: 'isJudge',
          align: 'center',
          minWidth: 120,
          render: (h, params) => {
            let judgeTag = h('Tag', {
              props: {
                color: !params.row.isJudge ? 'green' : 'red'
              },
              style: {
                marginRight: '5px'
              }
            }, !params.row.isJudge ? '是' : '否')
            let markBtn = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              on: {
                click: () => {
                  console.log('阅卷')
                }
              }
            }, '阅卷')
            return h('div', [
              judgeTag, markBtn
            ])
          }
        },
        {
          title: '是否阅卷',
          key: 'isMark',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('i-switch', {
              props: {
                'v-model': !params.row.isMark,
                'true-color': '#13ce66',
                'false-color': '#ff4949'
              },
              on: {
                'on-change': (value) => {
                  params.row.isMark = value ? 0 : 1
                  this.mark(params.row)
                }
              }
            })
          }
        },
        {
          title: '操作',
          key: 'action',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('Button', {
              props: {
                type: 'success',
                size: 'small'
              },
              on: {
                click: () => {
                  this.deleteExamPaper(params.row)
                }
              }
            }, '删除')
          }
        }
      ],
      openExamUserColumns: [
        {
          type: 'index',
          title: ' ',
          width: 20,
          align: 'center'
        },
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '信息采集',
          key: 'userInfo',
          align: 'left',
          minWidth: 110
        },
        {
          title: '得分',
          key: 'userScore',
          align: 'center',
          minWidth: 80
        },
        {
          title: '完成时间',
          key: 'finishTime',
          align: 'center',
          minWidth: 170
        },
        {
          title: '用时',
          key: 'useTime',
          align: 'center',
          width: 80,
          render: (h, params) => {
            let minute = parseInt(params.row.useTime / 60)
            let second = params.row.useTime % 60
            return h('Label', (minute < 10 ? ('0' + minute) : minute) + '\'' + (second < 10 ? ('0' + second) : second) + '"')
          }
        },
        {
          title: '已判分',
          key: 'isJudge',
          align: 'center',
          minWidth: 120,
          render: (h, params) => {
            let judgeTag = h('Tag', {
              props: {
                color: !params.row.isJudge ? 'green' : 'red'
              },
              style: {
                marginRight: '5px'
              }
            }, !params.row.isJudge ? '是' : '否')
            let markBtn = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              on: {
                click: () => {
                  console.log('阅卷')
                }
              }
            }, '阅卷')
            return h('div', [
              judgeTag, markBtn
            ])
          }
        },
        {
          title: '是否阅卷',
          key: 'isMark',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('i-switch', {
              props: {
                'v-model': !params.row.isMark,
                'true-color': '#13ce66',
                'false-color': '#ff4949'
              },
              on: {
                'on-change': (value) => {
                  params.row.isMark = value ? 0 : 1
                  this.mark(params.row)
                }
              }
            })
          }
        },
        {
          title: '操作',
          key: 'action',
          align: 'center',
          width: 100,
          render: (h, params) => {
            return h('Button', {
              props: {
                type: 'success',
                size: 'small'
              },
              on: {
                click: () => {
                  this.deleteExamPaper(params.row)
                }
              }
            }, '删除')
          }
        }
      ],
      unExamUserColumns: [
        {
          type: 'index',
          title: ' ',
          width: 20,
          align: 'center'
        },
        {
          title: '姓名',
          key: 'userName',
          align: 'center',
          minWidth: 150
        },
        {
          title: '帐号',
          key: 'userAccount',
          align: 'center',
          minWidth: 150
        },
        {
          title: '部门',
          key: 'organizationName',
          align: 'center',
          minWidth: 200
        }
      ],
      tableData: [],
      userTableData: [],
      titleTableData: [],
      examUserTableData: [],
      rules: {
        title: [{
          required: true,
          message: '试卷名称不能为空',
          trigger: 'blur'
        }]
      }
    }
  },
  watch: {
    $route: function (to, from) {
      if (to.name === 'paper') {
        this.getPaperType()
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
    editPaper (params) {
      this.$router.push({ name: 'paperEditor', query: { paperId: params.pkId } })
    },
    maintenanceTopics (params) {
      this.$router.push({ name: 'question', query: { id: params.pkId, name: params.name } })
    },
    init (reset) {
      if (reset === 'reset') {
        this.placeFile = false
        this.typeId = ''
        this.status = ''
        this.isOpen = ''
        this.isFixed = ''
        this.title = ''
      }
      this.loading = true
      let searchParams = {
        pageSize: this.pageSize,
        pageIndex: this.pageIndex,
        placeFile: this.placeFile ? 1 : 0,
        typeId: this.typeId,
        status: this.status,
        isOpen: this.isOpen,
        isFixed: this.isFixed,
        title: this.title
      }
      API.paper.list(searchParams).then(({ data }) => {
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
    getPaperType () {
      API.paperType.list().then(({ data }) => {
        if (data && data.code === 0) {
          this.paperTypeList = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
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
    getExamUser (reset) { // 数据初始化、搜索
      if (reset === 'reset') {
        this.examUserPageIndex = 1
        this.examUserPageSize = 10
        this.finishTime = 'down'
        this.scores = 'down'
        this.scoreType = 0
        this.isJudge = -1
        this.isMark = -1
        this.startTime = ''
        this.endTime = ''
        this.keyWord = ''
        this.userInfo = ''
      }
      this.examUserTableLoading = true
      let searchParams = {
        pageSize: this.examUserPageSize,
        page: this.examUserPageIndex,
        paperId: this.editForm.pkId,
        status: this.examPaperStatus,
        finishTime: this.finishTime,
        scores: this.scores,
        isJudge: this.isJudge,
        isMark: this.isMark,
        startTime: this.startTime,
        endTime: this.endTime,
        keyWord: this.keyWord,
        userInfo: this.userInfo
      }
      API.examinationPaper.list(searchParams).then(({ data }) => {
        this.examUserTableLoading = false
        if (data && data.code === 0) {
          this.examUserTableData = data.data.list
          this.examUserTotalCount = data.data.totalCount
        } else {
          this.examUserTableLoading = false
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.examUserTableLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getInformationCollection () {
      API.informationCollection.list({ relationId: this.editForm.pkId }).then(({ data }) => {
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
    setStatus (pkId, status) {
      let params = {
        pkId: pkId,
        status: status
      }
      this.$Modal.confirm({
        title: '提示',
        content: !status ? '<P>确定要取消吗</P>' : '<P>确定要发布吗</P>',
        onOk: () => {
          API.paper.setStatus(params).then(({ data }) => {
            if (data && data.code === 0) {
              this.init()
              this.$Message.success({
                content: data.msg
              })
            } else {
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.$Message.error('连接失败，请检查网络！')
          })
        }
      })
    },
    finishTimeOrder () {
      if (this.finishTime === 'down') {
        this.finishTime = 'up'
      } else {
        this.finishTime = 'down'
      }
      this.getExamUser()
    },
    scoresOrder () {
      if (this.scores === 'down') {
        this.scores = 'up'
      } else {
        this.scores = 'down'
      }
      this.getExamUser()
    },
    tabChange (value) {
      this.userSelection = []
      this.titleSelection = []
      this.inviteOrganizationList = []
      this.timer = new Date().getTime()
      this.getOrganization()
    },
    examTabChange (value) {
      switch (value) {
        case 0:this.examPaperStatus = 2; break
        case 1:this.examPaperStatus = 0; break
      }
      this.examUserSelection = []
      this.scoreType = 0
      this.isJudge = -1
      this.isMark = -1
      this.startTime = ''
      this.endTime = ''
      this.keyWord = ''
      this.userInfo = ''
      this.examUserTimer = new Date().getTime()
      this.getExamUser()
    },
    paperTypeMouseOver (index) {
      if (!this.isPaperTypeEdit) {
        this.currentPaperType = index
      }
    },
    paperTypeMouseOut () {
      if (!this.isPaperTypeEdit) {
        this.currentPaperType = -1
      }
    },
    editPaperType () {
      this.isPaperTypeEdit = true
    },
    functionChoose (name) {
      switch (name) {
        case 1 : alert(11); break
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
        case 1 : console.log(json.paper); break
        case 2 : console.log(json.paper); break
        case 3 : console.log(json.paper); break
        case 4 : console.log(json.paper); break
        case 5 : console.log(json.paper); break
        case 6 : this.deletePaper(json.paper); break
        case 7 : this.placeFilePaper(json.paper); break
      }
    },
    addButton (params) {
      if (typeof params !== 'undefined') { // 如果是修改状态，则填充必要数据
        this.editForm = JSON.parse(JSON.stringify(params))
      }
      this.addModal = true
    },
    showPaperType () {
      this.getPaperType()
      this.paperTypeModal = true
    },
    showInviteUser (params) {
      if (typeof params !== 'undefined') {
        this.editForm = JSON.parse(JSON.stringify(params))
      }
      this.getUser('reset')
      this.getOrganization()
      this.getTitle()
      this.inviteAll = !this.editForm.inviteAll
      this.inviteUserModal = true
    },
    showExamUser (params) {
      if (typeof params !== 'undefined') {
        this.editForm = JSON.parse(JSON.stringify(params))
      }
      this.getExamUser('reset')
      this.examUserSelection = []
      this.examPaperStatus = 2
      this.examUserModal = true
    },
    showInformationCollectionModal (params) {
      if (typeof params !== 'undefined') {
        this.editForm = JSON.parse(JSON.stringify(params))
      }

      this.getInformationCollection()
      this.informationCollectionModal = true
    },
    showAddOptionModal (info) {
      this.informationCollectionForm = info
      this.optionList = JSON.parse(info.option)
      this.optionModal = true
    },
    selectionChange (data) {
      // 记录选中的数据
      this.selection = data
    },
    userSelectionChange (data) {
      this.userSelection = data
    },
    examUserSelectionChange (data) {
      this.examUserSelection = data
    },
    titleSelectionChange (data) {
      this.titleSelection = data
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
    pageChange (val) {
      this.pageIndex = val
      this.init()
    },
    userPageChange (val) {
      this.userPageIndex = val
      this.getUser()
    },
    examUserPageChange (val) {
      this.examUserPageIndex = val
      this.getExamUser()
    },
    sizeChange (size) {
      this.pageSize = size
      this.init()
    },
    userSizeChange (size) {
      this.userPageSize = size
      this.getInvitedUser()
    },
    examUserSizeChange (size) {
      this.examUserPageSize = size
      this.getExamUser()
    },
    cancel () {
      this.addModal = false
    },
    resetPaperTypeData () {
      this.isPaperTypeEdit = false
      this.currentPaperType = -1
    },
    resetFormData (val) {
      this.examTabPaneNumber = 0
      if (!val) {
        this.editForm = {
          pkId: 0,
          typeId: 0,
          isOpen: 1,
          isFixed: 0,
          title: ''
        }
      }
    },
    addPaperType () {
      this.paperTypeList.push({
        pkId: 0,
        name: ''
      })
      this.currentPaperType = this.paperTypeList.length - 1
      this.isPaperTypeEdit = true
    },
    saveEditPaperType (index) {
      if (this.$refs['paperTypeName' + index][0].value !== this.$refs['paperTypeName' + index][0].currentValue || !this.paperTypeList[index].pkId) {
        if (this.$refs['paperTypeName' + index][0].currentValue !== '') {
          let params = {
            pkId: this.paperTypeList[index].pkId,
            name: this.$refs['paperTypeName' + index][0].currentValue
          }
          let link = !this.paperTypeList[index].pkId ? API.paperType.add(params) : API.paperType.update(params)
          link.then(({ data }) => {
            if (data && data.code === 0) {
              this.getPaperType()
              this.isPaperTypeEdit = false
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
          this.getPaperType()
          this.isPaperTypeEdit = false
        }
      }
    },
    saveEdit () {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          let params = {
            pkId: this.editForm.pkId || undefined,
            typeId: this.editForm.typeId,
            isOpen: this.editForm.isOpen,
            isFixed: this.editForm.isFixed,
            title: this.editForm.title,
            authorId: this.$store.state.sys.userId
          }
          this.saveLoading = true
          API.paper.add(params).then(({ data }) => {
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
    saveSetPaperUser () {
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
      API.paper.setUser(params).then(({ data }) => {
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
    mark (params) {
      API.examinationPaper.mark({ pkId: params.pkId, isMark: params.isMark }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$Message.success(data.msg)
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    saveInformationCollection () {
      let params = {
        relationId: this.editForm.pkId,
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
    placeFilePaper (params) {
      if (!params.placeFile) {
        params.placeFile = 1
      } else {
        params.placeFile = 0
      }
      this.$Modal.confirm({
        title: !params.placeFile ? '提示' : '试卷归档',
        content: !params.placeFile ? '<p>确认取消归档该试卷吗？</p>' : '<P>试卷归档后，默认试卷列表页面不显示，如需查看归档试卷，请在搜索条件里选择后查询。\n' +
        '试卷归档不影响考生查看答卷，如果希望考生无法查看试卷，请取消发布。</P>',
        width: !params.placeFile ? 400 : 670,
        onOk: () => {
          this.loading = true
          API.paper.placeFile({ pkId: params.pkId, placeFile: params.placeFile }).then(({ data }) => {
            if (data && data.code === 0) {
              this.init()
              this.$Message.success(data.msg)
            } else {
              this.$Message.error(data.msg)
            }
          }).catch(() => {
            this.$Message.error('连接失败，请检查网络！')
          })
        }
      })
    },
    deletePaperType (pkId) {
      let param = {
        pkId: pkId
      }
      this.$Modal.confirm({
        title: '提示',
        content: '<P>确定要删除吗</P>',
        onOk: () => {
          API.paperType.del(param).then(({ data }) => {
            if (data && data.code === 0) {
              this.getPaperType()
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
    deletePaper (params) {
      let paperIds = []
      if (typeof params === 'undefined') {
        paperIds = this.selection.map(c => {
          return c.pkId
        })
      } else {
        paperIds = [params.pkId]
      }
      if (paperIds.length === 0) {
        this.$Message.error('请选择试卷')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<P>确定要删除吗</P>',
          onOk: () => {
            this.loading = true
            API.paper.del(paperIds).then(({ data }) => {
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
    deleteExamPaper (params) {
      let examPaperIds = []
      if (typeof params === 'undefined') {
        examPaperIds = this.examUserSelection.map(p => {
          return p.pkId
        })
      } else {
        examPaperIds = [params.pkId]
      }
      if (examPaperIds.length === 0) {
        this.$Message.error('请选择答题试卷')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<P>确定要删除吗</P>',
          onOk: () => {
            this.examUserTableLoading = true
            API.examinationPaper.del(examPaperIds).then(({ data }) => {
              if (data && data.code === 0) {
                this.getExamUser()
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
    }
  },
  mounted () {
    this.init()
    this.getPaperType()
  }
}
</script>
<style>
  .ivu-table-header {
    color: #909399;
  }
</style>
