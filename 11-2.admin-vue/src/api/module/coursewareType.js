import axios from '@/libs/api.request'

// 获取所有课件分类
export const list = (params) => {
  return axios.request({
    url: 'courseware/type/list',
    params: params,
    method: 'get'
  })
}

// 添加课件分类
export const add = (params) => {
  return axios.request({
    url: 'courseware/type/add',
    data: params,
    method: 'post'
  })
}

// 修改课件分类
export const update = (params) => {
  return axios.request({
    url: 'courseware/type/update',
    data: params,
    method: 'post'
  })
}

// 删除课件分类
export const del = (params) => {
  return axios.request({
    url: 'courseware/type/delete',
    data: params,
    method: 'post'
  })
}
