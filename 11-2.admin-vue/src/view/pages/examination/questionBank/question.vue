<template>
  <div>
    <Card dis-hover :bordered="false">
      <Spin size="large" fix v-if="loading"></Spin>
      <Row align="top" style="height: 50px;border-bottom: 1px solid #e8eaec">
        <Col style="width: 900px">
          <Button type="primary" icon="ios-arrow-back" @click="goBack">返回题库</Button>
          <Button type="warning" style="border-radius: 5px 0px 0px 5px;margin-left: 5px">批量导题</Button>
          <Dropdown placement="bottom-end" @on-click="functionChoose">
            <Button type="warning" style="border-radius: 0px;margin-left: 1px">单个录入<Icon type="md-arrow-dropdown" /></Button>
            <DropdownMenu slot="list">
              <DropdownItem :name="Number(0)"><Icon type="ios-ionic" style="margin-right: 5px"/>单选题</DropdownItem>
              <DropdownItem :name="Number(1)"><Icon type="ios-keypad-outline" style="margin-right: 5px"/>多选题</DropdownItem>
              <DropdownItem :name="Number(2)"><Icon type="ios-menu-outline" style="margin-right: 5px"/>填空题</DropdownItem>
              <DropdownItem :name="Number(3)"><Icon type="md-checkmark-circle-outline" style="margin-right: 5px"/>判断题</DropdownItem>
              <DropdownItem :name="Number(4)"><Icon type="ios-paper-outline" style="margin-right: 5px"/>简答题</DropdownItem>
              <DropdownItem :name="Number(5)"><Icon type="ios-cube" style="margin-right: 5px"/>组合题</DropdownItem>
            </DropdownMenu>
          </Dropdown>
          <Button type="warning" style="border-radius: 0px 5px 5px 0px;margin-left: 1px">文档导入</Button>
          <Button style="border-radius: 5px 0px 0px 5px;margin-left: 5px" :icon="isOpenAll ? 'ios-contract':'ios-expand'" @click="openAll">{{isOpenAll ? '收起':'展开'}}题目</Button>
          <Button style="border-radius: 0px;" icon="ios-download-outline">导出</Button>
          <Checkbox
            style="border-radius: 0px;margin-right: 0px"
            border
            :indeterminate="indeterminate"
            :value="checkAll"
            @click.prevent.native="handleCheckAll">{{!checkAll?'全选':'全不选'}}</Checkbox>
          <Button style="border-radius: 0px;" type="success" icon="ios-trash-outline" @click="deleteQuestion()"></Button>
          <Dropdown placement="bottom-end" @on-click="functionChoose">
            <Button style="border-radius: 0px 5px 5px 0px;" icon="md-arrow-dropdown"/>
            <DropdownMenu slot="list">
              <DropdownItem :name="Number(1)"><Icon type="ios-paper-outline" style="margin-right: 5px"/>批量设置填空题答案</DropdownItem>
              <DropdownItem :name="Number(2)"><Icon type="ios-add" style="margin-right: 5px"/>批量设置知识点</DropdownItem>
            </DropdownMenu>
          </Dropdown>
          <Button style="margin-left: 5px">题库各题型统计</Button>
        </Col>
        <Col style="margin-left: 210px">
          <Select style="border-radius: 5px 0px 0px 5px;width: 100px;" v-model="type">
            <Option :value="Number(-1)">所有题型</Option>
            <Option :value="Number(0)">单选题</Option>
            <Option :value="Number(1)">多选题</Option>
            <Option :value="Number(2)">判断题</Option>
            <Option :value="Number(3)">填空题</Option>
            <Option :value="Number(4)">简答题</Option>
            <Option :value="Number(5)">组合题</Option>
          </Select>
          <Select style="border-radius: 0px;width: 100px" v-model="difficulty">
            <Option :value="Number(-1)">所有难度</Option>
            <Option :value="Number(0)">高</Option>
            <Option :value="Number(1)">中</Option>
            <Option :value="Number(2)">低</Option>
          </Select>
          <Input style="border-radius: 0px;width: 150px" v-model="title" placeholder="搜索题目"/>
          <ButtonGroup>
            <Button style="background-color: #F5F7FA" @click="init">搜索</Button>
            <Button style="background-color: #F5F7FA" @click="init('reset')">重置</Button>
          </ButtonGroup>
        </Col>
      </Row>
      <Row justify="center" style="margin-top: 10px">
        <h2>{{$route.query.name}}</h2>
      </Row>
      <Row style="margin-top: 15px;margin-bottom: 15px" justify="center">
        <Page :total="totalCount" :current="pageIndex" :page-sise="pageSize" loading show-total show-sizer show-elevator
              @on-change="pageChange" @on-page-size-change="sizeChange">
        </Page>
      </Row>
      <CheckboxGroup v-model="checkAllGroup" @on-change="checkAllGroupChange">
        <Row justify="center" v-for="(question,index) in tableData" :key="'question'+index">
          <Card style="width: 1000px;margin-top: 5px">
            <Row align="middle">
              <Checkbox :label="question.pkId">
                <span style="font-size: 20px">{{index+1}}、[{{question.typeName}}]</span>
                <span>[难度系数：{{getDifficultyName(question.difficulty)}}]</span>
              </Checkbox>
              <div style="width: 120px;">
                <Dropdown placement="bottom-end" @on-click="(name) => functionChoose(name,question.pkId)">
                  <Button v-show="question.type === 5" type="warning" size="small" icon="md-add">新增题目<Icon type="md-arrow-dropdown" /></Button>
                  <DropdownMenu slot="list">
                    <DropdownItem :name="Number(0)"><Icon type="ios-ionic" style="margin-right: 5px"/>单选题</DropdownItem>
                    <DropdownItem :name="Number(1)"><Icon type="ios-keypad-outline" style="margin-right: 5px"/>多选题</DropdownItem>
                    <DropdownItem :name="Number(2)"><Icon type="ios-menu-outline" style="margin-right: 5px"/>填空题</DropdownItem>
                    <DropdownItem :name="Number(3)"><Icon type="md-checkmark-circle-outline" style="margin-right: 5px"/>判断题</DropdownItem>
                    <DropdownItem :name="Number(4)"><Icon type="ios-paper-outline" style="margin-right: 5px"/>简答题</DropdownItem>
                  </DropdownMenu>
                </Dropdown>
              </div>
              <div style="width: 180px;margin-left: 55px">
                <span style="color: #C0C4CC;font-size: 10px;">创建时间：{{question.createTime}}</span>
                <span style="color: #C0C4CC;font-size: 10px;">更新时间：{{question.updateTime}}</span>
              </div>
              <Tooltip content="题目统计" placement="bottom">
                <Button style="border-radius: 5px 0px 0px 5px;margin-left: 5px;width: 50px" icon="ios-pie-outline"></Button>
              </Tooltip>
              <Button style="border-radius: 0px" :icon="openQuestionIds.indexOf(question.pkId) !== -1 ? 'ios-contract':'ios-expand'" @click="openQuestion(question.pkId,question.type)">{{openQuestionIds.indexOf(question.pkId) !== -1? '收起':'展开'}}</Button>
              <Button style="border-radius: 0px" icon="ios-create-outline" @click="addButton(question)">编辑</Button>
              <Button style="border-radius: 0px" icon="ios-copy-outline">复制</Button>
              <Button style="border-radius: 0px 5px 5px 0px" icon="ios-trash-outline" @click="deleteQuestion(question)">删除</Button>
            </Row>
            <Row style="margin-left: 10px;margin-top: 5px">
              <Tag color="blue" size="medium" style="margin-left: 10px" v-for="(questionKnowledgePoint,questionKnowledgePointIndex) in question.questionKnowledgePoint" :key="'questionKnowledgePoint'+questionKnowledgePointIndex">{{questionKnowledgePoint.name}}</Tag>
            </Row>
            <Row style="margin-left: 20px;margin-top: 10px">
              <div v-html="question.title"></div>
            </Row>
            <Row v-show="question.audio !== ''" style="margin-left: 20px;margin-top: 10px" align="middle">
              <Icon type="ios-paper-outline" />
              <span style="margin-left: 5px;">{{question.audioName}}</span>
              <Button type="primary" style="margin-left: 10px" size="small" icon="ios-paper-outline" @click="play(1,question.audio)">播放</Button>
            </Row>
            <Row v-show="question.video !== ''" style="margin-left: 20px;margin-top: 10px" align="middle">
              <Icon type="ios-paper-outline" />
              <span style="margin-left: 5px;">{{question.videoName}}</span>
              <Button type="primary" style="margin-left: 10px" size="small" icon="ios-paper-outline" @click="play(2,question.video)">播放</Button>
            </Row>
            <Row v-show="question.file !== ''" style="margin-left: 20px;margin-top: 10px" align="middle">
              <div style="width: 900px;margin-top: 5px" v-for="(fileName,fileIndex) in question.fileNameList" :key="question.pkId+'file'+fileIndex" >
                <Icon type="ios-paper-outline" />
                <a :href="uploadHost + '/' + question.fileList[fileIndex]">
                  <span style="margin-left: 5px">{{fileName}}</span>
                </a>
              </div>
            </Row>
            <Row v-show="question.analysisAudio !== ''" style="margin-left: 20px;margin-top: 10px" align="middle">
              <Icon type="ios-paper-outline" />
              <span style="margin-left: 5px;">{{question.analysisAudioName}}</span>
              <Button type="primary" style="margin-left: 10px" size="small" icon="ios-paper-outline" @click="play(4,question.analysisAudio)">播放</Button>
            </Row>
            <Row v-show="question.analysisVideo !== ''" style="margin-left: 20px;margin-top: 10px" align="middle">
              <Icon type="ios-paper-outline" />
              <span style="margin-left: 5px;">{{question.analysisVideoName}}</span>
              <Button type="primary" style="margin-left: 10px" size="small" icon="ios-paper-outline" @click="play(5,question.analysisVideo)">播放</Button>
            </Row>
            <Row style="margin-left: 20px;margin-top: 10px" v-show="openQuestionIds.indexOf(question.pkId) !== -1 && question.type === 0">
              <RadioGroup v-model="JSON.parse(question.answer).answerId">
                <Radio style="width: 900px;margin-top: 10px;color: #C0C4CC;" v-for="(option, singleChoiceIndex) in JSON.parse(question.answer).answerOptionList" :key="question.pkId+'singleChoice'+singleChoiceIndex" :label="option.id" disabled>{{optionTitle[singleChoiceIndex] + '、' +option.content}}</Radio>
              </RadioGroup>
            </Row>
            <Row style="margin-left: 20px;margin-top: 10px" v-show="openQuestionIds.indexOf(question.pkId) !== -1 && question.type === 1">
              <CheckboxGroup  v-model="JSON.parse(question.answer).answerIds">
                <Checkbox style="width: 900px;margin-top: 10px;color: #C0C4CC;" v-for="(option, multipleChoiceIndex) in JSON.parse(question.answer).answerOptionList" :key="question.pkId+'multipleChoice'+multipleChoiceIndex" :label="option.id" disabled>{{optionTitle[multipleChoiceIndex] + '、' +option.content}}</Checkbox>
              </CheckboxGroup>
            </Row>
            <Row style="margin-left: 20px;margin-top: 20px" v-show="openQuestionIds.indexOf(question.pkId) !== -1 && question.type === 2">
              <span style="color: #C0C4CC;">答案：</span>
              <div style="color: #C0C4CC;" v-for="(fillBlanks, fillBlanksIndex) in JSON.parse(question.answer).answerList" :key="question.pkId+'fillBlanks'+fillBlanksIndex">
                <span  v-for="(answer, fillBlanksAnswerIndex) in fillBlanks" :key="question.pkId+'fillBlanksAnswer'+fillBlanksAnswerIndex">{{answer+((fillBlanksIndex === JSON.parse(question.answer).answerList.length-1 && fillBlanksAnswerIndex === fillBlanks.length-1) ? '': '、')}}</span>
              </div>
            </Row>
            <Row style="margin-left: 20px;margin-top: 10px" v-show="openQuestionIds.indexOf(question.pkId) !== -1 && question.type === 3">
              <RadioGroup v-model="JSON.parse(question.answer).answer">
                <Radio style="margin-top: 10px;color: #C0C4CC;" disabled label="true">A、正确</Radio>
                <Radio style="margin-top: 10px;margin-left: 50px;color: #C0C4CC;" disabled label="false">B、错误</Radio>
              </RadioGroup>
            </Row>
            <Row style="margin-left: 20px;margin-top: 10px" v-show="openQuestionIds.indexOf(question.pkId) !== -1 && question.type === 5" :key="combinationQuestionTimer">
              <CheckboxGroup>
                <Row justify="center" v-for="(combinationQuestion,combinationQuestionIndex) in combinationQuestionList[question.pkId]" :key="'combinationQuestion'+combinationQuestionIndex">
                  <Card style="width: 925px;margin-top: 5px">
                    <Row align="middle">
                      <Checkbox>
                        <span style="font-size: 20px">{{index+1}}-{{combinationQuestionIndex+1}}、[{{combinationQuestion.typeName}}]</span>
                        <span>[难度系数：{{getDifficultyName(combinationQuestion.difficulty)}}]</span>
                      </Checkbox>
                      <div style="width: 180px;margin-left: 80px">
                        <span style="color: #C0C4CC;font-size: 10px;">创建时间：{{combinationQuestion.createTime}}</span>
                        <span style="color: #C0C4CC;font-size: 10px;">更新时间：{{combinationQuestion.updateTime}}</span>
                      </div>
                      <Tooltip content="题目统计" placement="bottom">
                        <Button style="border-radius: 5px 0px 0px 5px;margin-left: 5px;width: 50px" icon="ios-pie-outline"></Button>
                      </Tooltip>
                      <Button style="border-radius: 0px" :icon="openQuestionIds.indexOf(combinationQuestion.pkId) !== -1 ? 'ios-contract':'ios-expand'" @click="openQuestion(combinationQuestion.pkId,combinationQuestion.type)">{{openQuestionIds.indexOf(combinationQuestion.pkId) !== -1? '收起':'展开'}}</Button>
                      <Button style="border-radius: 0px" icon="ios-create-outline" @click="addButton(combinationQuestion)">编辑</Button>
                      <Button style="border-radius: 0px" icon="ios-copy-outline">复制</Button>
                      <Button style="border-radius: 0px 5px 5px 0px" icon="ios-trash-outline" @click="deleteQuestion(combinationQuestion,combinationQuestion.questionId)">删除</Button>
                    </Row>
                    <Row style="margin-left: 10px;margin-top: 5px">
                      <Tag color="blue" size="medium" style="margin-left: 10px" v-for="(questionKnowledgePoint,combinationQuestionKnowledgePointIndex) in combinationQuestion.questionKnowledgePoint" :key="combinationQuestion.pkId+'questionKnowledgePoint'+combinationQuestionKnowledgePointIndex">{{questionKnowledgePoint.name}}</Tag>
                    </Row>
                    <Row style="margin-left: 20px;margin-top: 10px">
                      <div v-html="combinationQuestion.title"></div>
                    </Row>
                    <Row v-show="combinationQuestion.audio !== ''" style="margin-left: 20px;margin-top: 10px" align="middle">
                      <Icon type="ios-paper-outline" />
                      <span style="margin-left: 5px;">{{combinationQuestion.audioName}}</span>
                      <Button type="primary" style="margin-left: 10px" size="small" icon="ios-paper-outline" @click="play(1,combinationQuestion.audio)">播放</Button>
                    </Row>
                    <Row v-show="combinationQuestion.video !== ''" style="margin-left: 20px;margin-top: 10px" align="middle">
                      <Icon type="ios-paper-outline" />
                      <span style="margin-left: 5px;">{{combinationQuestion.videoName}}</span>
                      <Button type="primary" style="margin-left: 10px" size="small" icon="ios-paper-outline" @click="play(2,combinationQuestion.video)">播放</Button>
                    </Row>
                    <Row v-show="combinationQuestion.file !== ''" style="margin-left: 20px;margin-top: 10px" align="middle">
                      <div style="width: 900px;margin-top: 5px" v-for="(fileName,combinationFileIndex) in combinationQuestion.fileNameList" :key="combinationQuestion.pkId+'file'+combinationFileIndex" >
                        <Icon type="ios-paper-outline" />
                        <a :href="uploadHost + '/' + combinationQuestion.fileList[combinationFileIndex]">
                          <span style="margin-left: 5px">{{fileName}}</span>
                        </a>
                      </div>
                    </Row>
                    <Row v-show="combinationQuestion.analysisAudio !== ''" style="margin-left: 20px;margin-top: 10px" align="middle">
                      <Icon type="ios-paper-outline" />
                      <span style="margin-left: 5px;">{{combinationQuestion.analysisAudioName}}</span>
                      <Button type="primary" style="margin-left: 10px" size="small" icon="ios-paper-outline" @click="play(4,combinationQuestion.analysisAudio)">播放</Button>
                    </Row>
                    <Row v-show="combinationQuestion.analysisVideo !== ''" style="margin-left: 20px;margin-top: 10px" align="middle">
                      <Icon type="ios-paper-outline" />
                      <span style="margin-left: 5px;">{{combinationQuestion.analysisVideoName}}</span>
                      <Button type="primary" style="margin-left: 10px" size="small" icon="ios-paper-outline" @click="play(5,combinationQuestion.analysisVideo)">播放</Button>
                    </Row>
                    <Row style="margin-left: 20px;margin-top: 10px" v-show="openQuestionIds.indexOf(combinationQuestion.pkId) !== -1 && combinationQuestion.type === 0">
                      <RadioGroup v-model="JSON.parse(combinationQuestion.answer).answerId">
                        <Radio style="width: 900px;margin-top: 10px;color: #C0C4CC;" v-for="(option, combinationSingleChoiceIndex) in JSON.parse(combinationQuestion.answer).answerOptionList" :key="combinationQuestion.pkId+'singleChoice'+combinationSingleChoiceIndex" :label="option.id" disabled>{{optionTitle[combinationSingleChoiceIndex] + '、' +option.content}}</Radio>
                      </RadioGroup>
                    </Row>
                    <Row style="margin-left: 20px;margin-top: 10px" v-show="openQuestionIds.indexOf(combinationQuestion.pkId) !== -1 && combinationQuestion.type === 1">
                      <CheckboxGroup  v-model="JSON.parse(combinationQuestion.answer).answerIds">
                        <Checkbox style="width: 900px;margin-top: 10px;color: #C0C4CC;" v-for="(option, combinationMultipleChoiceIndex) in JSON.parse(combinationQuestion.answer).answerOptionList" :key="combinationQuestion.pkId+'multipleChoice'+combinationMultipleChoiceIndex" :label="option.id" disabled>{{optionTitle[combinationMultipleChoiceIndex] + '、' + option.content}}</Checkbox>
                      </CheckboxGroup>
                    </Row>
                    <Row style="margin-left: 20px;margin-top: 20px" v-show="openQuestionIds.indexOf(combinationQuestion.pkId) !== -1 && combinationQuestion.type === 2">
                      <span style="color: #C0C4CC;">答案：</span>
                      <div style="color: #C0C4CC;" v-for="(fillBlanks, combinationFillBlanksIndex) in JSON.parse(combinationQuestion.answer).answerList" :key="combinationQuestion.pkId+'fillBlanks'+combinationFillBlanksIndex">
                        <span  v-for="(answer, combinationFillBlanksAnswerIndex) in fillBlanks" :key="combinationQuestion.pkId+'fillBlanksAnswer'+combinationFillBlanksAnswerIndex">{{answer+((combinationFillBlanksIndex === JSON.parse(combinationQuestion.answer).answerList.length-1 && combinationFillBlanksAnswerIndex === fillBlanks.length-1) ? '': '、')}}</span>
                      </div>
                    </Row>
                    <Row style="margin-left: 20px;margin-top: 10px" v-show="openQuestionIds.indexOf(combinationQuestion.pkId) !== -1 && combinationQuestion.type === 3">
                      <RadioGroup v-model="JSON.parse(combinationQuestion.answer).answer">
                        <Radio style="margin-top: 10px;color: #C0C4CC;" disabled label="true">A、正确</Radio>
                        <Radio style="margin-top: 10px;margin-left: 50px;color: #C0C4CC;" disabled label="false">B、错误</Radio>
                      </RadioGroup>
                    </Row>
                    <Row style="margin-left: 20px;margin-top: 20px" v-show="openQuestionIds.indexOf(combinationQuestion.pkId) !== -1">
                      <span style="color: #C0C4CC;">解析：</span>
                      <div v-html="combinationQuestion.analysis" style="color: #C0C4CC;"></div>
                    </Row>
                  </Card>
                </Row>
              </CheckboxGroup>
            </Row>
            <Row style="margin-left: 20px;margin-top: 20px" v-show="openQuestionIds.indexOf(question.pkId) !== -1">
              <span style="color: #C0C4CC;">解析：</span>
              <div v-html="question.analysis" style="color: #C0C4CC;"></div>
            </Row>
          </Card>
        </Row>
      </CheckboxGroup>
    </Card>
    <!--添加/修改题目对话框-->
    <Modal v-model="addModal" :closable='true' :mask-closable=false :width="900" @on-visible-change="resetFormData">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">{{!editForm.pkId?'新增题目':'编辑题目'}}</h3>
      <Form ref="editForm" :model="editForm" label-position="right" :label-width="10"
            @submit.native.prevent="saveEdit">
        <FormItem prop="title">
          <Row>
           <span style="font-size: 20px">[{{getTypeName(editForm.type)}}]</span>
           <span>[难度系数：{{getDifficultyName(editForm.difficulty)}}]</span>
         </Row>
          <Row v-show="editForm.type === 2">
            <span style="color: red">提示：填空用连续三个下划线"_"表示，若一个空有多个参考答案，请添加同义词，学员答案匹配任意一个都算正确。</span>
          </Row>
          <Row>
            <div style="width: 100px;height: 370px;background-color: #F5F7FA;text-align: center;line-height: 370px">
              <span>题干</span>
            </div>
            <div style="width: 750px;height: 370px">
              <tinymce-editor h='370' :content.sync='editForm.title'></tinymce-editor>
            </div>
          </Row>
        </FormItem>
        <FormItem prop="knowledgePoint">
          <Row>
            <Button type="warning" icon="ios-settings-outline" @click="showKnowledgePoint">设置知识点</Button>
          </Row>
          <Row style="margin-top: 10px">
            <Tag color="blue" size="medium" v-for="(questionKnowledgePoint,index) in editForm.questionKnowledgePoint" :key="'knowledgePoint'+index">{{questionKnowledgePoint.name}}<Icon type="md-close" style="cursor: pointer;margin-left: 5px" @click="saveKnowledgePoint(questionKnowledgePoint)" /></Tag>
          </Row>
        </FormItem>
        <FormItem prop="audio">
          <Upload :action="uploadHost" ref="uploadOss" :show-upload-list="false" :data="uploadData"
                  :format="['mp3']"
                  :on-success="(res) => handleOssSuccess(res,1)"
                  :max-size="5120"
                  :on-format-error="handleOssFormatError"
                  :on-exceeded-size="handleOssMaxSize"
                  :before-upload="(file) => handleOssBeforeUpload(file, 1)">
            <Button type="primary" :loading="uploadLoading && uploadType === 1" :disabled="uploadLoading && uploadType !== 1">音频上传</Button>
          </Upload>
          <Row style="margin-top: 10px">请注意您只能上传.mp3格式的音频文件(小于5MB),上传成功之后，点击列表中的名称进行预览</Row>
          <Row v-show="editForm.audio !== ''" style="margin-top: 10px" align="middle">
            <Icon type="ios-paper-outline" />
            <span style="margin-left: 5px;cursor: pointer" @click="play(1,editForm.audio)">{{editForm.audioName}}</span>
            <Icon style="margin-left: 5px;cursor: pointer" type="ios-close" @click="handleRemove(1)" />
          </Row>
        </FormItem>
        <FormItem prop="video">
          <Upload :action="uploadHost" ref="uploadOss" :show-upload-list="false" :data="uploadData"
                  :format="['mp4']"
                  :on-success="(res) => handleOssSuccess(res,2)"
                  :max-size="512000"
                  :on-format-error="handleOssFormatError"
                  :on-exceeded-size="handleOssMaxSize"
                  :before-upload="(file) => handleOssBeforeUpload(file, 2)">
            <Button type="primary" :loading="uploadLoading && uploadType === 2" :disabled="uploadLoading && uploadType !== 2">视频上传</Button>
          </Upload>
          <Row>请注意您只能上传.mp4格式的视频文件(小于50MB),上传成功之后，点击列表中的名称进行预览</Row>
          <Row v-show="editForm.video !== ''" style="margin-top: 10px" align="middle">
            <Icon type="ios-paper-outline" />
            <span style="margin-left: 5px;cursor: pointer" @click="play(2,editForm.video)">{{editForm.videoName}}</span>
            <Icon style="margin-left: 5px;cursor: pointer" type="ios-close" @click="handleRemove(2)" />
          </Row>
        </FormItem>
        <FormItem prop="file">
          <Upload :action="uploadHost" ref="uploadOss" :show-upload-list="false" :data="uploadData"
                  :format="['doc','docx','xls','xlsx','ppt','pdf']"
                  :on-success="(res) => handleOssSuccess(res,3)"
                  :max-size="5120"
                  :on-format-error="handleOssFormatError"
                  :on-exceeded-size="handleOssMaxSize"
                  :before-upload="(file) => handleOssBeforeUpload(file, 3)">
            <Button type="primary" :loading="uploadLoading && uploadType === 3" :disabled="uploadLoading && uploadType !== 3">上传文档附件</Button>
          </Upload>
          <Row>上传文档附件</Row>
          <div v-show="editForm.fileList.length !== 0">
            <Row v-for="(fileName,index) in editForm.fileNameList" :key="'file'+index" align="middle">
              <Icon type="ios-paper-outline" />
              <a :href="uploadHost + '/' + editForm.fileList[index]">
                <span style="margin-left: 5px">{{fileName}}</span>
              </a>
              <Icon style="margin-left: 5px;cursor: pointer" type="ios-close" @click="deleteFile(index)" />
            </Row>
          </div>
        </FormItem>
        <FormItem prop="analysis">
          <Row>
            <div style="width: 100px;height: 150px;background-color: #F5F7FA;text-align: center;line-height: 150px">
              <span>解析</span>
            </div>
            <div style="width: 750px;height: 150px">
              <tinymce-editor h='150' :showMenubar='true' :content.sync='editForm.analysis'></tinymce-editor>
            </div>
          </Row>
        </FormItem>
        <FormItem prop="analysisAudio">
          <Upload :action="uploadHost" ref="uploadOss" :show-upload-list="false" :data="uploadData"
                  :format="['mp3']"
                  :on-success="(res) => handleOssSuccess(res,4)"
                  :max-size="5120"
                  :on-format-error="handleOssFormatError"
                  :on-exceeded-size="handleOssMaxSize"
                  :before-upload="(file) => handleOssBeforeUpload(file, 4)">
            <Button type="primary" :loading="uploadLoading && uploadType === 4" :disabled="uploadLoading && uploadType !== 4">音频上传</Button>
          </Upload>
          <Row style="margin-top: 10px">请注意您只能上传.mp3格式的音频文件(小于5MB),上传成功之后，点击列表中的名称进行预览</Row>
          <Row v-show="editForm.analysisAudio !== ''" style="margin-top: 10px" align="middle">
            <Icon type="ios-paper-outline" />
            <span style="margin-left: 5px;cursor: pointer" @click="play(4,editForm.analysisAudio)">{{editForm.analysisAudioName}}</span>
            <Icon style="margin-left: 5px;cursor: pointer" type="ios-close" @click="handleRemove(4)" />
          </Row>
        </FormItem>
        <FormItem prop="analysisVideo">
          <Upload :action="uploadHost" ref="uploadOss" :show-upload-list="false" :data="uploadData"
                  :format="['mp4']"
                  :on-success="(res) => handleOssSuccess(res,5)"
                  :max-size="512000"
                  :on-format-error="handleOssFormatError"
                  :on-exceeded-size="handleOssMaxSize"
                  :before-upload="(file) => handleOssBeforeUpload(file, 5)">
            <Button type="primary" :loading="uploadLoading && uploadType === 5" :disabled="uploadLoading && uploadType !== 5">视频上传</Button>
          </Upload>
          <Row>请注意您只能上传.mp4格式的视频文件(小于50MB),上传成功之后，点击列表中的名称进行预览</Row>
          <Row v-show="editForm.analysisVideo !== ''" style="margin-top: 10px" align="middle">
            <Icon type="ios-paper-outline" />
            <span style="margin-left: 5px;cursor: pointer" @click="play(5,editForm.analysisVideo)">{{editForm.analysisVideoName}}</span>
            <Icon style="margin-left: 5px;cursor: pointer" type="ios-close" @click="handleRemove(5)" />
          </Row>
        </FormItem>
        <FormItem v-if="editForm.type === 0" prop="singleChoiceAnswer">
          <Row align="middle">
            <span style="margin-left: 250px">选项文字</span>
            <span style="margin-left: 265px">答案</span>
            <span style="margin-left: 130px">操作</span>
          </Row>
          <RadioGroup v-model="singleChoiceAnswerForm.answerId" :key="optionTimer">
            <Row align="middle" style="margin-top: 15px" v-for="(option, index) in singleChoiceAnswerForm.answerOptionList" :key="'singleChoiceAnswerOption'+index">
              <div style="width: 45px;height: 33px;border-radius: 5px 0px 0px 5px;background-color: #F5F7FA;text-align: center;line-height: 33px;border: 1px solid #e8eaec">
                <span>{{optionTitle[index]}}</span>
              </div>
              <div style="width: 480px;height: 33px">
                <Input v-model="option.content"/>
              </div>
              <Radio style="margin-left: 55px" :label="option.id">{{''}}</Radio>
              <Button type="primary" shape="circle" style="margin-left: 50px" icon="ios-add" size="small" @click="saveSingleChoiceOption(0, index)"></Button>
              <Button :disabled="singleChoiceAnswerForm.answerOptionList.length === 1" type="primary" shape="circle" style="margin-left: 25px" icon="ios-remove" size="small" @click="saveSingleChoiceOption(1, index)"></Button>
              <Button :disabled="index === 0" type="primary" shape="circle" style="margin-left: 25px" icon="ios-arrow-up" size="small" @click="saveSingleChoiceOption(2, index)"></Button>
              <Button :disabled="(index+1) === singleChoiceAnswerForm.answerOptionList.length" type="primary" shape="circle" style="margin-left: 25px" icon="ios-arrow-down" size="small" @click="saveSingleChoiceOption(3, index)"></Button>
            </Row>
          </RadioGroup>
        </FormItem>
        <FormItem v-else-if="editForm.type === 1" prop="multipleChoiceAnswer">
          <Row align="middle">
            <span style="margin-left: 250px">选项文字</span>
            <span style="margin-left: 265px">答案</span>
            <span style="margin-left: 130px">操作</span>
          </Row>
          <CheckboxGroup v-model="multipleChoiceAnswerForm.answerIds" :key="optionTimer">
            <Row align="middle" style="margin-top: 15px" v-for="(option, index) in multipleChoiceAnswerForm.answerOptionList" :key="'multipleChoiceAnswerOption'+index">
              <div style="width: 45px;height: 33px;border-radius: 5px 0px 0px 5px;background-color: #F5F7FA;text-align: center;line-height: 33px;border: 1px solid #e8eaec">
                <span>{{optionTitle[index]}}</span>
              </div>
              <div style="width: 480px;height: 33px">
                <Input v-model="option.content"/>
              </div>
              <Checkbox style="margin-left: 55px" :label="option.id">{{''}}</Checkbox>
              <Button type="primary" shape="circle" style="margin-left: 50px" icon="ios-add" size="small" @click="saveMultipleChoiceOption(0, index)"></Button>
              <Button :disabled="multipleChoiceAnswerForm.answerOptionList.length === 1" type="primary" shape="circle" style="margin-left: 25px" icon="ios-remove" size="small" @click="saveMultipleChoiceOption(1, index)"></Button>
              <Button :disabled="index === 0" type="primary" shape="circle" style="margin-left: 25px" icon="ios-arrow-up" size="small" @click="saveMultipleChoiceOption(2, index)"></Button>
              <Button :disabled="(index+1) === multipleChoiceAnswerForm.answerOptionList.length" type="primary" shape="circle" style="margin-left: 25px" icon="ios-arrow-down" size="small" @click="saveMultipleChoiceOption(3, index)"></Button>
            </Row>
          </CheckboxGroup>
        </FormItem>
        <FormItem v-else-if="editForm.type === 2" prop="fillBlanksAnswer">
          <Row align="middle">
            <Checkbox v-model="fillBlanksForm.isOrder">答案不分顺序</Checkbox>
            <Checkbox v-model="fillBlanksForm.isCase" style="margin-left: 20px">忽略大小写</Checkbox>
          </Row>
          <Row align="middle" style="margin-top: 15px" v-for="(answerList, listIndex) in fillBlanksForm.answerList" :key="'fillBlanksAnswerList'+listIndex">
            <div style="width: 100px;height: 45px;border-radius: 5px 0px 0px 5px;background-color: #F5F7FA;text-align: center;line-height: 45px;border: 1px solid #e8eaec">
              <span>第{{listIndex+1}}空答案</span>
            </div>
            <div style="width: 750px;height: 45px;line-height: 40px;border: 1px solid #e8eaec">
              <Tag color="blue" size="medium" style="margin-left: 5px" v-for="(answer,index) in answerList" :key="'fillBlanksAnswer'+index">{{answer}}<Icon type="md-create" style="cursor: pointer" @click="addAnswerKeyWorld({ type: 0, answerListIndex: listIndex, answerIndex: index, answer: answer })" /><Icon type="md-close" style="cursor: pointer;margin-left: 5px" @click="deleteFillBlanksAnswer(listIndex, index)" /></Tag>
              <Button style="margin-left: 10px" icon="md-add" @click="addAnswerKeyWorld({ type: 0, answerListIndex: listIndex, answerIndex: -1, answer: '' })">同义词</Button>
            </div>
          </Row>
      </FormItem>
        <FormItem v-else-if="editForm.type === 3" prop="judgeAnswer">
          <Row>
            <span style="margin-right: 40px">答案</span>
            <RadioGroup v-model="judgeAnswerForm.answer">
              <Radio label="true">正确</Radio>
              <Radio label="false">错误</Radio>
            </RadioGroup>
          </Row>
        </FormItem>
        <FormItem v-else-if="editForm.type === 4" prop="briefAnswer">
          <Row align="middle">
            <span>答案设置</span>
            <Checkbox v-model="briefAnswerForm.isCase" style="margin-left: 10px">忽略大小写</Checkbox>
            <Button type="primary" style="margin-left: 50px" @click="addBriefAnswer">新增关键词</Button>
            <span style="color: red;margin-left: 5px"><Icon type="ios-information-circle" />友情提示：级别在组卷过程中可用于批量设置分数</span>
          </Row>
          <Row align="middle" style="margin-top: 15px" v-for="(briefAnswer, listIndex) in briefAnswerForm.answerList" :key="'briefAnswerList'+listIndex">
            <Button type="success" icon="ios-trash-outline" @click="deleteBriefAnswerList(listIndex)"></Button>
            <span style="margin-left: 10px">级别：</span>
            <Select v-model="briefAnswer.level" style="width:50px">
              <Option v-for="item in briefAnswerLevel" :disabled="item.disabled" :value="item.level" :key="'briefAnswerLevel'+item.level">{{ item.level }}</Option>
            </Select>
            <span style="margin-left: 10px">关键词：{{briefAnswer.keyWords.length === 0 ? '暂无':''}}</span>
            <Tag color="blue" size="medium" style="margin-left: 5px" v-for="(answer,index) in briefAnswer.keyWords" :key="'briefAnswer'+index">{{answer}}<Icon type="md-create" style="cursor: pointer" @click="addAnswerKeyWorld({ type: 1, answerListIndex: listIndex, answerIndex: index, answer: answer })" /><Icon type="md-close" style="cursor: pointer;margin-left: 5px" @click="deleteBriefAnswer(listIndex, index)" /></Tag>
            <Button style="margin-left: 10px" icon="md-add" size="small" @click="addAnswerKeyWorld({ type: 1, answerListIndex: listIndex, answerIndex: -1, answer: '' })">添加同义词</Button>
          </Row>
        </FormItem>
        <FormItem prop="difficulty">
          <Row>
            <span style="margin-right: 10px">难度系数</span>
            <RadioGroup v-model="editForm.difficulty">
              <Radio :label="Number(0)">高</Radio>
              <Radio :label="Number(1)">中</Radio>
              <Radio :label="Number(2)">低</Radio>
            </RadioGroup>
          </Row>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="cancel">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveEdit">提交</Button>
      </div>
    </Modal>
    <!--设置知识点对话框-->
    <Modal v-model="knowledgePointModal" :closable="true" :mask-closable="false" :width="800">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">设置题目知识点</h3>
      <Row>
        <Tooltip content="刷新" placement="bottom">
          <Button style="margin-left: 10px" icon="ios-refresh" @click="getKnowledgePoint"></Button>
        </Tooltip>
        <Input style="width: 200px;margin-left: 400px" v-model="knowledgePointName" placeholder="请输入知识点名称"/>
        <ButtonGroup>
          <Button style="background-color: #F5F7FA" @click="getKnowledgePoint()">搜索</Button>
          <Button style="background-color: #F5F7FA" @click="getKnowledgePoint('reset')">重置</Button>
        </ButtonGroup>
      </Row>
      <Row style="margin-top: 10px">
        <Table ref="knowledgePointSelection" :columns="knowledgePointColumns" :data="knowledgePointTableData" :highlight-row="true" :current="knowledgePointPageIndex" style="width: 100%;" :key="timer"
               @on-selection-change="knowledgePointSelectionChange" :loading="knowledgePointTableLoading"></Table>
      </Row>
      <Row style="margin-top: 10px" justify="end">
        <Page :total="knowledgePointTotalCount" :current="knowledgePointPageIndex" :page-sise="knowledgePointPageSize" loading show-total show-sizer show-elevator
              @on-change="knowledgePointPageChange" @on-page-size-change="knowledgePointSizeChange">
        </Page>
      </Row>
      <div slot="footer">
        <Button @click="knowledgePointModal = false">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveKnowledgePoint()">确定</Button>
      </div>
    </Modal>
    <!--音/视频播放对话框-->
    <Modal v-model="playModal" :closable="true" :mask-closable="false" :width="800" @on-visible-change="playTimer = new Date().getTime()">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">{{(playType === 1 || playType === 4) ? '音频':'视频'}}播放</h3>
      <aplayer v-if="playType === 1 || playType === 4" style="margin-top: 5px" :playerOptions="{src: uploadHost + '/' + filePath}" :key="playTimer" />
      <vplayer v-else style="margin-top: 5px" :playerOptions = "{src: uploadHost + '/' + filePath}" :key="playTimer" />
      <div slot="footer"></div>
    </Modal>
    <!--编辑答案关键词-->
    <Modal v-model="answerKeyWorldModal" :closable="true" :mask-closable="false" :width="650" @on-visible-change="resetAnswerKeyWorld">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">编辑答案关键词</h3>
      <Row>
        <Input v-model="answerKeyWorld.answer" style="width: 610px" />
      </Row>
      <div slot="footer">
        <Button @click="answerKeyWorldModal = false">关闭</Button>
        <Button type="primary" :loading="saveLoading" @click="saveAnswerKeyWorld()">确定</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import API from '@/api'
