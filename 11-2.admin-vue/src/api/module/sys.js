import axios from '@/libs/api.request'

export const login = ({ account, password }) => {
  const data = {
    account,
    password
  }
  return axios.request({
    url: 'user/login',
    data: data,
    method: 'post'
  })
}

export const changePassWord = ({ account, password, newPassword }) => {
  const data = {
    account,
    password,
    newPassword
  }
  return axios.request({
    url: 'user/update/password',
    data: data,
    method: 'post'
  })
}

export const getUserInfo = () => {
  return axios.request({
    url: 'user/getCurrentInfo',
    method: 'get'
  })
}

export const logout = (token) => {
  return axios.request({
    url: 'user/logout',
    method: 'post'
  })
}

// 获取成员列表
export const list = (params) => {
  return axios.request({
    url: 'user/list',
    params: params,
    method: 'get'
  })
}

// 获取邀请成员列表
export const inviteList = (params) => {
  return axios.request({
    url: 'user/invite/list',
    params: params,
    method: 'get'
  })
}

// 获取成员信息
export const info = (id) => {
  return axios.request({
    url: 'user/info',
    params: id,
    method: 'get'
  })
}

// 重置密码
export const resetPassword = (params) => {
  return axios.request({
    url: 'user/reset/password',
    data: params,
    method: 'post'
  })
}

// 添加成员
export const add = (params) => {
  return axios.request({
    url: 'user/add',
    data: params,
    method: 'post'
  })
}

// 设置成员角色
export const setUserRole = (params) => {
  return axios.request({
    url: 'user/set/role',
    data: params,
    method: 'post'
  })
}

// 修改成员
export const update = (params) => {
  return axios.request({
    url: 'user/update',
    data: params,
    method: 'post'
  })
}

// 审核成员
export const verify = (params) => {
  return axios.request({
    url: 'user/verify',
    data: params,
    method: 'post'
  })
}

// 删除成员
export const del = (params) => {
  return axios.request({
    url: 'user/delete',
    data: params,
    method: 'post'
  })
}

// 获取oss上传token
// 0头像 1其他图片
export const ossToken = (type) => {
  let params = { type: type }
  return axios.request({
    url: 'token/aliOssToken',
    params: params,
    method: 'get'
  })
}
