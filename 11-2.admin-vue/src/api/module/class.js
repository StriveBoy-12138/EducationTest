import axios from '@/libs/api.request'

// 获取所有课程
export const list = (params) => {
  return axios.request({
    url: 'class/list',
    params: params,
    method: 'get'
  })
}

// 添加课程
export const add = (params) => {
  return axios.request({
    url: 'class/add',
    data: params,
    method: 'post'
  })
}

// 修改课程
export const update = (params) => {
  return axios.request({
    url: 'class/update',
    data: params,
    method: 'post'
  })
}

// 设置课程成员
export const setUser = (params) => {
  return axios.request({
    url: 'class/set/user',
    data: params,
    method: 'post'
  })
}

// 设置课程试卷
export const setPaper = (params) => {
  return axios.request({
    url: 'class/set/paper',
    data: params,
    method: 'post'
  })
}

// (取消)发布课程
export const status = (params) => {
  return axios.request({
    url: 'class/status',
    data: params,
    method: 'post'
  })
}

// 删除课程
export const del = (params) => {
  return axios.request({
    url: 'class/delete',
    data: params,
    method: 'post'
  })
}
