import axios from '@/libs/api.request'

// 获取所有试卷
export const list = (params) => {
  return axios.request({
    url: 'paper/list',
    params: params,
    method: 'get'
  })
}

// 获取试卷
export const get = (params) => {
  return axios.request({
    url: 'paper/get',
    params: params,
    method: 'get'
  })
}

// 添加试卷
export const add = (params) => {
  return axios.request({
    url: 'paper/add',
    data: params,
    method: 'post'
  })
}

// 修改试卷
export const update = (params) => {
  return axios.request({
    url: 'paper/update',
    data: params,
    method: 'post'
  })
}

// 修改试卷状态
export const setStatus = (params) => {
  return axios.request({
    url: 'paper/set/status',
    data: params,
    method: 'post'
  })
}

// 修改试卷考生
export const setUser = (params) => {
  return axios.request({
    url: 'paper/set/user',
    data: params,
    method: 'post'
  })
}

// 设置试卷题目
export const setQuestions = (params) => {
  return axios.request({
    url: 'paper/set/questions',
    data: params,
    method: 'post'
  })
}

// 设置试卷规则
export const setRules = (params) => {
  return axios.request({
    url: 'paper/set/rules',
    data: params,
    method: 'post'
  })
}

// 试卷归档
export const placeFile = (params) => {
  return axios.request({
    url: 'paper/placeFile',
    data: params,
    method: 'post'
  })
}

// 删除试卷
export const del = (params) => {
  return axios.request({
    url: 'paper/delete',
    data: params,
    method: 'post'
  })
}
