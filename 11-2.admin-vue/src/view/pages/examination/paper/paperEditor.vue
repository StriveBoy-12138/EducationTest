<template>
  <div style="width: 100%;height: 100%">
    <div style="width: 100%;height: 100%;display: flex;">
      <Card style="width: 100%;border-radius: 0px;overflow: auto" dis-hover :bordered="false">
        <Spin size="large" fix v-if="loading"></Spin>
        <Row align="top" style="border-bottom: 1px solid #e8eaec;height: 45px;">
          <Button type="primary" icon="ios-arrow-back" @click="goBack">返回试卷</Button>
          <ButtonGroup style="margin-left: 5px">
            <Button icon="md-share">考试入口</Button>
            <Button icon="ios-image-outline">设置考试页面</Button>
            <Button type="warning" icon="ios-share-alt-outline" @click="setStatus(!editForm.status ? 1:0)">{{!editForm.status ? '发布':'取消发布'}}</Button>
          </ButtonGroup>
          <ButtonGroup v-if="editForm.isOpen !== 0" style="margin-left: 5px">
            <Button type="warning" @click="showInviteUser">邀请考生</Button>
            <Button type="warning" style="margin-left: 1px" @click="showInvitedUser">已邀请列表</Button>
          </ButtonGroup>
          <div v-else style="margin-left: 5px">
            <Button type="warning" @click="showInformationCollectionModal">信息采集</Button>
            <span style="margin-left: 5px" v-for="(info, index) in informationCollectionList" :key="'infoTitle'+index">{{info.title+(index === informationCollectionList.length -1 ? '':',')}}</span>
          </div>
        </Row>
        <Row justify="center" style="margin-top: 5px">
        <Card style="width: 1000px;margin-top: 5px">
          <Row align="middle">
            <h1>{{editForm.title}}</h1>
            <h1 style="margin-left: 750px;color: red">{{questionStatistics.score}}</h1>
            <span style="margin-top: 5px;color: red">分</span>
          </Row>
          <Row align="middle" style="margin-top: 20px">
            <span style="color: #C0C4CC;font-size: 10px;">创建时间：{{editForm.createTime}}</span>
            <Tag color="orange" size="medium" style="margin-left: 25px">共{{questionStatistics.totalNum}}题</Tag>
            <Tag color="orange" size="medium" style="margin-left: 25px">{{!editForm.isOpen ? '公开试卷':'非公开试卷'}}</Tag>
            <Tag color="orange" size="medium" style="margin-left: 10px">{{!editForm.isFixed ? '固定试卷':'随机试卷'}}</Tag>
            <Tag color="blue" size="medium" style="margin-left: 10px">自动出分</Tag>
          </Row>{{editForm.des}}<Row>
        </Row>
          <Row justify="end">
            <Button icon="md-create" @click="editButton">编辑</Button>
          </Row>
        </Card>
      </Row>
        <Row v-if="editForm.isFixed === 0" justify="center" style="margin-top: 5px">
          <Card style="width: 1000px;margin-top: 5px">
            <Checkbox
              style="border-radius: 5px 0px 0px 5px;margin-right: 0px"
              border
              :indeterminate="indeterminate"
              :value="checkAll"
              @click.prevent.native="handleCheckAll">{{!checkAll?'全选':'全不选'}}</Checkbox>
            <Button style="border-radius: 0px 5px 5px 0px;" type="success" icon="ios-trash-outline" @click="deletePaperQuestion()"></Button>
            <ButtonGroup style="margin-left: 5px">
              <Button icon="md-add" @click="showManualChoose">手动选题</Button>
              <Button icon="md-add" @click="showRandomChoose">随机抽题</Button>
              <Button><Icon type="ios-swap" style="transform: rotate(90deg);margin-right: 5px" />排序</Button>
              <Button icon="ios-paper-outline">批量设置分数</Button>
              <Button icon="ios-download-outline">导出</Button>
            </ButtonGroup>
            <Button style="margin-left: 5px" :icon="isOpenAll ? 'ios-contract':'ios-expand'" @click="openAll">{{isOpenAll ? '收起':'展开'}}</Button>
          </Card>
        </Row>
        <CheckboxGroup v-if="editForm.isFixed === 0" v-model="checkAllGroup" @on-change="checkAllGroupChange">
          <Row justify="center" v-for="(question,index) in questionList" :key="'question'+index">
            <Card style="width: 1000px;margin-top: 5px">
              <Row align="middle">
                <Checkbox :label="question.pkId">{{''}}</Checkbox>
                <span>{{index+1}}、</span>
                <Tag color="orange" size="medium">{{getTypeName(question.type)}}</Tag>
                <Tag color="orange" size="medium" style="margin-left: 10px">难度：{{getDifficultyName(question.difficulty)}}</Tag>
                <div style="width: 200px;margin-left: 260px">
                  <Button v-if="question.scoreMode !== ''" icon="ios-settings-outline" type="info" @click="showScoreMode(0,question,index)">{{scoreModeTitle[question.scoreMode.modeId]}}</Button>
                </div>
                <div>
                  <InputNumber :disabled="question.scoreMode.modeId === 1 || question.scoreMode.modeId === 6 || question.type === 5" style="width: 60px" :min="Number(0)" :ref="'questionScore'+index" :value="question.score" @on-blur="saveEditPaperQuestions(2,index)"></InputNumber>
                  <div style="background-color: #F5F7FA;width: 60px;height: 32px;float: right;text-align: center;line-height: 32px;border-radius: 0px 5px 5px 0px;border: 1px solid #e8eaec">分</div>
                </div>
                <ButtonGroup style="margin-left: 5px">
                  <Button :icon="openQuestionIds.indexOf(question.pkId) !== -1 ? 'ios-contract':'ios-expand'" @click="openQuestion(question.pkId,question.type)">{{openQuestionIds.indexOf(question.pkId) !== -1? '收起':'展开'}}</Button>
                  <Button icon="ios-trash-outline" @click="deletePaperQuestion(question.pkId)"></Button>
                </ButtonGroup>
                <ButtonGroup style="margin-left: 5px">
                  <Button icon="ios-arrow-round-up" :disabled="index === 0" @click="saveEditPaperQuestions(4,index)"></Button>
                  <Button icon="ios-arrow-round-down" :disabled="index === questionList.length - 1" @click="saveEditPaperQuestions(5,index)"></Button>
                </ButtonGroup>
              </Row>
              <Row style="margin-left: 20px;margin-top: 10px">
                <div v-html="question.title"></div>
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
                  <Row justify="center" v-for="(combinationQuestion,combinationQuestionIndex) in question.questions" :key="'combinationQuestion'+combinationQuestionIndex">
                    <Card style="width: 925px;margin-top: 5px">
                      <Row align="middle">
                        <Checkbox>{{''}}</Checkbox>
                        <span>{{index+1}}-{{combinationQuestionIndex+1}}、</span>
                        <Tag color="orange" size="medium">{{getTypeName(combinationQuestion.type)}}</Tag>
                        <Tag color="orange" size="medium" style="margin-left: 10px">难度：{{getDifficultyName(combinationQuestion.difficulty)}}</Tag>
                        <div style="width: 185px;margin-left: 325px">
                          <Button v-if="combinationQuestion.scoreMode !== ''" size="small" icon="ios-settings-outline" type="info" @click="showScoreMode(1,combinationQuestion,index,combinationQuestionIndex)">{{scoreModeTitle[combinationQuestion.scoreMode.modeId]}}</Button>
                        </div>
                        <div>
                          <InputNumber :disabled="combinationQuestion.scoreMode.modeId === 1 || combinationQuestion.scoreMode.modeId === 6" size="small" style="width: 50px" :min="Number(0)" :ref="'combinationQuestionScore'+combinationQuestionIndex" :value="combinationQuestion.score" @on-blur="saveEditPaperQuestions(3,index,combinationQuestionIndex)"></InputNumber>
                          <div style="background-color: #F5F7FA;width: 50px;height: 24px;float: right;text-align: center;line-height: 24px;border-radius: 0px 5px 5px 0px;border: 1px solid #e8eaec">分</div>
                        </div>
                        <Button size="small" style="margin-left: 5px" :icon="openQuestionIds.indexOf(combinationQuestion.pkId) !== -1 ? 'ios-contract':'ios-expand'" @click="openQuestion(combinationQuestion.pkId,combinationQuestion.type)">{{openQuestionIds.indexOf(combinationQuestion.pkId) !== -1? '收起':'展开'}}</Button>
                      </Row>
                      <Row style="margin-left: 20px;margin-top: 10px">
                        <div v-html="combinationQuestion.title"></div>
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
        <Row v-if="editForm.isFixed === 1" justify="center" style="margin-top: 5px">
          <Card style="width: 1000px;margin-top: 5px">
            <Row v-if="editForm.questionRule === '{}'" align="middle" justify="center">
              <span style="margin-top: 5px;color: red;font-size: 20px;">请添加随机规则</span>
              <Button type="primary" icon="md-create" style="margin-left: 10px" @click="showRandomChoose">编辑</Button>
            </Row>
            <div v-else>
              <Row align="middle" justify="end">
                <Button type="primary" icon="md-create" style="margin-right: 10px" @click="showRandomChoose">编辑</Button>
              </Row>
              <Row align="middle" style="height: 50px;border: 1px dashed #409eff;margin-top: 5px" v-for="(rule, ruleIndex) in questionRule.rules" :key="'rule'+ruleIndex">
                <span style="font-weight: 700;margin-left: 20px">{{ruleTitle[questionRule.ruleId]}}</span>
                <div style="width: 570px">
                  <span v-for="(name,typeNameIndex) in rule.typeName" :key="'typeName'+typeNameIndex">{{!typeNameIndex ? name:(','+name)}}</span>
                </div>
                <span style="margin-left: 100px">选取{{rule.num}}/{{rule.totalNum}}题，每题{{rule.score}}分，总分{{rule.totalScore}}分</span>
              </Row>
            </div>
          </Card>
        </Row>
      </Card>
      <Card dis-hover :padding="0" :bordered="false" :style="'overflow: auto;border-radius: 0px;border-left: 1px solid #e8eaec;width: '+setWidth+'px'">
        <Spin size="large" fix v-if="loading"></Spin>
        <Row style="margin-top: 5px" align="middle">
          <Icon v-if="setWidth === 50" type="ios-arrow-back" size="25" style="cursor: pointer;margin-left: 5px" @click="showExamSet" />
          <Icon v-else type="ios-arrow-forward" size="25" style="cursor: pointer;margin-left: 5px" @click="showExamSet" />
          <div v-if="setWidth === 50" style="font-size: 20px;font-weight: 700;margin-left: 10px;margin-top: 5px">考试设置</div>
          <div v-else style="font-size: 20px;font-weight: 700;margin-left: 110px">考试设置</div>
        </Row>
        <Collapse v-if="setWidth === 450" style="border-radius: 0px" v-model="examSetName" accordion>
          <Panel hide-arrow name="1">
            <Icon type="ios-settings-outline" size="18" />
            <span style="font-size: 18px">基础设置</span>
            <Icon type="ios-arrow-forward" size="18" style="margin-left: 180px" />
            <div slot="content">
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>按题限时</b>
                <i-switch style="margin-left: 200px" v-model="examinationForm.questionLimit" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>时长(按{{examinationForm.questionLimit ? '题':'卷'}}限时)</b>
                <Poptip trigger="hover" :transfer="true" placement="top-start">
                <div slot="content">
                  <span style="font-size: 18px;">时长</span><br><br>
                  <span><b>按卷限时：</b>限制试卷总时长，答题<br>时间超过总时长会立即交卷。<br><b>按题限时：</b>每一题都限制时长，超<br>时自动提交答案并跳转到下一题，<br>只能按顺序答题，不能跳题，也不<br>会回退。</span>
                </div>
                <Icon style="margin-left: 5px" type="ios-help-circle"/>
                </Poptip>
                <div v-if="examinationForm.questionLimit">
                  <InputNumber style="margin-left: 20px;width: 60px" size="small" :min="Number(0)" v-model="examinationForm.timeMinute" @on-blur="saveEditExamination"></InputNumber><span style="margin-left: 5px">分</span>
                  <InputNumber style="margin-left: 5px;width: 60px" size="small" :min="Number(0)" :max="Number(59)" v-model="examinationForm.timeSecond" @on-blur="saveEditExamination"></InputNumber><span style="margin-left: 5px">秒</span>
                </div>
                <div v-else>
                  <InputNumber style="margin-left: 105px;width: 60px" size="small" :min="Number(0)" v-model="examinationForm.time" @on-blur="saveEditExamination"></InputNumber><span style="margin-left: 5px">秒</span>
                </div>
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>及格分</b>
                <InputNumber style="margin-left: 175px;width: 60px" size="small" :min="Number(0)" v-model="examinationForm.passingGrade" @on-blur="saveEditExamination"></InputNumber><span style="margin-left: 5px">分</span>
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>闯关模式</b>
                <Poptip trigger="hover" :transfer="true" placement="top-start">
                  <div slot="content">
                    <span>按顺序答题，不能跳题，也不能<br>后退，只要有一题答错，立即结<br>束考试。</span>
                  </div>
                  <Icon style="margin-left: 5px" type="ios-help-circle"/>
                </Poptip>
                <i-switch style="margin-left: 180px" v-model="examinationForm.brakBarrier" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>考试时间</b>
                <i-switch style="margin-left: 200px" v-model="examinationForm.examinationTimeUse" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec;background-color: #FCFCFC" v-if="examinationForm.examinationTimeUse">
                <span style="font-size: 5px;margin-left: 10px">考试开始时间</span>
                <DatePicker :transfer="true" type="datetime" placeholder="开始时间" size="small" :value="examinationForm.examinationStartTime" @on-change="saveEditExamination" style="width: 175px;margin-left: 40px"></DatePicker>
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec;background-color: #FCFCFC" v-if="examinationForm.examinationTimeUse">
                <span style="font-size: 5px;margin-left: 10px">考试结束时间</span>
                <DatePicker :transfer="true" type="datetime" placeholder="结束时间" size="small" :value="examinationForm.examinationEndTime" @on-change="saveEditExamination" style="width: 175px;margin-left: 40px"></DatePicker>
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>多次考试</b>
                <Poptip trigger="hover" :transfer="true" placement="top-start">
                  <div slot="content">
                    <span>设置同一份试卷可以考多次，仅<br>限于非公开试卷。公开试卷无需<br>设置多次，扫码或点击链接几次<br>，就可以考几次。</span>
                  </div>
                  <Icon style="margin-left: 5px" type="ios-help-circle"/>
                </Poptip>
                <i-switch v-if="!examinationForm.multipleExamination" style="margin-left: 180px" v-model="examinationForm.multipleExamination" @on-change="saveEditExamination" />
                <div v-else>
                  <i-switch style="margin-left: 90px" v-model="examinationForm.multipleExamination" @on-change="saveEditExamination" />
                  <InputNumber style="margin-left: 10px;width: 60px" size="small" :min="Number(0)" v-model="examinationForm.multipleExaminationTime" @on-blur="saveEditExamination"></InputNumber><span style="margin-left: 5px">次</span>
                </div>
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>收费考试</b>
                <Poptip trigger="hover" :transfer="true" placement="top-start">
                  <div slot="content">
                    <span>设置考生需支付考试费用后才可<br>以进入考试。需配置微信收款账<br>户，考生直接支付到您的账户下<br>，无需手续费。</span>
                  </div>
                  <Icon style="margin-left: 5px" type="ios-help-circle"/>
                </Poptip>
                <i-switch style="margin-left: 180px" v-model="examinationForm.chargeExamination" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec;background-color: #FCFCFC" v-if="examinationForm.chargeExamination">
                <span style="font-size: 5px;margin-left: 10px">收费金额</span>
                <InputNumber style="margin-left: 150px;width: 65px" size="small" :min="Number(0)" v-model="examinationForm.chargeExaminationPrice" @on-blur="saveEditExamination"></InputNumber><span style="margin-left: 5px">元</span>
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>是否需要签名</b>
                <i-switch style="margin-left: 170px" v-model="examinationForm.sign" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>是否显示分值</b>
                <i-switch style="margin-left: 170px" v-model="examinationForm.scoreDisplay" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>是否显示题型</b>
                <i-switch style="margin-left: 170px" v-model="examinationForm.questionTypeDisplay" @on-change="saveEditExamination" />
              </Row>
            </div>
          </Panel>
          <Panel hide-arrow name="2">
            <Icon type="ios-create-outline" size="18" />
            <span style="font-size: 18px">考试中</span>
            <Icon type="ios-arrow-forward" size="18" style="margin-left: 198px" />
            <div slot="content">
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>顺序随机</b>
                <Poptip trigger="hover" :transfer="true" placement="top-start">
                  <div slot="content">
                    <span style="font-size: 18px;">随机试卷</span><br>
                    <span><b>1、题目顺序随机：</b>考生答题时题目顺序打乱。<br><b>2、选项顺序随机：</b>考生答题时选项顺序打乱。</span>
                  </div>
                  <Icon style="margin-left: 5px" type="ios-help-circle"/>
                </Poptip>
                <ButtonGroup style="margin-left: 104px">
                  <Button :type="examinationForm.randomOrderList.indexOf('0') !== -1 ? 'primary':'default'" :key="questionRandomTimer" @click="questionRandomChange">题目</Button>
                  <Button :type="examinationForm.randomOrderList.indexOf('1') !== -1 ? 'primary':'default'" style="margin-left: 1px" :key="optionRandomTimer" @click="optionRandomChange">选项</Button>
                </ButtonGroup>
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>全部答完才能交卷</b>
                <i-switch style="margin-left: 140px" v-model="examinationForm.handAfterFinish" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>单选和判断题自动跳题</b>
                <i-switch style="margin-left: 113px" v-model="examinationForm.skipQuestion" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>防作弊</b>
                <Poptip trigger="hover" :transfer="true" placement="top-start">
                  <div slot="content">
                    <span>允许考生切出次数：限制考生切<br>出的次数，防止考生切出查询题<br>目信息。切出次数达到上限会强<br>制自动交卷。</span>
                  </div>
                  <Icon style="margin-left: 5px" type="ios-help-circle"/>
                </Poptip>
                <i-switch style="margin-left: 193px" v-model="examinationForm.preventionCheat" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec;background-color: #FCFCFC" v-if="examinationForm.preventionCheat">
                <span style="font-size: 5px;margin-left: 10px">切出次数不能大于</span>
                <InputNumber style="margin-left: 100px;width: 65px" size="small" :min="Number(1)" v-model="examinationForm.preventionCheatTime" @on-blur="saveEditExamination"></InputNumber><span style="margin-left: 5px">次</span>
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>霸屏考试</b>
                <Poptip trigger="hover" :transfer="true" placement="top-start">
                  <div slot="content">
                    <span style="font-size: 18px;">霸屏客户端</span><br>
                    <span>启用霸屏考试必需要下载霸屏<br>客户端，并只支持电脑端。如<br>只允许使用客户端考试，可以<br>配合 禁止终端 功能使用。</span>
                  </div>
                  <Icon style="margin-left: 5px" type="ios-help-circle"/>
                </Poptip>
                <i-switch style="margin-left: 180px" v-model="examinationForm.dominateScreen" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>科学计算器</b>
                <i-switch style="margin-left: 185px" v-model="examinationForm.calculator" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>公式编辑器</b>
                <i-switch style="margin-left: 185px" v-model="examinationForm.formula" @on-change="saveEditExamination" />
              </Row>
            </div>
          </Panel>
          <Panel hide-arrow name="3">
            <Icon type="md-checkbox-outline" size="18" />
            <span style="font-size: 18px">考试后</span>
            <Icon type="ios-arrow-forward" size="18" style="margin-left: 198px" />
            <div slot="content">
              <Row style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b style="margin-top: 5px">成绩查看</b>
                <Poptip trigger="hover" :transfer="true" placement="top-start">
                  <div slot="content">
                    <span style="font-size: 18px;">成绩查看</span><br>
                    <span>设置查成绩、查答案的限制，共有四个选项供<br>选择，限制依次放宽。</span><br>
                    <b>1、不可查成绩：</b><span>成绩对考生不可见，只对组<br>织考试的考官可见。</span><br>
                    <b>2、可查成绩：</b><span>考生只能查考试得分，不能看<br>题的对错以及答案。</span><br>
                    <b>3、可查成绩和对错：</b><span>考生能查考试得分，题<br>的对错，但不能看题的答案。</span><br>
                    <b>4、可查成绩、对错、答案和解析：</b><span>考生/学员<br>能查考试得分，题的对错及其答案。</span>
                  </div>
                  <Icon style="margin-left: 5px;margin-top: 8px" type="ios-help-circle"/>
                </Poptip>
                <RadioGroup style="margin-left: 15px" :vertical="true" v-model="examinationForm.resultsView" @on-change="saveEditExamination">
                  <Radio :label="Number(0)">不可查看成绩</Radio>
                  <Radio :label="Number(1)">可查看成绩</Radio>
                  <Radio :label="Number(2)">可查成绩和对错</Radio>
                  <Radio :label="Number(3)">可查成绩、对错、答案和解析</Radio>
                </RadioGroup>
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>考试评语</b>
                <div v-if="examinationForm.examinationCommentsUse">
                  <Button type="success" size="small" style="margin-left: 75px" @click="showExaminationComments">考试评语设置({{examinationCommentsList.length}})</Button>
                  <i-switch style="margin-left: 5px" v-model="examinationForm.examinationCommentsUse" @on-change="saveEditExamination" />
                </div>
                <i-switch v-else style="margin-left: 190px" v-model="examinationForm.examinationCommentsUse" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>判分后错题加入错题集</b>
                <i-switch style="margin-left: 105px" v-model="examinationForm.wrongQuestions" @on-change="saveEditExamination" />
              </Row>
              <Row align="middle" style="min-height: 50px;border-bottom: 1px solid #e8eaec">
                <b>主观题自评分</b>
                <Poptip trigger="hover" :transfer="true" placement="top-start">
                  <div slot="content">
                    <span>设置主观题自评分后，试卷如果<br>包含主观题，学员考试结束后可<br>进行主观题自我评分，试卷无需<br>后台阅卷；主观题自评分有30分<br>钟时间，超过30分钟，系统自动<br>判卷。</span>
                  </div>
                  <Icon style="margin-left: 5px" type="ios-help-circle"/>
                </Poptip>
                <i-switch style="margin-left: 142px" v-model="examinationForm.selfGrade" @on-change="saveEditExamination" />
              </Row>
            </div>
          </Panel>
        </Collapse>
      </Card>
    </div>
    <!--考试评语设置-->
    <Modal v-model="examinationCommentsModal" :closable='true' :mask-closable=false :width="600">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">考试评语设置</h3>
      <Row>
        <Button type="info" icon="md-add" @click="saveComment(0)">添加评语</Button>
      </Row>
      <Row style="margin-top: 8px" v-for="(comment, index) in examinationCommentsList" :key="'comment' + index">
        <Input :ref="'comment'+index" :value="comment" style="width: 250px" @on-blur="saveComment(1, index)"/>
        <Button type="primary" shape="circle" style="margin-left: 10px" icon="ios-remove" @click="saveComment(2,index)"></Button>
      </Row>
      <div slot="footer">
        <Button @click="examinationCommentsModal = false">取消</Button>
      </div>
    </Modal>
    <!--编辑试卷对话框-->
    <Modal v-model="editModal" :closable='true' :mask-closable=false :width="650">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">编辑试卷</h3>
      <Form ref="editForm" :model="editForm" label-position="right" :label-width="100"
            :rules="rules" @submit.native.prevent="saveEdit">
        <FormItem label="分类" prop="typeId">
          <Select v-model="editForm.typeId" style="margin-right:5px;width: 150px;" placeholder="选择分类">
            <Option v-for="item in paperTypeList" :value="item.pkId" :key="'choosePaperType'+item.pkId">{{item.name}}</Option>
          </Select>
        </FormItem>
        <FormItem label="标题" prop="title">
          <Input v-model="editForm.title" style="width: 300px" :maxlength="32"/>
        </FormItem>
        <FormItem label="描述" prop="des">
          <Input v-model="editForm.des" type="textarea" :rows="Number(2)" style="width: 500px" :maxlength="200"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="cancel">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveEdit">保存</Button>
      </div>
    </Modal>
    <!--邀请考生-->
    <Modal v-model="inviteUserModal" :closable="true" :mask-closable="false" :width="850">
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
          <i-switch style="margin-left: 5px" :disabled="!!editForm.inviteAll" v-model="inviteAll" @on-change="saveEditPaperUser(3)"/>
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
    <!--采集信息-->
    <Modal v-model="informationCollectionModal" :closable='true' :mask-closable=false :width="900">
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
    <!--手动选题对话框-->
    <Modal v-model="manualChooseModal" :closable="true" :mask-closable="false" :width="1200" :footer-hide="false" @on-visible-change="resetQuestionStatistics">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">选择题目</h3>
      <Row style="height: 450px;margin-bottom: 0px">
        <div style="height: 450px;width: 330px;border-right: 1px solid #e8eaec;overflow: auto">
          <Row style="height: 40px" align="middle">
            <Select style="width: 140px" placeholder="选择题库目录" v-model="bankTypeId">
              <Option :value="Number(0)">全部</Option>
              <Option v-for="(item, index) in questionBankTypeList" :key="index" :value="item.pkId">{{item.name}}</Option>
            </Select>
            <Input style="width: 135px;margin-left: 5px" v-model="bankName" placeholder="题库名称"/>
            <Button style="background-color: #F5F7FA" @click="getQuestionBank"><Icon type="ios-search" /></Button>
          </Row>
          <Row>
            <Table :columns="questionBankColumns" :data="questionBankTableData" :highlight-row="true" :current="questionBankPageIndex" :key="questionBankTimer" style="width: 100%" border :loading="questionBankTableLoading">
              <template slot="name" slot-scope="scope">
                <span @click="getQuestion('',scope.row.pkId)">{{scope.row.name}}</span>
              </template>
            </Table>
          </Row>
          <Row style="margin-top: 10px;" justify="end">
            <Page :total="questionBankTotalCount" :current="questionBankPageIndex" :page-sise="questionBankPageSize" loading size="small"
                  @on-change="questionBankPageChange" @on-page-size-change="questionBankSizeChange">
            </Page>
          </Row>
        </div>
        <div style="height: 450px;width: 710px;border-right: 1px solid #e8eaec;overflow: auto">
          <Row style="height: 40px" align="middle">
            <Select style="margin-left: 10px;width: 100px" v-model="questionType">
            <Option :value="Number(-1)">全部</Option>
            <Option :value="Number(0)">单选题</Option>
            <Option :value="Number(1)">多选题</Option>
            <Option :value="Number(2)">填空题</Option>
            <Option :value="Number(3)">判断题</Option>
            <Option :value="Number(4)">简答题</Option>
            <Option :value="Number(5)">组合题</Option>
          </Select>
            <Select style="margin-left: 5px;width: 100px" v-model="questionDifficulty">
              <Option :value="Number(-1)">全部</Option>
              <Option :value="Number(2)">低</Option>
              <Option :value="Number(1)">中</Option>
              <Option :value="Number(0)">高</Option>
            </Select>
            <Input style="margin-left: 197px;width: 150px" v-model="questionTitle" placeholder="搜索题目"/>
            <ButtonGroup>
              <Button style="background-color: #F5F7FA" @click="getQuestion">搜索</Button>
              <Button style="background-color: #F5F7FA" @click="getQuestion('reset')">重置</Button>
            </ButtonGroup>
          </Row>
          <Row style="margin-left: 10px;margin-right: 10px">
            <Table ref="questionSelection" :columns="questionColumns" :data="questionTableData" :highlight-row="true" :current="questionPageIndex" style="width: 100%;" :key="questionTimer" border
                   @on-select="addQuestionSelection" @on-select-cancel="removeQuestionSelection"
                   @on-selection-change="questionSelectionChange" :loading="questionTableLoading">
              <template slot="title" slot-scope="scope">
                <div v-html="scope.row.title"></div>
              </template>
            </Table>
          </Row>
          <Row style="margin-top: 10px" justify="end">
            <Page :total="questionTotalCount" :current="questionPageIndex" :page-sise="questionPageSize" loading show-total show-sizer
                  @on-change="questionPageChange" @on-page-size-change="questionSizeChange">
            </Page>
          </Row>
        </div>
        <div style="height: 450px;width: 125px;overflow: auto">
          <div style="height: 420px">
            <Row style="border-bottom: 1px solid #e8eaec" justify="center">
              <span style="font-size: 17px;margin-bottom: 10px">试卷题型统计</span>
            </Row>
            <Row v-if="questionStatistics.singleChoice.length > 0" style="margin-left: 10px">
              <span>单选题</span><span style="margin-left: 10px">{{questionStatistics.singleChoice.length}}</span>
            </Row>
            <Row v-if="questionStatistics.multipleChoice.length > 0" style="margin-left: 10px">
              <span>多选题</span><span style="margin-left: 10px">{{questionStatistics.multipleChoice.length}}</span>
            </Row>
            <Row v-if="questionStatistics.fillBlanks.length > 0" style="margin-left: 10px">
              <span>填空题</span><span style="margin-left: 10px">{{questionStatistics.fillBlanks.length}}</span>
            </Row>
            <Row v-if="questionStatistics.judge.length > 0" style="margin-left: 10px">
              <span>判断题</span><span style="margin-left: 10px">{{questionStatistics.judge.length}}</span>
            </Row>
            <Row v-if="questionStatistics.briefAnswer.length > 0" style="margin-left: 10px">
              <span>简答题</span><span style="margin-left: 10px">{{questionStatistics.briefAnswer.length}}</span>
            </Row>
            <Row v-if="questionStatistics.combination.length > 0" style="margin-left: 10px">
              <span>组合题</span><span style="margin-left: 10px">{{questionStatistics.combination.length}}</span>
            </Row>
          </div>
          <Row style="float: bottom" justify="end">
            <span>总题数：</span><span>{{questionStatistics.totalNum}}题</span>
          </Row>
        </div>
      </Row>
      <div slot="footer" style="margin-top: 0px">
        <Button @click="manualChooseModal = false">取消</Button>
        <Button type="primary" :loading="saveLoading" @click="saveSetPaperQuestions">确认</Button>
      </div>
    </Modal>
    <!--随机抽题对话框-->
    <Modal v-model="randomChooseModal" :closable='true' :mask-closable=false :width="1200">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">随机规则设置</h3>
      <Row align="middle" style="border-bottom: 1px solid #e8eaec;height: 50px">
        <Button type="warning" icon="md-add" @click="showAddRule">添加规则</Button>
        <span style="margin-left: 10px">共选择 <span style="color: #ff6700;font-size: 18px">{{questionRule.totalNum}}</span> 道题目， 总分 <span style="color: #ff6700;font-size: 18px">{{questionRule.totalScore}}</span> 分</span>
      </Row>
      <Row align="middle" style="height: 50px;border: 1px dashed #409eff;margin-top: 5px" v-for="(rule, ruleIndex) in questionRule.rules" :key="'rule'+ruleIndex">
        <span style="font-weight: 700;margin-left: 20px">{{ruleTitle[questionRule.ruleId]}}</span>
        <div style="width: 630px">
          <span v-for="(name,typeNameIndex) in rule.typeName" :key="'typeName'+typeNameIndex">{{!typeNameIndex ? name:(','+name)}}</span>
        </div>
        <span style="margin-left: 100px">选取</span>
        <InputNumber :min="1" :max="rule.totalNum" v-model="rule.num" @on-blur="ruleChange(ruleIndex)"></InputNumber>
        <span>/{{rule.totalNum}}题，每题</span>
        <InputNumber :min="1" v-model="rule.score" @on-blur="ruleChange(ruleIndex)"></InputNumber>
        <span>分，总分{{rule.totalScore}}分</span>
        <!--<Button icon="md-create" type="text" size="small"></Button>-->
        <Button icon="ios-trash-outline" type="text" size="small" @click="deleteRule(ruleIndex)"></Button>
      </Row>
      <div slot="footer">
        <Button @click="randomChooseModal = false">取消</Button>
        <Button type="primary" @click="savePaperRules" :loading="saveLoading">保存规则</Button>
      </div>
    </Modal>
    <!--添加规则对话框-->
    <Modal v-model="addRuleModal" :closable='true' :mask-closable=false :width="800" @on-visible-change="resetQuestionRule">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">添加规则</h3>
      <Row align="middle" style="margin-top: 10px">
        <span style="margin-left: 40px">选择规则类型</span>
        <RadioGroup v-model="questionRule.ruleId">
          <Radio v-if="questionRule.ruleId === 0 || questionRule.isAdd" style="margin-left: 15px" :label="Number(0)">题型</Radio>
          <Radio v-if="questionRule.ruleId === 1 || questionRule.isAdd" style="margin-left: 10px" :label="Number(1)">难度</Radio>
          <Radio v-if="questionRule.ruleId === 2 || questionRule.isAdd" style="margin-left: 10px" :label="Number(2)">题库</Radio>
        </RadioGroup>
      </Row>
      <Row v-if="questionRule.ruleId !== -1" align="middle" style="margin-top: 40px">
        <Poptip trigger="hover" content="若选择“分开设置”，则下方的每一项选择都将作为一条独立的随机规则。" placement="bottom-start" offset="5" :transfer="true">
          <Icon style="margin-left: 20px;margin-right: 5px" type="ios-alert" />
        </Poptip>
        <span>规则生成方式</span>
        <RadioGroup v-model="questionRule.ruleType">
          <Radio style="margin-left: 15px" :label="Number(0)">整体设置</Radio>
          <Radio style="margin-left: 20px" :label="Number(1)">分开设置</Radio>
        </RadioGroup>
      </Row>
      <Row v-if="questionRule.ruleId === 0" align="middle" style="margin-top: 40px">
        <span style="margin-left: 65px">选择题型</span>
        <CheckboxGroup v-model="questionRule.questionTypeList">
          <Checkbox v-for="type in questionRule.questionTypeAll" style="margin-left: 20px" :key="'type'+type.id" :label="type.id">{{type.name}}</Checkbox>
        </CheckboxGroup>
      </Row>
      <Row v-else-if="questionRule.ruleId === 1" align="middle" style="margin-top: 40px">
        <span style="margin-left: 65px">选择难度</span>
        <CheckboxGroup v-model="questionRule.questionDifficultyList">
          <Checkbox v-for="difficulty in questionRule.questionDifficultyAll" style="margin-left: 15px" :key="'difficulty'+difficulty.id" :label="difficulty.id">{{difficulty.name}}</Checkbox>
        </CheckboxGroup>
      </Row>
      <div v-show="questionRule.ruleId === 2" style="margin-top: 30px">
        <Row style="height: 40px" align="middle">
          <span style="margin-left: 40px">选择题库</span>
          <Select style="width: 140px;margin-left: 270px" placeholder="选择分类" v-model="bankTypeId">
            <Option :value="Number(0)">全部</Option>
            <Option v-for="(item, index) in questionBankTypeList" :key="index" :value="item.pkId">{{item.name}}</Option>
          </Select>
          <Input style="width: 135px;margin-left: 5px" v-model="bankName" placeholder="题库名称"/>
          <ButtonGroup>
            <Button style="background-color: #F5F7FA" @click="getQuestionBank">搜索</Button>
            <Button style="background-color: #F5F7FA" @click="getQuestionBank('reset')">重置</Button>
          </ButtonGroup>
        </Row>
        <Row style="margin-left: 40px;margin-top: 10px">
          <Table size="small" ref="questionBankSelection" :columns="questionBankRuleColumns" :data="questionBankTableData" :highlight-row="true" :current="questionBankPageIndex" :key="questionBankTimer" style="width: 100%" border :loading="questionBankTableLoading"
                 @on-selection-change="questionBankSelectionChange">
          </Table>
        </Row>
        <Row style="margin-top: 10px;" justify="end">
          <Page :total="questionBankTotalCount" :current="questionBankPageIndex" :page-sise="questionBankPageSize" loading size="small" show-total show-sizer show-elevator
                @on-change="questionBankPageChange" @on-page-size-change="questionBankSizeChange">
          </Page>
        </Row>
      </div>
      <div slot="footer">
        <Button @click="addRuleModal = false">取消</Button>
        <Button type="primary" @click="addRules">保存</Button>
      </div>
    </Modal>
    <!--得分模式-->
    <Modal v-model="scoreModeModal" :closable='true' :mask-closable=false :width="900">
      <h3 slot="header" style="font-size: 16px;font-weight: normal">选择得分模式</h3>
      <Row style="margin-left: 20px">
        <RadioGroup v-model="scoreModeForm.scoreMode.modeId" vertical>
          <div v-if="scoreModeForm.questionType === 1">
           <Radio :label="Number(0)">
             <span>漏选不得分</span>
           </Radio>
           <Radio :label="Number(1)">
             <span>按选项分值给分（需要给选项设置分值，可设置负分）</span>
           </Radio>
           <div style="margin-left: 40px" v-if="scoreModeForm.scoreMode.modeId === 1">
             <Row align="middle" style="margin-top: 10px" v-for="answer in scoreModeForm.scoreMode.answers" :key="answer.id">
               <InputNumber style="width: 70px" :min="Number(0)" v-model="answer.score" size="small"></InputNumber>
               <div style="background-color: #F5F7FA;width: 60px;height: 24px;float: right;text-align: center;line-height: 24px;border-radius: 0px 5px 5px 0px;border: 1px solid #e8eaec">分</div>
               <span style="margin-left: 20px">{{answer.content}}</span>
             </Row>
           </div>
           <Radio :label="Number(2)">
             <span>按答案数平均给分（例如题目分数为5分，ABCD四个选项，答案为ABC，选择AB，得分为3.3分（四舍五入））</span>
           </Radio>
           <Radio :label="Number(3)">
             <span>漏选给半分（例如：题目分值为5分，漏选给2.5分，错选得0分）</span>
           </Radio>
           <Radio :label="Number(4)">
             <span>漏选给指定分</span>
           </Radio>
           <div style="margin-left: 40px" v-if="scoreModeForm.scoreMode.modeId === 4">
             <Row align="middle" style="margin-top: 10px">
               <InputNumber style="width: 70px" :min="Number(0)" v-model="scoreModeForm.scoreMode.missSelectScore" size="small"></InputNumber>
               <div style="background-color: #F5F7FA;width: 60px;height: 24px;float: right;text-align: center;line-height: 24px;border-radius: 0px 5px 5px 0px;border: 1px solid #e8eaec">分</div>
             </Row>
           </div>
         </div>
          <div v-else-if="scoreModeForm.questionType === 2">
            <Radio :label="Number(5)">
              <span>只要一空错误就不得分</span>
            </Radio>
            <Radio :label="Number(6)">
              <span>按每空给分（必须要给每个空设置分值，分值都不能为0）</span>
            </Radio>
            <div style="margin-left: 40px" v-if="scoreModeForm.scoreMode.modeId === 6">
              <Row align="middle" style="margin-top: 10px" v-for="(answer,index) in scoreModeForm.scoreMode.answers" :key="index">
                <InputNumber style="width: 70px" :min="Number(0)" v-model="answer.score" size="small"></InputNumber>
                <div style="background-color: #F5F7FA;width: 60px;height: 24px;float: right;text-align: center;line-height: 24px;border-radius: 0px 5px 5px 0px;border: 1px solid #e8eaec">分</div>
                <span style="margin-left: 20px">第{{index+1}}空</span>
                <span style="margin-left: 5px" v-for="blank in answer.answers" :key="blank">{{blank}}</span>
              </Row>
            </div>
            <Radio :label="Number(7)">
              <span>按每空平均给分（例如题目分数为5分，有3个空，答对2个，得分为3.3分（四舍五入），如果全答对了得5分）</span>
            </Radio>
          </div>
          <div v-else-if="scoreModeForm.questionType === 4">
            <Radio :label="Number(8)">
              <span>需要阅卷判分</span>
            </Radio>
            <Radio :label="Number(9)">
              <span>按关键词自动判分</span>
            </Radio>
            <div style="margin-left: 40px" v-if="scoreModeForm.scoreMode.modeId === 9">
              <Row align="middle" style="margin-top: 10px" v-for="(answer, index) in scoreModeForm.scoreMode.answers" :key="'answer'+index">
                <InputNumber style="width: 70px" :min="Number(0)" v-model="answer.score" size="small"></InputNumber>
                <div style="background-color: #F5F7FA;width: 60px;height: 24px;float: right;text-align: center;line-height: 24px;border-radius: 0px 5px 5px 0px;border: 1px solid #e8eaec">分</div>
                <span style="margin-left: 20px">级别：{{answer.answers.level}}</span>
                <Tag color="blue" style="margin-left: 5px" v-for="(keyword, index) in answer.answers.keyWords" :key="'keyword'+index">{{keyword}}</Tag>
              </Row>
            </div>
          </div>
        </RadioGroup>
      </Row>
      <div slot="footer">
        <Button @click="scoreModeModal = false">取消</Button>
        <Button type="primary" @click="saveEditPaperQuestions(scoreModeForm.type,scoreModeForm.index,scoreModeForm.combinationIndex)">保存</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import API from '@/api'
