import axios from '@/libs/api.request'

export const getTableData = () => {
  return Promise.resolve({
    data: [
      {
        name: '小明',
        email: 'xiaoming@email.com',
        createTime: '2019-01-01'
      },
      {
        name: '小红',
        email: 'xiaohong@email.com',
        createTime: '2019-01-02'
      },
      {
        name: '小华',
        email: 'xiaohua@email.com',
        createTime: '2019-01-03'
      },
      {
        name: '小芳',
        email: 'xiaofang@email.com',
        createTime: '2019-01-04'
      }
    ]
  })
}

export const getDragList = () => {
  return Promise.resolve({
    data: [{ name: '任务1', id: 1 }, { name: '任务2', id: 2 }]
  })
}

export const errorReq = () => {
  return axios.request({
    url: 'error_url',
    method: 'post'
  })
}

export const saveErrorLogger = info => {
  return axios.request({
    url: 'save_error_logger',
    data: info,
    method: 'post'
  })
}

export const uploadImg = formData => {
  return Promise.resolve()
}

export const getOrgData = () => {
  return Promise.resolve({
    data: {
      id: 0,
      label: 'XXX科技有限公司',
      children: [
        {
          id: 2,
          label: '产品研发部',
          children: [
            {
              id: 5,
              label: '研发-前端'
            }, {
              id: 6,
              label: '研发-后端'
            }, {
              id: 9,
              label: 'UI设计'
            }, {
              id: 10,
              label: '产品经理'
            }
          ]
        },
        {
          id: 3,
          label: '销售部',
          children: [
            {
              id: 7,
              label: '销售一部'
            }, {
              id: 8,
              label: '销售二部'
            }
          ]
        },
        {
          id: 4,
          label: '财务部'
        }, {
          id: 11,
          label: 'HR人事'
        }
      ]
    }
  })
}

export const getTreeSelectData = () => {
  return axios.request({
    url: 'get_tree_select_data',
    method: 'get'
  })
}
