import axios from '@/libs/api.request'

// 获取所有课程分类
export const list = (params) => {
  return axios.request({
    url: 'class/type/list',
    params: params,
    method: 'get'
  })
}

// 添加课程分类
export const add = (params) => {
  return axios.request({
    url: 'class/type/add',
    data: params,
    method: 'post'
  })
}

// 修改课程分类
export const update = (params) => {
  return axios.request({
    url: 'class/type/update',
    data: params,
    method: 'post'
  })
}

// 删除课程分类
export const del = (params) => {
  return axios.request({
    url: 'class/type/delete',
    data: params,
    method: 'post'
  })
}
