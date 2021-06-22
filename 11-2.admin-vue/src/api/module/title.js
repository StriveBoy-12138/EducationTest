import axios from '@/libs/api.request'

// 获取所有标签
export const list = (params) => {
  return axios.request({
    url: 'title/list',
    params: params,
    method: 'get'
  })
}

// 获取邀请标签
export const inviteList = (params) => {
  return axios.request({
    url: 'title/invite/list',
    params: params,
    method: 'get'
  })
}

// 添加标签
export const add = (params) => {
  return axios.request({
    url: 'title/add',
    data: params,
    method: 'post'
  })
}

// 修改标签
export const update = (params) => {
  return axios.request({
    url: 'title/update',
    data: params,
    method: 'post'
  })
}

// 上移下移标签
export const move = (params) => {
  return axios.request({
    url: 'title/move',
    data: params,
    method: 'post'
  })
}

// 删除标签
export const del = (params) => {
  return axios.request({
    url: 'title/delete',
    data: params,
    method: 'post'
  })
}
