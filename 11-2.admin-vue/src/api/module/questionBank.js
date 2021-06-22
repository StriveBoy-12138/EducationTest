import axios from '@/libs/api.request'

// 获取所有题库
export const list = (params) => {
  return axios.request({
    url: 'question/bank/list',
    params: params,
    method: 'get'
  })
}

// 添加题库
export const add = (params) => {
  return axios.request({
    url: 'question/bank/add',
    data: params,
    method: 'post'
  })
}

// 修改题库
export const update = (params) => {
  return axios.request({
    url: 'question/bank/update',
    data: params,
    method: 'post'
  })
}

// 删除题库
export const del = (params) => {
  return axios.request({
    url: 'question/bank/delete',
    data: params,
    method: 'post'
  })
}
