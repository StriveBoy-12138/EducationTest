import axios from '@/libs/api.request'

// 获取所有题库分类
export const list = (params) => {
  return axios.request({
    url: 'question/bank/type/list',
    params: params,
    method: 'get'
  })
}

// 添加题库分类
export const add = (params) => {
  return axios.request({
    url: 'question/bank/type/add',
    data: params,
    method: 'post'
  })
}

// 修改题库分类
export const update = (params) => {
  return axios.request({
    url: 'question/bank/type/update',
    data: params,
    method: 'post'
  })
}

// 删除题库分类
export const del = (params) => {
  return axios.request({
    url: 'question/bank/type/delete',
    data: params,
    method: 'post'
  })
}
