import axios from '@/libs/api.request'

// 获取所有信息采集
export const list = (params) => {
  return axios.request({
    url: 'information/collection/list',
    params: params,
    method: 'get'
  })
}

// 添加信息采集
export const add = (params) => {
  return axios.request({
    url: 'information/collection/add',
    data: params,
    method: 'post'
  })
}

// 修改信息采集
export const update = (params) => {
  return axios.request({
    url: 'information/collection/update',
    data: params,
    method: 'post'
  })
}

// 上移下移信息采集
export const move = (params) => {
  return axios.request({
    url: 'information/collection/move',
    data: params,
    method: 'post'
  })
}

// 删除信息采集
export const del = (params) => {
  return axios.request({
    url: 'information/collection/delete',
    data: params,
    method: 'post'
  })
}