import tinymceEditor from '_c/text-editor/tinymce-editor.vue'
import { aplayer, vplayer } from 'vue-hls-player'
import { randomString } from '@/libs/util'
import { ossUrl } from '@/libs/ossUrl'
import { oss } from '@/libs/ossUtil'

export default {
  name: 'question',
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
      uploadType: 0,
      playType: 0,
      timer: '',
      combinationQuestionTimer: '',
      playTimer: '',
      optionTimer: '',
      type: -1,
      difficulty: -1,
      title: '',
      indeterminate: false,
      checkAll: false,
      checkAllGroup: [],
      openQuestionIds: [],
      isOpenAll: false,
      knowledgePointName: '',
      loading: false,
      knowledgePointTableLoading: false,
      uploadLoading: false,
      saveLoading: false,
      knowledgePointModal: false,
      addModal: false,
      playModal: false,
      answerKeyWorldModal: false,
      totalCount: 0,
      knowledgePointTotalCount: 0,
      pageIndex: 1,
      knowledgePointPageIndex: 1,
      pageSize: 10,
      knowledgePointPageSize: 10,
      questionBankTypeList: [],
      editForm: {
        pkId: 0,
        bankId: 0,
        questionId: 0,
        type: -1,
        title: '',
        knowledgePoint: '',
        audioName: '',
        audio: '',
        videoName: '',
        video: '',
        fileName: '',
        fileNameList: [],
        file: '',
        fileList: [],
        analysis: '',
        analysisAudioName: '',
        analysisAudio: '',
        analysisVideoName: '',
        analysisVideo: '',
        answer: '{}',
        difficulty: 1
      },
      optionTitle: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'],
      singleChoiceAnswerForm: {
        answerId: '',
        answerOptionList: [
          {
            id: randomString(10),
            content: '选项'
          },
          {
            id: randomString(10),
            content: '选项'
          },
          {
            id: randomString(10),
            content: '选项'
          },
          {
            id: randomString(10),
            content: '选项'
          }
        ]
      },
      multipleChoiceAnswerForm: {
        answerIds: [],
        answerOptionList: [
          {
            id: randomString(10),
            content: '选项'
          },
          {
            id: randomString(10),
            content: '选项'
          },
          {
            id: randomString(10),
            content: '选项'
          },
          {
            id: randomString(10),
            content: '选项'
          }
        ]
      },
      judgeAnswerForm: {
        answer: 'true'
      },
      fillBlanksForm: {
        isOrder: false,
        isCase: false,
        answerList: []
      },
      briefAnswerForm: {
        isCase: false,
        answerList: []
      },
      briefAnswerLevel: [
        {
          level: 1,
          disabled: false
        },
        {
          level: 2,
          disabled: false
        },
        {
          level: 3,
          disabled: false
        },
        {
          level: 4,
          disabled: false
        },
        {
          level: 5,
          disabled: false
        }
      ],
      answerKeyWorld: {
        type: -1,
        answerListIndex: -1,
        answerIndex: -1,
        answer: ''
      },
      knowledgePointForm: {
        pkId: 0,
        typeId: 0,
        name: ''
      },
      knowledgePointSelection: [],
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
          type: 'selection',
          width: 60,
          align: 'right'
        },
        {
          title: '名称',
          key: 'name',
          align: 'left',
          minWidth: 100
        },
        {
          title: '创建时间',
          key: 'createTime',
          align: 'left',
          minWidth: 170
        }
      ],
      tableData: [],
      combinationQuestionList: {},
      knowledgePointTableData: []
    }
  },
  watch: {
    $route: function (to, from) {
      if (to.name === 'question') {
        this.isOpenAll = false
        this.openQuestionIds = []
        this.tableData = []
        this.combinationQuestionList = {}
        this.init('reset')
      }
    },
    'editForm.title': function (val, oldVal) {
      if (val !== oldVal) {
        let blanks = this.editForm.title.split('___').length - 1
        let answers = this.fillBlanksForm.answerList.length
        if (blanks > answers) {
          this.fillBlanksForm.answerList.push([blanks.toString()])
        } else if (blanks < answers) {
          this.fillBlanksForm.answerList.splice(answers - 1, 1)
        }
      }
    }
  },
  methods: {
    goBack () {
      this.$router.go(-1)
    },
    init (reset) {
      if (reset === 'reset') {
        this.type = -1
        this.difficulty = -1
        this.title = ''
      }
      this.loading = true
      let searchParams = {
        pageSize: this.pageSize,
        pageIndex: this.pageIndex,
        bankId: this.$route.query.id,
        type: this.type,
        difficulty: this.difficulty,
        title: this.title
      }
      API.question.list(searchParams).then(({ data }) => {
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
    getCombinationQuestionList (questionId) {
      let searchParams = {
        pageSize: 100,
        pageIndex: 1,
        bankId: this.$route.query.id,
        questionId: questionId
      }
      API.question.list(searchParams).then(({ data }) => {
        if (data && data.code === 0) {
          this.combinationQuestionList[questionId] = data.data.list
          this.combinationQuestionTimer = new Date().getTime()
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    handleCheckAll () {
      if (this.indeterminate && this.checkAllGroup.length === this.tableData.length) {
        this.checkAll = false
      } else {
        this.checkAll = !this.checkAll
      }
      this.indeterminate = false

      if (this.checkAll) {
        this.checkAllGroup = this.tableData.map(question => {
          return question.pkId
        })
      } else {
        this.checkAllGroup = []
      }
    },
    checkAllGroupChange (data) {
      if (data.length === 3) {
        this.indeterminate = false
        this.checkAll = true
      } else if (data.length > 0) {
        this.indeterminate = true
        this.checkAll = false
      } else {
        this.indeterminate = false
        this.checkAll = false
      }
    },
    getTypeName (value) {
      let typeName = ''
      switch (value) {
        case 0: typeName = '单选题'; break
        case 1: typeName = '多选题'; break
        case 2: typeName = '填空题'; break
        case 3: typeName = '判断题'; break
        case 4: typeName = '简答题'; break
        case 5: typeName = '组合题'; break
      }
      return typeName
    },
    getDifficultyName (value) {
      let difficultyName = ''
      switch (value) {
        case 0: difficultyName = '高'; break
        case 1: difficultyName = '中'; break
        case 2: difficultyName = '低'; break
      }
      return difficultyName
    },
    openQuestion (id, type) {
      let index = this.openQuestionIds.indexOf(id)
      if (index === -1) {
        if (type === 5) {
          this.getCombinationQuestionList(id)
        }
        this.openQuestionIds.push(id)
      } else {
        this.openQuestionIds.splice(index, 1)
      }
      if (this.openQuestionIds.length === 0) {
        this.isOpenAll = false
      }
      if (this.openQuestionIds.length === this.tableData.length) {
        this.isOpenAll = true
      }
    },
    openAll () {
      if (!this.isOpenAll) {
        this.isOpenAll = true
        this.openQuestionIds = this.tableData.map(question => {
          return question.pkId
        })
      } else {
        this.isOpenAll = false
        this.openQuestionIds = []
      }
    },
    getKnowledgePoint (reset) {
      if (reset === 'reset') {
        this.knowledgePointName = ''
      }
      this.knowledgePointTableLoading = true
      let searchParams = {
        pageSize: this.knowledgePointPageSize,
        pageIndex: this.knowledgePointPageIndex,
        name: this.knowledgePointName,
        exceptKnowledgePointIds: this.editForm.knowledgePoint.split(',')
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
    functionChoose (name, questionId) {
      if (typeof questionId !== 'undefined') {
        this.editForm.questionId = questionId
      }
      switch (name) {
        case 0 : this.editForm.type = 0; this.addButton(); break
        case 1 : this.editForm.type = 1; this.addButton(); break
        case 2 : this.editForm.type = 2; this.addButton(); break
        case 3 : this.editForm.type = 3; this.addButton(); break
        case 4 : this.editForm.type = 4; this.addButton(); break
        case 5 : this.editForm.type = 5; this.addButton(); break
      }
    },
    addButton (params) {
      if (typeof params !== 'undefined') { // 如果是修改状态，则填充必要数据
        this.editForm = JSON.parse(JSON.stringify(params))
        switch (this.editForm.type) {
          case 0 : this.singleChoiceAnswerForm = JSON.parse(this.editForm.answer); break
          case 1 : this.multipleChoiceAnswerForm = JSON.parse(this.editForm.answer); break
          case 2 : this.fillBlanksForm = JSON.parse(this.editForm.answer); break
          case 3 : this.judgeAnswerForm = JSON.parse(this.editForm.answer); break
          case 4 : this.briefAnswerForm = JSON.parse(this.editForm.answer); break
        }
      }
      this.addModal = true
    },
    addBriefAnswer () {
      let level = 0
      this.briefAnswerForm.answerList.forEach((item) => {
        switch (item.level) {
          case 1:this.briefAnswerLevel[0].disabled = true; break
          case 2:this.briefAnswerLevel[1].disabled = true; break
          case 3:this.briefAnswerLevel[2].disabled = true; break
          case 4:this.briefAnswerLevel[3].disabled = true; break
          case 5:this.briefAnswerLevel[4].disabled = true; break
        }
      })
      for (let i in this.briefAnswerLevel) {
        if (!this.briefAnswerLevel[i].disabled) {
          level = this.briefAnswerLevel[i].level
          break
        }
      }
      if (!level) {
        this.$Message.error('关键词级别数目达到上限，不可再添加')
      } else {
        let briefAnswer = {
          level: level,
          keyWords: []
        }
        this.briefAnswerForm.answerList.push(briefAnswer)
      }
    },
    addAnswerKeyWorld (params) {
      if (typeof params !== 'undefined') { // 如果是修改状态，则填充必要数据
        this.answerKeyWorld = JSON.parse(JSON.stringify(params))
      }

      this.answerKeyWorldModal = true
    },
    showKnowledgePoint () {
      this.getKnowledgePoint()
      this.knowledgePointModal = true
    },
    knowledgePointSelectionChange (data) {
      // 记录选中的数据
      this.knowledgePointSelection = data
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
    saveSingleChoiceOption (type, index) {
      switch (type) {
        case 0 : let option = { id: randomString(10), content: '选项' }; this.singleChoiceAnswerForm.answerOptionList.splice(index + 1, 0, option); break
        case 1 : if (this.singleChoiceAnswerForm.answerOptionList[index].id === this.singleChoiceAnswerForm.answerId) { this.singleChoiceAnswerForm.answerId = '' }; this.singleChoiceAnswerForm.answerOptionList.splice(index, 1); break
        case 2 : this.moveOption(this.singleChoiceAnswerForm.answerOptionList, index, index - 1); break
        case 3 : this.moveOption(this.singleChoiceAnswerForm.answerOptionList, index, index + 1); break
      }
      this.optionTimer = new Date().getTime()
    },
    saveMultipleChoiceOption (type, index) {
      switch (type) {
        case 0 : let option = { id: randomString(10), content: '选项' }; this.multipleChoiceAnswerForm.answerOptionList.splice(index + 1, 0, option); break
        case 1 : let id = this.multipleChoiceAnswerForm.answerOptionList[index].id; if (id in this.multipleChoiceAnswerForm.answerIds) { this.multipleChoiceAnswerForm.answerIds.splice(this.multipleChoiceAnswerForm.answerIds.indexOf(id), 1) }; this.multipleChoiceAnswerForm.answerOptionList.splice(index, 1); break
        case 2 : this.moveOption(this.multipleChoiceAnswerForm.answerOptionList, index, index - 1); break
        case 3 : this.moveOption(this.multipleChoiceAnswerForm.answerOptionList, index, index + 1); break
      }
      this.optionTimer = new Date().getTime()
    },
    moveOption (list, index1, index2) {
      list[index1] = list.splice(index2, 1, list[index1])[0]
    },
    cancel () {
      this.addModal = false
    },
    resetFormData (val) {
      if (!val) {
        this.editForm = {
          pkId: 0,
          bankId: 0,
          questionId: 0,
          type: -1,
          title: '',
          knowledgePoint: '',
          audioName: '',
          audio: '',
          videoName: '',
          video: '',
          fileName: '',
          fileNameList: [],
          file: '',
          fileList: [],
          analysis: '',
          analysisAudioName: '',
          analysisAudio: '',
          analysisVideoName: '',
          analysisVideo: '',
          answer: '{}',
          difficulty: 1
        }
        this.singleChoiceAnswerForm = {
          answerId: '',
          answerOptionList: [
            {
              id: randomString(10),
              content: '选项'
            },
            {
              id: randomString(10),
              content: '选项'
            },
            {
              id: randomString(10),
              content: '选项'
            },
            {
              id: randomString(10),
              content: '选项'
            }
          ]
        }
        this.multipleChoiceAnswerForm = {
          answerIds: [],
          answerOptionList: [
            {
              id: randomString(10),
              content: '选项'
            },
            {
              id: randomString(10),
              content: '选项'
            },
            {
              id: randomString(10),
              content: '选项'
            },
            {
              id: randomString(10),
              content: '选项'
            }
          ]
        }
        this.fillBlanksForm = {
          isOrder: false,
          isCase: false,
          answerList: []
        }
        this.judgeAnswerForm = {
          answer: 'true'
        }
        this.briefAnswerForm = {
          isCase: false,
          answerList: []
        }
        this.briefAnswerLevel = [
          {
            level: 1,
            disabled: false
          },
          {
            level: 2,
            disabled: false
          },
          {
            level: 3,
            disabled: false
          },
          {
            level: 4,
            disabled: false
          },
          {
            level: 5,
            disabled: false
          }
        ]
      }
    },
    resetAnswerKeyWorld (val) {
      if (!val) {
        this.answerKeyWorld = {
          type: -1,
          answerListIndex: -1,
          answerIndex: -1,
          answer: ''
        }
      }
    },
    saveKnowledgePoint (knowledgePoint) {
      if (typeof knowledgePoint !== 'undefined') {
        this.editForm.questionKnowledgePoint.splice(this.editForm.questionKnowledgePoint.indexOf(knowledgePoint), 1)
        this.$forceUpdate()
        this.knowledgePointModal = false
      } else {
        this.editForm.questionKnowledgePoint = this.knowledgePointSelection
        this.knowledgePointModal = false
      }
      this.editForm.questionKnowledgePoint.forEach((item, index) => {
        if (index === 0) {
          this.editForm.knowledgePoint = item.pkId
        } else {
          this.editForm.knowledgePoint += (',' + item.pkId)
        }
      })
      if (this.editForm.pkId !== 0) {
        let params = {
          pkId: this.editForm.pkId || undefined,
          knowledgePoint: this.editForm.knowledgePoint
        }
        API.question.setKnowledgePoint(params).then(({ data }) => {
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
          this.saveLoading = false
          this.$Message.error('连接失败，请检查网络！')
        })
      }
    },
    saveAnswerKeyWorld () {
      if (this.answerKeyWorld.answerIndex === -1) {
        if (!this.answerKeyWorld.type) {
          this.fillBlanksForm.answerList[this.answerKeyWorld.answerListIndex].push(this.answerKeyWorld.answer)
        } else {
          this.briefAnswerForm.answerList[this.answerKeyWorld.answerListIndex].keyWords.push(this.answerKeyWorld.answer)
        }
      } else {
        if (!this.answerKeyWorld.type) {
          this.fillBlanksForm.answerList[this.answerKeyWorld.answerListIndex][this.answerKeyWorld.answerIndex] = this.answerKeyWorld.answer
        } else {
          this.briefAnswerForm.answerList[this.answerKeyWorld.answerListIndex].keyWords[this.answerKeyWorld.answerIndex] = this.answerKeyWorld.answer
        }
      }
      this.answerKeyWorldModal = false
    },
    saveEdit () {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          let answer = '{}'
          let isAdd = true
          switch (this.editForm.type) {
            case 0 : if (this.singleChoiceAnswerForm.answerId === '') { isAdd = false; this.$Message.error('题目答案未选择') }; answer = JSON.stringify(this.singleChoiceAnswerForm); break
            case 1 : if (this.multipleChoiceAnswerForm.answerIds.length < 2) { isAdd = false; this.$Message.error('题目答案未选择') }; answer = JSON.stringify(this.multipleChoiceAnswerForm); break
            case 2 : answer = JSON.stringify(this.fillBlanksForm); break
            case 3 : answer = JSON.stringify(this.judgeAnswerForm); break
            case 4 : answer = JSON.stringify(this.briefAnswerForm); break
          }
          if (isAdd) {
            let params = {
              pkId: this.editForm.pkId || undefined,
              bankId: this.$route.query.id,
              questionId: this.editForm.questionId,
              type: this.editForm.type,
              title: this.editForm.title,
              knowledgePoint: this.editForm.knowledgePoint,
              audioName: this.editForm.audioName,
              audio: this.editForm.audio,
              videoName: this.editForm.videoName,
              video: this.editForm.video,
              fileName: this.editForm.fileName,
              file: this.editForm.file,
              analysis: this.editForm.analysis,
              analysisAudioName: this.editForm.analysisAudioName,
              analysisAudio: this.editForm.analysisAudio,
              analysisVideoName: this.editForm.analysisVideoName,
              analysisVideo: this.editForm.analysisVideo,
              answer: answer,
              difficulty: this.editForm.difficulty
            }
            this.saveLoading = true
            let tick = !this.editForm.pkId ? API.question.add(params) : API.question.update(params)
            tick.then(({ data }) => {
              if (data && data.code === 0) {
                this.saveLoading = false
                if (this.editForm.questionId !== 0) {
                  this.getCombinationQuestionList(this.editForm.questionId)
                }
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
        }
      })
    },
    deleteQuestion (params, questionId) {
      let questionIds = []
      if (typeof params === 'undefined') {
        questionIds = this.checkAllGroup
      } else {
        questionIds = [params.pkId]
      }
      if (questionIds.length === 0) {
        this.$Message.error('请选择题目')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<P>确定要删除吗</P>',
          onOk: () => {
            API.question.del(questionIds).then(({ data }) => {
              if (data && data.code === 0) {
                this.indeterminate = false
                this.checkAll = false
                this.checkAllGroup = []
                if (typeof questionId === 'undefined') {
                  this.init()
                } else {
                  this.getCombinationQuestionList(questionId)
                }
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
    deleteFillBlanksAnswer (listIndex, index) {
      this.fillBlanksForm.answerList[listIndex].splice(index, 1)
    },
    deleteBriefAnswerList (listIndex) {
      this.briefAnswerLevel[this.briefAnswerForm.answerList[listIndex].level - 1].disabled = false
      this.briefAnswerForm.answerList.splice(listIndex, 1)
    },
    deleteBriefAnswer (listIndex, index) {
      this.briefAnswerForm.answerList[listIndex].keyWords.splice(index, 1)
    },
    handleOssSuccess (res, type) { // 文件上传成功
      this.uploadLoading = false
      if (res.status === 'ok') {
        switch (type) {
          case 1:this.editForm.audio = this.fileName; break
          case 2:this.editForm.video = this.fileName; break
          case 3:this.editForm.fileList.push(this.fileName)
            this.editForm.file += this.editForm.file === '' ? this.fileName : (',' + this.fileName); break
          case 4:this.editForm.analysisAudio = this.fileName; break
          case 5:this.editForm.analysisVideo = this.fileName; break
        }
      }
    },
    handleOssFormatError (file) { // 文件格式错误
      this.uploadLoading = false
      this.$Notice.warning({
        title: '文件格式错误',
        desc: '文件【 ' + file.name + ' 】格式不正确'
      })
    },
    handleOssMaxSize (file) { // 文件过大
      this.uploadLoading = false
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
    handleOssBeforeUpload (file, type) {
      // return this.checkImageWH(file)
      this.uploadType = type
      this.uploadLoading = true
      switch (type) {
        case 1:this.editForm.audioName = file.name; break
        case 2:this.editForm.videoName = file.name; break
        case 3:this.editForm.fileNameList.push(file.name)
          this.editForm.fileName += this.editForm.fileName === '' ? file.name : (',' + file.name); break
        case 4:this.editForm.analysisAudioName = file.name; break
        case 5:this.editForm.analysisVideoName = file.name; break
      }
      let vm = this
      return oss(file.name, 3).then(res => {
        vm.uploadHost = res.host
        vm.fileName = res.key
        vm.uploadData = res
      })
    },
    handleRemove (type) {
      switch (type) {
        case 1:this.editForm.audioName = ''; this.editForm.audio = ''; break
        case 2:this.editForm.videoName = ''; this.editForm.video = ''; break
        case 4:this.editForm.analysisAudioName = ''; this.editForm.analysisAudio = ''; break
        case 5:this.editForm.analysisVideoName = ''; this.editForm.analysisVideo = ''; break
      }
    },
    deleteFile (index) {
      this.editForm.fileNameList.splice(index, 1)
      this.editForm.fileList.splice(index, 1)
    },
    play (type, filePath) {
      this.playType = type
      switch (type) {
        case 1:this.filePath = filePath; break
        case 2:this.filePath = filePath; break
        case 4:this.filePath = filePath; break
        case 5:this.filePath = filePath; break
      }
      this.playTimer = new Date().getTime()
      this.playModal = true
    }
  },
  mounted () {
    this.init()
  }
}
</script>

<style scoped>

</style>
