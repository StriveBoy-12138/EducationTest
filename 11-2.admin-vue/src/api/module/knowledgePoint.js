import axios from '@/libs/api.request'

// 获取所有知识点
export const list = (params) => {
  return axios.request({
    url: 'knowledge/point/list',
    params: params,
    method: 'get'
  })
}

// 添加知识点
export const add = (params) => {
  return axios.request({
    url: 'knowledge/point/add',
    data: params,
    method: 'post'
  })
}

// 修改知识点
export const update = (params) => {
  return axios.request({
    url: 'knowledge/point/update',
    data: params,
    method: 'post'
  })
}

// 删除知识点
export const del = (params) => {
  return axios.request({
    url: 'knowledge/point/delete',
    data: params,
    method: 'post'
  })
}
