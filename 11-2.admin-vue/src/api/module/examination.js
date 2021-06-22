import axios from '@/libs/api.request'

// 获取考试设置
export const get = (params) => {
  return axios.request({
    url: 'examination/get',
    params: params,
    method: 'get'
  })
}

// 修改考试设置
export const update = (params) => {
  return axios.request({
    url: 'examination/update',
    data: params,
    method: 'post'
  })
}
