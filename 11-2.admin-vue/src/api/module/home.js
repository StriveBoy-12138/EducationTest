import axios from '@/libs/api.request'

// 获取所有标签
export const statistics = (params) => {
  return axios.request({
    url: 'home/statistics',
    params: params,
    method: 'get'
  })
}
