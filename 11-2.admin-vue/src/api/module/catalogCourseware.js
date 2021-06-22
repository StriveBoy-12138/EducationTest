import axios from '@/libs/api.request'

// 获取所有目录课件
export const list = (params) => {
  return axios.request({
    url: 'catalog/courseware/list',
    params: params,
    method: 'get'
  })
}

// 添加目录课件
export const add = (params) => {
  return axios.request({
    url: 'catalog/courseware/add',
    data: params,
    method: 'post'
  })
}

// 上移下移目录课件
export const move = (params) => {
  return axios.request({
    url: 'catalog/courseware/move',
    data: params,
    method: 'post'
  })
}

// 删除目录课件
export const del = (params) => {
  return axios.request({
    url: 'catalog/courseware/delete',
    data: params,
    method: 'post'
  })
}
