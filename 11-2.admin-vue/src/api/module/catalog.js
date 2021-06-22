import axios from '@/libs/api.request'

// 获取所有目录
export const list = (params) => {
  return axios.request({
    url: 'catalog/list',
    params: params,
    method: 'get'
  })
}

// 添加目录
export const add = (params) => {
  return axios.request({
    url: 'catalog/add',
    data: params,
    method: 'post'
  })
}

// 修改目录
export const update = (params) => {
  return axios.request({
    url: 'catalog/update',
    data: params,
    method: 'post'
  })
}

// 上移下移目录
export const move = (params) => {
  return axios.request({
    url: 'catalog/move',
    data: params,
    method: 'post'
  })
}

// 删除目录
export const del = (params) => {
  return axios.request({
    url: 'catalog/delete',
    data: params,
    method: 'post'
  })
}
