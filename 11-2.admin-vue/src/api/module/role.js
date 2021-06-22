import axios from '@/libs/api.request'

// 获取角色列表
export const list = (params) => {
  return axios.request({
    url: 'sys/role/list',
    params: params,
    method: 'get'
  })
}

// 获取所有角色
export const all = (params) => {
  return axios.request({
    url: 'sys/role/all',
    params: params,
    method: 'get'
  })
}

// 获取角色列表, 根据当前用户
export const select = () => {
  return axios.request({
    url: 'sys/role/select',
    method: 'get'
  })
}

// 获取角色信息
export const info = (id) => {
  return axios.request({
    url: '/sys/role/info',
    method: 'get',
    params: { id }
  })
}

// 添加角色
export const add = (params) => {
  return axios.request({
    url: 'sys/role/save',
    method: 'post',
    data: params
  })
}

// 修改角色
export const update = (params) => {
  return axios.request({
    url: 'sys/role/update',
    method: 'post',
    data: params
  })
}

// 删除角色
export const del = (params) => {
  return axios.request({
    url: 'sys/role/delete',
    method: 'post',
    data: params
  })
}

// 获取菜单列表
export const listMenu = () => {
  return axios.request({
    url: 'sys/menu/list',
    method: 'get'
  })
}
