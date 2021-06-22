import axios from '@/libs/api.request'

// 获取所有课件
export const list = (params) => {
  return axios.request({
    url: 'courseware/list',
    params: params,
    method: 'get'
  })
}

// 添加课件
export const add = (params) => {
  return axios.request({
    url: 'courseware/add',
    data: params,
    method: 'post'
  })
}

// 修改课件
export const update = (params) => {
  return axios.request({
    url: 'courseware/update',
    data: params,
    method: 'post'
  })
}

// 删除课件
export const del = (params) => {
  return axios.request({
    url: 'courseware/delete',
    data: params,
    method: 'post'
  })
}
