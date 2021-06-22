import axios from 'axios'
import store from '@/store'
import _i18n from '@/locale'
import { i18n, Modal } from 'view-design'
import router from '@/router'
import { getToken } from '@/libs/util'
// import { Spin } from 'iview'

// 解决 iview 组件 Cannot read property '$parent' of undefined 异常
i18n(function (path, options) {
  let value = _i18n.t(path, options)
  if (value !== null && value !== undefined) {
    return value
  }
  return ''
})

// const addErrorLog = errorInfo => {
//   const { statusText, status, request: { responseURL } } = errorInfo
//   let info = {
//     type: 'ajax',
//     code: status,
//     mes: statusText,
//     url: responseURL
//   }
//   if (!responseURL.includes('save_error_logger')) store.dispatch('addErrorLog', info)
// }

class HttpRequest {
  constructor (baseUrl = baseURL) {
    this.baseUrl = baseUrl
    this.queue = {}
  }
  getInsideConfig () {
    const config = {
      baseURL: this.baseUrl,
      withCredentials: true,
      timeout: 1000 * 30,
      headers: {
        // 默认将JS对象序列化为JSIN
      }
    }
    return config
  }
  destroy (url) {
    delete this.queue[url]
    if (!Object.keys(this.queue).length) {
      // Spin.hide()
    }
  }
  interceptors (instance, url) {
    // 请求拦截
    instance.interceptors.request.use(config => {
      config.headers['Authorization'] = getToken()
      // 添加全局的loading...
      if (!Object.keys(this.queue).length) {
        // Spin.show() // 不建议开启，因为界面不友好
      }
      this.queue[url] = true
      return config
    }, error => {
      return Promise.reject(error)
    })
    // 响应拦截
    instance.interceptors.response.use(res => {
      this.destroy(url)

      if (res.data && res.data.code === 302) {
        Modal.error({
          content: '登录失效，请重新登录',
          onOk: () => {
            store.dispatch('forceLogOut').then(res => {
              top.location.reload()
            })
          }
        })
        return
      }
      const { data, status } = res
      return { data, status }
    }, error => {
      this.destroy(url)
      let errorInfo = error.response
      if (!errorInfo) {
        const { request: { statusText, status }, config } = JSON.parse(JSON.stringify(error))
        errorInfo = {
          statusText,
          status,
          request: { responseURL: config.url }
        }
      }
      // addErrorLog(errorInfo)

      if (errorInfo.status === 404) {
        router.push({
          name: 'error_404'
        })
      } else if (errorInfo.status === 500) {
        router.push({
          name: 'error_500'
        })
      } else if (errorInfo.status === 302) {
        Modal.error({
          content: '登录失效，请重新登录',
          onOk: () => {
            store.dispatch('forceLogOut').then(res => {
              top.location.reload()
            })
          }
        })
      }

      return Promise.reject(error)
    })
  }
  request (options) {
    const instance = axios.create()
    options = Object.assign(this.getInsideConfig(), options)
    this.interceptors(instance, options.url)
    return instance(options)
  }
}
export default HttpRequest
