import axios from '@/libs/api.request'

// 获取组织架构列表
export const list = (params) => {
  return axios.request({
    url: 'organization/all',
    params: params,
    method: 'get'
  })
}

// 获取邀请组织架构列表
export const inviteList = (params) => {
  return axios.request({
    url: 'organization/invite/list',
    params: params,
    method: 'get'
  })
}

// 添加组织架构
export const add = (params) => {
  return axios.request({
    url: 'organization/add',
    method: 'post',
    data: params
  })
}

// 修改组织架构
export const update = (params) => {
  return axios.request({
    url: 'organization/update',
    method: 'post',
    data: params
  })
}

// 删除组织架构
export const del = (params) => {
  return axios.request({
    url: 'organization/delete',
    method: 'post',
    data: params
  })
}
