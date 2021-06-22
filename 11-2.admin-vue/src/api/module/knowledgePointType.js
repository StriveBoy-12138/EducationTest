import axios from '@/libs/api.request'

// 获取所有知识点分类
export const list = (params) => {
  return axios.request({
    url: 'knowledge/point/type/list',
    params: params,
    method: 'get'
  })
}

// 添加知识点分类
export const add = (params) => {
  return axios.request({
    url: 'knowledge/point/type/add',
    data: params,
    method: 'post'
  })
}

// 修改知识点分类
export const update = (params) => {
  return axios.request({
    url: 'knowledge/point/type/update',
    data: params,
    method: 'post'
  })
}

// 删除知识点分类
export const del = (params) => {
  return axios.request({
    url: 'knowledge/point/type/delete',
    data: params,
    method: 'post'
  })
}