import tinymceEditor from '_c/text-editor/tinymce-editor.vue'

export default {
  name: 'paperEditor',
  components: { tinymceEditor },
  data () {
    return {
      setWidth: 450,
      examSetName: '1',
      indeterminate: false,
      checkAll: false,
      checkAllGroup: [],
      openQuestionIds: [],
      isOpenAll: false,
      timer: '',
      questionRandomTimer: 'questionRandom',
      optionRandomTimer: 'optionRandom',
      questionBankTimer: '',
      questionTimer: '',
      combinationQuestionTimer: '',
      userAccount: '',
      userName: '',
      invitedUserAccount: '',
      invitedUserName: '',
      bankTypeId: '',
      bankName: '',
      bankId: 0,
      questionType: -1,
      questionDifficulty: -1,
      questionTitle: '',
      loading: false,
      editModal: false,
      inviteUserModal: false,
      invitedUserModal: false,
      examinationCommentsModal: false,
      informationCollectionModal: false,
      optionModal: false,
      manualChooseModal: false,
      randomChooseModal: false,
      addRuleModal: false,
      scoreModeModal: false,
      userTableLoading: false,
      invitedUserTableLoading: false,
      titleTableLoading: false,
      invitedTitleTableLoading: false,
      invitedOrganizationTableLoading: false,
      questionBankTableLoading: false,
      questionTableLoading: false,
      saveLoading: false,
      tabPaneNumber: 0,
      invitedTabPaneNumber: 0,
      userTotalCount: 0,
      invitedUserTotalCount: 0,
      questionBankTotalCount: 0,
      questionTotalCount: 0,
      userPageIndex: 1,
      invitedUserPageIndex: 1,
      questionBankPageIndex: 1,
      questionPageIndex: 1,
      userPageSize: 10,
      invitedUserPageSize: 10,
      questionBankPageSize: 10,
      questionPageSize: 10,
      optionTitle: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'],
      scoreModeTitle: ['漏选不得分', '按答案分值给分', '按答案数平均给分', '漏选给半分', '漏选给指定分', '只要一空错误就不得分', '按每空给分', '按每空平均给分', '需要阅', '按关键词自动判分'],
      ruleTitle: ['题型：', '难度：', '题库：'],
      paperTypeList: [],
      selectOrganizationList: [],
      organizationList: [],
      examinationCommentsList: [],
      questionBankTypeList: [],
      questionList: [],
      combinationQuestionList: {},
      editForm: {
        pkId: 0,
        typeId: 0,
        title: '',
        des: ''
      },
      scoreModeForm: {
        questionType: -1,
        type: -1,
        index: -1,
        combinationIndex: -1,
        scoreMode: {}
      },
      questionRule: {
        totalNum: 0,
        totalScore: 0,
        ruleId: -1,
        ruleType: 0,
        isAdd: true,
        isEdit: false,
        questionTypeAll: [{ id: 0, name: '单选题' }, { id: 1, name: '多选题' }, { id: 2, name: '填空题' }, { id: 3, name: '判断题' }, { id: 4, name: '简答题' }],
        questionTypeList: [],
        questionDifficultyAll: [{ id: 0, name: '高' }, { id: 1, name: '中' }, { id: 2, name: '低' }],
        questionDifficultyList: [],
        questionBankList: [],
        rules: []
      },
      questionNumber: {},
      questionStatistics: {
        singleChoice: [],
        multipleChoice: [],
        judge: [],
        fillBlanks: [],
        briefAnswer: [],
        combination: [],
        score: 0,
        totalNum: 0
      },
      examinationForm: {
        questionLimit: false,
        time: 0,
        timeMinute: 0,
        timeSecond: 0,
        passingGrade: 0,
        brakBarrier: false,
        examinationTimeUse: false,
        examinationStartTime: '',
        examinationEndTime: '',
        examinationTime: '',
        multipleExamination: false,
        multipleExaminationTime: 0,
        chargeExamination: false,
        chargeExaminationPrice: 0,
        sign: false,
        scoreDisplay: false,
        questionTypeDisplay: false,
        randomOrder: '',
        randomOrderList: [],
        handAfterFinish: false,
        skipQuestion: false,
        preventionCheat: false,
        preventionCheatTime: 1,
        dominateScreen: false,
        calculator: false,
        formula: false,
        resultsView: 0,
        examinationCommentsUse: false,
        examinationComments: '',
        wrongQuestions: false,
        selfGrade: false
      },
      informationCollectionList: [],
      informationCollectionForm: {},
      optionList: [],
      selection: [],
      userSelection: [],
      titleSelection: [],
      invitedUserSelection: [],
      questionSelection: [],
      questionBankSelection: [],
      checkOrganization: '',
      checkOrganizationList: [],
      inviteOrganizationList: [],
      inviteAll: false,
      invitedTitleIds: [],
      invitedOrganizationSelection: [],
      invitedOrganizationIds: [],
      userColumns: [
        {
          type: 'index',
          title: ' ',
          width: 60,
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
          width: 60,
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
      titleColumns: [
        {
          type: 'index',
          title: ' ',
          width: 60,
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
          width: 60,
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
          width: 60,
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
      questionBankColumns: [
        {
          type: 'index',
          title: ' ',
          width: 60,
          align: 'center'
        },
        {
          title: '名称',
          slot: 'name',
          align: 'center',
          minWidth: 150
        }
      ],
      questionBankRuleColumns: [
        {
          type: 'index',
          title: ' ',
          width: 60,
          align: 'center'
        },
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '题库名称',
          key: 'name',
          align: 'center',
          minWidth: 150
        },
        {
          title: '题目数量',
          key: 'questionNum',
          align: 'center',
          width: 100
        },
        {
          title: '分类',
          key: 'typeName',
          align: 'center',
          minWidth: 80
        }
      ],
      questionColumns: [
        {
          type: 'index',
          title: ' ',
          width: 60,
          align: 'center'
        },
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '题干',
          slot: 'title',
          align: 'left',
          minWidth: 370
        },
        {
          title: '题目类型',
          key: 'type',
          align: 'center',
          minWidth: 100,
          render: (h, params) => {
            switch (params.row.type) {
              case 0:return h('Label', '单选题')
              case 1:return h('Label', '多选题')
              case 2:return h('Label', '填空题')
              case 3:return h('Label', '判断题')
              case 4:return h('Label', '简答题')
              case 5:return h('Label', '组合题')
            }
          }
        },
        {
          title: '难度',
          key: 'difficulty',
          align: 'center',
          minWidth: 80,
          render: (h, params) => {
            switch (params.row.difficulty) {
              case 0:return h('Label', '高')
              case 1:return h('Label', '中')
              case 2:return h('Label', '低')
            }
          }
        }
      ],
      userTableData: [],
      invitedUserTableData: [],
      titleTableData: [],
      invitedTitleTableData: [],
      invitedOrganizationTableData: [],
      questionBankTableData: [],
      questionTableData: [],
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
      if (to.name === 'paperEditor') {
        this.questionRule = {
          totalNum: 0,
          totalScore: 0,
          ruleId: -1,
          ruleType: 0,
          isAdd: true,
          isEdit: false,
          questionTypeAll: [{ id: 0, name: '单选题' }, { id: 1, name: '多选题' }, { id: 2, name: '填空题' }, { id: 3, name: '判断题' }, { id: 4, name: '简答题' }],
          questionTypeList: [],
          questionDifficultyAll: [{ id: 0, name: '高' }, { id: 1, name: '中' }, { id: 2, name: '低' }],
          questionDifficultyList: [],
          questionBankList: [],
          rules: []
        }
        this.questionStatistics = {
          singleChoice: [],
          multipleChoice: [],
          judge: [],
          fillBlanks: [],
          briefAnswer: [],
          combination: [],
          score: 0,
          totalNum: 0
        }
        this.questionList = []
        this.checkAll = false
        this.checkAllGroup = []
        this.combinationQuestionList = {}
        this.informationCollectionList = []
        this.getPaperType()
        this.init()
        this.getExamination()
        this.getInformationCollection()
      }
    }
  },
  methods: {
    goBack () {
      this.$router.go(-1)
    },
    showExamSet () {
      if (this.setWidth === 50) {
        this.setWidth = 450
      } else {
        this.setWidth = 50
      }
    },
    init () {
      this.loading = true
      let searchParams = {
        paperId: this.$route.query.paperId
      }
      API.paper.get(searchParams).then(({ data }) => {
        this.loading = false
        if (data && data.code === 0) {
          this.editForm = data.data
          if (!this.editForm.isFixed) {
            this.questionList = JSON.parse(this.editForm.questions)
            this.questionStatistics = {
              singleChoice: [],
              multipleChoice: [],
              judge: [],
              fillBlanks: [],
              briefAnswer: [],
              combination: [],
              score: 0,
              totalNum: 0
            }
            this.questionList.forEach((question) => {
              switch (question.type) {
                case 0 : this.questionStatistics.singleChoice.push(question.pkId); this.questionStatistics.totalNum += 1; this.questionStatistics.score += question.score; break
                case 1 : this.questionStatistics.multipleChoice.push(question.pkId); this.questionStatistics.totalNum += 1; this.questionStatistics.score += question.score; break
                case 2 : this.questionStatistics.fillBlanks.push(question.pkId); this.questionStatistics.totalNum += 1; this.questionStatistics.score += question.score; break
                case 3 : this.questionStatistics.judge.push(question.pkId); this.questionStatistics.totalNum += 1; this.questionStatistics.score += question.score; break
                case 4 : this.questionStatistics.briefAnswer.push(question.pkId); this.questionStatistics.totalNum += 1; this.questionStatistics.score += question.score; break
                case 5 : this.questionStatistics.combination.push(question.pkId); this.questionStatistics.totalNum += 1; this.questionStatistics.score += question.score; break
              }
            })
          } else {
            if (this.editForm.questionRule !== '{}') {
              this.questionRule = JSON.parse(this.editForm.questionRule)
              this.questionStatistics.score = this.questionRule.totalScore
              this.questionStatistics.totalNum = this.questionRule.totalNum
            }
          }
          this.timer = new Date().getTime()
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.loading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getExamination () {
      this.loading = true
      API.examination.get({ paperId: this.$route.query.paperId }).then(({ data }) => {
        this.loading = false
        if (data && data.code === 0) {
          this.examinationForm = data.data
          this.examinationForm.timeMinute = this.examinationForm.questionLimit ? parseInt(this.examinationForm.time / 60) : 0
          this.examinationForm.timeSecond = this.examinationForm.questionLimit ? this.examinationForm.time % 60 : 0
          let examinationTime = JSON.parse(this.examinationForm.examinationTime)
          this.examinationForm.examinationStartTime = examinationTime.startTime
          this.examinationForm.examinationEndTime = examinationTime.endTime
          this.examinationForm.randomOrderList = []
          if (this.examinationForm.randomOrder.indexOf('0') !== -1) {
            this.examinationForm.randomOrderList.push('0')
          }
          if (this.examinationForm.randomOrder.indexOf('1') !== -1) {
            this.examinationForm.randomOrderList.push('1')
          }
          this.examinationCommentsList = JSON.parse(this.examinationForm.examinationComments)
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
    getQuestionBank (reset) {
      if (reset === 'reset') {
        this.bankTypeId = ''
        this.bankName = ''
        this.questionBankPageIndex = 1
        this.questionBankPageSize = 10
      }
      let searchParams = {
        pageSize: this.questionBankPageSize,
        pageIndex: this.questionBankPageIndex,
        typeId: this.bankTypeId,
        name: this.bankName
      }
      this.questionBankTableLoading = true
      API.questionBank.list(searchParams).then(({ data }) => {
        this.questionBankTableLoading = false
        if (data && data.code === 0) {
          this.questionBankTableData = data.data.list
          this.questionBankTotalCount = data.data.totalCount
          if (!this.questionRule.isAdd) {
            setTimeout(() => {
              Object.values(this.$refs.questionBankSelection.objData).forEach((bank) => {
                this.questionRule.questionBankList.forEach((id) => {
                  if (bank.pkId === id) {
                    bank._isDisabled = true
                  }
                })
              })
            }, 200)
          }
          this.questionBankTimer = new Date().getTime()
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.questionBankTableLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getQuestion (reset, bankId) {
      if (reset === 'reset') {
        this.questionType = -1
        this.questionDifficulty = -1
        this.questionTitle = ''
        this.questionPageIndex = 1
        this.questionPageSize = 10
      }
      if (typeof bankId !== 'undefined') {
        this.bankId = bankId
      }
      this.questionBankTableLoading = true
      let searchParams = {
        pageSize: this.questionPageSize,
        pageIndex: this.questionPageIndex,
        bankId: this.bankId,
        type: this.questionType,
        difficulty: this.questionDifficulty,
        title: this.questionTitle
      }
      API.question.list(searchParams).then(({ data }) => {
        this.questionBankTableLoading = false
        if (data && data.code === 0) {
          this.questionTableData = data.data.list
          this.questionTotalCount = data.data.totalCount
          this.questionTimer = new Date().getTime()
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.questionBankTableLoading = false
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    getQuestionNumber () {
      API.question.number().then(({ data }) => {
        if (data && data.code === 0) {
          this.questionNumber = data.data
        } else {
          this.$Message.error(data.msg)
        }
      }).catch(() => {
        this.$Message.error('连接失败，请检查网络！')
      })
    },
    handleCheckAll () {
      if (this.indeterminate && this.checkAllGroup.length === this.questionList.length) {
        this.checkAll = false
      } else {
        this.checkAll = !this.checkAll
      }
      this.indeterminate = false

      if (this.checkAll) {
        this.checkAllGroup = this.questionList.map((question) => {
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
    openAll () {
      if (!this.isOpenAll) {
        this.isOpenAll = true
        this.openQuestionIds = this.questionList.map(question => {
          return question.pkId
        })
      } else {
        this.isOpenAll = false
        this.openQuestionIds = []
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
        this.openQuestionIds.push(id)
      } else {
        this.openQuestionIds.splice(index, 1)
      }
      if (this.openQuestionIds.length === 0) {
        this.isOpenAll = false
      }
      if (this.openQuestionIds.length === this.questionList.length) {
        this.isOpenAll = true
      }
    },
    getCombinationQuestionList (questionId) {
      let searchParams = {
        pageSize: 100,
        pageIndex: 1,
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
    setStatus (status) {
      let params = {
        pkId: this.editForm.pkId,
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
    tabChange (value) {
      this.userSelection = []
      this.titleSelection = []
      this.inviteOrganizationList = []
      this.timer = new Date().getTime()
      this.getOrganization()
    },
    editButton () {
      if (this.editForm.status === 1) {
        this.$Modal.confirm({
          title: '提示',
          width: 560,
          content: '<P style="text-align: center">此试卷处于已发布状态，编辑试卷将导致正在考试的考生无法继续考试。按“确定”继续编辑并自动取消发布状态！</P>',
          onOk: () => {
            API.paper.setStatus({ pkId: this.editForm.pkId, status: 0 }).then(({ data }) => {
              if (data && data.code === 0) {
                this.init()
                this.$Message.success({
                  content: data.msg
                })
                this.editModal = true
              } else {
                this.$Message.error(data.msg)
              }
            }).catch(() => {
              this.$Message.error('连接失败，请检查网络！')
            })
          }
        })
      } else {
        this.editModal = true
      }
    },
    showInviteUser () {
      this.getUser('reset')
      this.getOrganization()
      this.getTitle()
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
    showInformationCollectionModal () {
      this.getInformationCollection()
      this.informationCollectionModal = true
    },
    showAddOptionModal (info) {
      this.informationCollectionForm = info
      this.optionList = JSON.parse(info.option)
      this.optionModal = true
    },
    showExaminationComments () {
      this.examinationCommentsModal = true
    },
    showScoreMode (type, params, index, combinationIndex) {
      this.scoreModeForm.scoreMode = params.scoreMode
      this.scoreModeForm.questionType = params.type
      this.scoreModeForm.type = type
      this.scoreModeForm.index = index
      if (type === 1) {
        this.scoreModeForm.combinationIndex = combinationIndex
      }
      this.scoreModeModal = true
    },
    showManualChoose () {
      this.bankTypeId = ''
      this.bankName = ''
      this.questionBankPageIndex = 1
      this.questionBankPageSize = 10
      this.getQuestionBankType()
      this.getQuestionBank()
      this.manualChooseModal = true
    },
    showRandomChoose () {
      if (!this.editForm.isFixed) {
        this.questionRule = {
          totalNum: 0,
          totalScore: 0,
          ruleId: -1,
          ruleType: 0,
          isAdd: true,
          isEdit: false,
          questionTypeAll: [{ id: 0, name: '单选题' }, { id: 1, name: '多选题' }, { id: 2, name: '填空题' }, { id: 3, name: '判断题' }, { id: 4, name: '简答题' }],
          questionTypeList: [],
          questionDifficultyAll: [{ id: 0, name: '高' }, { id: 1, name: '中' }, { id: 2, name: '低' }],
          questionDifficultyList: [],
          questionBankList: [],
          rules: []
        }
      }
      this.randomChooseModal = true
    },
    showAddRule () {
      this.getQuestionBank()
      this.getQuestionBankType()
      this.getQuestionNumber()
      this.addRuleModal = true
    },
    selectionChange (data) {
      // 记录选中的数据
      this.selection = data
    },
    questionBankSelectionChange (data) {
      this.questionBankSelection = data
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
    addQuestionSelection (data, row) {
      switch (row.type) {
        case 0 : if (this.questionStatistics.singleChoice.indexOf(row.pkId) !== -1) { this.$Message.warning('您选择的题目已存在') }; this.questionStatistics.singleChoice.push(row.pkId); this.questionStatistics.totalNum += 1; break
        case 1 : if (this.questionStatistics.multipleChoice.indexOf(row.pkId) !== -1) { this.$Message.warning('您选择的题目已存在') }; this.questionStatistics.multipleChoice.push(row.pkId); this.questionStatistics.totalNum += 1; break
        case 2 : if (this.questionStatistics.fillBlanks.indexOf(row.pkId) !== -1) { this.$Message.warning('您选择的题目已存在') }; this.questionStatistics.fillBlanks.push(row.pkId); this.questionStatistics.totalNum += 1; break
        case 3 : if (this.questionStatistics.judge.indexOf(row.pkId) !== -1) { this.$Message.warning('您选择的题目已存在') }; this.questionStatistics.judge.push(row.pkId); this.questionStatistics.totalNum += 1; break
        case 4 : if (this.questionStatistics.briefAnswer.indexOf(row.pkId) !== -1) { this.$Message.warning('您选择的题目已存在') }; this.questionStatistics.briefAnswer.push(row.pkId); this.questionStatistics.totalNum += 1; break
        case 5 : if (this.questionStatistics.combination.indexOf(row.pkId) !== -1) { this.$Message.warning('您选择的题目已存在') }; this.questionStatistics.combination.push(row.pkId); this.questionStatistics.totalNum += 1; this.getCombinationQuestionList(row.pkId); break
      }
    },
    removeQuestionSelection (data, row) {
      switch (row.type) {
        case 0 : this.questionStatistics.singleChoice.splice(this.questionStatistics.singleChoice.indexOf(row.pkId), 1); this.questionStatistics.totalNum -= 1; break
        case 1 : this.questionStatistics.multipleChoice.splice(this.questionStatistics.multipleChoice.indexOf(row.pkId), 1); this.questionStatistics.totalNum -= 1; break
        case 2 : this.questionStatistics.fillBlanks.splice(this.questionStatistics.fillBlanks.indexOf(row.pkId), 1); this.questionStatistics.totalNum -= 1; break
        case 3 : this.questionStatistics.judge.splice(this.questionStatistics.judge.indexOf(row.pkId), 1); this.questionStatistics.totalNum -= 1; break
        case 4 : this.questionStatistics.briefAnswer.splice(this.questionStatistics.briefAnswer.indexOf(row.pkId), 1); this.questionStatistics.totalNum -= 1; break
        case 5 : this.questionStatistics.combination.splice(this.questionStatistics.combination.indexOf(row.pkId), 1); this.questionStatistics.totalNum -= 1; break
      }
    },
    addRules () {
      if (this.questionRule.ruleId === 0) {
        if (this.questionRule.questionTypeList.length === 0) {
          this.$Message.warning('请至少选择一项')
        } else {
          if (!this.questionRule.ruleType) {
            let typeNam = []
            let totalNum = 0
            this.questionRule.questionTypeList.forEach((type) => {
              typeNam.push(this.getTypeName(type))
              totalNum += this.questionNumber.type[type]
            })
            let rule = {
              type: this.questionRule.questionTypeList,
              typeName: typeNam,
              num: 1,
              totalNum: totalNum,
              score: 1,
              totalScore: 1
            }
            this.questionRule.totalNum += 1
            this.questionRule.totalScore += 1
            this.questionRule.rules.push(rule)
          } else {
            this.questionRule.questionTypeList.forEach((type) => {
              let rule = {
                type: [type],
                typeName: [this.getTypeName(type)],
                num: 1,
                totalNum: this.questionNumber.type[type],
                score: 1,
                totalScore: 1
              }
              this.questionRule.totalNum += 1
              this.questionRule.totalScore += 1
              this.questionRule.rules.push(rule)
            })
          }
          this.questionRule.questionTypeList.forEach((id) => {
            this.questionRule.questionTypeAll.forEach((questionType, index) => {
              if (questionType.id === id) {
                this.questionRule.questionTypeAll.splice(index, 1)
              }
            })
          })
          this.questionRule.isAdd = false
          this.addRuleModal = false
        }
      } else if (this.questionRule.ruleId === 1) {
        if (this.questionRule.questionDifficultyList.length === 0) {
          this.$Message.warning('请至少选择一项')
        } else {
          if (!this.questionRule.ruleType) {
            let typeName = []
            let totalNum = 0
            this.questionRule.questionDifficultyList.forEach((difficulty) => {
              typeName.push(this.getDifficultyName(difficulty))
              totalNum += this.questionNumber.difficulty[difficulty]
            })
            let rule = {
              type: this.questionRule.questionDifficultyList,
              typeName: typeName,
              num: 1,
              totalNum: totalNum,
              score: 1,
              totalScore: 1
            }
            this.questionRule.totalNum += 1
            this.questionRule.totalScore += 1
            this.questionRule.rules.push(rule)
          } else {
            this.questionRule.questionDifficultyList.forEach((difficulty) => {
              let rule = {
                type: [difficulty],
                typeName: [this.getDifficultyName(difficulty)],
                num: 1,
                totalNum: this.questionNumber.difficulty[difficulty],
                score: 1,
                totalScore: 1
              }
              this.questionRule.totalNum += 1
              this.questionRule.totalScore += 1
              this.questionRule.rules.push(rule)
            })
          }
          this.questionRule.questionDifficultyList.forEach((id) => {
            this.questionRule.questionDifficultyAll.forEach((difficulty, index) => {
              if (difficulty.id === id) {
                this.questionRule.questionDifficultyAll.splice(index, 1)
              }
            })
          })
          this.questionRule.isAdd = false
          this.addRuleModal = false
        }
      } else if (this.questionRule.ruleId === 2) {
        if (this.questionBankSelection.length === 0) {
          this.$Message.warning('请至少选择一项')
        } else {
          if (!this.questionRule.ruleType) {
            let type = []
            let typeName = []
            let totalNumber = 0
            this.questionBankSelection.forEach((bank) => {
              this.questionRule.questionBankList.push(bank.pkId)
              type.push(bank.pkId)
              typeName.push(bank.name)
              totalNumber += bank.questionNum
            })
            let rule = {
              type: type,
              typeName: typeName,
              num: 1,
              totalNum: totalNumber,
              score: 1,
              totalScore: 1
            }
            this.questionRule.totalNum += 1
            this.questionRule.totalScore += 1
            this.questionRule.rules.push(rule)
          } else {
            this.questionBankSelection.forEach((bank) => {
              this.questionRule.questionBankList.push(bank.pkId)
              let rule = {
                type: [bank.pkId],
                typeName: [bank.name],
                num: 1,
                totalNum: bank.questionNum,
                score: 1,
                totalScore: 1
              }
              this.questionRule.totalNum += 1
              this.questionRule.totalScore += 1
              this.questionRule.rules.push(rule)
            })
          }
          this.questionRule.isAdd = false
          this.addRuleModal = false
        }
      }
    },
    ruleChange (index) {
      let num = this.questionRule.rules[index].num
      let score = this.questionRule.rules[index].score
      this.questionRule.rules[index].totalScore = num * score
      let totalNum = 0
      let totalScore = 0
      this.questionRule.rules.forEach((rule) => {
        totalNum += rule.num
        totalScore += rule.totalScore
      })
      this.questionRule.totalNum = totalNum
      this.questionRule.totalScore = totalScore
    },
    deleteRule (index) {
      this.$Modal.confirm({
        title: '提示',
        content: '<P>确定要删除吗</P>',
        onOk: () => {
          if (this.questionRule.ruleId === 0) {
            this.questionRule.rules[index].type.forEach((id) => {
              let type = {
                id: id,
                name: this.getTypeName(id)
              }
              this.questionRule.questionTypeAll.push(type)
            })
          } else if (this.questionRule.ruleId === 1) {
            this.questionRule.rules[index].type.forEach((id) => {
              let difficulty = {
                id: id,
                name: this.getDifficultyName(id)
              }
              this.questionRule.questionDifficultyAll.push(difficulty)
            })
          } else if (this.questionRule.ruleId === 2) {
            this.questionRule.rules[index].type.forEach((id) => {
              this.questionRule.questionBankList.splice(this.questionRule.questionBankList.indexOf(id), 1)
            })
          }
          let num = this.questionRule.rules[index].num
          let score = this.questionRule.rules[index].totalScore
          this.questionRule.totalNum -= num
          this.questionRule.totalScore -= score
          this.questionRule.rules.splice(index, 1)
          if (!this.questionRule.rules.length) {
            this.questionRule.ruleId = -1
            this.questionRule.ruleType = 0
            this.questionRule.questionTypeList = []
            this.questionRule.questionDifficultyList = []
            this.questionRule.questionBankList = []
            this.questionRule.isAdd = true
          }
        }
      })
    },
    savePaperRules () {
      if (!this.editForm.isFixed) {
        API.question.random(this.questionRule).then(({ data }) => {
          if (data && data.code === 0) {
            this.questionSelection = data.data
            this.questionSelection.forEach((question) => {
              if (question.type === 5) {
                this.getCombinationQuestionList(question.pkId)
              }
            })
            setTimeout(() => {
              this.saveSetPaperQuestions()
              this.randomChooseModal = false
            }, 200)
          } else {
            this.$Message.error(data.msg)
          }
        }).catch(() => {
          this.$Message.error('连接失败，请检查网络！')
        })
      } else {
        let params = {
          pkId: this.editForm.pkId,
          questionRule: JSON.stringify(this.questionRule)
        }
        this.saveLoading = true
        API.paper.setRules(params).then(({ data }) => {
          if (data && data.code === 0) {
            this.saveLoading = false
            this.randomChooseModal = false
            this.init()
            this.$Message.success({
              content: data.msg
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
    questionSelectionChange (data) {
      this.questionSelection = data
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
    userPageChange (val) {
      this.userPageIndex = val
      this.getUser()
    },
    invitedUserPageChange (val) {
      this.invitedUserPageIndex = val
      this.getUser()
    },
    questionBankPageChange (val) {
      this.questionBankPageIndex = val
      this.getQuestion()
    },
    questionPageChange (val) {
      this.questionPageIndex = val
      this.getQuestionBank()
    },
    userSizeChange (size) {
      this.userPageSize = size
      this.getInvitedUser()
    },
    invitedUserSizeChange (size) {
      this.invitedUserPageSize = size
      this.getInvitedUser()
    },
    questionBankSizeChange (size) {
      this.questionBankPageSize = size
      this.getQuestionBank()
    },
    questionSizeChange (size) {
      this.questionPageSize = size
      this.getQuestion()
    },
    cancel () {
      this.editModal = false
    },
    resetQuestionStatistics (val) {
      if (!val) {
        this.questionPageIndex = 1
        this.questionPageSize = 10
        this.questionTotalCount = 0
        this.questionTableData = []
        this.questionSelection = []
      }
    },
    resetQuestionRule (val) {
      if (!val) {
        this.bankTypeId = ''
        this.bankName = ''
        this.questionBankPageIndex = 1
        this.questionBankPageSize = 10
        this.questionRule.questionTypeList = []
        this.questionRule.questionDifficultyList = []
        this.questionBankSelection = []
      }
    },
    saveEdit () {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          let params = {
            pkId: this.editForm.pkId || undefined,
            typeId: this.editForm.typeId,
            title: this.editForm.title,
            des: this.editForm.des
          }
          this.saveLoading = true
          API.paper.update(params).then(({ data }) => {
            if (data && data.code === 0) {
              this.saveLoading = false
              this.$Message.success({
                content: data.msg,
                onClose: () => {
                  this.editModal = false
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
    saveEditPaperUser (type) {
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
      API.paper.setUser(params).then(({ data }) => {
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
    saveSetPaperQuestions () {
      let isAdd = true
      this.questionList.forEach((question) => {
        this.questionSelection.forEach((addQuestion) => {
          if (question.pkId === addQuestion.pkId) {
            isAdd = false
            this.$Message.warning('您选择的题目已存在')
          }
          return isAdd
        })
        return isAdd
      })
      if (isAdd) {
        this.questionSelection.forEach((question) => {
          let scoreMode = ''
          if (question.type === 1) {
            let answer = JSON.parse(question.answer)
            let answers = []
            answer.answerIds.forEach((id) => {
              answer.answerOptionList.forEach(option => {
                if (id === option.id) {
                  answers.push({ id: option.id, content: option.content, score: 0 })
                }
              })
            })
            scoreMode = {
              modeId: 0,
              answers: answers,
              missSelectScore: 0
            }
          } else if (question.type === 2) {
            let answer = JSON.parse(question.answer)
            let answers = answer.answerList.map(blank => {
              return { score: 0, answers: blank }
            })
            scoreMode = {
              modeId: 5,
              answers: answers,
              missSelectScore: 0
            }
          } else if (question.type === 4) {
            let answers = []
            if (question.answer !== '{}') {
              let answer = JSON.parse(question.answer)
              answers = answer.answerList.map(keyWords => {
                return { score: 0, answers: keyWords }
              })
            }
            scoreMode = {
              modeId: 8,
              answers: answers,
              missSelectScore: 0
            }
          }
          let questions = []
          if (question.type === 5) {
            questions = this.combinationQuestionList[question.pkId].map(item => {
              let scoreMode = ''
              if (item.type === 1) {
                let answer = JSON.parse(item.answer)
                let answers = []
                answer.answerIds.forEach((id) => {
                  answer.answerOptionList.forEach((option) => {
                    if (id === option.id) {
                      answers.push({ id: option.id, content: option.content, score: 0 })
                    }
                  })
                })
                scoreMode = {
                  modeId: 0,
                  answers: answers,
                  missSelectScore: 0
                }
              } else if (item.type === 2) {
                let answer = JSON.parse(item.answer)
                let answers = answer.answerList.map(blank => {
                  return { score: 0, answers: blank }
                })
                scoreMode = {
                  modeId: 5,
                  answers: answers,
                  missSelectScore: 0
                }
              } else if (item.type === 4) {
                let answers = []
                if (item.answer !== '{}') {
                  let answer = JSON.parse(item.answer)
                  answers = answer.answerList.map(keyWords => {
                    return { score: 0, answers: keyWords }
                  })
                }
                scoreMode = {
                  modeId: 8,
                  answers: answers,
                  missSelectScore: 0
                }
              }
              return {
                pkId: item.pkId,
                type: item.type,
                difficulty: item.difficulty,
                title: item.title,
                analysis: item.analysis,
                score: 0,
                scoreMode: scoreMode,
                answer: item.answer,
                userScore: 0,
                userAnswer: ''
              }
            })
          }
          let questionForm = {
            pkId: question.pkId,
            type: question.type,
            difficulty: question.difficulty,
            title: question.title,
            analysis: question.analysis,
            score: question.score,
            scoreMode: scoreMode,
            answer: question.answer,
            userScore: 0,
            userAnswer: '',
            questions: questions
          }
          this.questionList.push(questionForm)
        })
        let params = {
          pkId: this.editForm.pkId,
          questions: JSON.stringify(this.questionList)
        }
        this.saveLoading = true
        API.paper.setQuestions(params).then(({ data }) => {
          if (data && data.code === 0) {
            this.saveLoading = false
            this.manualChooseModal = false
            this.init()
            this.$Message.success({
              content: data.msg
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
    saveEditPaperQuestions (type, index, combinationIndex) {
      let save = true
      switch (type) {
        case 0 : this.questionList[index].scoreMode = this.scoreModeForm.scoreMode
          if (this.scoreModeForm.scoreMode.modeId === 1 || this.scoreModeForm.scoreMode.modeId === 6 || this.scoreModeForm.scoreMode.modeId === 9) {
            let score = 0
            this.scoreModeForm.scoreMode.answers.forEach((answer) => {
              score += answer.score
            })
            this.questionList[index].score = score
          }; break
        case 1 : this.questionList[index].questions[combinationIndex].scoreMode = this.scoreModeForm.scoreMode
          if (this.scoreModeForm.scoreMode.modeId === 1 || this.scoreModeForm.scoreMode.modeId === 6 || this.scoreModeForm.scoreMode.modeId === 9) {
            let score = 0
            this.scoreModeForm.scoreMode.answers.forEach((answer) => {
              score += answer.score
            })
            this.questionList[index].score = this.questionList[index].score - this.questionList[index].questions[combinationIndex].score + score
            this.questionList[index].questions[combinationIndex].score = score
          }; break
        case 2 : if (this.$refs['questionScore' + index][0].value !== this.$refs['questionScore' + index][0].currentValue) { this.questionList[index].score = this.$refs['questionScore' + index][0].currentValue } else { save = false }; break
        case 3 : let oldScore = this.$refs['combinationQuestionScore' + combinationIndex][0].value; let newScore = this.$refs['combinationQuestionScore' + combinationIndex][0].currentValue
          if (oldScore !== newScore) {
            this.questionList[index].score = this.questionList[index].score - oldScore + newScore
            this.questionList[index].questions[combinationIndex].score = newScore
          } else { save = false }; break
        case 4 : this.moveList(this.questionList, index, index - 1); break
        case 5 : this.moveList(this.questionList, index, index + 1); break
      }
      let params = {
        pkId: this.editForm.pkId,
        questions: JSON.stringify(this.questionList)
      }
      if (save) {
        API.paper.setQuestions(params).then(({ data }) => {
          if (data && data.code === 0) {
            this.scoreModeModal = false
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
    },
    saveEditExamination () {
      let randomOrder = ''
      this.examinationForm.randomOrderList.forEach((item, index) => {
        if (index === 0) {
          randomOrder = item
        } else {
          randomOrder = randomOrder + ',' + item
        }
      })
      let params = {
        pkId: this.examinationForm.pkId,
        questionLimit: this.examinationForm.questionLimit ? 0 : 1,
        time: this.examinationForm.questionLimit ? (this.examinationForm.timeMinute * 60 + this.examinationForm.timeSecond) : this.examinationForm.time,
        passingGrade: this.examinationForm.passingGrade,
        brakBarrier: this.examinationForm.brakBarrier ? 0 : 1,
        examinationTimeUse: this.examinationForm.examinationTimeUse ? 0 : 1,
        examinationTime: JSON.stringify({ startTime: this.examinationForm.examinationStartTime, endTime: this.examinationForm.examinationEndTime }),
        multipleExamination: this.examinationForm.multipleExamination ? 0 : 1,
        multipleExaminationTime: this.examinationForm.multipleExaminationTime,
        chargeExamination: this.examinationForm.chargeExamination ? 0 : 1,
        chargeExaminationPrice: this.examinationForm.chargeExaminationPrice,
        sign: this.examinationForm.sign ? 0 : 1,
        scoreDisplay: this.examinationForm.scoreDisplay ? 0 : 1,
        questionTypeDisplay: this.examinationForm.questionTypeDisplay ? 0 : 1,
        randomOrder: randomOrder,
        handAfterFinish: this.examinationForm.handAfterFinish ? 0 : 1,
        skipQuestion: this.examinationForm.skipQuestion ? 0 : 1,
        preventionCheat: this.examinationForm.preventionCheat ? 0 : 1,
        preventionCheatTime: this.examinationForm.preventionCheatTime,
        dominateScreen: this.examinationForm.dominateScreen ? 0 : 1,
        calculator: this.examinationForm.calculator ? 0 : 1,
        formula: this.examinationForm.formula ? 0 : 1,
        resultsView: this.examinationForm.resultsView,
        examinationCommentsUse: this.examinationForm.examinationCommentsUse ? 0 : 1,
        examinationComments: JSON.stringify(this.examinationCommentsList),
        wrongQuestions: this.examinationForm.wrongQuestions ? 0 : 1,
        selfGrade: this.examinationForm.selfGrade ? 0 : 1
      }
      API.examination.update(params).then(({ data }) => {
        if (data && data.code === 0) {
          this.$Message.success({
            content: data.msg,
            onClose: () => {
              this.getExamination()
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
          this.getInformationCollection()
          this.$Message.success({
            content: data.msg
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
        case 3 : this.moveList(this.optionList, index, index - 1); break
        case 4 : this.moveList(this.optionList, index, index + 1); break
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
    moveList (list, index1, index2) {
      list[index1] = list.splice(index2, 1, list[index1])[0]
    },
    questionRandomChange () {
      let questionRandom = this.examinationForm.randomOrderList.indexOf('0')
      if (questionRandom !== -1) {
        this.examinationForm.randomOrderList.splice(questionRandom, 1)
      } else {
        this.examinationForm.randomOrderList.push('0')
      }
      this.optionRandomTimer = new Date().getTime()
      this.saveEditExamination()
    },
    optionRandomChange () {
      let optionRandom = this.examinationForm.randomOrderList.indexOf('1')
      if (optionRandom !== -1) {
        this.examinationForm.randomOrderList.splice(optionRandom, 1)
      } else {
        this.examinationForm.randomOrderList.push('1')
      }
      this.questionRandomTimer = new Date().getTime()
      this.saveEditExamination()
    },
    saveComment (type, index) {
      let save = true
      switch (type) {
        case 0 : let comment = '评语' + (this.examinationCommentsList.length + 1); if (this.examinationCommentsList.indexOf(comment) === -1) { this.examinationCommentsList.push(comment) } else { this.$Message.warning({ content: '评语重复' }); save = false }; break
        case 1 : if (this.$refs['comment' + index][0].value !== this.$refs['comment' + index][0].currentValue) { this.examinationCommentsList[index] = this.$refs['comment' + index][0].currentValue } else { save = false }; break
        case 2 : this.examinationCommentsList.splice(index, 1); break
      }
      console.log(this.examinationCommentsList)
      if (save) {
        this.saveEditExamination()
      }
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
            this.saveEditPaperUser(0)
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
            this.saveEditPaperUser(1)
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
            this.saveEditPaperUser(2)
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
              this.getInformationCollection()
              this.$Message.success({
                content: data.msg
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
    deletePaperQuestion (pkId) {
      let questions = []
      if (typeof pkId === 'undefined') {
        questions = this.checkAllGroup
      } else {
        questions = [pkId]
      }
      if (questions.length === 0) {
        this.$Message.error('请选择题目')
      } else {
        this.$Modal.confirm({
          title: '提示',
          content: '<P>确定要删除吗</P>',
          onOk: () => {
            questions.forEach((id) => {
              this.questionList.forEach((question, index) => {
                if (question.pkId === id) {
                  this.questionList.splice(index, 1)
                }
              })
            })
            let params = {
              pkId: this.editForm.pkId,
              questions: JSON.stringify(this.questionList)
            }
            API.paper.setQuestions(params).then(({ data }) => {
              if (data && data.code === 0) {
                this.checkAll = false
                this.indeterminate = false
                this.checkAllGroup = []
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
      }
    }
  },
  mounted () {
    this.init()
    this.getPaperType()
    this.getExamination()
    this.getInformationCollection()
  }
}
</script>
<style>
  .ivu-table-header {
    color: #909399;
  }
</style>
