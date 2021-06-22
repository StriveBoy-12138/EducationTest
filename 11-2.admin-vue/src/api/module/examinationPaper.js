import axios from '@/libs/api.request'

// 获取考生列表
export const list = (params) => {
  return axios.request({
    url: 'examination/paper/list',
    params: params,
    method: 'get'
  })
}

// 阅卷
export const mark = (params) => {
  return axios.request({
    url: 'examination/paper/mark',
    data: params,
    method: 'post'
  })
}

// 删除
export const del = (params) => {
  return axios.request({
    url: 'examination/paper/delete',
    data: params,
    method: 'post'
  })
}
