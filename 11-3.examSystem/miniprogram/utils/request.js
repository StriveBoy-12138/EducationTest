const baseURL = 'http://49.234.31.118/examination-admin-api';

const request = (method, url, data) => {
  return new Promise(function (resolve, reject) {
    let token = wx.getStorageSync('token')
    let header = {
      'content-type': 'application/json',
    };
    if (token != '') {
      header['Authorization'] = token
    }
    wx.request({
      url: baseURL + url,
      method: method,
      data: method.toUpperCase === 'POST' ? JSON.stringify(data) : data,
      header: header,
      success: res => {
        if (res.statusCode === 200) {
          resolve(res)
        } else {
          reject(res.data.msg || res.data.message)
        }
      },
      fail: err => {
        reject(err.errMsg)
      }
    })
  })
}

module.exports = {
  request
}