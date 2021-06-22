import * as home from './module/home'
import * as manager from './module/sys'
import * as role from './module/role'
import * as organization from './module/organization'
import * as title from './module/title'
import * as informationCollection from './module/informationCollection'
import * as classManage from './module/class'
import * as classType from './module/classType'
import * as catalog from './module/catalog'
import * as coursewareType from './module/coursewareType'
import * as courseware from './module/courseware'
import * as catalogCourseware from './module/catalogCourseware'
import * as paperType from './module/paperType'
import * as paper from './module/paper'
import * as examination from './module/examination'
import * as examinationPaper from './module/examinationPaper'
import * as questionBankType from './module/questionBankType'
import * as questionBank from './module/questionBank'
import * as question from './module/question'
import * as knowledgePointType from './module/knowledgePointType'
import * as knowledgePoint from './module/knowledgePoint'

export default {
  home,
  manager, // 系统管理
  title, // 标签管理
  informationCollection, // 信息采集管理
  role, // 角色管理
  organization, // 组织架构管理
  classManage, // 课程管理
  classType, // 课程分类
  catalog, // 目录管理
  coursewareType, // 课件分类
  courseware, // 课件管理
  catalogCourseware, // 目录课件
  paperType, // 试卷分类管理
  paper, // 试卷管理
  examination, // 考试设置
  examinationPaper, // 考生试卷
  questionBankType, // 题库分类管理
  questionBank, // 题库管理
  question, // 题目管理
  knowledgePointType, // 知识点分类
  knowledgePoint // 知识点管理
}
