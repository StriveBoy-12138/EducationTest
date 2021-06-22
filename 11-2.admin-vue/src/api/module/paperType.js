import axios from '@/libs/api.request'

// 获取所有试卷分类
export const list = (params) => {
  return axios.request({
    url: 'paper/type/list',
    params: params,
    method: 'get'
  })
}

// 添加试卷分类
export const add = (params) => {
  return axios.request({
    url: 'paper/type/add',
    data: params,
    method: 'post'
  })
}

// 修改试卷分类
export const update = (params) => {
  return axios.request({
    url: 'paper/type/update',
    data: params,
    method: 'post'
  })
}

// 删除试卷分类
export const del = (params) => {
  return axios.request({
    url: 'paper/type/delete',
    data: params,
    method: 'post'
  })
}
