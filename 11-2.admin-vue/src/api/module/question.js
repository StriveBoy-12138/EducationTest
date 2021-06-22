import axios from '@/libs/api.request'

// 获取所有题目
export const list = (params) => {
  return axios.request({
    url: 'question/list',
    params: params,
    method: 'get'
  })
}

// 获取所有题目数量
export const number = (params) => {
  return axios.request({
    url: 'question/number',
    params: params,
    method: 'get'
  })
}

// 获取随机题目
export const random = (params) => {
  return axios.request({
    url: 'question/random',
    data: params,
    method: 'post'
  })
}

// 添加题目
export const add = (params) => {
  return axios.request({
    url: 'question/add',
    data: params,
    method: 'post'
  })
}

// 修改题目
export const update = (params) => {
  return axios.request({
    url: 'question/update',
    data: params,
    method: 'post'
  })
}

// 设置题目知识点
export const setKnowledgePoint = (params) => {
  return axios.request({
    url: 'question/set/knowledgePoint',
    data: params,
    method: 'post'
  })
}

// 删除题目
export const del = (params) => {
  return axios.request({
    url: 'question/delete',
    data: params,
    method: 'post'
  })
}
